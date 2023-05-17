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
        if(isMaximizingPlayer) this.bestEval = -100000;
        else this.bestEval = 100000;
        fils = new ArrayList<>();
    }

    public Coup getCoup(int depth, boolean withAlphaBeta, int joueur){
        Search(depth, withAlphaBeta,-100000,100000, joueur);
        return bestCoup;
    }


    float Search(int depth,boolean withAlphaBeta , float alpha, float beta, int joueur){
        Statistics.incrementConfigurationsLookedAt();
        if(depth == 0 || jeuCourant.joueurGagnant != -1){
            bestEval = Evaluate(jeuCourant, joueur);
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
                    if( Evaluate(jeuCourant, joueur) > bestEval) {
                        maxEval = Evaluate(jeuCourant, joueur);
                        bestCoup = moves.get(i);
                        bestEval = Evaluate(jeuCourant, joueur);
                    }
                    continue;
                }

                Arbre newFils = new Arbre(jeuBase, moves.get(i), false);

                //evaluation
                float evaluation = newFils.Search(depth - 1, withAlphaBeta, alpha, beta, joueur);
                fils.add(newFils);

                alpha = Math.max(alpha, evaluation);

                maxEval = Math.max(maxEval, evaluation);
                if (evaluation > bestEval) {
                    bestCoup = moves.get(i);
                    bestEval = maxEval;
                }
                if (withAlphaBeta && beta <= alpha) {
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
                    if( Evaluate(jeuCourant, joueur) < bestEval) {
                        minEval = Evaluate(jeuCourant, joueur);
                        bestCoup = moves.get(i);
                        bestEval = Evaluate(jeuCourant, joueur);
                    }
                    continue;
                }

                Arbre newFils = new Arbre(jeuBase, moves.get(i), true);

                //evaluation
                float evaluation = newFils.Search(depth - 1, withAlphaBeta, alpha, beta, joueur);
                fils.add(newFils);

                beta = Math.min(beta, evaluation);

                minEval = Math.min(minEval, evaluation);
                if (evaluation < bestEval) {
                    bestCoup = moves.get(i);
                    bestEval = minEval;
                }

                if (withAlphaBeta && beta <= alpha) {
                    break; // Alpha cutoff
                }
            }

            return minEval;
        }

    }


    float Evaluate(Jeu jeu, int joueur){
        float evaluation = 0;
        InfoJoueur IAInfo = jeu.getInfoJoueurs()[joueur];
        InfoJoueur AdversaireInfo = jeu.getInfoJoueurs()[(joueur+1)%2];

//        evaluation += (jeu.egaliteClash()) ? -50 : 0;

        // difference de points
        evaluation += (IAInfo.getPoints() - AdversaireInfo.getPoints()) * 100;
        // somme main
        evaluation += getEvaluationSommeMain(IAInfo.getMain(), jeu.getCodex().getCouleurInterdite())*10;
//        evaluation += getEvaluationDuosMain(IAInfo.getMain(), jeu.getCodex().getCouleurInterdite());

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