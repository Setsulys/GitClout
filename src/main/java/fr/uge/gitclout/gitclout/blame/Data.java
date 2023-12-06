package fr.uge.gitclout.gitclout.blame;

import org.eclipse.jgit.lib.Ref;


public record Data(Ref tag,Contributor contributor,String file,int lines,int comments) {

    @Override
    public String toString() {
        return file + "\n" + contributor.name() +"\n Code line" + lines +"\n Comments line"+ comments ;
    }

    public String nameAndNumbers() {
        return contributor.name()+" : ("+lines +", "+comments+")";
    }
}

