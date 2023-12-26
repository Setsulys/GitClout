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


    public void insertLangage(Langage langage) {
        this.langageRepo.save(langage);
    }

    public ArrayList<Langage> selectLangage(){
        return new ArrayList<>(langageRepo.findAll());
    }
}
