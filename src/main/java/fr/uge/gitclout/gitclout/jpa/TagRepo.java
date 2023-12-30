package fr.uge.gitclout.gitclout.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;


public interface TagRepo extends JpaRepository<Tag, String> {

    @Query("SELECT DISTINCT t FROM Tag t WHERE t.project= :project")
    ArrayList<Tag> findByProject(String project);

}
