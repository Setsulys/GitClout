package fr.uge.gitclout.gitclout.blame;

public record Contributor(String name,String mail) {

    @Override
    public String toString() {
        return name +" : " + mail;
    }
}
