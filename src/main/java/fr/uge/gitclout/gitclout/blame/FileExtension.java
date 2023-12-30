package fr.uge.gitclout.gitclout.blame;

import java.util.Objects;


public record FileExtension(String file, String extension) {


    public FileExtension{
        Objects.requireNonNull(file);
        Objects.requireNonNull(extension);
    }


    @Override
    public String toString() {
        return file+" & " + extension ;
    }
}
