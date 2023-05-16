package Modele;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

/**
 * <P> Exporte la partie dans un fichier. </P>
 * <P> On exporte que l'historique, on peut tout reconstruire à partir de celui-çi. </P>
 */
public class Export {
    File exportFile;
    FileWriter writer;
    
    /**
     * Créé un nouveau fichier qui contiendra l'historique.
     *
     * @param pathname Chemin absolu vers le fichier.
     */
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


    /**
     * <P> Exporte la partie vers le fichier ouvert. </P>
     *
     * @param jeu État du plateau à exporter.
     * */
    public void exporterJeu(Jeu jeu) {
        exportHistorique(jeu.historique);
    }
    
    
    /**
     * <P> Exporte l'historique vers le fichier ouvert. </P>
     *
     * @param historique Historique à exporter.
     * */
    void exportHistorique(Historique historique){
        ecrireLigneNewLine(historique.toString());
    }

    /**
     * <P> Écrit une ligne vers le fichier, flush le stream. </P>
     * <P> Ajoute aussi une newline. </P>
     *
     * @param string Chaîne de caractères à écrire dans le fichier.
     * */
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