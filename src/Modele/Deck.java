package Modele;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Deck {
	ArrayList<Carte> deck; //16 cartes
	
	Deck(){
		this.deck = new ArrayList<>();
		initDeck();
		melangerDeck();
	}
	
	/** <P>TODO: Refaire proprement</P>
	 *
	 * Ajoute les 16 cartes différentes dans le Deck.
	 */
	void initDeck(){
		this.deck.add(new Carte(1, Couleur.VIOLET, Symbole.CHAMPIGNON));
		this.deck.add(new Carte(2, Couleur.BLEU, Symbole.CHAMPIGNON));
		this.deck.add(new Carte(3, Couleur.ROUGE, Symbole.CHAMPIGNON));
		this.deck.add(new Carte(4, Couleur.VERT, Symbole.CHAMPIGNON));
		
		this.deck.add(new Carte(1, Couleur.VERT, Symbole.PAPIER));
		this.deck.add(new Carte(2, Couleur.VIOLET, Symbole.PAPIER));
		this.deck.add(new Carte(3, Couleur.BLEU, Symbole.PAPIER));
		this.deck.add(new Carte(4, Couleur.ROUGE, Symbole.PAPIER));
		
		this.deck.add(new Carte(1, Couleur.ROUGE, Symbole.CLEF));
		this.deck.add(new Carte(2, Couleur.VERT, Symbole.CLEF));
		this.deck.add(new Carte(3, Couleur.VIOLET, Symbole.CLEF));
		this.deck.add(new Carte(4, Couleur.BLEU, Symbole.CLEF));
		
		this.deck.add(new Carte(1, Couleur.BLEU, Symbole.CRANE));
		this.deck.add(new Carte(2, Couleur.ROUGE, Symbole.CRANE));
		this.deck.add(new Carte(3, Couleur.VERT, Symbole.CRANE));
		this.deck.add(new Carte(4, Couleur.VIOLET, Symbole.CRANE));
	}
	
	/** Mélange en place le Deck
	*/
	void melangerDeck(){
		Collections.shuffle(this.deck);
	}
	
	/**
	 * Renvoie un sous tableau de nbCartes cartes. Les retire du Deck.
	 * <P>
	 * @param nbCartes le nombre de cartes à prendre du Deck
	 *
	 * <P>
	 * @return un tableau de Cartes de taille nbCartes
	 * <p>
	 * null si le Deck ne contient pas assez de cartes
	 *
	*/
	Carte[] distribuer(int nbCartes){
		if (this.deck.size() < nbCartes){
			System.err.println("Le deck est vide on ne peut pas distribuer !");
			return null;
		}
		
		Carte[] carte_tab = new Carte[nbCartes];
		Random random_index = new Random();
		for (int i = 0; i < nbCartes; i++) {
			if (this.deck.size()==1){
				carte_tab[i] = this.deck.remove(0);
			}else{
				carte_tab[i] = this.deck.remove(random_index.nextInt(this.deck.size()-1));
			}
		}
		return carte_tab;
	}
}
