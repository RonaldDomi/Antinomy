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

import Modele.Jeu;
import Vue.InterfaceUtilisateur;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

// Classe commune à tous les joueurs : IA ou humain
// L'idée est que, en ayant la même interface, tous les joueurs sont traités de la même
// manière par le moteur de jeu. C'est plus simple et permet toutes les combinaisons.
//
// Tous les joueurs ont donc potentiellement la possibilité de :
// - provoquer une temporisation (utilisé dans une IA)
// - tenir compte d'une temporisation écoulée (utilisé dans une IA)
// - tenir compte d'un coup joué à la souris (utilisé par un joueur humain)
abstract class Joueur {
    Jeu jeu;
    int num;

    InterfaceUtilisateur vue;
    Socket clientSocket;
    
    // Le joueur connait son numéro, cela lui permet d'inspecter le plateau en
    // sachant
    // repérer ses pions et évaluer où il en est
    Joueur(int n, Jeu j) {
        num = n;
        jeu = j;
    }

    void ajouteInterfaceUtilisateur(InterfaceUtilisateur vue){
        this.vue = vue;
    }
    
    void ajouteSocket(Socket clientSocket){
        this.clientSocket = clientSocket;
    }
    
    void envoyerJeu(){
        try{
            PrintWriter outgoing = new PrintWriter(clientSocket.getOutputStream(), true);
            outgoing.println(jeu);
            outgoing.close();
        }catch(IOException e){
            System.err.println("Connection reset by peer??");
            System.exit(1);
        }
    }
    
    // Méthode appelée pour tous les joueurs une fois le temps écoulé
    // Si un joueur n'est pas concerné, il lui suffit de l'ignorer
    boolean tempsEcoule() {
        System.out.println("Il y a " + Thread.activeCount() + " threads actifs ");
        System.out.println(Thread.currentThread().getName());
        return false;
    }

    // Méthode appelée pour tous les joueurs lors d'un clic sur le plateau
    // Si un joueur n'est pas concerné, il lui suffit de l'ignorer
    boolean jeu(int i, int j) {
        return false;
    }

    void afficherPreSelection(){
        ;
    }

    int getEtape(){
        return -1;
    }
}