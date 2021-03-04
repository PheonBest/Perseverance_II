package app.utils;

import app.carte.Cellule;
import app.donnees.TypeMisAJour;

public interface Observer {
	public void update(TypeMisAJour type, Object nouveau);
}