package Controleur;
/*
 * Morpion pédagogique
 * Copyright (C) 2016 Guillaume Huard

 * Ce programme est libre, vous pouvez le redistribuer et/ou le
 * modifier selon les termes de la Licence Publique Générale GNU publiée par la
 * Free Software Foundation (version 2 ou bien toute autre version ultérieure
 * choisie par vous).

 * Ce programme est distribué car potentiellement utile, mais SANS
 * AUCUNE GARANTIE, ni explicite ni implicite, y compris les garanties de
 * commercialisation ou d'adaptation dans un but spécifique. Reportez-vous à la
 * Licence Publique Générale GNU pour plus de détails.

 * Vous devez avoir reçu une copie de la Licence Publique Générale
 * GNU en même temps que ce programme ; si ce n'est pas le cas, écrivez à la Free
 * Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307,
 * États-Unis.

 * Contact: Guillaume.Huard@imag.fr
 *          Laboratoire LIG
 *          700 avenue centrale
 *          Domaine universitaire
 *          38401 Saint Martin d'Hères
 */

import Modele.Historique;
import Modele.Jeu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


class JoueurEnLigne extends Joueur {

    int indexCarteMain = -1;
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
    
    @Override
    void envoyerJeu(){
        try{
            PrintWriter outgoing = new PrintWriter(clientSocket.getOutputStream(), true);
            outgoing.println(jeu);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    @Override
    boolean tempsEcoule() {
        System.out.println("En attente de l'autre machine...");
        
        try {
            System.out.println("Client tempsEcoule thread : " + Thread.currentThread().getName());
            BufferedReader incoming = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String jeu_string = incoming.readLine();
            
            if (jeu_string == null){
                System.err.println("Connection reset by peer??");
                System.exit(1);
            }
            
            jeu.modifierJeu(jeu_string);
            
        }catch (IOException ioException){
            System.err.println("IOException : " + ioException.getMessage());
            System.err.println("Connection reset by peer??");
            System.exit(1);
        }
        
        System.out.println("Fin de l'attente");
        jeu.historique = new Historique(); //Pour l'instant, on ne gère pas d'historique
        jeu.metAJour();
        
        return true;
    }
}