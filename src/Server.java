import Controleur.ControleurMediateur;
import Controleur.Reception;
import Global.Configuration;
import Modele.Jeu;
import Vue.CollecteurEvenements;
import Vue.InterfaceGraphique;

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
	
	public static void main(String[] args) {
		
		Configuration.typeJoueur = 1;
		ServerSocket server_socket;
		Socket client_socket = null;
		PrintWriter outgoing = null;
		Jeu jeu = new Jeu();
		
		try{
			
			server_socket = new ServerSocket(Configuration.numeroPort);
			client_socket = server_socket.accept();
			outgoing = new PrintWriter(client_socket.getOutputStream(), true);
			
			//Aucune utilitée
			//BufferedReader incoming = null;
			//incoming = new BufferedReader(new InputStreamReader(client_socket.getInputStream()));
			
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
		} finally {
			CollecteurEvenements control = new ControleurMediateur(jeu);
			
			control.setTypeJoueur(Configuration.typeJoueur, 3);
			InterfaceGraphique.demarrer(jeu, control, client_socket);
		}
		
		//Envoie le jeu à l'autre machine distante
		outgoing.println(jeu);
		
		//Initialisation du thread de reçu
		//TODO: Le laisser Daemon?? Ne semble pas avoir de différence
		Reception reception = new Reception(jeu, client_socket);
		Thread threadReception = new Thread(reception, "==ReceptionServerThread==");
		threadReception.setDaemon(true);
		threadReception.start();
		
		
//		Thread dummyThread = new Thread(new DumbWork());
//		dummyThread.start();
		if (Thread.activeCount() != 4){
			System.err.println("Le nombre de thread est incorrect");
		}else{
			System.out.println("Il y a 4 threads actifs");
		}
	}
	
}

