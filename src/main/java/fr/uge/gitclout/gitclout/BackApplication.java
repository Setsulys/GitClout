package fr.uge.gitclout.gitclout;

import fr.uge.gitclout.gitclout.blame.Blame;
import fr.uge.gitclout.gitclout.blame.Data;
import fr.uge.gitclout.gitclout.blame.JGitBlame;
import fr.uge.gitclout.gitclout.blame.UtilsMethods;
import fr.uge.gitclout.gitclout.jpa.Contributeur;
import fr.uge.gitclout.gitclout.jpa.ContributeurService;
import fr.uge.gitclout.gitclout.jpa.DatabaseManager;
import jdk.jshell.execution.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Collectors;
@Service
public class BackApplication{

    private DatabaseManager databaseManager;


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
            databaseManager.fillDatabase(jGit.projectData(),jGit.getGit(),jGit.getDateMap());

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