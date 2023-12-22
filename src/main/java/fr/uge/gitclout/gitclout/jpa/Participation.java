package fr.uge.gitclout.gitclout.jpa;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class Participation {

    @EmbeddedId
    private ParticipationPrimaryKey id;

    private int nbLignesCode;


    public int getLignes() {
        return nbLignesCode;
    }

    public void setNbLignesCode(int lignes) {
        this.nbLignesCode = lignes;
    }


    public ParticipationPrimaryKey getId() {
        return id;
    }

    public void setId(ParticipationPrimaryKey id) {
        this.id = id;
    }
}
