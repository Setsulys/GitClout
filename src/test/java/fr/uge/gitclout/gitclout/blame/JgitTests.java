package fr.uge.gitclout.gitclout.blame;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class JgitTests {

public class BlameTest{
    private final Extensions extension = Extensions.JAVA;

    private final Git git;
    private final List<Ref> getTags;
    private final RevTree tagTree;
    private final TreeWalk treeWalk;
    private final Blame blame;
    private final List<String> diffs;

        @SuppressWarnings("resource")
        private BlameTest() throws IOException, GitAPIException {
            var repositoryURL = "https://gitlab.com/Setsulys/the_light_corridor.git";
            StringWork sW = new StringWork();
            String gitPath = sW.localPathFromURI(repositoryURL) + "/.git";
            JGitBlame jgit = new JGitBlame();
            Repository repos = jgit.getRepos(gitPath);
            git = new Git(repos);
            var pull = Git.lsRemoteRepository().setRemote(repositoryURL).setTags(true).call();
            var list = new ArrayList<>(new ArrayList<>(pull));
            list.add(git.fetch().call().getAdvertisedRefs().iterator().next());
            getTags = list;
            getTags.add(git.fetch().call().getAdvertisedRefs().iterator().next());;
            tagTree = new RevWalk(git.getRepository()).parseCommit(getTags.getFirst().getObjectId()).getTree();
            treeWalk = new TreeWalk(git.getRepository()); //init the treewalk
            diffs = GitTools.checkModifiedFiles(git, getTags, 0);
            blame = new Blame(git, treeWalk, tagTree, getTags,0,diffs);
        }

        @Test
        public void precondition() throws GitAPIException, IOException {
            assertThrows(NullPointerException.class, ()-> new Blame(null, treeWalk, tagTree, getTags,0,diffs));
            assertThrows(NullPointerException.class, ()-> new Blame(git, null, tagTree, getTags,0,diffs));
            assertThrows(NullPointerException.class, ()-> new Blame(git, treeWalk, null, getTags,0,diffs));
            assertThrows(NullPointerException.class, ()-> new Blame(git, treeWalk, tagTree, null,0,diffs));
            assertThrows(NullPointerException.class, ()-> new Blame(git, treeWalk, tagTree, getTags,0,null));

            assertThrows(NullPointerException.class, ()-> blame.checkCommentsInit(null,extension));

        }
    }

    @Nested
    public class StringWorkTest{
        private final StringWork sW = new StringWork();

        @Test
        public void checkLocalPathFromGitURL()  throws IOException{
            String repositoryURL = "https://gitlab.com/Contributor/Project";
            String repositoryURL2 = "https://gitlab.com/Contributor/Project2";
            Path path = Paths.get("");
            assertEquals(path.toAbsolutePath()+File.separator+"GitDataBase"+File.separator+"gitlab.com"+File.separator+"Contributor"+File.separator+"Project",sW.localPathFromURI(repositoryURL));
            assertNotEquals(path.toAbsolutePath()+File.separator+"GitDataBase"+File.separator+"gitlab.com"+File.separator+"Contributor"+File.separator+"Project",sW.localPathFromURI(repositoryURL2));
        }

        @Test
        public void precondition() {
            assertThrows(NullPointerException.class,()-> StringWork.splitExtention(null));
            assertThrows(NullPointerException.class,()-> sW.localPathFromURI(null));
        }
    }

    @Nested
    public class JGitBlameTest{
        private final JGitBlame jGit = new JGitBlame();


        @Test
        public void precondition() throws IOException {
            var gitPath = new StringWork().localPathFromURI("https://gitlab.com/Setsulys/the_light_corridor.git") + "/.git";
            var repos = new JGitBlame().getRepos(gitPath);
            var git = new Git(repos);
            var localPath = Paths.get("").toAbsolutePath().getParent()+File.separator+"Contributor"+File.separator+"Project";
            var repositoryURL = "https://gitlab.com/Contributor/Project";
            assertThrows(NullPointerException.class,()->GitTools.cloneRepository(null,localPath));
            assertThrows(NullPointerException.class,()->GitTools.cloneRepository(repositoryURL,null));

//			assertThrows(NullPointerException.class,()->jGit.CheckRepository(null));
//			assertThrows(NullPointerException.class,()->jGit.getRefs(null));
//			assertThrows(NullPointerException.class,()->jGit.getTag(null));
            assertThrows(NullPointerException.class,()->jGit.getRepos(null));

            assertThrows(NullPointerException.class,()->GitTools.checkAndClone(null, new File(localPath), repositoryURL,git));
            assertThrows(NullPointerException.class,()->GitTools.checkAndClone(localPath, null, repositoryURL,git));
            assertThrows(NullPointerException.class,()->GitTools.checkAndClone(localPath, new File(localPath), null,git));
            assertThrows(NullPointerException.class,()->GitTools.checkAndClone(localPath, new File(localPath), repositoryURL,null));
            //assertThrows(NullPointerException.class,()->jGit.displayBlame(null,git));

        }
    }

    @Nested
    public class FileExtensiontest{

        @Test
        public void recordTest() {
            var fe = new FileExtension("Test","java");
            assertEquals("Test", fe.file());
            assertEquals("java",fe.extension());
            assertEquals("Test & java",fe.toString());
        }

        @Test
        public void testAllExtensionsReturn() {
            assertEquals(Extensions.C,UtilsMethods.extensionDescription("c"));
            assertEquals(Extensions.C,UtilsMethods.extensionDescription("h"));
            assertEquals(Extensions.JAVA,UtilsMethods.extensionDescription("java"));
            assertEquals(Extensions.JAVASCRIPT,UtilsMethods.extensionDescription("js"));
            assertEquals(Extensions.HTML,UtilsMethods.extensionDescription("html"));
            assertEquals(Extensions.CSS,UtilsMethods.extensionDescription("css"));
            assertEquals(Extensions.PYTHON,UtilsMethods.extensionDescription("py"));
            assertEquals(Extensions.CPLUSPLUS,UtilsMethods.extensionDescription("c++"));
            assertEquals(Extensions.CPLUSPLUS,UtilsMethods.extensionDescription("cpp"));
            assertEquals(Extensions.PHP,UtilsMethods.extensionDescription("php"));
            assertEquals(Extensions.TYPESCRIPT,UtilsMethods.extensionDescription("ts"));
            assertEquals(Extensions.CSHARP,UtilsMethods.extensionDescription("cs"));
            assertEquals(Extensions.RUBY,UtilsMethods.extensionDescription("rb"));
            assertEquals(Extensions.MEDIA,UtilsMethods.extensionDescription("mp3"));
            assertEquals(Extensions.MEDIA,UtilsMethods.extensionDescription("mp4"));
            assertEquals(Extensions.MEDIA,UtilsMethods.extensionDescription("wav"));
            assertEquals(Extensions.MEDIA,UtilsMethods.extensionDescription("mkv"));
            assertEquals(Extensions.MEDIA,UtilsMethods.extensionDescription("jpg"));
            assertEquals(Extensions.MEDIA,UtilsMethods.extensionDescription("png"));
            assertEquals(Extensions.MEDIA,UtilsMethods.extensionDescription("jpeg"));
            assertEquals(Extensions.MEDIA,UtilsMethods.extensionDescription("webm"));
            assertEquals(Extensions.MEDIA,UtilsMethods.extensionDescription("jiff"));
            assertEquals(Extensions.MEDIA,UtilsMethods.extensionDescription("gif"));
            assertEquals(Extensions.MEDIA,UtilsMethods.extensionDescription("png"));
            assertEquals(Extensions.MEDIA,UtilsMethods.extensionDescription("xls"));
            assertEquals(Extensions.OTHER,UtilsMethods.extensionDescription("pdf"));
            assertEquals(Extensions.BUILD,UtilsMethods.extensionDescription("xml"));
            assertEquals(Extensions.BUILD,UtilsMethods.extensionDescription("yml"));
            assertEquals(Extensions.DOC,UtilsMethods.extensionDescription("md"));
            assertEquals(Extensions.RESSOURCES,UtilsMethods.extensionDescription("csv"));
            assertEquals(Extensions.RESSOURCES,UtilsMethods.extensionDescription("docx"));
            assertEquals(Extensions.RESSOURCES,UtilsMethods.extensionDescription("txt"));
            assertEquals(Extensions.CONFIGURATION,UtilsMethods.extensionDescription("git"));
            assertEquals(Extensions.CONFIGURATION,UtilsMethods.extensionDescription("project"));
            assertEquals(Extensions.CONFIGURATION,UtilsMethods.extensionDescription("gitignore"));
            assertEquals(Extensions.MAKEFILE,UtilsMethods.extensionDescription("makefile"));
            assertEquals(Extensions.OTHER,UtilsMethods.extensionDescription("pdf"));

        }

        @Test
        public void preconditions() {
            assertThrows(NullPointerException.class, ()-> new FileExtension("Test.java", null));
            assertThrows(NullPointerException.class, ()-> new FileExtension(null, "java"));
            assertThrows(NullPointerException.class, ()->UtilsMethods.extensionDescription(null));
        }
    }

}
