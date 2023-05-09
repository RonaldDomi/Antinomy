//
//package Modele;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.RepeatedTest;
//import org.junit.jupiter.api.Test;
//
//import java.util.Random;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class InfoJoueurTest {
//
//	InfoJoueur testInfoJoueur;
//	@BeforeEach
//	void beforeEach(){
//		testInfoJoueur = new InfoJoueur(new Random());
//		Carte[] main = {new Carte(1, Couleur.BLEU, Symbole.CHAMPIGNON),
//				new Carte(4, Couleur.VIOLET, Symbole.PAPIER),
//				new Carte(2, Couleur.ROUGE, Symbole.CRANE)};
//		testInfoJoueur.setMain(main);
//
//	}
//
//	@RepeatedTest(30)
//	void testChangePoints(){
//		int points_expected = 0;
//		assertEquals(points_expected, testInfoJoueur.getPoints());
//		if (testInfoJoueur.r.nextBoolean()){
//			testInfoJoueur.addPoint();
//			assertEquals(++points_expected, testInfoJoueur.getPoints());
//		}else{
//			testInfoJoueur.remPoint();
//			assertEquals(--points_expected, testInfoJoueur.getPoints());
//		}
//		testInfoJoueur.getCarteAleatoire();
//	}
//
//
//	@RepeatedTest(30)
//	void testChangeCarte(){
//
//		int pos = testInfoJoueur.r.nextInt(5);
//		if (pos < 3){
//			Carte carte = new Carte(4, Couleur.VIOLET, Symbole.PAPIER);
//			testInfoJoueur.changeCarte(pos, carte);
//			assertEquals(testInfoJoueur.getCarteMain(pos), carte);
//		}else{
//			assertNull(testInfoJoueur.changeCarte(3, new Carte(4, Couleur.VIOLET, Symbole.PAPIER)));
//		}
//	}
//
//
//	@RepeatedTest(10)
//	void testExisteParadoxCouleur() {
//		Couleur couleurRandom;
//		do {
//			couleurRandom = Couleur.getRandomCouleur();
//		} while (couleurRandom == Couleur.VERT);
//
//		Carte[] mainCouleur = {new Carte(1, couleurRandom, Symbole.CRANE),
//				new Carte(2, couleurRandom, Symbole.CLEF),
//				new Carte(3, couleurRandom, Symbole.CHAMPIGNON),
//		};
//
//		testInfoJoueur.setMain(mainCouleur);
//		assertTrue(testInfoJoueur.existeParadox(Couleur.VERT));
//		assertFalse(testInfoJoueur.existeParadox(couleurRandom));
//	}
//
//	@RepeatedTest(10)
//	void testExisteParadoxSymbole() {
//
//		Symbole symboleRandom = Symbole.getRandomSymbole();
//
//		Carte[] mainSymbole = {new Carte(1, Couleur.BLEU, symboleRandom),
//				new Carte(2, Couleur.ROUGE, symboleRandom),
//				new Carte(3, Couleur.VIOLET, symboleRandom),
//		};
//
//		testInfoJoueur.setMain(mainSymbole);
//		assertTrue(testInfoJoueur.existeParadox(Couleur.VERT));
//	}
//
//	@RepeatedTest(10)
//	void testExisteParadoxIndice() {
//		int indiceCarte = testInfoJoueur.r.nextInt(5);
//
//		Carte[] mainIndice = {
//				new Carte(indiceCarte, Couleur.getRandomCouleur(), Symbole.getRandomSymbole()),
//				new Carte(indiceCarte, Couleur.getRandomCouleur(), Symbole.getRandomSymbole()),
//				new Carte(indiceCarte, Couleur.getRandomCouleur(), Symbole.getRandomSymbole()),
//		};
//
//		testInfoJoueur.setMain(mainIndice);
//
//		if (testInfoJoueur.existeParadox(Couleur.getRandomCouleur())){
//			assertEquals(mainIndice[0].getNumero(), mainIndice[1].getNumero());
//			assertEquals(mainIndice[1].getNumero(), mainIndice[2].getNumero());
//		}else{
//
//		}
//
//	}
//	@RepeatedTest(10)
//	void testExisteParadoxNulle(){
//		Carte[] mainNulle = {new Carte(1, Couleur.BLEU, Symbole.CRANE),
//				new Carte(2, Couleur.ROUGE, Symbole.PAPIER),
//				new Carte(3, Couleur.VIOLET, Symbole.CHAMPIGNON),
//		};
//
//		testInfoJoueur.setMain(mainNulle);
//		assertFalse(testInfoJoueur.existeParadox(Couleur.BLEU));
//	}
//
//
//	@RepeatedTest(10)
//	void testSommeMain(){
//		Couleur couleur_interdite = Couleur.getRandomCouleur();
//
//		switch (couleur_interdite){
//			case BLEU:
//				assertEquals(testInfoJoueur.sommeMain(couleur_interdite), 6);
//				break;
//			case ROUGE:
//				assertEquals(testInfoJoueur.sommeMain(couleur_interdite), 5);
//				break;
//			case VIOLET:
//				assertEquals(testInfoJoueur.sommeMain(couleur_interdite), 3);
//				break;
//			case VERT:
//				assertEquals(testInfoJoueur.sommeMain(couleur_interdite), 7);
//				break;
//			default:
//				System.err.println("Uh oh une nouvelle couleur????");
//				fail();
//		}
//	}
//
//
//	@Test
//	void testSommeMainNulle(){
//		Carte[] main = {new Carte(1, Couleur.BLEU, Symbole.CHAMPIGNON),
//				new Carte(4, Couleur.BLEU, Symbole.PAPIER),
//				new Carte(2, Couleur.BLEU, Symbole.CRANE)};
//		testInfoJoueur.setMain(main);
//
//		assertEquals(0, testInfoJoueur.sommeMain(Couleur.BLEU));
//	}
//
//
//	@RepeatedTest(1000)
//	void testMoveSorcier(){
//		Random rand = new Random();
//		int rand_dep, rand_pos;
//
//		do{
//			rand_dep = rand.nextInt()%8;
//			rand_pos = rand.nextInt(9);
//		}while (rand_dep == 0);
//
//
//		testInfoJoueur.setSorcierIndice(rand_pos);
//
//		if((rand_pos + (rand_dep * testInfoJoueur.getDirection()) >= 0) && (rand_pos + (rand_dep * testInfoJoueur.getDirection()) <= 8)){
//			assertTrue(testInfoJoueur.moveSorcier(rand_dep));
//		}else{
//			assertFalse(testInfoJoueur.moveSorcier(rand_dep));
//		}
//	}
//}
//
