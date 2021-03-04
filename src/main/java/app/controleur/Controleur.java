package app.controleur;

import app.donnees.Donnees;

public class Controleur {
    
    Donnees donnees;

    public Controleur(Donnees donnees) {
        this.donnees = donnees;
    }

	public void jouer() {
        //
	}

	public void rafraichir() {
        //donnees.notifyObserver();
	}
    
}
