package Modele;

import java.util.ArrayList;
import java.util.List;

public class Arbre2 {
    Jeu jeuCourant;
    List<Arbre2> fils;
//    Coup bestsMoveThisPosition;
//    Coup bestMoveThisIteration;
//    int bestEvalThisIteration;
    int bestEval;
    Coup bestCoup;
    Coup coupDeParent;

    public Arbre2(Jeu jeuCourant, Coup coupDeParent){
        this.jeuCourant = jeuCourant;
        this.coupDeParent = coupDeParent;
        fils = new ArrayList<>();
        bestEval = -1000;
    }

    public Coup getCoup(){
        Search(2, this);
        return bestCoup;
    }

    int Search(int depth, Arbre2 arbreCourant){
        if(depth == 0){
            arbreCourant.bestEval = Evaluate(arbreCourant.jeuCourant);
            return arbreCourant.bestEval;
        }

        int bestEvalThisIteration = -1000;

        List<Coup> moves = arbreCourant.jeuCourant.getCoupsPossibles();
        for(int i = 0; i < moves.size(); i++) {
            int indexMain = moves.get(i).indexMain;
            int indexContinuum = moves.get(i).indexContinuum;
            int dirParadox = moves.get(i).paradox;
            //faire coup
            Jeu jeuBase;
            try {
                jeuBase = arbreCourant.jeuCourant.clone();
                jeuBase.historique = new Historique(arbreCourant.jeuCourant.historique);
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
            //faire paradox
            jeuBase.coupEchangeCarteMainContinuum(indexMain, indexContinuum);
            if(dirParadox == 1){
                jeuBase.coupParadox(1);
            }else if(dirParadox == -1){
                jeuBase.coupParadox(-1);
            }
            //faire clash if exist
            if (jeuBase.existeClash()){
                jeuBase.coupClash();
            }

            //evaluation
            Arbre2 newFils = new Arbre2(jeuBase, moves.get(i));
            int evaluation = -Search(depth - 1, newFils);
            arbreCourant.fils.add(newFils);
            bestEvalThisIteration = Math.max(evaluation, bestEvalThisIteration);

            if (bestEvalThisIteration > arbreCourant.bestEval) {
                arbreCourant.bestCoup = moves.get(i);
                arbreCourant.bestEval = bestEvalThisIteration;
            }
        }
        return bestEvalThisIteration;
    }

    int Evaluate(Jeu jeu){return jeu.getInfoJoueurs()[1].getPoints();}
}