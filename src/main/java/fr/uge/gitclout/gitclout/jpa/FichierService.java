package fr.uge.gitclout.gitclout.jpa;

import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

@Service
public class FichierService {

    private EntityManager entityManager;


    public void insertFichier(String name, String langage) {
        Fichier file = new Fichier();
        file.setNomFichier(name);
        file.setLangage(langage);
        if(entityManager.contains(file)){
            return;
        }
        entityManager.persist(file);
    }

    public void getAllFichiers() {
        var files = entityManager.createQuery("SELECT c FROM Fichier c", Fichier.class).getResultList();

        for (Fichier file : files) {
            System.out.println(file.getFichierId() + " - " + file.getNomFichier());
        }
    }
}
