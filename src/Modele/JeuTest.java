/*
package Modele;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class JeuTest {

	Jeu jeuTest;

	@BeforeEach
	void beforeEach(){
		jeuTest = new Jeu(null);
	}

	
	boolean isIn(Carte[] liste, Carte carte){
		for (int i = 0; i < liste.length; i++) {
			if(liste[i].sontEquivalentes(carte)){
				return true;
			}
		}
		return false;
	}
	

	@RepeatedTest(1000)
	void testEchangerCarteMainContinuum(){
		Random rand = new Random();
		int randMain = rand.nextInt(3);
		int randContinuum = rand.nextInt(9);

		Carte carteMain = jeuTest.getInfoJoueurCourant().getCarteMain(randMain);
		Carte carteContinuum = jeuTest.getContinuumCarte()[randContinuum];

		jeuTest.echangerCarteMainContinuum(randMain, randContinuum);

		assertEquals(carteMain, jeuTest.getContinuumCarte()[randContinuum]);
		assertEquals(carteContinuum, jeuTest.getInfoJoueurCourant().getCarteMain(randMain));
	}


	@RepeatedTest(100)
	void testCoupChangerPositionSorcier(){
		int rand = new Random().nextInt(9);
		jeuTest.coupChangerPositionSorcier(rand);

		assertEquals(rand, jeuTest.getInfoJoueurCourant().getSorcierIndice());
	}


	@RepeatedTest(100)
	void testCoupEchangeCarteMainContinuum(){
		Random rand = new Random();
		int randContinuum = rand.nextInt(9);
		int randMain = rand.nextInt(3);

		jeuTest.coupEchangeCarteMainContinuum(randMain, randContinuum);

		assertEquals(randContinuum, jeuTest.getInfoJoueurCourant().getSorcierIndice());
		testEchangerCarteMainContinuum();
	}


	@RepeatedTest(100)
	void testCoupParadox(){
		Random rand = new Random();
		int dir;

		jeuTest.getInfoJoueurCourant().setSorcierIndice(4);
		
		if (rand.nextBoolean()){
			dir = 1;
		}else{
			dir = -1;
		}

		Carte[] cartes_continuum_avant = {
			jeuTest.continuum.getCarteContinuum(jeuTest.getInfoJoueurCourant().getSorcierIndice() + (1 * dir)).deepCopy(),
			jeuTest.continuum.getCarteContinuum(jeuTest.getInfoJoueurCourant().getSorcierIndice() + (2 * dir)).deepCopy(),
			jeuTest.continuum.getCarteContinuum(jeuTest.getInfoJoueurCourant().getSorcierIndice() + (3 * dir)).deepCopy()
		};
		
		Carte[] cartes_main_avant = jeuTest.getMainJoueurCourant().clone();
		int points_joueur = jeuTest.getInfoJoueurCourant().getPoints();

		jeuTest.coupParadox(dir);
		
		Carte[] cartes_continuum_apres = {
				jeuTest.continuum.getCarteContinuum(jeuTest.getInfoJoueurCourant().getSorcierIndice() + (1 * dir)).deepCopy(),
				jeuTest.continuum.getCarteContinuum(jeuTest.getInfoJoueurCourant().getSorcierIndice() + (2 * dir)).deepCopy(),
				jeuTest.continuum.getCarteContinuum(jeuTest.getInfoJoueurCourant().getSorcierIndice() + (3 * dir)).deepCopy()
		};
		
		Carte[] cartes_main_apres = jeuTest.getMainJoueurCourant();
		
		for (int i = 0; i < 3; i++) {
			assertTrue(isIn(cartes_continuum_avant, cartes_main_apres[i]));
			assertTrue(isIn(cartes_continuum_apres, cartes_main_avant[i]));
		}
		assertEquals(1, jeuTest.getInfoJoueurCourant().getPoints() - points_joueur);
	}
	
	
	@RepeatedTest(100)
	void testGagnantClash(){

		Random rand = new Random();

		int somme_main1 = jeuTest.getInfoJoueurs()[0].sommeMain(Couleur.VERT);
		int somme_main2 = jeuTest.getInfoJoueurs()[1].sommeMain(Couleur.VERT);

		int ans = jeuTest.gagnantClash();

		if (ans == -1){
			assertEquals(jeuTest.getInfoJoueurs()[0].sommeMain(Couleur.VERT),
					jeuTest.getInfoJoueurs()[1].sommeMain(Couleur.VERT));
		} else if (ans == 0) {
			if (somme_main1 == somme_main2){
				; //Comment tester ??
			}else{
				assertTrue(jeuTest.getInfoJoueurs()[0].sommeMain(Couleur.VERT) >
						jeuTest.getInfoJoueurs()[1].sommeMain(Couleur.VERT));
			}

		} else if (ans == 1) {
			if (somme_main1 == somme_main2){
				; //Comment tester ??
			}else{
				assertTrue(jeuTest.getInfoJoueurs()[0].sommeMain(Couleur.VERT) <
						jeuTest.getInfoJoueurs()[1].sommeMain(Couleur.VERT));
			}
		}
	}
}*/
