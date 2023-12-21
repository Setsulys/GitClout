package fr.uge.gitclout.gitclout;

import fr.uge.gitclout.gitclout.blame.Blame;
import fr.uge.gitclout.gitclout.blame.JGitBlame;
import fr.uge.gitclout.gitclout.blame.UtilsMethods;
import jdk.jshell.execution.Util;

import java.util.*;
import java.util.stream.Collectors;

public class BackApplication{

    private final HashMap<String,JGitBlame> urlAndData = new HashMap<>();
    public boolean tryAndAdd (String gitLink){
        Objects.requireNonNull(gitLink);
        var runnable = UtilsMethods.isGitRepo(gitLink);
        JGitBlame jGit = new JGitBlame();
        if(runnable){
            urlAndData.put(gitLink,jGit);
            jGit.run(gitLink);
        }
        return runnable;
    }

    public ArrayList<Blame> projectData(String gitLink){
        return urlAndData.get(gitLink).projectData();
    }
    public List<String> displayProjects(){
        return new ArrayList<>(urlAndData.keySet());
    }

    public double getPercentageOfProject(String gitLink){
        var get = urlAndData.getOrDefault(gitLink,null);
        return get!=null? get.PercentOfFinishedTask():0;
    }
}
