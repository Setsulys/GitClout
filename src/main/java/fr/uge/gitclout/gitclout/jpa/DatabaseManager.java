package fr.uge.gitclout.gitclout.jpa;

import fr.uge.gitclout.gitclout.blame.*;
import fr.uge.gitclout.gitclout.blame.Blame;
import fr.uge.gitclout.gitclout.blame.Extensions;
import org.eclipse.jgit.lib.Ref;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

@Service
public class DatabaseManager {


    private ContributeurService contributeurService;

    private LangageService langageService;
    private TagService tagService;
    private ParticipationService participationService;



    @Autowired
    public DatabaseManager(ContributeurService contributeurService, LangageService langageService, TagService tagService, ParticipationService participationService) {
        Objects.requireNonNull(contributeurService);
        Objects.requireNonNull(langageService);
        Objects.requireNonNull(tagService);
        Objects.requireNonNull(participationService);
        this.participationService = participationService;
        this.tagService = tagService;
        this.langageService = langageService;
        this.contributeurService = contributeurService;
        fillLanguages();
    }

    /**
     * fill contributeur table
     * @param con new insert in contributeur
     * @param data data of current blame
     */
    private void fillContributor(Contributeur con,Data data){
        Objects.requireNonNull(data);
        con.setGitId(data.getContributor().mail());
        con.setName(data.getContributor().name());
        contributeurService.insertContributeur(con);
    }

    /**
     * fill tag table
     * @param tag new insert in tag
     * @param tago current tag
     * @param git string of the git
     * @param map map of the date of the tag
     */
    private void fillTag(Tag tag,Ref tago,String git, HashMap<Ref, java.sql.Date> map){
        Objects.requireNonNull(tag);
        Objects.requireNonNull(tag);
        Objects.requireNonNull(git);
        Objects.requireNonNull(map);
        tag.setTagId(tago.toString());
        tag.setProject(git);
        tag.setNomTag(tago.getName());
        tag.setDate(getDateFromRef(map, tago));
        tagService.insertTag(tag);
    }

    /**
     * fill participation primary key
     * @param participationPK new insert in participationPK
     * @param data data of the blame to get contributor
     * @param langage the language used
     * @param tago the tag used
     */
    private void fillParticipationPK(ParticipationPrimaryKey participationPK,Data data,Langage langage,Ref tago){
        Objects.requireNonNull(participationPK);
        Objects.requireNonNull(data);
        Objects.requireNonNull(langage);
        Objects.requireNonNull(tago);
        participationPK.setGitId(data.getContributor().mail());
        participationPK.setLanguageName(langage.getLanguageName());
        participationPK.setTagId(tago.toString());
    }

    /**
     *
     * @param tag table tag
     * @param con table contributeur
     * @param participationPK table participation PrimaryKey
     * @param langage language of the data inserted
     * @param data data inserted
     */
    private void fillParticipation(Tag tag,Contributeur con,ParticipationPrimaryKey participationPK,Langage langage,Data data){
        Objects.requireNonNull(tag);
        Objects.requireNonNull(con);
        Objects.requireNonNull(participationPK);
        Objects.requireNonNull(langage);
        Objects.requireNonNull(data);
        Participation participation = new Participation();
        participation.setTag(tag);
        participation.setContributeur(con);
        participation.setLangage(langage);
        participation.setId(participationPK);
        participation.setNbLignesCode(data.nbLine());
        participationService.insertParticipation(participation);
    }

    /**
     * main loop to fill data
     * @param tago current ref of the tag
     * @param data data to insert
     * @param git string of the git
     * @param map date to check for the tag
     */
    private void fillLoop(Ref tago,Data data,String git,HashMap<Ref, java.sql.Date> map){
        Objects.requireNonNull(data);
        Objects.requireNonNull(tago);
        Objects.requireNonNull(git);
        Objects.requireNonNull(map);
        Contributeur con = new Contributeur();
        fillContributor(con,data);
        Langage langage = new Langage();
        langage.setLangage(data.getExtension().toString());
        Tag tag = new Tag();
        fillTag(tag,tago,git,map);
        ParticipationPrimaryKey participationPK = new ParticipationPrimaryKey();
        fillParticipationPK(participationPK,data,langage,tago);
        fillParticipation(tag,con,participationPK,langage,data);
    }

    /**
     * Display the queries done in the database for self use
     */
    private void selfDisplay(){
        //        System.out.println(langageService.selectLangage());
        //        System.out.println(tagService.findTagsByProject("gitlab.com/Setsulys/the_light_corridor"));
        //        System.out.println(participationService.findParticipationsByLanguage("C"));
        //        System.out.println(participationService.findParticipationsByLanguageAndContributor("C","steven.ly412@gmail.com"));
        //        System.out.println(participationService.findParticipationsByContributor("steven.ly412@gmail.com"));
        System.out.println("MAP OF PART "+MapOfPartByTagAndProject("refs/tags/v2.0","gitlab.com/Setsulys/the_light_corridor"));
        System.out.println("MAP OF AVERAGE " + MapOfAverage("gitlab.com/Setsulys/the_light_corridor"));
        System.out.println("MAP OF FULL " + MapOfPartFull("gitlab.com/Setsulys/the_light_corridor"));
        System.out.println("TAGS " + getTags("gitlab.com/Setsulys/the_light_corridor"));
    }

