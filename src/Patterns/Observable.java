package Patterns;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <P> Implémentation du Pattern Observateur. </P>
 * <P> Utilisé pour l'implémentation du niveau graphique. </P>
 *
 * @author Guillaume Huard — Projet Sokoban
 */
public class Observable {
	List<Observateur> observateurs;

	public Observable() {
		observateurs = new ArrayList<>();
	}

	public void ajouteObservateur(Observateur o) {
		observateurs.add(o);
	}

	public void metAJour() {
		Iterator<Observateur> it;

		it = observateurs.iterator();
		while (it.hasNext()) {
			Observateur o = it.next();
			o.miseAJour();
		}
	}
}
