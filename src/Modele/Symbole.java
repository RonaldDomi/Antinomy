package Modele;

import java.util.Random;

public enum Symbole {
    CHAMPIGNON, CLEF, PAPIER, CRANE;
    
    
    private static final Symbole[] valeurs = values();
    private static final int taille = valeurs.length;
    private static final Random random = new Random();
    
    public static Symbole getRandomSymbole(){
        return valeurs[random.nextInt(taille)];
    }

    public static Symbole parseSymbole(String symbole){
        Symbole mySymbole;
        try {
            mySymbole = Symbole.valueOf(symbole);
        } catch (IllegalArgumentException e) {
            // handle invalid input
            System.err.println("Invalid enum string: " + symbole);
            mySymbole = null; // or set a default value
        }
        return mySymbole;
    }
}
