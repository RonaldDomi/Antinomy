import Controleur.ControleurMediateur;
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
		PrintWriter outgoing = null;
		BufferedReader incoming = null;
		BufferedReader stdIn = null;
		Socket client_socket=null;
		
		try{
			client_socket = new Socket(nom_host, numero_port);
			
			outgoing = new PrintWriter(client_socket.getOutputStream(), true);
			incoming = new BufferedReader(new InputStreamReader(client_socket.getInputStream()));
			stdIn = new BufferedReader(new InputStreamReader(System.in));
			
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
		}catch (IOException ioException){
			System.err.println("Le serveur s'est déconnecté?? " + ioException.getMessage());
			client_socket.close();
			System.exit(1);
		}
		
		System.out.println("Client main thread : " + Thread.currentThread().getName());
		jeu.historique = new Historique();
		CollecteurEvenements control = new ControleurMediateur(jeu);
		InterfaceGraphique.demarrer(jeu, control, client_socket);
	}
}
