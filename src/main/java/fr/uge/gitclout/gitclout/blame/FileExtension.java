package fr.uge.gitclout.gitclout.blame;

import java.util.Objects;


public record FileExtension(String file, String extension) {


    public FileExtension{
        Objects.requireNonNull(file);
        Objects.requireNonNull(extension);
    }
    /**
     * Convert extensions to type of extensions
     * @param extension last part of the file, after the dot
     * @return an enum of the extension
     */
    public static Extensions extensionDescription(String extension) {
        Objects.requireNonNull(extension);
        return switch(extension.toLowerCase()) {
            case "c", "h" -> Extensions.C;
            case "java" -> Extensions.JAVA;
            case "js" -> Extensions.JAVASCRIPT;
            case "html" -> Extensions.HTML;
            case "css" -> Extensions.CSS;
            case "py" -> Extensions.PYTHON;
            case "cpp", "hpp", "c++", "hh", "cc" -> Extensions.CPLUSPLUS;
            case "php" -> Extensions.PHP;
            case "ts" -> Extensions.TYPESCRIPT;
            case "rb" -> Extensions.RUBY;
            case "cs" -> Extensions.CSHARP;

            case "xml", "yml" -> Extensions.BUILD;

            case "md" -> Extensions.DOC;

            case "csv", "docx", "txt" -> Extensions.RESSOURCES;

            case "mp3", "mp4", "wav", "mkv", "jpg", "png", "jpeg", "webm", "jiff", "gif", "xls" -> Extensions.MEDIA;

            case "git", "gitignore", "project" -> Extensions.CONFIGURATION;
            case "makefile" -> Extensions.MAKEFILE;

            default -> Extensions.OTHER;
        };
    }

    @Override
    public String toString() {
        return file+" & " + extension ;
    }
}
