package Modele;

import java.util.Random;

/**
 * Représentation de la couleur sur les cartes, chaque carte dispose d'une couleur.
 * Chaque couleur est représentée comme une constante.
 */
public enum Couleur {
    ROUGE, BLEU, VIOLET, VERT;
    
    private static final Couleur[] valeurs = values();
    private static final int taille = valeurs.length;
    private static final Random random = new Random();
    
    /**
     * Renvoie une couleur aléatoire.
     *
     * @return Une couleur aléatoire symbolisée par une constante.
     */
    public static Couleur getRandomCouleur(){
        return valeurs[random.nextInt(taille)];
    }
    
    /**
     * <P>Convertit une chaîne de caractères en une couleur, par exemple "Rouge" -> ROUGE .</P>
     * @param couleur Chaîne de caractères à parse.
     * @return La couleur correspondant à la chaîne de caractères.
     */
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

