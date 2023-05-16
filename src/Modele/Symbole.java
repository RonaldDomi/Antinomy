package Modele;

import java.util.Random;

/**
 * <P> Représentation du symbole sur les cartes. Chaque carte dispose d'un symbole. </P>
 * <P> Chaque symbole est représenté comme une constante. </P>
 */
public enum Symbole {
    CHAMPIGNON, CLEF, PAPIER, CRANE;
    private static final Symbole[] valeurs = values();
    private static final int taille = valeurs.length;
    private static final Random random = new Random();
    
    public static Symbole getRandomSymbole(){
        return valeurs[random.nextInt(taille)];
    }
    
    /**
     * <P>Convertit une chaîne de caractères en un symbole, par exemple "Clef" -> CLEF .</P>
     * @param symbole Chaîne de caractères à parse.
     * @return Le symbole correspondant à la chaîne de caractères.
     */
    public static Symbole parseSymbole(String symbole){
        Symbole my_symbole;
        try {
            my_symbole = Symbole.valueOf(symbole);
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid enum string: " + symbole);
            my_symbole = null;
        }
        return my_symbole;
    }
}
