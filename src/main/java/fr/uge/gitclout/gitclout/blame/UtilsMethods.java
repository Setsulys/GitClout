package fr.uge.gitclout.gitclout.blame;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilsMethods {
    /**
     * Check if the current url is a git repos
     * @param url string of the url
     * @return true if it is a git repos false otherwise
     */
    public static boolean isGitRepo(String url){
        Objects.requireNonNull(url);
        Pattern pattern = Pattern.compile("^(https?|git|ssh)://.+\\.git$");
        Matcher matcher = pattern.matcher(url);
        return matcher.find();
    }
}
