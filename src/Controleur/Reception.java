package Controleur;

import Modele.Import;
import Modele.Jeu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * <P> Reception d'un message à travers un Socket </P>
 * <P> Code à exécuter pour un Thread de reception </P>*/
public class Reception implements Runnable{
	
	Jeu jeu;
	
	Socket clientSocket;
	
	public Reception(Jeu jeu, Socket clientSocket){
		this.jeu = jeu;
		this.clientSocket = clientSocket;
	}
	
	public void run(){
		System.out.println("En attente de l'autre machine...");
		
		try {
			BufferedReader incoming = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			String jeu_string;
			
			while ((jeu_string = incoming.readLine()) != null){
				
				switch(jeu_string){
					case "UNDO" :
						jeu.undo();
						break;
					case "REDO":
						jeu.redo();
						break;
					case "LOAD": //TODO: Fix
						String jeu_a_import = incoming.readLine();
						Import import_jeu = new Import(jeu_a_import, true);
						jeu = import_jeu.lire_fichier();
						break;
					default:
						jeu.modifierJeu(jeu_string);
						break;
				}
				jeu.metAJour();
				System.out.println("Fin de l'attente");
			}
		}catch (IOException exception){
			/* Jamais entré, la déconnexion de la machine distante entraine une valeur null à readLine() mais pas
			une IOException... */
			System.err.println("Connection reset by peer : " + exception.getMessage());
			System.exit(1);
		}
		
		/* Arrive seulement si la machine distante se déconnecte, on arrête la JVM dans ce cas */
		System.out.println("Bye bye reception !");
		System.exit(1);
	}
}
