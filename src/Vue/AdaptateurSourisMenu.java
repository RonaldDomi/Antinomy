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
		switch (m.affichage) {
			case 1:
				if (e.getX() >= m.centre_largeur - m.taille_bouton / 2 && e.getX() <= m.centre_largeur + m.taille_bouton / 2) {
					//bouton
					for (int i = 0; i < 5; i++) {
						if (e.getY() >= m.deb_bouton + i * m.mesureLargeur + i * m.padding && e.getY() < m.deb_bouton + (i + 1) * m.mesureLargeur + i * m.padding) {
							System.out.println("Bouton Clicked : " + i);
							control.clicSourisBoutonMenu(i);
							break;
						}
					}
				}
				break;
			case 2:

				break;
		}
		m.unselectBouton();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		switch (m.affichage){
			case 1:
				if (e.getX() >= m.centre_largeur - m.taille_bouton/2 && e.getX() <= m.centre_largeur + m.taille_bouton/2 ){
				//bouton
					for (int i = 0; i < 5; i++) {
						if (e.getY() >= m.deb_bouton + i*m.mesureLargeur + i*m.padding && e.getY() < m.deb_bouton + (i+1)*m.mesureLargeur + i*m.padding){
							System.out.println("Bouton Clicked : " + i);
							m.selectBouton(i);
							return;
						}
					}
				}
				break;
			case 2:
				break;
		}
	}
}
