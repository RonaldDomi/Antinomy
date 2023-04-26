package Modele;

public class Codex {
	Carte carte;
	int couleurInterdite; //1 à 4
	
	
	Codex(Carte carte){
		this.carte = carte;
		this.couleurInterdite=1;
	}
	
	
	Carte getCarte(){
		return this.carte;
	}
	
	
	int getCouleurInterdite(){
		return this.couleurInterdite;
	}
	
}



