package Controleur;

import Modele.Jeu;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Définit un joueur comme Joueur en ligne (donc humain)
 */
class JoueurEnLigne extends Joueur {
    
    Socket clientSocket;
    
    JoueurEnLigne(int n, Jeu p) {
        super(n, p);
    }
    
    @Override
    void ajouteSocket(Socket clientSocket){
        this.clientSocket = clientSocket;
    }

    @Override
    int getEtape(){
        return jeu.getEtape();
    }
    
    /**
     * <P> Envoie le jeu à travers la Socket. </P>
     * <P> Si l'envoi échoue, ferme le jeu. </P>
     */
    @Override
    void envoyerJeu(){
        try{
            PrintWriter outgoing = new PrintWriter(clientSocket.getOutputStream(), true);
            outgoing.println(jeu);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    @Override
    boolean tempsEcoule() {
        return true;
    }
}