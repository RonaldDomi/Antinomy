package Modele;

import java.util.LinkedList;

public class Historique {
    LinkedList<Jeu> listeJeu;
    int jeuIndex=0;

    Historique(){
        listeJeu = new LinkedList<>();
    }

    Historique(String stringHist){
//        jeu1!jeu2!jeu3!jeu4
        listeJeu = new LinkedList<>();

        String[] tabHist = stringHist.split("!");

        for (int i = 0; i < tabHist.length; i++) {
            listeJeu.add(new Jeu(tabHist[i]));
        }
    }


    public boolean peut_annuler(){
        return jeuIndex >= 1;
    }


    public boolean peut_refaire(){
        return jeuIndex < listeJeu.size();
    }


    void ajouter_jeu(Jeu jeu){
        listeJeu.add(jeuIndex, jeu);
        jeuIndex++;

        supprimer_suite_coup();
        System.out.println("Historique : ");
        System.out.println(listeJeu);
        System.out.println();
    }


    Jeu annuler_coup(){
        if (!peut_annuler()){
            System.err.println("Impossible d'annuler le coup. Aucun coup n'a été joué !");
            return null;
        }
        Jeu dernierJoue;
        jeuIndex--;
        dernierJoue = listeJeu.get(jeuIndex);
        return dernierJoue;
    }


    Jeu refaire_coup() {
        Jeu aRefaire;

        if (!peut_refaire()){
            System.err.println("Impossible de refaire le coup. Aucun coup n'a été annulé !");
            return null;
        }

        aRefaire = listeJeu.get(jeuIndex);
        jeuIndex++;

        return aRefaire;
    }


    void supprimer_suite_coup(){
        if (listeJeu.size() > jeuIndex) {
            listeJeu.subList(jeuIndex, listeJeu.size()).clear();
        }
    }

    public String toString(){
//        output format is as follows:
//        1st line: current jeu indice
//        2nd line: listJeu separated from !
//        jeu1!jeu2!jeu3!jeu4

        String outputList = "";
        for(int i = 0; i < listeJeu.size(); i++){
            outputList += listeJeu.get(i).toString();
            outputList += "!";
        }
        return jeuIndex + "\n" + outputList;
    }

    public Historique clone(){
        Historique h = new Historique();
        h.listeJeu = (LinkedList<Jeu>) listeJeu.clone();
        h.jeuIndex = jeuIndex;
        return h;
    }
}