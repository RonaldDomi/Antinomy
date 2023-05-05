package Modele;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Continuum implements Cloneable {
	private Carte[] continuum; //9 cartes

	Continuum(Carte[] continuum){
		this.continuum = continuum;
	}

	Continuum(){}

	Continuum(String stringContinuum){
//		Continuum: carte1,carte2,carte3,....
		String[] strings = stringContinuum.split(",");
		this.continuum = new Carte[9];
		for(int i = 0; i < strings.length; i++){
			continuum[i] = new Carte(strings[i]);
		}
	}

	Carte getCarteContinuum(int index){
		return this.continuum[index];
	}

	void setCarteContinuum(int index, Carte carte){
		this.continuum[index] = carte;
	}

	Carte[] getContinuum(){
		return continuum;
	}

	int getContinuumSize(){
		return continuum.length;
	}


	/**
	 * Renvoie les déplacements possibles pour toutes les cartes en main.
	 * <BR> L'élément renvoyé est un tableau de taille 3 contenant un sous tableau de tous les
	 *  indices valides pour un déplacement.
	 *
	 * @param cartes Une liste correspondant aux 3 cartes en main
	 * @param indice_sorcier l'index du sorcier sur le Continuum
	 * <P>
	 * @return Un tableau de 3 tableaux d'entiers où tab[0] est le tableau des déplacements possibles
	 * à partir de cartes[0]
	 * </P>
	 * <BR>
	 * @see #getCoupsPossibles(Carte, int, int)
	 */
	int[][] calculeOptions(Carte[] cartes, int indice_sorcier){
		int[][] tab_options = new int[cartes.length][];
		for (int i = 0; i < cartes.length; i++) {
			List<Integer> coups_possibles = this.getCoupsPossibles(cartes[i], indice_sorcier, 0);
			if (coups_possibles.size() != 0){
				tab_options[i] = new int[coups_possibles.size()];
				for (int j = 0; j < coups_possibles.size(); j++) {
					tab_options[i][j] = coups_possibles.get(j);
				}
			}
		}
		return tab_options;
	}


	/**
	 * <P> Renvoie les déplacements possibles suivant la carte jouée et l'index du sorcier. Peu importe la direction.
	 * <BR>  L'élément renvoyé est une liste chaînée d'index de cartes où le déplacement est valide.
	 * <BR> Il peut n'y avoir aucun déplacement possible pour une carte donnée. </P>
	 *
	 * <P>
    * @param carte_jouee La carte jouée durant ce tour
	 * @param num_sorcier L'index du sorcier sur le Continuum
	 * @param direction La direction où le sorcier doit se déplacer pour aller dans le Futur (-1 ou +1)
	 * </P>
	 * <P>
	 * @return Une liste chaînée d'index valides, vide ou non.
	 * </P>
	 */
	public LinkedList<Integer> getCoupsPossibles(Carte carte_jouee, int num_sorcier, int direction){
		LinkedList<Integer> index_coups_possibles = new LinkedList<>();

		for (int i = num_sorcier-direction; i >= 0 && i < this.getContinuumSize(); i-= direction){
			if(this.getCarteContinuum(i).getSymbole() == carte_jouee.getSymbole() ||
					this.getCarteContinuum(i).getCouleur() == carte_jouee.getCouleur()){
				index_coups_possibles.add(i);
			}
		}

		int projection = num_sorcier + carte_jouee.getNumero() * direction;
		if ((projection < this.getContinuumSize()) && projection >= 0){
			index_coups_possibles.add(projection);
		}

		return index_coups_possibles;
	}

	@Override
	public String toString() {
		String res = "";
		for(int i=0; i<this.continuum.length; i++){
			res = res + continuum[i].toString() + ",";
		}
		return res;
	}

	public static Carte[] mockContinuum(){
		Carte[] res = new Carte[9];
		res[0] = new Carte(1, Couleur.ROUGE, Symbole.CLEF);
		res[1] = new Carte(2, Couleur.VERT, Symbole.PAPIER);
		res[2] = new Carte(3, Couleur.BLEU, Symbole.CRANE);
		res[3] = new Carte(2, Couleur.VERT, Symbole.CRANE);
		res[4] = new Carte(1, Couleur.VIOLET, Symbole.PAPIER);
		res[5] = new Carte(3, Couleur.VERT, Symbole.CHAMPIGNON);
		res[6] = new Carte(3, Couleur.ROUGE, Symbole.CHAMPIGNON);
		res[7] = new Carte(2, Couleur.VERT, Symbole.CLEF);
		res[8] = new Carte(1, Couleur.BLEU, Symbole.PAPIER);
		return res;
	}

	@Override
	public Continuum clone() throws CloneNotSupportedException {
//		Continuum c = (Continuum) super.clone();
		Continuum c = new Continuum();
		c.continuum = new Carte[9];
		for (int i = 0; i < 9; i++) {
			c.continuum[i] = continuum[i].clone();
		}
		return c;
	}


	public LinkedList<Integer> getIndexSorcierPossible(Couleur couleur_interdite){
		LinkedList<Integer> tabIndex = new LinkedList<>();

		for (int i = 0; i < 9; i++) {
			if (this.getCarteContinuum(i).getCouleur() == couleur_interdite){
				tabIndex.add(i);
			}
		}
		return tabIndex;
	}
}
