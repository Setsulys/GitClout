package fr.uge.gitclout.gitclout.blame;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
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
    private final Object lock = new Object();


    /**
     * Get the date of
     * @throws IncorrectObjectTypeException
     * @throws MissingObjectException
     * @throws IOException
     */
    private void dateFromTag(Git git, List<Ref> allTag) throws IncorrectObjectTypeException, MissingObjectException, IOException {
        for(var ref : allTag) {
            java.util.Date commitDate = new java.util.Date(GitTools.getCommitDate(git, ref));
            java.sql.Date sqlDate = new java.sql.Date(commitDate.getTime());
            tagDate.put(ref, sqlDate);
        }
    }
    /**
     * GEt tags from remote
     * @param remote
     * @throws InvalidRemoteException
     * @throws TransportException
     * @throws GitAPIException
     */
    public void checkRepositoryTags(Git git,String repositoryURL) throws GitAPIException {
        UtilsMethods.checkNonNull(repositoryURL);
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
        UtilsMethods.checkNonNull(gitPath);
        FileRepositoryBuilder repoBuilder = new FileRepositoryBuilder();
        return repoBuilder.setGitDir(new File(gitPath)).readEnvironment().findGitDir().build();
    }

    /**
     * Display the blame
     * @param blame
     * @throws IOException
     * @throws GitAPIException
     */
    public void displayBlame(Blame blame,Git git) throws IOException, GitAPIException {
        UtilsMethods.checkNonNull(blame);
        synchronized(lock) {
            System.out.println("\n\n---------------------------------------------------------------\n---------------------------------------------------------------");

            System.out.println(blame.currentRef().getName()+" Date : " + tagDate.get(blame.currentRef()));
            System.out.println(blame.getFilesExtensions()+"\n");
            System.out.println("\n\n\n------Blame Data------\n");
            System.out.println(blame.divideIntoDataString());
            System.out.println("\n\n\n------Comments lines-----\n");
            System.out.println(blame.divideIntoDataStringGetCommentByFile());
            System.out.println("\n\n\n------Code lines-----\n");
            System.out.println(blame.divideIntoDataStringGetLinesByFile());
            System.out.println("\n\n\n------Total-----\n");
            System.out.println(blame.totalLines()+"\n");
            //System.out.println("Tracked files : ");
            //GitTools.checkModifiedFiles(git, tagOfProject, blame.currentRefPosition());
        }
    }

    public void displayInformations(Git git) {
        UtilsMethods.checkNonNull(git);
        System.out.println("\n\n"+GitTools.getAuthorCredentials(git));
        System.out.println(getTagOfProjectString());
    }



    public void working(Blame blame,Git git) throws IOException, GitAPIException {
        synchronized (lock) {
            blame.blaming();
            displayBlame(blame,git);
        }
    }


    public void inerRun(Git git,List<Ref> allTag, int actualTag) throws MissingObjectException, IncorrectObjectTypeException, IOException, GitAPIException{
        UtilsMethods.checkNonNull(git,allTag);
        try {
            ExecutorService executor = Executors.newFixedThreadPool(16);
            executor.submit(()->{
                try {
                    @SuppressWarnings("resource")
                    var tagtree = new RevWalk(git.getRepository()).parseCommit(allTag.get(actualTag).getObjectId()).getTree(); //take for the first tag
                    var treewalk = new TreeWalk(git.getRepository()); //init the treewalk
                    var filesChange = GitTools.checkModifiedFiles(git, tagOfProject, actualTag);
                    var blame = new Blame(git,treewalk,tagtree,allTag,actualTag, filesChange);
                    working(blame,git);
                }catch(IOException e) {
                    throw new AssertionError(e);
                } catch (GitAPIException gae) {
                    throw new AssertionError(gae);
                }
            });
            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        }catch(Exception e ) {
            throw new AssertionError(e);
        }
    }


    public void run(String repositoryURL) {
        try {
            if(!UtilsMethods.isGitRepo(repositoryURL)){
                return;
            }
            var threads = new ArrayList<Thread>();
            String localPath = new StringWork().localPathFromURI(repositoryURL);
            String gitPath  = localPath + "/.git";
            File tmpDir  = new File(gitPath);
            Repository repos =getRepos(gitPath);
            Git git = new Git(repos);
            GitTools.checkAndClone(localPath, tmpDir, repositoryURL,git);
            displayInformations(git);
            checkRepositoryTags(git, repositoryURL);
            dateFromTag(git, tagOfProject);
            for(var i =0; i < tagOfProject.size();i++) {
                var j=i;
                threads.add(Thread.ofPlatform().start(()->{
                    try {
                        inerRun(git,tagOfProject,j);
                    } catch (IOException | GitAPIException e) {
                        throw new AssertionError(e);
                    }
                }));
            }
            for(var thread : threads) {
                thread.join();
            }
//			while(!allTags.isEmpty()) {
//				inerRun(git, allTags);
//			}
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }


    public static void main(String[] args) throws IOException {
        var jgit = new JGitBlame();
        jgit.run("https://gitlab.com/Setsulys/the_light_corridor.git");
        //jgit.run("https://github.com/openjdk/jdk.git");
        //jgit.run("https://gitlab.ow2.org/asm/asm.git");
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