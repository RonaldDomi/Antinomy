package Modele;

public class Carte {
	int numero; //1 à 4
	String couleur;
//	public enum Symbole{
//		symbole1,
//		symbole2,
//		symbole3,
//		symbole4
//	}
	String symbole;
	
	
	Carte(int num,String col, String sym){
		this.numero = num;
		this.couleur = col;
		this.symbole = sym;
	}
	
	int getNumero(){
		return this.numero;
	}
	
	String getCouleur(){
		return this.couleur;
	}
	
	String getSymbole(){
		return this.symbole;
	}
}
