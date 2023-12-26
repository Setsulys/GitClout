package fr.uge.gitclout.gitclout.blame;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class StringWork {

    /**
     * get Files extensions
     * @param file split the file and put it in a record
     * @return the file and it extension in a record
     */
    public static FileExtension splitExtention(String file) {
        Objects.requireNonNull(file);
        String[] split =file.split("[.]",2);
        //System.out.println(Arrays.asList(split).stream().collect(Collectors.joining(",")));
        if(split.length>1) {
            return split[0].isBlank()? new FileExtension(split[1], split[1]) : new FileExtension(split[0], split[1]);
        }
        return new FileExtension(split[0], split[0]);
    }

    /**
     * create folder with URL name and project and return localpath of the folder
     * @param repositoryURL string of the git url
     * @return localpath local path of the git
     * @throws IOException exception
     */
    public String localPathFromURI(String repositoryURL) throws IOException {
        Objects.requireNonNull(repositoryURL);
        String[] test = repositoryURL.split("/",5);
        //System.out.println(Arrays.stream(test).map(e -> e).collect(Collectors.joining(", ","[","]")));
        String gitName = test[test.length-3];
        String name = test[test.length-2]; //get owner name
        String projectName = test[test.length-1].replace(".git", ""); // remove .git at the end of the string
        String localPath = Paths.get("").toAbsolutePath().toString() +File.separator+"GitDataBase"+File.separator + gitName + File.separator+name +File.separator+projectName;
        //System.out.println(localPath);
        Files.createDirectories(Paths.get(localPath));
        return localPath;
    }
}
