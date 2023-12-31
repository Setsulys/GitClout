package fr.uge.gitclout.gitclout.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

@Service
public class ParticipationService {

    private ParticipationRepo participationRepo;

    @Autowired
    public ParticipationService(ParticipationRepo participationRepo){
        Objects.requireNonNull(participationRepo);
        this.participationRepo = participationRepo;
    }

    /**
     * Insert into participation
     * @param participation participation
     */
    public void insertParticipation(Participation participation) {
        Objects.requireNonNull(participation);
        participationRepo.save(participation);
    }

    /**
     * NOT FINISHED, MAYBE WANTED TO USE IT
     * return all participation
     * @return all participation
     */
/*    public ArrayList<Participation> findall(){
        return new ArrayList<>(participationRepo.findAll());
    }*/

    /**
     * NOT FINISHED
     * Get all participation of a precise language
     * @param languageName name of the language
     * @return all participation of a precise language
     */
/*    public ArrayList<Participation> findParticipationsByLanguage(String languageName) {
        Objects.requireNonNull(languageName);
        return participationRepo.findParticipationsByLangage(languageName);
    }*/

    /**
     * NOT FINISHED
     * Get participation for a precise language for a contributor
     * @param languageName name of the language
     * @param gitId email of the contributor
     * @return
     */
/*    public ArrayList<Participation> findParticipationsByLanguageAndContributor(String languageName, String gitId) {
        Objects.requireNonNull(languageName);
        Objects.requireNonNull(gitId);
        return participationRepo.findParticipationsByLanguageAndContributor(languageName, gitId);
    }*/

    /**
     * NOT FINISHED
     * Get all participation of the contributor
     * @param gitId contributor mail
     * @return all participation of the contributor
     */
/*    public ArrayList<Participation> findParticipationsByContributor(String gitId){
        Objects.requireNonNull(gitId);
        return participationRepo.findParticipationsByContributor(gitId);
    }*/

    /**
     * return for a tag and a contributor all participation that he has done
     * @param nomTag tagName
     * @param gitId contributor Email
     * @return for a tag and a contributor all participation that he has done
     */
    public ArrayList<Participation> findParticipationsByTagAndContributor(String nomTag,String gitId){
        Objects.requireNonNull(gitId);
        Objects.requireNonNull(nomTag);
        return participationRepo.findParticipationsByTagAndContributeur(nomTag,gitId);
    }

    /**
     * return participation by project and contributor
     * @param project project link
     * @param gitId email of the contributor
     * @return participation by project and contributor
     */
    public ArrayList <Participation> findParticipationsByProjectAndContributor(String project, String gitId){
        Objects.requireNonNull(project);
        Objects.requireNonNull(gitId);
        return participationRepo.findParticipationsByProjectAndContributor(project,gitId);
    }
}
