package Modele;

import java.util.ArrayList;
import java.util.List;

public class Arbre2 {
    List<Arbre2> fils;

//    Coup bestsMoveThisPosition;
//    Coup bestMoveThisIteration;
//    int bestEvalThisIteration;
    int bestEval;
    Coup bestCoup;

    public Arbre2(Jeu j){
        fils = new ArrayList<>();
        bestEval = -1000;
    }

    Coup getCoup(Jeu jeu){
        Search(5, jeu);
        return bestCoup;
    }

    int Search(int depth, Jeu jeu){
        if(depth == 0) return Evaluate(jeu);

        int bestEvalThisIteration = -1000;

        List<Coup> moves = jeu.getCoupsPossibles();
        for(int i = 0; i < moves.size(); i++) {
            int indexMain = moves.get(i).indexMain;
            int indexContinuum = moves.get(i).indexContinuum;
            int dirParadox = moves.get(i).paradox;
            //faire coup
            Jeu jeuBase;
            try {
                jeuBase = jeu.clone();
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
            int evaluation = -Search(depth - 1, jeuBase);
            bestEvalThisIteration = Math.max(evaluation, bestEvalThisIteration);

            if (bestEvalThisIteration > bestEval) {
                bestCoup = moves.get(i);
                bestEval = bestEvalThisIteration;
            }
        }
        return bestEvalThisIteration;
    }

    int Evaluate(Jeu jeu){return 0;}
}