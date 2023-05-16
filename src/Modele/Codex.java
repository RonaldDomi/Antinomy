package Modele;

/** <P>Représente la carte du Codex avec sa couleur interdite.</P>
 * */
public class Codex implements Cloneable{
	private Carte carte;
	private Couleur couleurInterdite;

	public Codex(Carte carte, Couleur couleurInterdite){
		this.carte = carte;
		this.couleurInterdite = couleurInterdite;
	}

	public Codex(){};
	
	/**
	 * <P>Reconstruit un Codex à partir de sa représentation textuelle.</P>
	 *
	 * @param stringCodex Représentation textuelle du Codex.
	 * */
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
	
	/**
	 * <P> Clone le Codex dans un nouvel objet. </P>
	 * @return Le Codex cloné.
	 */
	public Codex clone(){
//		Codex c = (Codex) super.clone();
		Codex c = new Codex(this.carte, this.couleurInterdite);
		return c;
	}
	
	/** <P> Représentation textuelle du Codex. </P>
	 * <P> La carte suivi de la couleur interdite, séparées par une virgule. </P>
	 *
	 * @return Forme textuelle du Codex.
	 */
	@Override
	public String toString() {
		String res = carte.toString() + "," + couleurInterdite;
		return res;
	}

	/** <P> Change la couleur interdite. </P>
	 * <P> Le cycle est prédéfini. Il est visible sur la représentation graphique du Codex. </P>
	 */
	public void cycleCouleur() {
		switch (couleurInterdite) {
			case VERT:
				couleurInterdite = Couleur.VIOLET;
				break;
			case ROUGE:
				couleurInterdite = Couleur.VERT;
				break;
			case BLEU:
				couleurInterdite = Couleur.ROUGE;
				break;
			case VIOLET:
				couleurInterdite = Couleur.BLEU;
				break;
			default:
				System.err.println("Couleur indéfinie??");
				break;
		}
	}
}



