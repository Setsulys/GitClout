package fr.uge.gitclout.gitclout.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import jakarta.persistence.*;

@Service
public class ContributeurService {

    private ContributeurRepo contributeurRepo;
    @Autowired
    public ContributeurService(ContributeurRepo contributeurRepo){
        this.contributeurRepo = contributeurRepo;
    }


    public void insertContributeur(Contributeur con) {
        contributeurRepo.save(con);
    }


}
