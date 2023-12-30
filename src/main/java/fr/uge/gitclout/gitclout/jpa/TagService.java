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


    /**
     * Insert a tag in the table
     * @param tag tag
     */
    public void insertTag(Tag tag) {
        tagRepo.save(tag);
    }

    /**
     * find tags by project link
     * @param project project link
     * @return all tag linked to this project
     */
    public ArrayList<Tag> findTagsByProject(String project){
        return tagRepo.findByProject(project);
    }

    /**
     * find all tag in the database
     * @return all tag in the database
     */
    public ArrayList<Tag> findAllTags(){
        return new ArrayList<>(tagRepo.findAll());
    }

    /**
     * return the size of the tag list by project
     * @param project link of the project
     * @return size of the tag list by project
     */
    public int sizeOfTagsByProject(String project){
        return findTagsByProject(project).size();
    }

}
