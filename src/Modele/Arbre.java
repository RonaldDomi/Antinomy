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
        if(withAlphaBeta) SearchAlphaBeta(depth, this, -1000, 1000);
        else Search(depth);
        return bestCoup;
    }


    float Search(int depth){
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
                float evaluation = newFils.Search(depth - 1);
                fils.add(newFils);

                maxEval = Math.max(maxEval, evaluation);
                if (evaluation > bestEval) {
                    bestCoup = moves.get(i);
                    bestEval = maxEval;
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
                float evaluation = newFils.Search(depth - 1);
                fils.add(newFils);

                minEval = Math.min(minEval, evaluation);
                if (evaluation < bestEval) {
                    bestCoup = moves.get(i);
                    bestEval = minEval;
                }
            }

            return minEval;
        }

    }

    float SearchAlphaBeta(int depth, Arbre arbreCourant, float alpha, float beta){
        Statistics.incrementConfigurationsLookedAt();
        if(depth == 0){
            arbreCourant.bestEval = Evaluate(arbreCourant.jeuCourant);
            return arbreCourant.bestEval;
        }

        List<Coup> moves = arbreCourant.jeuCourant.getCoupsPossibles();
        if(isMaximizingPlayer){
            float maxEval = -1000;
            for(int i = 0; i < moves.size(); i++) {
                //faire coup
                Jeu jeuBase = faireCoupClone(arbreCourant.jeuCourant, moves.get(i));
                // si clash, saute au prochain coup
                if(jeuBase == null) continue;

                Arbre newFils = new Arbre(jeuBase, moves.get(i), false);

                //evaluation
                float evaluation = SearchAlphaBeta(depth - 1, newFils, alpha, beta);
                arbreCourant.fils.add(newFils);

                maxEval = Math.max(maxEval, evaluation);
                alpha = Math.max(alpha, evaluation);
                if (evaluation > arbreCourant.bestEval) {
                    arbreCourant.bestCoup = moves.get(i);
                    arbreCourant.bestEval = maxEval;
                }

                if (beta <= alpha) {
                    break; // Beta cutoff
                }
            }
            return maxEval;
        }else{
            float minEval = 1000;
            for(int i = 0; i < moves.size(); i++) {
                //faire coup
                Jeu jeuBase = faireCoupClone(arbreCourant.jeuCourant, moves.get(i));
                // si clash, saute au prochain coup
                if(jeuBase == null) continue;
                Arbre newFils = new Arbre(jeuBase, moves.get(i), true);

                //evaluation
                float evaluation = SearchAlphaBeta(depth - 1, newFils, alpha, beta);
                arbreCourant.fils.add(newFils);

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