package Modele;

public class Codex implements Cloneable{
	private Carte carte;
	private Couleur couleurInterdite;

	public Codex(Carte carte, Couleur couleurInterdite){
		this.carte = carte;
		this.couleurInterdite = couleurInterdite;
	}

	public Codex(){};

	public Codex(String stringCodex){
//		Codex: carte,couleur
		String[] strings = stringCodex.split(",");
		carte = new Carte(strings[0]);
		couleurInterdite = Couleur.parseCouleur(strings[1]);
	}

	Carte getCarte(){
		return this.carte;
	}
	public Couleur getCouleurInterdite(){
		return this.couleurInterdite;
	}
	void setCarte(Carte carte){ this.carte = carte;}
	void setCouleurInterdite(Couleur couleurInterdite){this.couleurInterdite = couleurInterdite;}

	public Codex clone(){
//		Codex c = (Codex) super.clone();
		Codex c = new Codex(this.carte, this.couleurInterdite);
		return c;
	}

	@Override
	public String toString() {
		String res = carte.toString() + "," + couleurInterdite;
		return res;
	}

	public void cycleCouleur() {
		switch (couleurInterdite) {
			case VERT:
				couleurInterdite = Couleur.ROUGE;
				break;
			case ROUGE:
				couleurInterdite = Couleur.BLEU;
				break;
			case BLEU:
				couleurInterdite = Couleur.VIOLET;
				break;
			case VIOLET:
				couleurInterdite = Couleur.VERT;
				break;
			default:
				// Handle any unexpected cases
				break;
		}
	}
}



