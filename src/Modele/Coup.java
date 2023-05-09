package Modele;

public class Coup {
    int indexMain;
    int indexContinuum;
    int paradox;
    // 1 for paradox droite
    // -1 for paradox gauche
    // 0 sinon

    Coup(int indexMain, int indexContinuum, int paradox){

        this.indexContinuum = indexContinuum;
        this.indexMain = indexMain;
        this.paradox = paradox;
    }

    public int getIndexMain(){return this.indexMain;}
    public int getIndexContinuum(){return this.indexContinuum;}
    public int getParadox(){return this.paradox;}
}
