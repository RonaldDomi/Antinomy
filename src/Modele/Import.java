package Modele;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Import {
    Scanner scanner;

    public Import(String pathname) {
        File importFile = new File(pathname);
        try {
            scanner = new Scanner(importFile);
        } catch (FileNotFoundException ignored) {
            System.err.println("Le fichier n'existe pas!");
        }

    }

    public Jeu lire_fichier() {
        Jeu jeu = new Jeu();

        int currentJeuIndex = Integer.parseInt(scanner.nextLine()) - 1;
        Historique historique = new Historique(scanner.nextLine());
        historique.jeuIndex = currentJeuIndex;

        jeu.historique = historique;
        jeu.infoJoueurs[0] = historique.listeJeu.get(historique.jeuIndex).infoJoueurs[0];
        jeu.infoJoueurs[1] = historique.listeJeu.get(historique.jeuIndex).infoJoueurs[1];
        jeu.continuum = historique.listeJeu.get(historique.jeuIndex).continuum;
        jeu.tour = historique.listeJeu.get(historique.jeuIndex).tour;
        jeu.codex = historique.listeJeu.get(historique.jeuIndex).codex;
        jeu.joueurGagnant = historique.listeJeu.get(historique.jeuIndex).joueurGagnant;
        jeu.joueurCourant = historique.listeJeu.get(historique.jeuIndex).joueurCourant;

        return jeu;
    }
}