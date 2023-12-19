package fr.uge.gitclout.gitclout.blame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.diff.RawText;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.treewalk.TreeWalk;

public class Blame {

	private final Map<Extensions,ArrayList<String>> extensionsFiles = new HashMap<>();
	private final Git git;
	private final TreeWalk treeWalk;
	private final Ref currentTag;
	private final int currentTagPosition;
	private final ArrayList<Data> blameData = new ArrayList<>();
	private final ArrayList<Contributor> contributorData ;
	private final List<String> changedFiles;

	/**
	 * Constructor of blame class
	 * @param git the git on what we blame
	 * @param treeWalk
	 * @param tagTree
	 * @param allTag list of all tags, chronologicaly sorted
	 * @param currentTagPosition is the current position of the tag in the allTag list
	 * @param filesChanged list of changed files
	 * @throws MissingObjectException
	 * @throws IncorrectObjectTypeException
	 * @throws CorruptObjectException
	 * @throws IOException
	 */
	public Blame(Git git, TreeWalk treeWalk, RevTree tagTree,List<Ref> allTag,int currentTagPosition,List<String> filesChanged) throws MissingObjectException, IncorrectObjectTypeException, CorruptObjectException, IOException{
		Objects.requireNonNull(git);
		Objects.requireNonNull(treeWalk);
		Objects.requireNonNull(tagTree);
		Objects.requireNonNull(allTag);
		Objects.requireNonNull(filesChanged);
		if(currentTagPosition<0) {
			throw new IllegalArgumentException();
		}
		this.git = git;
		this.treeWalk = treeWalk;
		this.currentTagPosition=currentTagPosition;
		this.currentTag = allTag.get(currentTagPosition);
		this.contributorData = GitTools.authorCredentials(git);
		this.changedFiles =filesChanged;
		treeWalk.addTree(tagTree);
		treeWalk.setRecursive(true);
	}

	/**
	 * This method take a file and put it in the class map to know what is this file language
	 * @param extension the extension of the file (like file.java, the extension is java)
	 * @param filePath name of the file
	 */
	private void addFilesForExtensions(Extensions extension,String filePath) {
		Objects.requireNonNull(extension);
		Objects.requireNonNull(filePath);
		if(extensionsFiles.putIfAbsent(extension, new ArrayList<>(List.of(filePath)))!=null) {
			var list = extensionsFiles.get(extension);
			list.add(filePath);
			extensionsFiles.put(extension, list);
		}
	}

	/**
	 * Principal method of this class that check the blame of git files, this will look upon the lines of codes and lines of comments
	 * @throws MissingObjectException
	 * @throws IncorrectObjectTypeException
	 * @throws CorruptObjectException
	 * @throws IOException
	 * @throws GitAPIException
	 */
	public void blaming() throws MissingObjectException, IncorrectObjectTypeException, CorruptObjectException, IOException, GitAPIException  {
		var sW = new StringWork();
		System.out.println("-------------------------------------------");
		while(treeWalk.next()) {
			String filePath = treeWalk.getPathString();
			checkBlame(sW,filePath);

		}
	}

	/**
	 * check if a file has been modified, deleted or added
	 * if the condition is matching the method will blame the file
	 * @param sW is the stringWork class that permit to split or do anything on the file
	 * @param filePath name of the file
	 * @throws GitAPIException
	 */
	private void checkBlame(StringWork sW,String filePath) throws GitAPIException {
		if(sW.splitExtention(filePath)!=null) {
			var extension = sW.splitExtention(filePath).extension(); //get file extension from record Extension(File,extension)
			if(FileExtension.extensionDescription(extension)!=Extensions.OTHER && FileExtension.extensionDescription(extension)!=Extensions.RESSOURCES) {
				var blameResult = git.blame().setStartCommit(currentTag.getObjectId()).setFilePath(filePath).call();
				//System.out.println(extension +" to do" + FileExtension.extensionDescription(extension));
				if(currentTagPosition==0) { //we need to blame the first tag
					checkCommentsInit(blameResult,FileExtension.extensionDescription(extension));
				}
				else if(changedFiles.contains(filePath)) {//Check if the current file is modified
					//System.out.println(changedFiles.size()+" & " +filePath +" is present ");
					checkCommentsInit(blameResult,FileExtension.extensionDescription(extension));
				}
			}
			addFilesForExtensions(FileExtension.extensionDescription(extension), filePath);
		}
	}


