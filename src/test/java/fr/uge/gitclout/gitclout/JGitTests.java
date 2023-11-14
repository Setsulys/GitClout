package fr.uge.gitclout.gitclout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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

public class JGitTests {

    @Nested
    public class BlameTest{
        private final JGitBlame jgit = new JGitBlame();
        private final StringWork sW = new StringWork();
        private final Extensions extension = Extensions.JAVA;

        private final String gitPath;
        private final Repository repos;
        private final Git git;
        private final List<Ref> getTags;
        private final RevTree tagTree;
        private final TreeWalk treeWalk;
        private final Blame blame;

        @SuppressWarnings("resource")
        private BlameTest() throws IOException, GitAPIException {
            gitPath = sW.localPathFromURI("https://gitlab.com/Setsulys/the_light_corridor.git") + "/.git";
            repos = jgit.getRepos(gitPath);
            git = new Git(repos);
            getTags =jgit.getTag(git);
            tagTree = new RevWalk(git.getRepository()).parseCommit(getTags.getFirst().getObjectId()).getTree();
            treeWalk = new TreeWalk(git.getRepository()); //init the treewalk
            blame = new Blame(git, treeWalk, tagTree, getTags);
        }

        @Test
        public void check() throws MissingObjectException, IncorrectObjectTypeException, CorruptObjectException, IOException, GitAPIException{
            var strings = new ArrayList<String>();
            strings.add("//TEST TRUE 0");
            strings.add("/* TEST TRUE 1*/");
            strings.add("/** TEST TRUE 2**/");
            strings.add("/* * TEST TRUE 3* */");
            strings.add("/** TEST TRUE 4*/ System.out.println(\"aaaa\");"); //valide quand on a pas le \"aaaa\"
            //strings.add("/** TEST 5 *//** System.out.print(\"Faux positif\"); */");
            blame.checkComments(extension, strings, "Tests");
            assertEquals(strings.size()-1,blame.nbCommentsForFileMap().get("Tests"));

        }

        @Test
        public void checkWithFalse() {
            var strings = new ArrayList<String>();
            strings.add("System.out.println(\"/* TEST FALSE 0\");");
            strings.add("System.out.println(\"// TEST FALSE 1\");");
            strings.add("System.out.println(\" // TEST FALSE 2\");");
            strings.add("System.out.println(\"/* TEST FALSE 3\");");
            strings.add("System.out.println(\"/* TEST FALSE 4 */\");");
            strings.add("/ / TEST FALSE 5");
            strings.add("Sytem.out.println(\" \" /* TEST FALSE 6 \"*/ + \" \");");
            blame.checkComments(extension, strings, "Tests");
            assertEquals(0,blame.nbCommentsForFileMap().get("Tests"));
        }

        @Test
        public void checkCodeAndCommentsTogether() {
            var strings = new ArrayList<String>();
            strings.add("System.out.println(\"TEST 0 \"); /* TEST 0 */");
            strings.add("System.out.println(\"/* TEST FALSE 5 */\"); /* TEST FALSE 5 */");
            strings.add("/** TEST 11 */ System.out.println(\"TEST 11\");");
            blame.checkComments(extension, strings, "Tests");
            assertEquals(0,blame.nbCommentsForFileMap().get("Tests"));
        }

        @Test
        public void checkListPrecondition() {
            var strings = new ArrayList<String>();
            strings.add(null);
            assertThrows(NullPointerException.class,()-> blame.checkComments(extension, strings, "Tests"));
        }

        @Test
        public void precondition() {
            assertThrows(NullPointerException.class, ()-> new Blame(null, treeWalk, tagTree, getTags));
            assertThrows(NullPointerException.class, ()-> new Blame(git, null, tagTree, getTags));
            assertThrows(NullPointerException.class, ()-> new Blame(git, treeWalk, null, getTags));
            assertThrows(NullPointerException.class, ()-> new Blame(git, treeWalk, tagTree, null));
            assertThrows(NullPointerException.class, ()-> Blame.checkNonNull(null,null));
            assertThrows(NullPointerException.class, ()-> blame.checkComments(extension,null,"file"));
            assertThrows(NullPointerException.class, ()-> blame.checkComments(null,new ArrayList<String>(),"file"));
            assertThrows(NullPointerException.class, ()-> blame.checkComments(extension,new ArrayList<String>(),null));

        }
    }

