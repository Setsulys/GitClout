package fr.uge.gitclout.gitclout;

import fr.uge.gitclout.gitclout.blame.Blame;
import fr.uge.gitclout.gitclout.blame.Data;
import fr.uge.gitclout.gitclout.blame.JGitBlame;
import fr.uge.gitclout.gitclout.blame.UtilsMethods;
import fr.uge.gitclout.gitclout.jpa.Contributeur;
import fr.uge.gitclout.gitclout.jpa.ContributeurService;
import fr.uge.gitclout.gitclout.jpa.DatabaseManager;
import fr.uge.gitclout.gitclout.jpa.Tag;
import jdk.jshell.execution.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceContext;
import java.sql.Array;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class BackApplication{

    private final DatabaseManager databaseManager;


    private final HashMap<String,JGitBlame> urlAndData = new HashMap<>();
    @Autowired
    public BackApplication(DatabaseManager databaseManager){
        this.databaseManager = databaseManager;
    }
    public boolean tryAndAdd (String gitLink){
        Objects.requireNonNull(gitLink);
        var runnable = UtilsMethods.isGitRepo(gitLink);
        JGitBlame jGit = new JGitBlame();
        if(runnable){
            urlAndData.put(gitLink,jGit);
            jGit.run(gitLink);
            System.out.println(jGit.projectData().stream().map(e-> e.blameDatas().toString()).collect(Collectors.joining("\n")));
            databaseManager.fillDatabase(jGit.projectData(),jGit.getGit(),jGit.getDateMap());

        }
        return runnable;
    }

    public HashMap<String,HashMap<String,Integer>> radarData(String gitLink){
        return databaseManager.MapOfAverage(gitLink);
    }

    public ArrayList<String> getTagOfProject(String gitLink){
        return databaseManager.getTags(gitLink);
    }

    public List<String> displayProjects(){
        return new ArrayList<>(urlAndData.keySet());
    }

    public double getPercentageOfProject(String gitLink){
        var get = urlAndData.getOrDefault(gitLink,null);
        return get!=null? get.PercentOfFinishedTask():0;
    }
}
