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
//class JeuTest {
//
//	Jeu jeuTest;
//
//	@BeforeEach
//	void beforeEach(){
//		jeuTest = new Jeu(null);
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
//		jeuTest.coupChangerPositionSorcier(rand);
//
//		assertEquals(rand, jeuTest.getInfoJoueurCourant().getSorcierIndice());
//	}
//
//
//	@RepeatedTest(100)
//	void testCoupEchangeCarteMainContinuum(){
//		Random rand = new Random();
//		int randContinuum = rand.nextInt(9);
//		int randMain = rand.nextInt(3);
//
//		jeuTest.coupEchangeCarteMainContinuum(randMain, randContinuum);
//
//		assertEquals(randContinuum, jeuTest.getInfoJoueurCourant().getSorcierIndice());
//		testEchangerCarteMainContinuum();
//	}
//
//
//	@RepeatedTest(100)
//	void testCoupParadox(){
//		Random rand = new Random();
//		int dir;
//
//		if (rand.nextBoolean()){
//			dir = 1;
//		}else{
//			dir = -1;
//		}
//
//		Carte[] cartes_continuum_avant =
//				{jeuTest.continuum.getCarteContinuum(jeuTest.getInfoJoueurCourant().getSorcierIndice()),
//				jeuTest.continuum.getCarteContinuum(jeuTest.getInfoJoueurCourant().getSorcierIndice()+1),
//				jeuTest.continuum.getCarteContinuum(jeuTest.getInfoJoueurCourant().getSorcierIndice()+2)};
//		Carte[] cartes_main_avant = jeuTest.getMainJoueurCourant();
//
//		jeuTest.coupParadox(true, dir);
//
//	}
//}