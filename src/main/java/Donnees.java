import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineUnavailableException;

import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

public class Donnees implements Observable {

    private Robot joueur;
    private String scene;
    private ArrayList<Observer> listObserver = new ArrayList<Observer>();
    private Cellule[][] cellules = { {} };
    private Cellule[][] cellulesFixes = { {} };
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
    private List<BoutonCercle> competences = new LinkedList<BoutonCercle>();
    private int rayonDeSelection = 0;
    
    // CSV des cartes
    private HashMap<String, InputStream> cartes;
    private String nomCarte;

    // Images du joueur
    // On les stocke car elles mettent un certain temps à charger
    private ArrayList<ArrayList<Image>> images;
    //Images des symboles
    private HashMap<String, Image> imagesSymboles;

    private int avancementChargement;
    private Point positionFenetre;

    // Lecteurs de sons
    private Son lecteurMusique;
    private Son lecteurEffets;
    private boolean etatMusique = true;
    private boolean etatEffets = true;
    private BoutonCercle derniereCompetence = null;
    
    public Donnees(int largeur, int hauteur) {
        this.largeur = largeur;
        this.hauteur = hauteur;

        // Création d'une carte

        List<String[]> data = new ArrayList<String[]>();

        cellules = new Cellule[Options.LARGEUR_CARTE][Options.HAUTEUR_CARTE];
        for (int i=0; i < cellules.length; i++) {
            for (int j=0; j < cellules[i].length; j++) {
                cellules[i][j] = new Cellule(i,j);
                cellules[i][j].translate(-largeur/2, -hauteur /2); // Décalage de l'affichage
            }
        }

        try {
            CSV.givenDataArray_whenConvertToCSV_thenOutputCreated(cellules, "new");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
                case RayonDeSelection:
                    obs.mettreAJour(TypeMisAJour.RayonDeSelection, rayonDeSelection);
                case Competences:
                    obs.mettreAJour(TypeMisAJour.Competences, competences);
                    break;
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
                case Cartes:
                    obs.mettreAJour(TypeMisAJour.Cartes, cartes);
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
    public BoutonCercle obtenirDerniereCompetence() {
        return derniereCompetence;
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
    public void majDerniereCompetence(BoutonCercle b) {
        derniereCompetence = b;
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

    // MUSIQUE
    public void majVolumeEffets(int volumeEffets) {
        lecteurEffets.setVolume(volumeEffets);
    }

    public void majVolumeMusique(int volumeEffets) {
        lecteurMusique.setVolume(volumeEffets);
    }

    public void majEtatMusique(boolean etatMusique) {
        this.etatMusique = etatMusique;
        if (!etatMusique)
            lecteurMusique.stop();
        else {
            lecteurMusique.resume();
            lecteurMusique.loop();
        }
    }

    public void majEtatEffets(boolean etatEffets) {
        this.etatEffets = etatEffets;
        if (!etatEffets)
            lecteurEffets.stop();
    }

    public boolean obtenirEtatMusique() {
        return etatMusique;
    }
    public boolean obtenirEtatEffets() {
        return etatEffets;
    }

    public void majMusique(int indexMusique) {
        try {
            lecteurMusique.play(indexMusique);
        } catch (IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void majMusique(String musique) {
        try {
            lecteurMusique.play(musique);
        } catch (IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void boucleMusique() {
        lecteurMusique.loop();
    }

    public void majEffet(int indexEffet) {
        try {
            lecteurEffets.play(indexEffet);
        } catch (IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void majEffet(String effet) {
        try {
            lecteurEffets.play(effet);
        } catch (IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void musiqueSuivante() {
        try {
            lecteurMusique.play();
        } catch (IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void majListeEffets(HashMap<String, AudioInputStream> effets) {
        try {
            lecteurEffets = new Son(effets, Options.MUSIQUE_VOLUME_INIT, true); // "true": créée un nouveau clip à chaque effet audio
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        
    }

    public void majListeMusiques(HashMap<String, AudioInputStream> musiques) {
        try {
            lecteurMusique = new Son(musiques, Options.MUSIQUE_VOLUME_INIT, false); // "false": ne créée pas un nouveau clip à chaque musique
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void majCartes(HashMap<String, InputStream> cartes) {
        /*
        for (String i : cartes.keySet())
            System.out.println("Nom du CSV : " + i);
        */
        this.cartes = cartes;
    }

    public HashMap<String, InputStream> obtenirCartes() {
        return cartes;
    }

    public void majCompetences(List<BoutonCercle> competences) {
        this.competences = competences;
    }

    public List<BoutonCercle> obtenirCompetences() {
        return competences;
    }
    
    public int obtenirRayonDeSelection() {
        return rayonDeSelection;
    }

    public void majRayonDeSelection(int rayonDeSelection) {
        this.rayonDeSelection = rayonDeSelection;
    }

    public void majNomCarte(String nomCarte) {
        this.nomCarte = nomCarte;
    }

    public String obtenirNomCarte() {
        return nomCarte;
    }
}
