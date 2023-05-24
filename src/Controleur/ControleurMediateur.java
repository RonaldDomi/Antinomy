package Controleur;

import Global.Configuration;
import Modele.Historique;
import Modele.Jeu;
import Reseau.Client;
import Reseau.Server;
import Vue.CollecteurEvenements;
import Vue.InterfaceUtilisateur;
import Vue.MenuGraphique;

import javax.swing.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Contrôleur médiateur pour le jeu.
 */
public class ControleurMediateur implements CollecteurEvenements {
	Jeu jeu;

	Joueur[][] joueurs;
	public int [] typeJoueur;

	InterfaceUtilisateur vue;

	MenuGraphique menuGraphique;

//	Sequence<Animation> animations;
	double vitesseAnimations;
	int lenteurPas;

	public boolean enAttenteConnexion = false;

	Animation mouvement;
	boolean animationsSupportees, animationsActives;
	int lenteurJeuAutomatique;
	boolean IAActive;

	boolean enLigne;

	public int joueurEnLigne;

	int etapeAnimation = 0;

	String[] animationChargement = {".", "..", "..."};

	JTextField hostName;

	int joueursCourant = -1;

	Socket clientSocket;

	final int lenteurAttente = Configuration.lenteurAttente;
	int decompte;

	int animationDecompte;

	Thread threadServer;

	public ControleurMediateur(Jeu j) {
		jeu = j;
		joueurs = new Joueur[2][6];
		typeJoueur = new int[6];
		for (int i = 0; i < joueurs.length; i++) {
			joueurs[i][0] = new JoueurHumain(i, jeu);
			joueurs[i][1] = new JoueurAIAleatoire(i, jeu);
			joueurs[i][2] = new JoueurAI(i, jeu, Configuration.profondeurIAFacile);
			joueurs[i][3] = new JoueurAI(i, jeu, Configuration.profondeurIAMedium);
			joueurs[i][4] = new JoueurAI(i, jeu, Configuration.profondeurIADifficile);
			joueurs[i][5] = new JoueurEnLigne(i, jeu);
			joueurEnLigne = 5;
			typeJoueur[i] = 0;
		}

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
		if (clientSocket != null){
			this.clientSocket = clientSocket;
			for (Joueur[] joueur: joueurs) {
				joueur[joueurEnLigne].ajouteSocket(clientSocket);
				joueur[0].ajouteSocket(clientSocket);
			}
			enLigne = true;
		}
	}

	@Override
	public void ajouteTextFieldHostName(JTextField textField) {
		hostName = textField;
	}
	
	@Override
	public void clicSouris(int l, int c) {

		// Lors d'un clic, on le transmet au joueur courant.
		// Si un coup a effectivement été joué (humain, coup valide), on change de joueur.

		if (jeu.getJoueurGagnant() == -1){
			if (joueurs[jeu.getJoueurCourant()][typeJoueur[jeu.getJoueurCourant()]].jeu(l, c)) {
				if (typeJoueur[jeu.getJoueurCourant()] == joueurEnLigne){ // adversaire en ligne
					joueursCourant = 1;
					joueurs[jeu.getJoueurCourant()][typeJoueur[jeu.getJoueurCourant()]].envoyerJeu();
				}
				resetSelection();
				if (typeJoueur[jeu.getJoueurCourant()] == 0){
					resetSelection();
					joueurs[jeu.getJoueurCourant()][0].afficherPreSelection();
				}
				decompte = lenteurAttente;
			}
		}
	}

	@Override
	public void clicSourisBouton(int index){
		if (typeJoueur[0] == joueurEnLigne || typeJoueur[1] == joueurEnLigne){
			switch (index){
				case 0: //save
					vue.sauvegarder();
					break;
				case 1: //undo
					jeu.undo();
					envoyerCommandeSocket("UNDO");
					resetSelection();
					afficherPreSelection();
					decompte = lenteurAttente;
					break;
				case 2: //redo
					jeu.redo();
					envoyerCommandeSocket("REDO");
					resetSelection();
					afficherPreSelection();
					decompte = lenteurAttente;
					break;
			}
		}else{
			
		
			switch (index){
				case 0: //save
					vue.sauvegarder();
					break;
				case 1: //charger
					if (vue.charger()){
						resetSelection();
						afficherPreSelection();
						decompte = lenteurAttente;
					}
					break;
				case 2: //undo
					jeu.undo();
					resetSelection();
					afficherPreSelection();
					decompte = lenteurAttente;
					break;
				case 3: //redo
					jeu.redo();
					resetSelection();
					afficherPreSelection();
					decompte = lenteurAttente;
					break;
				case 4: //restart
					jeu.charger(new Jeu(), false);
					jeu.historique = new Historique(jeu);
					resetSelection();
					afficherPreSelection();
					break;
				case 5: //menu IA
					vue.afficherMenuIA();
					break;
			}
		}
		jeu.metAJour();
	}


