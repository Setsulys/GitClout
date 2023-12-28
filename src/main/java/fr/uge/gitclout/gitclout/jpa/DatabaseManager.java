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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DatabaseManager {
    /**
     * Main Service of JPA
     */

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

    /**
     * Method which fill the database
     * @param listo An ArrayList of Blame
     * @param git String of the project
     * @param map Map of date
     */
    public void fillDatabase(ArrayList<Blame> listo, String git, HashMap<Ref, java.sql.Date> map) {
        for (var rec : listo) {
            var datas = rec.blameDatas();
            var tago = rec.currentRef();
            for (var data : datas) {
                if(data.nbLine() == 0){
                    continue;
                }
                Contributeur con = new Contributeur(data.getContributor().mail(),data.getContributor().name());
                contributeurService.insertContributeur(con);
                Langage langage = new Langage(data.getExtension().toString());
                Tag tag = new Tag(tago.toString(),git,tago.getName(),getDateFromRef(map, tago));
                tagService.insertTag(tag);
                ParticipationPrimaryKey participationPK = new ParticipationPrimaryKey(data.getContributor().mail(),tago.toString(),langage.getLanguageName());
                Participation participation = new Participation(participationPK,data.nbLine(),con,langage,tag);
                participationService.insertParticipation(participation);
            }
        }
//        System.out.println(langageService.selectLangage());
//        System.out.println(tagService.findTagsByProject("gitlab.com/Setsulys/the_light_corridor"));
//        System.out.println(participationService.findParticipationsByLanguage("C"));
//        System.out.println(participationService.findParticipationsByLanguageAndContributor("C","steven.ly412@gmail.com"));
//        System.out.println(participationService.findParticipationsByContributor("steven.ly412@gmail.com"));
//        System.out.println(MapOfPartByTagAndProject("refs/tags/v2.0","gitlab.com/Setsulys/the_light_corridor"));
//        System.out.println("GNEEEE" + MapOfAverage("gitlab.com/Setsulys/the_light_corridor"));
//        System.out.println("GNEEEE " + MapOfPartFull("gitlab.com/Setsulys/the_light_corridor"));
    }

    /**
     * Method which fills the first 10 languages in the database
     */
    public void fillLanguages(){
        for(var ex : Extensions.values()){
            Langage langage = new Langage();
            langage.setLangage(ex.toString());
            langageService.insertLangage(langage);
        }
    }

    /**
     * Method which returns a map containing the number of lines made by languages over contributors for one project
     * @param project The project
     * @return The map containing data
     */
    public HashMap<String, ArrayList<Integer>> MapOfPartFull(String project){
        var listContributor = contributeurService.findAllContributor();
        var mapFinal = new HashMap<String,ArrayList<Integer>>();

        for( var con : listContributor){
            var map = new HashMap<String,Integer>();
            var listo = participationService.findParticipationsByProjectAndContributor(project, con.getGitId());
            arrayListo(map,listo);
            mapFinal.put(con.getName(),forFront(map));
        }
        return mapFinal;
    }


    /**
     * Method which returns a map containing the average of lines by language by contributor over a project
     * @param project The project
     * @return The map containing data
     */
    public HashMap<String, HashMap<String ,Integer>> MapOfAverage(String project){
        int nbTag = tagService.sizeOfTagsByProject(project);
        var listContributor = contributeurService.findAllContributor();
        var mapFinal = new HashMap<String, HashMap<String, Integer>>();

        for( var con : listContributor){
            var map = new HashMap<String,Integer>();
            var mapTag = new HashMap<String,ArrayList<Integer>>();
            var listo = participationService.findParticipationsByProjectAndContributor(project, con.getGitId());

            arrayListo(map,listo);
            map.forEach((k,v) -> map.replace(k,v/nbTag));
            mapFinal.put(con.getName(),map);
        }
        return mapFinal;


    }

    /**
     * Method which returns a map containing the number of lines by contributors by languages over a tag of a project
     * @param nomTag The tag
     * @param project The project
     * @return The map containing data
     */
    public HashMap<String,HashMap<String, ArrayList<Integer>>> MapOfPartByTagAndProject(String nomTag, String project){
        var listContributor = contributeurService.findAllContributor();

        var mapFinal = new HashMap<String,HashMap<String, ArrayList<Integer>>>();

        for( var con : listContributor){
            var map = new HashMap<String,Integer>();
            var mapTag = new HashMap<String,ArrayList<Integer>>();
            var listo = participationService.findParticipationsByTagAndContributor(nomTag,con.getGitId());
            if(listo.isEmpty()){
                continue;
            }
            arrayListo(map,listo);
            mapTag.put(nomTag,forFront(map));
            mapFinal.put(con.getName(),mapTag);
        }
        return mapFinal;
    }

    /**
     * Method to arrange data to facilitate the front work
     * @param map
     * @return
     */
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

    public static HashMap<String,Integer> arrayListo(HashMap<String,Integer> map, ArrayList<Participation> listo){
        for( var x : listo){
            if(map.containsKey(x.getLangage().getLanguageName())){
                map.put(x.getLangage().getLanguageName(),x.getLignes() + map.get(x.getLangage().getLanguageName()));
            } else {
                map.put(x.getLangage().getLanguageName(),x.getLignes());
            }
        }
        return map;
    }

    public ArrayList<Tag> retreiveTags(){
      return tagService.findAllTags();
    }





    public static Date getDateFromRef(HashMap<Ref, Date> map,Ref ref){
        return map.get(ref);
    }

    }
