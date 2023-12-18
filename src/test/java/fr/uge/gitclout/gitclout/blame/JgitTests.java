package fr.uge.gitclout.gitclout.blame;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class JgitTests {



    @Nested
    public class StringWorkTest{
        private StringWork sW = new StringWork();

        @Test
        public void checkLocalPathFromGitURL()  throws IOException{
            String repositoryURL = "https://gitlab.com/Contributor/Project";
            String repositoryURL2 = "https://gitlab.com/Contributor/Project2";
            assertEquals(Paths.get("").toAbsolutePath()+File.separator+"GitDataBase"+File.separator+"Contributor"+File.separator+"Project",sW.localPathFromURI(repositoryURL));
            assertNotEquals(Paths.get("").toAbsolutePath()+File.separator+"GitDataBase"+File.separator+"Contributor"+File.separator+"Project",sW.localPathFromURI(repositoryURL2));
        }

        @Test
        public void precondition() {
            assertThrows(NullPointerException.class,()-> sW.splitExtention(null));
            assertThrows(NullPointerException.class,()-> sW.localPathFromURI(null));
        }
    }

    @Nested
    public class JGitBlameTest{
        private JGitBlame jGit = new JGitBlame();


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
            assertThrows(NullPointerException.class,()->jGit.displayBlame(null,git));
            assertThrows(NullPointerException.class,()->jGit.displayInformations(null));

        }
    }

    @Nested
    public class FileExtensiontest{

        @Test
        public void recordTest() {
            var fe = new FileExtension("Test","java");
            assertEquals("Test", fe.file());
            assertEquals("java",fe.extension());
            assertEquals("File : Test.java\n=>Test & java",fe.toString());
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
            assertEquals(Extensions.CSHARP,FileExtension.extensionDescription("cs"));
            assertEquals(Extensions.RUBY,FileExtension.extensionDescription("rb"));
            assertEquals(Extensions.OTHER,FileExtension.extensionDescription("jpg"));
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
