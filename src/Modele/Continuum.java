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
	void setCarteContinuum(int index, Carte carte){ this.continuum.set(index, carte);}
}
