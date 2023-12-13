package fr.uge.gitclout.gitclout.blame;

import org.eclipse.jgit.lib.Ref;


public record Data(Ref tag,Contributor contributor,String file,int lines,int comments) {

    @Override
    public String toString() {
        return file + "\n" + contributor.name() +"\n Code line" + lines +"\n Comments line"+ comments ;
    }

    /**
     * return the contributor name and number of lines of codes and comments that he typed
     * @return the contributor name and number of lines of codes and comments that he typed
     */
    public String nameAndNumbers() {
        return contributor.name()+" : ("+lines +", "+comments+")";
    }
}

