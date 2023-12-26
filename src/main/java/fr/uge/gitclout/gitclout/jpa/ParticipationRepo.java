package fr.uge.gitclout.gitclout.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface ParticipationRepo extends JpaRepository<Participation,ParticipationPrimaryKey> {
}
