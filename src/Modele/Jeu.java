package Modele;


import Patterns.Observable;

import java.util.*;


public class Jeu extends Observable implements Cloneable{
	
	Random r;

	int tour; //0 à +inf
	Continuum continuum;
	InfoJoueur[] infoJoueurs; //2 infoJoueurs
	Codex codex;
	public Historique historique;
	int joueurCourant; //0 ou 1
	int joueurGagnant; //0 ou 1
	int etape; //Etape de jeu
	
	
	/**
	 * Construit un jeu pour une nouvelle partie.
	 */
	public Jeu() {
		r = new Random();

		Deck d = new Deck();
		continuum = new Continuum(d.distribuer(9));
		infoJoueurs = new InfoJoueur[2];
		infoJoueurs[0] = new InfoJoueur(1, r);
		infoJoueurs[0].setMain(d.distribuer(3));
		infoJoueurs[1] = new InfoJoueur(-1, r);
		infoJoueurs[1].setMain(d.distribuer(3));
		etape = -1;
		codex = new Codex(d.distribuer(1)[0], continuum.getCarteContinuum(0).getCouleur());
		joueurCourant = 0;
		joueurGagnant = -1;
		tour = 0;

		historique = new Historique();
		Jeu jeuClone;
		try {
			jeuClone = this.clone();
			jeuClone.historique = null;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
		historique.ajouterJeu(jeuClone);
	}
	
	/**
	 * Reconstruit un jeu à partir de sa représentation textuelle, permet de charger une partie.
	 * @param stringJeu Représentation textuelle du jeu à charger.
	 */
	public Jeu(String stringJeu){
//		continuum;info[0];info[1];tour;codex;joueurCourant;joueurGagnant;etape;
		String[] stringJeuSep = stringJeu.split(";");
		this.continuum = new Continuum(stringJeuSep[0]);
		this.infoJoueurs = new InfoJoueur[2];
		this.infoJoueurs[0] = new InfoJoueur(stringJeuSep[1]);
		this.infoJoueurs[1] = new InfoJoueur(stringJeuSep[2]);
		this.tour = Integer.parseInt(stringJeuSep[3]);
		this.codex = new Codex(stringJeuSep[4]);
		this.joueurCourant = Integer.parseInt(stringJeuSep[5]);
		this.joueurGagnant = Integer.parseInt(stringJeuSep[6]);
		this.etape = Integer.parseInt(stringJeuSep[7]);
	}
	
	
	/**
	 * Clone un jeu.
	 * @param j Jeu à cloner
	 */
	public Jeu(Jeu j){
		this.r = j.r;
		this.joueurCourant = j.joueurCourant;
		this.joueurGagnant = j.joueurGagnant;
		this.tour = j.tour;
		this.etape = j.etape;

		this.infoJoueurs = new InfoJoueur[2];
		try{
			this.infoJoueurs[0] = j.infoJoueurs[0].clone();
			this.infoJoueurs[1] = j.infoJoueurs[1].clone();
			this.continuum = j.continuum.clone();
			this.codex = j.codex.clone();
		}catch(CloneNotSupportedException e){
			System.out.println("Could not clone Jeu in Jeu.constructor(jeu)");
		}
//		this.historique = j.historique;
	}
	
	/**
	 * Échange une carte du Continuum contre la main du joueur.
	 * @param carteMainIndice Indice de la carte en main
	 * @param carteContinuumIndice Indice de la carte du Continuum
	 */
	void echangerCarteMainContinuum(int carteMainIndice, int carteContinuumIndice){
		//change la carte de la main donnée par l'utilisateur avec la carte dans le continuum
		continuum.setCarteContinuum(carteContinuumIndice, infoJoueurs[joueurCourant].changeCarte(carteMainIndice, continuum.getCarteContinuum(carteContinuumIndice)));
		//renvoie rien
	}

	/**
	 * Tente de déplacer le sorcier vers la position de la carte choisie (donnée en argument)
	 * @param indexCarte Index où déplacer le sorcier
	 * @return Un booléen si le déplacement à réussi ou non.
	 * <P> Renvoie false si l'étape de jeu ne permet pas un déplacement de sorcier. </P>
	 *
	 * <P> Voir : {@link Continuum#getIndexSorcierPossible(Couleur)} pour les indexes possibles </P>
	 * */
	public boolean coupChangerPositionSorcier(int indexCarte){

		if (getEtape() != -1 && getEtape() != -2 )
			return false;

		java.util.LinkedList<Integer> indexPossible = continuum.getIndexSorcierPossible(codex.getCouleurInterdite());
		
		for (Integer index: indexPossible) {
			if (index == indexCarte){
				getInfoJoueurCourant().setSorcierIndice(indexCarte);
				etapeSuivante();
				metAJour();
				return true;
			}
		}

		return false;
	}
	
	/**
	 * Tente d'échanger la carte en main avec une carte du Continuum.
	 * @param indexMain Indice de la carte en main.
	 * @param indexContinuum Indice de la carte du Continuum
	 * @return Un booléen si l'échange a réussi ou non.
	 * <P> Renvoie false si l'étape ne permet pas de changer une carte </P>
	 * <P> Voir : {@link Continuum#getCoupsPossibles(Carte, int, int)} pour les coups possibles pour chaque carte}</P>
	 */
	public boolean coupEchangeCarteMainContinuum(int indexMain, int indexContinuum){

		if (getEtape() != 1)
			return false;


		java.util.LinkedList<Integer> indexPossible = continuum.getCoupsPossibles(getInfoJoueurCourant().getCarteMain(indexMain), getInfoJoueurCourant().getSorcierIndice(), getInfoJoueurCourant().getDirection());
		
		for (Integer index: indexPossible) {
			if (index == indexContinuum){
				infoJoueurs[joueurCourant].setSorcierIndice(indexContinuum);
				echangerCarteMainContinuum(indexMain, indexContinuum);
				etapeSuivante();
				metAJour();
				return true;
			}
		}
		return false;
	}
	
	/**
	 * <P> Réalise un paradox. </P>
	 * <P> Mélange les cartes en main et les échange avec le Continuum selon la direction choisie par le joueur. </P>
	 * <P> Ajoute un point au joueur. </P>
	 * @param direction -1/+1 Gauche/Droite où échanger les cartes
	 * @return Un booléen si le paradox a été réalisé
	 * <P> Renvoie false si l'étape, la couleur interdite ou les cartes en main ne permettent pas de faire un Paradox
	 * </P>
	 */
	public boolean coupParadox(int direction){

		if (getEtape() != 2)
			return false;

		if(!getInfoJoueurCourant().existeParadox(codex.getCouleurInterdite()))
			return false;

		if(!((direction == 1 && existeParadoxSuperieur()) || (direction == -1 && existeParadoxInferieur())))
			return false;

		Collections.shuffle(Arrays.asList(getInfoJoueurCourant().getMain()));
		if (direction == 1){
			int indexMain=0;
			for (int i = 0; i < 3; i++) {
				echangerCarteMainContinuum(indexMain,
						getInfoJoueurCourant().getSorcierIndice() + ((i+1) * direction * getInfoJoueurCourant().getDirectionMouvement()));
				indexMain++;
			}

		}else{
			int indexMain=2;
			for (int i = 0; i < 3; i++) {
				echangerCarteMainContinuum(indexMain, getInfoJoueurCourant().getSorcierIndice() + ((i+1) * direction* getInfoJoueurCourant().getDirectionMouvement()));
				indexMain--;
			}
		}

		infoJoueurs[joueurCourant].addPoint();
		codex.cycleCouleur();
		jeuGagnant();
		etapeSuivante();
		metAJour();
		return true;
	}
	
	/**
	 * <P> Résout un clash entre les deux joueurs. Ajoute un point au gagnant </P>
	 * @return Un booléen si un clash est arrivé dans la bonne étape.
	 * <P> Renvoie false si l'étape ne permet pas de faire un clash. </P>
	 */
	public boolean coupClash(){

		if (getEtape() != 3)
			return false;


		int res = gagnantClash();
		if (res != -1){
			if (infoJoueurs[1-res].getPoints() > 0){
				infoJoueurs[1-res].remPoint();
				infoJoueurs[res].addPoint();
				codex.cycleCouleur();
				etapeSuivante();
				jeuGagnant();
				metAJour();
				return true;
			}
			return true;
		}
		return true;
	}
	
	/**
	 * Retourne l'index de l'adversaire du joueur.
	 * @return Index de l'adversaire, 0/+1
	 */
	public int adversaire(){
		return 1-joueurCourant;
	}
	
	/**
	 * Finit un tour, donne la main à l'adversaire et clone le jeu pour le mettre dans l'historique.
	 */
	public void finTour(){
		if (joueurGagnant == -1)
			joueurCourant = adversaire();
		tour++;
		Jeu jeuClone = null;
		try{
			jeuClone = clone();
		}catch(CloneNotSupportedException e){
			System.out.println("Error on update historique: clone failed\n");
		}
		historique.ajouterJeu(jeuClone);
	}

	/**
	* Finit un tour, donne la main à l'adversaire, clone le jeu, mais ne l'ajoute PAS à l'historique.
	 * @param hist Booléen si le jeu doit-être ajouté à l'historique ou non.
	*/
	public void finTour(boolean hist){
		joueurCourant = adversaire();
		tour++;
		Jeu jeuClone = null;
		try{
			jeuClone = clone();
		}catch(CloneNotSupportedException e){
			System.out.println("Error on update historique: clone failed\n");
		}
		if( hist)
			historique.ajouterJeu(jeuClone);
	}
	
	
	/**
	 * Change d'étape de jeu en fonction de l'étape actuelle.
	 * Vérifie s'il existe un Paradox/Clash, les résout en conséquence.
	 * @return Un booléen TODO: Selon quoi??
	 */
	public boolean etapeSuivante() {
		switch (getEtape()) {
			case (-1):
				setEtape(-2);
				finTour();
				return true;
			case (-2):
				setEtape(1);
				finTour();
				return true;
			case (1):
				if (getInfoJoueurCourant().existeParadox(getCodex().getCouleurInterdite())) {
					setEtape(2);
					return false;
				} else if (existeClash()) {
					setEtape(3);
					coupClash();
					setEtape(1);
					finTour();
					return false;
				} else {
					setEtape(1);
					finTour();
					return true;
				}
			case (2):
				if (existeClash()) {
					setEtape(3);
					coupClash();
					setEtape(1);
					finTour();
					return true;
				} else {
					setEtape(1);
					finTour();
					return true;
				}
			default:
				return false;
		}
	}
	
	/**
	 * <P> Vérifie s'il existe un clash. </P>
	 * C'est-à-dire que les deux joueurs sont sur le même indice sur le Continuum
	 * @return Un booléen si les deux joueurs sont sur le même indice
	 */
	public boolean existeClash(){
		return infoJoueurs[0].getSorcierIndice() == infoJoueurs[1].getSorcierIndice();
	}
	
	/**
	 * Résout un clash. Compare la somme des mains de chaque joueur.
	 * @return 0 si le joueur 1 gagne. <BR>
	 *  1 si le joueur 2 gagne. <BR>
	 * -1 si égalité.<BR>
	 */
	int gagnantClash(){
		int sommeJ0 = infoJoueurs[0].sommeMain(codex.getCouleurInterdite());
		int sommeJ1 = infoJoueurs[1].sommeMain(codex.getCouleurInterdite());

		if (sommeJ0 == sommeJ1){
			Carte carteJ0 = infoJoueurs[0].getCarteAleatoire();
			sommeJ0 = carteJ0.getCouleur() != codex.getCouleurInterdite() ? carteJ0.getNumero() : 0;
			Carte carteJ1 = infoJoueurs[1].getCarteAleatoire();
			sommeJ1 = carteJ1.getCouleur() != codex.getCouleurInterdite() ? carteJ1.getNumero() : 0;
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
	
	/**
	 * Vérifie si la somme de main est égale entre les deux joueurs.
	 * @return Un booléen si la somme est égale.
	 */
	public boolean egaliteClash(){
		return infoJoueurs[0].sommeMain(codex.getCouleurInterdite()) == infoJoueurs[1].sommeMain(codex.getCouleurInterdite());
	}
	
	/**
	 * Vérifie si un des jeux joueurs a gagné la partie.
	 */
	void jeuGagnant(){
		if (infoJoueurs[0].getPoints() >= 5)
			joueurGagnant = 0;
		else if (infoJoueurs[1].getPoints() >= 5)
			joueurGagnant = 1;
	}
	
	/**
	 * Vérifie si les cartes à droite du joueur permettent de faire un Paradox.
	 * @return Un booléen si les cartes le permettent.
	 */
	public boolean existeParadoxSuperieur(){
		return infoJoueurs[joueurCourant].getSorcierIndice()+3*infoJoueurs[joueurCourant].getDirection() < continuum.getContinuumSize() &&
				infoJoueurs[joueurCourant].getSorcierIndice()+3*infoJoueurs[joueurCourant].getDirection() >= 0;
	}
	
	/**
	 * Vérifie si les cartes à gauche du joueur permettent de faire un Paradox.
	 * @return Un booléen si les cartes le permettent.
	 */
	public boolean existeParadoxInferieur(){
		return infoJoueurs[joueurCourant].getSorcierIndice()-3*infoJoueurs[joueurCourant].getDirection() < continuum.getContinuumSize() &&
				infoJoueurs[joueurCourant].getSorcierIndice()-3*infoJoueurs[joueurCourant].getDirection() >= 0;
	}
	
	/**
	 * Annule un coup, change le jeu en conséquence.
	 */
	public void undo(){
		if(!historique.peutAnnuler()) return;
		System.out.println("doing undo");
		charger(historique.annuler(), false);
//		metAJour();
	}
	
	/**
	 * Refait un coup, change le jeu en conséquence.
	 */
	public void redo(){
		if(!historique.peutRefaire()) return;
		System.out.println("doing redo");
		charger(historique.refaire(), false);
//		metAJour();
	}

	public int getEtape(){ return this.etape; }
	public void setEtape(int etape){this.etape = etape;}

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
	
	public int getTour(){
		return tour;
	}
	
	public int getJoueurGagnant(){
		return joueurGagnant;
	}
	public Jeu clone() throws CloneNotSupportedException {
		Jeu jClone = new Jeu(this);
		return jClone;
	}
	
	/**
	 * Modifie le jeu en place, avec la représentation textuelle d'un jeu.
	 * @param stringJeu Représentation textuelle du jeu
	*/
	public void modifierJeu(String stringJeu) {
		synchronized (this) {
			Jeu jeuClone = new Jeu(stringJeu);
			historique.ajouterJeu(jeuClone);
			
			this.r = new Random();
			String[] stringJeuSep = stringJeu.split(";");
			this.continuum = new Continuum(stringJeuSep[0]);
			this.infoJoueurs = new InfoJoueur[2];
			this.infoJoueurs[0] = new InfoJoueur(stringJeuSep[1]);
			this.infoJoueurs[0].r = r;
			this.infoJoueurs[1] = new InfoJoueur(stringJeuSep[2]);
			this.infoJoueurs[1].r = r;
			this.tour = Integer.parseInt(stringJeuSep[3]);
			this.codex = new Codex(stringJeuSep[4]);
			this.joueurCourant = Integer.parseInt(stringJeuSep[5]);
			this.joueurGagnant = Integer.parseInt(stringJeuSep[6]);
			this.etape = Integer.parseInt(stringJeuSep[7]);
			
		}
	}
	
	/**
	 * Modifie le jeu avec un jeu chargé. Charge aussi l'historique si un chargement a été fait
	 * @param j Jeu à charger sur le plateau
	 * @param isImport Booléen si l'historique doit aussi être chargé
	 */
	public void charger(Jeu j, boolean isImport){
		this.r = j.r;
		this.joueurGagnant = j.joueurGagnant;
		this.joueurCourant = j.joueurCourant;
		this.tour = j.tour;
		this.etape = j.etape;

		try {
			this.codex = j.codex.clone();
//			this.infoJoueurs = j.infoJoueurs.clone();
			this.infoJoueurs[0] = j.infoJoueurs[0].clone();
			this.infoJoueurs[1] = j.infoJoueurs[1].clone();
			this.continuum = j.continuum.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}


		if(isImport) this.historique = j.historique;
//		metAJour();
	}

	public Codex getCodex(){return codex;}

	/**
	 * Calcule les coups possibles pour toutes les cartes de la main.
	 * @return Une liste chaînée de coups possibles
	 */
	public List<Coup> getCoupsPossibles(){
		List<Coup> coupsPossibles = new ArrayList<>();
		for(int i = 0; i < getInfoJoueurCourant().getMain().length; i++){
			// pour chaque carte Main
			List<Integer> coupsCarteMain = continuum.getCoupsPossibles(getMainJoueurCourant()[i], getInfoJoueurCourant().getSorcierIndice(), getInfoJoueurCourant().getDirectionMouvement());
			for(int c: coupsCarteMain){
				// pour chaque carte Continuum
				Jeu temp;
				try {
					temp = clone();
					temp.historique = new Historique();
				} catch (CloneNotSupportedException e) {
					throw new RuntimeException(e);
				}

				//faire echange
				temp.coupEchangeCarteMainContinuum(i, c);
				// if paradox
				if (temp.infoJoueurs[temp.joueurCourant].existeParadox(temp.codex.getCouleurInterdite())){
					// if paradox superieur
					if (temp.existeParadoxSuperieur()) {
						coupsPossibles.add(new Coup(i, c, 1));
					}
					if(temp.existeParadoxInferieur()){
						coupsPossibles.add(new Coup(i, c, -1));
					}
				}else{
					// no paradox
					coupsPossibles.add(new Coup(i, c, 0));
				}


			}
		}

		return coupsPossibles;
	}
	
	/**
	 * Fait un coup sans changer le plateau de Jeu. Permet à l'IA d'explorer les possibilitées.
	 * @param jeu Le jeu actuel.
	 * @param coup Un coup à effectuer.
	 * @return Le Jeu avec le coup effectué.
	 */
	public static Jeu faireCoupClone(Jeu jeu, Coup coup){
		int indexMain = coup.indexMain;
		int indexContinuum = coup.indexContinuum;
		int dirParadox = coup.paradox;

		//dirParadox -1, +1, or 0 for nothing
		Jeu jeuBase;
		try {
			jeuBase = jeu.clone();
			jeuBase.historique = new Historique();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}

		Couleur codexVieux = jeuBase.getCodex().getCouleurInterdite();

		jeuBase.coupEchangeCarteMainContinuum(indexMain, indexContinuum);

		//faire paradox
		if(dirParadox != 0) jeuBase.coupParadox(dirParadox);
		//faire clash if exist
		if (jeuBase.existeClash()){
			// le codex il est avance, on le remet on place
			if(jeuBase.getCodex().getCouleurInterdite() != codexVieux){
//				System.out.println("avant couleur " + codexVieux);
//				System.out.println("apres couleur " + jeuBase.getCodex().getCouleurInterdite());
				jeuBase.codex.cycleCouleur();
				jeuBase.codex.cycleCouleur();
				jeuBase.codex.cycleCouleur();
//				System.out.println("apres2 couleur " + jeuBase.getCodex().getCouleurInterdite());
			}

			if(jeuBase.egaliteClash())
				return null;
			jeuBase.coupClash();
		}


		return jeuBase;
	}




	
	/**
	 * <P> Représentation textuelle d'un jeu. </P>
	 * <P> Sous la forme: Continuum, infoJ1, infoJ2, tour, codex, joueurCourant, joueurGagnant, étape.</P>
	 * <P> Séparés par des ; </P>
	 * @return Une chaîne de caractères représentant le Jeu
	 */
	@Override
	public String toString() {
		return continuum.toString() + ";" +
				infoJoueurs[0].toString() + ";" +
				infoJoueurs[1].toString() + ";" +
				tour + ";" +
				codex.toString() + ";" +
				joueurCourant + ";" +
				joueurGagnant + ";" +
				etape + ";";
	}
}
