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
import java.util.LinkedList;

import static java.lang.Math.min;

public class MenuGraphique extends JComponent implements Observateur {
	Image bouton;
	int mesureLargeur;
	int mesureHauteur;
	int padding;
	int largeur, hauteur;

	int centre_largeur, centre_hauteur;

	int taille_bouton;

	int deb_bouton;

	Font h1, h2;

	MenuGraphique() {
		bouton = lisImage("BoutonL");
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

		mesureLargeur = min(largeur/6, hauteur/8);
		mesureHauteur = min(hauteur/8, largeur/6);
		padding = mesureLargeur / 4;

		centre_largeur = largeur / 2;
		centre_hauteur = hauteur / 2;

		h1 = new Font("Medieval English", Font.PLAIN, min(largeur/6, hauteur/8));
		h2 = new Font("Medieval English", Font.PLAIN, min(largeur/12, hauteur/16));

		FontMetrics m;

		//title
		g.setFont(h1);
		m = g.getFontMetrics();
		g.drawString("Antinomy", centre_largeur-m.stringWidth("Antinomy")/2, centre_hauteur/4+m.getHeight());

		//bouton
		taille_bouton = mesureLargeur*5;


		g.setFont(h2);
		m = g.getFontMetrics();

		String[] bouton_string = {"Nouvelle Partie", "En ligne", "Charger", "Tutoriel"};

		int y;
		deb_bouton = centre_hauteur -(mesureHauteur+padding);
		for (int i = -1; i < 3; i++) {
			y = centre_hauteur + i*(mesureHauteur+padding);
			tracer(drawable, bouton, centre_largeur - taille_bouton/2, y, taille_bouton, mesureHauteur);
			g.drawString(bouton_string[i+1], centre_largeur-m.stringWidth(bouton_string[i+1])/2, y+m.getHeight()+padding/2);
		}
	}
	@Override
	public void miseAJour() {
		repaint();
	}
}