package fr.uge.gitclout.gitclout;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/messages")
public class MessageController {

    @GetMapping("/hello")
    public String hello(){
        return "GITCLOUT DE JULIEN ET STEVEN";
    }

    @PostMapping("/toTheBack")
    public String displayLink(@RequestBody GitLinkRequest gitLinkRequest){
        String gitLink = gitLinkRequest.getGitLink();
         return "Données recu : " + gitLink;
    }


}
