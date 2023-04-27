package Modele;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Continuum {
	private List<Carte> continuum; //9 cartes
	
	Continuum(List<Carte> continuum){
		this.continuum = continuum;
	}

	Carte getCarteContinuum(int index){
		return this.continuum.get(index);
	}

	void setCarteContinuum(int index, Carte carte){
		this.continuum.set(index, carte);
	}
	
	int getContinuumSize(){
		return continuum.size();
	}
	

	
	/**
	 * TODO: tester fonction
	 * <P>
	* <BR> Renvoie les déplacements possibles pour toutes les cartes en main.
	* <BR> L'élément renvoyé est un tableau de taille 3 contenant un sous tableau de tous les
	*  indices valides pour un déplacement.
	*
	* @param cartes Une liste correspondant aux 3 cartes en main
	* @param indice_sorcier l'index du sorcier sur le Continuum
	* <P>
	* @return Un tableau de 3 tableaux d'entiers où tab[0] est le tableau des déplacements possibles
	* à partir de cartes[0]
	*
	*/
	int[][] calculeOptions(List<Carte> cartes, int indice_sorcier){
		int[][] tab_options = new int[cartes.size()][];
		for (int i = 0; i < cartes.size(); i++) {
			List<Integer> coups_possibles = this.getCoupsPossibles(cartes.get(i), indice_sorcier, 0);
			tab_options[i] = new int[coups_possibles.size()];
			for (int j = 0; j < coups_possibles.size(); j++) {
				tab_options[i][j] = coups_possibles.get(j);
			}
		}
		return tab_options;
	}
	
	/**
	* <P> TODO: tester fonction </P>
	*
	* <P> Renvoie les déplacements possibles suivant la carte jouée et l'index du sorcier. Peu importe la direction.
	* <BR>  L'élément renvoyé est une liste chaînée d'index de cartes où le déplacement est valide.
	* <BR> Il existe toujours au moins 1 déplacement possible. </P>
	*
	* <P>
    * @param carte_jouee La carte jouée durant ce tour
	* @param num_sorcier L'index du sorcier sur le Continuum
	* @param direction La direction où le sorcier doit se déplacer pour aller dans le Futur (-1 ou +1)
	* </P>
	* <P>
	* @return Une liste chaînée d'index valides.
	* </P>
	*/
	public LinkedList<Integer> getCoupsPossibles(Carte carte_jouee, int num_sorcier, int direction){
		LinkedList<Integer> index_coups_possibles = new LinkedList<Integer>();
		
		for (int i = num_sorcier-direction; i >= 0 && i < this.getContinuumSize(); i-= direction){
			if(this.getCarteContinuum(i).getSymbole() == carte_jouee.getSymbole() ||
					this.getCarteContinuum(i).getCouleur() == carte_jouee.getCouleur()){
				index_coups_possibles.add(i);
			}
		}
		
		if (((num_sorcier + (carte_jouee.getNumero() * direction)) < this.getContinuumSize()) ||
				(num_sorcier + (carte_jouee.getNumero() * direction)) >= 0){
			index_coups_possibles.add(num_sorcier + (carte_jouee.getNumero() * direction));
		}
		return index_coups_possibles;
	}
	
	
	void echangeAvecMain(List<Carte> main, int indice_sorcier){
		// todo
		// prend comme argument les 3 cartes du main
		// renvoie les 3 cartes echanges
	}

	public static List<Carte> mockContinuum(){
		List<Carte> res = new LinkedList<>();
		res.add(new Carte(1, Couleur.ROUGE, Symbole.CLEF));
		res.add(new Carte(2, Couleur.VERT, Symbole.PAPIER));
		res.add(new Carte(3, Couleur.BLEU, Symbole.CRANE));
		res.add(new Carte(2, Couleur.VERT, Symbole.CRANE));
		res.add(new Carte(1, Couleur.VIOLET, Symbole.PAPIER));
		res.add(new Carte(3, Couleur.VERT, Symbole.CHAMPIGNON));
		res.add(new Carte(3, Couleur.ROUGE, Symbole.CHAMPIGNON));
		res.add(new Carte(2, Couleur.VERT, Symbole.CLEF));
		res.add(new Carte(1, Couleur.BLEU, Symbole.PAPIER));
		return res;
	}
}
