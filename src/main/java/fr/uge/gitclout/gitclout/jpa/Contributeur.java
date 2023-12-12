package fr.uge.gitclout.gitclout.jpa;
import javax.persistence.*;

@Entity

public class Contributeur {
    @Id
    private String gitId;

    private String name;

    public String getGitId(){
        return gitId;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setGitId(String id){
        gitId = id;
    }
}
