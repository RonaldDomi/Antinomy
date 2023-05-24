package Vue;

import Global.Configuration;
import Patterns.Observateur;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

import static java.lang.Math.min;

/**
 * Implémentation graphique du menu principal
 */
public class MenuGraphique extends JComponent implements Observateur {
	Image bouton, boutonSelected, boutonS, boutonSelectedS,
	exit, back,
	BG_BG, BG_BD, BG_HD;
	int mesureLargeur;
	int mesureHauteur;
	int padding;
	int largeur, hauteur;

	int centre_largeur, centre_hauteur;

	int taille_bouton;

	int taille_bouton_s;

	int deb_bouton_x;
	int deb_bouton_y;
	int deb_bouton_nouvelle_partie_y, deb_bouton_nouvelle_partie_x;

	int affichage = 1;

	boolean selectBoutonExit;

	Font h1, h2;

	FontMetrics m;

	String[] type_possible = {"Humain", "IA Aleatoire", "IA Facile", "IA Moyenne", "IA Difficile"};

	int selectBouton = -1;

	String boutonString;

	int[] choix_type;

	InterfaceGraphique parent;

	public MenuGraphique(InterfaceGraphique parent) {
		this.parent = parent;
		bouton = lisImage("BoutonL");
		boutonSelected = lisImage("BoutonSelectedL");
		boutonS = lisImage("Bouton");
		boutonSelectedS = lisImage("BoutonSelected");
		exit = lisImage("Exit");
		back = lisImage("Back");
		BG_BG = lisImage("Background_BG");
		BG_HD = lisImage("Background_HD");
		BG_BD = lisImage("Background_BD");
		choix_type = new int[2];
		choix_type[0] = 0;
		choix_type[1] = 0;
	}

	/**
	 * Lit une image. TODO: Adapter pour une archive JAR
	 * ClassLoader.getSystemClassLoader().getResourceAsStream("filename");
	 * @param nom Nom de l'image
	 * @return Objet de l'image ouverte
	 */
	private Image lisImage(String nom) {
		InputStream in = Configuration.ouvre(nom);
		Configuration.info("Chargement de l'image " + nom);
		try {
			// Chargement d'une image utilisable dans Swing
			return ImageIO.read(in);
		} catch (Exception e) {
			Configuration.erreur("Impossible de charger l'image " + nom);
		}
		return null;
	}

	/**
	 * Affiche une image suivant les coordonnées
	 * @param x Coordonnée X où afficher l'image
	 * @param y Coordonnée Y où afficher l'image
	 * @param l Longueur de l'image
	 * @param h Hauteur de l'image
	 * @param i L'image à afficher
	 * @param g Objet Graphics2D
	 * */
	protected void tracer(Graphics2D g, Image i, int x, int y, int l, int h) {
		g.drawImage(i, x, y, l, h, null);
	}

	/**
	 *  <P> Dessine tout le menu principal </P>
	 * @param g Objet Graphics
	 * */
	@Override
	public void paintComponent(Graphics g) {

		Graphics2D drawable = (Graphics2D) g;

		g.setColor(new Color(0, 0, 0));

		drawable.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		largeur = getSize().width;
		hauteur = getSize().height;

		taille_bouton_s = min(largeur/17, hauteur/6 * 54 / 84)*3/4;

		int taille_x = min(largeur*1/3, (hauteur*2/5));
		int taille_y = min(hauteur*2/5, (largeur*1/3));
		tracer(drawable, BG_BG, 0, hauteur-taille_y, taille_x, taille_y);

		taille_x = min(largeur*1/5, (hauteur*1/5) * 35/24);
		taille_y = min(hauteur*1/5, (largeur*1/5) * 24/35);
		tracer(drawable, BG_BD, largeur-taille_x, hauteur-taille_y, taille_x, taille_y);

		taille_x = min(largeur*1/4, (hauteur*1/3) * 5/4);
		taille_y = min(hauteur*1/3, (largeur*1/4) * 4/5);
		tracer(drawable, BG_HD, largeur-taille_x, 0, taille_x, taille_y);

		switch (affichage){
			case 1:
				menuPrincipale(g);
				break;
			case 2:
				nouvellePartie(g);
				break;
			case 3:
				enLigne(g);
				break;
		}

		if (parent.typeMenuSelectionJoueur == 0)
			boutonRetour(g);
	}

	/**
	 * Dessine le bouton retour
	 * @param g Objet Graphics
	 * */
	private void boutonRetour(Graphics g) {
			System.out.println("ici");
		if (affichage == 1){
			//quitter
			bouton((Graphics2D) g, selectBoutonExit ? boutonSelectedS : boutonS, exit, taille_bouton_s/3+taille_bouton_s/2, taille_bouton_s/3+taille_bouton_s/2, taille_bouton_s, taille_bouton_s);
		}
		else {
			//retour
			bouton((Graphics2D) g, selectBoutonExit ? boutonSelectedS : boutonS, back, taille_bouton_s/3+taille_bouton_s/2, taille_bouton_s/3+taille_bouton_s/2, taille_bouton_s, taille_bouton_s);
		}
	}

