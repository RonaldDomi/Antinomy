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

public class ControleurMediateur implements CollecteurEvenements {
	Jeu jeu;

	int etape = 0;
	int indexCarteMain = -1;
	int indexCarteContinuum = -1;

	InterfaceUtilisateur vue;
//	Sequence<Animation> animations;
	double vitesseAnimations;
	int lenteurPas;
	Animation mouvement;
	boolean animationsSupportees, animationsActives;
	int lenteurJeuAutomatique;
	IA joueurAutomatique;
	boolean IAActive;
	AnimationJeuAutomatique animationIA;

	public ControleurMediateur(Jeu j) {
		jeu = j;

//		animations = Configuration.nouvelleSequence();
		vitesseAnimations = Configuration.vitesseAnimations;
		lenteurPas = Configuration.lenteurPas;
//		animations.insereTete(new AnimationPousseur(lenteurPas, this));
		mouvement = null;
		// Tant qu'on ne reçoit pas d'évènement temporel, on n'est pas sur que les
		// animations soient supportées (ex. interface textuelle)
		animationsSupportees = false;
		animationsActives = false;
	}

	@Override
	public void clicSouris(int type, int indexCarte) {
		switch (type){
			case 1:
				clicMain(indexCarte);
				break;
			case 2:
				clicContinuum(indexCarte);
				break;
			case 3:
				clicMain(indexCarte);
				break;
			default:
				break;
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
				break;
			case 2: //undo
//				jeu.undo();
				break;
			case 3: //redo
//				jeu.redo();
				break;
		}
	}

	void clicMain(int indexCarte){
		switch (etape){
			case 0: //debut de jeu
				break;
			case 1: //debut de tour
				System.out.println("Joueur " + jeu.getJoueurCourant() + " selectionne dans ça main la carte d'index " + indexCarte);
				indexCarteMain = indexCarte;
				vue.selectionnerCarteMain(indexCarteMain);
				vue.selectionnerCarteContinuum(jeu.getContinuum().getCoupsPossibles(jeu.getInfoJoueurCourant().getCarteMain(indexCarteMain), jeu.getInfoJoueurCourant().getSorcierIndice(), jeu.getInfoJoueurCourant().getDirection()));
				break;
			default:
				break;
		}
	}

	void clicContinuum(int indexCarte){
		switch (etape){
			case 0: //debut de jeu
				if (jeu.coupChangerPositionSorcier(indexCarte)){
					etapeSuivante();
					System.out.println("Joueur " + jeu.getJoueurCourant() + " pose son sorcier en " + indexCarte);
				}
				break;
			case 1: //debut de tour
				if (indexCarteMain != -1){
					java.util.LinkedList<Integer> indexPossible = jeu.getContinuum().getCoupsPossibles(jeu.getInfoJoueurCourant().getCarteMain(indexCarteMain), jeu.getInfoJoueurCourant().getSorcierIndice(), jeu.getInfoJoueurCourant().getDirection());
					for (Integer index:
							indexPossible) {
						if (index == indexCarte){
							System.out.println("Joueur " + jeu.getJoueurCourant() + " échange la carte de ça main " + indexCarteMain + " avec la carte du continuum " + indexCarte);
							jeu.coupEchangeCarteMainContinuum(indexCarteMain, indexCarte);
							indexCarteMain = -1;
							etapeSuivante();
							vue.selectionnerCarteMain(-1);
							vue.selectionnerCarteContinuum(null);
							break;
						}
					}
				}
				break;
			case 2: //paradox droite/gauche
				if (indexCarte > jeu.getInfoJoueurCourant().getSorcierIndice() && indexCarte <= jeu.getInfoJoueurCourant().getSorcierIndice()+3 && jeu.existeParadoxSuperieur()){
					if (jeu.getJoueurCourant() == 0)
						jeu.coupParadox(+1);
					else
						jeu.coupParadox(-1);
					etapeSuivante();
					System.out.println("Paradox, selection des carte dans le future");
				}
				else if (indexCarte < jeu.getInfoJoueurCourant().getSorcierIndice() && indexCarte >= jeu.getInfoJoueurCourant().getSorcierIndice()-3 && jeu.existeParadoxInferieur()) {
					if (jeu.getJoueurCourant() == 0)
						jeu.coupParadox(-1);
					else
						jeu.coupParadox(+1);
					etapeSuivante();
					System.out.println("Paradox, selection des carte dans le passé");
				}
				break;
			default:
				break;
		}
	}

