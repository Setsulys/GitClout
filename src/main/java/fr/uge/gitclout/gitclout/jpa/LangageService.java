package fr.uge.gitclout.gitclout.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class LangageService {

    private LangageRepo langageRepo;

    @Autowired
    public LangageService(LangageRepo langageRepo){
        this.langageRepo = langageRepo;
    }


    /**
     * Insert a language in the database
     * @param langage language of a file
     */
    public void insertLangage(Langage langage) {
        this.langageRepo.save(langage);
    }

    /**
     * NOT FINISHED, WANTED TO USE IT
     * return all languages in the database
     * @return all languages in the database
     */
    /*public ArrayList<Langage> selectLangage(){
        return new ArrayList<>(langageRepo.findAll());
    }*/
}