	@Override
	public void clicSourisBoutonMenu(int fenetre, int index){
		//	System.out.println("Fenetre "+fenetre);
		switch (fenetre){
			case 1: //Menu principal
				switch (index){
					case -2: //Quitter
						System.exit(0);
					case 0: //Nouvelle partie
						vue.setAffichage(0, 2);
						break;
					case 1: //En ligne
						vue.setAffichage(0, 3);
						break;
				}
				break;
			case 2: //Local
				switch (index){
					case -2: //Quitter
						vue.setAffichage(0, 1);
						break;
				}
				break;
			case 3: //En ligne
				//System.out.println("index "+fenetre);
					switch (index){
						case -2: //Quitter
							if (enAttenteConnexion){
								enAttenteConnexion = false;
								System.out.println("Interupt");
								vue.getMenu().changerTexteBouton("Lancer");
								threadServer.interrupt();
							}
							vue.setAffichage(0, 1);
							break;
					case 0: //Server
						if (enAttenteConnexion) {
							break;
						}
						System.out.println("En attente...");
						vue.getMenu().changerTexteBouton("En attente...");
						enAttenteConnexion = true;
						threadServer = new Thread(() -> clientSocket = Server.initServer(jeu, this));
						threadServer.start();
						break;
					case 1: //Client
						if (enAttenteConnexion)
							break;

						clientSocket = Client.initClient(hostName.getText(), jeu);
						if (clientSocket != null){
							ajouteSocket(clientSocket);
							typeJoueur[0] = joueurEnLigne;
							vue.typeJoueur(typeJoueur);
							vue.setAffichage(1, -1);
						}
						break;
				}
				break;
		}




	}

	private void resetSelection(){
		vue.selectionnerParadox(-1, -1, -1, -1);
		vue.selectionnerCarteContinuum(null);
		vue.selectionnerCarteMain(-1);
		vue.selectionnerMain(false);
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
				afficherPreSelection();
				decompte = lenteurAttente;
				break;
			case "Right":
				jeu.redo();
				resetSelection();
				afficherPreSelection();
				decompte = lenteurAttente;
				break;
			case "Import":
				vue.charger();
				jeu.metAJour();
				afficherPreSelection();
				decompte = lenteurAttente;
				break;
			case "Save":
				vue.sauvegarder();
				jeu.metAJour();
				afficherPreSelection();
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
	public void tictac() {
		if (jeu.getJoueurGagnant() == -1) {

			if(enLigne){
				if (joueursCourant != jeu.getJoueurCourant()){
					joueursCourant = jeu.getJoueurCourant();
					afficherPreSelection();
				}
			}

			if (decompte == 0) {
//				System.out.println("tic");
				int type = typeJoueur[jeu.getJoueurCourant()];
				// Lorsque le temps est écoulé on le transmet au joueur courant.
				// Si un coup a été joué (IA) on change de joueur.
//				System.out.println(type);
				if (joueurs[jeu.getJoueurCourant()][type].tempsEcoule()) {
					resetSelection();
					if (!afficherPreSelection())
						decompte = lenteurAttente;

				} else {
					if (jeu.getEtape() < 0){
						joueurs[jeu.getJoueurCourant()][0].afficherPreSelection();
					}
				}
			} else {
				decompte--;
			}

			if (animationDecompte == 0 && Configuration.tempsAnimation!=0){
//				System.out.println("anim");
				if (enAttenteConnexion){
//					System.out.println("Chargement"+animationChargement[etapeAnimation%3]);
					vue.getMenu().changerTexteBouton("En attente"+animationChargement[etapeAnimation%3]);
					vue.getMenu().repaint();
				}

				vue.changeEtape();

				etapeAnimation ++;
				animationDecompte = Configuration.tempsAnimation;
			}
			else {
				animationDecompte--;
			}

			vue.decale();
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


	public boolean afficherPreSelection(){
		if (typeJoueur[jeu.getJoueurCourant()] == 0){ //prochain joueur est un humain
			joueurs[jeu.getJoueurCourant()][0].afficherPreSelection();
			return true;
		}
		return false;
	}


	public void nouvellePartie(int type_j0, int type_j1){
		typeJoueur[0] = type_j0;
		typeJoueur[1] = type_j1;
		vue.typeJoueur(typeJoueur);
		vue.setAffichage(1, -1);
	}

	public void nouvellePartie2(int type_j0, int type_j1){
		typeJoueur[0] = type_j0;
		typeJoueur[1] = type_j1;
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

	public InterfaceUtilisateur getVue() {
		return vue;
	}

	public void charger(int typeJ0, int typeJ1){
		if (vue.charger()){
			nouvellePartie(typeJ0, typeJ1);
		}
	}

	public int jouerPartie(){
		while (jeu.getJoueurGagnant() == -1){
			joueurs[jeu.getJoueurCourant()][typeJoueur[jeu.getJoueurCourant()]].tempsEcoule();
			jeu.etapeSuivante();
			jeu.finTour();
		}
		return jeu.getJoueurGagnant();
	}
}
