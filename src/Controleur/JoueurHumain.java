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

import Modele.Jeu;
import Vue.InterfaceUtilisateur;


class JoueurHumain extends Joueur {

    int etape = 0;
    int indexCarteMain = -1;

    JoueurHumain(int n, Jeu p) {
        super(n, p);
    }

    void ajouteInterfaceUtilisateur(InterfaceUtilisateur vue){
        this.vue = vue;
    }

    @Override
    int getEtape(){
        return etape;
    }

    void afficherPreSelection(){
        switch (etape){
            case 0:
                vue.selectionnerCarteContinuum(jeu.getContinuum().getIndexSorcierPossible(jeu.getCodex().getCouleurInterdite()));
                break;
        }
    }

    @Override
    boolean jeu(int type, int indexCarte) {
        switch (type){
            case 1: //main
                if (clicMain(indexCarte))
                    return etapeSuivante();
                return false;
            case 2: //continuum
                if (clicContinuum(indexCarte)){
                    return etapeSuivante();
                }
                return false;
            default:
                return false;
        }
    }

    boolean clicMain(int indexCarte){
        switch (etape){
            case 0: //debut de jeu
                return false;
            case 1: //debut de tour
                System.out.println("Joueur " + jeu.getJoueurCourant() + " selectionne dans ça main la carte d'index " + indexCarte);
                indexCarteMain = indexCarte;
                vue.selectionnerCarteMain(indexCarte);
                vue.selectionnerCarteContinuum(jeu.getContinuum().getCoupsPossibles(jeu.getInfoJoueurCourant().getCarteMain(indexCarte), jeu.getInfoJoueurCourant().getSorcierIndice(), jeu.getInfoJoueurCourant().getDirection()));
                return false;
            default:
                return false;
        }
    }

    boolean clicContinuum(int indexCarte){
        switch (etape){
            case 0: //debut de jeu
//                    System.out.println("Joueur " + jeu.getJoueurCourant() + " pose son sorcier en " + indexCarte);
                return jeu.coupChangerPositionSorcier(indexCarte);
            case 1: //debut de tour
                if (indexCarteMain != -1){
                    java.util.LinkedList<Integer> indexPossible = jeu.getContinuum().getCoupsPossibles(jeu.getInfoJoueurCourant().getCarteMain(indexCarteMain), jeu.getInfoJoueurCourant().getSorcierIndice(), jeu.getInfoJoueurCourant().getDirection());
                    for (Integer index:
                            indexPossible) {
                        if (index == indexCarte){
                            System.out.println("Joueur " + jeu.getJoueurCourant() + " échange la carte de ça main " + indexCarteMain + " avec la carte du continuum " + indexCarte);
                            jeu.coupEchangeCarteMainContinuum(indexCarteMain, indexCarte);
                            indexCarteMain = -1;
                            vue.selectionnerCarteMain(-1);
                            vue.selectionnerCarteContinuum(null);
                            return true;
                        }
                    }
                }
                return false;
            case 2: //paradox droite/gauche
                if (indexCarte > jeu.getInfoJoueurCourant().getSorcierIndice() && indexCarte <= jeu.getInfoJoueurCourant().getSorcierIndice()+3){
                    if (jeu.getJoueurCourant() == 0 && jeu.existeParadoxSuperieur()){
                        jeu.coupParadox(+1);
                    }
                    else if (jeu.getJoueurCourant() == 1 && jeu.existeParadoxInferieur()){
                        jeu.coupParadox(-1);
                    }
                    System.out.println("Paradox, selection des carte dans le future");
                    return true;
                }
                else if (indexCarte < jeu.getInfoJoueurCourant().getSorcierIndice() && indexCarte >= jeu.getInfoJoueurCourant().getSorcierIndice()-3){
                    if (jeu.getJoueurCourant() == 0 && jeu.existeParadoxInferieur()){
                        jeu.coupParadox(-1);
                    }
                    else if (jeu.getJoueurCourant() == 1 && jeu.existeParadoxSuperieur()){
                        jeu.coupParadox(+1);
                    }
                    System.out.println("Paradox, selection des carte dans le passé");
                    return true;
                }
                return false;
            default:
                return false;
        }
    }

    boolean etapeSuivante() {
        switch (etape) {
            case (0):
                etape = 1;
                vue.selectionnerCarteContinuum(null);
                jeu.finTour();
                return true;
            case (1):
                if (jeu.getInfoJoueurCourant().existeParadox(jeu.getCodex().getCouleurInterdite())) {
                    System.out.println();
                    System.out.println("Paradox :");
                    etape = 2;
                    return false;
                } else if (jeu.existeClash()) {
                    System.out.println();
                    System.out.println("Clash :");
                    jeu.coupClash();
                    etape = 1;
                    System.out.println();
                    System.out.println("Debut Tour :");
                    jeu.finTour();
                    return true;
                } else {
                    etape = 1;
                    System.out.println();
                    System.out.println("Debut Tour :");
                    jeu.finTour();
                    return true;
                }
            case (2):
                if (jeu.existeClash()) {
                    System.out.println();
                    System.out.println("Clash :");
                    jeu.coupClash();
                    jeu.finTour();
                    System.out.println();
                    System.out.println("Debut Tour :");
                    return true;
                } else {
                    etape = 1;
                    jeu.finTour();
                    System.out.println();
                    System.out.println("Debut Tour :");
                    return true;
                }
            default:
                return false;
        }
    }
}