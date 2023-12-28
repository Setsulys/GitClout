package fr.uge.gitclout.gitclout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public String hello(){
        return "GITCLOUT DE JULIEN ET STEVEN";
    }

    @RequestMapping("/toTheBack")
    public boolean displayLink(@RequestBody GitLinkRequest gitLinkRequest){
        String gitLink = gitLinkRequest.getGitLink();
        return back.tryAndAdd(gitLink);
    }

    @RequestMapping("/percentFinished")
    public double percentFinished(@RequestBody GitLinkRequest gitLinkRequest){
        String gitLink = gitLinkRequest.getGitLink();
        return back.getPercentageOfProject(gitLink);

    }

    @GetMapping("/getlink")
    @ResponseBody
    public List<String> displayGit(){
        return back.displayProjects();
    }


    @RequestMapping("/RadarData")
    public HashMap<String, HashMap<String, Integer>> radarData(@RequestBody GitLinkRequest gitLinkRequest){
        String gitLink = gitLinkRequest.getGitLink();
        return back.radarData(gitLink);
    }
}
