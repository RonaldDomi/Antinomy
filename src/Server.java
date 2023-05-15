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

public class Server {
	
	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Usage : Server <port number>");
			System.exit(1);
		}
		
		int portNumber = Integer.parseInt(args[0]);
		
		Configuration.typeJoueur = 1;
		ServerSocket server_socket;
		Socket client_socket = null;
		PrintWriter outgoing = null;
		Jeu jeu = new Jeu();
		
		try{
			
			server_socket = new ServerSocket(portNumber);
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

