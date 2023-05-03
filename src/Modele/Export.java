package Modele;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

public class Export {
    File exportFile;
    FileWriter writer;

    public Export(String pathname){
        try {
            exportFile = new File(pathname);
            writer = new FileWriter(exportFile);
            exportFile.createNewFile();
        }catch(FileAlreadyExistsException ignored){
        }catch(IOException ioException){
            System.err.println("Impossible de créer un fichier !");
        }
    }


    public void exporterJeu(Jeu jeu) {
        exportHistorique(jeu.historique);
    }


    void exportHistorique(Historique historique){
        ecrireLigneNewLine(historique.toString());
    }

    void ecrireLigneNewLine(String string){
        System.out.println(string);
        try{
            writer.write(string + "\n");
            writer.flush();
        }catch (IOException ioException){
            System.err.println("IOException sur l'écriture de la sauvegarde ! ");
        }
    }


}