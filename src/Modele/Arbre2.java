package Modele;

import java.util.ArrayList;
import java.util.List;
import Global.Statistics;

public class Arbre2 {
    Jeu jeuCourant;
    List<Arbre2> fils;
//    Coup bestsMoveThisPosition;
//    Coup bestMoveThisIteration;
//    int bestEvalThisIteration;
    float bestEval;
    Coup bestCoup;
    Coup coupDeParent;

    public Arbre2(Jeu jeuCourant, Coup coupDeParent){
        this.jeuCourant = jeuCourant;
        this.coupDeParent = coupDeParent;
        fils = new ArrayList<>();
        bestEval = -1000;
    }

    public Coup getCoup(int depth){
        Search(depth, this);
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
            Jeu jeuBase, jeuBase2 = null;
            try {
                jeuBase = arbreCourant.jeuCourant.clone();
                jeuBase.historique = new Historique(arbreCourant.jeuCourant.historique);
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
            jeuBase.coupEchangeCarteMainContinuum(indexMain, indexContinuum);
            //faire paradox
            if(dirParadox == 1){
                try {
                    jeuBase2 = jeuBase.clone();
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }
                jeuBase2.coupParadox(1);
            }else if(dirParadox == -1){
                jeuBase.coupParadox(-1);
            }
            //faire clash if exist
            if (jeuBase.existeClash()){
                jeuBase.coupClash();
                if(jeuBase2 != null)
                    jeuBase2.coupClash();
            }

            jeuBase.finTour(false);
            //evaluation
            Arbre2 newFils = new Arbre2(jeuBase, moves.get(i)), newFils2 = null;
            float evaluation = -Search(depth - 1, newFils), evaluation2 = 0;

            if(jeuBase2 != null){
                jeuBase2.finTour(false);
                //evaluation
                newFils2 = new Arbre2(jeuBase2, moves.get(i));
                evaluation2 = -Search(depth - 1, newFils2);
            }
//            int evaluation = Search(depth - 1, newFils);
            arbreCourant.fils.add(newFils);
            if(jeuBase2 == null)
                bestEvalThisIteration = Math.max(evaluation, bestEvalThisIteration);
            else {
                arbreCourant.fils.add(newFils2);
                bestEvalThisIteration = Math.max(evaluation, Math.max(evaluation2, bestEvalThisIteration));
            }
            //ici pas compris a voir
            //!!!!!!!!!!!!!!!!!!!!!!
            if (bestEvalThisIteration > arbreCourant.bestEval) {
                arbreCourant.bestCoup = moves.get(i);
                arbreCourant.bestEval = bestEvalThisIteration;
            }
        }
        return bestEvalThisIteration;
    }

    float Evaluate(Jeu jeu){
        float evaluation;
        evaluation = jeu.getInfoJoueurCourant().getPoints() - jeu.getInfoJoueurs()[jeu.adversaire()].getPoints();
        return evaluation;
    }

    @Override
    public String toString() {
        String res = "";
        return res + this.jeuCourant.getInfoJoueurs()[1].getPoints();
    }
}