package Modele;

import java.util.ArrayList;
import java.util.List;
import Global.Statistics;

import static Modele.InfoJoueur.getEvaluationDuosMain;
import static Modele.InfoJoueur.getEvaluationSommeMain;
import static Modele.Jeu.faireCoupClone;

public class Arbre {
    Jeu jeuCourant;
    List<Arbre> fils;
    float bestEval;
    Coup bestCoup;
    Coup coupDeParent;
    boolean isMaximizingPlayer;

    public Arbre(Jeu jeuCourant, Coup coupDeParent, boolean isMaximizingPlayer){
        this.jeuCourant = jeuCourant;
        this.coupDeParent = coupDeParent;
        this.isMaximizingPlayer = isMaximizingPlayer;
        if(isMaximizingPlayer) this.bestEval = -1000;
        else this.bestEval = 1000;
        fils = new ArrayList<>();
    }

    public Coup getCoup(int depth, boolean withAlphaBeta){
        Search(depth, withAlphaBeta, -1000, 1000);
        return bestCoup;
    }


    float Search(int depth, boolean withAlphaBeta, float alpha, float beta){
        Statistics.incrementConfigurationsLookedAt();
        if(depth == 0 || jeuCourant.joueurGagnant != -1){
            bestEval = Evaluate(jeuCourant);
            return bestEval;
        }

        List<Coup> moves = jeuCourant.getCoupsPossibles();
        if(isMaximizingPlayer){
            float maxEval = -1000;
            for(int i = 0; i < moves.size(); i++) {
                //faire coup
                Jeu jeuBase = faireCoupClone(jeuCourant, moves.get(i));
                // si clash, saute au prochain coup
                if(jeuBase == null){
                    if( Evaluate(jeuCourant) > bestEval) {
                        maxEval = Evaluate(jeuCourant);
                        bestCoup = moves.get(i);
                        bestEval = Evaluate(jeuCourant);
                    }
                    continue;
                }

                Arbre newFils = new Arbre(jeuBase, moves.get(i), false);

                //evaluation
                float evaluation = newFils.Search(depth - 1, withAlphaBeta, alpha, beta);
                fils.add(newFils);

                maxEval = Math.max(maxEval, evaluation);
                alpha = Math.max(alpha, evaluation);
                if (evaluation > bestEval) {
                    bestCoup = moves.get(i);
                    bestEval = maxEval;
                }
                if (beta <= alpha && withAlphaBeta) {
                    break; // Beta cutoff
                }
            }
            return maxEval;
        }else{
            float minEval = 1000;
            for(int i = 0; i < moves.size(); i++) {
                //faire coup
                Jeu jeuBase = faireCoupClone(jeuCourant, moves.get(i));
                // si clash, saute au prochain coup
                if(jeuBase == null){
                    if( Evaluate(jeuCourant) < bestEval) {
                        minEval = Evaluate(jeuCourant);
                        bestCoup = moves.get(i);
                        bestEval = Evaluate(jeuCourant);
                    }
                    continue;
                }

                Arbre newFils = new Arbre(jeuBase, moves.get(i), true);

                //evaluation
                float evaluation = newFils.Search(depth - 1, withAlphaBeta, alpha, beta);
                fils.add(newFils);

                minEval = Math.min(minEval, evaluation);
                beta = Math.min(beta, evaluation);
                if (evaluation < bestEval) {
                    bestCoup = moves.get(i);
                    bestEval = minEval;
                }
                if (beta <= alpha && withAlphaBeta) {
                    break; // Alpha cutoff
                }
            }

            return minEval;
        }

    }

    float Evaluate(Jeu jeu){
        float evaluation = 0;
        InfoJoueur IAInfo = jeu.getInfoJoueurs()[1];
        InfoJoueur AdversaireInfo = jeu.getInfoJoueurs()[0];

//        evaluation += (jeu.egaliteClash()) ? -50 : 0;

        // difference de points
        evaluation += (IAInfo.getPoints() - AdversaireInfo.getPoints()) * 100;
        // somme main
        evaluation += getEvaluationSommeMain(IAInfo.getMain(), jeu.getCodex().getCouleurInterdite());
        evaluation += getEvaluationDuosMain(IAInfo.getMain(), jeu.getCodex().getCouleurInterdite());

        if(IAInfo.getPoints() >= 5) evaluation = 99999;
        if(AdversaireInfo.getPoints() >= 5) evaluation = -99999;
        return evaluation;
    }

    @Override
    public String toString() {
        String res = "";
        return res + this.bestEval;
    }
}