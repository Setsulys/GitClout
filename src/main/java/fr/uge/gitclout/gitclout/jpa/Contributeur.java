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
