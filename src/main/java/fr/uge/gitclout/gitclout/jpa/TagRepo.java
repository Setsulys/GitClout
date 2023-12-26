package fr.uge.gitclout.gitclout.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface TagRepo extends JpaRepository<Tag, String> {
}
