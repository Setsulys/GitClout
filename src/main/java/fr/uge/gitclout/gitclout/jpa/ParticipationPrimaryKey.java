package fr.uge.gitclout.gitclout.jpa;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import java.io.Serializable;

@Embeddable
public class ParticipationPrimaryKey implements Serializable {
    @JoinColumn(name = "gitId", referencedColumnName = "gitId")
    private String gitId;

    @JoinColumn(name = "tagId", referencedColumnName = "TagId")
    private String tagId;

    @JoinColumn(name = "fichierId", referencedColumnName = "fichierId")
    private int fichierId;

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

    public int getFichierId() {
        return fichierId;
    }

    public void setFichierId(int fichierId) {
        this.fichierId = fichierId;
    }
}
