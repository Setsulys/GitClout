package fr.uge.gitclout.gitclout.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.Set;

@Entity

public class Contributeur {
    @Id
    private String gitId;

    private String name;
    @OneToMany
    private Set<Participation> participation;

    /**
     * JPA Class of a contributor
     */
    public Contributeur() {
    }

    public Contributeur(String gitId,String name, Set<Participation> participation) {
        this.gitId = gitId;
        this.name =name;
        this.participation = participation;
    }

    public Contributeur(String gitId,String name){
        this.gitId = gitId;
        this.name = name;
    }

    public String getGitId(){
        return gitId;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setGitId(String id){
        gitId = id;
    }
}