	/**
	 * Dessine le menu nouvelle partie local
	 * @param g Objet Graphics
	 * */

	void nouvellePartie(Graphics g){

		mesureLargeur = min(largeur/(4*5+4), hauteur/10);
		mesureHauteur = min(hauteur/10, largeur/(4*5+4));
		padding = mesureLargeur / 4;

		centre_largeur = largeur / 2;
		centre_hauteur = hauteur / 2;

		h2 = new Font("Medieval English", Font.PLAIN, min(largeur/36, hauteur/18));
		Font h3 = new Font("Medieval English", Font.PLAIN, min(largeur/54, hauteur/24));
		int x;
		int y;

		g.setFont(h2);
		m = g.getFontMetrics();

		taille_bouton = 5*mesureLargeur/2;

		deb_bouton_y = centre_hauteur + (-1) * (mesureHauteur + padding) - mesureHauteur/6;
		deb_bouton_x = centre_largeur + (-2) * (taille_bouton + padding)  + (taille_bouton + padding)/2 - taille_bouton/2;

		g.setFont(h3);
		for (int j = 0; j < 2; j++) {
			y = centre_hauteur + (j-1) * (mesureHauteur + padding);
			for (int i = 0; i <= type_possible.length; i++) {
				if (i==0){
					x = centre_largeur - 3*(taille_bouton+padding) + (taille_bouton + padding)/2;
					g.drawString("Joueur " + (j+1) + " :", x-m.stringWidth("Joueur " + (j+1) + " :")/2, y+m.getHeight() - mesureHauteur/4);
				}
				else {
					x = centre_largeur + (i-3) * (taille_bouton+padding) + (taille_bouton + padding)/2;
//					bouton((Graphics2D) g, choix_type[j] == i-1 ? boutonSelected : bouton, type_possible[i-1], x, y, taille_bouton, mesureHauteur/2);
					tracer((Graphics2D) g, choix_type[j] == i-1 ? boutonSelected : bouton, x - taille_bouton/2, y - mesureHauteur/6, taille_bouton, mesureHauteur);
					g.drawString(type_possible[i-1], x-m.stringWidth(type_possible[i-1])/3, y+m.getHeight() - mesureHauteur/4);

				}
			}
		}

		g.setFont(h2);

		if (parent.typeMenuSelectionJoueur == 1){
			deb_bouton_nouvelle_partie_y = hauteur*3/4 - mesureHauteur/2;
			bouton((Graphics2D) g, selectBouton == 1 ? boutonSelected : bouton, "Continuer", centre_largeur, hauteur*3/4, taille_bouton*2, mesureHauteur);
		}
		else {
			deb_bouton_nouvelle_partie_y = hauteur*3/4 - mesureHauteur/2;
			deb_bouton_nouvelle_partie_x = centre_largeur - taille_bouton*2 - padding/2;
			bouton((Graphics2D) g, selectBouton == 1 ? boutonSelected : bouton, "Charger", centre_largeur - taille_bouton - padding/2, hauteur*3/4, taille_bouton*2, mesureHauteur);
			bouton((Graphics2D) g, selectBouton == 2 ? boutonSelected : bouton, "Nouvelle Partie", centre_largeur + taille_bouton + padding/2, hauteur*3/4, taille_bouton*2, mesureHauteur);
		}
	}

	/**
	 * Dessine le menu nouvelle partie en ligne
	 * @param g Objet Graphics
	 * */
	void enLigne(Graphics g){

		mesureLargeur = min(largeur/(4*5+4), hauteur/10);
		mesureHauteur = min(hauteur/10, largeur/(4*5+4));
		padding = mesureLargeur / 4;

		centre_largeur = largeur / 2;
		centre_hauteur = hauteur / 2;

		h2 = new Font("Medieval English", Font.PLAIN, min(largeur/36, hauteur/18));

		int x;
		int y;

		g.setFont(h2);
		m = g.getFontMetrics();

		taille_bouton = 5*mesureLargeur;

		deb_bouton_y = hauteur*3/4;
		deb_bouton_x = centre_largeur/4;

		if(boutonString == null){
			boutonString = "Lancer";
		}

		g.drawLine(centre_largeur, hauteur/4, centre_largeur, hauteur * 3/4);

		for (int j = 0; j < 2; j++) {
			x = centre_largeur/4 + j * centre_largeur + taille_bouton / 2;
			switch (j){
				case 0:
					g.drawString("Serveur", x - m.stringWidth("Serveur")/2, hauteur/4);
					g.drawString("Nom de la machine :", x - m.stringWidth("Nom de la machine :")/2, hauteur/2- m.getHeight() - padding);
					bouton((Graphics2D) g, selectBouton == j ? boutonSelected : bouton, boutonString, x, hauteur*3/4, taille_bouton, mesureHauteur);
					break;
				case 1:
					g.drawString("Client", x - m.stringWidth("Client")/2, hauteur/4);
					g.drawString("Nom de la machine :", x - m.stringWidth("Nom de la machine :")/2, hauteur/2- m.getHeight() - padding);
					bouton((Graphics2D) g, selectBouton == j ? boutonSelected : bouton, "Se connecter", x, hauteur*3/4, taille_bouton, mesureHauteur);
					break;
			}
		}
	}

