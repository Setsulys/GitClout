package fr.uge.gitclout.gitclout;


import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/app/rest")
public class MessageController {
    private final BackApplication back = new BackApplication();
    @GetMapping("/hello")
    public String hello(){
        return "GITCLOUT DE JULIEN ET STEVEN";
    }

    @RequestMapping("/toTheBack")
    public boolean displayLink(@RequestBody GitLinkRequest gitLinkRequest){
        String gitLink = gitLinkRequest.getGitLink();
        return back.tryAndAdd(gitLink);
    }


    @GetMapping("/getlink")
    @ResponseBody
    public List<String> displayGit(){
        return back.displayProjects();
    }

}
