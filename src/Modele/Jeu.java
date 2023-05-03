package Modele;
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
 * commercialisation ou d'adaptation dindex_coups_possibles un but spécifique. Reportez-vous à la
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

import Patterns.Observable;

import java.util.LinkedList;
import java.util.Random;

import static java.lang.Math.abs;

public class Jeu extends Observable implements Cloneable{
	///////////
	LecteurNiveaux l;

	Random r;

	int tour; //0 à +inf
	Continuum continuum;
	InfoJoueur[] infoJoueurs; //2 infoJoueurs
	Codex codex;
	Historique historique;
	int joueurCourant; //0 ou 1
	int joueurGagnant; //0 ou 1
	
	
	
	public Jeu(LecteurNiveaux lect) {
		l = lect;
		r = new Random();

		Deck d = new Deck();
		continuum = new Continuum(d.distribuer(9));
		infoJoueurs = new InfoJoueur[2];
		infoJoueurs[0] = new InfoJoueur(1, r);
		infoJoueurs[0].setMain(d.distribuer(3));
		infoJoueurs[1] = new InfoJoueur(-1, r);
		infoJoueurs[1].setMain(d.distribuer(3));
		codex = new Codex(d.distribuer(1)[0]);

		joueurCourant = 0;
		joueurGagnant = -1;

		tour = 0;
	}

	public Jeu(String stringJeu){
//		continuum;info[0];info[1];tour;codex;joueurCourant;joueurGagnant
		String[] stringJeuSep = stringJeu.split(";");
		this.continuum = new Continuum(stringJeuSep[0]);
		this.infoJoueurs[0] = new InfoJoueur(stringJeuSep[1]);
		this.infoJoueurs[1] = new InfoJoueur(stringJeuSep[2]);
		this.tour = Integer.parseInt(stringJeuSep[3]);
		this.codex = new Codex(stringJeuSep[4]);
		this.joueurCourant = Integer.parseInt(stringJeuSep[5]);
		this.joueurGagnant = Integer.parseInt(stringJeuSep[6]);

		this.tour = Integer.parseInt(stringJeuSep[2]);
	}

//	public Coup elaboreCoup(int x, int y) {
//		return n.elaboreCoup(x, y);
//	}
//
//	public void joue(Coup c) {
//		n.joue(c);
//		metAJour();
//	}

	void echangerCarteMainContinuum(int carteMainIndice, int carteContinuumIndice){
		//change la carte de la main donnée par l'utilisateur avec la carte dans le continuum
		continuum.setCarteContinuum(carteContinuumIndice, infoJoueurs[joueurCourant].changeCarte(carteMainIndice, continuum.getCarteContinuum(carteContinuumIndice)));
		//renvoie rien
	}

	public void coupChangerPositionSorcier(int indexCarte){
		getInfoJoueurCourant().setSorcierIndice(indexCarte);
		metAJour();
	}

	public void coupEchangeCarteMainContinuum(int indexCarte, int indexContinuum){
		infoJoueurs[joueurCourant].setSorcierIndice(indexContinuum);
		echangerCarteMainContinuum(indexCarte, indexContinuum);
		metAJour();
	}

	public void coupParadox(int direction){
		Collections.shuffle(Arrays.asList(getInfoJoueurCourant().getMain()));


		if (direction == 1){
			int indexMain=2;
			for (int i = 0; i < 3; i++) {
				echangerCarteMainContinuum(indexMain, getInfoJoueurCourant().getSorcierIndice() + ((i+1)));
				indexMain--;
			}

		}else{
			int indexMain=0;
			for (int i = 0; i < 3; i++) {
				echangerCarteMainContinuum(indexMain, getInfoJoueurCourant().getSorcierIndice() + ((i+1) * direction));
				indexMain++;
			}
		}

		infoJoueurs[joueurCourant].addPoint();
		jeuGagnant();
		metAJour();
	}

	boolean coupClash(){
		int res = gagnantClash();
		if (res != -1){
			if (infoJoueurs[1-res].getPoints() > 0){
				infoJoueurs[1-res].remPoint();
				infoJoueurs[res].addPoint();
				jeuGagnant();
				metAJour();
				return true;
			}
			return false;
		}
		return false;
	}

	int adversaire(){
		return 1-joueurCourant;
	}

	void finTour(){
		joueurCourant = adversaire();
		tour++;
	}



	//-1 si égalité sinon index du gagnant
	int gagnantClash(){
		int sommeJ0 = infoJoueurs[0].sommeMain(codex.getCouleurInterdite());
		int sommeJ1 = infoJoueurs[1].sommeMain(codex.getCouleurInterdite());

		if (sommeJ0 == sommeJ1){
			sommeJ0 = infoJoueurs[0].getCarteAleatoire().getNumero();
			sommeJ1 = infoJoueurs[1].getCarteAleatoire().getNumero();
		}

		if (sommeJ0 == sommeJ1){
			return -1;
		}
		else if (sommeJ0 > sommeJ1){
			return 0;
		} else {
			return 1;
		}
	}

	public boolean egaliteClash(){
		return infoJoueurs[0].sommeMain(codex.getCouleurInterdite()) == infoJoueurs[1].sommeMain(codex.getCouleurInterdite());
	}

	void jeuGagnant(){
		if (infoJoueurs[0].getPoints() >= 5)
			joueurGagnant = 0;
		else if (infoJoueurs[1].getPoints() >= 5)
			joueurGagnant = 1;
	}

	boolean existeParadoxSuperieur(){
		return infoJoueurs[joueurCourant].getSorcierIndice() < 3;
	}

	boolean existeParadoxInferieur(){
		return infoJoueurs[joueurCourant].getSorcierIndice() > continuum.getContinuumSize() - 3;
	}

	public Carte[] getMainJoueurCourant(){
		return infoJoueurs[joueurCourant].getMain();
	}

	public InfoJoueur[] getInfoJoueurs(){
		return infoJoueurs;
	}

	public InfoJoueur getInfoJoueurCourant(){
		return infoJoueurs[joueurCourant];
	}

	public Carte[] getContinuumCarte(){
		return continuum.getContinuum();
	}

	public Continuum getContinuum(){
		return continuum;
	}

	public Historique getHistorique(){ return historique;}

	public int getJoueurCourant(){
		return joueurCourant;
	}
	public Jeu clone() throws CloneNotSupportedException{
		Jeu j = (Jeu) super.clone();
		j.l = l;
		j.r = r;
		j.joueurCourant = joueurCourant;
		j.joueurGagnant = joueurGagnant;
		j.tour = tour;
		for (int i = 0; i < 2; i++) {
			j.infoJoueurs[i] = infoJoueurs[i].clone();
		}
		j.codex = codex.clone();
		j.continuum = continuum.clone();
		return j;
	}


	@Override
	public String toString() {
//		continuum;info[0];info[1];tour;codex;joueurCourant;joueurGagnant
		return continuum.toString() + ";" +
				infoJoueurs[0].toString() + ";" +
				infoJoueurs[1].toString() + ";" +
				tour + ";" +
				codex.toString() + ";" +
				joueurCourant + ";" +
				joueurGagnant + ";";
	}
}
