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
    public void addLines(int lines) {
        nbLine+=lines;
    }

    public int nbLine() {
        return nbLine;
    }

    @Override
    public String toString() {
        return tag.getName()+": "+extension + "----" + contributor.name() /*+"("+contributor.mail()+")"*/+" :" + nbLine;
    }

}

