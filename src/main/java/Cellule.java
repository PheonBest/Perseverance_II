import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.BasicStroke;
import java.awt.geom.AffineTransform;
import java.awt.Dimension;
import java.awt.Image;

public class Cellule extends Polygon implements Dessin {
    //////////////////////////////////////////////////////////////////// Attributs 
    
    private TypeCase type;
    private Color couleur;
    private Symbole symbole;
    
    //Position dans la matrice de la carte            
    private int ligne;
    private int colonne;

    private double largeur;
    private double hauteur;
    private double taille;
    private boolean sourisDessus = false;

    private boolean estDecouverte = false;
    private boolean selectionne = false;
     
    //////////////////////////////////////////////////////////////////// Constructeurs 
    
    /* Constructeur complet
     * On va calculer les coordonées de chacun des points du polygône et les 
     * stocker dans des tableaux x et y, en fonction de leurs ligne et de leur colonne
     * */
    
    public Cellule(TypeCase unType, int uneLigne, int uneColonne, double taille, int espaceInterCase, boolean estDecouverte, Symbole symbole){

        this.taille = taille;
        this.symbole = symbole;
        this.estDecouverte = estDecouverte;
        //Position dans la matrice de la carte
        ligne = uneLigne;
        colonne = uneColonne;
        type = unType;
        couleur = this.obtenirCouleur();
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

    public Cellule(TypeCase unType, int uneLigne, int uneColonne, double taille, int espaceInterCase, boolean estDecouverte){
        this(unType, uneLigne, uneColonne, taille, espaceInterCase, estDecouverte, new Symbole(TypeSymbole.VIDE, null, false));
    }

    public Cellule(TypeCase type, int uneLigne, int uneColonne){
        this(type, uneLigne, uneColonne, 1, Options.ESPACE_INTER_CASE, false);
    }

    public Cellule(int uneLigne, int uneColonne){
        this(TypeCase.VIDE, uneLigne, uneColonne, 1, Options.ESPACE_INTER_CASE, false);
    }
    
    public void dessiner(Graphics g, double agrandissement) {
        // On dessine le polygone
        Graphics2D g2d = (Graphics2D) g;
        //Graphics2D g2d = (Graphics2D) g.create();
        
        if (!estDecouverte)
            g2d.setColor(obtenirCouleur(TypeCase.VIDE));
        else
            g2d.setColor(couleur);

        final int[] CENTRE_CASE = {(int)(xpoints[0] + largeur/4), (int)(ypoints[0] + hauteur/2)};
        if (sourisDessus || agrandissement != 1) {
            double facteurDeTaille = Options.RATIO_TAILLE_SELECTION;
            if (agrandissement != 1)
                facteurDeTaille = agrandissement;

            
            final AffineTransform ANCIENNE_TRANSFORMATION = g2d.getTransform();
            final double[] CENTRE = {xpoints[0]+largeur/4, ypoints[0]+hauteur/2};
            final double[] COIN_EN_HAUT_A_GAUCHE = {xpoints[0]-largeur/4, ypoints[0]};
            final double DELTA_LARGEUR = largeur*(facteurDeTaille-1)/2.;
            final double DELTA_HAUTEUR = hauteur*(facteurDeTaille-1)/2.;

            translate((int)(-COIN_EN_HAUT_A_GAUCHE[0]), (int)(-COIN_EN_HAUT_A_GAUCHE[1]));
            AffineTransform at = g2d.getTransform();
            at.translate(COIN_EN_HAUT_A_GAUCHE[0]-DELTA_LARGEUR,COIN_EN_HAUT_A_GAUCHE[1]-DELTA_HAUTEUR);
            at.scale(facteurDeTaille, facteurDeTaille);
            g2d.setTransform(at);

            g2d.fillPolygon(this);
            translate((int)(COIN_EN_HAUT_A_GAUCHE[0]), (int)(COIN_EN_HAUT_A_GAUCHE[1]));
            g2d.setTransform(ANCIENNE_TRANSFORMATION);

            if (symbole != null && symbole.obtenirImage() != null && estDecouverte) {
                Image img = symbole.obtenirImage();
                if (estDecouverte)
                    img = null;
                Image imageAgrandie;
                if (symbole.estVisible)
                    imageAgrandie = TailleImage.resizeImage(img, (int)(img.getWidth(null)*taille*facteurDeTaille), (int)(img.getHeight(null)*taille*facteurDeTaille), true);
                else
                    imageAgrandie = TailleImage.resizeImage(img, (int)(img.getWidth(null)*taille*facteurDeTaille), (int)(img.getHeight(null)*taille*facteurDeTaille), true);
                g2d.drawImage(imageAgrandie, (int)(CENTRE_CASE[0] - imageAgrandie.getWidth(null)/2), (int)(CENTRE_CASE[1] - imageAgrandie.getHeight(null)/2), null);
                /*
                g2d.drawImage(  img,
                                (int)(CENTRE_CASE[0] - img.getWidth(null)*agrandissement*taille/2),
                                (int)(CENTRE_CASE[1] - img.getHeight(null)*agrandissement*taille/2),
                                (int)(CENTRE_CASE[0] + img.getWidth(null)*agrandissement*taille/2),
                                (int)(CENTRE_CASE[1] + img.getHeight(null)*agrandissement*taille/2),

                                (int)(CENTRE_CASE[0] - img.getWidth(null)/2),
                                (int)(CENTRE_CASE[1] - img.getHeight(null)/2)•,
                                (int)(CENTRE_CASE[0] + img.getWidth(null)/2),
                                (int)(CENTRE_CASE[1] + img.getHeight(null)/2),
                                null);
                */
            }
        } else {
            g2d.fillPolygon(this);
            g2d.setStroke(new BasicStroke(8));
            g2d.setPaint(Color.BLUE);
            if (selectionne)
                g2d.drawPolygon(this);
            if (symbole != null && symbole.obtenirImage() != null && estDecouverte) {
                Image img = symbole.obtenirImage();
                if (estDecouverte)
                    img = null;
                if (taille == 1)
                    g2d.drawImage(symbole.obtenirImage(), (int)(CENTRE_CASE[0] - symbole.obtenirImage().getWidth(null)/2), (int)(CENTRE_CASE[1] - symbole.obtenirImage().getHeight(null)/2), null);
                else {
                    Image imageAgrandie = TailleImage.resizeImage(symbole.obtenirImage(), (int)(symbole.obtenirImage().getWidth(null)*taille), (int)(symbole.obtenirImage().getHeight(null)*taille), true);
                    g2d.drawImage(imageAgrandie, (int)(CENTRE_CASE[0] - imageAgrandie.getWidth(null)/2), (int)(CENTRE_CASE[1] - imageAgrandie.getHeight(null)/2), null);
                    /*
                    g2d.drawImage(  symbole.obtenirImage(),
                                    (int)(CENTRE_CASE[0] - symbole.obtenirImage().getWidth(null)*taille/2),
                                    (int)(CENTRE_CASE[1] - symbole.obtenirImage().getHeight(null)*taille/2),
                                    (int)(CENTRE_CASE[0] + symbole.obtenirImage().getWidth(null)*taille/2),
                                    (int)(CENTRE_CASE[1] + symbole.obtenirImage().getHeight(null)*taille/2),

                                    (int)(CENTRE_CASE[0] - symbole.obtenirImage().getWidth(null)/2),
                                    (int)(CENTRE_CASE[1] - symbole.obtenirImage().getHeight(null)/2),
                                    (int)(CENTRE_CASE[0] + symbole.obtenirImage().getWidth(null)/2),
                                    (int)(CENTRE_CASE[1] + symbole.obtenirImage().getHeight(null)/2),
                                    null);
                    */
                }
            }
        }
    }

    public void dessiner(Graphics g) {
        dessiner(g, 1);
    }
    
    //////////////////////////////////////////////////////////////////// Méthodes
    
    public boolean memeType(Cellule uneCellule){
        return (this.type==uneCellule.type);
    }
    
    public Color obtenirCouleur() {
        return obtenirCouleur(type);
    }

    public Color obtenirCouleur(TypeCase type) {

        switch(type){
            case VIDE:
                return new Color(44, 62, 80);   // noir
            case EAU:
                return new Color(52, 152, 219); // bleu
            case MONTAGNE:
                return new Color(189, 195, 199);// gris
            case DESERT:
                return new Color(241, 196, 15); // jaune
            case SABLE_MOUVANTS :
                return new Color(230, 126, 34); // orange
            case NEIGE:
                return new Color(236, 240, 241);// blanc
            case FORET:
                return new Color(46, 204, 113); //vert
            default:
                return null;
        }
    }

    public int[] obtenirPositionTableau(){
        return (new int[]{ligne,colonne});
    }

	public boolean estVisible(double largeurEcran, double hauteurEcran, double zoom) {
		Rectangle b = getBounds();
        final int MOITIE_DIFF_Y = (int) ( hauteurEcran*( 1/zoom)/2 );
        final int MOITIE_DIFF_X = (int) ( largeurEcran*( 1/zoom)/2 );
        // Si la case est visible à l'écran:
        if ( ((b.x + largeur) > - MOITIE_DIFF_X && (b.x - largeur) < MOITIE_DIFF_X)
                && ((b.y + hauteur) > - MOITIE_DIFF_Y && (b.y - hauteur) < MOITIE_DIFF_Y))
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
        couleur = this.obtenirCouleur();
    }
    
    // Renvoie les coordonnées du centre de la cellule et sa position dans la matrice de cellule
    public int[] obtenirCentre() {
        return new int[] {(int)(xpoints[0]+largeur/4),(int)(ypoints[0]+largeur/2), ligne, colonne};
    }
    // Renvoie la cellule d'une matrice de cellule qui contient les coordonnées (x,y)
    public Cellule obtenirCellule(int x, int y, Cellule[][] mat ){
        Cellule c = null;
        for(int i=0; i<mat.length; i++){
            for(int j=0; j<mat[i].length; j++){
                if( mat[i][j].contains(x,y) ){
                    c = mat[i][j];
                }
            }
        }
        return c;
    }

    public String toString() {
        if (symbole != null)
            return type.name()+";"+symbole.type+";"+estDecouverte+";"+symbole.estVisible;
        else
            return type.name()+";null;"+estDecouverte+";null";
    }

    public boolean estDecouverte() {
        return estDecouverte;
    }
    public void majEstDecouverte(boolean estDecouverte) {
        this.estDecouverte = estDecouverte;
    }

    public void majEstIdentifie(boolean estIdentifie) {
        symbole.majEstVisible(estIdentifie);
    }

    public Symbole obtenirSymbole() {
        if (symbole == null)
            symbole = new Symbole();
        return symbole;
    }

    public void majSelectionne(boolean selectionne) {
        this.selectionne = selectionne;
    }
}
