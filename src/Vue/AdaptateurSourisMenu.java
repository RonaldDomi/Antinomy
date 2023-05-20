package Vue;
/*
 * Sokoban - Encore une nouvelle version (à but pédagogique) du célèbre jeu
 * Copyright (C) 2018 Guillaume Huard
 *
 * Ce programme est libre, vous pouvez le redistribuer et/ou le
 * modifier selon les termes de la Licence Publique Générale GNU publiée par la
 * Free Software Foundation (version 2 ou bien toute autre version ultérieure
 * choisie par vous).
 *
 * Ce programme est distribué car potentiellement utile, mais SANS
 * AUCUNE GARANTIE, ni explicite ni implicite, y compris les garanties de
 * commercialisation ou d'adaptation dans un but spécifique. Reportez-vous à la
 * Licence Publique Générale GNU pour plus de détails.
 *
 * Vous devez avoir reçu une copie de la Licence Publique Générale
 * GNU en même temps que ce programme ; si ce n'est pas le cas, écrivez à la Free
 * Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307,
 * États-Unis.
 *
 * Contact:
 *          Guillaume.Huard@imag.fr
 *          Laboratoire LIG
 *          700 avenue centrale
 *          Domaine universitaire
 *          38401 Saint Martin d'Hères
 */

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AdaptateurSourisMenu extends MouseAdapter {
	MenuGraphique m;
	CollecteurEvenements control;

	AdaptateurSourisMenu(MenuGraphique menu, CollecteurEvenements c) {
		m = menu;
		control = c;
	}


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
				if ( e.getY() >= m.deb_bouton_nouvelle_partie_y && e.getY() < m.deb_bouton_nouvelle_partie_y + m.mesureHauteur){
					if ( e.getX() >= m.centre_largeur - m.taille_bouton/2 && e.getX() < m.centre_largeur + m.taille_bouton/2){
//						System.out.println("Bouton Nouvelle Partie clicked");
						control.nouvellePartie(m.choix_type[0], m.choix_type[1]);
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

	@Override
	public void mousePressed(MouseEvent e) {

		if (e.getY() >= m.taille_bouton_s/3 && e.getY() <= m.taille_bouton_s/3 + m.taille_bouton_s ) {
			//bouton
			if (e.getX() >= m.taille_bouton_s/3 && e.getX() <= m.taille_bouton_s/3 + m.taille_bouton_s ){
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
				if ( e.getY() >= m.deb_bouton_nouvelle_partie_y && e.getY() <= m.deb_bouton_nouvelle_partie_y + m.mesureHauteur){
					if ( e.getX() >= m.centre_largeur - m.taille_bouton/2 && e.getX() <= m.centre_largeur + m.taille_bouton/2)
//						System.out.println("Bouton Nouvelle Partie clicked");
					m.selectBouton(1);
					break;
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
