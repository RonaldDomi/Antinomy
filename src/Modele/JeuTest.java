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
//		Carte carteMain = jeuTest.getInfoJoueurCourant().gsetCarteMain(randMain);
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
//}