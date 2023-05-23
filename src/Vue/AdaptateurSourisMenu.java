package Vue;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Adaptateur de souris pour le menu principal
 */
public class AdaptateurSourisMenu extends MouseAdapter {
	MenuGraphique m;
	CollecteurEvenements control;

	AdaptateurSourisMenu(MenuGraphique menu, CollecteurEvenements c) {
		m = menu;
		control = c;
	}


	/**
	 * <P> Invoqué lorsqu'un clic de souris a été effectué </P>
	 * <P> Suivant le menu, vérifie si le clic est sur les coordonnées d'un bouton </P>
	 * <P> Le cas échant, noircit le bouton</P>
	 * <P> L'action associée au bouton n'est pas effectuée si le curseur n'est pas sur le bouton au moment de la détente du clic </P>
	 * @param e Un événement de souris
	 */
	@Override
	public void mouseReleased(MouseEvent e){
		if (e.getY() >= m.taille_bouton_s/3 && e.getY() <= m.taille_bouton_s/3 + m.taille_bouton_s ) {
			//bouton
			if (e.getX() >= m.taille_bouton_s/3 && e.getX() <= m.taille_bouton_s/3 + m.taille_bouton_s ){
				control.clicSourisBoutonMenu(m.affichage, -2);
			}
		}

		switch (m.affichage) {
			case 1:
				if (e.getX() >= m.centre_largeur - m.taille_bouton / 2 && e.getX() < m.centre_largeur + m.taille_bouton / 2) {
					//bouton
					for (int i = 0; i < 3; i++) {
						if (e.getY() >= m.deb_bouton_y + i * m.mesureLargeur + i * m.padding && e.getY() < m.deb_bouton_y + (i + 1) * m.mesureLargeur + i * m.padding) {
//							System.out.println("Bouton Clicked : " + i);
							control.clicSourisBoutonMenu(m.affichage, i);
							break;
						}
					}
				}
				m.unselectBouton();
				break;
			case 2:
				if (m.parent.typeMenuSelectionJoueur == 1){
					if ( e.getY() >= m.deb_bouton_nouvelle_partie_y && e.getY() < m.deb_bouton_nouvelle_partie_y + m.mesureHauteur){
						if (e.getX() >= m.centre_largeur - m.taille_bouton && e.getX() <= m.centre_largeur + m.taille_bouton && m.selectBouton == 1)
						{
	//						System.out.println("Bouton Accepter clicked");
							control.nouvellePartie(m.choix_type[0], m.choix_type[1]);
						}
					}
				}
				else {
					if ( e.getY() >= m.deb_bouton_nouvelle_partie_y && e.getY() < m.deb_bouton_nouvelle_partie_y + m.mesureHauteur){
						if (e.getX() >= m.deb_bouton_nouvelle_partie_x && e.getX() <= m.deb_bouton_nouvelle_partie_x + m.taille_bouton*2 && m.selectBouton == 1)
						{
//							System.out.println("Bouton Charger clicked");
							control.charger(m.choix_type[0], m.choix_type[1]);
						} else if (e.getX() >= m.deb_bouton_nouvelle_partie_x + m.taille_bouton*2 + m.padding && e.getX() <= m.deb_bouton_nouvelle_partie_x + m.taille_bouton*2 + m.padding + m.taille_bouton*2&& m.selectBouton == 2){
//							System.out.println("Bouton nouvelle Partie clicked");
							control.nouvellePartie(m.choix_type[0], m.choix_type[1]);
						}
					}
				}

				m.unselectBouton();
				break;
			case 3:
				if ( e.getY() >= m.deb_bouton_y - m.mesureHauteur/2 && e.getY() < m.deb_bouton_y + m.mesureHauteur/2){
//					System.out.println("clicked");
					for (int i = 0; i < 2; i++) {
						if (e.getX() >= m.deb_bouton_x + i * m.centre_largeur && e.getX() < m.deb_bouton_x + i * m.centre_largeur + m.taille_bouton) {
//							System.out.println("clicked " + i);
							control.clicSourisBoutonMenu(m.affichage, i);
						}
					}
				}
				m.unselectBouton();
				break;
		}
	}