	/**
	 * return a regex of the language comments
	 * @param extension extension of the current file language
	 * @return a regex of the language comments
	 */
	private String regex(Extensions extension) {
		return switch(extension) {
		case Extensions.C -> "^(?!.*([\"'])(?:(?!\\1|//|/\\*).)*\\1)(^[\s\t]*+//|//|^[\s\t]*+/\\*|/\\*|^[\s\t]*+\\*|\\*).*$";
		case Extensions.JAVA -> "^(?!.*([\"'])(?:(?!\\1|//|/\\*).)*\\1)(^[\s\t]*+//|//|^[\s\t]*+/\\*|/\\*|^[\s\t]*+\\*|\\*).*$";
		case Extensions.JAVASCRIPT-> "^(?!.*([\"'])(?:(?!\\1|//|/\\*).)*\\1)(^[\s\t]*+//|//|^[\s\t]*+/\\*|/\\*|^[\s\t]*+\\*|\\*).*$";
		case Extensions.HTML -> "(?!.*([\"']).*\\1.*)(?:^\s*<!--|<!--(?:[^-]|-(?!->))*-->)";
		case Extensions.CSS -> "(?!.*([\"']).*\\1.*)(?:^\s*\\/\\*.*?\\*\\/)";
		case Extensions.PYTHON -> "(?!.*([\"']).*\\1.*)(?:^\s*#.*|^\s*(?:'''|\"\"\")(?:[^\n]*\\w[^\n]*)*(?:'''|\"\"\"))";
		case Extensions.CPLUSPLUS -> "^(?!.*([\"'])(?:(?!\\1|//|/\\*).)*\\1)(^[\s\t]*+//|//|^[\s\t]*+/\\*|/\\*|^[\s\t]*+\\*|\\*).*$";
		case Extensions.PHP -> "(?!.*([\"']).*\\1.*)(?:^\s*(?:\\/\\/|#|\\/\\*).*)";
		case Extensions.TYPESCRPIPT -> "^(?!.*([\"'])(?:(?!\\1|//|/\\*).)*\\1)(^[\s\t]*+//|//|^[\s\t]*+/\\*|/\\*|^[\s\t]*+\\*|\\*).*$";
		case Extensions.RUBY -> "(#.*$|^=begin(\s(.?))*(=end\s*$))";
		case Extensions.CSHARP -> "^(?!.*([\"'])(?:(?!\\1|//|/\\*).)*\\1)(^[\s\t]*+//|//|^[\s\t]*+/\\*|/\\*|^[\s\t]*+\\*|\\*).*$";
		default ->  "";
		};

	}
	/**
	 * Init all elements and put in a record list all the lines typed by all contributors
	 * @param blame blame result of the file
	 * @param extension extension of the current file language
	 */
	public void checkCommentsInit(BlameResult blame,Extensions extension) {
		Objects.requireNonNull(blame);
		Objects.requireNonNull(extension);
		var codeCount = contributorData.stream().collect(Collectors.toMap(person -> person,person -> 0,(oldValue,newValue)->newValue,HashMap::new));
		if(extension.equals(Extensions.OTHER) || extension.equals(Extensions.MEDIA)) {
			return;
		}
		RawText rawText = blame.getResultContents();
		String regex = regex(extension);
		Pattern pattern = Pattern.compile(regex);
		try {
			checkComments(blame,rawText,pattern,codeCount);
		} catch (Exception e) {
			return;
		}
	}

