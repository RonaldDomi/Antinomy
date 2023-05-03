package Modele;


public class Carte implements Cloneable {
	private int numero; //1 à 4
	private Couleur couleur;
	private Symbole symbole;

	public Carte(int numero, Couleur couleur, Symbole symbole){
		this.numero = numero;
		this.couleur = couleur;
		this.symbole = symbole;
	}

	public Carte(String stringCarte){
//		Carte:  couleur|symbole|numero
		String[] strings = stringCarte.split("|");

		// parse "Couleur" to Couleur
		this.couleur = Couleur.parseCouleur(strings[0]);
		// parse Symbole
		this.symbole = Symbole.parseSymbole(strings[1]);
		this.numero = Integer.parseInt(strings[2]);

	}


	
	public int getNumero(){
		return this.numero;
	}
	
	public Couleur getCouleur(){
		return this.couleur;
	}
	
	public Symbole getSymbole(){
		return this.symbole;
	}

	void setNumero(int numero){this.numero = numero;};
	void setCouleur(Couleur couleur){this.couleur = couleur;};
	void setSymbole(Symbole symbole){this.symbole = symbole;};

	public void cycleCouleur() {
		switch (couleur) {
			case VERT:
				couleur = Couleur.ROUGE;
				break;
			case ROUGE:
				couleur = Couleur.BLEU;
				break;
			case BLEU:
				couleur = Couleur.VIOLET;
				break;
			case VIOLET:
				couleur = Couleur.VERT;
				break;
			default:
				// Handle any unexpected cases
				break;
		}
	}

	@Override
	public String toString() {
		String res = couleur + "|" + symbole + "|" + numero;
		return res;
	}

	@Override
	public Carte clone() throws CloneNotSupportedException {
		Carte c = (Carte) super.clone();
		c = new Carte(numero, couleur, symbole);
		return c;
	}
}
