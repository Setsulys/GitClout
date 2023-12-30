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

    /**
     * Try to check if the link is a git link and run it if it is
     * @param gitLink link of the git
     * @return true if it is a git
     */
    public boolean tryAndAdd (String gitLink){
        Objects.requireNonNull(gitLink);
        var runnable = UtilsMethods.isGitRepo(gitLink);
        JGitBlame jGit = new JGitBlame();
        if(runnable){
            urlAndData.put(gitLink,jGit);
            jGit.run(gitLink);
            System.out.println(jGit.projectData().stream().map(e-> e.currentRef().getName() +" : "+ e.blameDatas().toString()).collect(Collectors.joining("\n")));
            databaseManager.fillDatabase(jGit.projectData(),jGit.getGit(),jGit.getDateMap());

        }
        return runnable;
    }

    /**
     * return data for the radar chart
     * @param gitLink link of the git
     * @return data for the radar chart
     */
    public HashMap<String,HashMap<String,Integer>> radarData(String gitLink){
        return databaseManager.MapOfAverage(gitLink);
    }

    /**
     * Return all tags of a project
     * @param gitLink link of the git
     * @return all tags of a project
     */
    public ArrayList<String> getTagOfProject(String gitLink){
        return databaseManager.getTags(gitLink);
    }

    /**
     * return a list of project that have already been blamed
     * @return a list project that have already been blamed
     */
    public List<String> displayProjects(){
        return new ArrayList<>(urlAndData.keySet());
    }

    /**
     * Return the percentage of project advance
     * @param gitLink link of the project
     * @return the percentage of project advance
     */
    public double getPercentageOfProject(String gitLink){
        var get = urlAndData.getOrDefault(gitLink,null);
        return get!=null? get.PercentOfFinishedTask():0;
    }
}
