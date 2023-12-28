package fr.uge.gitclout.gitclout.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import jakarta.persistence.*;

import java.util.ArrayList;

@Service
public class ContributeurService {
    /**
     * JPA Service class for contributor
     */
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

    public ArrayList<Contributeur> findContributorsByProject(String project) {
        return contributeurRepo.findContributorsByProject(project);
    }


}
