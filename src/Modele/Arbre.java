package Modele;

import java.util.ArrayList;
import java.util.List;

public class Arbre {
    Jeu j;
    List<Arbre> fils;

    Arbre(Jeu j){
        this.j = j;
        fils = new ArrayList<>();
    }

    public void create(){
        //condition arret
        //ici ce jeu est une feuille
        if (this.j.joueurGagnant == 1)
            return;

        Jeu temp2 = null, temp3 = null, temp4 = null, temp5 = null, temp6 = null;
        //etape 1
        //pour chaque carte choisie
        for (int i = 0; i < 3; i++) {
            //pour chaque coup avec chaque carte
            List<Integer> cpPossible = j.continuum.getCoupsPossibles(j.getMainJoueurCourant()[i], j.getInfoJoueurCourant().getSorcierIndice(), j.getInfoJoueurCourant().getDirectionMouvement());
            for (int c: cpPossible) {
                //etape 2
                Jeu temp = j.Clone();

                //direction paradox ?
                //je mets 0 pour signifier pas de paradox à cette etape
                //on joue le coup
                temp.coupEchangeCarteMainContinuum(i, c);

                if (temp.infoJoueurs[temp.joueurCourant].existeParadox(temp.codex.getCouleurInterdite())){
                    //pour chaque coup
                    //on joue le paradox si il existe et on le joue pas
                    //respectivement temp2 et temp3
                    if (temp.existeParadoxSuperieur()){
                        //ici, on donne juste sens de paradox
                        temp2 = temp.Clone();
                        temp2.coupParadox(true, 1);
                    }
                    if (temp.existeParadoxInferieur()){
                        temp3 = temp.Clone();
                        temp3.coupParadox(true, -1);
                    }
                }

                //temp ne contient pas de clash et pas de paradox
                //temp2 contient paradox superieur et pas de clash
                //temp3 contient paradox inferieur et pas de clash
                //temp4 contient clash et pas de paradox
                //temp5 contient paradox superieur et clash
                //temp6 contient paradox inferieur et clash

                //ici, si les 2 sorciers on le même indice on peut avoir un clash
                if (temp.infoJoueurs[temp.joueurCourant].getSorcierIndice() == temp.infoJoueurs[(temp.joueurCourant+1)%2].getSorcierIndice()){
                    temp4 = temp.Clone();
                    temp4.coupClash();

                    if (temp2 != null){
                        temp5 = temp2.Clone();
                        temp5.coupClash();
                    }
                    if (temp3 != null){
                        temp6 = temp3.Clone();
                        temp6.coupClash();
                    }
                }

                //ici, on insère dans fils que les jeux existants
                //si pas de paradox alors temp2,3,5 et 6 n existe pas (etc...)
                if (temp2 != null){
                    temp2.finTour();
                    fils.add(new Arbre(temp2));
                    if (temp5 != null){
                        temp5.finTour();
                        fils.add(new Arbre(temp5));
                    }
                }
                if (temp3 != null){
                    temp3.finTour();
                    fils.add(new Arbre(temp3));
                    if (temp6 != null){
                        temp6.finTour();
                        fils.add(new Arbre(temp6));
                    }
                }
                if (temp4 != null) {
                    temp4.finTour();
                    fils.add(new Arbre(temp4));
                }
            }
        }
        //on appelle récursivement create avec tous les fils cree
        for (Arbre a:fils) {
            a.create();
        }
    }
}
