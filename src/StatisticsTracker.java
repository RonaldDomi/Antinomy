import Global.Statistics;

//import Modele.ArbreOld2;
import Modele.Arbre;
import Modele.Jeu;

public class StatisticsTracker {
    public static void main(String[] args) {
        for(int depth = 3; depth <12; depth++){
            //double total = generateDepthAverage(depth, false);
            double totalWithAlphaBeta = generateDepthAverage(depth, true);
            //System.out.println("Average number of configs(without alpha/beta) with depth " + depth + " is: " + total);
            System.out.println("Average number of configs(with alpha/beta) with depth " + depth + " is: " + totalWithAlphaBeta);
            System.out.println("");
        }
    }

    static double generateDepthAverage(int depth, boolean withAlphaBeta){
        for(int i = 0; i < 10; i++){
            Jeu jeu = new Jeu();
            jeu.getInfoJoueurs()[0].setSorcierIndice(0);
            jeu.getInfoJoueurs()[1].setSorcierIndice(0);
            jeu.setEtape(1);

            Arbre arbre = new Arbre(jeu, null, true);

            Statistics.setCurrentDepthTotalConfigurations(0);
            if(withAlphaBeta) arbre.getCoup(depth, true,0);
            else arbre.getCoup(depth, false, 0);
            Statistics.addDepth(depth, Statistics.currentDepthTotalConfigurations, withAlphaBeta);
        }
        double total = Statistics.getAverageForDepth(depth, withAlphaBeta);
        return total;
    }

}