	/**
	 * Try to get the number of comments and code line for each contributor and put it in a record list
	 * @param blame blame result of the file
	 * @param rawText is the result content of the blame
	 * @param pattern is the pattern get from the method regex
	 * @param codeCount a hashmap collecting contributors and it contribution on codes lines
	 * @throws ExecutionException
	 */
	public void checkComments(BlameResult blame,RawText rawText, Pattern pattern, HashMap<Contributor,Integer> codeCount) throws InterruptedException{
		Objects.requireNonNull(blame);
		Objects.requireNonNull(rawText);
		Objects.requireNonNull(codeCount);
		for(int i =0;i < rawText.size();i++) {
			var author = blame.getSourceAuthor(i); //getmail
			var contr = new Contributor(author.getName(),author.getEmailAddress());
			String line = rawText.getString(i);
			Matcher matcher = pattern.matcher(line);
			if(!matcher.find()) {
				codeCount.compute(contr, (k,v)->(v==null)?1:v+1);
			};
		}
		divideIntoData(treeWalk.getPathString(), codeCount);
	}
	/**
	 * Get all information for the actual file ( number of line of code/line of comments)  and put it in an arraylist of record Data
	 * @param file is the name of this file
	 * @param countline a hashmap collecting contributors and it contribution on codes lines
	 */
	private void divideIntoData(String file,Map<Contributor,Integer> countline) {
		for(var contributor : contributorData) {
			var line =countline.getOrDefault(contributor, null);
			//System.out.println(new Data(currentTag, contributor,file,line!=null?line:0));
			blameData.add(new Data(currentTag, contributor,file,line!=null?line:0));
		}
	}








	/**
	 * return all the blame done in this tag
	 * @return all the blame done in this tag
	 */
	public ArrayList<Data> blameDatas(){
		return blameData;
	}

	/**
	 * return a map containting for each extensions present in the tag the files link to this extension
	 * @return a map containting for each extensions present in the tag the files link to this extension
	 */
	public Map<Extensions,ArrayList<String>> extensionsFilesMap(){
		return extensionsFiles;
	}

	public Ref currentRef() {
		return currentTag;
	}

	public int currentRefPosition() {
		return currentTagPosition;
	}


	/**
	 *return String of file and whom typed how many line of each type
	 * @return String of file and whom typed how many line of each type
	 */
	public String divideIntoDataString() {
		var affichage = blameData.stream().collect(Collectors.groupingBy(Data::file));
		return affichage.entrySet().stream().map(e -> e.getKey() + " : " + e.getValue().stream().map(f-> f.toString()).collect(Collectors.joining(", ","{","}"))).collect(Collectors.joining("\n"));
	}


	/**
	 * return a string of number of code line in each files
	 * @return a string of number of code line in each files
	 */
	public String divideIntoDataStringGetLinesByFile() {
		var affichage = blameData.stream().collect(Collectors.groupingBy(Data::file));
		return affichage.entrySet().stream().map(e -> e.getKey() + " ----> " + e.getValue().stream().map(f-> f.lines()).reduce(0, (a,b)-> a+b)).collect(Collectors.joining("\n"));
	}

	/**
	 * return a string displaying the total number of lines of code typed by each contributor
	 * @return a string displaying the total number of lines of code typed by each contributor
	 */
	public String totalLinesCode() {
		var affichage = blameData.stream().collect(Collectors.groupingBy(Data::contributor));
		return affichage.entrySet().stream().map(e-> e.getKey().name()+ " ----> "+ e.getValue().stream().map(f-> f.lines()).reduce(0, (a,b)-> a+b)).collect(Collectors.joining("\n"));
	}

	/**
	 * Display Technologies used for each files
	 * @return Name of the technology with a list of every files for this technology
	 */
	public String getFilesExtensions() {
		return extensionsFiles.entrySet().stream()
				.map(k -> k.getKey() + " : " +k.getValue())
				.collect(Collectors.joining("\n"));
	}
}

