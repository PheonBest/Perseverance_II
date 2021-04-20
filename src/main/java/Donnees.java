import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineUnavailableException;

public class Donnees implements Observable {

    private ArrierePlan arrierePlan;
    private Image imageMenu;
    private Robot joueur;
    private String scene;
    private ArrayList<Observateur> listObserver = new ArrayList<Observateur>();
    private Cellule[][] cellules = { {} };
    private BoutonCercle[] boutonsCercle = {};
    private Cellule[] boutonsType = {};
    private Cellule[] boutonsSymbole = {};
    private HashMap<TypeSymbole, Boolean> symbolesDecouverts = new HashMap<TypeSymbole, Boolean>();
    private int largeur;
    private int hauteur;
    private Cellule derniereCellule = null;
    private Cellule derniereCaseType = null;
    private Cellule derniereCaseSymbole = null;
    private BoutonCercle dernierBouton = null;
    private double zoom = 1.;
    private StatutSouris statutSouris = new StatutSouris();
    private List<BoutonCercle> competences = new LinkedList<BoutonCercle>();
    private int rayonDeSelection = 0;
    private boolean etatOptions = false;
    private Cellule derniereCelluleMinijeu = null;
    private int nombrePonts = 0;
    
    // CSV des cartes && éditeur
    private HashMap<String, InputStream> cartes;
    private String nomCarte;
    private int[] celluleDepart = null;

    // Images du joueur
    // On les stocke car elles mettent un certain temps à charger
    private ArrayList<ArrayList<Image>> images;
    //Images des symboles
    public HashMap<String, Image> imagesSymboles;

    private int avancementChargement;
    private Point borduresFenetres;

    // Lecteurs de sons
    private Son lecteurMusique;
    private Son lecteurEffets;
    private boolean etatMusique = true;
    private boolean etatEffets = true;
    private BoutonCercle derniereCompetence = null;

    // Variables communes aux minijeux
    private long chronometreSuppresion = 0;

    // Minijeu Extraction
    private Etat etatMinijeuExtraction = Etat.OFF;
    private boolean sensVariationExtraction = true;
    private double positionCurseurExtraction = 0;

    // Minijeu Laser
    private Etat etatMinijeuLaser = Etat.OFF;
    private int tempsAvantChrono = 0; // Détermine le temps avant le début du minijeu
    private long repereMinijeuLaser = 0; // Repère de temps
    private String chronometreMinijeuLaser = ""; // Temps affiché en direct
    private int nombreErreursLaser = 0;
    
