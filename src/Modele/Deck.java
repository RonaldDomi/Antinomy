package Modele;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Deck {
	ArrayList<Carte> deck; //16 cartes
	
	Deck(){
		deck = new ArrayList<>();
		initDeck();
		melangerDeck();
	}
	
	/**
	 * <P> Ajoute les 16 cartes différentes dans le Deck. </P>
	 */
	void initDeck(){
		deck.add(new Carte(1, Couleur.VIOLET, Symbole.CHAMPIGNON));
		deck.add(new Carte(2, Couleur.BLEU, Symbole.CHAMPIGNON));
		deck.add(new Carte(3, Couleur.ROUGE, Symbole.CHAMPIGNON));
		deck.add(new Carte(4, Couleur.VERT, Symbole.CHAMPIGNON));
		
		deck.add(new Carte(1, Couleur.VERT, Symbole.PAPIER));
		deck.add(new Carte(2, Couleur.VIOLET, Symbole.PAPIER));
		deck.add(new Carte(3, Couleur.BLEU, Symbole.PAPIER));
		deck.add(new Carte(4, Couleur.ROUGE, Symbole.PAPIER));
		
		deck.add(new Carte(1, Couleur.ROUGE, Symbole.CLEF));
		deck.add(new Carte(2, Couleur.VERT, Symbole.CLEF));
		deck.add(new Carte(3, Couleur.VIOLET, Symbole.CLEF));
		deck.add(new Carte(4, Couleur.BLEU, Symbole.CLEF));
		
		deck.add(new Carte(1, Couleur.BLEU, Symbole.CRANE));
		deck.add(new Carte(2, Couleur.ROUGE, Symbole.CRANE));
		deck.add(new Carte(3, Couleur.VERT, Symbole.CRANE));
		deck.add(new Carte(4, Couleur.VIOLET, Symbole.CRANE));
	}
	
	/** Mélange en place le Deck.
	*/
	void melangerDeck(){
		Collections.shuffle(deck);
	}


	/**
	 * <P> Renvoie un sous tableau de nbCartes cartes. Les retire du Deck. </P>
	 *
	 * @param nbCartes Le nombre de cartes à prendre du Deck.
	 *
	 * @return Un tableau de Cartes de taille nbCartes.
	 * <p>
	 * null si le Deck ne contient pas assez de cartes.
	 *
	*/
	Carte[] distribuer(int nbCartes){
		if (deck.size() < nbCartes){
			System.err.println("Le deck est vide on ne peut pas distribuer !");
			return null;
		}
		
		Carte[] carte_tab = new Carte[nbCartes];
		Random random_index = new Random();
		for (int i = 0; i < nbCartes; i++) {
			if (deck.size()==1){
				carte_tab[i] = deck.remove(0);
			}else{
				carte_tab[i] = deck.remove(random_index.nextInt(deck.size()));
			}
		}
		return carte_tab;
	}
}
