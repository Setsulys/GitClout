package fr.uge.gitclout.gitclout.jpa;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class Participation {

    @EmbeddedId
    private ParticipationPrimaryKey id;

    private int nbLignesCode;

    private int nbLignesCom;


    public int getLignes() {
        return nbLignesCode;
    }

    public void setNbLignesCode(int lignes) {
        this.nbLignesCode = lignes;
    }

    public int getNbLignesCom() {
        return nbLignesCom;
    }

    public void setNbLignesCom(int nbLignesCom) {
        this.nbLignesCom = nbLignesCom;
    }

    public ParticipationPrimaryKey getId() {
        return id;
    }

    public void setId(ParticipationPrimaryKey id) {
        this.id = id;
    }
}
