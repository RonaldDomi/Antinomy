
package Modele;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <P> Vérifie le bon fonctionnement de la classe Deck. </P>
 * <P> Teste la distribution des cartes et le mélange. </P>
 * */
public class DeckTest {

	private static Deck deckTest;

	@BeforeEach
	public void BeforeEach() {
		deckTest = new Deck();
	}

	@Test
	public void testInitDeck() {
		assertEquals(deckTest.deck.size(), 16);
	}

	@Test
	public void testTailleDistribueDeck() {
		Carte[] main1 = deckTest.distribuer(3);
		Carte[] main2 = deckTest.distribuer(3);
		assertEquals(3, main1.length);
		assertEquals(3, main2.length);
		assertEquals( 16 - 3 - 3, deckTest.deck.size());

		Carte[] continuum = deckTest.distribuer(9);
		assertEquals(9, continuum.length);
		assertEquals(16 - 3 - 3 - 9, deckTest.deck.size());

		Codex codexTest = new Codex(deckTest.distribuer(1)[0], continuum[0].getCouleur());
		assertNotNull(codexTest.getCarte());

		assertEquals(16 - 3 - 3 - 9 - 1, deckTest.deck.size());
		assertNull(deckTest.distribuer(1));
	}

	@Test
	public void testCartesDifferentes() {
		for (int i = 0; i < deckTest.deck.size(); i++) {
			for (int j = 0; j < deckTest.deck.size(); j++) {
				if (i != j) {
					assertNotEquals(deckTest.deck.get(i), deckTest.deck.get(j));
				}
			}
		}

		Carte[] main1 = deckTest.distribuer(3);
		Carte[] main2 = deckTest.distribuer(3);

		for (int i = 0; i < main1.length; i++) {
			assertNotEquals(main1[i], main2[i]);
		}
	}

	
	/* Il ne devrait pas y avoir de faux positifs avec ces assertions,
	 Collections.shuffle() déplace toujours un élément de la liste */
	@RepeatedTest(30000)
	public void testDeckMelange() {
		assertNotEquals(deckTest.deck.get(1), new Carte(1, Couleur.VIOLET, Symbole.CHAMPIGNON));
		assertNotEquals(deckTest.deck.get(2), new Carte(2, Couleur.BLEU, Symbole.CHAMPIGNON));
		assertNotEquals(deckTest.deck.get(3), new Carte(3, Couleur.ROUGE, Symbole.CHAMPIGNON));
		assertNotEquals(deckTest.deck.get(4), new Carte(4, Couleur.VERT, Symbole.CHAMPIGNON));
	}
}
