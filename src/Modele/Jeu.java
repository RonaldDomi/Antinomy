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

public class Jeu extends Observable {
	//To delete
	Niveau n;
	///////////
	LecteurNiveaux l;

	Random r;

	int tour; //0 à +inf
	Continuum continuum;
	InfoJoueur[] infoJoueurs; //2 infoJoueurs
	Codex codex;
	int joueurCourant; //0 ou 1
	int joueurGagnant; //0 ou 1
	
	
	
	public Jeu(LecteurNiveaux lect) {
		l = lect;
		r = new Random();
//		prochainNiveau();
	}

	public Niveau niveau() {
		return n;
	}

//	public Coup elaboreCoup(int x, int y) {
//		return n.elaboreCoup(x, y);
//	}
//
//	public void joue(Coup c) {
//		n.joue(c);
//		metAJour();
//	}

	public void prochainNiveau() {
		n = l.lisProchainNiveau();
	}

	public boolean niveauTermine() {
		return n.estTermine();
	}

	public boolean jeuTermine() {
		return n == null;
	}

	public int lignePousseur() {
		return n.lignePousseur();
	}

	public int colonnePousseur() {
		return n.colonnePousseur();
	}

	void echangerCarteMainContinuum(int carteMainIndice, int carteContinuumIndice){
		//todo
		//change le carte du main donne par l'utilisateur avec la carte dans le continuum
		continuum.setCarteContinuum(carteContinuumIndice, infoJoueurs[joueurCourant].changeCarte(carteMainIndice, continuum.getCarteContinuum(carteContinuumIndice)));
		//renvoie rien
	}

	public void coupEchangeCarteMainContinium(int indexCarte, int indexContinuum){
		echangerCarteMainContinuum(indexCarte, indexContinuum);
		infoJoueurs[joueurCourant].moveSorcier(indexContinuum);
	}

	public void coupParadox(boolean faireParadox, int direction){

	}

	boolean coupClash(){
		gagnantClash();
		return true;
	}

	int gagnantClash(){
		//-1 si égalité sinon index du gagnant
		int sommeJ0 = infoJoueurs[0].sommeMain();
		int sommeJ1 = infoJoueurs[1].sommeMain();

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

	boolean existeParadoxSuperieur(){
		return infoJoueurs[joueurCourant].getSorcierIndice() < 3;
	}

	boolean existeParadoxInferieur(){
		return infoJoueurs[joueurCourant].getSorcierIndice() > continuum.getContinuumSize() - 3;
	}
}
