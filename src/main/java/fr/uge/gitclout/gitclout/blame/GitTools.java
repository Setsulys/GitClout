package fr.uge.gitclout.gitclout.blame;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
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
     * @param localPath local path where the git is stored
     * @param tmpDir dump file to check if the directory exist
     * @param repositoryURL url of the git repository
     * @throws IOException exception
     * @throws GitAPIException exception
     */
    public static void checkAndClone(String localPath,File tmpDir, String repositoryURL,Git git) throws IOException, GitAPIException {
        Objects.requireNonNull(localPath);
        Objects.requireNonNull(tmpDir);
        Objects.requireNonNull(repositoryURL);
        Objects.requireNonNull(git);
        if(Files.list(Paths.get(localPath)).findAny().isEmpty()) {
            if(!tmpDir.exists()) {
                cloneRepository(repositoryURL, localPath);
            }
        }
        System.out.println("START BLAMING");
    }

    /**
     * Clone remote repository in the localPath
     * @param repositoryURL url of the git repository
     * @param localPath local path where the git is stored
     * @throws GitAPIException exception
     */
    public static void cloneRepository(String repositoryURL, String localPath) throws GitAPIException {
        Objects.requireNonNull(repositoryURL);
        Objects.requireNonNull(localPath);
        CloneCommand cloneCommand = Git.cloneRepository()
                .setURI(repositoryURL)
                .setDirectory(new File(localPath));

        try (Git git = cloneCommand.call()) {
            System.out.println(" -----------------\n|Repository cloned|\n -----------------");
        }
    }

    /**
     * return the commit date of the tag
     * @param git the git on what we blame
     * @param ref is the tag on what we search
     * @return a long that is the commit time
     * @throws IncorrectObjectTypeException exception
     * @throws MissingObjectException exception
     * @throws IOException exception
     */
    public static long getCommitDate(Git git,Ref ref) throws IncorrectObjectTypeException, MissingObjectException, IOException {
        return git.getRepository().parseCommit(ref.getObjectId()).getCommitTime() * 1000L;
    }




    /**
     * Return list of Contributors data
     * @param git the git on what we blame
     * @return list of Contributors data
     */
    public static ArrayList<Contributor> authorCredentials(Git git) {
        var list = new HashSet<Contributor>();
        try {
            for(var commit : git.log().all().call()) {
                var authorIdent = commit.getAuthorIdent();
                list.add(new Contributor(authorIdent.getName(), authorIdent.getEmailAddress()));
            }
            return new ArrayList<>(list);
        }catch(Exception e) {
            throw new AssertionError();
        }
    }

    /**
     * return a string of the authors in the project
     * @param git the git on what we blaming
     * @return a string of the authors in the project
     */
    public static String getAuthorCredentials(Git git) {
        return authorCredentials(git).stream().map(String::valueOf).collect(Collectors.joining("\n"));
    }





















    /**
     * Get the tag before current one if exist counting the main head
     * @param allTag list of all tags, chronologicaly sorted
     * @param currentTagPosition is the current position of the tag in the allTag list
     * @return the last tag if there is tag before the actual one
     */
    private static Ref getLastRef(List<Ref> allTag,int currentTagPosition) {
        if(currentTagPosition!=0) {
            return allTag.get(currentTagPosition-1);
        }
        return allTag.get(currentTagPosition);
    }

    /**
     * return the commit from the ref
     * @param git the git on what we blame
     * @param ref is the tag on what we search
     * @return the commit from the ref
     * @throws MissingObjectException exception
     * @throws IncorrectObjectTypeException exception
     * @throws IOException exception
     */
    private static RevCommit getCommitByRef(Git git,Ref ref) throws MissingObjectException, IncorrectObjectTypeException, IOException {
        try(RevWalk revWalk = new RevWalk(git.getRepository())) {
            ObjectId refObjectId = ref.getObjectId();
            return revWalk.parseCommit(refObjectId);
        }
    }

    /**
     * Prepare to parse a tree to get the tree where we gave the commit
     * @param git the git on what we blame
     * @param commit the commit that we want to parse
     * @return a AbstractTreeIterator to get the tree where we gave the commit
     * @throws IOException exception
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
     * @param git the git on what we blame
     * @param oldCommit is the last commit of this git
     * @param newCommit is the current commit of this git
     * @return a list of changes between last tag and current one
     * @throws IOException exception
     * @throws GitAPIException exception
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
     * @param git the git on what we blame
     * @param allTag list of all tags, chronologicaly sorted
     * @param currentTagPosition is the current position of the tag in the allTag list
     * @return list of all files that have been modified, deleted or added
     * @throws IOException exception
     * @throws GitAPIException exception
     */
    public static List<String> checkModifiedFiles(Git git,List<Ref> allTag,int currentTagPosition) throws IOException, GitAPIException {
        RevCommit oldCommit = getCommitByRef(git,getLastRef(allTag,currentTagPosition));
        RevCommit newCommit = getCommitByRef(git,allTag.get(currentTagPosition));
        List<DiffEntry> diffEntries = getChangedFiles(git,oldCommit,newCommit);
        diffEntries.removeIf(i-> i.getPath(null).equals("/dev/null"));
        //diffEntries.removeIf(e-> e.getChangeType().equals(ChangeType.DELETE));
        var  diffList= diffEntries.stream().map(e -> e.getPath(null)).collect(Collectors.toList());
        //System.out.println("tag : "+ allTag.get(currentTagPosition).getName() +"\n "+diffList);
        return diffList;
    }

}