    @Nested
    public class StringWorkTest{
        private StringWork sW = new StringWork();

        @Test
        public void checkLocalPathFromGitURL()  throws IOException{
            String repositoryURL = "https://gitlab.com/Contributor/Project";
            String repositoryURL2 = "https://gitlab.com/Contributor/Project2";
            assertEquals(Paths.get("").toAbsolutePath()+ File.separator + "GitDataBase" + File.separator + "Contributor" + File.separator + "Project",sW.localPathFromURI(repositoryURL));
            assertNotEquals(Paths.get("").toAbsolutePath()+ File.separator + "GitDataBase" + File.separator + "Contributor" + File.separator + "Project",sW.localPathFromURI(repositoryURL2));
        }

        @Test
        public void precondition() {
            assertThrows(NullPointerException.class,()-> sW.splitExtention(null));
            assertThrows(NullPointerException.class,()-> sW.localPathFromURI(null));
        }
    }

    @Nested
    public class JGitBlameTest{

        @Test
        public void precondition() {
            var jGit = new JGitBlame();
            var localPath = Paths.get("").toAbsolutePath().getParent()+ File.separator + "Contributor"+ File.separator +"Project";
            var repositoryURL = "https://gitlab.com/Contributor/Project";
            assertThrows(NullPointerException.class,()->jGit.cloneRepository(null,localPath));
            assertThrows(NullPointerException.class,()->jGit.cloneRepository(repositoryURL,null));

            assertThrows(NullPointerException.class,()->jGit.CheckRepository(null));
            assertThrows(NullPointerException.class,()->jGit.getRefs(null));
            assertThrows(NullPointerException.class,()->jGit.getTag(null));
            assertThrows(NullPointerException.class,()->jGit.getRepos(null));

            assertThrows(NullPointerException.class,()->jGit.checkAndClone(null, new File(localPath), repositoryURL));
            assertThrows(NullPointerException.class,()->jGit.checkAndClone(localPath, null, repositoryURL));
            assertThrows(NullPointerException.class,()->jGit.checkAndClone(localPath, new File(localPath), null));
            assertThrows(NullPointerException.class,()->jGit.display(null));
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
            assertEquals(Extensions.HEADER,FileExtension.extensionDescription("h"));
            assertEquals(Extensions.JAVA,FileExtension.extensionDescription("java"));
            assertEquals(Extensions.JAVASCRIPT,FileExtension.extensionDescription("js"));
            assertEquals(Extensions.HTML,FileExtension.extensionDescription("html"));
            assertEquals(Extensions.CSS,FileExtension.extensionDescription("css"));
            assertEquals(Extensions.PYTHON,FileExtension.extensionDescription("py"));
            assertEquals(Extensions.CPLUSPLUS,FileExtension.extensionDescription("c++"));
            assertEquals(Extensions.CPLUSPLUS,FileExtension.extensionDescription("cpp"));
            assertEquals(Extensions.PHP,FileExtension.extensionDescription("php"));
            assertEquals(Extensions.TYPESCRPIPT,FileExtension.extensionDescription("ts"));
            assertEquals(Extensions.OCAML,FileExtension.extensionDescription("ml"));
            assertEquals(Extensions.HASKELL,FileExtension.extensionDescription("hs"));
            assertEquals(Extensions.HASKELL,FileExtension.extensionDescription("lhs"));
            assertEquals(Extensions.TEXT,FileExtension.extensionDescription("txt"));
            assertEquals(Extensions.MARKDOWN,FileExtension.extensionDescription("md"));
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
