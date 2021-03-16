package app;

import app.TypeMisAJour;

public interface Observable {
    public void addObserver(Observer obs);
    public void removeObserver();
    public void notifyObserver(TypeMisAJour type);
}