    /**
     * fill all information in the database
     * @param listo list of analyzed files and contributor
     * @param git local path of the git
     * @param map map of the date of all tags
     */
    public void fillDatabase(ArrayList<Blame> listo, String git, HashMap<Ref, java.sql.Date> map) {
        Objects.requireNonNull(listo);
        Objects.requireNonNull(git);
        Objects.requireNonNull(map);
        for (var rec : listo) {
            var datas = rec.blameDatas();
            var tago = rec.currentRef();
            for (var data : datas) {
                if(data.nbLine() != 0){
                    fillLoop(tago,data,git,map);
                }
            }
        }
        selfDisplay();
    }

    /**
     * Fill all languages on what we work on the database
     */
    public void fillLanguages(){
        for(var ex : Extensions.values()){
            Langage langage = new Langage();
            langage.setLangage(ex.toString());
            langageService.insertLangage(langage);
        }
    }

    /**
     * Return a map of a language and number of lines in this language
     * @param listo list of participation by project and contributor
     * @return a map of a language and number of lines in this language
     */
    private HashMap<String,Integer> mapForLanguageCount(ArrayList<Participation> listo){
        Objects.requireNonNull(listo);
        var map = new HashMap<String,Integer>();
        for(var x : listo){
            if(map.containsKey(x.getLangage().getLanguageName())){
                map.put(x.getLangage().getLanguageName(),x.getLignes()+ map.get(x.getLangage().getLanguageName()));
            } else {
                map.put(x.getLangage().getLanguageName(),x.getLignes());
            }
        }
        return map;
    }

    /**
     * return a map of all contribution
     * @param project String link
     * @return a map of all contribution
     */
    public HashMap<String, ArrayList<Integer>> MapOfPartFull(String project){
        Objects.requireNonNull(project);
        var listContributor = contributeurService.findAllContributor();
        var mapFinal = new HashMap<String,ArrayList<Integer>>();
        for( var con : listContributor){
            var listo = participationService.findParticipationsByProjectAndContributor(project, con.getGitId());
            var map = mapForLanguageCount(listo);
            mapFinal.put(con.getName(),forFront(map));
        }
        return mapFinal;
    }


    /**
     * Return a map of contributor name and a map of language and number of lines
     * @param project String of the project link
     * @return a map of contributor name and a map of language and number of lines
     */
    public HashMap<String, HashMap<String ,Integer>> MapOfAverage(String project){
        Objects.requireNonNull(project);
        int nbTag = tagService.sizeOfTagsByProject(project);
        var listContributor = contributeurService.findAllContributor();
        var mapFinal = new HashMap<String, HashMap<String, Integer>>();
        for( var con : listContributor){
            var listo = participationService.findParticipationsByProjectAndContributor(project, con.getGitId());
            var map = mapForLanguageCount(listo);
            map.forEach((k,v) -> map.replace(k,v/nbTag));
            mapFinal.put(con.getName(),map);
        }
        return mapFinal;
    }

    /**
     * return the blaming info of the current tag of this project
     * @param nomTag name of the current tag
     * @param project name of the project
     * @return the blaming info of the current tag of this project
     */
    public HashMap<String,HashMap<String, ArrayList<Integer>>> MapOfPartByTagAndProject(String nomTag, String project){
        Objects.requireNonNull(project);
        Objects.requireNonNull(nomTag);
        var listContributor = contributeurService.findAllContributor();
        var mapFinal = new HashMap<String,HashMap<String, ArrayList<Integer>>>();
        for( var con : listContributor){
            var mapTag = new HashMap<String,ArrayList<Integer>>();
            var listo = participationService.findParticipationsByTagAndContributor(nomTag,con.getGitId());
            if(!listo.isEmpty()){
                var map = mapForLanguageCount(listo);
                mapTag.put(nomTag,forFront(map));
                mapFinal.put(con.getName(),mapTag);
            }
        }
        return mapFinal;
    }

    /**
     * Convert a map to an arraylist removing the other files
     * @param map of language and value
     * @return list of value in a precise order
     */
    public static ArrayList<Integer> forFront(HashMap<String,Integer> map){
        Objects.requireNonNull(map);
        var list = new ArrayList<Integer>();
        for( var x : Extensions.values()){
            if(x != Extensions.OTHER){
                var iter = map.get(x.toString());
                list.add(iter!=null?iter:0);
            }
        }
        return list;
    }


    /**
     * NOT FINISHED
     * Get the tags of one project
     * @param project name of the project
     * @return list of tags that are in the project
     */
    public ArrayList<String> getTags(String project){
        Objects.requireNonNull(project);
        var list = tagService.findAllTags();
        return new ArrayList<>(list.stream().map(Tag::getNomTag).toList());
    }


    /**
     * return the date of the current tag
     * @param map of Refs and it date
     * @param ref of actual tag
     * @return the date of the current tag
     */
    public static Date getDateFromRef(HashMap<Ref, Date> map,Ref ref){
        Objects.requireNonNull(map);
        Objects.requireNonNull(ref);
        return map.get(ref);
    }

}
