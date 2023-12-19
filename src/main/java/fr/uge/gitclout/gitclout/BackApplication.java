package fr.uge.gitclout.gitclout;

import fr.uge.gitclout.gitclout.blame.JGitBlame;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class BackApplication{

    private final HashSet<String> gitProjects = new HashSet<>();
    //private final ArrayList<String> gitProjects = new ArrayList<>();

    public boolean tryAndAdd (String gitLink){
        Objects.requireNonNull(gitLink);
        JGitBlame jGit = new JGitBlame();
        var isRunnable = jGit.run(gitLink);
        if(isRunnable){
            gitProjects.add(gitLink);
        }
        return isRunnable;
    }
    public List<String> displayProjects(){
        return new ArrayList<>(gitProjects);
    }
}
