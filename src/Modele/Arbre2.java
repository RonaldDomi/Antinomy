package Modele;

import java.util.ArrayList;
import java.util.List;
import Global.Statistics;

import static Modele.InfoJoueur.getEvaluationDuosMain;
import static Modele.InfoJoueur.getEvaluationSommeMain;
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

        List<Coup> moves = arbreCourant.jeuCourant.getCoupsPossibles();
        if(isMaximizingPlayer){
            float maxEval = Integer.MIN_VALUE;
            for(int i = 0; i < moves.size(); i++) {
                //faire coup
                Jeu jeuBase = faireCoupClone(arbreCourant.jeuCourant, moves.get(i));
                Arbre2 newFils = new Arbre2(jeuBase, moves.get(i), false);

                //evaluation
                float evaluation = Search(depth - 1, newFils);
                arbreCourant.fils.add(newFils);

                maxEval = Math.max(maxEval, evaluation);
                if (evaluation > arbreCourant.bestEval) {
                    arbreCourant.bestCoup = moves.get(i);
                    arbreCourant.bestEval = maxEval;
                }

            }
            return maxEval;
        }else{
            float minEval = Integer.MAX_VALUE;
            for(int i = 0; i < moves.size(); i++) {
                //faire coup
                Jeu jeuBase = faireCoupClone(arbreCourant.jeuCourant, moves.get(i));
                //ici on fait la verification que il existe un jeu qui n'est pas null
                if(jeuBase != null){
                    Arbre2 newFils = new Arbre2(jeuBase, moves.get(i), true);
                    //evaluation
                    float evaluation = Search(depth - 1, newFils);
                    arbreCourant.fils.add(newFils);
                    minEval = Math.min(minEval, evaluation);
                    if (evaluation < arbreCourant.bestEval) {
                        arbreCourant.bestCoup = moves.get(i);
                        arbreCourant.bestEval = minEval;
                    }
                }

            }

            return minEval;
        }

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
                if(jeuBase == null) break;

                Arbre2 newFils = new Arbre2(jeuBase, moves.get(i), false);

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
            float minEval = Integer.MAX_VALUE;
            for(int i = 0; i < moves.size(); i++) {
                //faire coup
                Jeu jeuBase = faireCoupClone(arbreCourant.jeuCourant, moves.get(i));
                if(jeuBase == null) break;
                Arbre2 newFils = new Arbre2(jeuBase, moves.get(i), true);

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

        if(IAInfo.getPoints() >= 5) evaluation = Integer.MAX_VALUE;
        if(AdversaireInfo.getPoints() >= 5) evaluation = Integer.MIN_VALUE;
        return evaluation;
    }

    @Override
    public String toString() {
        String res = "";
        return res + this.bestEval;
    }
}