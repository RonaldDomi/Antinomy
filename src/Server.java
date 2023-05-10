import java.io.*;
import java.net.*;

import Controleur.ControleurMediateur;
import Global.Configuration;
import Modele.*;
import Vue.CollecteurEvenements;
import Vue.InterfaceGraphique;

import java.util.ArrayList;

public class Server {
	
	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Usage : Server <port number>");
			System.exit(1);
		}
		
		int portNumber = Integer.parseInt(args[0]);
		
		
		try{
			
			Configuration.typeJoueur = 1;
			
			ServerSocket serverSocket = new ServerSocket(portNumber);
			Socket clientSocket = serverSocket.accept();
			
			PrintWriter outgoing = new PrintWriter(clientSocket.getOutputStream(), true);
			BufferedReader incoming = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
			Jeu jeu = new Jeu();
			CollecteurEvenements control = new ControleurMediateur(jeu);
			InterfaceGraphique.demarrer(jeu, control, clientSocket);
			
			
			
			outgoing.println(jeu);
//
//			while ((inputLine = incoming.readLine()) != null) {
//				System.out.println("Incoming!");
//				System.out.println("Message : " + inputLine);
//			}
		
		} catch (IOException e) {
			System.err.println("Erreur :" + e.getMessage());
			e.printStackTrace();
			
		} catch(Exception except){
			System.err.println("Autre erreur : " + except.getMessage());
			except.printStackTrace();
		}
	}
}
