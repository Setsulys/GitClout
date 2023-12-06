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
        return switch(extension) {
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
            case "ml" -> Extensions.OCAML;
            case "rb" -> Extensions.RUBY;
            case "cs" -> Extensions.CSHARP;
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
        return "File : " + file+"."+ extension +"\n=>"+ file+" & " + extension ;
    }
}
