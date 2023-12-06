package fr.uge.gitclout.gitclout.blame;


import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.TransportConfigCallback;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.URIish;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilsMethods {
    @SafeVarargs
    public static void checkNonNull(Object ... check) {
        Arrays.stream(check).forEach(e -> Objects.requireNonNull(e));
    }

    public static boolean isGitRepo(String url){
        Objects.requireNonNull(url);
        Pattern pattern = Pattern.compile("^(https?|git|ssh)://.+\\.git$");
        Matcher matcher = pattern.matcher(url);
        var match =matcher.find();
        if(!match){
            System.out.println("Ce n'est pas un repo git :  Les repos Git doivent etre de la forme https://<Pseudo>/<Projet>.git");
        }
        return match;
    }
}
