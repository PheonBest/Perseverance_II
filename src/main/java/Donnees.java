import java.util.ArrayList;
import java.util.HashMap;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;

public class Donnees implements Observable {

    private Robot joueur;
    private String scene;
    private ArrayList<Observer> listObserver = new ArrayList<Observer>();
    private Cellule[][] cellules = { {} };
    private BoutonCercle[] boutonsCercle = {};
    private Cellule[] boutonsType = {};
    private int largeur;
    private int hauteur;
    private Cellule derniereCellule = null;
    private Cellule derniereCaseType = null;
    private BoutonCercle dernierBouton = null;
    private double zoom = 1.;
    private Point centreZoom;
    private StatutSouris statutSouris = new StatutSouris();

    // Images du joueur
    // On les stocke car elles mettent un certain temps à charger
    private ArrayList<ArrayList<Image>> images;
    //Images des symboles
    private HashMap<String, Image> imagesSymboles;

    private int avancementChargement;
    private Point positionFenetre;
    
    public Donnees(int largeur, int hauteur) {
        this.largeur = largeur;
        this.hauteur = hauteur;
        /*
        Si il y a une carte, la charger.
        Sinon:
        */
        cellules = new Cellule[Options.LARGEUR_CARTE][Options.HAUTEUR_CARTE];
        for (int i=0; i < cellules.length; i++) {
            for (int j=0; j < cellules[i].length; j++) {
                cellules[i][j] = new Cellule(i,j);
                cellules[i][j].translate(-largeur/2, -hauteur /2); // Décalage de l'affichage
            }
        }
    }

    // Getters et setters
    // Getters: Renvoie une donnée (attribut de Donnees.java)
    // Setters: Définit une donnée (attribut de Donnees.java)
    public void majAvancement(int avancementChargement) {
        this.avancementChargement = avancementChargement;
    }

    public String getScene() {
        return scene;
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
        //notifierObserveur(); // Pas besoin de notifier l'observeur lorsqu'il vient d'être ajouté
        // Car l'observeur est ajouté dès le départ
    }

    @Override
    public void removeObserver() {
        listObserver.clear();
    }

    @Override
    public void notifierObserveur(TypeMisAJour type) {
        for (Observer obs: listObserver) {
            switch (type) {
                case Cellules:
                    obs.mettreAJour(TypeMisAJour.Cellules, cellules);
                    break;
                case Joueur:
                    obs.mettreAJour(TypeMisAJour.Joueur, joueur);
                    break;
                case Avancement:
                    obs.mettreAJour(TypeMisAJour.Avancement, avancementChargement);
                    break;
                case Peindre:
                    obs.mettreAJour(TypeMisAJour.Peindre, null);// Prévoit de mettre à jour le composant
                    // n'appelle pas la méthode paint() !
                    break;
                case BoutonsType:
                    obs.mettreAJour(TypeMisAJour.BoutonsType, boutonsType);
                    break;
                case BoutonsCercle:
                    obs.mettreAJour(TypeMisAJour.BoutonsCercle, boutonsCercle);
                    break;
                case CentreZoom:
                    obs.mettreAJour(TypeMisAJour.CentreZoom, centreZoom);
                    break;
                case Zoom:
                    obs.mettreAJour(TypeMisAJour.Zoom, zoom);
                    break;
                case Scene:
                    obs.mettreAJour(TypeMisAJour.Scene, scene);
                    break;
            }
        }
    }

    public Cellule[][] obtenirCellules() {
        return cellules;
    }
    public Robot obtenirJoueur() {
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


    public Cellule obtenirDerniereCellule() {
        return derniereCellule;
    }
    public Cellule obtenirDerniereCaseType() {
        return derniereCaseType;
    }
    public BoutonCercle obtenirDernierBouton() {
        return dernierBouton;
    }

    public void majDernierBouton(BoutonCercle b) {
        dernierBouton = b;
    }
    public void majDerniereCaseType(Cellule c) {
        derniereCaseType = c;
    }
    public void majDerniereCellule(Cellule c) {
        derniereCellule = c;
    }

    public Cellule[] obtenirBoutonsType() {
        return boutonsType;
    }

    public BoutonCercle[] obtenirBoutonsCercle() {
        return boutonsCercle;
    }

    public void majBoutonsCercle(BoutonCercle[] boutonsCercle) {
        this.boutonsCercle = boutonsCercle;
    }

    public void majBoutonsType(Cellule[] boutonsType) {
        this.boutonsType = boutonsType;
    }

    public void majZoom(double zoom) {
        this.zoom = zoom;
    }

    public double obtenirZoom() {
        return zoom;
    }

    public void majCentreZoom(Point centreZoom) {
        this.centreZoom = centreZoom;
    }

    public void majStatutSouris(MouseEvent e, boolean clic) {
        if (clic)
            statutSouris.majClicGauche(true);
        else   
            statutSouris.majClicGauche(false);

        statutSouris.majX(e.getX());
        statutSouris.majY(e.getY());
    }

    public void majStatutSouris(MouseEvent e) {
        statutSouris.majX(e.getX());
        statutSouris.majY(e.getY());
    }

    public StatutSouris obtenirStatutSouris() {
        return this.statutSouris;
    }

    public void majPositionFenetre(Point location) {
        this.positionFenetre = location;
    }

    public Point obtenirPositionFenetre() {
        return positionFenetre;
    }

    public void majImagesSymboles(HashMap<String, Image> imagesSymboles) {
        this.imagesSymboles = imagesSymboles;
    }

    public HashMap<String, Image> getImagesSymboles() {
        return imagesSymboles;
    }
}
