
import Controleur.ControleurMediateur;
import Global.Configuration;
import Modele.Jeu;
import Vue.InterfaceGraphique;
import Vue.CollecteurEvenements;

public class StatJeuIA {
    public static void main(String[] args) {
//        facile contre moyen
        float facile = 0, moyen = 0, difficile = 0, nb_rep = 20;
        for (int i = 0; i < nb_rep; i++) {
            Jeu j = new Jeu();
            ControleurMediateur c = new ControleurMediateur(j);
            c.nouvellePartie2(3, 4);
            int gagnant = c.jouerPartie();
            if(gagnant == 0){
                facile++;
            }
            else
                moyen++;
        }
        System.out.println("L IA difficile gagne a " + (moyen/nb_rep)*100 + " contre la moyenne.");
    }
}
