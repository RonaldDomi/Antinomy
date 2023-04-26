package Modele;

public class Codex {
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
}



