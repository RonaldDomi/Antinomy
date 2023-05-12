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

import Global.Configuration;
import Modele.Export;
import Modele.Jeu;
import Modele.Import;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import static java.lang.Math.min;


// L'interface runnable déclare une méthode run
public class InterfaceGraphique implements Runnable, InterfaceUtilisateur {
	Jeu j;
	CollecteurEvenements control;
	JFrame menuPrincipale;
	JFrame enJeu;
	boolean maximized;
	NiveauGraphique niv;
	MenuGraphique menu;
	JFrame courant;

	static Font h1;
	static Font h2 = new Font("TimesRoman", Font.PLAIN, 15);
	Font h2MenuJeu;

	InterfaceGraphique(Jeu jeu, CollecteurEvenements c) {
		j = jeu;
		control = c;


		try {
			Font medievalFont = Font.createFont(Font.TRUETYPE_FONT, new File("res/Fonts/Medieval-English.ttf"));
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(medievalFont);
		} catch (FontFormatException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void demarrer(Jeu j, CollecteurEvenements c) {
		InterfaceGraphique vue = new InterfaceGraphique(j, c);
		c.ajouteInterfaceUtilisateur(vue);
		SwingUtilities.invokeLater(vue);
	}

	public void toggleFullscreen() {
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice device = env.getDefaultScreenDevice();
		if (maximized) {
			device.setFullScreenWindow(null);
			maximized = false;
		} else {
			device.setFullScreenWindow(courant);
			maximized = true;
		}
	}

	public void run() {

		menuPrincipale = new JFrame("Antinomy");
		enJeu = new JFrame("Antinomy");


		Image icon;
		InputStream in = Configuration.ouvre("Images/Diamant.png");
		Configuration.info("Chargement de l'image " + "Icon");
		try {
			icon = ImageIO.read(in);
			menuPrincipale.setIconImage(icon);
			enJeu.setIconImage(icon);
		} catch (Exception e) {
			Configuration.erreur("Impossible de charger l'image " + "Icon");
		}


		enJeu.setSize(1000, 600);
		menuPrincipale.setSize(1000, 600);
//		h2MenuJeu = new Font("Medieval English", Font.PLAIN, enJeu.getWidth()/10);

		menu = new MenuGraphique();
		menu.addMouseListener(new AdaptateurSourisMenu(menu, control));

		niv = new NiveauGraphique(j);
		niv.addMouseListener(new AdaptateurSouris(niv, control));

//		menuPrincipale.addKeyListener(new AdaptateurClavier(control));
		enJeu.addKeyListener(new AdaptateurClavier(control));

//		h1 = new Font("Medieval English", Font.PLAIN, menuPrincipale.getWidth()/2);

//		Box enJeuListe = Box.createVerticalBox();
//
//		Box menuJeu = Box.createHorizontalBox();
//		JLabel name = new JLabel("Antinomy");
//		name.setFont(h2MenuJeu);


//		menuJeu.add(name);

//		enJeuListe.add(menuJeu);
//		enJeuListe.add(niv);


		Timer chrono = new Timer(16, new AdaptateurTemps(control));
		chrono.start();

		enJeu.add(niv);

		menuPrincipale.add(menu);

		enJeu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		Color background_color = new Color(62, 129, 125, 255);
		Color background_color = new Color(100, 182, 176, 255);
		enJeu.getContentPane().setBackground(background_color);
		menuPrincipale.getContentPane().setBackground(background_color);
		menuPrincipale.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		menuPrincipale.setVisible(false);
//		menuPrincipale.setVisible(true);
		enJeu.setVisible(true);
//		enJeu.setVisible(false);
		courant = enJeu;
//		courant = menuPrincipale;
	}

	public void decale(int l, int c, double dl, double dc) {
//		niv.decale(l, c, dl, dc);
	}


	public void metAJourDirection(int dL, int dC) {

//		niv.metAJourDirection(dL, dC);
	}

	public void changeEtape() {
//		niv.changeEtape();
	}


	@Override
	public void selectionnerCarteMain(int index){
		niv.selectionnerCarteMain(index);
	}

	@Override
	public void selectionnerCarteContinuum(LinkedList<Integer> indices){
		niv.selectionnerCarteContinuum(indices);
	}

	@Override
	public void selectionnerParadox(int debParadoxInf, int finParadoxInf, int debParadoxSup, int finParadoxSup){
		niv.selectionnerParadox(debParadoxInf, finParadoxInf, debParadoxSup, finParadoxSup);
	}

	@Override
	public void sauvegarder(){
		JFileChooser Save = new JFileChooser();
		int r = Save.showSaveDialog(null);
		if (r == JFileChooser.APPROVE_OPTION) {
			System.out.println("Sauvegarder " + Save.getSelectedFile().getAbsolutePath());
			Export exp = new Export(Save.getSelectedFile().getAbsolutePath());
			exp.exporterJeu(j);
		}
		else{
			System.out.println("Sauvegarde annulée");
		}
	}
	@Override
	public void charger(){
		JFileChooser Load = new JFileChooser();
		int r = Load.showOpenDialog(null);
		if (r == JFileChooser.APPROVE_OPTION) {
			System.out.println("Charger " + Load.getSelectedFile().getAbsolutePath());
			Import imp = new Import(Load.getSelectedFile().getAbsolutePath());
			j.charger(imp.lire_fichier(), true);
		}
		else
			System.out.println("Chargement annulée");
	}

	@Override
	public void setAffichage(int i) {
		menu.setAffichage(i);
	}
}