	/**
	 * <P> Invoqué lorsqu'une détente de clic a été effectuée </P>
	 * <P> Suivant le menu, vérifie si la détente est sur les coordonnées d'un bouton </P>
	 * <P> Enlève le noircissement le bouton et effectue l'action associée au bouton</P>
	 * @param e Un événement de souris
	 */
	@Override
	public void mousePressed(MouseEvent e) {

		if (e.getY() >= m.taille_bouton_s/3 && e.getY() <= m.taille_bouton_s/3 + m.taille_bouton_s ) {
			//bouton
			if (e.getX() >= m.taille_bouton_s/3 && e.getX() <= m.taille_bouton_s/3 + m.taille_bouton_s && m.parent.typeMenuSelectionJoueur == 0){
				m.selectBoutonExit(true);
			}
		}

		switch (m.affichage){
			case 1:
				if (e.getX() >= m.centre_largeur - m.taille_bouton/2 && e.getX() <= m.centre_largeur + m.taille_bouton/2 ){
				//bouton
					for (int i = 0; i < 3; i++) {
						if (e.getY() >= m.deb_bouton_y + i*m.mesureLargeur + i*m.padding && e.getY() < m.deb_bouton_y + (i+1)*m.mesureLargeur + i*m.padding){
							m.selectBouton(i);
							return;
						}
					}
				}
				break;
			case 2:
				if (m.parent.typeMenuSelectionJoueur == 1){
					if ( e.getY() >= m.deb_bouton_nouvelle_partie_y && e.getY() < m.deb_bouton_nouvelle_partie_y + m.mesureHauteur){
						if (e.getX() >= m.centre_largeur - m.taille_bouton && e.getX() <= m.centre_largeur + m.taille_bouton)
						{
							//						System.out.println("Bouton Accepter clicked");
							m.selectBouton(1);
						}
					}
				}
				else {
					if ( e.getY() >= m.deb_bouton_nouvelle_partie_y && e.getY() < m.deb_bouton_nouvelle_partie_y + m.mesureHauteur){
						if (e.getX() >= m.deb_bouton_nouvelle_partie_x && e.getX() <= m.deb_bouton_nouvelle_partie_x + m.taille_bouton*2)
						{
							m.selectBouton(1);
						} else if (e.getX() >= m.deb_bouton_nouvelle_partie_x + m.taille_bouton*2 + m.padding && e.getX() <= m.deb_bouton_nouvelle_partie_x + m.taille_bouton*2 + m.padding + m.taille_bouton*2){
							m.selectBouton(2);
						}
					}
				}

				for (int j = 0; j < 2; j++) {
					if (e.getY() >= m.deb_bouton_y + j*m.mesureHauteur + j*m.padding && e.getY() < m.deb_bouton_y + (j+1)*m.mesureHauteur + j*m.padding){
						//bouton
//						System.out.println("click Joueur : "+j);
						for (int i = 0; i < m.type_possible.length; i++) {
//							x = centre_largeur + (i-3) * (taille_bouton+padding) + (taille_bouton + padding)/2;
							if (e.getX() >= m.deb_bouton_x + i*m.taille_bouton + i*m.padding && e.getX() < m.deb_bouton_x + (i+1)*m.taille_bouton + i*m.padding){
								m.selectBoutonChoixJoueur(j, i);
//								System.out.println("Bouton clicked : "+i);
							}
						}
					}
				}
				break;
			case 3:
				if ( e.getY() >= m.deb_bouton_y - m.mesureHauteur/2 && e.getY() < m.deb_bouton_y + m.mesureHauteur/2){
//					System.out.println("clicked");
					for (int i = 0; i < 2; i++) {
						if (e.getX() >= m.deb_bouton_x + i * m.centre_largeur && e.getX() < m.deb_bouton_x + i * m.centre_largeur + m.taille_bouton) {
//							System.out.println("clicked " + i);
							m.selectBouton(i);
							return;
						}
					}
				}
				break;
		}
	}
}
