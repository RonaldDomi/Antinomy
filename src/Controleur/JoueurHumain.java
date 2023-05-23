package Controleur;

import Modele.Jeu;
import Vue.InterfaceUtilisateur;

/**
 * <P> Définit un joueur comme joueur humain. </P>
 * <P> Les clics de souris seront pris en compte pour jouer au jeu</P>
 */
class JoueurHumain extends Joueur {

    int indexCarteMain = -1;

    JoueurHumain(int n, Jeu p) {
        super(n, p);
    }

    @Override
    void ajouteInterfaceUtilisateur(InterfaceUtilisateur vue){
        this.vue = vue;
    }

    @Override
    int getEtape(){
        return jeu.getEtape();
    }

    @Override
    void afficherPreSelection(){
        switch (jeu.getEtape()) {
            case -1:
                vue.selectionnerCarteContinuum(jeu.getContinuum().getIndexSorcierPossible(jeu.getCodex().getCouleurInterdite()));
                break;
            case -2:
                vue.selectionnerCarteContinuum(jeu.getContinuum().getIndexSorcierPossible(jeu.getCodex().getCouleurInterdite()));
                break;
            case 1:
                if (indexCarteMain == -1) {
                    vue.selectionnerMain(true);
                }
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
//                System.out.println(debParInf + " " + finParInf + " " + debParSup + " " + finParSup);
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
//                System.out.println("Joueur " + jeu.getJoueurCourant() + " selectionne dans ça main la carte d'index " + indexCarte);
                indexCarteMain = indexCarte;
                vue.selectionnerMain(false);
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
//                            System.out.println("Joueur " + jeu.getJoueurCourant() + " échange la carte de ça main " + indexCarteMain + " avec la carte du continuum " + indexCarte);
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
//                    System.out.println("Paradox, selection des carte dans le future");
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
//                    System.out.println("Paradox, selection des carte dans le passé");
                    vue.selectionnerParadox(-1, -1, -1, -1);
                    return true;
                }
                return false;
            default:
                return false;
        }
    }
}