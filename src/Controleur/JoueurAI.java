package Controleur;


import Modele.*;
import Vue.InterfaceUtilisateur;

/**
 * Définit un joueur comme une IA Facile/Normale/Difficile.
 * Les clics ne sont donc pas pris en compte pour jouer au jeu.
 */
class JoueurAI extends Joueur {

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

        Arbre arbre = new Arbre(jeu, null, true);
        Coup bestCoup = arbre.getCoup(depth, true, this.num);
        jeu.coupEchangeCarteMainContinuum(bestCoup.getIndexMain(), bestCoup.getIndexContinuum());
        if(bestCoup.getParadox() != 0) jeu.coupParadox(bestCoup.getParadox());
        return true;
    }
}