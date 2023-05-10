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

import Modele.Arbre;
import Modele.Jeu;
import Vue.InterfaceUtilisateur;


class JoueurHumain extends Joueur {

    int indexCarteMain = -1;

    JoueurHumain(int n, Jeu p) {
        super(n, p);
    }

    void ajouteInterfaceUtilisateur(InterfaceUtilisateur vue){
        this.vue = vue;
    }

    @Override
    int getEtape(){
        return jeu.getEtape();
    }

    void afficherPreSelection(){
        switch (jeu.getEtape()) {
            case -1:
                vue.selectionnerCarteContinuum(jeu.getContinuum().getIndexSorcierPossible(jeu.getCodex().getCouleurInterdite()));
                break;
            case -2:
                vue.selectionnerCarteContinuum(jeu.getContinuum().getIndexSorcierPossible(jeu.getCodex().getCouleurInterdite()));
                break;
            case 2:
                int debParInf = -1, finParInf = -1, debParSup = -1, finParSup = -1;
                if (jeu.existeParadoxSuperieur() && jeu.getInfoJoueurCourant().getDirection() == -1
                || jeu.existeParadoxInferieur() && jeu.getInfoJoueurCourant().getDirection() == +1){
                    debParInf = jeu.getInfoJoueurCourant().getSorcierIndice() - 3;
                    finParInf = jeu.getInfoJoueurCourant().getSorcierIndice();
                }
                if ((jeu.existeParadoxInferieur() && jeu.getInfoJoueurCourant().getDirection() == -1
                        || jeu.existeParadoxSuperieur() && jeu.getInfoJoueurCourant().getDirection() == +1)){
                    debParSup = jeu.getInfoJoueurCourant().getSorcierIndice() + 1;
                    finParSup = jeu.getInfoJoueurCourant().getSorcierIndice() + 4;
                }
                System.out.println(debParInf + " " + finParInf + " " + debParSup + " " + finParSup);
                vue.selectionnerParadox(debParInf, finParInf, debParSup, finParSup);
        }
    }

    @Override
    boolean jeu(int type, int indexCarte) {
//        Arbre a = new Arbre(this.jeu);
//        a.create();
//        Arbre temp = a.prochain_Coup();
        switch (type){
            case 1: //main
                return clicMain(indexCarte);
            case 2: //continuum
                if (clicContinuum(indexCarte)){
                    vue.selectionnerCarteContinuum(null);
                    return true;
                }
                return false;
            default:
                return false;
        }
    }

    boolean clicMain(int indexCarte){
        switch (jeu.getEtape()){
            case -1: //debut de jeu
            case -2: //debut de jeu
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
        switch (jeu.getEtape()){
            case -1: //debut de jeu
//                    System.out.println("Joueur " + jeu.getJoueurCourant() + " pose son sorcier en " + indexCarte);
            case -2: //debut de jeu
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
                            afficherPreSelection();
                            return getEtape() == 1;
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
                    vue.selectionnerParadox(-1, -1, -1, -1);
                    return getEtape() == 1;
                }
                else if (indexCarte < jeu.getInfoJoueurCourant().getSorcierIndice() && indexCarte >= jeu.getInfoJoueurCourant().getSorcierIndice()-3){
                    if (jeu.getJoueurCourant() == 0 && jeu.existeParadoxInferieur()){
                        jeu.coupParadox(-1);
                    }
                    else if (jeu.getJoueurCourant() == 1 && jeu.existeParadoxSuperieur()){
                        jeu.coupParadox(+1);
                    }
                    System.out.println("Paradox, selection des carte dans le passé");
                    vue.selectionnerParadox(-1, -1, -1, -1);
                    return true;
                }
                return false;
            default:
                return false;
        }
    }
}