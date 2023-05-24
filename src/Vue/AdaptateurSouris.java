package Vue;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Adaptateur de souris pour le plateau de Jeu
 */
public class AdaptateurSouris extends MouseAdapter {
	NiveauGraphique n;
	CollecteurEvenements control;

	AdaptateurSouris(NiveauGraphique niv, CollecteurEvenements c) {
		n = niv;
		control = c;
	}

	/**
	 * <P> Invoqué lorsqu'un clic de souris a été effectué. </P>
	 * <P> Vérifie si le clic est sur les coordonnées d'un objet cliquable. </P>
	 * <P> L'action associée à l'élément est effectuée directement. </P>
	 * <P> Le comportement est similaire au <I>Drag & Drop</I>. </P>
	 * @param e Un événement de souris
	 */
	@Override
	public void mouseReleased(MouseEvent e){
		if (e.getY() >= n.padding && e.getY() <= n.padding + n.taille_bouton ){
			//bouton
			for (int i = 0; i < 6; i++) {
				if (e.getX() >= n.deb_bouton + i*n.taille_bouton + i*n.padding && e.getX() < n.deb_bouton + (i+1)*n.taille_bouton + i*n.padding && n.selectBouton == i){
//					System.out.println("Bouton Clicked : " + i);
					control.clicSourisBouton(i);
					break;
				}
			}
		}  else if (e.getY() >= n.centre_hauteur - n.hauteurCarte/2 - (n.joueurCourant) * (n.largeurCarte + n.padding) && e.getY() <= n.centre_hauteur + n.hauteurCarte/2 + (1-n.joueurCourant) * (n.largeurCarte + n.padding)) {
			for (int i = 0; i < 9; i++) {
				if (e.getX() >= n.deb_continuum + i*n.largeurCarte + i*n.padding && e.getX() < n.deb_continuum + (i+1)*n.largeurCarte + i*n.padding){
//					System.out.println("Continium Clicked : " + i);
					control.clicSouris(2, i);
					break;
				}
			}
		}
		n.unselectBouton();
	}

	/**
	 * <P> Invoqué lorsqu'une détente de clic a été effectué. </P>
	 * <P> Vérifie si le clic est sur les coordonnées d'un objet cliquable. </P>
	 * <P> L'action associée à l'élément est effectuée directement. </P>
	 * <P> Le comportement est similaire au <I>Drag & Drop</I>. </P>
	 * @param e Un événement de souris
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getY() >= n.hauteur - n.hauteurCarte - n.padding && e.getY() <= n.hauteur - n.padding && n.joueurCourant == 0) {
			for (int i = 0; i < 3; i++) {
				if (e.getX() >= n.deb_joueur + i * n.largeurCarte + i * n.padding && e.getX() < n.deb_joueur + (i + 1) * n.largeurCarte + i * n.padding) {
//					System.out.println("Joueur0 Clicked : " + i);
					control.clicSouris(1, i);
					return;
				}
			}
		}
		if (e.getY() >= n.padding && e.getY() <= n.padding + n.hauteurCarte && n.joueurCourant == 1) {
			for (int i = 0; i < 3; i++) {
				if (e.getX() >= n.deb_joueur + i * n.largeurCarte + i * n.padding && e.getX() < n.deb_joueur + (i + 1) * n.largeurCarte + i * n.padding) {
//					System.out.println("Joueur1 Clicked : " + i);
					control.clicSouris(1, i);
					return;
				}
			}
		}
		if (e.getY() >= n.padding && e.getY() <= n.padding + n.taille_bouton ){
			//bouton
			
			for (int i = 0; i < 6; i++) {
				if (e.getX() >= n.deb_bouton + i*n.taille_bouton + i*n.padding && e.getX() < n.deb_bouton + (i+1)*n.taille_bouton + i*n.padding){
					n.selectBouton(i);
					//bouton
					return;
				}
			}
		}
	}
}
