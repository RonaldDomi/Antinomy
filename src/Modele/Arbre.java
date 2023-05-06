package Modele;

import java.util.ArrayList;
import java.util.List;

public class Arbre {
    Jeu j;
    List<Arbre> fils;

    int profondeur, idMain, idContinuum, score;

    Boolean paradox, paradoxBas, paradoxHaut, clash;

    public Arbre(Jeu j){
        this.j = j;
        fils = new ArrayList<>();
        this.profondeur = 0;
    }

    public Arbre(Jeu j, int profondeur){
        this.j = j;
        fils = new ArrayList<>();
        this.profondeur = profondeur;
    }

    public Arbre(Jeu j, int profondeur, boolean paradox, boolean paradoxHaut, boolean paradoxBas, boolean clash, int idMain, int idContinuum, int score){
        this.score = score;
        this.paradox = paradox;
        this.paradoxHaut = paradoxHaut;
        this.paradoxBas = paradoxBas;
        this.clash = clash;
        this.idMain = idMain;
        this.idContinuum = idContinuum;
        this.j = j;
        fils = new ArrayList<>();
        this.profondeur = profondeur;
    }


    //on suppose qu l'ia est le joueur 0
    //le score est le nombre de gemmes de l'ia
    //l'heuristique du score est a changé ici elle est mauvaise car elle ne minimise pas celle de l'adversaire
    //on pourrait prendre (scoreIA - scoreADVERSAIRE) et maximiser cela
    //à discuter avec l'équipe
    public void create(){
        //condition arret
        //ici ce jeu est une feuille
        if (this.j.joueurGagnant == 1 || this.profondeur >= 5)
            return;

        Jeu temp = null, temp2 = null;
        boolean paradox = false, paradoxBas = false, paradoxHaut = false, clash = false;
        //etape 1
        //pour chaque carte choisie
        for (int i = 0; i < 3; i++) {
            //pour chaque coup avec chaque carte
            List<Integer> cpPossible = j.continuum.getCoupsPossibles(j.getMainJoueurCourant()[i], j.getInfoJoueurCourant().getSorcierIndice(), j.getInfoJoueurCourant().getDirectionMouvement());
            for (int c: cpPossible) {
                //etape 2
                try {
                    temp = j.clone();
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }

                //direction paradox ?
                //on joue le coup
                temp.coupEchangeCarteMainContinuum(i, c);

                if (temp.infoJoueurs[temp.joueurCourant].existeParadox(temp.codex.getCouleurInterdite())){
                    paradox = true;
                    //pour chaque coup
                    //on joue le paradox si il existe et on le joue pas
                    //respectivement temp2 et temp3
                    if (temp.existeParadoxSuperieur()){
                        paradoxHaut = true;
                        if (temp.existeParadoxInferieur()){
                            paradoxBas = true;
                            try {
                                temp2 = temp.clone();
                            } catch (CloneNotSupportedException e) {
                                throw new RuntimeException(e);
                            }
                            temp2.coupParadox(-1);
                        }
                        //ici, on donne juste sens de paradox
                        temp.coupParadox(1);
                    }
                    else {
                        paradoxBas = true;
                        temp.coupParadox(-1);
                    }
                }

                //ici, si les 2 sorciers on le même indice on peut avoir un clash
                if (temp.existeClash()){
                    clash = true;
                    temp.coupClash();

                    if (temp2 != null){
                        temp2.coupClash();
                    }

                }

                //ici, on insère dans fils que les jeux existants
                // le false de fintour pour ne pas lancer l'historique
                temp.finTour(false);
                if (temp2 == null) {
                    fils.add(new Arbre(temp, profondeur + 1, paradox, paradoxHaut, paradoxBas, clash, i, c, temp.infoJoueurs[/*temp.joueurCourant*/0].getPoints()));
                }
                else {
                    //si temp2 existe alors le paradox haut est dans temp et le bas est dans temp2
                    fils.add(new Arbre(temp, profondeur + 1, paradox, paradoxHaut, false, clash, i, c, temp.infoJoueurs[/*temp.joueurCourant*/0].getPoints()));
                    temp2.finTour(false);
                    fils.add(new Arbre(temp2, profondeur + 1, paradox, false, paradoxBas, clash, i, c, temp.infoJoueurs[/*temp.joueurCourant*/0].getPoints()));
                }
            }
        }
        //on appelle récursivement create avec tous les fils cree
        for (Arbre a:fils) {
            a.create();
        }
    }


    //ici on cherche a maximiser le score
    public Arbre prochain_Coup(){
        if (this.fils.isEmpty())
            return this;
        Arbre max = null;
        int maxVal = -1;
        for (Arbre a: this.fils){
            Arbre temp = a.prochain_Coup2();
            if (temp.score > maxVal){
                max = a;
                maxVal = temp.score;
            }
        }
        return max;
    }

    public Arbre prochain_Coup2(){
        if (this.fils.isEmpty())
            return this;
        Arbre min = null;
        int minVal = 6;
        for (Arbre a: this.fils){
            Arbre temp = a.prochain_Coup();
            if (temp.score < minVal){
                min = a;
                minVal = temp.score;
            }
        }
        return min;
    }
}