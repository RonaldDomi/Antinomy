package Modele;

import java.util.LinkedList;

public class Historique {
    LinkedList<Jeu> listeJeu;

    // if we do undo, we will increment courrant.
    // courrant contains the number of steps we traced back

    // insert en tete
    int courrant=0;

    Historique(){
        listeJeu = new LinkedList<>();
    }
    Historique(Historique hist){
        this.courrant = hist.courrant;
        this.listeJeu = (LinkedList<Jeu>) hist.listeJeu.clone();
    }

    Historique(String stringHist){
//        jeu1!jeu2!jeu3!jeu4
        listeJeu = new LinkedList<>();

        String[] tabHist = stringHist.split("!");

        for (String s : tabHist) {
            listeJeu.add(new Jeu(s));
        }
    }

    Historique(Jeu jeu){
        listeJeu = new LinkedList<>();
        ajouterJeu(jeu);
    }


    public boolean peutAnnuler(){
        return courrant != listeJeu.size()-1;
    }


    public boolean peutRefaire(){
        return courrant > 0;
    }


    void ajouterJeu(Jeu jeu){

        // if we did a bunch of undos, we should remove them
        // being that we are rewriting history
        for (int k = 0; k < courrant; k++) {
            //remove first
            listeJeu.remove(0);
        }
        listeJeu.add(0, jeu);
        courrant = 0;

        System.out.println("Historique(" + listeJeu.size() + ", " + courrant + ") : ");
        System.out.println(listeJeu);
        System.out.println();
    }


    Jeu annuler(){
        if (!peutAnnuler()){
            System.err.println("Impossible d'annuler le coup. Aucun coup n'a été joué !");
            return null;
        }
        courrant++;
        Jeu dernierJoue = listeJeu.get(courrant);
        System.out.println("did undo");
        System.out.println("Historique(" + listeJeu.size() + ", " + courrant + ")");
        return dernierJoue;
    }


    Jeu refaire() {
        if (!peutRefaire()){
            System.err.println("Impossible de refaire le coup. Aucun coup n'a été annulé !");
            return null;
        }

        courrant--;
        Jeu aRefaire = listeJeu.get(courrant);
        System.out.println("did redo");
        System.out.println("Historique(" + listeJeu.size() + ", " + courrant + ")");
        return aRefaire;
    }

    public String toString(){
//        output format is as follows:
//        1st line: current jeu indice
//        2nd line: listJeu separated from !
//        jeu1!jeu2!jeu3!jeu4

        String outputList = "";
        for (Jeu jeu : listeJeu) {
            outputList += jeu.toString();
            outputList += "!";
        }
        return courrant + "\n" + outputList;
    }

    public Historique clone(){
        Historique h = new Historique();
        h.listeJeu = (LinkedList<Jeu>) listeJeu.clone();
        h.courrant = courrant;
        return h;
    }
}