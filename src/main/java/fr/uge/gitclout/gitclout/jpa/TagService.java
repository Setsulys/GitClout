package fr.uge.gitclout.gitclout.jpa;

import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class TagService {

    @PersistenceContext
    private EntityManager entityManager;


    public void insertTag(String TagId, String nomTag, String project) {
        Tag tag = new Tag();
        tag.setTagId(TagId);
        tag.setNomTag(nomTag);
        tag.setProject(project);
        if(entityManager.contains(tag)){
            return;
        }

        entityManager.persist(tag);
    }

    public void getAllContributeurs(String projectName) {
        var tags = entityManager.createQuery("SELECT t FROM Tag t WHERE t.project = :projectName", Tag.class)
                .setParameter("projectName", projectName)
                .getResultList();

        for (Tag tag : tags) {
            System.out.println(tag.getNomTag() + " - " + tag.getProject());
        }
    }

}
