package fr.uge.gitclout.gitclout.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;


public interface ParticipationRepo extends JpaRepository<Participation,ParticipationPrimaryKey> {

/*    @Query("SELECT p FROM Participation p WHERE p.langage.languageName = :languageName")
    ArrayList<Participation> findParticipationsByLangage(String languageName);

    @Query("SELECT p FROM Participation p WHERE p.langage.languageName = :languageName AND p.contributeur.gitId = :gitId")
    ArrayList<Participation> findParticipationsByLanguageAndContributor(String languageName,String gitId);

    @Query("SELECT p FROM Participation p WHERE p.contributeur.gitId = :gitId")
    ArrayList<Participation> findParticipationsByContributor(String gitId);*/

    /**
     * return all participation on this tag by the contributor
     * @param nomTag name of the tag
     * @param gitId email of the contributor
     * @return all participation on this tag by the contributor
     */
    @Query("SELECT p FROM Participation p WHERE p.tag.nomTag = :nomTag AND p.contributeur.gitId = :gitId")
    ArrayList<Participation> findParticipationsByTagAndContributeur(String nomTag,String gitId);

    /**
     * return all participation on this project by the contributor
     * @param project link of the project
     * @param gitId email of the contributor
     * @return all participation on this project by the contributor
     */
    @Query("SELECT p FROM Participation p WHERE p.tag.project = :project AND p.contributeur.gitId = :gitId")
    ArrayList<Participation> findParticipationsByProjectAndContributor(String project, String gitId);


}