package fr.uge.gitclout.gitclout.jpa;


import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class Participation {

    @EmbeddedId
    private ParticipationPrimaryKey id;

    private int nbLignesCode;

    @ManyToOne
    private Contributeur contributeur;
    @ManyToOne
    private Langage langage;
    @ManyToOne
    private Tag tag;

    public Participation(){

    }

    public Participation(ParticipationPrimaryKey id, int nbLignesCode, Contributeur contributeur, Langage langage, Tag tag){
        this.id = id;
        this.nbLignesCode = nbLignesCode;
        this.contributeur = contributeur;
        this.langage = langage;
        this.tag = tag;
    }


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