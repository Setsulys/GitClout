package fr.uge.gitclout.gitclout.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import jakarta.persistence.*;

import java.util.ArrayList;

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

    public ArrayList<Contributeur> findAllContributor(){
        return new ArrayList<>(contributeurRepo.findAll());
    }


}
