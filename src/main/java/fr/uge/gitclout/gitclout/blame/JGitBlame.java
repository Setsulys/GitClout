package fr.uge.gitclout.gitclout.blame;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;

public class JGitBlame {
    private final ArrayList<Ref> tagOfProject = new ArrayList<>();
    private final ArrayList<Blame> blameList = new ArrayList<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private int finishedTask =0;
    private double percentOfFinished=0;
    private final DecimalFormat df = new DecimalFormat("0.00");
    private final HashMap<Ref,java.sql.Date> tagDate = new HashMap<>();


    private String gito = null;

    /**
     * Get the date of all the refs
     * @param git the git on what we're blaming
     * @param allTag list of all tags, chronologicaly sorted
     * @throws IncorrectObjectTypeException exception
     * @throws MissingObjectException exception
     * @throws IOException exception
     */
    private void dateFromTag(Git git, List<Ref> allTag) throws IncorrectObjectTypeException, MissingObjectException, IOException {
        Objects.requireNonNull(git);
        Objects.requireNonNull(allTag);
        for(var ref : allTag) {
            java.util.Date commitDate = new java.util.Date(GitTools.getCommitDate(git, ref));
            java.sql.Date sqlDate = new java.sql.Date(commitDate.getTime());
            tagDate.put(ref, sqlDate);
        }
    }



