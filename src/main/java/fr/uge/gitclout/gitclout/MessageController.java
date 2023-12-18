package fr.uge.gitclout.gitclout;
import fr.uge.gitclout.gitclout.blame.*;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/app/rest")
public class MessageController {
    private final HashSet<String> gitProject  = new HashSet<>();
    @GetMapping("/hello")
    public String hello(){
        return "GITCLOUT DE JULIEN ET STEVEN";
    }

    @PostMapping("/toTheBack")
    public String displayLink(@RequestBody GitLinkRequest gitLinkRequest){
        String gitLink = gitLinkRequest.getGitLink();
        System.out.println(gitLink);
        JGitBlame jGit = new JGitBlame();
        var isRunnable = jGit.run(gitLink);
        if(isRunnable){
            gitProject.add(gitLink);
        }
         return "Données recu : " + gitLink;
    }

    @GetMapping("/getlink")
    @ResponseBody
    public List<String> displayGit(){
        return gitProject.stream().collect(Collectors.toList());
    }

}
