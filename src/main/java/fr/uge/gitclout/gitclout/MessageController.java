package fr.uge.gitclout.gitclout;
import fr.uge.gitclout.gitclout.blame.*;
import fr.uge.gitclout.gitclout.jpa.DatabaseManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/rest")
public class MessageController {

    private final DatabaseManager databaseManager;

    // Inject DatabaseManager using constructor injection
    @Autowired
    public MessageController(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

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

        databaseManager.fillDatabase(jGit.projectData(),jGit.getGit(),jGit.getDateMap());
         return "Données recu : " + gitLink;
    }


}
