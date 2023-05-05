//
//package Modele;
//
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//class CodexTest {
//
//	@Test
//	void testCycleCouleur(){
//		Codex codex = new Codex(new Carte(1, Couleur.VERT, Symbole.CRANE), Couleur.VERT);
//		for (int i = 0; i < 10; i++) {
//				codex.cycleCouleur();
//				assertEquals(Couleur.ROUGE, codex.getCouleurInterdite());
//				codex.cycleCouleur();
//				assertEquals(Couleur.BLEU, codex.getCouleurInterdite());
//				codex.cycleCouleur();
//				assertEquals(Couleur.VIOLET, codex.getCouleurInterdite());
//				codex.cycleCouleur();
//				assertEquals(Couleur.VERT, codex.getCouleurInterdite());
//		}
//	}
//}
