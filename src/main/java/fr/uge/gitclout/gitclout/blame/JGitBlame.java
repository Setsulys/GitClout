package fr.uge.gitclout.gitclout.blame;

import java.io.File;
import java.io.IOException;
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
    private final HashMap<Ref,java.sql.Date> tagDate = new HashMap<>();
    private final ArrayList<Blame> blameList = new ArrayList<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private int finishedTask =0;
    private double percentOfFinished=0;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    /**
     * Get the date of all the refs
     * @param git the git on what we blaming
     * @param allTag list of all tags, chronologicaly sorted
     * @throws IncorrectObjectTypeException
     * @throws MissingObjectException
     * @throws IOException
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
     * @param git the git on what we blaming
     * @param repositoryURL
     * @throws GitAPIException
     */
    public void checkRepositoryTags(Git git,String repositoryURL) throws GitAPIException {
        Objects.requireNonNull(git);
        Objects.requireNonNull(repositoryURL);
        var pull = Git.lsRemoteRepository().setRemote(repositoryURL).setTags(true).call();
        var list = new ArrayList<>(pull.stream().sorted(Comparator.comparing(ref -> {
            try {
                return GitTools.getCommitDate(git,ref);
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        })).map(e -> e).collect(Collectors.toList()));
        //list.add(git.fetch().call().getAdvertisedRefs().iterator().next());  //For adding final project
        tagOfProject.addAll(list);
    }




    public String getTagOfProjectString() {
        return tagOfProject.stream().map(String::valueOf).collect(Collectors.joining("","","\n"));
    }



    /**
     * Get the local repository
     * @param gitPath
     * @return a Repository of the current gitPath
     * @throws IOException
     */
    public Repository getRepos(String gitPath) throws IOException {
        Objects.requireNonNull(gitPath);
        FileRepositoryBuilder repoBuilder = new FileRepositoryBuilder();
        return repoBuilder.setGitDir(new File(gitPath)).readEnvironment().findGitDir().build();
    }



    public void displayInformations(Git git) {
        Objects.requireNonNull(git);
        System.out.println("\n\n"+GitTools.getAuthorCredentials(git));
        System.out.println(getTagOfProjectString());
    }

    /**
     * Check how many task has ended until now
     */
    public void checkEndedTask() {
        scheduler.scheduleAtFixedRate(() -> {
            percentOfFinished = Double.valueOf(finishedTask)*100/Double.valueOf(tagOfProject.size());
            System.out.println("Task ended : " + df.format(percentOfFinished) +"%" + " == "+finishedTask+"/"+tagOfProject.size());
        }, 1, 2, TimeUnit.SECONDS);
    }

    public void subRun(Git git,List<Ref> allTag, int actualTag) throws MissingObjectException, IncorrectObjectTypeException, IOException, GitAPIException{
        Objects.requireNonNull(git);
        Objects.requireNonNull(allTag);
        if(actualTag <0) {
            throw new IllegalArgumentException();
        }
        try {
            @SuppressWarnings("resource")
            var tagtree = new RevWalk(git.getRepository()).parseCommit(allTag.get(actualTag).getObjectId()).getTree(); //take for the first tag
            var treewalk = new TreeWalk(git.getRepository()); //init the treewalk
            var filesChange = GitTools.checkModifiedFiles(git, tagOfProject, actualTag);
            var blame = new Blame(git,treewalk,tagtree,allTag,actualTag, filesChange);
            blameList.add(blame);
            blame.blaming();
        }catch(Exception e ) {
            throw new AssertionError(e);
        }
    }

    public void run(String repositoryURL) {
        try {
            String localPath = new StringWork().localPathFromURI(repositoryURL);
            String gitPath  = localPath + "/.git";
            File tmpDir  = new File(gitPath);
            Repository repos =getRepos(gitPath);
            Git git = new Git(repos);
            GitTools.checkAndClone(localPath, tmpDir, repositoryURL,git);
            displayInformations(git);
            checkRepositoryTags(git, repositoryURL);
            dateFromTag(git, tagOfProject);

            checkEndedTask();

            var executors = Executors.newFixedThreadPool(3*Runtime.getRuntime().availableProcessors()/4);
            var task = new ArrayList<Future>();
            for(var i =0; i < tagOfProject.size();i++) {
                var j= i;
                Future future = executors.submit(()->{

                    try {
                        System.out.println("start --------------------------" + tagOfProject.get(j).getName());
                        subRun(git,tagOfProject,j);
                        finishedTask++;
                    } catch (IOException | GitAPIException e) {
                        throw new AssertionError(e);
                    }
                });
                task.add(future);
            }
            for(var future : task) {
                future.get();
            }
            executors.shutdown();
            scheduler.shutdown();
        } catch (Exception e) {
            throw new AssertionError(e);
        }
        System.out.println("Task ended :100.00%" + " == "+finishedTask+"/"+tagOfProject.size());
        System.out.println("work done");
        //System.out.println(blameList.stream().map(e-> e.blameDatas().stream().filter(f-> f.file().equals("src/lights.c")).map(f-> f.nameAndNumbers()).collect(Collectors.toList()).toString()).collect(Collectors.joining("\n")));
        //System.out.println(blameList.stream().map(e-> e.blameDatas().stream().map(i-> i.tag()).collect(Collectors.toList()).toString()).collect(Collectors.joining("\n")));
    }

    public ArrayList<Blame> projectData(){
        return blameList;
    }


    public static void main(String[] args) throws IOException {
        var jgit = new JGitBlame();
        jgit.run("https://gitlab.com/Setsulys/the_light_corridor.git");
        //jgit.run("https://github.com/openjdk/jdk.git");
        //jgit.run("https://gitlab.ow2.org/asm/asm.git");
        //jgit.projectData();
    }

    //	/**
    //	 * Get all the tags of the repository
    //	 * @param git
    //	 * @return return all tags and shawan
    //	 * @throws GitAPIException
    //	 */
    //	public List<Ref> getTag(Git git) throws GitAPIException{
    //		Objects.requireNonNull(git);
    //		return git.tagList().call();
    //	}

    //	/**
    //	 * Get all the references of the repsository
    //	 * @param git
    //	 * @return Return all commits shawan
    //	 * @throws GitAPIException
    //	 */
    //	public List<Ref> getRefs(Git git) throws GitAPIException{
    //		Objects.requireNonNull(git);
    //		return git.branchList().setListMode(ListMode.ALL).call();
    //	}
}