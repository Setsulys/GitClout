package fr.uge.gitclout.gitclout.jpa;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.Set;

@Entity
public class Langage {
    @Id
    private String languageName;
    @OneToMany
    private Set<Participation> participation;
    /**
     * JPA Class of a Language
     */
    public Langage(){

    }

    public Langage(String languageName, Set<Participation> participation){
        this.languageName = languageName;
        this.participation = participation;
    }

    public Langage(String languageName){
        this.languageName = languageName;
    }


    public String getLanguageName() {
        return languageName;
    }

    public void setLangage(String languageName) {
        this.languageName = languageName;
    }

    @Override
    public String toString(){
        return this.languageName;
    }
}
