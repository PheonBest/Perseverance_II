package app.utils;

import app.carte.Cellule;

public interface Observer {
    public void update(Cellule cellule);
}