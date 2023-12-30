package fr.uge.gitclout.gitclout.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

@Service
public class ParticipationService {

    private ParticipationRepo participationRepo;

    @Autowired
    public ParticipationService(ParticipationRepo participationRepo){
        this.participationRepo = participationRepo;
    }


    public void insertParticipation(Participation participation) {
        participationRepo.save(participation);
    }

    public ArrayList<Participation> findall(){
        return new ArrayList<>(participationRepo.findAll());
    }

    /**
     * NOT FINISHED
     * Get all participation of a precise language
     * @param languageName name of the language
     * @return all participation of a precise language
     */
/*    public ArrayList<Participation> findParticipationsByLanguage(String languageName) {
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
        return participationRepo.findParticipationsByLanguageAndContributor(languageName, gitId);
    }*/

    /**
     * NOT FINISHED
     * Get all participation of the contributor
     * @param gitId contributor mail
     * @return all participation of the contributor
     */
/*    public ArrayList<Participation> findParticipationsByContributor(String gitId){
        return participationRepo.findParticipationsByContributor(gitId);
    }*/

    /**
     * return for a tag and a contributor all participation that he has done
     * @param nomTag tagName
     * @param gitId contributor Email
     * @return for a tag and a contributor all participation that he has done
     */
    public ArrayList<Participation> findParticipationsByTagAndContributor(String nomTag,String gitId){
        return participationRepo.findParticipationsByTagAndContributeur(nomTag,gitId);
    }

    /**
     *
     * @param project
     * @param gitId
     * @return
     */
    public ArrayList <Participation> findParticipationsByProjectAndContributor(String project, String gitId){
        return participationRepo.findParticipationsByProjectAndContributor(project,gitId);
    }





}
