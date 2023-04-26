package Modele;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class Deck {
	ArrayList<Carte> deck; //16 cartes
	
	Deck(){
		this.deck = new ArrayList<Carte>();
		initDeck();
	}
	
	void initDeck(){
		this.deck.add(new Carte(1, "violet", "anneau"));
		this.deck.add(new Carte(2, "bleu", "anneau"));
		this.deck.add(new Carte(3, "orange", "anneau"));
		this.deck.add(new Carte(4, "vert", "anneau"));
		
		this.deck.add(new Carte(1, "vert", "plume"));
		this.deck.add(new Carte(2, "violet", "plume"));
		this.deck.add(new Carte(3, "bleu", "plume"));
		this.deck.add(new Carte(4, "orange", "plume"));
		
		this.deck.add(new Carte(1, "orange", "clef"));
		this.deck.add(new Carte(2, "vert", "clef"));
		this.deck.add(new Carte(3, "violet", "clef"));
		this.deck.add(new Carte(4, "bleu", "clef"));
		
		this.deck.add(new Carte(1, "bleu", "crane"));
		this.deck.add(new Carte(2, "orange", "crane"));
		this.deck.add(new Carte(3, "vert", "crane"));
		this.deck.add(new Carte(4, "violet", "crane"));
	}
	
	
	void melangerDeck(){
		Collections.shuffle(this.deck);
	}
	
	Carte[] distribuer(int nbCartes){
		if (this.deck.size()==0){
			System.err.println("Le deck est vide on ne peut pas distribuer !");
			return null;
		}
		
		Carte[] carte_tab = new Carte[nbCartes];
		Random randomIndex = new Random();
		for (int i = 0; i < nbCartes; i++) {
			carte_tab[i] = this.deck.remove(randomIndex.nextInt(this.deck.size()-1));
		}
		return carte_tab;
	}
	
	
}
