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
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;



/**
 * <P> Initialise un client, tente de se connecter à un serveur, reçoit un jeu le cas échéant. </P>
 * <P> Initialisé par défaut sur le port 6969, configurable dans {@link Configuration}. </P>
 * <P> Initialise également l'interface graphique et le thread de {@link Reception}. </P>
 */
public class Client {
	
	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.err.println("Usage : Client <host name>");
			System.exit(1);
		}
		
		String nom_host = args[0];
		
		Configuration.typeJoueur = 0;
		Jeu jeu = null;
		
		BufferedReader incoming = null;
		Socket client_socket=null;
		
		try{
			client_socket = new Socket(nom_host, Configuration.numeroPort);
			incoming = new BufferedReader(new InputStreamReader(client_socket.getInputStream()));
			
			//Aucune utilitée
			//PrintWriter outgoing = new PrintWriter(client_socket.getOutputStream(), true);
			//BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			System.out.println(InetAddress.getLocalHost().getHostName());
			
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
			jeu = new Jeu(incoming.readLine());
			jeu.historique = new Historique();
			jeu.historique.ajouterJeu(jeu);
			
		}catch (IOException ioException){
			System.err.println("Le serveur s'est déconnecté?? " + ioException.getMessage());
			client_socket.close();
			System.exit(1);
		} finally {
			CollecteurEvenements control = new ControleurMediateur(jeu);
			control.setTypeJoueur(Configuration.typeJoueur, 3);
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
