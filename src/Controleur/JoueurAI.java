package Controleur;


import Global.Configuration;
import Modele.*;
import Vue.InterfaceUtilisateur;

/**
 * Définit un joueur comme une IA Facile/Normale/Difficile.
 * Les clics ne sont donc pas pris en compte pour jouer au jeu.
 */
class JoueurAI extends Joueur {

    int indexCarteMain = -1;
    int depth;

    JoueurAI(int n, Jeu p, int depth) {
        super(n, p);
        this.depth = depth;
    }

    void ajouteInterfaceUtilisateur(InterfaceUtilisateur vue){
        this.vue = vue;
    }

    @Override
    int getEtape(){
        return jeu.getEtape();
    }

    @Override
    public boolean tempsEcoule() {
        //jouer indice sorcier
        switch (getEtape()) {
            case -2:
            case -1:
                int sorcier = jeu.getContinuum().getIndexSorcierPossible(jeu.getCodex().getCouleurInterdite()).get(0);
                jeu.coupChangerPositionSorcier(sorcier);
                return true;
        }

//        Arbre arbre = new ArbrePapa(jeu, 4);
//        Statistics.setNbTotalConfigurations(0);
//        arbre.create(4);

//        System.out.println("total for this configuration: " + Statistics.getNbTotalConfigurations());

        Arbre arbre = new Arbre(jeu, null, true);
//        Coup bestCoup = arbre.getCoup(Configuration.profondeurIA, true, this.num);
        Coup bestCoup = arbre.getCoup(depth, true, this.num);
        //Coup bestCoupF = arbre.getCoup(Configuration.profondeurIA, false);
        jeu.coupEchangeCarteMainContinuum(bestCoup.getIndexMain(), bestCoup.getIndexContinuum());
//        System.out.println("la couleur interdite est " + jeu.getCodex().getCouleurInterdite());
//        System.out.println("somme main IA : " + jeu.getInfoJoueurs()[jeu.getJoueurCourant()].sommeMain(jeu.getCodex().getCouleurInterdite()));
//        System.out.println("somme main adversaire : " + jeu.getInfoJoueurs()[1-jeu.getJoueurCourant()].sommeMain(jeu.getCodex().getCouleurInterdite()));
        if(bestCoup.getParadox() != 0) jeu.coupParadox(bestCoup.getParadox());
        return true;
    }
}