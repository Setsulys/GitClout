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
/*    public Langage(){

    }*/


    /**
     * Get the language
     * @return the language
     */
    public String getLanguageName() {
        return languageName;
    }

    /**
     * Set language in current tag
     * @param languageName language name
     */
    public void setLangage(String languageName) {
        this.languageName = languageName;
    }

    @Override
    public String toString(){
        return this.languageName;
    }
}
