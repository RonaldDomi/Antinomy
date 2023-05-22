package Reseau;

import Controleur.ControleurMediateur;
import Controleur.Reception;
import Global.Configuration;
import Modele.Jeu;
import Vue.CollecteurEvenements;
import Vue.InterfaceGraphique;

import javax.naming.ldap.Control;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * <P> Initialise un serveur, attend une connexion entrante et lui envoie le Jeu. </P>
 * <P> Initialisé par défaut sur le port 6969, configurable dans {@link Configuration}. </P>
 * <P> Initialise également l'interface graphique et le thread de {@link Reception}. </P>
 */
public class Server {
	
	public static Socket initServer(Jeu jeu, ControleurMediateur controleur) {

		Configuration.typeJoueur = 1;
		ServerSocket server_socket;
		Socket client_socket = null;
		PrintWriter outgoing = null;
		
		try{
			
			server_socket = new ServerSocket(Configuration.numeroPort);
			client_socket = server_socket.accept();
			controleur.enAttenteConnexion = false;
			outgoing = new PrintWriter(client_socket.getOutputStream(), true);
			
		}
		catch(SocketTimeoutException socketTimeoutException){
			System.err.println("Socket timeout exception : " + socketTimeoutException.getMessage());
			System.exit(1);
		}
		catch (IOException ioException){
			System.err.println("IOException :" + ioException.getMessage());
			System.exit(1);
		}
		catch(IllegalArgumentException illegalArgumentException){
			System.err.println("Illegal port number : " + illegalArgumentException.getMessage());
			System.exit(1);
		}
		
		//Envoie le jeu à l'autre machine distante
		outgoing.println(jeu);
		
		//Initialisation du thread de reçu
		//TODO: Le laisser Daemon?? Ne semble pas avoir de différence
		Reception reception = new Reception(jeu, client_socket);
		Thread threadReception = new Thread(reception, "==ReceptionServerThread==");
		threadReception.setDaemon(true);
		threadReception.start();
		
		
		if (Thread.activeCount() != 5){
			System.err.println("Le nombre de thread est incorrect");
			System.err.println("Il y en a " + Thread.activeCount() + " à la place");
		}else{
			System.out.println("Il y a 5 threads actifs");
		}

		controleur.ajouteSocket(client_socket);
		controleur.typeJoueur[1] = controleur.joueurEnLigne;
		controleur.getVue().setAffichage(1, -1);

		return client_socket;
	}
	
}

