package Global;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Configuration du jeu, de l'IA des animations
 */
public class Configuration {
	static final int silence = 1;
	public static int lenteurAttente = 40;
	public static int tempsAnimation = 10;
	public static int profondeurIAFacile = 1;
	public static int profondeurIAMedium = 3;
	public static int profondeurIADifficile = 7;

	public static boolean mainAdverseCachee = true;
	public static int typeJoueur;
	public static int numeroPort = 6969;
	
	/**
	 * Ouvre un fichier, TODO: Le rendre compatible JAR
	 * @param s Un nom de fichier à ouvrir.
	 * @return Un InputStream du fichier ouvert.
	 */
	public static InputStream ouvre(String s) {
		InputStream in = null;
		try {
			in = new FileInputStream("res/" + s);
		} catch (FileNotFoundException e) {
			erreur("impossible de trouver le fichier " + s);
		}
		return in;
	}
	
	
	/**
	 * Affiche des messages avec INFO/ALERTE/ERREUR
	 * @author Guillaume Huard : Projet Sokoban
	 */
	public static void affiche(int niveau, String message) {
		if (niveau > silence)
			System.err.println(message);
	}

	public static void info(String s) {
		affiche(1, "INFO : " + s);
	}

	public static void erreur(String s) {
		affiche(3, "ERREUR : " + s);
		System.exit(1);
	}
	
}
