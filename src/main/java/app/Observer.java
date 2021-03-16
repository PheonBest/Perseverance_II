package app;

import app.Cellule;
import app.TypeMisAJour;

public interface Observer {
	public void update(TypeMisAJour type, Object nouveau);
}