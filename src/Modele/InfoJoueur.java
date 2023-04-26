package Modele;

import java.util.List;

public class InfoJoueur {
	private List<Carte> main; //3 cartes
	private int points; //0 à 5
	private int directionMouvement; //-1 ou +1
	private int sorcierIndice; //index 0 ou 1

	public InfoJoueur(){
	}

	public InfoJoueur(int directionMouvement){
		this.directionMouvement = directionMouvement;
	}

	// GETTERS
	int getPoints(){return this.points;}
	int getSorcierIndice(){return this.sorcierIndice;}
	int getDirectionMouvement(){return this.directionMouvement;}
	List<Carte> getMain(){return this.main;}

	// SETTEERS
	void setMain(List<Carte> main){this.main = main;}
	void setPoints(int points){this.points = points;}
	// no setter in directionMouvement, it is supposed to never change
	void setSorcierIndice(int indice){this.sorcierIndice = indice;}

	// UTILS
	void addPoint(){this.points += 1;}
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
		//TODO
		// Change la carte de this.main.get(position) avec new_carte
		// il faut tester si position est valide, et il faut retourner la carde qu'on jete de notre main
		return null;
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

	boolean existeParadox(){
		//todo
		// regarde le main et voit si il y a des triplets
		return false;
	}

}
