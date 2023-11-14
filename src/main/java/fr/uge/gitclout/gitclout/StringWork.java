package fr.uge.gitclout.gitclout;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class StringWork {
    /**
     * get Files extensions
     * @param file
     * @return
     */
    public FileExtension splitExtention(String file) {
        Objects.requireNonNull(file);
        String[] split =file.split("[.]",2);
        if(split.length>1) {
            if(!split[0].isBlank()) {
                FileExtension fe = new FileExtension(split[0], split[1]);
                return fe;
            }
        }
        return null;
    }

    /**
     * create folder with URL name and project and return localpath of the folder
     * @param repositoryURL
     * @return
     * @throws IOException
     */
    public String localPathFromURI(String repositoryURL) throws IOException {
        Objects.requireNonNull(repositoryURL);
        String[] test = repositoryURL.split("/",5);
        //System.out.println(Arrays.stream(test).map(e -> e).collect(Collectors.joining(", ","[","]")));
        String name = test[test.length-2]; //get owner name
        String projectName = test[test.length-1].replace(".git", ""); // remove .git at the end of the string
        String localPath = Paths.get("").toAbsolutePath().toString() +"\\GitDataBase"+File.separator+name +File.separator+projectName;
        //System.out.println(localPath);
        Files.createDirectories(Paths.get(localPath));
        return localPath;
    }
}