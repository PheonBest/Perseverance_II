import java.awt.*;

public class Voyants{
    /* Cette classe a pour intérêt de décrire des voyants d'alertes, principalement pour consulter 
     * les paramètres vitaux du robot*/
    
    //------------------------------------------------------------------ Attributs
    public String nom;
    
    // On chosit de définir 3 états : bleu, orange et rouge
    // chaque état a sa propre couleur
    private int etat;
    private Color couleur;
    
    // position DU CENTRE 
    public int x;
    public int y;
    
    // taille ( rayon )
    public int r; 
    
    //------------------------------------------------------------------ Constructeurs
    
    // Constructeur principal
    public Voyants(String unNom, int unEtat, int xv, int yv, int rv){
        this.nom = unNom;
        setEtat(unEtat);
        setPositionR(xv,yv,rv);
    }
    
    // Constructeur de voyants sans positions
    public Voyants(String unNom, int unEtat){
        this.nom = unNom;
        setEtat(unEtat);
        setPositionR(0,0,0);
    }
    // Constructeur de voyants sans positions ni états
    public Voyants(String unNom){
        this.nom = unNom;
        setEtat(Options.ALERTE_MIN);
        setPositionR(0,0,0);
    }
    //------------------------------------------------------------------ setters et getters
    
    public int getEtat(){
        return this.etat;
    }
    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }
    public int getR(){
        return this.r;
    }
    
    public void setEtat(int unEtat){
        // L'état est un nombre entre 0 et 2
        if(unEtat>=Options.ALERTE_MAX){
            this.etat = Options.ALERTE_MAX;
        }
        else if(unEtat<=Options.ALERTE_MIN){
            this.etat = Options.ALERTE_MIN;
        }
        else this.etat = unEtat;
        // La couleur est définie selon l'état
        // l'état est un niveau d'alerte : 0=ok 2=gros problème
        switch(this.etat){
            case 0 : 
                this.couleur = new Color(0,200,200);
                break;
            case 1 : 
                this.couleur = Color.orange;
                break;
            case 2 :
                this.couleur = Color.red;
                break;
        }
    }
    public void setX(int unX){
        this.x = unX;
    }
    public void setY(int unY){
        this.y = unY;
    }
    public void setR(int unR){
        this.r = unR;
    }
    public void setPosition(int unX, int unY){
        this.setX(unX);
        this.setY(unY);
    }
    public void setPositionR(int unX, int unY, int unR){
        this.setPosition(unX,unY);
        this.setR(unR);
    }
    
    //------------------------------------------------------------------ Autres méthodes
    public void dessinerVoyant(Graphics g){
        g.setColor(Color.black);
        g.drawString(nom,x-(int)(nom.length()*6/2.0),y-r-5);
        g.setColor(Color.black);
        g.fillOval(x-r,y-r,2*r,2*r);
        g.setColor(Color.white);
        g.fillOval(x-(int)(0.9*r),y-(int)(0.9*r),(int)(2*0.9*r),(int)(2*0.9*r));
        g.setColor(Color.black);
        g.fillOval(x-(int)(0.75*r),y-(int)(0.75*r),(int)(2*0.75*r),(int)(2*0.75*r));
        g.setColor(couleur);
        g.fillOval(x-(int)(0.5*r),y-(int)(0.5*r),(int)(2*0.5*r),(int)(2*0.5*r));
    }
        

}
