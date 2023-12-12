package fr.uge.gitclout.gitclout.jpa;

import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Service
public class ParticipationService {

    @PersistenceContext
    private EntityManager entityManager;


    public void insertParticipation(String gitId, String TagId, int fichierId, int nbLignesCode, int nbLignesCom) {
        Participation participation = new Participation();
        ParticipationPrimaryKey id = new ParticipationPrimaryKey();
        id.setGitId(gitId);
        id.setTagId(TagId);
        id.setFichierId(fichierId);
        participation.setId(id);
        participation.setNbLignesCode(nbLignesCode);
        participation.setNbLignesCom(nbLignesCom);

        entityManager.persist(participation);
    }

    public List<Object[]> getData(String tagParameter) {
        String jpql = "SELECT c.name, t.nomTag, f.nomFichier, p.nbLignesCode, p.nbLignesCom " +
                "FROM Participation p " +
                "JOIN Contributeur c ON p.id.gitId = c.gitId " +
                "JOIN Tag t ON p.id.tagId = t.TagId " +
                "JOIN Fichier f ON p.id.fichierId = f.fichierId " +
                "WHERE t.nomTag = :tagParameter";

        TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);
        query.setParameter("tagParameter", tagParameter);

        return query.getResultList();
    }




}
