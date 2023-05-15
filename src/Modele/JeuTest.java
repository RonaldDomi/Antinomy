//
//package Modele;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.RepeatedTest;
//
//import java.util.Random;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class JeuTest {
//
//	Jeu jeuTest;
//
//	@BeforeEach
//	void beforeEach(){
//		jeuTest = new Jeu();
//	}
//
//
//	private boolean isIn(Carte[] liste, Carte carte){
//		for (Carte value : liste) {
//			if (value.sontEquivalentes(carte)) {
//				return true;
//			}
//		}
//		return false;
//	}
//
//
//	@RepeatedTest(1000)
//	void testEchangerCarteMainContinuum(){
//		Random rand = new Random();
//		int randMain = rand.nextInt(3);
//		int randContinuum = rand.nextInt(9);
//
//		Carte carteMain = jeuTest.getInfoJoueurCourant().getCarteMain(randMain);
//		Carte carteContinuum = jeuTest.getContinuumCarte()[randContinuum];
//
//		jeuTest.echangerCarteMainContinuum(randMain, randContinuum);
//
//		assertEquals(carteMain, jeuTest.getContinuumCarte()[randContinuum]);
//		assertEquals(carteContinuum, jeuTest.getInfoJoueurCourant().getCarteMain(randMain));
//	}
//
//
//	@RepeatedTest(100)
//	void testCoupChangerPositionSorcier(){
//		int rand = new Random().nextInt(9);
//		if (jeuTest.coupChangerPositionSorcier(rand)){
//			assertEquals(rand, jeuTest.getInfoJoueurCourant().getSorcierIndice());
//		}else{
//			assertNotEquals(jeuTest.getContinuumCarte()[rand].getCouleur(), jeuTest.getCodex().getCouleurInterdite());
//		}
//	}
//
//
//	@RepeatedTest(1000)
//	void testCoupEchangeCarteMainContinuum(){
//		Random rand = new Random();
//		int randContinuum = rand.nextInt(9);
//		int randMain = rand.nextInt(3);
//
//		if (jeuTest.coupEchangeCarteMainContinuum(randMain, randContinuum)){
//			assertEquals(randContinuum, jeuTest.getInfoJoueurCourant().getSorcierIndice());
//		}else{
//			assertNotEquals(jeuTest.getContinuumCarte()[randContinuum].getCouleur(),
//					jeuTest.getMainJoueurCourant()[randMain].getCouleur());
//			assertNotEquals(jeuTest.getContinuumCarte()[randContinuum].getSymbole(),
//					jeuTest.getMainJoueurCourant()[randMain].getSymbole());
//		}
//	}
//
//
//	@RepeatedTest(1000)
//	void testCoupParadox(){
//		Random rand = new Random();
//		int dir;
//
//		jeuTest.getInfoJoueurCourant().setSorcierIndice(4);
//
//		if (rand.nextBoolean()){
//			dir = 1;
//			jeuTest.infoJoueurs[0].setDirectionMouvement(1);
//		}else{
//			dir = -1;
//			jeuTest.infoJoueurs[0].setDirectionMouvement(-1);
//		}
//
//		Carte[] cartes_continuum_avant = {
//				jeuTest.continuum.getCarteContinuum(jeuTest.getInfoJoueurCourant().getSorcierIndice() + (1 * dir)).clone(),
//				jeuTest.continuum.getCarteContinuum(jeuTest.getInfoJoueurCourant().getSorcierIndice() + (2 * dir)).clone(),
//				jeuTest.continuum.getCarteContinuum(jeuTest.getInfoJoueurCourant().getSorcierIndice() + (3 * dir)).clone()
//		};
//
//		Carte[] cartes_main_avant = jeuTest.getMainJoueurCourant().clone();
//		int points_joueur = jeuTest.getInfoJoueurCourant().getPoints();
//
//		if (!jeuTest.coupParadox(jeuTest.getInfoJoueurCourant().getDirectionMouvement())){
//			assertTrue(!jeuTest.getInfoJoueurCourant().existeParadox(jeuTest.getCodex().getCouleurInterdite()) ||
//					(!(dir == 1 && jeuTest.existeParadoxSuperieur() || dir == -1 && jeuTest.existeParadoxSuperieur())));
//			return;
//		}
//
//		Carte[] cartes_continuum_apres = {
//				jeuTest.continuum.getCarteContinuum(jeuTest.getInfoJoueurCourant().getSorcierIndice() + (1 * dir)).clone(),
//				jeuTest.continuum.getCarteContinuum(jeuTest.getInfoJoueurCourant().getSorcierIndice() + (2 * dir)).clone(),
//				jeuTest.continuum.getCarteContinuum(jeuTest.getInfoJoueurCourant().getSorcierIndice() + (3 * dir)).clone()
//		};
//
//		Carte[] cartes_main_apres = jeuTest.getMainJoueurCourant();
//
//		for (int i = 0; i < 3; i++) {
//			assertTrue(isIn(cartes_continuum_avant, cartes_main_apres[i]));
//			assertTrue(isIn(cartes_continuum_apres, cartes_main_avant[i]));
//		}
//		assertEquals(1, jeuTest.getInfoJoueurCourant().getPoints() - points_joueur);
//
//	}
//
//
//	@RepeatedTest(1000)
//	void testGagnantClash(){
//
//		Random rand = new Random();
//		Couleur couleur_interdite = jeuTest.getCodex().getCouleurInterdite();
//		int somme_main1 = jeuTest.getInfoJoueurs()[0].sommeMain(couleur_interdite);
//		int somme_main2 = jeuTest.getInfoJoueurs()[1].sommeMain(couleur_interdite);
//
//		int ans = jeuTest.gagnantClash();
//
//		if (ans == -1){
//			assertEquals(jeuTest.getInfoJoueurs()[0].sommeMain(couleur_interdite),
//					jeuTest.getInfoJoueurs()[1].sommeMain(couleur_interdite));
//		} else if (ans == 0) {
//			if (somme_main1 == somme_main2){
//				; //Comment tester ??
//			}else{
//				assertTrue(jeuTest.getInfoJoueurs()[0].sommeMain(couleur_interdite) >
//						jeuTest.getInfoJoueurs()[1].sommeMain(couleur_interdite));
//			}
//
//		} else if (ans == 1) {
//			if (somme_main1 == somme_main2){
//				; //Comment tester ??
//			}else{
//				assertTrue(jeuTest.getInfoJoueurs()[0].sommeMain(couleur_interdite) <
//						jeuTest.getInfoJoueurs()[1].sommeMain(couleur_interdite));
//			}
//		}
//	}
//}
