package app.carte;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;

import app.Options;

public class Cellule extends Polygon {
    
    public enum TypeCase{
        VIDE,
        EAU, 
        MONTAGNE,
        DESERT,
        SABLE_MOUVANTS,
        NEIGE,
        FORET
    }

    //////////////////////////////////////////////////////////////////// Attributs 
    
    TypeCase type;
    Color couleur;
    
    //Position dans la matrice de la carte            
    int ligne;
    int colonne;
     
    //////////////////////////////////////////////////////////////////// Constructeurs 
    
    // Constructeur de base
    
    public Cellule(int uneLigne, int uneColonne){
        type = TypeCase.VIDE;
        //Position dans la matrice de la carte
        ligne = uneLigne;
        colonne = uneColonne;
        couleur = this.getColor(this);

        // On créée les points du polygone
        double[][] points = {
            {Options.LARGEUR_CASE/4,0},
            {3*Options.LARGEUR_CASE/4,0},
            {Options.LARGEUR_CASE,Options.HAUTEUR_CASE/2},
            {3*Options.LARGEUR_CASE/4,Options.HAUTEUR_CASE},
            {Options.LARGEUR_CASE/4,Options.HAUTEUR_CASE},
            {0,Options.HAUTEUR_CASE/2}
        };
        for (int i=0; i < points.length; i++) {
            addPoint((int)points[i][0],(int)points[i][1]);
        }

        // On place le polygone
        double xInit = -Options.LARGEUR_CASE/2;
        double yInit = -Options.HAUTEUR_CASE/2;

        // On décale verticalement les cases hexagonales
        double compensationY = 0;
        if (colonne %2 == 0)
            compensationY = Options.HAUTEUR_CASE/2;
        
        // Calcul des coordonnées
        // Pour que 2 hexagones se touches, on enlève à x un quart de la largeur d'une case
        // Donc on multiplie LARGEUR_CASE par 3/4 = 0.75
        int xCoord = (int)(xInit + colonne*(0.75*Options.LARGEUR_CASE+Options.ESPACE_INTER_CASE));
        int yCoord = (int)(yInit + compensationY + ligne*(Options.HAUTEUR_CASE+Options.ESPACE_INTER_CASE));

        // Déplacement du polygone
        translate(xCoord, yCoord);

        // Déplacement du symbole
    }
    
    public void dessiner(Graphics2D gg) {
        // On dessine le polygone
        gg.setColor(couleur);
        gg.fillPolygon(this);
    }
    /* Constructeur complet
     * On va calculer les coordonées de chacun des points du polygône et les 
     * stocker dans des tableaux x et y, en fonction de leurs ligne et de leur colonn
     * */
    public Cellule(TypeCase unType, int uneLigne, int uneColonne){
        type = unType;
        ligne = uneLigne;
        colonne = uneColonne;
        couleur = this.getColor(this);
    }
    
    //////////////////////////////////////////////////////////////////// Méthodes
    
    public boolean equals(Cellule uneCellule){
        return (this.type==uneCellule.type 
                && this.ligne==uneCellule.ligne 
                && this.colonne==uneCellule.colonne);
    }
    
    public boolean memeType(Cellule uneCellule){
        return (this.type==uneCellule.type);
    }
    
    public Color getColor(Cellule uneCellule){
        switch(uneCellule.type){
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
        if (    (b.x + Options.LARGEUR_CASE > 0 && b.x - Options.LARGEUR_CASE < largeurEcran)
                && (b.y + Options.HAUTEUR_CASE > 0 && b.y - Options.HAUTEUR_CASE < hauteurEcran))
            return true;
        // Sinon, la case n'est pas visible
        return false;
	}
}
