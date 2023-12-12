package fr.uge.gitclout.gitclout.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Fichier {
    @Id
    @GeneratedValue
    private int FichierId;
    private String nomFichier;
    private String Langage;

    public int getFichierId() {
        return FichierId;
    }

    public String getNomFichier() {
        return nomFichier;
    }

    public void setNomFichier(String nomFichier) {
        this.nomFichier = nomFichier;
    }

    public String getLangage() {
        return Langage;
    }

    public void setLangage(String langage) {
        Langage = langage;
    }
}
