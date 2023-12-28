package fr.uge.gitclout.gitclout.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;

@Service
public class TagService {

    private TagRepo tagRepo;

    @Autowired
    public TagService(TagRepo tagRepo){
        this.tagRepo = tagRepo;
    }


    public void insertTag(Tag tag) {
        tagRepo.save(tag);
    }


    public ArrayList<Tag> findTagsByProject(String project){
        return tagRepo.findByProject(project);
    }

    public ArrayList<Tag> findAllTags(){
        return new ArrayList<>(tagRepo.findAll());
    }

    public int sizeOfTagsByProject(String project){
        return findTagsByProject(project).size();
    }

}
