package app.donnees;

import java.util.ArrayList;

import app.Options;
import app.carte.Cellule;
import app.avatar.Robot;
import app.utils.Observable;
import app.utils.Observer;

import java.awt.Image;

public class Donnees implements Observable {

    private Robot joueur;
    private String scene;
    private ArrayList<Observer> listObserver = new ArrayList<Observer>();
    private Cellule[][] cellules = { {} };
    private int largeur;
    private int hauteur;

    // Images du joueur
    // On les stocke car elles mettent un certain temps à charger
    private ArrayList<ArrayList<Image>> images;

    private int avancementChargement;
    
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

    public void majAvancement(int avancementChargement) {
        this.avancementChargement = avancementChargement;
    }

    public void majScene(String scene) {
        this.scene = scene;
    }
    
    public void majImagesJoueur(ArrayList<ArrayList<Image>> images) {
        //(images.size());
        this.images = images;
    }

    public ArrayList<ArrayList<Image>> getImagesJoueur() {
        return images;
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
        //notifyObserver(); // Pas besoin de notifier l'observeur lorsqu'il vient d'être ajouté
        // Car l'observeur est ajouté dès le départ
    }

    @Override
    public void removeObserver() {
        listObserver.clear();
    }

    @Override
    public void notifyObserver(TypeMisAJour type) {
        for (Observer obs: listObserver) {
            switch (type) {
                case Cellules:
                    obs.update(TypeMisAJour.Cellules, cellules);
                    break;
                case Joueur:
                    obs.update(TypeMisAJour.Joueur, joueur);
                    break;
                case Avancement:
                    //System.out.println(avancementChargement);
                    obs.update(TypeMisAJour.Avancement, avancementChargement);
                    break;
                case Peindre:
                    obs.update(TypeMisAJour.Peindre, null);// Prévoit de mettre à jour le composant
                    // n'appelle pas la méthode paint() !
                    break;
                case Scene:
                    obs.update(TypeMisAJour.Scene, scene);
                    break;
            }
        }
    }

    public Cellule[][] getCellules() {
        return cellules;
    }
    public Robot getJoueur() {
        return joueur;
    }
    public void majJoueur(Robot joueur) {
        this.joueur = joueur;
    }

    public int obtenirHauteur() {
        return hauteur;
    }

    public int obtenirLargeur() {
        return largeur;
    }
}
