package Controleur;
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
//import Structures.Iterateur;
//import Structures.Sequence;
import Vue.CollecteurEvenements;
import Vue.InterfaceUtilisateur;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ControleurMediateur implements CollecteurEvenements {
	Jeu jeu;

	Joueur[][] joueurs;
	int [] typeJoueur;

	InterfaceUtilisateur vue;

//	Sequence<Animation> animations;
	double vitesseAnimations;
	int lenteurPas;
	Animation mouvement;
	boolean animationsSupportees, animationsActives;
	int lenteurJeuAutomatique;
	boolean IAActive;

	Socket clientSocket;

	final int lenteurAttente = Configuration.lenteurAttente;
	int decompte;

	public ControleurMediateur(Jeu j) {
		jeu = j;
		joueurs = new Joueur[2][4];
		typeJoueur = new int[4];
		for (int i = 0; i < joueurs.length; i++) {
			joueurs[i][0] = new JoueurHumain(i, jeu);
			joueurs[i][1] = new JoueurAIAleatoire(i, jeu);
			joueurs[i][2] = new JoueurAI(i, jeu);
			joueurs[i][3] = new JoueurEnLigne(i, jeu);
			typeJoueur[i] = 0;
		}
//		if (Configuration.typeJoueur == 3)
//		typeJoueur[Configuration.typeJoueur] = 3;

//		animations = Configuration.nouvelleSequence();
//		animations.insereTete(new AnimationPousseur(lenteurPas, this));
		mouvement = null;
		// Tant qu'on ne reçoit pas d'évènement temporel, on n'est pas sur que les
		// animations soient supportées (ex. interface textuelle)
		animationsSupportees = false;
		animationsActives = false;
	}



	public void ajouteInterfaceUtilisateur(InterfaceUtilisateur v) {
		vue = v;
		for (Joueur[] joueur : joueurs) {
			joueur[0].ajouteInterfaceUtilisateur(vue);
			
//			joueurs[i][1].ajouteInterfaceUtilisateur(vue);
		}
	}

	
	
	public void ajouteSocket(Socket clientSocket){
		this.clientSocket = clientSocket;
		for (Joueur[] joueur: joueurs) {
			joueur[3].ajouteSocket(clientSocket);
			joueur[0].ajouteSocket(clientSocket);
		}
	}
	
	
	@Override
	public void clicSouris(int l, int c) {

		// Lors d'un clic, on le transmet au joueur courant.
		// Si un coup a effectivement été joué (humain, coup valide), on change de joueur.

		if (jeu.getJoueurGagnant() == -1){
			if (joueurs[jeu.getJoueurCourant()][typeJoueur[jeu.getJoueurCourant()]].jeu(l, c)) {
				decompte = lenteurAttente;
				if (typeJoueur[jeu.getJoueurCourant()] == 3){
					joueurs[jeu.getJoueurCourant()][typeJoueur[jeu.getJoueurCourant()]].envoyerJeu();
				}
			}
		}
	}

	@Override
	public void clicSourisBouton(int index){
		switch (index){
			case 0: //save
				vue.sauvegarder();
				break;
			case 1: //charger
				vue.charger();
				envoyerCommandeSocket("LOAD");
				envoyerCommandeSocket(jeu.toString());
				resetSelection();
				break;
			case 2: //undo
				jeu.undo();
				envoyerCommandeSocket("UNDO");
				resetSelection();
				break;
			case 3: //redo
				jeu.redo();
				envoyerCommandeSocket("REDO");
				resetSelection();
				break;
			case 4: //restart
				jeu.charger(new Jeu(), false);
				jeu.historique = new Historique(jeu);
				resetSelection();
				break;
		}
		jeu.metAJour();
	}


	@Override
	public void clicSourisBoutonMenu(int index){
		switch (index){
			case 0: //Nouvelle partie
				vue.setAffichage(0, 2);
				break;
			case 1: //Charger
				break;
			case 2: //En ligne;
				break;
			case 3: //Tutoriel
				break;
		}

	}

	private void resetSelection(){
		vue.selectionnerParadox(-1, -1, -1, -1);
		vue.selectionnerCarteContinuum(null);
		vue.selectionnerCarteMain(-1);
	}

	private void testFin() {
//		if (jeu.niveauTermine()) {
//			jeu.prochainNiveau();
//			if (jeu.jeuTermine())
//				System.exit(0);
//		}
	}


	@Override
	public void toucheClavier(String touche) {
		switch (touche) {
			case "Left":
				jeu.undo();
				resetSelection();
				break;
			case "Right":
				jeu.redo();
				resetSelection();
				break;
			case "Import":
				vue.charger();
				jeu.metAJour();
				break;
			case "Save":
				vue.sauvegarder();
				jeu.metAJour();
				break;
			case "Quit":
				System.exit(0);
				break;
			case "Pause":
//				basculeAnimations();
				break;
			case "IA":
//				basculeIA();
				break;
			case "Full":
				vue.toggleFullscreen();
				break;
			default:
				System.out.println("Touche inconnue : " + touche);
		}
	}

	@Override
//	public void tictac() {
//		// On sait qu'on supporte les animations si on reçoit des évènements temporels
//		if (!animationsSupportees) {
//			animationsSupportees = true;
//			animationsActives = Configuration.animations;
//		}
//		// On traite l'IA séparément pour pouvoir l'activer même si les animations
//		// "esthétiques" sont désactivées
//		if (IAActive && (mouvement == null)) {
//			animationIA.tictac();
//		}
//		if (animationsActives) {
//			Iterateur<Animation> it = animations.iterateur();
//			while (it.aProchain()) {
//				Animation a = it.prochain();
//				a.tictac();
//				if (a.estTerminee()) {
//					if (a == mouvement) {
//						testFin();
//						mouvement = null;
//					}
//					it.supprime();
//				}
//			}
//		}
//	}

	public void tictac() {
		if (jeu.getJoueurGagnant() == -1) {
			if (decompte == 0) {
//				System.out.println("tic");
				int type = typeJoueur[jeu.getJoueurCourant()];
				// Lorsque le temps est écoulé on le transmet au joueur courant.
				// Si un coup a été joué (IA) on change de joueur.
				if (joueurs[jeu.getJoueurCourant()][type].tempsEcoule()) {
					decompte = lenteurAttente;
				} else {
					if (typeJoueur[jeu.getJoueurCourant()] == 0){
						joueurs[jeu.getJoueurCourant()][0].afficherPreSelection();
					}
					// Sinon on indique au joueur qui ne réagit pas au temps (humain) qu'on l'attend.
//					System.out.println("On vous attend, joueur " + joueurs[joueurCourant][type].num());
					decompte = lenteurAttente;
				}
			} else {
				decompte--;
			}
		}
	}

	public void basculeAnimations() {
		if (animationsSupportees && (mouvement == null))
			animationsActives = !animationsActives;
	}

	public void basculeIA() {
//		if (animationsSupportees) {
//			if (joueurAutomatique == null) {
//				joueurAutomatique = IA.nouvelle(jeu);
//				if (joueurAutomatique != null) {
//					animationIA = new AnimationJeuAutomatique(lenteurJeuAutomatique, joueurAutomatique, this);
//				}
//			}
//			if (joueurAutomatique != null)
//				IAActive = !IAActive;
//		}
	}



	public void nouvellePartie(int type_j0, int type_j1){
		typeJoueur[0] = type_j0;
		typeJoueur[1] = type_j1;
		vue.setAffichage(1, -1);
	}
	
	public void envoyerCommandeSocket(String toSend) {
		
		if (clientSocket!=null){
			System.out.println("En ligne");
			PrintWriter outgoing;
			try {
				outgoing = new PrintWriter(clientSocket.getOutputStream(), true);
				outgoing.println(toSend);
			} catch (IOException ignored) {
				;
			}
		}
	}
	
	public void setTypeJoueur(int joueur, int i){
		typeJoueur[joueur] = i;
	}
	
}
