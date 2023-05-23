package Modele;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
	Informations sur le joueur : main, cartes, indices et la direction de son mouvement (où est sa droite selon la
 perspective)
 */
public class InfoJoueur implements Cloneable{
	private Carte[] main; //3 cartes
	private int points; //0 à 5
	private int directionMouvement; //-1 ou +1
	private int sorcierIndice = 9; //index

	protected Random r;

	public InfoJoueur(Random r){
		this.r = r;
	}

	public InfoJoueur(int directionMouvement, Random r){
		this.directionMouvement = directionMouvement;
		this.r = r;
	}
	
	/**
	 * Reconstruit une infoJoueur selon sa représentation textuelle.
	 * @param stringInfo Représentation textuelle de l'infoJoueur
	 */
	public InfoJoueur(String stringInfo){
//		InfoJoueur: carte1,carte2,carte3,points,directionMouvement,sorcierIndice
		String[] strings = stringInfo.split(",");
		main = new Carte[3];
		main[0] = new Carte(strings[0]);
		main[1] = new Carte(strings[1]);
		main[2] = new Carte(strings[2]);
		points = Integer.parseInt(strings[3]);
		directionMouvement = Integer.parseInt(strings[4]);
		sorcierIndice = Integer.parseInt(strings[5]);
	}

	// GETTERS
	public int getPoints(){return this.points;}
	public int getSorcierIndice(){return this.sorcierIndice;}
	int getDirectionMouvement(){return this.directionMouvement;}
	public Carte getCarteMain(int index){return this.main[index];}
	public Carte getCarteAleatoire(){
		return this.main[r.nextInt(main.length)];
	}
	public Carte[] getMain(){return this.main;}

	public int getDirection(){return this.directionMouvement;}

	// SETTERS
	public void setMain(Carte[] main){this.main = main;}
	public void setPoints(int points){this.points = points;}
	// no setter in directionMouvement, it is supposed to never change
	public void setDirectionMouvement(int directionMouvement){this.directionMouvement = directionMouvement;}

	public void setSorcierIndice(int indice){this.sorcierIndice = indice;}

	// UTILS
	void addPoint(){this.points += 1;}
	void remPoint(){this.points -= 1;}
	/**
	 * Tente de déplacer le sorcier vers une nouvelle position.
	 * @param deplacement Nombre de cases à avancer/reculer.
	 * @return Un booléen si le déplacement a été effectué.
	 */
	boolean moveSorcier(int deplacement){
		int nouvelle_position = this.getSorcierIndice() + (deplacement * this.getDirectionMouvement());
		if(nouvelle_position >= 0 && nouvelle_position <= 8){
			this.setSorcierIndice(nouvelle_position);
			return true;
		}else{
			return false;
		}
	}

	/**
	 * Tente d'échanger une carte de la main avec une autre carte.
	 * @param position Indice de la carte à échanger dans la main
	 * @param new_carte Nouvelle carte à mettre en main
	 * @return La carte échangée. Null si l'échange n'est pas valide (mauvaise position).
	* */
	Carte changeCarte(int position, Carte new_carte){
		Carte carte = null;
		if(position >= 0 && position < 3){
			carte = main[position];
			main[position] = new_carte;
		}
		return carte;
	}
	
	/**
	 * Vérifie s'il existe un Paradox dans la main du joueur.
	 * @param couleur_interdite La couleur interdite durant ce tour
	 * @return Un booléen s'il existe un paradox
	 */
	public boolean existeParadox(Couleur couleur_interdite){
		if(main[0].getCouleur() == couleur_interdite || main[1].getCouleur() == couleur_interdite || main[2].getCouleur() == couleur_interdite)
			return false;

		//regarde la main et voit si il y a des triplets
		boolean couleur = main[0].getCouleur() == main[1].getCouleur() && main[1].getCouleur() == main[2].getCouleur();
		boolean symbole = main[0].getSymbole() == main[1].getSymbole() && main[1].getSymbole() == main[2].getSymbole();
		boolean nombre = main[0].getNumero() == main[1].getNumero() && main[1].getNumero() == main[2].getNumero();
		return couleur || symbole || nombre;
	}
	
	/**
	 * Calcule la somme de la main du joueur. Les cartes de la couleur interdite valent zéro.
	 * @param couleur_interdite La couleur interdite durant ce tour.
	 * @return Un entier représentant la somme de la main.
	 */
	public int sommeMain(Couleur couleur_interdite){
		int sum = 0;
		for (Carte carte:main) {
			if (carte.getCouleur() != couleur_interdite){
				sum += carte.getNumero();
			}
		}
		return sum;
	}
	
	/**
	 * Calcule la somme de la main pour une main donnée.
	 * @param main La main où calculer la somme.
	 * @param couleurInterdite La couleur interdite.
	 * @return Un entier représentant la somme de la main.
	 */
	public static float getEvaluationSommeMain(Carte[] main, Couleur couleurInterdite){
		float valeur = 0;
		for(int carteI = 0; carteI < main.length; carteI++){
			if(main[carteI].getCouleur() != couleurInterdite){
//				valeur += main[carteI].getNumero() - 2;
				valeur += main[carteI].getNumero();
			}
		}
		return valeur;
	}

	//TODO: DELETE?
	public static float getEvaluationDuosMain(Carte[] main, Couleur couleurInterdite){
		float valeur = 0;
//		for(int carteI = 0; carteI < main.length; carteI++) {
//			if(main[carteI].getCouleur() != couleurInterdite) {
//
//			}
//		}
		return valeur;
	}
	
	/**
	 * <P> Représentation textuelle de infoJoueur.</P>
	 * <P> De la forme: main, points, directionMouvement, indice du sorcier.</P>
	 * <P> Séparés par des ,</P>
	 * @return Chaîne de caractères représentant l'infoJoueur
	 */
	@Override
	public String toString() {
		String res = "";
		res += main[0].toString() + ",";
		res += main[1].toString() + ",";
		res += main[2].toString() + ",";
		res += points + ",";
		res += directionMouvement + ",";
		res += sorcierIndice;
		return res;
	}
	
	/**
	 * Clone l'infoJoueur.
	 * @return L'infoJoueur cloné
	 * @throws CloneNotSupportedException
	 */
	@Override
	public InfoJoueur clone() throws CloneNotSupportedException{
//		InfoJoueur j = (InfoJoueur) super.clone();
		InfoJoueur j = new InfoJoueur(this.directionMouvement, this.r);
		j.main = new Carte[3];
		for (int i = 0; i < 3; i++) {
			j.main[i] = main[i].clone();
		}
		j.points = points;
		j.sorcierIndice = sorcierIndice;
		return j;
	}

}
