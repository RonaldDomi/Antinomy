package Modele;

import java.util.*;

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

	// GETTERS
	public int getPoints(){return this.points;}

	public int getSorcierIndice(){return this.sorcierIndice;}

	int getDirectionMouvement(){return this.directionMouvement;}

	public Carte getCarteMain(int index){return this.main[index];}

	Carte getCarteAleatoire(){
		return this.main[r.nextInt(main.length)];
	}

	public Carte[] getMain(){return this.main;}

	public int getDirection(){return this.directionMouvement;}

	// SETTERS
	public void setMain(Carte[] main){this.main = main;}

	public void setPoints(int points){this.points = points;}

	// no setter in directionMouvement, it is supposed to never change
	public void setSorcierIndice(int indice){this.sorcierIndice = indice;}

	public void setDirectionMouvement(int directionMouvement){this.directionMouvement = directionMouvement;}

	// UTILS
	void addPoint(){this.points += 1;}

	void remPoint(){this.points -= 1;}

	boolean moveSorcier(int deplacement){
		int nouvelle_position = this.getSorcierIndice() + (deplacement * this.getDirectionMouvement());
		if(nouvelle_position >= 0 && nouvelle_position <= 8){
			this.setSorcierIndice(nouvelle_position);
			return true;
		}else{
			return false;
		}
	}


	Carte changeCarte(int position, Carte new_carte){
		// Change la carte de this.main.get(position) avec new_carte
		// il faut tester si position est valide, et il faut retourner la carde qu'on jete de notre main
		Carte carte = null;
		if(position >= 0 && position < 3){
			carte = main[position];
			main[position] = new_carte;
		}
		return carte;
	}

	boolean existeParadox(Couleur couleur_interdite){
		if(main[0].getCouleur() == couleur_interdite || main[1].getCouleur() == couleur_interdite || main[2].getCouleur() == couleur_interdite)
			return false;

		//regarde la main et voit si il y a des triplets
		boolean couleur = main[0].getCouleur() == main[1].getCouleur() && main[1].getCouleur() == main[2].getCouleur();
		boolean symbole = main[0].getSymbole() == main[1].getSymbole() && main[1].getSymbole() == main[2].getSymbole();
		boolean nombre = main[0].getNumero() == main[1].getNumero() && main[1].getNumero() == main[2].getNumero();
		return couleur || symbole || nombre;
	}

	public int sommeMain(Couleur couleur_interdite){
		int sum = 0;
		for (Carte carte:main) {
			if (carte.getCouleur() != couleur_interdite){
				sum += carte.getNumero();
			}
		}
		return sum;
	}

	public void melangeMain(){
		Random rand = new Random();
		for (int i = 0; i < main.length; i++) {
			int randomIndexToSwap = rand.nextInt(main.length);
			Carte temp = main[randomIndexToSwap];
			main[randomIndexToSwap] = main[i];
			main[i] = temp;
		}
	}

	public static Carte[] mockMain(){
		Carte[] res = new Carte[3];
		res[0] = new Carte(1, Couleur.ROUGE, Symbole.CLEF);
		res[1] = new Carte(2, Couleur.VERT, Symbole.PAPIER);
		res[2] = new Carte(3, Couleur.BLEU, Symbole.PAPIER);
		return res;
	}

	public InfoJoueur clone() throws CloneNotSupportedException{
		InfoJoueur j = (InfoJoueur) super.clone();
		for (int i = 0; i < 3; i++) {
			j.main[i] = main[i].clone();
		}
		j.points = points;
		j.directionMouvement = directionMouvement;
		j.sorcierIndice = sorcierIndice;
		j.r = r;
		return j;
	}

}
