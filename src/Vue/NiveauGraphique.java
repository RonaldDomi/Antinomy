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

public class NiveauGraphique extends JComponent implements Observateur {
	Image carteVide, carteDos, carteDosR, bleu, rouge, violet, vert, clef, crane, papier, champignon,
			diamant, diamantVide,
			codexBleu, codexVert, codexRouge, codexViolet, backCodex,
			fleche, bouton, boutonBlocked, carteSelect,
			load, save, undo, redo;
	Jeu j;
	int largeurCarte;
	int hauteurCarte;
	int padding;
	int deb_joueur;
	int deb_continuum;

	int deb_bouton;
	int largeur, hauteur;

	int indexCarteSelectionneeMain = -1;

	int joueurCourant = 0;

	int centre_largeur, centre_hauteur;

	int taille_bouton;


	int debParadoxInf = -1, finParadoxInf = -1;
	int debParadoxSup = -1, finParadoxSup = -1;

	// Décalage des éléments (pour pouvoir les animer)
	Vecteur [][] decalages;
	// Images du pousseur (pour l'animation)
	Image [][] pousseurs;
	int direction, etape;

	Font h1, fontCarte, h2;
	LinkedList<Integer> indexCarteSelectionneeContinuum;

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
		diamant = lisImage("Diamant");
		diamantVide = lisImage("DiamantVide");
		codexBleu = lisImage("CodexBleu");
		codexRouge = lisImage("CodexRouge");
		codexVert = lisImage("CodexVert");
		codexViolet = lisImage("CodexViolet");
		fleche = lisImage("Fleche");
		bouton = lisImage("Bouton");
		boutonBlocked = lisImage("BoutonBlocked");
		carteSelect = lisImage("CarteSelect");
		load = lisImage("Load");
		save = lisImage("Save");
		undo = lisImage("Undo");
		redo = lisImage("Redo");
		backCodex = lisImage("BackCodex");
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

		joueurCourant = j.getJoueurCourant();

		largeur = getSize().width;
		hauteur = getSize().height;

		largeurCarte = min(largeur/17, hauteur/6 * 54 / 84);
		hauteurCarte = min(hauteur/6, largeur/17 * 84 / 54);
		padding = largeurCarte / 4;

		centre_largeur = largeur / 2;
		centre_hauteur = hauteur / 2;

		h1 = new Font("Medieval English", Font.PLAIN, min(largeur/25, hauteur/12));
		fontCarte = new Font("Medieval English", Font.PLAIN, min(largeur/62, hauteur/30));
		//h2 = new Font("Medieval English", Font.PLAIN, min(largeur/12, hauteur/6));
		
		//Main
		paintMain(drawable);
		
		//Continuum + codex
		paintContinuum(drawable);

		//Poisitions joueurs
		paintPositionJoueurs(g);
		
		//Titre joueurs
		paintTitreJoueurs(g, drawable);
		
		//couleur interdite
		paintCouleurInterdite(drawable);
		
