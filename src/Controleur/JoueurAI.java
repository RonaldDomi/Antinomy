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

import Global.Configuration;
import Modele.*;
import Vue.InterfaceUtilisateur;


class JoueurAI extends Joueur {

    int indexCarteMain = -1;

    JoueurAI(int n, Jeu p) {
        super(n, p);
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
        Coup bestCoup = arbre.getCoup(Configuration.profondeurIA, true, this.num);
        //Coup bestCoupF = arbre.getCoup(Configuration.profondeurIA, false);
        jeu.coupEchangeCarteMainContinuum(bestCoup.getIndexMain(), bestCoup.getIndexContinuum());
        System.out.println("la couleur interdite est " + jeu.getCodex().getCouleurInterdite());
        System.out.println("somme main IA : " + jeu.getInfoJoueurs()[jeu.getJoueurCourant()].sommeMain(jeu.getCodex().getCouleurInterdite()));
        System.out.println("somme main adversaire : " + jeu.getInfoJoueurs()[1-jeu.getJoueurCourant()].sommeMain(jeu.getCodex().getCouleurInterdite()));
        if(bestCoup.getParadox() != 0) jeu.coupParadox(bestCoup.getParadox());
        return true;
    }
}