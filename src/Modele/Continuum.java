package Modele;

import java.util.ArrayList;
import java.util.List;

public class Continuum {
	private List<Carte> continuum; //9 cartes
	
	Continuum(List<Carte> continuum){
		this.continuum = continuum;
	}

	Carte getCarteContinuum(int index){
		return this.continuum.get(index);
	}

	void setCarteContinuum(int index, Carte carte){
		this.continuum.set(index, carte);
	}
	
	int getContinuumSize(){
		return continuum.size();
	}
	

	int[][] calculeOptions(List<Carte> cartes, int indiceSorcier){
		// todo
		// prend comme argument les 3 cartes du main
		// envoie 3 listes d'options, ou
		// int[0] c'est un list de int de options possibles a partir de cartes[0]
		return new int[0][0];
	}

	void echangeAvecMain(List<Carte> main, int indiceSorcier){
		// todo
		// prend comme argument les 3 cartes du main
		// renvoie les 3 cartes echanges
	}
}
