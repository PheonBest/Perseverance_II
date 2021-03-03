package app.carte;
import java.awt.Polygon;
import java.awt.Color;

// Voir la javadoc sur la classe Polygon

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

    ///////////////////////////////////////// Attributs 
    
    TypeCase type;
    Color Couleur;
    
    // Position dans la matrice 
    int ligne;
    int colonne;
     
    
    ////////////////////////////////////////// Constructeurs 
    
    // Constructeur de base
    
    public Cellule(){
        
        type = TypeCase.VIDE;
        ligne = 0;
        colonne = 0;
        Couleur = this.getColor(this.type);
    }
    
    /* Constructeur complet
     * On va calculer les coordonées de chacun des points du polygône et les 
     * stocker dans des tableaux x et y, en fonction de leurs ligne et de leur colonne*/
    
    
    
    
    /////////////////////////////////////////// Méthodes
    
    public Color getColor(TypeCase unTypedeCase){
        switch(unTypedeCase){
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
}
