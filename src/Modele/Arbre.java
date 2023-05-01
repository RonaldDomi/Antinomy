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
            List<Integer> cpPossible = j.continuum.getCoupsPossibles(j.infoJoueurs[j.joueurCourant].getMain()[i], j.infoJoueurs[j.joueurCourant].getSorcierIndice(), j.infoJoueurs[j.joueurCourant].getDirectionMouvement());
            for (int c: cpPossible) {
                //etape 2
                Jeu temp = j.Clone();

                //direction paradox ?
                //je mets 0 pour signifier pas de paradox a cette etape
                //on joue le coup
                temp.coupEchangeCarteMainContinuum(i, c);
                //ici verifier quelle indice mettre
                temp.coupChangerPositionSorcier(i);

                if (temp.infoJoueurs[temp.joueurCourant].existeParadox(temp.codex.getCouleurInterdite())){
                    //pour chaque coup
                    //on joue le paradox si il existe et on le joue pas
                    //respectivement temp2 et temp3
                    if (temp.existeParadoxSuperieur()){
                        temp2 = temp.Clone();
                        //ici  on donne juste sens de paradox
                        //peut etre a rectifier
                        temp2.coupParadox(true, 1);
                    }
                    if (temp.existeParadoxInferieur()){
                        temp3 = temp.Clone();
                        temp3.coupParadox(true, -1);
                    }
                }

                //temp contient pas de clash et pas de paradox
                //temp4 contient clash et pas de paradox
                //temp3 contient paradox inferieur et pas de paradox
                //temp2 contient paradox superieur et pas de paradox
                //temp6 contient paradox inferieur et paradox
                //temp5 contient paradox superieur et paradox

                //ici si les 2 sorcier on meme indice on peut avoir un clash
                if (temp.infoJoueurs[temp.joueurCourant].getSorcierIndice() == temp.infoJoueurs[(temp.joueurCourant+1)%2].getSorcierIndice()){
                    temp4 = temp.Clone();
                    temp4.gagnantClash();
                    //*******************
                    //echanger les gemmes
                    //*******************
                    if (temp2 != null){
                        temp5 = temp2.Clone();
                        temp5.gagnantClash();
                        //*******************
                        //echanger les gemmes
                        //*******************
                        if (temp5.infoJoueurs[(temp5.joueurCourant+1)%2].getPoints() > 0){
                            temp5.infoJoueurs[temp5.joueurCourant].addPoint();
                            temp5.infoJoueurs[(temp5.joueurCourant+1)%2].setPoints(temp5.infoJoueurs[(temp5.joueurCourant+1)%2].getPoints() - 1);
                        }
                    }
                    if (temp3 != null){
                        temp6 = temp3.Clone();
                        temp6.gagnantClash();
                        //*******************
                        //echanger les gemmes
                        //*******************
                        if (temp5.infoJoueurs[(temp5.joueurCourant+1)%2].getPoints() > 0){
                            temp5.infoJoueurs[temp5.joueurCourant].addPoint();
                            temp5.infoJoueurs[(temp5.joueurCourant+1)%2].setPoints(temp5.infoJoueurs[(temp5.joueurCourant+1)%2].getPoints() - 1);
                        }
                    }
                }
                //ici on insere dans fils que les jeu existant
                //si pas de pas de paradox alors temp2,3,5 et 6 n existe pas (etc...)
                if (temp2 != null){
                    fils.add(new Arbre(temp2));
                    if (temp5 != null){
                        fils.add(new Arbre(temp5));
                    }
                }
                if (temp3 != null){
                    fils.add(new Arbre(temp3));
                    if (temp6 != null){
                        fils.add(new Arbre(temp6));
                    }
                }
                if (temp4 != null) {
                    fils.add(new Arbre(temp4));
                }
            }
        }
        //on appelle recursivement create avec touts les fils cree
        for (Arbre a:fils) {
            a.create();
        }
    }
}
