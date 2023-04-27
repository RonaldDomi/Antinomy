package Vue;
/*
 * Sokoban - Encore une nouvelle version (à but pédagogique) du célèbre jeu
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
import Modele.*;
import Patterns.Observateur;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.util.List;

import static java.lang.Math.min;

public class NiveauGraphique extends JComponent implements Observateur {
	Image carteVide, carteDos, carteDosR, bleu, rouge, violet, vert, clef, crane, papier, champignon;
	Jeu j;
	int largeurCarte;
	int hauteurCarte;
	int padding;

	int deb_joueur;
	int deb_continuum;
	int largeur, hauteur;

	int centre_largeur, centre_hauteur;


	// Décalage des éléments (pour pouvoir les animer)
	Vecteur [][] decalages;
	// Images du pousseur (pour l'animation)
	Image [][] pousseurs;
	int direction, etape;

	NiveauGraphique(Jeu jeu) {
		j = jeu;
		j.ajouteObservateur(this);
		carteDos = lisImage("Carte_dos");
		carteDosR = lisImage("Carte_dos_R");
		carteVide = lisImage("Carte_vide");
		vert = lisImage("Vert");
		violet = lisImage("Violet");
		bleu = lisImage("Bleu");
		rouge = lisImage("Rouge");
		crane = lisImage("Crane");
		clef = lisImage("Clef");
		papier = lisImage("Papier");
		champignon = lisImage("Champignon");
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

	public void paintComponent(Graphics g) {
		Graphics2D drawable = (Graphics2D) g;

		largeur = getSize().width;
		hauteur = getSize().height;

		largeurCarte = min(largeur/14, hauteur/6 * 54 / 84);
		hauteurCarte = min(hauteur/6, largeur/14 * 84 / 54);
		padding = largeurCarte / 4;

		centre_largeur = largeur / 2;
		centre_hauteur = hauteur / 2;

		int x;

		int i;

		Couleur couleur;
		Symbole symbole;
		int numero;
		List<Carte> main = InfoJoueur.mockMain();
		List<Carte> continuum = Continuum.mockContinuum();

		//Joueurs
		deb_joueur = centre_largeur + -1 * (largeurCarte + padding) - largeurCarte / 2;
		for (i = -1; i < 2; i++) {
			x = centre_largeur + i * (largeurCarte + padding) - largeurCarte / 2;

			couleur = main.get(i+1).getCouleur();
			symbole = main.get(i+1).getSymbole();
			numero = main.get(i+1).getNumero();

			tracer(drawable, carteDos, x, 0, largeurCarte, hauteurCarte);
			dessinerCarte(drawable, x, hauteur - hauteurCarte, couleur, symbole, numero);
		}

		deb_continuum = centre_largeur + -5 * (largeurCarte + padding);
		//Continuum
		for (i = -5; i < 4; i++) {
			x = centre_largeur + i * (largeurCarte + padding);

			couleur = continuum.get(i+5).getCouleur();
			symbole = continuum.get(i+5).getSymbole();
			numero = continuum.get(i+5).getNumero();

			dessinerCarte(drawable, x, centre_hauteur-hauteurCarte / 2, couleur, symbole, numero);
		}

		//Codex
		x = centre_largeur + i * (largeurCarte + padding);
		tracer(drawable, carteDosR, x, centre_hauteur-largeurCarte / 2, hauteurCarte, largeurCarte);

		//Positions Joueurs
		g.fillOval(centre_largeur, centre_hauteur+hauteurCarte/2 + padding, largeurCarte, largeurCarte);
		g.fillOval(centre_largeur, centre_hauteur-hauteurCarte/2 - padding - largeurCarte, largeurCarte, largeurCarte);
	}

	protected void tracer(Graphics2D g, Image i, int x, int y, int l, int h) {
		g.drawImage(i, x, y, l, h, null);
	}

	private void dessinerCarte(Graphics2D g, int x, int y, Couleur couleur, Symbole symbole, int numero){
		tracer(g, carteVide, x, y, largeurCarte, hauteurCarte);
		switch (couleur){
			case ROUGE:
				tracer(g, rouge, x, y, largeurCarte, hauteurCarte);
				break;
			case VERT:
				tracer(g, vert, x, y, largeurCarte, hauteurCarte);
				break;
			case VIOLET:
				tracer(g, violet, x, y, largeurCarte, hauteurCarte);
				break;
			case BLEU:
				tracer(g, bleu, x, y, largeurCarte, hauteurCarte);
				break;
		}
		switch (symbole){
			case CRANE:
				tracer(g, crane, x, y, largeurCarte, hauteurCarte);
				break;
			case CLEF:
				tracer(g, clef, x, y, largeurCarte, hauteurCarte);
				break;
			case CHAMPIGNON:
				tracer(g, champignon, x, y, largeurCarte, hauteurCarte);
				break;
			case PAPIER:
				tracer(g, papier, x, y, largeurCarte, hauteurCarte);
				break;
		}
	}

	@Override
	public void miseAJour() {
		repaint();
	}

	public void decale(int l, int c, double dl, double dc) {
		if ((dl != 0) || (dc != 0)) {
			Vecteur v = decalages[l][c];
			if (v == null) {
				v = new Vecteur();
				decalages[l][c] = v;
			}
			v.x = dc;
			v.y = dl;
		} else {
			decalages[l][c] = null;
		}
		miseAJour();
	}

	// Animation du pousseur
	void metAJourPousseur() {
//		pousseur = pousseurs[direction][etape];
	}

	public void metAJourDirection(int dL, int dC) {
		switch (dL + 2 * dC) {
			case -2:
				direction = 1;
				break;
			case -1:
				direction = 0;
				break;
			case 0:
				// Rien, pas de mouvement, direction inchangée
				break;
			case 1:
				direction = 2;
				break;
			case 2:
				direction = 3;
				break;
			default:
				Configuration.erreur("Bug interne, direction invalide");
		}
		metAJourPousseur();
	}

	public void changeEtape() {
		etape = (etape + 1) % pousseurs[direction].length;
		metAJourPousseur();
		miseAJour();
	}
}