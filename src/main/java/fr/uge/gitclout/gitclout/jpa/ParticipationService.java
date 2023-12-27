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

    public ArrayList<Participation> findParticipationsByLanguage(String languageName) {
        return participationRepo.findParticipationsByLangage(languageName);
    }

    public ArrayList<Participation> findParticipationsByLanguageAndContributor(String languageName, String gitId) {
        return participationRepo.findParticipationsByLanguageAndContributor(languageName, gitId);
    }

    public ArrayList<Participation> findParticipationsByContributor(String gitId){
        return participationRepo.findParticipationsByContributor(gitId);
    }

    public ArrayList<Participation> findParticipationsByTagAndContributor(String nomTag,String gitId){
        return participationRepo.findParticipationsByTagAndContributeur(nomTag,gitId);
    }

    public ArrayList <Participation> findParticipationsByProjectAndContributor(String project, String gitId){
        return participationRepo.findParticipationsByProjectAndContributor(project,gitId);
    }




}
