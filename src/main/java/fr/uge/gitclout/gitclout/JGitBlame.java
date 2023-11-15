package fr.uge.gitclout.gitclout;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;


public class JGitBlame {

    /**
     * Clone remote repository in the localPath
     * @param repositoryURL
     * @param localPath
     * @throws GitAPIException
     */
    public void cloneRepository(String repositoryURL, String localPath) throws GitAPIException {
        Objects.requireNonNull(repositoryURL);
        Objects.requireNonNull(localPath);
        CloneCommand cloneCommand = Git.cloneRepository()
                .setURI(repositoryURL)
                .setDirectory(new File(localPath));

        try (Git git = cloneCommand.call()) {
            System.out.println("Repository cloned");
        }
    }

    /**
     * GEt tags from remote
     * @param remote
     * @throws InvalidRemoteException
     * @throws TransportException
     * @throws GitAPIException
     */
    public void CheckRepository(String remote) throws GitAPIException {
        Objects.requireNonNull(remote);
        var pull = Git.lsRemoteRepository().setRemote(remote).setTags(true).call();
        System.out.println(pull);
    }

    /**
     * Get all the tags of the repository
     * @param git
     * @return return all tags and shawan
     * @throws GitAPIException
     */
    public List<Ref> getTag(Git git) throws GitAPIException{
        Objects.requireNonNull(git);
        return git.tagList().call();
    }

    /**
     * Get all the references of the repsository
     * @param git
     * @return Return all commits shawan
     * @throws GitAPIException
     */
    public List<Ref> getRefs(Git git) throws GitAPIException{
        Objects.requireNonNull(git);
        return git.branchList().setListMode(ListMode.ALL).call();
    }

    /**
     * Get the local repository
     * @param gitPath
     * @return a Repository of the curen gitPath
     * @throws IOException
     */
    public Repository getRepos(String gitPath) throws IOException {
        Objects.requireNonNull(gitPath);
        FileRepositoryBuilder repoBuilder = new FileRepositoryBuilder();
        return repoBuilder.setGitDir(new File(gitPath)).readEnvironment().findGitDir().build();
    }


    /**
     * Check if the folder is empty and if it already contains a git directory
     * Clone the given repository
     * @param localPath
     * @param tmpDir
     * @param repositoryURL
     * @throws IOException
     * @throws GitAPIException
     */
    public void checkAndClone(String localPath,File tmpDir, String repositoryURL) throws IOException, GitAPIException {
        Objects.requireNonNull(localPath);
        Objects.requireNonNull(tmpDir);
        Objects.requireNonNull(repositoryURL);
        if(!Files.list(Paths.get(localPath)).findAny().isPresent()) {
            if(!tmpDir.exists()) {
                cloneRepository(repositoryURL, localPath);
            }
        }
    }

    public static boolean isGitRepo(String url){
        Objects.requireNonNull(url);
        Pattern pattern = Pattern.compile("\\.git$");
        Matcher matcher = pattern.matcher(url);
        var match =matcher.find();
        if(!match){
            System.out.println("Ce n'est pas un repo git");
        }
        return match;
    }

    /**
     * Display the blame
     * @param blame
     */
    public void display(Blame blame) {
        Objects.requireNonNull(blame);
        System.out.println(blame.getFilesExtensions()+"\n");
        System.out.println(blame.getBlameByFileForEachContributor()+"\n");
        System.out.println(blame.getBlameByFile()+"\n");
        System.out.println(blame.getNbCommentsByFile() + "\n");
        System.out.println(blame.getNbCommentsForLanguage() + "\n");
        System.out.println(blame.getTotalNbComments());
        System.out.println(blame.getTotalBlame()+"\n");
    }

    public void run(String repositoryURL) {
        try {
            StringWork sW = new StringWork();
            if(!JGitBlame.isGitRepo(repositoryURL)){
                return;
            }
            //String repositoryURL = "https://gitlab.com/Setsulys/the_light_corridor.git";
            String localPath = sW.localPathFromURI(repositoryURL);
            String gitPath  = localPath + "/.git";
            File tmpDir  = new File(gitPath);
            //CheckRepository(repositoryURL);
            Repository repos =getRepos(gitPath);
            Git git = new Git(repos);

            checkAndClone(localPath, tmpDir, repositoryURL);
            var getTags = getTag(git);
            if(getTags.isEmpty()) {
                return;
            }
            @SuppressWarnings("resource")
            var tagtree = new RevWalk(git.getRepository()).parseCommit(getTags.getFirst().getObjectId()).getTree(); //take for the first tag
            var treewalk = new TreeWalk(git.getRepository()); //init the treewalk

            var blame = new Blame(git,treewalk,tagtree,getTags);
            blame.blame();
            display(blame);

        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    public static void main(String[] args) {
        var jgit = new JGitBlame();
        jgit.run("https://gitlab.com/Setsulys/the_light_corridor.git");
    }
}