		//bouton
		deb_bouton = padding;
		taille_bouton = largeurCarte*3/4;
		for (int k = 0; k < 4; k++) {
			switch (k){
				case 0:
					tracer(drawable, bouton, (k+1)*padding + k*taille_bouton, padding, taille_bouton, taille_bouton);
					tracer(drawable, save, (k+1)*padding + k*taille_bouton, padding, taille_bouton, taille_bouton);
					break;
				case 1:
					tracer(drawable, bouton, (k+1)*padding + k*taille_bouton, padding, taille_bouton, taille_bouton);
					tracer(drawable, load, (k+1)*padding + k*taille_bouton, padding, taille_bouton, taille_bouton);
					break;
				case 2:
					if (j.getHistorique().peutAnnuler())
						tracer(drawable, bouton, (k+1)*padding + k*taille_bouton, padding, taille_bouton, taille_bouton);
					else
						tracer(drawable, boutonBlocked, (k+1)*padding + k*taille_bouton, padding, taille_bouton, taille_bouton);

					tracer(drawable, undo, (k+1)*padding + k*taille_bouton, padding, taille_bouton, taille_bouton);
					break;
				case 3:
					if (j.getHistorique().peutRefaire())
						tracer(drawable, bouton, (k+1)*padding + k*taille_bouton, padding, taille_bouton, taille_bouton);
					else
						tracer(drawable, boutonBlocked, (k+1)*padding + k*taille_bouton, padding, taille_bouton, taille_bouton);
					tracer(drawable, redo, (k+1)*padding + k*taille_bouton, padding, taille_bouton, taille_bouton);
					break;
			}
		}
	}

	
	private void paintMain(Graphics2D drawable){
		//Joueurs
		Carte[][] mains = new Carte[2][3];
		mains[0] = j.getInfoJoueurs()[0].getMain();
		mains[1] = j.getInfoJoueurs()[1].getMain();
		deb_joueur = centre_largeur + -1 * (largeurCarte + padding) - largeurCarte / 2;
		carteMain(drawable, mains);
	}
	
	
	private void paintContinuum(Graphics2D drawable){
		Carte[] continuum = j.getContinuumCarte();
		deb_continuum = centre_largeur + -4 * (largeurCarte + padding);
		carteContinuum(drawable, continuum);
	}
	
	
	private void paintTitreJoueurs(Graphics g, Graphics2D drawable){
		g.setFont(h1);
		FontMetrics m = g.getFontMetrics();
		String[] StringJoueur = new String[2];
		
		StringJoueur[joueurCourant] = "• ";
		StringJoueur[j.adversaire()] = "  ";
		
//		StringJoueur[0] += "Joueur " + 0 + "   " + j.getInfoJoueurs()[0].getPoints() + "/5";
		StringJoueur[0] += "Joueur " + 0;
//		StringJoueur[1] += "Joueur " + 1 + "   " + j.getInfoJoueurs()[1].getPoints() + "/5";
		StringJoueur[1] += "Joueur " + 1;

		//Texte joueur 0
//		g.drawString(StringJoueur[0], padding, hauteur-padding);
//		tracer(drawable, diamant, m.stringWidth(StringJoueur[0]) + 2*padding, hauteur-padding-largeurCarte/2,
//				largeurCarte/2, largeurCarte/2);

		g.drawString(StringJoueur[0], largeur - m.stringWidth(StringJoueur[0]) - 2*padding, hauteur - 2*padding - largeurCarte/2);

		//V1
		for (int i = -5; i<1; i++){
			if (5-j.getInfoJoueurs()[0].getPoints() >  i+5)
				tracer(drawable, diamantVide, largeur + i*(largeurCarte/2 + padding), hauteur - padding - largeurCarte/2, largeurCarte/2, largeurCarte/2);
			else
				tracer(drawable, diamant, largeur + i*(largeurCarte/2 + padding), hauteur - padding - largeurCarte/2, largeurCarte/2, largeurCarte/2);
		}

		//V2
//		tracer(drawable, diamant, largeur - padding - largeurCarte/2, hauteur - largeurCarte/2 - padding, largeurCarte/2, largeurCarte/2);

		//Texte joueur 1
		g.drawString(StringJoueur[1], largeur - m.stringWidth(StringJoueur[1]) - 2*padding, m.getHeight());

		//V1
		for (int i = -5; i<1; i++){
			if (5-j.getInfoJoueurs()[1].getPoints() > i+5)
				tracer(drawable, diamantVide, largeur + i*(largeurCarte/2 + padding), m.getHeight() + padding, largeurCarte/2, largeurCarte/2);
			else
				tracer(drawable, diamant, largeur + i*(largeurCarte/2 + padding), m.getHeight() + padding, largeurCarte/2, largeurCarte/2);
		}

//		V2
		//		tracer(drawable, diamant, largeur - padding - largeurCarte/2, m.getHeight() - largeurCarte/2, largeurCarte/2, largeurCarte/2);
	}
	
	
	private void paintPositionJoueurs(Graphics g){
		int x = centre_largeur + (j.getInfoJoueurs()[0].getSorcierIndice()-4) * (largeurCarte + padding);
		g.fillOval(x, centre_hauteur+hauteurCarte/2 + padding, largeurCarte, largeurCarte);
		x = centre_largeur + (j.getInfoJoueurs()[1].getSorcierIndice()-4) * (largeurCarte + padding);
		g.fillOval(x, centre_hauteur-hauteurCarte/2 - padding - largeurCarte, largeurCarte, largeurCarte);
	}
	
	
	private void paintCouleurInterdite(Graphics2D drawable){
		tracer(drawable, backCodex, largeur/8 - largeurCarte*2, centre_hauteur - largeurCarte*2,largeurCarte*4, largeurCarte*4);
		switch (j.getCodex().getCouleurInterdite()){
			case BLEU:
				tracer(drawable, codexBleu, largeur/8 - largeurCarte, centre_hauteur - largeurCarte,largeurCarte*2, largeurCarte*2);
				break;
			case VERT:
				tracer(drawable, codexVert, largeur/8 - largeurCarte, centre_hauteur - largeurCarte,largeurCarte*2, largeurCarte*2);
				break;
			case ROUGE:
				tracer(drawable, codexRouge, largeur/8 - largeurCarte, centre_hauteur - largeurCarte,largeurCarte*2, largeurCarte*2);
				break;
			case VIOLET:
				tracer(drawable, codexViolet, largeur/8 - largeurCarte, centre_hauteur - largeurCarte,largeurCarte*2, largeurCarte*2);
				break;
		}
		tracer(drawable, fleche, largeur/8 - largeurCarte*5/2, centre_hauteur - largeurCarte,largeurCarte*2, largeurCarte*2);
	}
	
	
	protected void carteMain(Graphics2D g, Carte[][] mains){
		int y = hauteur - hauteurCarte - padding;
		for (int j = 0; j<2; j++){
			for (int i = -1; i < 2; i++) {
				int x = centre_largeur + i * (largeurCarte + padding) - largeurCarte / 2;

				Couleur couleur = mains[j][i+1].getCouleur();
				Symbole symbole = mains[j][i+1].getSymbole();
				int numero = mains[j][i+1].getNumero();
				
				if (i+1 == indexCarteSelectionneeMain && j == this.j.getJoueurCourant()){
					dessinerCarte(g, x - padding/2, y - (1-j)*padding, largeurCarte+padding, hauteurCarte+padding, couleur, symbole, numero);
				}
				else{
					dessinerCarte(g, x, y, largeurCarte, hauteurCarte, couleur, symbole, numero);
				}
			}
			y = padding;
			
		}
	}

	protected void carteContinuum(Graphics2D g, Carte[] continuum){
		int x, i;
		if (indexCarteSelectionneeContinuum != null){
			for (Integer elem : indexCarteSelectionneeContinuum){
				x = centre_largeur + (elem-4) * (largeurCarte + padding);
				tracer(g, carteSelect, x-padding/4,  centre_hauteur-hauteurCarte / 2- padding/4, largeurCarte + padding/2, hauteurCarte+padding/2);
			}
		}

//		if (debParadoxInf >= 0){
//			tracer(g, carteSelectL, centre_largeur + (debParadoxInf-4) * (largeurCarte + padding) + padding*3/4, centre_hauteur-hauteurCarte / 2 - padding/4, 3*largeurCarte + padding/2, hauteurCarte+padding/2);
//		}
//
//		if (debParadoxSup >= 0){
//			tracer(g, carteSelectL, centre_largeur + (debParadoxSup-4) * (largeurCarte + padding) + padding*3/4, centre_hauteur-hauteurCarte / 2 - padding/4, 3*largeurCarte + padding/2, hauteurCarte+padding/2);
//		}
		

		for (i = -4; i < 5; i++) {
			x = centre_largeur + i * (largeurCarte + padding);

			Couleur couleur = continuum[i+4].getCouleur();
			Symbole symbole = continuum[i+4].getSymbole();
			int numero = continuum[i+4].getNumero();

			if (i+4 >= debParadoxInf && i+4 < finParadoxInf){
				x = x+(-(i + 4 - debParadoxInf - 1))*(padding-padding/4);
				tracer(g, carteSelect, x-padding/4,  centre_hauteur-hauteurCarte / 2- padding/4, largeurCarte + padding/2, hauteurCarte+padding/2);
			}
			else if (i+4 >= debParadoxSup && i+4 < finParadoxSup){
				x = x+(-(i + 4 - debParadoxSup - 1))*(padding-padding/4);
				tracer(g, carteSelect, x-padding/4,  centre_hauteur-hauteurCarte / 2- padding/4, largeurCarte + padding/2, hauteurCarte+padding/2);
			}
			dessinerCarte(g, x, centre_hauteur-hauteurCarte / 2, largeurCarte, hauteurCarte, couleur, symbole, numero);

		}

		x = centre_largeur + i * (largeurCarte + padding);
		tracer(g, carteDos, x, centre_hauteur-hauteurCarte / 2, largeurCarte, hauteurCarte);
	}

	private void dessinerCarte(Graphics2D g, int x, int y, int l, int h, Couleur couleur, Symbole symbole, int numero){
		tracer(g, carteVide, x, y, l, h);
		g.setFont(fontCarte);
		FontMetrics m = g.getFontMetrics();
		g.drawString(String.valueOf(numero), x+padding/2, y+m.getHeight()); //haut
		//g.drawString(numero+"", x+largeurCarte-padding/2-m.stringWidth(numero+""), y+hauteurCarte-padding/2); //bas
		
		y = y + padding/4;
		switch (couleur){
			case ROUGE:
				tracer(g, rouge, x, y, l, h);
				break;
			case VERT:
				tracer(g, vert, x, y, l, h);
				break;
			case VIOLET:
				tracer(g, violet, x, y, l, h);
				break;
			case BLEU:
				tracer(g, bleu, x, y, l, h);
				break;
		}
		switch (symbole){
			case CRANE:
				tracer(g, crane, x, y, l, h);
				break;
			case CLEF:
				tracer(g, clef, x, y, l, h);
				break;
			case CHAMPIGNON:
				tracer(g, champignon, x, y, l, h);
				break;
			case PAPIER:
				tracer(g, papier, x, y, l, h);
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
	}

	public void changeEtape() {
		etape = (etape + 1) % pousseurs[direction].length;
		miseAJour();
	}

	void selectionnerCarteMain(int index){
		indexCarteSelectionneeMain = index;
		miseAJour();
	}

	void selectionnerCarteContinuum(LinkedList<Integer> indices){
		indexCarteSelectionneeContinuum = indices;
		miseAJour();
	}

	void selectionnerParadox(int debParadoxInf, int finParadoxInf, int debParadoxSup, int finParadoxSup){
		this.debParadoxInf = debParadoxInf;
		this.finParadoxInf = finParadoxInf;
		this.debParadoxSup = debParadoxSup;
		this.finParadoxSup = finParadoxSup;
	}
}