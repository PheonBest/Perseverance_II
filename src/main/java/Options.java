
import java.awt.Font;

public class Options {
	public static final int LARGEUR_CARTE = 100;
	public static final int HAUTEUR_CARTE = 100;
    public static final double RATIO_LARGEUR_HAUTEUR = 0.86;
	public static final int LARGEUR_CASE = 80;
	public static final int HAUTEUR_CASE = (int)(LARGEUR_CASE*RATIO_LARGEUR_HAUTEUR);
    public static final int ESPACE_INTER_CASE = 5;
	public static final int DELAI_ANIMATION = 16;
    public static final int SANTE_MAX = 100;
    public static final int BATTERIE_MAX = 100;

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
}
