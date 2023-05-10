import Controleur.ControleurMediateur;
import Global.Configuration;
import Modele.Historique;
import Modele.Import;
import Modele.Jeu;
import Vue.CollecteurEvenements;
import Vue.InterfaceGraphique;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	
	public static void main(String[] args) {
		if (args.length != 2) {
			System.err.println("Usage : Client <host name> <port number>");
			System.exit(1);
		}
		
		String hostName = args[0];
		int portNumber = Integer.parseInt(args[1]);
		
		
		try{
			Configuration.typeJoueur = 0;
			
			Socket clientSocket = new Socket(hostName, portNumber);
			
			PrintWriter outgoing = new PrintWriter(clientSocket.getOutputStream(), true);
			BufferedReader incoming = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			
			Jeu jeu = new Jeu(incoming.readLine());
			jeu.historique = new Historique();
			CollecteurEvenements control = new ControleurMediateur(jeu);
			InterfaceGraphique.demarrer(jeu, control, clientSocket);

//			System.out.println("Carte : " + incoming.readLine());
//
//			String userInput;
//			while ((userInput = stdIn.readLine()) != null) {
//				outgoing.println(userInput);
//				System.out.println("echo : " + incoming.readLine());
//			}
		
		} catch (UnknownHostException e){
			System.err.println("Unknown host : " + e.getMessage());
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Erreur : " + e.getMessage());
			System.exit(1);
		}
		
	}
}