    public Donnees() {
        // Initialisation de la hashmap des symboles découverts
        for (TypeSymbole type: TypeSymbole.values())
            symbolesDecouverts.put(type, false);
            
        // Création d'une carte par défaut si elle n'existe pas déjà

        cellules = new Cellule[Options.LARGEUR_CARTE][Options.HAUTEUR_CARTE];
        for (int i=0; i < cellules.length; i++) {
            for (int j=0; j < cellules[i].length; j++)
                cellules[i][j] = new Cellule(i,j);
        }

        try {
            CSV.givenDataArray_whenConvertToCSV_thenOutputCreated(cellules, "Campagne", this.obtenirJoueur());
        } catch (IOException e) {
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
    public void ajouterObservateur(Observateur obs) {
        this.listObserver.add(obs);
        //notifierObservateur(); // Pas besoin de notifier l'observeur lorsqu'il vient d'être ajouté
        // Car l'observeur est ajouté dès le départ
    }

    @Override
    public void enleverObservateur() {
        listObserver.clear();
    }

    @Override
    public void notifierObservateur(TypeMisAJour type) {
        for (Observateur obs: listObserver) {
            switch (type) {

                // Carte
                case Cellules:
                    obs.mettreAJour(TypeMisAJour.Cellules, cellules);
                    break;
                case Peindre:
                    obs.mettreAJour(TypeMisAJour.Peindre, null);// Prévoit de mettre à jour le composant
                    // n'appelle pas la méthode paint() !
                    break;
                case Avancement:
                    obs.mettreAJour(TypeMisAJour.Avancement, avancementChargement);
                    break;
                case Scene:
                    obs.mettreAJour(TypeMisAJour.Scene, scene);
                    break;
                    case Zoom:
                    obs.mettreAJour(TypeMisAJour.Zoom, zoom);
                    break;
                case Cartes:
                    obs.mettreAJour(TypeMisAJour.Cartes, cartes);
                    break;
                case ArrierePlan:
                    obs.mettreAJour(TypeMisAJour.ArrierePlan, arrierePlan);
                    break;

                // Joueur
                case Joueur:
                    obs.mettreAJour(TypeMisAJour.Joueur, joueur);
                    break;
                case Competences:
                    obs.mettreAJour(TypeMisAJour.Competences, competences);
                    break;
                case RayonDeSelection:
                    obs.mettreAJour(TypeMisAJour.RayonDeSelection, rayonDeSelection);
                    break;

                // Editeur de carte
                case BoutonsType:
                    obs.mettreAJour(TypeMisAJour.BoutonsType, boutonsType);
                    break;
                case BoutonsCercle:
                    obs.mettreAJour(TypeMisAJour.BoutonsCercle, boutonsCercle);
                    break;
                case BoutonsSymbole:
                    obs.mettreAJour(TypeMisAJour.BoutonsSymbole, boutonsSymbole);
                    break;

                // Mini jeu extraction
                case MinijeuExtraction:
                    obs.mettreAJour(TypeMisAJour.MinijeuExtraction, etatMinijeuExtraction);
                    break;
                case PositionCurseurExtraction:
                    obs.mettreAJour(TypeMisAJour.PositionCurseurExtraction, positionCurseurExtraction);
                    break;
                case SensVariationExtraction:
                    obs.mettreAJour(TypeMisAJour.SensVariationExtraction, sensVariationExtraction);
                    break;

                // Mini jeu laser
                case MinijeuLaser:
                    obs.mettreAJour(TypeMisAJour.MinijeuLaser, etatMinijeuLaser);
                    break;
                case ChronometreLaser:
                    obs.mettreAJour(TypeMisAJour.ChronometreLaser, chronometreMinijeuLaser);
                    break;
                case NombreErreursLaser:
                    obs.mettreAJour(TypeMisAJour.NombreErreursLaser, nombreErreursLaser);
                    break;
                    
                // Menus
                case Options:
                    obs.mettreAJour(TypeMisAJour.Options, etatOptions);
                    break;
                case ImageMenu:
                    obs.mettreAJour(TypeMisAJour.ImageMenu, imageMenu);
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

    public Cellule[] obtenirBoutonsSymbole() {
        return boutonsSymbole;
    }

    public Cellule[] obtenirBoutonsType() {
        return boutonsType;
    }

    public BoutonCercle[] obtenirBoutonsCercle() {
        return boutonsCercle;
    }

    public void majBoutonsSymbole(Cellule[] boutonsSymbole) {
        this.boutonsSymbole = boutonsSymbole;
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

    public void majStatutSouris(MouseEvent e, boolean clic) {
        if (clic)
            statutSouris.majClicGauche(true);
        else   
            statutSouris.majClicGauche(false);

        statutSouris.majX(e.getX());
        statutSouris.majY(e.getY());
    }

    public StatutSouris obtenirStatutSouris() {
        return this.statutSouris;
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

    public HashMap<TypeSymbole, Boolean> obtenirSymbolesDecouverts() {
        return symbolesDecouverts;
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

    // Mini jeux
    public void majChronometreSuppression(long chronometreSuppresion) {
        this.chronometreSuppresion = chronometreSuppresion;
    }
    public long obtenirChronometreSuppresion() {
        return chronometreSuppresion;
    }

    // Mini jeu extraction ----------
    public void majEtatMinijeuExtraction(Etat etatMinijeuExtraction) {
        this.etatMinijeuExtraction = etatMinijeuExtraction;
    }
    public Etat obtenirEtatMiniJeuExtraction() {
        return etatMinijeuExtraction;
    }
    public void majSensVariationExtraction(boolean sensVariationExtraction) {
        this.sensVariationExtraction = sensVariationExtraction;
    }
    public boolean obtenirSensVariationExtraction() {
        return sensVariationExtraction;
    }
    public void majPositionCurseurExtraction(double positionCurseurExtraction) {
        this.positionCurseurExtraction = positionCurseurExtraction;
    }
    public double obtenirPositionCurseurExtraction() {
        return positionCurseurExtraction;
    }
    
    // Mini jeu extraction ----------

    // Mini jeu laser ----------
    public void majEtatMinijeuLaser(Etat etatMinijeuLaser) {
        this.etatMinijeuLaser = etatMinijeuLaser;
    }
    public Etat obtenirEtatMiniJeuLaser() {
        return etatMinijeuLaser;
    }
    public void majRepereMinijeuLaser(long repereMinijeuLaser) {
        this.repereMinijeuLaser = repereMinijeuLaser;
    }
    public long obtenirRepereMinijeuLaser() {
        return repereMinijeuLaser;
    }
    public void majChronometreMinijeuLaser(String chronometreMinijeuLaser) {
        this.chronometreMinijeuLaser = chronometreMinijeuLaser;
    }
    public String obtenirChronometreMinijeuLaser() {
        return chronometreMinijeuLaser;
    }
    public void majTempsAvantChrono(int tempsAvantChrono) {
        this.tempsAvantChrono = tempsAvantChrono;
    }
    public int obtenirTempsAvantChrono() {
        return tempsAvantChrono;
    }
    public void majNombreErreursLaser(int nombreErreursLaser) {
        this.nombreErreursLaser = nombreErreursLaser;
    }
    public int obtenirNombreErreursLaser() {
        return nombreErreursLaser;
    }
    // Mini jeu laser ----------

    public ArrierePlan obtenirArrierePlan() {
        return arrierePlan;
    }
    public void majArrierePlan(ArrierePlan arrierePlan) {
        this.arrierePlan = arrierePlan;
    }

    public boolean obtenirEtatOptions() {
        return etatOptions;
    }

    public void majEtatOptions(boolean etatOptions) {
        this.etatOptions = etatOptions;
    }

    public void majImageMenu(Image imageMenu) {
        this.imageMenu = imageMenu;
    }

    public Cellule obtenirDerniereCaseSymbole() {
        return derniereCaseSymbole;
    }

    public void majDerniereCaseSymbole(Cellule derniereCaseSymbole) {
        this.derniereCaseSymbole = derniereCaseSymbole;
    }

    public void majDerniereCelluleMinijeu(Cellule derniereCelluleMinijeu) {
        this.derniereCelluleMinijeu = derniereCelluleMinijeu;
    }
    public Cellule obtenirDerniereCelluleMinijeu() {
        return derniereCelluleMinijeu;
    }

    public int obtenirNombrePont() {
        return nombrePonts;
    }

    public void majNombrePonts(int nombrePonts) {
        this.nombrePonts = nombrePonts;
    }

    public void majLargeur(int largeur) {
        this.largeur = largeur;
    }
    public void majHauteur(int hauteur) {
        this.hauteur = hauteur;
    }
    public void majCelluleDepart(int[] celluleDepart) {
        this.celluleDepart = celluleDepart;
    }
    public int[] obtenirCelluleDepart() {
        return celluleDepart;
    }
}
