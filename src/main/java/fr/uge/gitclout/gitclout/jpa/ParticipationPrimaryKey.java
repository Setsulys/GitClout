package fr.uge.gitclout.gitclout.jpa;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ParticipationPrimaryKey implements Serializable {
    @JoinColumn(name = "gitId", referencedColumnName = "gitId")
    private String gitId;

    @JoinColumn(name = "tagId", referencedColumnName = "tagId")
    private String tagId;

    @JoinColumn(name = "languageName", referencedColumnName = "languageName")
    private String languageName;
    public ParticipationPrimaryKey(){

    }

/*    public ParticipationPrimaryKey(String gitId,String tagId, String languageName){
        this.gitId = gitId;
        this.tagId = tagId;
        this.languageName = languageName;
    }*/

    /**
     * Get the email of contributor
     * @return email of contributor
     */
    public String getGitId() {
        return gitId;
    }

    /**
     * set for current participation, the contributor
     * @param gitId contributor email
     */
    public void setGitId(String gitId) {
        Objects.requireNonNull(gitId);
        this.gitId = gitId;
    }

    /**
     * NOT FINISHED, MAYBE WANTED TO USE IT
     * Get the participation tagID
     * @return the tagID
     */
/*    public String getTagId() {
        return tagId;
    }*/

    /**
     * Set the tagId for the current participation
     * @param tagId id of the tag blamed
     */
    public void setTagId(String tagId) {
        Objects.requireNonNull(tagId);
        this.tagId = tagId;
    }

    /**
     * Get the language
     * @return language
     */
    public String getLanguageName() {
        return languageName;
    }

    /**
     * set the language for current participation
     * @param languageName language name
     */
    public void setLanguageName(String languageName) {
        Objects.requireNonNull(languageName);
        this.languageName = languageName;
    }
}
