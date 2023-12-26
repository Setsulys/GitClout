package fr.uge.gitclout.gitclout.jpa;

import fr.uge.gitclout.gitclout.blame.Blame;
import fr.uge.gitclout.gitclout.blame.FileExtension;
import fr.uge.gitclout.gitclout.blame.StringWork;
import org.eclipse.jgit.lib.Ref;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

@Service
public class DatabaseManager {


    private ContributeurService contributeurService;

    private LangageService langageService;
    private TagService tagService;
    private ParticipationService participationService;

    @Autowired
    public DatabaseManager(ContributeurService contributeurService, LangageService langageService, TagService tagService, ParticipationService participationService) {
        this.participationService = participationService;
        this.tagService = tagService;
        this.langageService = langageService;
        this.contributeurService = contributeurService;
    }


    public void fillDatabase(ArrayList<Blame> listo, String git, HashMap<Ref, java.sql.Date> map) {


        for (int i = 0; i < listo.size(); i++) {

            Blame rec = listo.get(i);
            var datas = rec.blameDatas();
            var tago = rec.currentRef();
            for (int a = 0; a < datas.size(); a++) {

                Contributeur con = new Contributeur();
                con.setGitId(datas.get(a).getContributor().mail());
                con.setName(datas.get(a).getContributor().name());
                contributeurService.insertContributeur(con);

                Langage langage = new Langage();
                langage.setLangage(datas.get(a).getExtension().toString());
                langageService.insertLangage(langage);

                Tag tag = new Tag();
                tag.setTagId(tago.toString());
                tag.setProject(git);
                tag.setNomTag(tago.getName());
                tag.setDate(getDateFromRef(map, tago));

                tagService.insertTag(tag);

                ParticipationPrimaryKey participationPK = new ParticipationPrimaryKey();
                participationPK.setGitId(datas.get(a).getContributor().mail());
                participationPK.setLanguageName(langage.getLanguageName());
                participationPK.setTagId(tago.toString());

                Participation participation = new Participation();
                participation.setId(participationPK);
                participation.setNbLignesCode(datas.get(a).nbLine());

                participationService.insertParticipation(participation);

            }

        }
        System.out.println(langageService.selectLangage());

    }





    public static Date getDateFromRef(HashMap<Ref, Date> map,Ref ref){
        return map.get(ref);
    }

    }
