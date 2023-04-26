package Modele;


public class Carte {
	private int numero; //1 à 4
	private Couleur couleur;
	private Symbole symbole;

	public Carte(int numero, Couleur couleur, Symbole symbole){
		this.numero = numero;
		this.couleur = couleur;
		this.symbole = symbole;
	}

	
	int getNumero(){
		return this.numero;
	}
	
	Couleur getCouleur(){
		return this.couleur;
	}
	
	Symbole getSymbole(){
		return this.symbole;
	}

	void setNumero(int numero){this.numero = numero;};
	void setCouleur(Couleur couleur){this.couleur = couleur;};
	void setSymbole(Symbole symbole){this.symbole = symbole;};

	public void cycleCouleur() {
		switch (couleur) {
			case VERT:
				couleur = Couleur.ORANGE;
				break;
			case ORANGE:
				couleur = Couleur.BLEU;
				break;
			case BLEU:
				couleur = Couleur.VIOLET;
				break;
			case VIOLET:
				couleur = Couleur.ORANGE;
				break;
			default:
				// Handle any unexpected cases
				break;
		}
	}

	@Override
	public String toString() {
		return "Card{" +
				", couleur='" + couleur + '\'' +
				", symbole='" + symbole + '\'' +
				"numero'" + numero + '\'' +
				'}';
	}
}
