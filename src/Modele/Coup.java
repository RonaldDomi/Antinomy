package Modele;

public class Coup {
    int indexMain;
    int indexContinuum;
    int[] ordre;
    // 1 for paradox droite
    // -1 for paradox gauche
    // 0 sinon

    Coup(int indexMain, int indexContinuum, int[] ordre){
        this.indexContinuum = indexContinuum;
        this.indexMain = indexMain;
        this.ordre = ordre;
    }

    public int getIndexMain(){return this.indexMain;}
    public int getIndexContinuum(){return this.indexContinuum;}
    public int[] getOrdre(){return this.ordre;}
}