    /**
     * GEt tags from remote
     * @param git the git on what we're blaming
     * @param repositoryURL url of the current repo
     * @throws GitAPIException exception
     */
    private void checkRepositoryTags(Git git,String repositoryURL) throws GitAPIException {
        Objects.requireNonNull(git);
        Objects.requireNonNull(repositoryURL);
        var pull = Git.lsRemoteRepository().setRemote(repositoryURL).setTags(true).call();
        var list = new ArrayList<>(pull.stream().sorted(Comparator.comparing(ref -> {
            try {
                return GitTools.getCommitDate(git,ref);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        })).collect(Collectors.toList()));
        tagOfProject.addAll(list);
    }



    /**
     * return all the tags presents in the project
     * @return all the tags presents in the project
     */
    public String getTagOfProjectString() {
        return tagOfProject.stream().map(Ref::getName).collect(Collectors.joining("\n","----------TAGS----------\n","\n------------------------\n"));
    }



    /**
     * Get the local repository
     * @param gitPath gitpath in local
     * @return a Repository of the current gitPath
     * @throws IOException exception
     */
    public Repository getRepos(String gitPath) throws IOException {
        Objects.requireNonNull(gitPath);
        FileRepositoryBuilder repoBuilder = new FileRepositoryBuilder();
        return repoBuilder.setGitDir(new File(gitPath)).readEnvironment().findGitDir().build();
    }


    /**
     * Display the information of the project (like tags of the project and contributors)
     * @param git git of the current project
     */
    private void displayInformations(Git git) {
        Objects.requireNonNull(git);
        System.out.println("\n\n"+GitTools.getAuthorCredentials(git));
        //System.out.println(getTagOfProjectString());
    }

    /**
     * This method prepare all things to execute the blame
     * @param git the git on what we blame
     * @param tmpDir converting gitpath in file to do verifications
     * @param repositoryURL url of the current repo
     * @param localPath local path of the current repo
     * @throws IOException exception
     * @throws GitAPIException exception
     */
    private void prepareTag(Git git,File tmpDir,String repositoryURL,String localPath) throws IOException, GitAPIException {
        GitTools.checkAndClone(localPath, tmpDir, repositoryURL,git);
        checkRepositoryTags(git, repositoryURL);
        dateFromTag(git,tagOfProject);
        displayInformations(git);
        checkEndedTask();
    }

    /**
     * Check how many task has ended until now
     */
    private void checkEndedTask() {
        scheduler.scheduleAtFixedRate(() -> {
            percentOfFinished = Double.valueOf(finishedTask)*100/Double.valueOf(tagOfProject.size());
            //percentOfFinished = finishedTask*100/tagOfProject.size();
            System.out.println("Task ended : " + df.format(percentOfFinished) +"%" + " == "+finishedTask+"/"+tagOfProject.size());
        }, 1, 2, TimeUnit.SECONDS);
    }

    /**
     *
     * @param git current git
     * @param allTag list of all tags of this project
     * @param actualTag position of the current tag in the list
     * @throws MissingObjectException exception
     * @throws IncorrectObjectTypeException exception
     * @throws IOException exception
     * @throws GitAPIException exception
     */
    private void runByTag(Git git,List<Ref> allTag, int actualTag) throws MissingObjectException, IncorrectObjectTypeException, IOException, GitAPIException{
        Objects.requireNonNull(git);
        Objects.requireNonNull(allTag);
        if(actualTag <0) {
            throw new IllegalArgumentException();
        }
        try {
            @SuppressWarnings("resource")
            var tagTree = new RevWalk(git.getRepository()).parseCommit(allTag.get(actualTag).getObjectId()).getTree(); //take for the first tag
            var treewalk = new TreeWalk(git.getRepository()); //init the treewalk
            var filesChange = GitTools.checkModifiedFiles(git, tagOfProject, actualTag);
            var blame = new Blame(git,treewalk,tagTree,allTag,actualTag, filesChange);
            blameList.add(blame);
            blame.blaming();
        }catch(Exception e ) {
            throw new AssertionError("run by tag",e);
        }
    }

    /**
     *  main loop on the tags
     * @param git current git
     * @throws MissingObjectException exception
     * @throws IncorrectObjectTypeException exception
     * @throws IOException exception
     * @throws GitAPIException exception
     */
    private void executeForEveryTag(Git git) throws MissingObjectException, IncorrectObjectTypeException, IOException, GitAPIException {
        for(var i =0; i < tagOfProject.size();i++) {
            System.out.println("start --------------------------" + tagOfProject.get(i).getName());
            runByTag(git,tagOfProject,i);
            finishedTask++;
        }
        scheduler.shutdown();
    }

    /**
     * The method used for using this class
     * @param repositoryURL link of the repo
     */
    public void run(String repositoryURL) {
        try {
            System.out.println("-----Pulling or cloning-----");
            String localPath = new StringWork().localPathFromURI(repositoryURL);
            String gitPath  = localPath + "/.git";
            File tmpDir  = new File(gitPath);
            Repository repos =getRepos(gitPath);
            Git git = new Git(repos);

            gito = repositoryURL.substring(8,repositoryURL.length()-4);
            prepareTag(git,tmpDir,repositoryURL,localPath);
            executeForEveryTag(git);

        } catch (Exception e) {
            throw new AssertionError("Problem in run",e);
        }
        System.out.println("Task ended :100.00%" + " == "+finishedTask+"/"+tagOfProject.size());
        System.out.println("work done");
        //System.out.println(blameList.stream().map(e-> e.blameDatas().toString()).collect(Collectors.joining("\n")));
        //System.out.println("-------------------------------------------------------");
        //System.out.println(blameList.stream().map(Blame::DataString).collect(Collectors.joining("\n")));

    }

    /**
     * Return the blame result of the actual project
     * @return blame result of the project
     */
    public ArrayList<Blame> projectData(){
        return blameList;
    }


    public String getGit(){
        return gito;
    }

    public HashMap<Ref,java.sql.Date> getDateMap(){
        return tagDate;
    }




    public double PercentOfFinishedTask(){
        return percentOfFinished;
    }

    public static void main(String[] args) throws IOException {
        var jgit = new JGitBlame();
        jgit.run("https://gitlab.com/Setsulys/the_light_corridor.git");
        //jgit.run("https://github.com/openjdk/jdk.git");
        //jgit.run("https://gitlab.ow2.org/asm/asm.git");
    }
}