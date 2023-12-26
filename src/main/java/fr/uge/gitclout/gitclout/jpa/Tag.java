package fr.uge.gitclout.gitclout.jpa;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.Date;
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

    public Tag(){

    }

    public Tag(String tagId,String nomTag, String project, Date date, Set<Participation> participation){
        this.tagId = tagId;
        this.nomTag = nomTag;
        this.date = date;
        this.participation = participation;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getNomTag() {
        return nomTag;
    }

    public void setNomTag(String nomTag) {
        this.nomTag = nomTag;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
