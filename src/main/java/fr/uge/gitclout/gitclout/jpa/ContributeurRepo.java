package fr.uge.gitclout.gitclout.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;


public interface ContributeurRepo extends JpaRepository<Contributeur, String> {

    @Query("SELECT DISTINCT c FROM Contributeur c JOIN c.participation p WHERE p.tag.project = :project")
    ArrayList<Contributeur> findContributorsByProject( String project);
}


