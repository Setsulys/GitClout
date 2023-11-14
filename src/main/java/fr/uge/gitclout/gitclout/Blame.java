package fr.uge.gitclout.gitclout;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.treewalk.TreeWalk;

public class Blame {

    private final Map<String,Map<String,Long>> fileBlame = new HashMap<>();
    private Map<String,Long> totalCount = new HashMap<>();
    private final Map<Extensions,ArrayList<String>> extensionsFiles = new HashMap<>();
    private final Map<Extensions,Integer> nbComments = new HashMap<>();
    private final Map<String,Integer> nbCommentsForFile = new HashMap<>();



    private final Git git;
    private final TreeWalk treeWalk;
    private final List<Ref> getTags;

    @SafeVarargs
    public static void checkNonNull(Object ... check) {
        Arrays.stream(check).forEach(e -> Objects.requireNonNull(e));
    }

    /**
     * Check the extension for each files and put it in a map for later use
     * @param extension
     * @param filePath
     */
    private void addFilesForExtensions(Extensions extension,String filePath) {
        checkNonNull(extension,filePath);
        if(extensionsFiles.putIfAbsent(extension, new ArrayList<>(List.of(filePath)))!=null) {
            var list = extensionsFiles.get(extension);
            list.add(filePath);
            extensionsFiles.put(extension, list);
        }
    }

    /**
     *  Check if the line of the file is a comment, if so we increment the counter
     * @param extension
     * @param lines
     * @param path
     */
    public void checkComments(Extensions extension, ArrayList<String> lines,String path) {
        checkNonNull(extension,lines,path);
        if(!extension.equals(Extensions.OTHER)){
            int nbCom = 0;
            for(var line : lines){
                String regex = "^(?!.*([\"'])(?:(?!\\1|//|/\\*).)*\\1)(^[\s\t]*+//|//|^[\s\t]*+/\\*|/\\*|^[\s\t]*+\\*|\\*).*$";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(line);
                if(matcher.find()){
                    //var match = matcher.group();
                    nbCom++;
                }
            }
            nbCommentsForFile.put(path, nbCom);
            if(nbComments.getOrDefault(extension, -1)==-1) {
                nbComments.put(extension, nbCom);
            }
            else {
                nbComments.merge(extension, nbCom,Integer::sum);
            }

        }
    }

    /**
     * prepares all we need to use the regex
     * @param treewalk
     * @param git
     * @param extension
     * @throws MissingObjectException
     * @throws IOException
     */
    private void prepareAndCheckComments(TreeWalk treewalk,Git git,Extensions extension) throws MissingObjectException, IOException {
        checkNonNull(treewalk,git,extension);
        ObjectId objectId = treewalk.getObjectId(0);
        ObjectLoader loader = git.getRepository().open(objectId);
        var content = new String(loader.getBytes());
        ArrayList<String> lines = new ArrayList<>(Arrays.asList(content.split("\n"))) ;
        checkComments(extension, lines,treewalk.getPathString());
    }

    /**
     * Check if the file as an extension, if not the file will not be registered
     * @param filePath
     * @param extension
     * @param countByName
     */
    private void checkIfCode(String filePath,Extensions extension,Map<String,Long> countByName) {
        checkNonNull(filePath,extension,countByName);
        if(!extension.equals(Extensions.OTHER)) {
            fileBlame.put(filePath, countByName);
        }
        totalCount = fileBlame.values().stream().flatMap(e -> e.entrySet().stream()).collect(Collectors.groupingBy(Map.Entry::getKey,Collectors.summingLong(Map.Entry::getValue)));

    }


    /**
     * Make the git blame on all the files of the directory
     * @param git
     * @param treeWalk
     * @param tagTree
     * @param getTags
     * @throws MissingObjectException
     * @throws IncorrectObjectTypeException
     * @throws CorruptObjectException
     * @throws IOException
     * @throws GitAPIException
     */
    public Blame(Git git, TreeWalk treeWalk, RevTree tagTree,List<Ref> getTags/*A voir si on modifie pas par un unique tag*/) throws IOException {
        checkNonNull(git,treeWalk,tagTree,getTags);
        this.git = git;
        this.treeWalk = treeWalk;
        this.getTags = getTags;
        treeWalk.addTree(tagTree);
        treeWalk.setRecursive(true);
    }

