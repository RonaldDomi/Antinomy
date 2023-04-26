package Modele;

import java.util.ArrayList;

public class Continuum {
	ArrayList<Carte> continuum; //9 cartes

	Continuum(){
		this.continuum = new ArrayList<Carte>();
		initContinuum();
	}
	
	
	void initContinuum(){
	
	}
	
	Carte getCarteContinuum(int index){
		return this.continuum.get(index);
	}
	
	
}
