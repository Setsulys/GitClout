package fr.uge.gitclout.gitclout.jpa;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
public class Tag {
    @Id
    private String tagId;
    private String nomTag;
    private String project;
    private Date date;

    @OneToMany
    private Set<Participation> participation;
    /**
     * JPA Class of Tags
     */
    public Tag(){

    }

    public Tag(String tagId,String nomTag, String project, Date date, Set<Participation> participation){
        Objects.requireNonNull(tagId);
        Objects.requireNonNull(nomTag);
        Objects.requireNonNull(date);
        Objects.requireNonNull(participation);
        this.tagId = tagId;
        this.nomTag = nomTag;
        this.date = date;
        this.project = project;
        this.participation = participation;
    }

    public Tag(String tagId,String nomTag, String project, Date date){
        Objects.requireNonNull(tagId);
        Objects.requireNonNull(nomTag);
        Objects.requireNonNull(date);
        this.tagId = tagId;
        this.nomTag = nomTag;
        this.date = date;
        this.project = project;
    }

    /**
     * Get tag id
     * @return shawan
     */
/*    public String getTagId() {
        return tagId;
    }*/

    /**
     * Set tag id
     * @param tagId the shawan of the tag
     */
    public void setTagId(String tagId) {
        Objects.requireNonNull(tagId);
        this.tagId = tagId;
    }

    /**
     * get tag name
     * @return tag name
     */
    public String getNomTag() {
        return nomTag;
    }

    /**
     * set tag name
     * @param nomTag tag name
     */
    public void setNomTag(String nomTag) {
        Objects.requireNonNull(nomTag);
        this.nomTag = nomTag;
    }

    /**
     * get project link
     * @return project link
     */
    public String getProject() {
        return project;
    }

    /**
     * Set project name
     * @param project link
     */
    public void setProject(String project) {
        Objects.requireNonNull(project);
        this.project = project;
    }

    /** NOT FINISHED, MAYBE USE LATER
     * get tag date
     * @return tag date
     */
/*    public Date getDate() {
        return date;
    }*/

    /**
     * Set the date for the tag
     * @param date a date of tag
     */
    public void setDate(Date date) {
        Objects.requireNonNull(date);
        this.date = date;
    }

    @Override
    public String toString(){
        return this.nomTag;
    }
}
