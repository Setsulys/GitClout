package fr.uge.gitclout.gitclout.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;


public interface TagRepo extends JpaRepository<Tag, String> {

    /**
     * find all tag of a precise project
     * @param project link of the project
     * @return all tags of a precise project
     */
    @Query("SELECT DISTINCT t FROM Tag t WHERE t.project= :project")
    ArrayList<Tag> findByProject(String project);

}
