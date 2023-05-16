import Global.Statistics;

import Modele.ArbrePapa;
import Modele.Arbre;
import Modele.Jeu;

public class StatisticsTracker {
    public static void main(String[] args) {
        for(int depth = 1; depth<4; depth++){
            double total = generateDepthAverage(depth, false);
            double totalWithAlphaBeta = generateDepthAverage(depth, true);
            System.out.println("Average number of configs(without alpha/beta) with depth " + depth + " is: " + total);
            System.out.println("Average number of configs(with alpha/beta) with depth " + depth + " is: " + totalWithAlphaBeta);
            System.out.println("");
        }
    }

    static double generateDepthAveragePapa(int depth){
        for(int i = 0; i < 100; i++){
            Jeu jeu = new Jeu();
            jeu.getInfoJoueurs()[0].setSorcierIndice(0);
            jeu.getInfoJoueurs()[1].setSorcierIndice(0);
            jeu.setEtape(1);

            ArbrePapa arbre = new ArbrePapa(jeu);

            Statistics.setCurrentDepthTotalConfigurations(0);
            arbre.create(depth);
            Statistics.addDepth(depth, Statistics.currentDepthTotalConfigurations, false);
        }
        double total = Statistics.getAverageForDepth(depth, false);
        return total;
    }

    static double generateDepthAverage(int depth, boolean withAlphaBeta){
        for(int i = 0; i < 10; i++){
            Jeu jeu = new Jeu();
            jeu.getInfoJoueurs()[0].setSorcierIndice(0);
            jeu.getInfoJoueurs()[1].setSorcierIndice(0);
            jeu.setEtape(1);
//            int J1iM;
//            int J1iC;
//            jeu.coupEchangeCarteMainContinuum(J1iM, J1iC);
//            if(jeu.getInfoJoueurCourant().existeParadox(jeu.getCodex().getCouleurInterdite())){
//                // paradox
//                if(jeu.existeParadoxInferieur())
//                    jeu.coupParadox(ordre);
//            }

            Arbre arbre = new Arbre( null, true);

            Statistics.setCurrentDepthTotalConfigurations(0);
            arbre.getCoup(jeu, depth, withAlphaBeta);
            Statistics.addDepth(depth, Statistics.currentDepthTotalConfigurations, withAlphaBeta);
        }
        double total = Statistics.getAverageForDepth(depth, withAlphaBeta);
        return total;
    }

}
