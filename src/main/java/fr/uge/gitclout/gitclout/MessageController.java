package fr.uge.gitclout.gitclout;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/app/messages")
public class MessageController {

    @GetMapping("/hello")
    public String hello(){
        return "GITCLOUT DE JULIEN ET STEVEN";
    }

    @GetMapping("/getLinkMessage")
    public String getlinkMessage(){
        return "Veuillez inserer votre lien Git";
    }
}
