package fr.uge.gitclout.gitclout;

import fr.uge.gitclout.gitclout.jpa.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/app/rest")
public class MessageController {
    private final BackApplication back;

    @Autowired
    public MessageController(BackApplication back){
        this.back = back;
    }

    /**
     * Send title of project
     * @return title of project
     */
    @GetMapping("/hello")
    public ResponseEntity<String> hello(){
        return ResponseEntity.ok("GITCLOUT DE JULIEN ET STEVEN");
    }

    /**
     * return boolean if the git is valid after analysing
     * @param gitLinkRequest link of the git
     * @return boolean if the git is valid
     */
    @PostMapping("/toTheBack")
    public ResponseEntity<Boolean> displayLink(@RequestBody GitLinkRequest gitLinkRequest){
        return ResponseEntity.ok(back.tryAndAdd(gitLinkRequest.getGitLink()));
    }

    /**
     * send a double of the current percentage of task finished
     * @param gitLinkRequest link of the git
     * @return a double of the current percentage of task finished
     */
    @RequestMapping("/percentFinished")
    public ResponseEntity<Double> percentFinished(@RequestBody GitLinkRequest gitLinkRequest){
        return ResponseEntity.ok(back.getPercentageOfProject(gitLinkRequest.getGitLink()));

    }

    /**
     * display the valid git link to the front
     * @return list of valid git link
     */
    @GetMapping("/getlink")
    @ResponseBody
    public ResponseEntity<List<String>> displayGit(){
        return ResponseEntity.ok(back.displayProjects());
    }

    /**
     * send allTags of the project
     * @param gitLinkRequest link of the git
     * @return list of allTags
     */
    @PostMapping("getTags")
    public ResponseEntity<ArrayList<String>> getTagOfProject(@RequestBody GitLinkRequest gitLinkRequest){
        System.out.println(back.getTagOfProject(gitLinkRequest.getGitLink()));
        return ResponseEntity.ok(back.getTagOfProject(gitLinkRequest.getGitLink()));
    }

    /**
     * Rest api send map of map to the front with average value of contribution of all contributors
     * @param gitLinkRequest link of the git
     * @return a map of map to the front with average value of contribution of all contributors
     */
    @PostMapping("/RadarData")
    public ResponseEntity<HashMap<String, HashMap<String, Integer>>> radarData(@RequestBody GitLinkRequest gitLinkRequest){
        return ResponseEntity.ok(back.radarData(gitLinkRequest.getGitLink()));
    }
}
