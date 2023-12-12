package fr.uge.gitclout.gitclout.jpa;

import org.springframework.stereotype.Service;


import javax.persistence.*;

@Service
public class ContributeurService {

    @PersistenceContext
    private EntityManager entityManager;


    public void insertContributeur(String gitId, String name) {
        Contributeur contributeur = new Contributeur();
        contributeur.setGitId(gitId);
        contributeur.setName(name);

        entityManager.persist(contributeur);
    }

    public void getData() {
        var contributeurs = entityManager.createQuery("SELECT c FROM Contributeur c", Contributeur.class).getResultList();

        for (Contributeur contributeur : contributeurs) {
            System.out.println(contributeur.getName() + " - " + contributeur.getGitId());
        }
    }

}
