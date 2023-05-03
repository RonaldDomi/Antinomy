package Modele;

import java.util.Random;

public enum Couleur {
    ROUGE, BLEU, VIOLET, VERT;
    
    private static final Couleur[] valeurs = values();
    private static final int taille = valeurs.length;
    private static final Random random = new Random();
    
    public static Couleur getRandomCouleur(){
        return valeurs[random.nextInt(taille)];
    }

    public static Couleur parseCouleur(String couleur){
        Couleur myCouleur;
        try {
            myCouleur = Couleur.valueOf(couleur);
        } catch (IllegalArgumentException e) {
            // handle invalid input
            System.err.println("Invalid enum string: " + couleur);
            myCouleur = null; // or set a default value
        }
        return myCouleur;
    }
}

