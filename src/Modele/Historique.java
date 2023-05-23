package Modele;

import java.util.LinkedList;

/** <P> Historique des coups de chaque joueur. </P>
 *
 * <P> Permet d'annuler/refaire un coup. </P>
 *
 * <P> Représente avec une liste chaînée de Jeu, et un index pour connaître la position courante. </P>
* */
public class Historique {
    LinkedList<Jeu> listeJeu;

    // if we do undo, we will increment courrant.
    // courrant contains the number of steps we traced back

    // insert en tete
    int courrant=0;

    public Historique(){
        listeJeu = new LinkedList<>();
    }
    
    //TODO: DELETE?
    Historique(Historique hist){
        this.courrant = hist.courrant;
        this.listeJeu = (LinkedList<Jeu>) hist.listeJeu.clone();
    }
    
    /**
     * <P> Reconstruit un historique à partir de sa représentation textuelle. </P>
     *
     * @param stringHist Chaîne de caractères représentant une suite de jeu, séparés par un !, par exemple
     *                   jeu1!jeu2!jeu3
     */
    Historique(String stringHist){
//        jeu1!jeu2!jeu3!jeu4
        listeJeu = new LinkedList<>();

        String[] tabHist = stringHist.split("!");

        for (String s : tabHist) {
            listeJeu.add(new Jeu(s));
        }
    }

    /**
     * <P> Construit un nouveau historique avec un seul Jeu. </P>
     * @param jeu Jeu à ajouter à l'historique.
     * */
    public Historique(Jeu jeu){
        listeJeu = new LinkedList<>();
        ajouterJeu(jeu);
    }
    
    
    /**
     * Vérifie si on peut annuler un coup dans l'état actuel.
     * @return booléen indiquant si on peut annuler.
     */
    public boolean peutAnnuler(){
        return courrant != listeJeu.size()-1;
    }
    
    
    /**
     * Vérifie si on peut refaire un coup dans l'état actuel.
     * @return booléen indiquant si on peut refaire.
     */
    public boolean peutRefaire(){
        return courrant > 0;
    }
    
    
    /**
     * <P> Ajoute un jeu dans l'historique. </P>
     * <P> Supprime tous les Jeu situés après l'ajout dans la liste chaînée. (si il en existe) </P>
     * @param jeu Jeu à ajouter à l'historique.
     */
    public void ajouterJeu(Jeu jeu){

        // if we did a bunch of undos, we should remove them
        // being that we are rewriting history
        for (int k = 0; k < courrant; k++) {
            //remove first
            listeJeu.remove(0);
        }
        listeJeu.add(0, jeu);
        courrant = 0;

//        System.out.println("Historique(" + listeJeu.size() + ", " + courrant + ") : ");
//        System.out.println(listeJeu);
//        System.out.println();
    }


    /**
     * <P> Annule un coup, incrémente le pointeur de l'index courant et récupère le jeu à l'index. </P>
     *
     * @return Un jeu sans le dernier coup joué.
     * */
    Jeu annuler(){
        //Nécessite quand même une vérification car on peut cliquer sur un bouton grisé
        if (!peutAnnuler()){
            System.err.println("Impossible d'annuler le coup. Aucun coup n'a été joué !");
            return null;
        }
        courrant++;
        Jeu dernierJoue = listeJeu.get(courrant);
//        System.out.println("did undo");
//        System.out.println("Historique(" + listeJeu.size() + ", " + courrant + ")");
        return dernierJoue;
    }
    
    
    /**
     * <P> Refaire un coup, décrémente le pointeur de l'index courant et récupère le jeu à l'index. </P>
     *
     * @return Un jeu avec un coup récupéré.
     * */
    Jeu refaire() {
        //Nécessite quand même une vérification car on peut cliquer sur un bouton grisé
        if (!peutRefaire()){
            System.err.println("Impossible de refaire le coup. Aucun coup n'a été annulé !");
            return null;
        }

        courrant--;
        Jeu aRefaire = listeJeu.get(courrant);
//        System.out.println("did redo");
//        System.out.println("Historique(" + listeJeu.size() + ", " + courrant + ")");
        return aRefaire;
    }
    
    /**
     * <P> Représente sous forme textuelle l'historique. </P>
     * <P> En première ligne, indice de l'historique.
     * En seconde ligne la liste chaînée de Jeu, separés par des ! </P>
     *
     * @return Une chaîne de caractères représentant l'historique.
     */
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
    
    /**
     * Clone l'historique dans un nouvel Objet.
     *
     * @return L'historique cloné.
     */
    public Historique clone(){
        Historique h = new Historique();
        h.listeJeu = (LinkedList<Jeu>) listeJeu.clone();
        h.courrant = courrant;
        return h;
    }
}