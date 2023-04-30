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
}
