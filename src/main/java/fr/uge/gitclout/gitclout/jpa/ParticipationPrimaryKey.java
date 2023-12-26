package fr.uge.gitclout.gitclout.jpa;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import java.io.Serializable;

@Embeddable
public class ParticipationPrimaryKey implements Serializable {
    @JoinColumn(name = "gitId", referencedColumnName = "gitId")
    private String gitId;

    @JoinColumn(name = "tagId", referencedColumnName = "tagId")
    private String tagId;

    @JoinColumn(name = "languageName", referencedColumnName = "languageName")
    private String languageName;

    public String getGitId() {
        return gitId;
    }

    public void setGitId(String gitId) {
        this.gitId = gitId;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }
}
