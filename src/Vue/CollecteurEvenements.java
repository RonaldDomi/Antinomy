package Vue;

import javax.swing.*;
import java.net.Socket;

public interface CollecteurEvenements {
	void clicSouris(int l, int c);
	void clicSourisBouton(int i);
	void clicSourisBoutonMenu(int fenetre, int i);
	void toucheClavier(String t);
	void ajouteSocket(Socket clientSocket);
	void ajouteInterfaceUtilisateur(InterfaceUtilisateur vue);
	void tictac();
	void nouvellePartie(int i, int j);
	
	void setTypeJoueur(int typeJoueur, int i);

	void ajouteTextFieldHostName(JTextField textField);

	void charger(int i, int j);
}
