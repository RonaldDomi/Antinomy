import Controleur.ControleurMediateur;
import Global.Configuration;
import Modele.Jeu;
import Vue.CollecteurEvenements;
import Vue.InterfaceGraphique;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
		ServerSocket server_socket = null ;
		Socket client_socket = null;
		PrintWriter outgoing = null;
		BufferedReader incoming = null;
		try{
			
			
			
			server_socket = new ServerSocket(portNumber);
			
			client_socket = server_socket.accept();
			
			outgoing = new PrintWriter(client_socket.getOutputStream(), true);
			incoming = new BufferedReader(new InputStreamReader(client_socket.getInputStream()));
			
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
		
		Jeu jeu = new Jeu();
		CollecteurEvenements control = new ControleurMediateur(jeu);
		InterfaceGraphique.demarrer(jeu, control, client_socket);
		
		outgoing.println(jeu);
	}
	
}

