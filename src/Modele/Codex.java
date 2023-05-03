package Modele;

public class Codex {
	private Carte carte;
	private Couleur couleurInterdite;

	public Codex(Carte carte){
		this.carte = carte;
		this.couleurInterdite = Couleur.VERT;
	}

	public Codex(String stringCodex){
//		Codex: carte,couleur
		String[] strings = stringCodex.split(",");
		carte = new Carte(strings[0]);
		couleurInterdite = Couleur.parseCouleur(strings[1]);
	}

	Carte getCarte(){
		return this.carte;
	}
	Couleur getCouleurInterdite(){
		return this.couleurInterdite;
	}
	void setCarte(Carte carte){ this.carte = carte;}
	void setCouleurInterdite(Couleur couleurInterdite){this.couleurInterdite = couleurInterdite;}

	@Override
	public String toString() {
		String res = carte.toString() + "," + couleurInterdite;
		return res;
	}
}



