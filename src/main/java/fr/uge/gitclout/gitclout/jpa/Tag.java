package fr.uge.gitclout.gitclout.jpa;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Tag {
    @Id
    private String TagId;
    private String nomTag;
    private String project;
    private Date date;

    public String getTagId() {
        return TagId;
    }

    public void setTagId(String tagId) {
        this.TagId = tagId;
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
