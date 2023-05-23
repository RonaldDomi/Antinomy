package Modele;

/** <P> Un coup est symbolisé par deux cartes représentées par leur index. </P>
 * <P> Ainsi que d'un entier représentant si un Paradox est fait et dans quel sens.</P>
 */
public class Coup {
    int indexMain;
    int indexContinuum;
    int paradox;
    // 1 for paradox droite
    // -1 for paradox gauche
    // 0 sinon
    
    /** Construit un coup.
     *
     * @param indexMain Index de la carte dans la main.
     * @param indexContinuum Index de la carte dans le Continuum.
     * @param paradox Indique si un Paradox existe et dans quelle direction.
     */
    Coup(int indexMain, int indexContinuum, int paradox){
        this.indexContinuum = indexContinuum;
        this.indexMain = indexMain;
        this.paradox = paradox;
    }

    public int getIndexMain(){return indexMain;}
    public int getIndexContinuum(){return indexContinuum;}
    public int getParadox(){return paradox;}
}
