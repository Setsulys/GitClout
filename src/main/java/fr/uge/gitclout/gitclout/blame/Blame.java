package fr.uge.gitclout.gitclout.blame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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

	private final Git git;
	private final TreeWalk treeWalk;
	private final Ref currentTag;
	private final int currentTagPosition;
	private final ArrayList<Contributor> contributorData ;
	private final List<String> changedFiles;
	private final HashMap<ContributorLanguage,Data> datas = new HashMap<>();
	private final Object lock = new Object();


	/**
	 * Constructor of blame class
	 * @param git the git on what we blame
	 * @param treeWalk permit to walk across the files
	 * @param tagTree reference of the current tree of the tag
	 * @param allTag list of all tags, chronologically sorted
	 * @param currentTagPosition is the current position of the tag in the allTag list
	 * @param filesChanged list of changed files
	 * @throws MissingObjectException exception
	 * @throws IncorrectObjectTypeException exception
	 * @throws CorruptObjectException exception
	 * @throws IOException exception
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
	 * Principal method of this class that check the blame of git files, this will look upon the lines of codes and lines of comments
	 * @throws MissingObjectException exception
	 * @throws IncorrectObjectTypeException exception
	 * @throws CorruptObjectException exception
	 * @throws IOException exception
	 */
	public void blaming() throws MissingObjectException, IncorrectObjectTypeException, CorruptObjectException, IOException {
		try (ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*3/4)) {
			while (treeWalk.next()) {
				String filePath = treeWalk.getPathString();
				executor.submit(() -> {
					try {
						checkBlame(filePath);
					} catch (GitAPIException e) {
						throw new AssertionError(e);
					}
				});
			}
		}
	}

	/**
	 * check if a file has been modified, deleted or added
	 * if the condition is matching the method will blame the file
	 * @param filePath name of the file
	 * @throws GitAPIException exception
	 */
	private void checkBlame(String filePath) throws GitAPIException {
		Objects.requireNonNull(filePath);
		var extension = StringWork.splitExtention(filePath).extension(); //get file extension from record Extension(File,extension)
		var description = FileExtension.extensionDescription(extension);
		if(!(description.equals(Extensions.OTHER) || description.equals(Extensions.MEDIA))) {
			var blameResult = git.blame().setStartCommit(currentTag.getObjectId()).setFilePath(filePath).call();
			if(currentTagPosition==0 || changedFiles.contains(filePath)) { //we need to blame the first tag
				checkCommentsInit(blameResult,description);
			}
		}
	}


	/**
	 * return a regex of the language comments
	 * Its best to have a regex for each type of file instead of a big one
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
		case Extensions.TYPESCRIPT -> "^(?!.*([\"'])(?:(?!\\1|//|/\\*).)*\\1)(^[\s\t]*+//|//|^[\s\t]*+/\\*|/\\*|^[\s\t]*+\\*|\\*).*$";
		case Extensions.RUBY -> "(#.*$|^=begin(\s(.?))*(=end\s*$))";
		case Extensions.CSHARP -> "^(?!.*([\"'])(?:(?!\\1|//|/\\*).)*\\1)(^[\s\t]*+//|//|^[\s\t]*+/\\*|/\\*|^[\s\t]*+\\*|\\*).*$";
		case Extensions.CONFIGURATION ->"#.*$";
		case Extensions.MAKEFILE ->"#.*$";
		default ->  "a^";
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
			checkComments(blame,rawText,pattern,extension,codeCount);
		} catch (Exception e) {
			return;
		}
	}

	/**
	 * Try to get the number of comments and code line for each contributor and put it in a record list
	 * @param blame blame result of the file
	 * @param rawText is the result content of the blame
	 * @param pattern is the pattern get from the method regex
	 * @param extension is the extension of the file
	 * @param codeCount a hashmap collecting contributors and it contribution on codes lines
	 */
	public void checkComments(BlameResult blame,RawText rawText, Pattern pattern,Extensions extension, HashMap<Contributor,Integer> codeCount){
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
		divideIntoData(extension, codeCount);
	}
	/**
	 * Get all information for the actual file ( number of line of code/line of comments)  and put it in an arraylist of record Data
	 * @param extension is the extension of this file
	 * @param countLine a hashmap collecting contributors and it contribution on codes lines
	 */
	private void divideIntoData(Extensions extension,Map<Contributor,Integer> countLine) {
		synchronized (lock){
			for(var contributor : contributorData) {
				var line =countLine.getOrDefault(contributor, null);
				var ce =new ContributorLanguage(contributor, extension);
				if(datas.containsKey(ce)) {
					datas.get(ce).addLines(line!=null?line:0);
				}
				else{
					var data =new Data(currentTag, contributor,extension);
					data.addLines(line!=null?line:0);
					datas.put(ce, data);
				}
			}
		}

	}







	/**
	 * return all the blame done in this tag
	 * @return all the blame done in this tag
	 */
	public ArrayList<Data> blameDatas(){
		return new ArrayList<>(datas.values());
	}

	public Ref currentRef() {
		return currentTag;
	}

}

