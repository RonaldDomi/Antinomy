//package Modele;
//
//import java.util.ArrayList;
//import java.util.List;
//import Global.Statistics;
//
//import static Modele.InfoJoueur.getEvaluationDuosMain;
//import static Modele.InfoJoueur.getEvaluationSommeMain;
//
//public class Arbre2 {
//    List<Arbre2> fils;
//    float bestEval;
//    Coup bestCoup;
//    Coup coupDeParent;
//    boolean isMaximizingPlayer;
//
//    public Arbre2(Coup coupDeParent, boolean isMaximizingPlayer){
//        this.coupDeParent = coupDeParent;
//        this.isMaximizingPlayer = isMaximizingPlayer;
//        if(isMaximizingPlayer) this.bestEval = -1000;
//        else this.bestEval = 1000;
//        fils = new ArrayList<>();
//    }
//
//    public Coup getCoup(Jeu jeuCourant, int depth, boolean withAlphaBeta){
//        Search(jeuCourant, depth, withAlphaBeta, -1000, 1000);
//        return bestCoup;
//    }
//
//
//    float Search(Jeu jeuCourant, int depth, boolean withAlphaBeta, float alpha, float beta){
//        Statistics.incrementConfigurationsLookedAt();
//        if(depth == 0 || jeuCourant.joueurGagnant != -1){
//            bestEval = Evaluate(jeuCourant);
//            return bestEval;
//        }
//
//        List<Coup> moves = jeuCourant.getCoupsPossibles();
//        if(isMaximizingPlayer){
//            float maxEval = -1000;
//            for(int i = 0; i < moves.size(); i++) {
////                int previousSorcierIndice = jeuCourant.getInfoJoueurCourant().getSorcierIndice();
//
//                //faire coup
//                boolean clashEgalite = jeuCourant.faireCoup(moves.get(i));
//                if(clashEgalite)
//                    continue;
//
//                Arbre2 newFils = new Arbre2(moves.get(i), false);
//
//                //evaluation
//                float evaluation = newFils.Search(jeuCourant, depth - 1, withAlphaBeta, alpha, beta);
//                fils.add(newFils);
//
//                // undo coup
//                jeuCourant.historique.listeJeu.remove(0);
//                Historique currentHistorique = jeuCourant.historique;
//                jeuCourant.charger(jeuCourant.historique.listeJeu.get(0), true);
//                jeuCourant.historique = currentHistorique;
////                jeuCourant.undoCoup(moves.get(i));
//
//                maxEval = Math.max(maxEval, evaluation);
//                alpha = Math.max(alpha, evaluation);
//                if (evaluation > bestEval) {
//                    bestCoup = moves.get(i);
//                    bestEval = maxEval;
//                }
//                if (beta <= alpha && withAlphaBeta) {
//                    break; // Beta cutoff
//                }
//            }
//            return maxEval;
//        }else{
//            float minEval = 1000;
//            for(int i = 0; i < moves.size(); i++) {
//                //faire coup
//                boolean clashEgalite = jeuCourant.faireCoup(moves.get(i));
//                if(clashEgalite)
//                    continue;
//
//                Arbre2 newFils = new Arbre2(moves.get(i), true);
//
//                //evaluation
//                float evaluation = newFils.Search(jeuCourant,depth - 1, withAlphaBeta, alpha, beta);
//                fils.add(newFils);
//
//                // undo coup
//                jeuCourant.historique.listeJeu.remove(0);
//                Historique currentHistorique = jeuCourant.historique;
//                jeuCourant.charger(jeuCourant.historique.listeJeu.get(0), true);
//                jeuCourant.historique = currentHistorique;
////                jeuCourant.undoCoup(moves.get(i));
//
//                minEval = Math.min(minEval, evaluation);
//                beta = Math.min(beta, evaluation);
//                if (evaluation < bestEval) {
//                    bestCoup = moves.get(i);
//                    bestEval = minEval;
//                }
//                if (beta <= alpha && withAlphaBeta) {
//                    break; // Alpha cutoff
//                }
//            }
//
//            return minEval;
//        }
//
//    }
//
//    float Evaluate(Jeu jeu){
//        float evaluation = 0;
//        InfoJoueur IAInfo = jeu.getInfoJoueurs()[1];
//        InfoJoueur AdversaireInfo = jeu.getInfoJoueurs()[0];
//
////        evaluation += (jeu.egaliteClash()) ? -50 : 0;
//
//        // difference de points
//        evaluation += (IAInfo.getPoints() - AdversaireInfo.getPoints()) * 100;
//        // somme main
//        evaluation += getEvaluationSommeMain(IAInfo.getMain(), jeu.getCodex().getCouleurInterdite());
//        evaluation += getEvaluationDuosMain(IAInfo.getMain(), jeu.getCodex().getCouleurInterdite());
//
//        if(IAInfo.getPoints() >= 5) evaluation = 99999;
//        if(AdversaireInfo.getPoints() >= 5) evaluation = -99999;
//        return evaluation;
//    }
//
//    @Override
//    public String toString() {
//        String res = "";
//        return res + this.bestEval;
//    }
//}