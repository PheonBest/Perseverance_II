package app.donnees;

import java.util.ArrayList;

import app.Options;
import app.carte.Cellule;
import app.sprites.Joueur;
import app.utils.Observable;
import app.utils.Observer;

public class Donnees implements Observable {

    private Joueur joueur;
    private ArrayList<Observer> listObserver = new ArrayList<Observer>();
    private Cellule[][] cellules = { {} };
    private int largeur;
    private int hauteur;
    
    public Donnees(int largeur, int hauteur) {
        this.largeur = largeur;
        this.hauteur = hauteur;
        /*
        Si il y a une carte, la charger.
        Sinon:
        */
        cellules = new Cellule[Options.LARGEUR_CARTE][Options.HAUTEUR_CARTE];
        for (int i=0; i < cellules.length; i++) {
            for (int j=0; j < cellules[i].length; j++)
                cellules[i][j] = new Cellule(i,j);
        }
    }

    public void majCellules(Cellule[][] cellules) {
        this.cellules = cellules;
    }

    public void majCellule(Cellule cellule, int i, int j) {
        cellules[i][j] = cellule;
    }

    @Override
    public void addObserver(Observer obs) {
        this.listObserver.add(obs);
        notifyObserver();
    }

    @Override
    public void removeObserver() {
        listObserver.clear();
    }

    @Override
    public void notifyObserver() {
        for (Observer obs: listObserver) {
            obs.update(TypeMisAJour.Cellules, cellules);
            obs.update(TypeMisAJour.Joueur, joueur);
            obs.update(TypeMisAJour.Peindre, null);
        }
    }

    public Cellule[][] getCellules() {
        return cellules;
    }
    public Joueur getJoueur() {
        return joueur;
    }
    public void majJoueur(Joueur joueur) {
        this.joueur = joueur;
    }

    public int obtenirHauteur() {
        return hauteur;
    }

    public int obtenirLargeur() {
        return largeur;
    }
}
