package Modele;

import java.io.*;
import java.net.Socket;
import java.util.Random;
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
    
    public Import(Socket clientSocket){
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            
            System.out.println("En attente de l'autre machine...");
            this.scanner = new Scanner(in.readLine());
            
            System.out.println("Hors attente");
        }catch (IOException ioException){
            System.err.println("IOException : " + ioException.getMessage());
            ioException.printStackTrace();
        }
    }

    public Jeu lire_fichier() {
        Jeu jeu = new Jeu();
        System.out.println("Dans lire fichier");
        int currentJeuIndex = Integer.parseInt(scanner.nextLine());
        Historique historique = new Historique(scanner.nextLine());
        historique.courrant = currentJeuIndex;
        
        jeu.historique = historique;
        jeu.r = new Random();
        jeu.infoJoueurs[0] = historique.listeJeu.get(historique.courrant).infoJoueurs[0];
        jeu.infoJoueurs[0].r = jeu.r;
        jeu.infoJoueurs[1] = historique.listeJeu.get(historique.courrant).infoJoueurs[1];
        jeu.infoJoueurs[1].r = jeu.r;
        jeu.continuum = historique.listeJeu.get(historique.courrant).continuum;
        jeu.tour = historique.listeJeu.get(historique.courrant).tour;
        jeu.codex = historique.listeJeu.get(historique.courrant).codex;
        jeu.joueurGagnant = historique.listeJeu.get(historique.courrant).joueurGagnant;
        jeu.joueurCourant = historique.listeJeu.get(historique.courrant).joueurCourant;
        jeu.etape = historique.listeJeu.get(historique.courrant).etape;
        
        return jeu;
    }
}