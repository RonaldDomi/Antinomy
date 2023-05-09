package Controleur;
/*
 * Morpion pédagogique
 * Copyright (C) 2016 Guillaume Huard

 * Ce programme est libre, vous pouvez le redistribuer et/ou le
 * modifier selon les termes de la Licence Publique Générale GNU publiée par la
 * Free Software Foundation (version 2 ou bien toute autre version ultérieure
 * choisie par vous).

 * Ce programme est distribué car potentiellement utile, mais SANS
 * AUCUNE GARANTIE, ni explicite ni implicite, y compris les garanties de
 * commercialisation ou d'adaptation dans un but spécifique. Reportez-vous à la
 * Licence Publique Générale GNU pour plus de détails.

 * Vous devez avoir reçu une copie de la Licence Publique Générale
 * GNU en même temps que ce programme ; si ce n'est pas le cas, écrivez à la Free
 * Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307,
 * États-Unis.

 * Contact: Guillaume.Huard@imag.fr
 *          Laboratoire LIG
 *          700 avenue centrale
 *          Domaine universitaire
 *          38401 Saint Martin d'Hères
 */

import Modele.Carte;
import Modele.Jeu;
import Vue.InterfaceUtilisateur;

import java.util.LinkedList;
import java.util.Random;


class JoueurAIAleatoire extends Joueur {

    Random r;
    int indexCarteMain = -1;

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
    boolean tempsEcoule() {
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
                coup_choisi = coup_possible.get(r.nextInt(coup_possible.size()));

                while (!jeu.coupEchangeCarteMainContinuum(index_carte_main, coup_choisi)){
                    index_carte_main = r.nextInt(3);
                    carte_main = jeu.getInfoJoueurCourant().getCarteMain(index_carte_main);
                    coup_possible =  jeu.getContinuum().getCoupsPossibles(carte_main, jeu.getInfoJoueurCourant().getSorcierIndice(), jeu.getInfoJoueurCourant().getDirection());
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