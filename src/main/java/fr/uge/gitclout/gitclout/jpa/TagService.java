package fr.uge.gitclout.gitclout.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

@Service
public class TagService {

    private TagRepo tagRepo;

    @Autowired
    public TagService(TagRepo tagRepo){
        Objects.requireNonNull(tagRepo);
        this.tagRepo = tagRepo;
    }


    /**
     * Insert a tag in the table
     * @param tag tag
     */
    public void insertTag(Tag tag) {
        Objects.requireNonNull(tag);
        tagRepo.save(tag);
    }

    /**
     * find tags by project link
     * @param project project link
     * @return all tag linked to this project
     */
    public ArrayList<Tag> findTagsByProject(String project){
        Objects.requireNonNull(project);
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
        Objects.requireNonNull(project);
        return findTagsByProject(project).size();
    }

}
