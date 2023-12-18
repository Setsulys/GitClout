package fr.uge.gitclout.gitclout.blame;

import java.util.Objects;


public record FileExtension(String file, String extension) {


    public FileExtension{
        Objects.requireNonNull(file);
        Objects.requireNonNull(extension);
    }
    /**
     * Convert extensions to type of extensions
     * @param extension
     * @return
     */
    public static Extensions extensionDescription(String extension) {
        Objects.requireNonNull(extension);
        return switch(extension.toLowerCase()) {
            case "c" -> Extensions.C;
            case "h" -> Extensions.C;
            case "java" -> Extensions.JAVA;
            case "js" -> Extensions.JAVASCRIPT;
            case "html" -> Extensions.HTML;
            case "css" -> Extensions.CSS;
            case "py" -> Extensions.PYTHON;
            case "cpp" -> Extensions.CPLUSPLUS;
            case "hpp" -> Extensions.CPLUSPLUS;
            case "c++" -> Extensions.CPLUSPLUS;
            case "cc" -> Extensions.CPLUSPLUS;
            case "hh" -> Extensions.CPLUSPLUS;
            case "php" -> Extensions.PHP;
            case "ts" -> Extensions.TYPESCRPIPT;
            case "rb" -> Extensions.RUBY;
            case "cs" -> Extensions.CSHARP;

            case "xml" -> Extensions.BUILD;
            case "yml" -> Extensions.BUILD;

            case "md" -> Extensions.DOC;

            case "csv" -> Extensions.RESSOURCES;
            case "docx" -> Extensions.RESSOURCES;
            case "txt" -> Extensions.RESSOURCES;

            case "mp3" -> Extensions.MEDIA;
            case "mp4" -> Extensions.MEDIA;
            case "wav" -> Extensions.MEDIA;
            case "mkv" -> Extensions.MEDIA;
            case "jpg" -> Extensions.MEDIA;
            case "png" -> Extensions.MEDIA;
            case "jpeg" -> Extensions.MEDIA;
            case "webm" -> Extensions.MEDIA;
            case "jiff" -> Extensions.MEDIA;
            case "gif" -> Extensions.MEDIA;
            case "xls" -> Extensions.MEDIA;

            case "git" -> Extensions.CONFIGURATION;
            case "gitignore" -> Extensions.CONFIGURATION;
            case "makefile" -> Extensions.MAKEFILE;
            case "project" -> Extensions.CONFIGURATION;

            default -> Extensions.OTHER;
        };
    }


    @Override
    public String file() {
        return file;
    }

    @Override
    public String extension() {
        return extension;
    }

    @Override
    public String toString() {
        return file+" & " + extension ;
    }
}
