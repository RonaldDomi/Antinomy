package Vue;
/*
 * Antinomy - Encore une nouvelle version (à but pédagogique) du célèbre jeu
 * Copyright (C) 2018 Guillaume Huard
 *
 * Ce programme est libre, vous pouvez le redistribuer et/ou le
 * modifier selon les termes de la Licence Publique Générale GNU publiée par la
 * Free Software Foundation (version 2 ou bien toute autre version ultérieure
 * choisie par vous).
 *
 * Ce programme est distribué car potentiellement utile, mais SANS
 * AUCUNE GARANTIE, ni explicite ni implicite, y compris les garanties de
 * commercialisation ou d'adaptation dans un but spécifique. Reportez-vous à la
 * Licence Publique Générale GNU pour plus de détails.
 *
 * Vous devez avoir reçu une copie de la Licence Publique Générale
 * GNU en même temps que ce programme ; si ce n'est pas le cas, écrivez à la Free
 * Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307,
 * États-Unis.
 *
 * Contact:
 *          Guillaume.Huard@imag.fr
 *          Laboratoire LIG
 *          700 avenue centrale
 *          Domaine universitaire
 *          38401 Saint Martin d'Hères
 */

import Global.Configuration;
import Modele.Carte;
import Modele.Couleur;
import Modele.Jeu;
import Modele.Symbole;
import Patterns.Observateur;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.LinkedList;

import static java.lang.Math.min;

public class MenuGraphique extends JComponent implements Observateur {
	Image bouton, boutonSelected;
	int mesureLargeur;
	int mesureHauteur;
	int padding;
	int largeur, hauteur;

	int centre_largeur, centre_hauteur;

	int taille_bouton;

	int deb_bouton_x;
	int deb_bouton_y;
	int deb_bouton_nouvelle_partie_y;

	int affichage = 1;

	Font h1, h2;

	FontMetrics m;

	int selectBouton = -1;

	String boutonString;

	int[] choix_type;

	public MenuGraphique() {
		bouton = lisImage("BoutonL");
		boutonSelected = lisImage("BoutonSelectedL");
		choix_type = new int[2];
		choix_type[0] = 0;
		choix_type[1] = 0;
	}

	private Image lisImage(String nom) {
		InputStream in = Configuration.ouvre("Images/" + nom + ".png");
		Configuration.info("Chargement de l'image " + nom);
		try {
			// Chargement d'une image utilisable dans Swing
			return ImageIO.read(in);
		} catch (Exception e) {
			Configuration.erreur("Impossible de charger l'image " + nom);
		}
		return null;
	}
	
	
	protected void tracer(Graphics2D g, Image i, int x, int y, int l, int h) {
		g.drawImage(i, x, y, l, h, null);
	}
	
	
	@Override
	public void paintComponent(Graphics g) {

		Graphics2D drawable = (Graphics2D) g;

		g.setColor(new Color(0, 0, 0));

		drawable.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		largeur = getSize().width;
		hauteur = getSize().height;

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
	}

	void nouvellePartie(Graphics g){

		String[] type_possible = {"Humain", "IA Aleatoire", "IA Difficile"};

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

		deb_bouton_y = centre_hauteur + (-1) * (mesureHauteur + padding) - mesureHauteur/2;
		deb_bouton_x = centre_largeur + (-2) * (taille_bouton + padding) + (taille_bouton + padding);

		for (int j = 0; j < 2; j++) {
			y = centre_hauteur + (j-1) * (mesureHauteur + padding);
			for (int i = 0; i < 4; i++) {
				if (i==0){
					x = centre_largeur - 2*(5 * mesureLargeur+padding) + (taille_bouton + padding)/2;
					g.drawString("Joueur " + j + " :", x-m.stringWidth("Joueur " + j + " :")/2, y+m.getHeight() - mesureHauteur/2);
				}
				else {
					x = centre_largeur + (i-2) * (5 * mesureLargeur + padding) + (taille_bouton + padding)/2;
					bouton((Graphics2D) g, choix_type[j] == i-1 ? boutonSelected : bouton, type_possible[i-1], x, y, taille_bouton, mesureHauteur);
				}
			}
		}

		deb_bouton_nouvelle_partie_y = hauteur*3/4 - mesureHauteur/2;
		bouton((Graphics2D) g, selectBouton == 1 ? boutonSelected : bouton, "Nouvelle Partie", centre_largeur, hauteur*3/4, taille_bouton, mesureHauteur);
	}

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
					bouton((Graphics2D) g, selectBouton == j ? boutonSelected : bouton, "Lancer", x, hauteur*3/4, taille_bouton, mesureHauteur);
					break;
			}
		}
	}
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

		String[] bouton_string = {"Nouvelle Partie", "En ligne", "Charger", "Tutoriel"};

		int y;

		deb_bouton_y = centre_hauteur - mesureHauteur/2;
		for (int i = 0; i < 4; i++) {
			y = centre_hauteur + i*(mesureHauteur+padding);
			bouton(drawable, selectBouton == i ? boutonSelected : bouton, bouton_string[i], centre_largeur, y, taille_bouton, mesureHauteur);
		}


	}

	private void bouton(Graphics2D drawable, Image bouton,String string, int x, int y, int taille_bouton_l, int taille_bouton_h){
		tracer(drawable, bouton, x - taille_bouton_l/2, y - taille_bouton_h/2, taille_bouton_l, taille_bouton_h);
		drawable.drawString(string, x-m.stringWidth(string)/2, y+m.getHeight() - taille_bouton_h/2);
	}

	@Override
	public void miseAJour() {
		repaint();
	}

	public void selectBouton(int index) {
		selectBouton= index;
		miseAJour();
	}

	public void unselectBouton() {
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
		System.out.println("ici " + string);
		repaint();
	}
}