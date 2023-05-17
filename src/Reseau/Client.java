package Reseau;

import Controleur.Reception;
import Global.Configuration;
import Modele.Historique;
import Modele.Jeu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;



/**
 * <P> Initialise un client, tente de se connecter à un serveur, reçoit un jeu le cas échéant. </P>
 * <P> Initialisé par défaut sur le port 6969, configurable dans {@link Configuration}. </P>
 * <P> Initialise également l'interface graphique et le thread de {@link Reception}. </P>
 */
public class Client {
	
	public static Socket initClient(String nom_host, Jeu jeu){
		
		Configuration.typeJoueur = 0;
		
		BufferedReader incoming = null;
		Socket client_socket=null;
		
		try{
			client_socket = new Socket(nom_host, Configuration.numeroPort);
			incoming = new BufferedReader(new InputStreamReader(client_socket.getInputStream()));
			
			
		} catch (UnknownHostException unknownHostException){
			System.err.println("Unknown host : " + unknownHostException.getMessage());
			System.exit(1);
		} catch (IOException ioException) {
			System.err.println("IOException : " + ioException.getMessage());
			System.exit(1);
		} catch (IllegalArgumentException illegalArgumentException){
			System.err.println("Illegal port number : " + illegalArgumentException.getMessage());
			System.exit(1);
		}
		
		try{
			jeu.modifierJeu(incoming.readLine());
			jeu.historique = new Historique();
			jeu.historique.ajouterJeu(jeu);
			
		}catch (IOException ioException){
			System.err.println("Le serveur s'est déconnecté?? " + ioException.getMessage());
			System.exit(1);
		}
		
		//Initialisation du thread de reçu
		//TODO: Le laisser Daemon?? Ne semble pas avoir de différence
		Reception reception = new Reception(jeu, client_socket);
		Thread threadReception = new Thread(reception, "==ReceptionClientThread==");
		threadReception.setDaemon(true);
		threadReception.start();
		
		if (Thread.activeCount() != 4){
			System.err.println("Le nombre de thread est incorrect");
		}else{
			System.out.println("Il y a 4 threads actifs");
		}
		
		return client_socket;
	}
}
