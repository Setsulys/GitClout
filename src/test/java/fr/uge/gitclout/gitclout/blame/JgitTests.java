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
            assertEquals(Extensions.C,FileExtension.extensionDescription("c"));
            assertEquals(Extensions.C,FileExtension.extensionDescription("h"));
            assertEquals(Extensions.JAVA,FileExtension.extensionDescription("java"));
            assertEquals(Extensions.JAVASCRIPT,FileExtension.extensionDescription("js"));
            assertEquals(Extensions.HTML,FileExtension.extensionDescription("html"));
            assertEquals(Extensions.CSS,FileExtension.extensionDescription("css"));
            assertEquals(Extensions.PYTHON,FileExtension.extensionDescription("py"));
            assertEquals(Extensions.CPLUSPLUS,FileExtension.extensionDescription("c++"));
            assertEquals(Extensions.CPLUSPLUS,FileExtension.extensionDescription("cpp"));
            assertEquals(Extensions.PHP,FileExtension.extensionDescription("php"));
            assertEquals(Extensions.TYPESCRIPT,FileExtension.extensionDescription("ts"));
            assertEquals(Extensions.CSHARP,FileExtension.extensionDescription("cs"));
            assertEquals(Extensions.RUBY,FileExtension.extensionDescription("rb"));
            assertEquals(Extensions.MEDIA,FileExtension.extensionDescription("mp3"));
            assertEquals(Extensions.MEDIA,FileExtension.extensionDescription("mp4"));
            assertEquals(Extensions.MEDIA,FileExtension.extensionDescription("wav"));
            assertEquals(Extensions.MEDIA,FileExtension.extensionDescription("mkv"));
            assertEquals(Extensions.MEDIA,FileExtension.extensionDescription("jpg"));
            assertEquals(Extensions.MEDIA,FileExtension.extensionDescription("png"));
            assertEquals(Extensions.MEDIA,FileExtension.extensionDescription("jpeg"));
            assertEquals(Extensions.MEDIA,FileExtension.extensionDescription("webm"));
            assertEquals(Extensions.MEDIA,FileExtension.extensionDescription("jiff"));
            assertEquals(Extensions.MEDIA,FileExtension.extensionDescription("gif"));
            assertEquals(Extensions.MEDIA,FileExtension.extensionDescription("png"));
            assertEquals(Extensions.MEDIA,FileExtension.extensionDescription("xls"));
            assertEquals(Extensions.OTHER,FileExtension.extensionDescription("pdf"));
            assertEquals(Extensions.BUILD,FileExtension.extensionDescription("xml"));
            assertEquals(Extensions.BUILD,FileExtension.extensionDescription("yml"));
            assertEquals(Extensions.DOC,FileExtension.extensionDescription("md"));
            assertEquals(Extensions.RESSOURCES,FileExtension.extensionDescription("csv"));
            assertEquals(Extensions.RESSOURCES,FileExtension.extensionDescription("docx"));
            assertEquals(Extensions.RESSOURCES,FileExtension.extensionDescription("txt"));
            assertEquals(Extensions.CONFIGURATION,FileExtension.extensionDescription("git"));
            assertEquals(Extensions.CONFIGURATION,FileExtension.extensionDescription("project"));
            assertEquals(Extensions.CONFIGURATION,FileExtension.extensionDescription("gitignore"));
            assertEquals(Extensions.MAKEFILE,FileExtension.extensionDescription("makefile"));
            assertEquals(Extensions.OTHER,FileExtension.extensionDescription("pdf"));

        }

        @Test
        public void preconditions() {
            assertThrows(NullPointerException.class, ()-> new FileExtension("Test.java", null));
            assertThrows(NullPointerException.class, ()-> new FileExtension(null, "java"));
            assertThrows(NullPointerException.class, ()->FileExtension.extensionDescription(null));
        }
    }

}
