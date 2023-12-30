package fr.uge.gitclout.gitclout.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;


public interface ContributeurRepo extends JpaRepository<Contributeur, String> {

    /**
     * NOT FINISHED, WANTED TO USE IT
     * Find all contributors of a precise project
     * @param project link of the project
     * @return list of all contributor
     */
/*    @Query("SELECT DISTINCT c FROM Contributeur c JOIN c.participation p WHERE p.tag.project = :project")
    ArrayList<Contributeur> findContributorsByProject( String project);*/
}


