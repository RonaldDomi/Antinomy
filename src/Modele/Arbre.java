package Modele;

import java.util.ArrayList;
import java.util.List;

public class Arbre {
    Jeu j;
    List<Arbre> fils;

    int profondeur, idMain, idContinuum;

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

    public Arbre(Jeu j, int profondeur, boolean paradox, boolean paradoxHaut, boolean paradoxBas, boolean clash, int idMain, int idContinuum){
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
                    fils.add(new Arbre(temp, profondeur + 1, paradox, paradoxHaut, paradoxBas, clash, i, c));
                }
                else {
                    //si temp2 existe alors le paradox haut est dans temp et le bas est dans temp2
                    fils.add(new Arbre(temp, profondeur + 1, paradox, paradoxHaut, false, clash, i, c));
                    temp2.finTour(false);
                    fils.add(new Arbre(temp2, profondeur + 1, paradox, false, paradoxBas, clash, i, c));
                }
            }
        }
        //on appelle récursivement create avec tous les fils cree
        for (Arbre a:fils) {
            a.create();
        }
    }
}