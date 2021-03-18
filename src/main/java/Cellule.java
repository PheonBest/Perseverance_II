import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.BasicStroke;
import java.awt.geom.AffineTransform;

public class Cellule extends Polygon implements Dessin {
    //////////////////////////////////////////////////////////////////// Attributs 
    
    private TypeCase type;
    private Color couleur;
    
    //Position dans la matrice de la carte            
    private int ligne;
    private int colonne;

    private double largeur;
    private double hauteur;
    private boolean sourisDessus = false;
     
    //////////////////////////////////////////////////////////////////// Constructeurs 
    
    /* Constructeur complet
     * On va calculer les coordonées de chacun des points du polygône et les 
     * stocker dans des tableaux x et y, en fonction de leurs ligne et de leur colonn
     * */
    
    public Cellule(TypeCase unType, int uneLigne, int uneColonne, double taille, int espaceInterCase){
        
        //Position dans la matrice de la carte
        ligne = uneLigne;
        colonne = uneColonne;
        type = unType;
        couleur = this.getColor();
        largeur = Options.LARGEUR_CASE*taille;
        hauteur = Options.HAUTEUR_CASE*taille;
        // On créée les points du polygone
        double[][] points = {
            {largeur/4,0},
            {3*largeur/4,0},
            {largeur,hauteur/2},
            {3*largeur/4,hauteur},
            {largeur/4,hauteur},
            {0,hauteur/2}
        };
        for (int i=0; i < points.length; i++)
            addPoint((int)points[i][0],(int)points[i][1]);

        // On place le polygone
        double xInit = -largeur/2;
        double yInit = -hauteur/2;

        // On décale verticalement les cases hexagonales
        double compensationY = 0;
        if (colonne %2 == 0)
            compensationY = hauteur/2;
        
        // Calcul des coordonnées
        // Pour que 2 hexagones se touches, on enlève à x un quart de la largeur d'une case
        // Donc on multiplie LARGEUR_CASE par 3/4 = 0.75
        int xCoord = (int)(xInit + colonne*(0.75*largeur+espaceInterCase));
        int yCoord = (int)(yInit + compensationY + ligne*(hauteur+espaceInterCase));

        // Déplacement du polygone
        translate(xCoord, yCoord);

        // Déplacement du symbole
    }

    public Cellule(int uneLigne, int uneColonne){
        this(TypeCase.VIDE, uneLigne, uneColonne, 1, Options.ESPACE_INTER_CASE);
    }
    
    public void dessiner(Graphics g) {
        // On dessine le polygone
        Graphics2D g2d = (Graphics2D) g.create();
        
        if (sourisDessus) {
            final double[] CENTRE = {xpoints[0]+largeur/4, ypoints[0]+hauteur/2};
            final double[] COIN_EN_HAUT_A_GAUCHE = {xpoints[0]-largeur/4, ypoints[0]};
            final double deltaLargeur = largeur*(Options.RATIO_TAILLE_SELECTION-1)/2.;
            final double deltaHauteur = hauteur*(Options.RATIO_TAILLE_SELECTION-1)/2.;
            translate((int)(-COIN_EN_HAUT_A_GAUCHE[0]), (int)(-COIN_EN_HAUT_A_GAUCHE[1]));
            AffineTransform at = g2d.getTransform();
            // On a un décalage de quart de largeur entre le polygone initial, et le polygone aggrandi
            at.translate(COIN_EN_HAUT_A_GAUCHE[0]-deltaLargeur*Options.RATIO_TAILLE_SELECTION/2.,COIN_EN_HAUT_A_GAUCHE[1]-deltaHauteur*Options.RATIO_TAILLE_SELECTION/2.);
            at.scale(Options.RATIO_TAILLE_SELECTION, Options.RATIO_TAILLE_SELECTION);
            g2d.setTransform(at);
            g2d.setColor(couleur);
            g2d.fillPolygon(this);
            translate((int)(COIN_EN_HAUT_A_GAUCHE[0]), (int)(COIN_EN_HAUT_A_GAUCHE[1]));
            /*
            g2d = (Graphics2D) g.create();
            g2d.setColor(Color.RED);
            g2d.fillPolygon(this);
            */
        } else {
            g2d.setColor(couleur);
            g2d.fillPolygon(this);
        }
    }
    
    //////////////////////////////////////////////////////////////////// Méthodes
    
    public boolean memeType(Cellule uneCellule){
        return (this.type==uneCellule.type);
    }
    
    public Color getColor(){
        switch(type){
            case VIDE:
                return Color.black;
            case EAU:
                return Color.blue; 
            case MONTAGNE:
                return Color.gray;
            case DESERT:
                return Color.yellow;
            case SABLE_MOUVANTS :
                return Color.orange;
            case NEIGE:
                return Color.white;
            case FORET:
                return Color.green;
            default :
                return null;
                
        }
    }

    public int[] getCoordMatrice(Cellule uneCellule){
        return (new int[]{ligne,colonne});
    }

	public boolean estVisible(double largeurEcran, double hauteurEcran) {
		Rectangle b = getBounds();
        // Si la case est visible à l'écran:
        if (    (b.x + largeur > 0 && b.x - largeur < largeurEcran)
                && (b.y + hauteur > 0 && b.y - hauteur < hauteurEcran))
            return true;
        // Sinon, la case n'est pas visible
        return false;
	}

    public void majSourisDessus(boolean sourisDessus){
        this.sourisDessus = sourisDessus;
    }

    public TypeCase obtenirType() {
        return type;
    }

    public void majType(TypeCase unType) {
        type = unType;
        couleur = this.getColor();
    }
}
