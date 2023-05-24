//
//package Modele;
//
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///** <P> Teste le bon fonctionnement de la classe {@link Codex}. </P>
// * Teste si le cycle des couleurs est correct.
// */
//class CodexTest {
//
//	@Test
//	void testCycleCouleur(){
//		Codex codex = new Codex(new Carte(1, Couleur.VERT, Symbole.CRANE), Couleur.VERT);
//		for (int i = 0; i < 10; i++) {
//				codex.cycleCouleur();
//				assertEquals(Couleur.VIOLET, codex.getCouleurInterdite());
//				codex.cycleCouleur();
//				assertEquals(Couleur.BLEU, codex.getCouleurInterdite());
//				codex.cycleCouleur();
//				assertEquals(Couleur.ROUGE, codex.getCouleurInterdite());
//				codex.cycleCouleur();
//				assertEquals(Couleur.VERT, codex.getCouleurInterdite());
//		}
//	}
//}
