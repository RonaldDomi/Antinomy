package Vue;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
/**
 * <P> Adaptateur clavier pour le jeu, définit les keybindings suivants : </P>
 * <P></P>
 * <P> Flèche Gauche/Droite : Choisit une direction lors d'un paradox </P>
 * <P> I : Importe une partie </P>
 * <P> S : Exporte une partie </P>
 * <P> P : Pause le jeu</P>
 * <P> Q/A : Quite le jeu </P>
 * <P> Échap : Met le jeu en plein-écran </P>
 */
public class AdaptateurClavier extends KeyAdapter {
	CollecteurEvenements control;

	AdaptateurClavier(CollecteurEvenements c) {
		control = c;
	}

	@Override
	public void keyPressed(KeyEvent event) {
		switch (event.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				control.toucheClavier("Left");
				break;
			case KeyEvent.VK_RIGHT:
				control.toucheClavier("Right");
				break;
			case KeyEvent.VK_I:
				control.toucheClavier("Import");
				break;
			case KeyEvent.VK_S:
				control.toucheClavier("Save");
				break;
			case KeyEvent.VK_Q:
			case KeyEvent.VK_A:
				control.toucheClavier("Quit");
				break;
			case KeyEvent.VK_P:
				control.toucheClavier("Pause");
				break;
			case KeyEvent.VK_ESCAPE:
				control.toucheClavier("Full");
				break;
		}
	}
}
