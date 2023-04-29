package Modele;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContinuumBiasedTest {
	
	Continuum continuum;
	
	@BeforeEach
	void biasContinuum(){
		continuum.setCarteContinuum(0, new Carte(1, Couleur.VIOLET, Symbole.CHAMPIGNON));
		continuum.setCarteContinuum(1, new Carte(1, Couleur.VERT, Symbole.PAPIER));
		continuum.setCarteContinuum(2, new Carte(1, Couleur.ROUGE, Symbole.CLEF));
		continuum.setCarteContinuum(3, new Carte(1, Couleur.BLEU, Symbole.CRANE));
		continuum.setCarteContinuum(4, new Carte(2, Couleur.BLEU, Symbole.CHAMPIGNON));
		continuum.setCarteContinuum(5, new Carte(3, Couleur.VIOLET, Symbole.CLEF));
		continuum.setCarteContinuum(6, new Carte(4, Couleur.ROUGE, Symbole.PAPIER));
		continuum.setCarteContinuum(7, new Carte(3, Couleur.VERT, Symbole.CRANE));
		continuum.setCarteContinuum(8, new Carte(4, Couleur.VIOLET, Symbole.CRANE));
	}
	
	
}