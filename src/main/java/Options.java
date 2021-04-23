
import java.awt.Font;

public class Options {
    
    // Paramètres de la carte
	public static final int LARGEUR_CARTE = 100;
	public static final int HAUTEUR_CARTE = 100;
    public static final double RATIO_LARGEUR_HAUTEUR = 0.86;
	public static final int LARGEUR_CASE = 80;
	public static final int HAUTEUR_CASE = (int)(LARGEUR_CASE*RATIO_LARGEUR_HAUTEUR);
    public static final int ESPACE_INTER_CASE = 5;
	public static final int DELAI_ANIMATION = 15; // Le jeu se met à jour toutes les 15 milisecondes
    public static final double VITESSE_DEPLACEMENT_ROBOT = 7; // vitesse (pixel/durée d'itération de la boucle du jeu, c.à.d pixel/DELAI_ANIMATION)
    public static final long DUREE_NOTIFICATION = 3000; // Durée des messages d'information

    // Paramètres du robot
    public static final int BATTERIE_MIN = 0;
    public static final int BATTERIE_MAX = 100;
    public static final double CONSO_BATTERIE_PAR_KM = 0.001; // en %/Km 
    public static final double KM_PAR_PIXELS = 0.1;
    public static final int ALERTE_MIN = 0;
    public static final int ALERTE_MOY = 1;
    public static final int ALERTE_MAX = 2;
    public static final int USURE_MIN = 0;
    public static final int USURE_MAX = 100;
    public static final double USURE_PAR_KM = 0.01;
    public static final int USURE_PAR_UTILISATION = 10; // bras, capteurs
    public static final int CHANCE_DEGRADATION_JAMBES = 1; // en %
    public static final int CHANCE_DEGRADATION_AUTRES = 10; // en %
    public static final int PANNES_MAX = 2;
    
    // Animation robot
	public static final String[] JOUEURS_IMAGES = {"Idle", "Jump", "Walk", "Run"};
	public static final int JOUEUR_DUREE_ANIMATION = 50;
	public static final int JOUEUR_LARGEUR = 300;
	public static final int JOUERUR_TOLERANCE_DEPLACEMENT = 5;
    public static final int JOUEUR_TOLERANCE_CLICK = 500;

    public static final int ESPACE_INTER_BOUTON = 20;
	public static final int LARGEUR_BOUTON_CIRCULAIRE = 30;
	public static final int LARGEUR_BOUTON_CASE = 30;
	public static final int RAYON_BOUTON_CERCLE = 17;
	public static final int NOMBRE_BOUTONS_TYPE_PAR_LIGNE = 4;

    public static final int EPAISSEUR_BORDURE_CERCLE = 12;

    
	public static final double RATIO_LARGEUR_MENU = 5.; // La largeur du menu correspond à largeurEcran/RATIO_LARGEUR_MENU

	public static final Font police = new Font("Courier", Font.BOLD, 24);
    public static final Font POLICE_PLAIN = new Font("Courier", Font.PLAIN, 16);
    public static final double RATIO_TAILLE_SELECTION = 1.15;
	public static final int ESPACE_INTER_CASE_BOUTON = 8;

	public static final String[] infoLabels = {   "Taille",
                                            "Type de case",
                                            "Symboles"};
    public static final int[] yLabels = {   0,
                                      		110,
                                      		240};
											  
    public static final int INCREMENT_DE_DEPLACEMENT = 15; // Ajout de l'incrément à chaque appui sur une touche de déplacement (dans l'éditeur de carte)
    public static final double MULTIPLICATEUR_ZOOM = 1.1;

    public static final String NOM_DOSSIER_SYMBOLE = "symboles";
    public static final int INCREMENT_MINIMAP = 6;
    public static final double ZOOM_MINIMAP = 0.02;
    public static final double AGRANDISSEMENT_CELLULE_MINICARTE = 5.5;
    public static final double POSITION_X_MINIMAP = 0.85; // e.g: 0.9 de la largeur de l'écran
    public static final double POSITION_Y_MINIMAP = 0.08; //  e.g: 1/10 de la hauteur de l'écran
    public static final int[] DIMENSIONS_CASES =  {
        (int) (Options.LARGEUR_CASE*Options.ZOOM_MINIMAP*Options.AGRANDISSEMENT_CELLULE_MINICARTE),
        (int) (Options.LARGEUR_CASE*Options.RATIO_LARGEUR_HAUTEUR*Options.ZOOM_MINIMAP*Options.AGRANDISSEMENT_CELLULE_MINICARTE)
    };

    // Musique
    public static final int EFFETS_VOLUME_MIN = 0;
	public static final int EFFETS_VOLUME_MAX = 100;
    public static final int EFFETS_VOLUME_INIT = 80;

    public static final int MUSIQUE_VOLUME_MIN = 0;
	public static final int MUSIQUE_VOLUME_MAX = 100;
    public static final int MUSIQUE_VOLUME_INIT = 70;
    public static final String NOM_DOSSIER_MUSIQUES = "musiques";
    public static final String NOM_DOSSIER_EFFETS = "effets";
    public static final String NOM_DOSSIER_CARTES = "cartes";
    public static final long TEMPS_AVANT_SUPPRESSION_MINIJEU = 3000; // en secondes
    public static final int NOMBRE_ERREURS_LASER = 3;

    public static final String NOM_DOSSIER_IMAGES = "images";
    public static final int RAYON_JOUEUR = 4; // Rayon de sélection autorisé du joueur
    public static final int RAYON_DECOUVERTE = 3;
    public static final String NOM_DOSSIER_DETAILS = "details";
    public static final int NOMBRE_MAX_ITERATIONS_TRAJECTOIRE = 15;

    // Mini-jeu Extraction
    public static final double INCREMENT_CURSEUR = 0.05;
    public static final int NOMBRE_MAX_LIGNES = 1000;
    public static final int NOMBRE_MAX_COLONNES = 1000;
    public static final int TEMPS_DE_REACTION_MIN = 360;
    public static final int PRECISION_MIN = 85;
    public static final double PROBABILITE_CELLULE_VISIBLE = 0.4; // Probabilité qu'un symbole soit visible
    public static final String NOM_DOSSIER_CARTES_PAR_DEFAUT = "cartesParDefaut";
}
