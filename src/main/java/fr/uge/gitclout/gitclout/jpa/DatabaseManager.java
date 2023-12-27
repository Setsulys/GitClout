package fr.uge.gitclout.gitclout.jpa;

import fr.uge.gitclout.gitclout.blame.Blame;
import fr.uge.gitclout.gitclout.blame.Extensions;
import fr.uge.gitclout.gitclout.blame.FileExtension;
import fr.uge.gitclout.gitclout.blame.StringWork;
import org.eclipse.jgit.lib.Ref;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.SQLOutput;
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
        fillLanguages();
    }


    public void fillDatabase(ArrayList<Blame> listo, String git, HashMap<Ref, java.sql.Date> map) {


        for (var rec : listo) {
            var datas = rec.blameDatas();
            var tago = rec.currentRef();


            for (var data : datas) {
//                if(data == null){
//                    System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAHHHHHHHHHHH" + tago);
//                    continue;
//                }

                if(data.nbLine() == 0){
                    continue;
                }
                Contributeur con = new Contributeur();
                con.setGitId(data.getContributor().mail());
                con.setName(data.getContributor().name());
                contributeurService.insertContributeur(con);

                Langage langage = new Langage();
                langage.setLangage(data.getExtension().toString());

                Tag tag = new Tag();
                tag.setTagId(tago.toString());
                tag.setProject(git);
                tag.setNomTag(tago.getName());
                tag.setDate(getDateFromRef(map, tago));

                tagService.insertTag(tag);

                ParticipationPrimaryKey participationPK = new ParticipationPrimaryKey();
                participationPK.setGitId(data.getContributor().mail());
                participationPK.setLanguageName(langage.getLanguageName());
                participationPK.setTagId(tago.toString());

                Participation participation = new Participation();
                participation.setTag(tag);
                participation.setContributeur(con);
                participation.setLangage(langage);
                participation.setId(participationPK);
                participation.setNbLignesCode(data.nbLine());

                participationService.insertParticipation(participation);
                System.out.println("ici : " + participation);

            }

        }
//        System.out.println(langageService.selectLangage());
//        System.out.println(tagService.findTagsByProject("gitlab.com/Setsulys/the_light_corridor"));
//        System.out.println(participationService.findParticipationsByLanguage("C"));
//        System.out.println(participationService.findParticipationsByLanguageAndContributor("C","steven.ly412@gmail.com"));
//        System.out.println(participationService.findParticipationsByContributor("steven.ly412@gmail.com"));
//        System.out.println(MapOfPart());
    }

    public void fillLanguages(){
        for(var ex : Extensions.values()){
            Langage langage = new Langage();
            langage.setLangage(ex.toString());
            langageService.insertLangage(langage);
        }
    }

    public HashMap<String, ArrayList<Integer>> MapOfPart(){
        var listContributor = contributeurService.findAllContributor();
        var mapFinal = new HashMap<String,ArrayList<Integer>>();

        for( var con : listContributor){
            var map = new HashMap<String,Integer>();
            var listo = participationService.findParticipationsByContributor(con.getGitId());
            for( var x : listo){
                map.put(x.getLangage().getLanguageName(),x.getLignes());
            }
            mapFinal.put(con.getGitId(),forFront(map));
        }
        return mapFinal;
    }

    public static ArrayList<Integer> forFront(HashMap<String,Integer> map){
        var list = new ArrayList<Integer>();
        for( var x : Extensions.values()){
            if(x == Extensions.OTHER){
                continue;
            }
            var iter = map.get(x.toString());
            if(iter == null){
                list.add(0);
            } else {
                list.add(iter);
            }
        }
        return list;
    }





    public static Date getDateFromRef(HashMap<Ref, Date> map,Ref ref){
        return map.get(ref);
    }

    }
