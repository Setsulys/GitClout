package fr.uge.gitclout.gitclout.jpa;

import fr.uge.gitclout.gitclout.blame.Blame;
import fr.uge.gitclout.gitclout.blame.Contributor;
import fr.uge.gitclout.gitclout.blame.StringWork;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Ref;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

@Service
public class DatabaseManager {

    private EntityManager entityManager;


    public DatabaseManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    private static final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);
    @Transactional
    public void fillDatabase(ArrayList<Blame> listo, String git, HashMap<Ref,java.sql.Date> map){

//        try {
//            if (entityManager == null) {
//                logger.error("EntityManager is null. Injection issue?");
//                return;
//            }

        for(int i = 0; i< listo.size();i++){

                Blame rec = listo.get(i);
                var datas = rec.blameDatas();
                var tago = rec.currentRef();
                for(int a = 0; a<datas.size();a++){

                    Contributeur con = new Contributeur();
                    con.setGitId(datas.get(a).contributor().mail());
                    con.setName(datas.get(a).contributor().name());

                    Fichier file = new Fichier();
                    file.setNomFichier(git+'/'+datas.get(a).file());
                    file.setLangage(StringWork.splitExtention(datas.get(a).file()).extension());

                    Tag tag = new Tag();
                    tag.setTagId(tago.toString());
                    tag.setProject(git);
                    tag.setNomTag(tago.getName());
                    tag.setDate(getDateFromRef(map,tago));

                    ParticipationPrimaryKey participationPK = new ParticipationPrimaryKey();
                    participationPK.setGitId(datas.get(a).contributor().mail());
                    participationPK.setFichierId(file.getFichierId());
                    participationPK.setTagId(tago.toString());

                    Participation participation = new Participation();
                    participation.setId(participationPK);
                    participation.setNbLignesCode(datas.get(a).lines());


                    entityManager.persist(con);
                    entityManager.persist(file);
                    entityManager.persist(tag);
                    entityManager.persist(participation);

                }

            }
//        } catch (Exception e) {
//            logger.error("Error in fillDatabase", e);
//        }

        }
    public static Date getDateFromRef(HashMap<Ref, Date> map,Ref ref){
        return map.get(ref);
    }

    }