    public void blame() throws MissingObjectException, IncorrectObjectTypeException, CorruptObjectException, IOException, GitAPIException{
        var sW = new StringWork();
        while(treeWalk.next()) {
            String filePath = treeWalk.getPathString();
            if(sW.splitExtention(filePath)!=null) {
                var extension = sW.splitExtention(filePath).extension(); //get file extension from record Extension(File,extension)
                var blame = git.blame().setStartCommit(getTags.getFirst().getObjectId()).setFilePath(filePath).call(); //blame the curent file
                prepareAndCheckComments(treeWalk, git,FileExtension.extensionDescription(extension));
                addFilesForExtensions(FileExtension.extensionDescription(extension), filePath);
                Map<String,Long> countByName =IntStream.range(0, blame.getResultContents().size()).mapToObj(e -> blame.getSourceAuthor(e).getName()).collect(Collectors.groupingBy(Function.identity(),Collectors.counting()));
                checkIfCode(filePath,FileExtension.extensionDescription(extension), countByName);
            }
        }
    }



    /**
     * Display number of line for each contributor in a file
     * @return number of line by contributor for each files
     */
    public String getBlameByFileForEachContributor() {
        return fileBlame.entrySet().stream().map(k -> k.getKey()+" ----> "+k.getValue()).collect(Collectors.joining("\n"));
    }

    /**
     *
     * @return the number of lines in the file without distinguishing contributor
     */
    public String getBlameByFile() {
        return fileBlame.entrySet().stream().map(k -> k.getKey()+" ----> "+k.getValue().values().stream().mapToLong(Long::valueOf).sum()).collect(Collectors.joining("\n","\n----- Number of lines by files -----\n","\n---------------"));
    }

    /**
     * Display total number of line in the version for each contributor
     * @return The contributors and total number of line in the version
     */
    public String getTotalBlame() {
        return totalCount.entrySet().stream().map(k -> k.getKey() +" : "+k.getValue()).collect(Collectors.joining("\n"));
    }

    /**
     * Display Technologies used for each files
     * @return Name of the technology with a list of every files for this technology
     */
    public String getFilesExtensions() {
        return extensionsFiles.entrySet().stream().map(k -> k.getKey() + " : " +k.getValue()).collect(Collectors.joining("\n"));
    }

    /**
     * Display comment for all language
     * @return the number of comments in the tag project for a précise language
     */
    public String getNbCommentsForLanguage() {
        return nbComments.entrySet().stream().map(k ->" Language ->"+ k.getKey() + " : " +k.getValue()).collect(Collectors.joining("\n"));
    }

    /**
     * Display for total comments
     * @return the total number of comments lines in the project
     */
    public String getTotalNbComments() {
        return "Total Comments of the project : "+nbComments.values().stream().mapToInt(Integer::valueOf).sum();
    }

    /**
     * Display comments for all files
     * @return the number of comments in the tag project for a précise file
     */
    public String getNbCommentsByFile() {
        return nbCommentsForFile.entrySet().stream().map(k ->k.getKey() + ":" + k.getValue()).collect(Collectors.joining("\n","\n----- Nb of comments by files -----\n","\n---------------"));
    }

    /**
     *
     * @return Map of file blame for each contributor
     */
    public Map<String, Map<String,Long>> fileBlameMap() {
        return fileBlame;
    }

    /**
     *
     * @return Map of contributor and how many lines the contributor have done in total
     */
    public Map<String,Long> totalCountMap() {
        return totalCount;
    }

    /**
     *
     * @return Map of the files with each extensions of it own
     */
    public Map<Extensions,ArrayList<String>> extensionsFilesMap(){
        return extensionsFiles;
    }

    /**
     *
     * @return Map of number of Comments for each language
     */
    public Map<Extensions,Integer> nbCommentsMap(){
        return nbComments;
    }

    /**
     *
     * @return Map of number of Comment for each files
     */
    public Map<String,Integer> nbCommentsForFileMap(){
        return nbCommentsForFile;
    }

}
