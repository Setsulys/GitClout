package fr.uge.gitclout.gitclout.blame;

import org.eclipse.jgit.lib.Ref;


public record Data(Ref tag,Contributor contributor,String file,int lines) {

    @Override
    public String toString() {
        return tag().getName()+": "+file + "----" + contributor.name() +" :" + lines;
    }

}


