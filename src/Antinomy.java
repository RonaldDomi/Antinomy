import Controleur.ControleurMediateur;
import Global.Configuration;
import Modele.Jeu;
import Vue.InterfaceGraphique;
import Vue.CollecteurEvenements;

/**
 * <P> Initialise le menu principal et son interface graphique. </P>
 */
public class Antinomy {
//	final static String typeInterface = Configuration.typeInterface;

	public static void main(String[] args) {
		
		Configuration.typeJoueur = -1;
		Jeu j = new Jeu();
		CollecteurEvenements control = new ControleurMediateur(j);
//		switch (typeInterface) {
		InterfaceGraphique.demarrer(j, control, null);
		
	}
}