	/**
	 * Dessine le menu principal
	 * @param g Objet Graphics
	 * */
	void menuPrincipale(Graphics g){
		mesureLargeur = min(largeur/6, hauteur/10);
		mesureHauteur = min(hauteur/10, largeur/6);
		padding = mesureLargeur / 5;

		centre_largeur = largeur / 2;
		centre_hauteur = hauteur / 2;

		h1 = new Font("Medieval English", Font.PLAIN, min(largeur/6, hauteur/8));
		h2 = new Font("Medieval English", Font.PLAIN, min(largeur/12, hauteur/16));


		//title

		g.setFont(h1);
		m = g.getFontMetrics();

		g.drawString("Antinomy", centre_largeur-m.stringWidth("Antinomy")/2, centre_hauteur/4+m.getHeight());

		Graphics2D drawable = (Graphics2D) g;

		//bouton

		taille_bouton = mesureLargeur*5;

		g.setFont(h2);
		m = g.getFontMetrics();

		String[] bouton_string = {"Local", "En ligne"};

		int y;

		deb_bouton_y = centre_hauteur - mesureHauteur/2;
		for (int i = 0; i < bouton_string.length; i++) {
			y = centre_hauteur + i*(mesureHauteur+padding);
			bouton(drawable, selectBouton == i ? boutonSelected : bouton, bouton_string[i], centre_largeur, y, taille_bouton, mesureHauteur);
		}


	}

	/**
	 * Dessine un bouton avec un texte donné en paramètre
	 * @param drawable Objet Graphics2D
	 * @param bouton Image en arrière-plan du bouton
	 * @param string Texte du bouton
	 * @param x Coordonnée X du bouton
	 * @param y Coordonnée Y du bouton
	 * @param taille_bouton_l Largeur du bouton
	 * @param taille_bouton_h Hauteur du bouton
	 * */
	private void bouton(Graphics2D drawable, Image bouton,String string, int x, int y, int taille_bouton_l, int taille_bouton_h){
		tracer(drawable, bouton, x - taille_bouton_l/2, y - taille_bouton_h/2, taille_bouton_l, taille_bouton_h);
		drawable.drawString(string, x-m.stringWidth(string)/2, y+m.getHeight() - taille_bouton_h/2);
	}

	/**
	 * Dessine un bouton avec une image donné en paramètre
	 * @param drawable Objet Graphics2D
	 * @param bouton Image en arrière-plan du bouton
	 * @param image Image du bouton
	 * @param x Coordonnée X du bouton
	 * @param y Coordonnée Y du bouton
	 * @param taille_bouton_l Largeur du bouton
	 * @param taille_bouton_h Hauteur du bouton
	 * */
	private void bouton(Graphics2D drawable, Image bouton,Image image, int x, int y, int taille_bouton_l, int taille_bouton_h){
		tracer(drawable, bouton, x - taille_bouton_l/2, y - taille_bouton_h/2, taille_bouton_l, taille_bouton_h);
		tracer(drawable, image, x - taille_bouton_l/2, y - taille_bouton_h/2, taille_bouton_l, taille_bouton_h);
	}

	/**
	 * Met-à-jour l'interface en la redessinant
	 */
	@Override
	public void miseAJour() {
		repaint();
	}

	public void selectBouton(int index) {
		selectBouton= index;
		miseAJour();
	}

	public void unselectBouton() {
		selectBoutonExit = false;
		selectBouton = -1;
		miseAJour();
	}

	public void setAffichage(int affichage){
		this.affichage = affichage;
	}

	public void selectBoutonChoixJoueur(int j, int i) {
		choix_type[j] = i;
		miseAJour();
	}

	public int getH2Size() {
		return h2.getSize();
	}

	public FontMetrics getFontMetrics() {
		return m;
	}


	public void changerTexteBouton(String string){
		boutonString = string;
		repaint();
	}

	public void selectBoutonExit(Boolean bool){
		selectBoutonExit = bool;
		miseAJour();
	}
}