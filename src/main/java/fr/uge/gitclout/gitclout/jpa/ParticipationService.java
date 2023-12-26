package fr.uge.gitclout.gitclout.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
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




}
