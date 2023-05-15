import Controleur.ControleurMediateur;
import Controleur.Reception;
import Global.Configuration;
import Modele.Historique;
import Modele.Jeu;
import Vue.CollecteurEvenements;
import Vue.InterfaceGraphique;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	
	public static void main(String[] args) throws IOException {
		if (args.length != 2) {
			System.err.println("Usage : Client <host name> <port number>");
			System.exit(1);
		}
		
		String nom_host = args[0];
		int numero_port = Integer.parseInt(args[1]);
		
		Configuration.typeJoueur = 0;
		Jeu jeu = null;
		
		BufferedReader incoming = null;
		Socket client_socket=null;
		
		try{
			client_socket = new Socket(nom_host, numero_port);
			incoming = new BufferedReader(new InputStreamReader(client_socket.getInputStream()));
			
			//Aucune utilitée
			PrintWriter outgoing = new PrintWriter(client_socket.getOutputStream(), true);
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			
		} catch (UnknownHostException unknownHostException){
			System.err.println("Unknown host : " + unknownHostException.getMessage());
			System.exit(1);
		} catch (IOException ioException) {
			System.err.println("IOException : " + ioException.getMessage());
			System.exit(1);
		} catch (IllegalArgumentException illegalArgumentException){
			System.err.println("illegal port number : " + illegalArgumentException.getMessage());
			System.exit(1);
		}
		
		
		try{
			jeu = new Jeu(incoming.readLine());
			jeu.historique = new Historique();
			jeu.historique.ajouterJeu(jeu);
			
		}catch (IOException ioException){
			System.err.println("Le serveur s'est déconnecté?? " + ioException.getMessage());
			client_socket.close();
			System.exit(1);
		} finally {
			CollecteurEvenements control = new ControleurMediateur(jeu);
			InterfaceGraphique.demarrer(jeu, control, client_socket);
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
	}
}
