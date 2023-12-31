package fr.uge.gitclout.gitclout.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/*import jakarta.persistence.*;*/

import java.util.ArrayList;
import java.util.Objects;

@Service
public class ContributeurService {
    /**
     * JPA Service class for contributor
     */
    private ContributeurRepo contributeurRepo;
    @Autowired
    public ContributeurService(ContributeurRepo contributeurRepo){
        Objects.requireNonNull(contributeurRepo);
        this.contributeurRepo = contributeurRepo;
    }


    public void insertContributeur(Contributeur con) {
        Objects.requireNonNull(con);
        contributeurRepo.save(con);
    }

    /**
     * Find all contributors in database
     * @return all contributors in database
     */
    public ArrayList<Contributeur> findAllContributor(){
        return new ArrayList<>(contributeurRepo.findAll());
    }

    /**
     * NOT FINISHED, WANTED TO USE IT
     * find all contributor for  a précise project
     * @param project link of the project
     * @return a list of contributors
     */
/*    public ArrayList<Contributeur> findContributorsByProject(String project) {
        return contributeurRepo.findContributorsByProject(project);
    }*/


}
