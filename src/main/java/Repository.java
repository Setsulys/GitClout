import fr.uge.gitclout.gitclout.GitcloutApplication;
import org.springframework.boot.SpringApplication;

public class Repository {

//    public static void main(String[] args) {
//
//        // Path to the Git repository
//        String repositoryPath = "/path/to/your/repository";
//
//        try {
//            // Execute 'git show-ref --tags' command to list tags
//            ProcessBuilder processBuilder = new ProcessBuilder("git", "show-ref", "--tags");
//            processBuilder.directory(new File(repositoryPath));
//            Process process = processBuilder.start();
//
//            // Read the output of the command
//            InputStream inputStream = process.getInputStream();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//            String line;
//
//            while ((line = reader.readLine()) != null) {
//                // Each line represents a tag reference
//                System.out.println(line);
//            }
//
//            // Check for errors
//            int exitCode = process.waitFor();
//            if (exitCode != 0) {
//                // Handle error
//                InputStream errorStream = process.getErrorStream();
//                BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
//                String errorLine;
//                while ((errorLine = errorReader.readLine()) != null) {
//                    System.err.println(errorLine);
//                }
//            }
//
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//    }

}
