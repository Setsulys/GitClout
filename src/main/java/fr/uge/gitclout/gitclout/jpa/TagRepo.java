package fr.uge.gitclout.gitclout.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;


public interface TagRepo extends JpaRepository<Tag, String> {

    ArrayList<Tag> findByProject(String project);

}
