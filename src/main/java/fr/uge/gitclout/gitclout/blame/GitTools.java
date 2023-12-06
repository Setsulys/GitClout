package fr.uge.gitclout.gitclout.blame;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;


//Add a bool to check if we need to made change in the data base
public class GitTools {
    /**
     * Check if the folder is empty and if it already contains a git directory
     * Clone the given repository
     * @param localPath
     * @param tmpDir
     * @param repositoryURL
     * @throws IOException
     * @throws GitAPIException
     */
    public static void checkAndClone(String localPath,File tmpDir, String repositoryURL,Git git) throws IOException, GitAPIException {
        UtilsMethods.checkNonNull(localPath,tmpDir,repositoryURL,git);
        if(!Files.list(Paths.get(localPath)).findAny().isPresent()) {
            if(!tmpDir.exists()) {
                cloneRepository(repositoryURL, localPath);
            }
        }else {
            pullRepository(git, repositoryURL, localPath);
        }
    }
    /**
     * Clone remote repository in the localPath
     * @param repositoryURL
     * @param localPath
     * @throws GitAPIException
     */
    public static void cloneRepository(String repositoryURL, String localPath) throws GitAPIException {
        UtilsMethods.checkNonNull(repositoryURL,localPath);
        CloneCommand cloneCommand = Git.cloneRepository()
                .setURI(repositoryURL)
                .setDirectory(new File(localPath));

        try (Git git = cloneCommand.call()) {
            System.out.println(" -----------------\n|Repository cloned|\n -----------------");
        }
    }

    /**
     * If the repository already exist, try to pull new information to analyse
     * @param git
     * @param repositoryURL
     * @param localPath
     * @throws GitAPIException
     * @throws IOException
     */
    public static void pullRepository(Git git,String repositoryURL,String localPath) throws GitAPIException, IOException{
        Objects.requireNonNull(repositoryURL);
        Objects.requireNonNull(localPath);
        var localHead = git.getRepository().resolve("HEAD");
        var remoteHead = git.fetch().call().getAdvertisedRefs().iterator().next().getObjectId();
        if(!localHead.equals(remoteHead)) {
            git.pull().call();
            System.out.println(" ------------\n|pull réussis|\n ------------");
        }
        else {
            System.out.println(" -----------------\n|Repos déja à jour|\n -----------------");
        }
    }





    public static long getCommitDate(Git git,Ref ref) throws IncorrectObjectTypeException, MissingObjectException, IOException {
        return git.getRepository().parseCommit(ref.getObjectId()).getCommitTime() * 1000L;
    }




    /**
     * Return list of Contributors data
     * @param git
     * @return list of Contributors data
     */
    public static ArrayList<Contributor> authorCredentials(Git git) {
        var list = new ArrayList<Contributor>();
        try {
            for(var commit : git.log().all().call()) {
                var authorIdent = commit.getAuthorIdent();
                if(!list.contains(new Contributor(authorIdent.getName(), authorIdent.getEmailAddress()))) {
                    list.add(new Contributor(authorIdent.getName(), authorIdent.getEmailAddress()));
                }
            }
            return list;
        }catch(Exception e) {
            throw new AssertionError();
        }
    }


    public static String getAuthorCredentials(Git git) {
        return authorCredentials(git).stream().map(String::valueOf).collect(Collectors.joining("\n"));
    }





















    /**
     * Get the tag before current one if exist counting the main head
     * @return last tag of the project
     */
    private static Ref getLastRef(List<Ref> allTag,int currentTagPosition) {
        if(currentTagPosition!=0) {
            return allTag.get(currentTagPosition-1);
        }
        return allTag.get(currentTagPosition);
    }

    /**
     *
     * @param ref
     * @return
     * @throws MissingObjectException
     * @throws IncorrectObjectTypeException
     * @throws IOException
     */
    private static RevCommit getCommitByRef(Git git,Ref ref) throws MissingObjectException, IncorrectObjectTypeException, IOException {
        try(RevWalk revWalk = new RevWalk(git.getRepository())) {
            ObjectId refObjectId = ref.getObjectId();
            return revWalk.parseCommit(refObjectId);
        }
    }

    /**
     * Prepare to parse a tree to get the tree where we gave the commit
     * @param commit
     * @return an iterator of the tree at the point of the commit
     * @throws IOException
     */
    private static AbstractTreeIterator prepareTreeParser(Git git,RevCommit commit) throws IOException {
        try (ObjectReader reader = git.getRepository().newObjectReader()) {
            CanonicalTreeParser treeParser = new CanonicalTreeParser();
            treeParser.reset(reader, commit.getTree());
            return treeParser;
        }
    }

    /**
     * return a list of changes between last tag and current one
     * @param oldCommit
     * @param newCommit
     * @return a list of changes between last tag and current one
     * @throws IOException
     * @throws GitAPIException
     */
    private static List<DiffEntry> getChangedFiles(Git git,RevCommit oldCommit, RevCommit newCommit) throws IOException, GitAPIException{
        try (RevWalk revWalk = new RevWalk(git.getRepository())) {
            AbstractTreeIterator oldTree = prepareTreeParser(git,oldCommit);
            AbstractTreeIterator newTree = prepareTreeParser(git,newCommit);
            return git.diff().setOldTree(oldTree).setNewTree(newTree).call();
        }
    }

    /**
     * Check the modified files between actual and last tag
     * @throws IOException
     * @throws GitAPIException
     */
    public static List<String> checkModifiedFiles(Git git,List<Ref> allTag,int currentTagPosition) throws IOException, GitAPIException {
        RevCommit oldCommit = getCommitByRef(git,getLastRef(allTag,currentTagPosition));
        RevCommit newCommit = getCommitByRef(git,allTag.get(currentTagPosition));
        List<DiffEntry> diffEntries = getChangedFiles(git,oldCommit,newCommit);
        diffEntries.removeIf(i-> i.getPath(null).toString().equals("/dev/null"));
        //diffEntries.removeIf(e-> e.getChangeType().equals(ChangeType.DELETE));
        var  diffList= diffEntries.stream().map(e -> e.getPath(null).toString()).collect(Collectors.toList());
        //System.out.println("tag : "+ allTag.get(currentTagPosition).getName() +"\n "+diffList);
        return diffList;
    }

}
