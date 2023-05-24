package Controleur;

import Modele.Carte;
import Modele.Jeu;
import Vue.InterfaceUtilisateur;

import java.util.LinkedList;
import java.util.Random;

/**
 * Définit un joueur comme une IA Aléatoire.
 * Les clics ne sont donc pas pris en compte pour jouer au jeu.
 */
class JoueurAIAleatoire extends Joueur {

    Random r;

    JoueurAIAleatoire(int n, Jeu p) {
        super(n, p);
        r = new Random();
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
        LinkedList<Integer> coup_possible;
        Integer coup_choisi;
        switch (getEtape()){
            case -1:
            case -2:
                coup_possible =  jeu.getContinuum().getIndexSorcierPossible(jeu.getCodex().getCouleurInterdite());
                coup_choisi = coup_possible.get(r.nextInt(coup_possible.size()));
                while (!jeu.coupChangerPositionSorcier(coup_choisi)){
                    coup_choisi = coup_possible.get(r.nextInt(coup_possible.size()));
                }
                return true;
            case 1:
                int index_carte_main = r.nextInt(3);
                Carte carte_main = jeu.getInfoJoueurCourant().getCarteMain(index_carte_main);
                coup_possible =  jeu.getContinuum().getCoupsPossibles(carte_main, jeu.getInfoJoueurCourant().getSorcierIndice(), jeu.getInfoJoueurCourant().getDirection());

                while (coup_possible.size() == 0){
                    index_carte_main = r.nextInt(3);
                    carte_main = jeu.getInfoJoueurCourant().getCarteMain(index_carte_main);
                    coup_possible =  jeu.getContinuum().getCoupsPossibles(carte_main, jeu.getInfoJoueurCourant().getSorcierIndice(), jeu.getInfoJoueurCourant().getDirection());
                }

                coup_choisi = coup_possible.get(r.nextInt(coup_possible.size()));

                while (!jeu.coupEchangeCarteMainContinuum(index_carte_main, coup_choisi)){
                    while (coup_possible.size() == 0 ){
                        index_carte_main = r.nextInt(3);
                        carte_main = jeu.getInfoJoueurCourant().getCarteMain(index_carte_main);
                        coup_possible =  jeu.getContinuum().getCoupsPossibles(carte_main, jeu.getInfoJoueurCourant().getSorcierIndice(), jeu.getInfoJoueurCourant().getDirection());
                    }
                    coup_choisi = coup_possible.get(r.nextInt(coup_possible.size()));
                }

                if (getEtape() != 1)
                    return false;

                return true;
            case 2:
                if (jeu.existeParadoxSuperieur() && jeu.existeParadoxInferieur()){
                    jeu.coupParadox(-1 + 2 * r.nextInt(1));
                }
                else if (jeu.existeParadoxInferieur()){
                    jeu.coupParadox(-1);
                }
                else {
                    jeu.coupParadox(1);
                }

                if (getEtape() != 1)
                    return false;

                return true;
            case 3:
                jeu.coupClash();
                return true;
        }
        return false;
    }
}