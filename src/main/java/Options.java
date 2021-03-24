
import java.awt.Font;

public class Options {
    
    // Paramètres de la carte
	public static final int LARGEUR_CARTE = 100;
	public static final int HAUTEUR_CARTE = 100;
    public static final double RATIO_LARGEUR_HAUTEUR = 0.86;
	public static final int LARGEUR_CASE = 80;
	public static final int HAUTEUR_CASE = (int)(LARGEUR_CASE*RATIO_LARGEUR_HAUTEUR);
    public static final int ESPACE_INTER_CASE = 5;
	public static final int DELAI_ANIMATION = 16;
    
    // Paramètres du robot
    public static final int BATTERIE_MAX = 100;
    public static final int BATTERIE_MIN = 0;
    public static final double CONSO_BATTERIE_PAR_KM = 0.1;
    public static final int ALERTE_MAX = 2;
    public static final int ALERTE_MOY = 1;
    public static final int ALERTE_MIN = 0;
    public static final int USURE_MIN = 0;
    public static final int USURE_MAX = 100;
    public static final double USURE_PAR_KM = 0.1;
    public static final int CHANCE_DEGRADATION = 5; // en pourcent
    public static final int PANNES_MAX = 2;
    
    // Animation robot
	public static final String[] JOUEURS_IMAGES = {"Idle", "Jump", "Walk", "Run"};
	public static final int JOUEUR_DUREE_ANIMATION = 50;
	public static final int JOUEUR_LARGEUR = 300;
	public static final int JOUERUR_TOLERANCE_DEPLACEMENT = 5;

    public static final int ESPACE_INTER_BOUTON = 50;
	public static final int LARGEUR_BOUTON_CIRCULAIRE = 30;
	public static final int LARGEUR_BOUTON_CASE = 30;
	public static final double RAYON_BOUTON_CERCLE = 20;
	public static final int NOMBRE_BOUTONS_TYPE_PAR_LIGNE = 4;

    
	public static final double RATIO_LARGEUR_MENU = 5.; // La largeur du menu correspond à largeurEcran/RATIO_LARGEUR_MENU

	public static final Font police = new Font("Courier", Font.BOLD, 24);
    public static final double RATIO_TAILLE_SELECTION = 1.15;
	public static final int ESPACE_INTER_CASE_BOUTON = 8;

	public static final String[] infoLabels = {   "Taille de pinceau",
                                            "Type de case",
                                            "Symboles"};
    public static final int[] yLabels = {   0,
                                      		150,
                                      		320};
											  
    public static final int INCREMENT_DE_DEPLACEMENT = 15; // Ajout de l'incrément à chaque appui sur une touche de déplacement (dans l'éditeur de carte)
    public static final double MULTIPLICATEUR_ZOOM = 1.1;

    public static final String NOM_DOSSIER_SYMBOLE = "symboles";
    public static final int INCREMENT_MINIMAP = 6;
    public static final double ZOOM_MINIMAP = 0.02;
    public static final double AGRANDISSEMENT_CELLULE_MINICARTE = 4;
    public static final double POSITION_X_MINIMAP = 9./10.; // e.g: 11/12 de la largeur de l'écran
    public static final double POSITION_Y_MINIMAP = 1./10.; //  e.g: 1/10 de la hauteur de l'écran
    public static final int[] DIMENSIONS_CASES =  {
        (int) (Options.LARGEUR_CASE*Options.ZOOM_MINIMAP*Options.AGRANDISSEMENT_CELLULE_MINICARTE),
        (int) (Options.LARGEUR_CASE*Options.RATIO_LARGEUR_HAUTEUR*Options.ZOOM_MINIMAP*Options.AGRANDISSEMENT_CELLULE_MINICARTE)
    };
}
