package Modele;

import java.util.ArrayList;
import java.util.List;
import Global.Statistics;

import static Modele.Jeu.faireCoupClone;

public class Arbre2 {
    Jeu jeuCourant;
    List<Arbre2> fils;
    float bestEval;
    Coup bestCoup;
    Coup coupDeParent;
    boolean isMaximizingPlayer;

    public Arbre2(Jeu jeuCourant, Coup coupDeParent, boolean isMaximizingPlayer){
        this.jeuCourant = jeuCourant;
        this.coupDeParent = coupDeParent;
        this.isMaximizingPlayer = isMaximizingPlayer;
        if(isMaximizingPlayer) this.bestEval = Integer.MIN_VALUE;
        else this.bestEval = Integer.MAX_VALUE;
        fils = new ArrayList<>();
    }

    public Coup getCoup(int depth, boolean withAlphaBeta){
        if(withAlphaBeta) SearchAlphaBeta(depth, this, Integer.MIN_VALUE, Integer.MAX_VALUE);
        else Search(depth, this);
        return bestCoup;
    }
    float Search(int depth, Arbre2 arbreCourant){
        Statistics.incrementConfigurationsLookedAt();
        if(depth == 0){
            arbreCourant.bestEval = Evaluate(arbreCourant.jeuCourant);
            return arbreCourant.bestEval;
        }

        float bestEvalThisIteration = -1000;

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
            jeuBase.coupEchangeCarteMainContinuum(indexMain, indexContinuum);
            //faire paradox
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
            Arbre2 newFils = new Arbre2(jeuBase, moves.get(i), true);
            float evaluation = -Search(depth - 1, newFils);
//            int evaluation = Search(depth - 1, newFils);
            arbreCourant.fils.add(newFils);
            bestEvalThisIteration = Math.max(evaluation, bestEvalThisIteration);

            if (bestEvalThisIteration > arbreCourant.bestEval) {
                arbreCourant.bestCoup = moves.get(i);
                arbreCourant.bestEval = bestEvalThisIteration;
            }
        }
        return bestEvalThisIteration;
    }

    float SearchAlphaBeta(int depth, Arbre2 arbreCourant, float alpha, float beta){
        Statistics.incrementConfigurationsLookedAt();
        if(depth == 0){
            arbreCourant.bestEval = Evaluate(arbreCourant.jeuCourant);
            return arbreCourant.bestEval;
        }

        List<Coup> moves = arbreCourant.jeuCourant.getCoupsPossibles();
        if(isMaximizingPlayer){
            float maxEval = Integer.MIN_VALUE;
            for(int i = 0; i < moves.size(); i++) {
                //faire coup
                Jeu jeuBase = faireCoupClone(arbreCourant.jeuCourant, moves.get(i));
                Arbre2 newFils = new Arbre2(jeuBase, moves.get(i), false);

                //evaluation
                float evaluation = SearchAlphaBeta(depth - 1, newFils, alpha, beta);
                maxEval = Math.max(maxEval, evaluation);
                alpha = Math.max(alpha, evaluation);
                if (evaluation > arbreCourant.bestEval) {
                    arbreCourant.bestCoup = moves.get(i);
                    arbreCourant.bestEval = maxEval;
                }

                arbreCourant.fils.add(newFils);
                if (beta <= alpha) {
                    break; // Beta cutoff
                }
            }
            return maxEval;
        }else{
            float minEval = Integer.MAX_VALUE;
            for(int i = 0; i < moves.size(); i++) {
                //faire coup
                Jeu jeuBase = faireCoupClone(arbreCourant.jeuCourant, moves.get(i));
                Arbre2 newFils = new Arbre2(jeuBase, moves.get(i), true);

                //evaluation
                float evaluation = SearchAlphaBeta(depth - 1, newFils, alpha, beta);
                minEval = Math.min(minEval, evaluation);
                beta = Math.min(beta, evaluation);
                if (evaluation < arbreCourant.bestEval) {
                    arbreCourant.bestCoup = moves.get(i);
                    arbreCourant.bestEval = minEval;
                }

                if (beta <= alpha) {
                    break; // Alpha cutoff
                }
            }

            return minEval;
        }

    }


    float Evaluate(Jeu jeu){
        float evaluation;
        evaluation = jeu.getInfoJoueurs()[1].getPoints() - jeu.getInfoJoueurs()[0].getPoints();
        return evaluation;
    }

    @Override
    public String toString() {
        String res = "";
        return res + this.jeuCourant.getInfoJoueurs()[1].getPoints();
    }
}