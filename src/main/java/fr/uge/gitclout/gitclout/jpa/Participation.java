package fr.uge.gitclout.gitclout.jpa;


import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

import java.util.Objects;

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
    /**
     * JPA Class of the associative table Participe of the database
     */
    public Participation(){

    }

    public Participation(ParticipationPrimaryKey id, int nbLignesCode, Contributeur contributeur, Langage langage, Tag tag){
        Objects.requireNonNull(id);
        Objects.requireNonNull(contributeur);
        Objects.requireNonNull(langage);
        Objects.requireNonNull(tag);
        if(nbLignesCode < 0){
            throw new IllegalArgumentException();
        }
        this.id = id;
        this.nbLignesCode = nbLignesCode;
        this.contributeur = contributeur;
        this.langage = langage;
        this.tag = tag;
    }


    /**
     * return the number of line in current participation
     * @return the number of line in current participation
     */
    public int getLignes() {
        return nbLignesCode;
    }

    /**
     * Set number of line of this participation
     * @param lignes number of lines of code
     */
    public void setNbLignesCode(int lignes) {
        if(lignes< 0){
            throw new IllegalArgumentException();
        }
        this.nbLignesCode = lignes;
    }

    /**
     * get primary key of this class
     * @return primary key
     */
    public ParticipationPrimaryKey getId() {
        return id;
    }

    /**
     * Set the primary key of this class
     * @param id primary key
     */
    public void setId(ParticipationPrimaryKey id) {
        Objects.requireNonNull(id);
        this.id = id;
    }

    public String toString(){
        return "contributeur : " + this.getId().getGitId() + ", langage : " + this.getId().getLanguageName() + ", pour le tag : " + this.getTag().getNomTag() + ", avec  le nombre de ligne : " + this.nbLignesCode;
    }


    /**
     * NOT FINISHED, MAYBE WANTED TO USE IT
     * param contributeur return the contributors of the current participation
     * @param contributeur return the contributors of the current participation
     */
/*    public Contributeur getContributeur() {
        return contributeur;
    }*/

    /**
     * Set the contributor for current participation
     * @param contributeur contributor
     */
    public void setContributeur(Contributeur contributeur) {
        Objects.requireNonNull(contributeur);
        this.contributeur = contributeur;
    }

    /**
     * get the language of current participation
     * @return the language of current participation
     */
    public Langage getLangage() {
        return langage;
    }

    /**
     * set the language of current participation
     * @param langage the language of current participation
     */
    public void setLangage(Langage langage) {
        Objects.requireNonNull(langage);
        this.langage = langage;
    }

    /**
     * Get current tag
     * @return a tag
     */
    public Tag getTag() {
        return tag;
    }

    /**
     * Set current participation tag
     * @param tag a tag
     */
    public void setTag(Tag tag) {
        Objects.requireNonNull(tag);
        this.tag = tag;
    }
}


