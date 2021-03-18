
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
    public static final int BATTERIE_MIN = 100;
    public static final int ALERTE_MAX = 2;
    public static final int ALERTE_MIN = 0;
    public static final int PANNES_MAX = 2;
    
    // Animation robot
	public static final String[] JOUEURS_IMAGES = {"Idle", "Jump", "Walk", "Run"};
	public static final int JOUEUR_DUREE_ANIMATION = 50;
	public static final int JOUEUR_LARGEUR = 300;
	public static final int JOUERUR_TOLERANCE_DEPLACEMENT = 5;

    // Widgets
    public static final int ESPACE_INTER_BOUTON = 70;
	public static final int LARGEUR_BOUTON_CIRCULAIRE = 30;
	public static final int LARGEUR_BOUTON_CASE = 30;
	public static final double RAYON_BOUTON_CERCLE = 30;
	public static final int NOMBRE_BOUTONS_TYPE_PAR_LIGNE = 4;

	public static final Font police = new Font("Courier", Font.BOLD, 24);
}
