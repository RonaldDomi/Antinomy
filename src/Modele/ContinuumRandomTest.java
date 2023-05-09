
package Modele;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class ContinuumRandomTest {


	Deck deckTest;
	Continuum continuum;


	@BeforeEach
	void beforeEach(){
		deckTest = new Deck();
		continuum = new Continuum(deckTest.distribuer(9));
	}


	@Test
	void testSetCarteContinuum() {
		Carte carte_avant = continuum.getCarteContinuum(3);
		continuum.setCarteContinuum(3, new Carte(1, Couleur.BLEU, Symbole.CHAMPIGNON));
		assertNotEquals(carte_avant, continuum.getCarteContinuum(3));
	}


	@Test
	void testGetContinuumSize() {
		assertEquals(9, continuum.getContinuumSize());
	}


	@RepeatedTest(1000)
	void testGetCoupsPossiblesJoueur1BordMax() {

		int index_sorcier = 8;
		Carte mock_carte = new Carte(1, Couleur.BLEU, Symbole.CHAMPIGNON);
		LinkedList<Integer> res = continuum.getCoupsPossibles(mock_carte, index_sorcier, 1);

		assertTrue(res.size() <= 7);
		for (Integer index: res
		     ) {
			assertTrue(index < index_sorcier);
			Carte carte_selectionnee = continuum.getCarteContinuum(index);
			assertTrue(carte_selectionnee.getCouleur() == mock_carte.getCouleur() ||
					carte_selectionnee.getSymbole() == mock_carte.getSymbole());
		}
	}


	@RepeatedTest(1000)
	void testGetCoupsPossiblesJoueur2BordMax() {

		int index_sorcier = 0;
		Carte mock_carte = new Carte(1, Couleur.BLEU, Symbole.CHAMPIGNON);
		LinkedList<Integer> res = continuum.getCoupsPossibles(mock_carte, index_sorcier, -1);

		assertTrue(res.size() <= 7);
		for (Integer index: res
		) {
			Carte carte_selectionnee = continuum.getCarteContinuum(index);
			assertTrue(carte_selectionnee.getCouleur() == mock_carte.getCouleur() ||
					carte_selectionnee.getSymbole() == mock_carte.getSymbole());
		}
	}


	@RepeatedTest(1000)
	void testGetCoupsPossiblesJoueur1BordMilieu() {

		int index_sorcier = 4;
		Carte mock_carte = new Carte(1, Couleur.BLEU, Symbole.CHAMPIGNON);
		LinkedList<Integer> res = continuum.getCoupsPossibles(mock_carte, index_sorcier, 1);

		assertTrue(res.size() > 0);
		assertTrue(res.size() <= 7);
		int i = 0;
		int index;
		Carte carte_selectionnee;
		while((index = res.get(i)) < index_sorcier){
			carte_selectionnee = continuum.getCarteContinuum(index);
			assertTrue(carte_selectionnee.getCouleur() == mock_carte.getCouleur() ||
					carte_selectionnee.getSymbole() == mock_carte.getSymbole());
			i++;
		}

		assertEquals(res.get(i), index_sorcier + mock_carte.getNumero());
	}


	@RepeatedTest(1000)
	void testGetCoupsPossiblesJoueur2BordMilieu() {

		int index_sorcier = 4;
		Carte mock_carte = new Carte(1, Couleur.BLEU, Symbole.CHAMPIGNON);
		LinkedList<Integer> res = continuum.getCoupsPossibles(mock_carte, index_sorcier, -1);

		assertTrue(res.size() > 0);
		assertTrue(res.size() <= 7);
		int i = 0;
		int index;
		Carte carte_selectionnee;
		while ((index = res.get(i)) > index_sorcier) {
			carte_selectionnee = continuum.getCarteContinuum(index);
			assertTrue(carte_selectionnee.getCouleur() == mock_carte.getCouleur() ||
					carte_selectionnee.getSymbole() == mock_carte.getSymbole());
			i++;
		}

		assertEquals(res.get(i), index_sorcier - mock_carte.getNumero());
	}


	@RepeatedTest(1000)
	void testGetCoupsPossiblesJoueur1BordMin() {

		int index_sorcier = 0;
		Carte mock_carte = new Carte(1, Couleur.BLEU, Symbole.CHAMPIGNON);
		LinkedList<Integer> res = continuum.getCoupsPossibles(mock_carte, index_sorcier, 1);

		assertEquals(1, res.size());
		assertEquals(index_sorcier+ mock_carte.getNumero(), res.get(0));
	}


	@RepeatedTest(1000)
	void testGetCoupsPossiblesJoueur2BordMin() {
		int index_sorcier = 8;
		Carte mock_carte = new Carte(1, Couleur.BLEU, Symbole.CHAMPIGNON);
		LinkedList<Integer> res = continuum.getCoupsPossibles(mock_carte, index_sorcier, -1);

		assertEquals(1, res.size());
		assertEquals(7, res.get(0));
	}


	@RepeatedTest(1000)
	void calculeOptionsJoueur1BordMax() {
		int index_sorcier = 8;
		Carte[] main = {new Carte(1, Couleur.BLEU, Symbole.CHAMPIGNON),
				new Carte(4, Couleur.VIOLET, Symbole.PAPIER),
				new Carte(2, Couleur.ROUGE, Symbole.CRANE)};

		for (int i = 0; i < main.length; i++) {
			LinkedList<Integer> resMain = continuum.getCoupsPossibles(main[i], index_sorcier, 1);
			assertTrue(resMain.size() <= 7);

			for (int index:resMain){
				Carte carte_selectionnee = continuum.getCarteContinuum(index);
				assertTrue(carte_selectionnee.getCouleur() == main[i].getCouleur() ||
						carte_selectionnee.getSymbole() == main[i].getSymbole());
			}

		}
	}


	@RepeatedTest(1000)
	void calculeOptionsJoueur2BordMax() {
		int index_sorcier = 0;
		Carte[] main = {new Carte(1, Couleur.BLEU, Symbole.CHAMPIGNON),
				new Carte(4, Couleur.VIOLET, Symbole.PAPIER),
				new Carte(2, Couleur.ROUGE, Symbole.CRANE)};

		for (int i = 0; i < main.length; i++) {
			LinkedList<Integer> resMain = continuum.getCoupsPossibles(main[i], index_sorcier, -1);
			assertTrue(resMain.size() <= 7);

			for (int index:resMain){
				Carte carte_selectionnee = continuum.getCarteContinuum(index);
				assertTrue(carte_selectionnee.getCouleur() == main[i].getCouleur() ||
						carte_selectionnee.getSymbole() == main[i].getSymbole());
			}

		}
	}


	@RepeatedTest(1000)
	void calculeOptionsJoueur1BordMilieu() {
		int index_sorcier = 4;
		Carte[] main = {new Carte(1, Couleur.BLEU, Symbole.CHAMPIGNON),
				new Carte(4, Couleur.VIOLET, Symbole.PAPIER),
				new Carte(2, Couleur.ROUGE, Symbole.CRANE)};

		for (int i = 0; i < main.length; i++) {
			LinkedList<Integer> resMain = continuum.getCoupsPossibles(main[i], index_sorcier, 1);
			assertTrue(resMain.size() > 0);
			assertTrue(resMain.size() <= 7);

			for (int index:resMain) {
				if (index < index_sorcier){
					Carte carte_selectionnee = continuum.getCarteContinuum(index);
					assertTrue(carte_selectionnee.getCouleur() == main[i].getCouleur() ||
							carte_selectionnee.getSymbole() == main[i].getSymbole());
				}else{
					assertEquals(index, index_sorcier + main[i].getNumero());
				}
			}

		}

	}


	@RepeatedTest(1000)
	void calculeOptionsJoueur2BordMilieu() {
		int index_sorcier = 4;
		Carte[] main = {new Carte(1, Couleur.BLEU, Symbole.CHAMPIGNON),
				new Carte(4, Couleur.VIOLET, Symbole.PAPIER),
				new Carte(2, Couleur.ROUGE, Symbole.CRANE)};

		for (int i = 0; i < main.length; i++) {
			LinkedList<Integer> resMain = continuum.getCoupsPossibles(main[i], index_sorcier, -1);
			assertTrue(resMain.size() > 0);
			assertTrue(resMain.size() <= 7);

			for (int index:resMain) {
				if (index > index_sorcier){
					Carte carte_selectionnee = continuum.getCarteContinuum(index);
					assertTrue(carte_selectionnee.getCouleur() == main[i].getCouleur() ||
							carte_selectionnee.getSymbole() == main[i].getSymbole());
				}else{
					assertEquals(index, index_sorcier - main[i].getNumero());
				}
			}

		}

	}


	@RepeatedTest(1000)
	void testGetCoupsPossiblesJoueur1DroiteImpossible() {

		int index_sorcier = 6;
		Carte mock_carte = new Carte(3, Couleur.BLEU, Symbole.CHAMPIGNON);
		LinkedList<Integer> res = continuum.getCoupsPossibles(mock_carte, index_sorcier, 1);

		for (int index:res) {
			assertTrue(index < 6);
		}
	}


	@RepeatedTest(1000)
	void testGetCoupsPossiblesJoueur2DroiteImpossible() {

		int index_sorcier = 3;
		Carte mock_carte = new Carte(4, Couleur.BLEU, Symbole.CHAMPIGNON);
		LinkedList<Integer> res = continuum.getCoupsPossibles(mock_carte, index_sorcier, -1);

		for (int index:res) {
			assertTrue(index > 3);
		}
	}
}
