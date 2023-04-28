package Modele;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class InfoJoueur {
	private Carte[] main; //3 cartes
	private int points; //0 à 5
	private int directionMouvement; //-1 ou +1
	private int sorcierIndice; //index 0 ou 1

	private Random r;

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
	Carte getCarteMain(int index){return this.main[index];}
	Carte getCarteAleatoire(){
		return this.main[r.nextInt(main.length)];
	}
	Carte[] getMain(){return this.main;}

	// SETTEERS
	void setMain(Carte[] main){this.main = main;}
	void setPoints(int points){this.points = points;}
	// no setter in directionMouvement, it is supposed to never change
	void setSorcierIndice(int indice){this.sorcierIndice = indice;}

	// UTILS
	void addPoint(){this.points += 1;}
	void remPoint(){this.points -= 1;}
	boolean moveSorcier(int deplacement){
		//TODO
		// deplace l'indice de le sourcier avec le deplacement
		// deplacement peut etre +2, -3 +4 ....
		// il faut tester si c'est bien possible de faire le mouvement, si
		// c'est pas possible, on renvoie faux, sinon vrai
		// avec vrai on change aussi this.sorcierIndice = newIndice;
		return false;
	}

	Carte changeCarte(int position, Carte new_carte){
		// Change la carte de this.main.get(position) avec new_carte
		// il faut tester si position est valide, et il faut retourner la carde qu'on jete de notre main
		Carte carte = null;
		if(position >= 0 && position <= 3){
			carte = main[position];
			main[position] = new_carte;
		}
		return carte;
	}

	int choisirCarte(){
		//todo
		//fonction qui prend input text le choix de la carde de main
		//...
		return 1;
	}

	int choisirPositionDansContinuum(int[] options){
		//todo
		//input text un option parme les options qu'on fournit l'utilisateur
		return 0;
	}

	boolean existeParadox(Couleur couleur_interdite){
		//regarde la main et voit si il y a des triplets
		boolean couleur = main[0].getCouleur() != couleur_interdite && main[0].getCouleur() == main[1].getCouleur() && main[1].getCouleur() == main[2].getCouleur();
		boolean symbole = main[0].getSymbole() == main[1].getSymbole() && main[1].getSymbole() == main[2].getSymbole();
		boolean nombre = main[0].getNumero() == main[1].getNumero() && main[1].getNumero() == main[2].getNumero();
		return couleur || symbole || nombre;
	}

	public int sommeMain(){
		return main[0].getNumero() + main[1].getNumero() + main[2].getNumero();
	}

	public static Carte[] mockMain(){
		Carte[] res = new Carte[3];
		res[0] = new Carte(1, Couleur.ROUGE, Symbole.CLEF);
		res[1] = new Carte(2, Couleur.VERT, Symbole.PAPIER);
		res[2] = new Carte(3, Couleur.BLEU, Symbole.PAPIER);
		return res;
	}

}
