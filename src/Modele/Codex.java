package Modele;

public class Codex implements Cloneable{
	private Carte carte;
	private Couleur couleurInterdite;

	public Codex(Carte carte){
		this.carte = carte;
		this.couleurInterdite = Couleur.VERT;
	}

	Carte getCarte(){
		return this.carte;
	}
	Couleur getCouleurInterdite(){
		return this.couleurInterdite;
	}
	void setCarte(Carte carte){ this.carte = carte;}
	void setCouleurInterdite(Couleur couleurInterdite){this.couleurInterdite = couleurInterdite;}

	public Codex clone() throws CloneNotSupportedException {
		Codex c = (Codex) super.clone();
		c.setCouleurInterdite(couleurInterdite);
		c.setCarte(carte);
		return c;
	}
}



