import Global.Statistics;

import java.util.List;

import Modele.Arbre;
import Modele.Arbre2;
import Modele.Coup;
import Modele.Jeu;

public class StatisticsTracker {
    public static void main(String[] args) {
        for(int depth = 3; depth <8; depth++){
            double total = generateDepthAverage(depth);
            System.out.println("Average number of configs with depth " + depth + " is: " + total);
        }
    }

    static double generateDepthAverage(int depth){
        for(int i = 0; i < 100; i++){
            Jeu jeu = new Jeu();
            jeu.getInfoJoueurs()[0].setSorcierIndice(0);
            jeu.getInfoJoueurs()[1].setSorcierIndice(0);
            jeu.setEtape(1);

            Arbre arbre = new Arbre(jeu);

            Statistics.setCurrentDepthTotalConfigurations(0);
            arbre.create(depth);
            Statistics.addDepth(depth, Statistics.currentDepthTotalConfigurations);
        }
        double total = Statistics.getAverageForDepth(depth);
        return total;
    }

    static double generateDepthAverage2(int depth){
        for(int i = 0; i < 100; i++){
            Jeu jeu = new Jeu();
            jeu.getInfoJoueurs()[0].setSorcierIndice(0);
            jeu.getInfoJoueurs()[1].setSorcierIndice(0);
            jeu.setEtape(1);

            Arbre2 arbre2 = new Arbre2(jeu, null);

            Statistics.setCurrentDepthTotalConfigurations(0);
            arbre2.getCoup(depth);
            Statistics.addDepth(depth, Statistics.currentDepthTotalConfigurations);
        }
        double total = Statistics.getAverageForDepth(depth);
        return total;
    }
}
