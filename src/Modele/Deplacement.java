package Modele;

import Global.Configuration;

/**
 * Implémentation d'un déplacement pour les animations.
 */
public class Deplacement {

	int indice;
	Position avant;
	Position actuel;
	Position apres;
	private int etape;

	public Deplacement(Position av, Position ap, int indice, boolean estFini){
		avant = av;
		actuel = new Position(av.getX(), av.getY());
		apres = ap;
		etape = estFini ? Configuration.tempsAnimation : 0;
		this.indice = indice;
	}

	public void setIndice(int indice) {
		this.indice = indice;
	}

	public int getIndice() {
		return indice;
	}

	public boolean est_arrive(){
		return etape == Configuration.tempsAnimation;
	}

	public Position decale(){
		if (!est_arrive()){
			etape++;
			actuel = new Position(actuel.getX()-(avant.getX()-apres.getX())/Configuration.tempsAnimation, actuel.getY()-(avant.getY()-apres.getY())/Configuration.tempsAnimation);
			return actuel;
		}
		return actuel;
	}

	public Position getActuel() {
		if (!est_arrive()){
			return actuel;
		}
		return apres;
	}

	public Position getAvant() {
		return avant;
	}

	public Position getApres() {
		return apres;
	}
	public void setApres(Position apres) {
		this.apres = apres;
	}

	public void setAvant(Position avant) {
		this.avant = avant;
	}
	public void setActuel(Position actuel) {
		this.actuel = actuel;
	}

	public boolean teste(int condition, int newX, int newY, int newIndice){
		if (condition != getIndice()){
			avant = apres;
			apres = new Position(newX, newY);
			indice = newIndice;
			etape = 0;
			return true;
		}
		else {
			if (!est_arrive()){
				return true;
			}
			else{
				apres = new Position(newX, newY);
				actuel = apres;
				return true;
			}
		}
	}

	@Override
	public String toString() {
		return "[" + avant.toString() + "; " + actuel.toString() + "; " + apres.toString() + "] "+ etape;
	}
}
