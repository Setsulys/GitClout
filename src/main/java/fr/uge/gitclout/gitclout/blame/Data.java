package fr.uge.gitclout.gitclout.blame;

import java.util.Objects;

import org.eclipse.jgit.lib.Ref;

public final class Data{

    private final Ref tag;
    private final Contributor contributor;

    private final Extensions extension;
    private int nbLine=0;

    public Data(Ref tag,Contributor contributor,Extensions extension) {
        Objects.requireNonNull(tag);
        Objects.requireNonNull(contributor);
        Objects.requireNonNull(extension);
        this.tag = tag;
        this.contributor=contributor;
        this.extension=extension;
    }

    /**
     * Permit to add a number of line to the data
     * @param lines number of lines added
     */
    public void addLines(int lines) {
        nbLine+=lines;
    }

    /**
     * USED IN DATABASEMANAGER
     * return the current number of line of this class
     * @return the current number of line of this class
     */
    public int nbLine() {
        return nbLine;
    }

    /**
     * Return the contributor of this class
     * @return the contributor of this class
     */
    public Contributor getContributor() {
        return contributor;
    }

    /**
     * return the extension of the data
     * @return the extension of the data
     */
    public Extensions getExtension() {
        return extension;
    }

    @Override
    public String toString() {
        return tag.getName()+": "+extension + "----" + contributor.name() /*+"("+contributor.mail()+")"*/+" :" + nbLine;
    }

}

