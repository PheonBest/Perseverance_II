package app;

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
}
