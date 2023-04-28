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

	public boolean jouer_coup(int indexCarte, int indexContinuum, int direction_paradox){

//		if (ligne == 0 && colonne == 0){
//			gagnant = joueur_courant;
//		}
//		Coup nouveau_coup =  plateau.placer_coup(joueur_courant, ligne, colonne, tour);
//		historique.ajouter_coup(nouveau_coup);
//		tour++;
//		joueur_courant = (1 - (joueur_courant - 1)) + 1;
//		metAJour();
//		return true;

		InfoJoueur info_joueur_courant = infoJoueurs[joueurCourant];

		//TODO
		// calcule les options pour chaque carte(aide a IA et anti-idiot si l'utilisateur change la carte beacoup beacoup // todo dans continum

		// ETAPE 1
		// prend input, la carte choisis de l'utilisateur // todo dans infoJoueur
		// prend input, la position ou on veux aller et la carte change //todo dans infojoueur?

		// ETAPE 2
		// echange cartes // todo dans jeu.java
		echangerCarteMainContinuum(indexCarte, indexContinuum);
		// moveSorcier // todo dans infojoueur
		info_joueur_courant.moveSorcier(indexContinuum);

		// ETAPE 3
		// si active paradox, traitement // todo pas specifiee

		// ETAPE 4
		// clash() // todo dans jeu

		// VERIF GAGNE // en place
		// TRAITEMENTE GAGNE // todo continuum, infojoueur, codex

		return true;
	}

	void echangerCarteMainContinuum(int carteMainIndice, int carteContinuumIndice){
		//todo
		//change le carte du main donne par l'utilisateur avec la carte dans le continuum
		continuum.setCarteContinuum(carteContinuumIndice, infoJoueurs[joueurCourant].changeCarte(carteMainIndice, continuum.getCarteContinuum(carteContinuumIndice)));
		//renvoie rien
	}
	
	boolean clash(){
		int gagnantClash()
	}

	int gagnantClash(){
		//-1 si égalité sinon index du gagnant
		if (infoJoueurs[0].sommeMain() > infoJoueurs[1].sommeMain()){
			return 0;
		} else if (infoJoueurs[0].sommeMain() < infoJoueurs[1].sommeMain()) {
			return 1;
		}
		else{
//			if (infoJoueurs[0].getCarteMain(r.nextInt(2));
		}
	}

	boolean existeParadoxSuperieur(){
		return infoJoueurs[joueurCourant].getSorcierIndice() < 3;
	}

	boolean existeParadoxInferieur(){
		return infoJoueurs[joueurCourant].getSorcierIndice() > continuum.getContinuumSize() - 3;
	}
}
