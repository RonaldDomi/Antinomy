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

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

import static java.lang.Math.abs;

public class Jeu extends Observable implements Cloneable{
	///////////
	Random r;

	int tour; //0 à +inf
	Continuum continuum;
	InfoJoueur[] infoJoueurs; //2 infoJoueurs
	Codex codex;
	Historique historique;
	int joueurCourant; //0 ou 1
	int joueurGagnant; //0 ou 1
	
	
	
	public Jeu() {
		r = new Random();

		Deck d = new Deck();
		continuum = new Continuum(d.distribuer(9));
		infoJoueurs = new InfoJoueur[2];
		infoJoueurs[0] = new InfoJoueur(1, r);
		infoJoueurs[0].setMain(d.distribuer(3));
		infoJoueurs[1] = new InfoJoueur(-1, r);
		infoJoueurs[1].setMain(d.distribuer(3));
		historique = new Historique();

		codex = new Codex(d.distribuer(1)[0], continuum.getCarteContinuum(0).getCouleur());
		Jeu jeuClone = new Jeu(this);

		historique.ajouter_jeu(jeuClone);

		joueurCourant = 0;
		joueurGagnant = -1;

		tour = 0;
	}

	public Jeu(String stringJeu){
//		continuum;info[0];info[1];tour;codex;joueurCourant;joueurGagnant
		String[] stringJeuSep = stringJeu.split(";");
		this.continuum = new Continuum(stringJeuSep[0]);
		this.infoJoueurs = new InfoJoueur[2];
		this.infoJoueurs[0] = new InfoJoueur(stringJeuSep[1]);
		this.infoJoueurs[1] = new InfoJoueur(stringJeuSep[2]);
		this.tour = Integer.parseInt(stringJeuSep[3]);
		this.codex = new Codex(stringJeuSep[4]);
		this.joueurCourant = Integer.parseInt(stringJeuSep[5]);
		this.joueurGagnant = Integer.parseInt(stringJeuSep[6]);
	}

	public Jeu(Jeu j){
		this.r = j.r;
		this.joueurCourant = j.joueurCourant;
		this.joueurGagnant = j.joueurGagnant;
		this.tour = j.tour;

		this.infoJoueurs = new InfoJoueur[2];
		try{
			this.infoJoueurs[0] = j.infoJoueurs[0].clone();
			this.infoJoueurs[1] = j.infoJoueurs[1].clone();
			this.continuum = j.continuum.clone();
		}catch(CloneNotSupportedException e){
			System.out.println("Could not clone Jeu in Jeu.constructor(jeu)");
		}
		this.historique = j.historique;
		this.codex = j.codex.clone();
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

	public boolean coupChangerPositionSorcier(int indexCarte){
		java.util.LinkedList<Integer> indexPossible = continuum.getIndexSorcierPossible(codex.getCouleurInterdite());
		
		for (Integer index: indexPossible) {
			if (index == indexCarte){
				getInfoJoueurCourant().setSorcierIndice(indexCarte);
				metAJour();
				return true;
			}
		}

		return false;
	}

	public boolean coupEchangeCarteMainContinuum(int indexMain, int indexContinuum){
		java.util.LinkedList<Integer> indexPossible = continuum.getCoupsPossibles(getInfoJoueurCourant().getCarteMain(indexMain), getInfoJoueurCourant().getSorcierIndice(), getInfoJoueurCourant().getDirection());
		
		for (Integer index: indexPossible) {
			if (index == indexContinuum){
				infoJoueurs[joueurCourant].setSorcierIndice(indexContinuum);
				echangerCarteMainContinuum(indexMain, indexContinuum);
				metAJour();
				return true;
			}
		}
		return false;
	}

	public boolean coupParadox(int direction){

		if(!getInfoJoueurCourant().existeParadox(codex.getCouleurInterdite()))
			return false;

		if(!((direction == 1 && existeParadoxSuperieur()) || (direction == -1 && existeParadoxInferieur())))
			return false;

		Collections.shuffle(Arrays.asList(getInfoJoueurCourant().getMain()));
		if (direction == 1){
			int indexMain=2;
			for (int i = 0; i < 3; i++) {
				echangerCarteMainContinuum(indexMain,
						getInfoJoueurCourant().getSorcierIndice() + ((i+1) * direction * getInfoJoueurCourant().getDirectionMouvement()));
				indexMain--;
			}

		}else{
			int indexMain=0;
			for (int i = 0; i < 3; i++) {
				echangerCarteMainContinuum(indexMain, getInfoJoueurCourant().getSorcierIndice() + ((i+1) * direction* getInfoJoueurCourant().getDirectionMouvement()));
				indexMain++;
			}
		}

		infoJoueurs[joueurCourant].addPoint();
		codex.cycleCouleur();
		jeuGagnant();
		metAJour();
		return true;
	}

	public boolean coupClash(){
		int res = gagnantClash();
		if (res != -1){
			if (infoJoueurs[1-res].getPoints() > 0){
				infoJoueurs[1-res].remPoint();
				infoJoueurs[res].addPoint();
				codex.cycleCouleur();
				jeuGagnant();
				metAJour();
				return true;
			}
			return false;
		}
		return false;
	}

	public int adversaire(){
		return 1-joueurCourant;
	}

	public void finTour(){
		joueurCourant = adversaire();
		Jeu jeuClone = null;
		tour++;
		try{
			jeuClone = clone();
		}catch(CloneNotSupportedException e){
			System.out.println("Error on update historique: clone failed\n");
		}
		historique.ajouter_jeu(jeuClone);
	}

	public void finTour(boolean hist){
		joueurCourant = adversaire();
		Jeu jeuClone = null;
		tour++;
		try{
			jeuClone = clone();
		}catch(CloneNotSupportedException e){
			System.out.println("Error on update historique: clone failed\n");
		}
		if( hist)
			historique.ajouter_jeu(jeuClone);
	}

	public boolean existeClash(){
		return infoJoueurs[0].getSorcierIndice() == infoJoueurs[1].getSorcierIndice();
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

	public boolean existeParadoxSuperieur(){
		return infoJoueurs[joueurCourant].getSorcierIndice()+3*infoJoueurs[joueurCourant].getDirection() < continuum.getContinuumSize() &&
				infoJoueurs[joueurCourant].getSorcierIndice()+3*infoJoueurs[joueurCourant].getDirection() >= 0;
	}

	public boolean existeParadoxInferieur(){
		return infoJoueurs[joueurCourant].getSorcierIndice()-3*infoJoueurs[joueurCourant].getDirection() < continuum.getContinuumSize() &&
				infoJoueurs[joueurCourant].getSorcierIndice()-3*infoJoueurs[joueurCourant].getDirection() >= 0;
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
	public Jeu clone() throws CloneNotSupportedException {
		Jeu jClone = new Jeu(this);
		return jClone;
	}

	public void charger(Jeu j){
		this.codex = j.codex;
		this.infoJoueurs = j.infoJoueurs;
		this.continuum = j.continuum;
		this.historique = j.historique;

		this.r = j.r;
		this.joueurGagnant = j.joueurGagnant;
		this.joueurCourant = j.joueurCourant;
		this.tour = j.tour;
		metAJour();
	}

	public Codex getCodex(){return codex;}

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
