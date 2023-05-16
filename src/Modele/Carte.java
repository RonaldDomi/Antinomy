package Modele;

/** <P>Représente une carte du Jeu.</P>
 * <P>Contient le numéro, le symbole et la couleur</P>
 */
public class Carte implements Cloneable {
	private int numero; //1 à 4
	private Couleur couleur;
	private Symbole symbole;

	public Carte(int numero, Couleur couleur, Symbole symbole){
		this.numero = numero;
		this.couleur = couleur;
		this.symbole = symbole;
	}
	
	public Carte(Carte carte){
		this.numero = carte.getNumero();
		this.couleur = carte.getCouleur();
		this.symbole = carte.getSymbole();
	}
	
	/**
	 * <P>Reconstruit une Carte à partir de sa représentation textuelle.</P>
	 *
	 * @param stringCarte Représentation textuelle d'une Carte.
	 * */
	public Carte(String stringCarte){
//		Carte:  couleur|symbole|numero
		String[] strings = stringCarte.split("\\|");

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
	
	/** <P> Représentation textuelle d'une Carte. </P>
	 * <P> La coueleur suivi du symbole suivi du numéro, séparées par un | </P>
	 *
	 * @return Forme textuelle de la carte.
	 */
	@Override
	public String toString() {
		return couleur + "|" + symbole + "|" + numero;
	}
	
	/**
	 * <P> Clone la Carte dans un nouvel objet. </P>
	 * @return La Carte clonée.
	 */
	@Override
	public Carte clone() {
		return new Carte(numero, couleur, symbole);
	}
	
	/**
	 * <P> Vérifie si la carte est équivalente à la carte donnée en argument. </P>
	 * @param carte La carte où vérifier l'équivalence
	 * @return Un booléen indiquant si les cartes sont équivalentes.
	 */
	public boolean sontEquivalentes(Carte carte){
		return this.getNumero() == carte.getNumero() && this.getSymbole() == carte.getSymbole() && this.getCouleur() == carte.getCouleur();
	}
}
