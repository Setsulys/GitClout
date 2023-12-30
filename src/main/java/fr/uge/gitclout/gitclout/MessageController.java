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
    @GetMapping("/hello")
    public ResponseEntity<String> hello(){

        return ResponseEntity.ok("GITCLOUT DE JULIEN ET STEVEN");
    }

    @PostMapping("/toTheBack")
    public ResponseEntity<Boolean> displayLink(@RequestBody GitLinkRequest gitLinkRequest){
        return ResponseEntity.ok(back.tryAndAdd(gitLinkRequest.getGitLink()));
    }

    @RequestMapping("/percentFinished")
    public ResponseEntity<Double> percentFinished(@RequestBody GitLinkRequest gitLinkRequest){
        return ResponseEntity.ok(back.getPercentageOfProject(gitLinkRequest.getGitLink()));

    }

    @GetMapping("/getlink")
    @ResponseBody
    public ResponseEntity<List<String>> displayGit(){
        return ResponseEntity.ok(back.displayProjects());
    }

    @PostMapping("getTags")
    public ResponseEntity<ArrayList<String>> getTagOfProject(@RequestBody GitLinkRequest gitLinkRequest){
        System.out.println(back.getTagOfProject(gitLinkRequest.getGitLink()));
        return ResponseEntity.ok(back.getTagOfProject(gitLinkRequest.getGitLink()));
    }

    @PostMapping("/RadarData")
    public ResponseEntity<HashMap<String, HashMap<String, Integer>>> radarData(@RequestBody GitLinkRequest gitLinkRequest){
        return ResponseEntity.ok(back.radarData(gitLinkRequest.getGitLink()));
    }
}
