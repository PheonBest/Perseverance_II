package app.carte;
import java.awt.Color;

public class Cellule{
    
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
    Color Couleur;
    
    //Position dans la matrice de la carte            
    int ligne;
    int colonne;
     
    //////////////////////////////////////////////////////////////////// Constructeurs 
    
    // Constructeur de base
    
    public Cellule(){
        type = TypeCase.VIDE;
        ligne = 0;
        colonne = 0;
        Couleur = this.getColor(this);
    }
    
    // Constructeur complet
     
    public Cellule(TypeCase unType, int uneLigne, int uneColonne){
        type = unType;
        ligne = uneLigne;
        colonne = uneColonne;
        Couleur = this.getColor(this);
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
}
