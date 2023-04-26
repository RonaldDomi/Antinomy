package Modele;

public class Carte {
	int numero; //1 à 4
	Couleur couleur;
	public enum Symbole{
		symbole1,
		symbole2,
		symbole3,
		symbole4
	}
	Symbole symbole;
	
	
	int getNumero(){
		return this.numero;
	}
	
	Couleur getCouleur(){
		return this.couleur;
	}
	
	Symbole getSymbole(){
		return this.symbole;
	}
}
