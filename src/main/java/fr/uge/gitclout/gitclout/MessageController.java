package fr.uge.gitclout.gitclout;
import fr.uge.gitclout.gitclout.blame.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/rest")
public class MessageController {

    @GetMapping("/hello")
    public String hello(){
        return "GITCLOUT DE JULIEN ET STEVEN";
    }

    @PostMapping("/toTheBack")
    public String displayLink(@RequestBody GitLinkRequest gitLinkRequest){
        String gitLink = gitLinkRequest.getGitLink();
        System.out.println(gitLink);
        JGitBlame jGit = new JGitBlame();
        jGit.run(gitLink);
         return "Données recu : " + gitLink;
    }


}
