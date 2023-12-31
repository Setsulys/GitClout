package fr.uge.gitclout.gitclout.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.Objects;
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

    /**
     * Get the git id from contributor
     * @return the git id
     */
    public String getGitId(){
        return gitId;
    }

    /**
     * Set contributor name
     * @param name name of contributor
     */
    public void setName(String name){
        Objects.requireNonNull(name);
        this.name = name;
    }

    /**
     * Get contributor name
     * @return contributor name
     */
    public String getName(){
        return name;
    }

    /**
     * get contributor id
     * @param id contributor id
     */
    public void setGitId(String id){
        Objects.requireNonNull(id);
        gitId = id;
    }
}