	void etapeSuivante(){
		switch (etape){
			case (0):
				System.out.println();
				System.out.println("Echange Carte :");
				if (jeu.getInfoJoueurs()[jeu.adversaire()].getSorcierIndice() == 9)
					etape = 0;
				else
					etape = 1;
				jeu.finTour();
				break;
			case (1):
				if (jeu.getInfoJoueurCourant().existeParadox(jeu.getCodex().getCouleurInterdite())){
					System.out.println();
					System.out.println("Paradox :");
					etape = 2;
				} else if (jeu.existeClash()) {
					jeu.coupClash();
					System.out.println();
					System.out.println("Clash :");

					etape = 1;
					System.out.println();
					System.out.println("Echange Carte :");
					jeu.finTour();
				}
				else {
					etape = 1;
					System.out.println();
					System.out.println("Echange Carte :");
					jeu.finTour();
				}
				break;
			case (2):
				if (jeu.existeClash()) {
					etape = 3;
					jeu.coupClash();
					System.out.println();
					System.out.println("Clash :");

					etape = 1;
					System.out.println();
					System.out.println("Echange Carte :");
					jeu.finTour();
				}
				else {
					etape = 1;
					System.out.println();
					System.out.println("Echange Carte :");
					jeu.finTour();
				}
				break;
		}
	}


//	void joue(Coup cp) {
//		if (cp != null) {
//			jeu.joue(cp);
//			vue.metAJourDirection(cp.dirPousseurL(), cp.dirPousseurC());
//			if (animationsActives) {
//				mouvement = new AnimationCoup(cp, vitesseAnimations, this);
//				animations.insereQueue(mouvement);
//			} else
//				testFin();
//		} else {
//			Configuration.alerte("Coup null fourni, probablement un bug dans l'IA");
//		}
//	}
//
//	void deplace(int dL, int dC) {
//		if (mouvement == null) {
//			Coup cp = jeu.elaboreCoup(dL, dC);
//			if (cp != null)
//				joue(cp);
//		}
//	}

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
//			case "Left":
//				deplace(0, -1);
//				break;
//			case "Right":
//				deplace(0, 1);
//				break;
//			case "Up":
//				deplace(-1, 0);
//				break;
//			case "Down":
//				deplace(1, 0);
//				break;
			case "Quit":
				System.exit(0);
				break;
			case "Pause":
				basculeAnimations();
				break;
			case "IA":
				basculeIA();
				break;
			case "Full":
				vue.toggleFullscreen();
				break;
			default:
				System.out.println("Touche inconnue : " + touche);
		}
	}

	public void ajouteInterfaceUtilisateur(InterfaceUtilisateur v) {
		vue = v;
	}

	@Override
	public void tictac() {
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
	}

	public void changeEtape() {
		vue.changeEtape();
	}

	public void decale(int versL, int versC, double dL, double dC) {
		vue.decale(versL, versC, dL, dC);
	}

	public void basculeAnimations() {
		if (animationsSupportees && (mouvement == null))
			animationsActives = !animationsActives;
	}

	public void basculeIA() {
		if (animationsSupportees) {
			if (joueurAutomatique == null) {
				joueurAutomatique = IA.nouvelle(jeu);
				if (joueurAutomatique != null) {
					lenteurJeuAutomatique = Configuration.lenteurJeuAutomatique;
					animationIA = new AnimationJeuAutomatique(lenteurJeuAutomatique, joueurAutomatique, this);
				}
			}
			if (joueurAutomatique != null)
				IAActive = !IAActive;
		}
	}
}
