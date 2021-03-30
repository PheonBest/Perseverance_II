// Cette classe a pour but de définir ce qu'est un avatar classique. C'est la classe mère de celle de joueur

import java.awt.Dimension;

//import utils.MathUtils;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;

public abstract class Avatar implements Dessin {
    
    // ----------------------------------------------------------------- Attributs
    
    // Gestion de l'image de l'avatar
    protected int imageIndex = 0;
    protected int animationIndex = 0;
	protected int dureeImage;
	protected ArrayList<ArrayList<Image>> image = new ArrayList<ArrayList<Image>>();

    // Position de l'avatar
	protected double x; // Coordonnée x
	protected double y; // Coordonnée y
	protected double r; // angle d'orientation

    // Vitesse de déplacement de l'avatar
	protected int dx; // Vitesse du joueur en pixels/itération de la boucle dans la direction x
	protected int dy; // Vitesse du joueur en pixels/itération de la boucle dans la direction y
	protected double dr; // Angle de rotation (inutile pour le robot, mais utile pour les autres potentiels Avatars (caractères animés))

    
	protected boolean removable = false; // Condition d'existence de l'avatar
	protected boolean movable = true; // Autorisation de  déplacement de l'avatar
    
    // Définition de la taille de l'avatar
	protected double largeur;
	protected double hauteur;

    //****************************************************************** Coordonnées de l'image par rapport au centre de rotation - à revoir ...
    protected double[] coords;

    // Variable de temps surlaquelle évolue l'avatar
	protected long temps;
    
    // ----------------------------------------------------------------- Constructeur 

    public Avatar(int animationIndex, int dureeImage, ArrayList<ArrayList<Image>> image, int[] coords, double x, double y, double r, int dx, int dy, double dr) {
        this.image = image;
        this.x = x;
		this.y = y;
		this.r = r;
		this.dx = dx;
		this.dy = dy;
		this.dr = dr;

		this.dureeImage = dureeImage;
		this.temps = System.currentTimeMillis();
        
        this.coords = new double[coords.length];
		for(int i=0; i<coords.length; i++)
		    this.coords[i] = coords[i];

        this.largeur = image.get(animationIndex).get(imageIndex).getWidth(null);
        this.hauteur = image.get(animationIndex).get(imageIndex).getHeight(null);
    }
    // ----------------------------------------------------------------- Méthodes - set() et get()
    
    public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

/*	public double getR() {
		return r;
	}

	public void setR(double r) {
		this.r = r;
	}
*/
	public int getDx() {
		return dx;
	}

	public void setDx(int dx) {
		this.dx = dx;
	}

	public int getDy() {
		return dy;
	}

	public void setDy(int dy) {
		this.dy = dy;
	}
/*
	public double getDr() {
		return dr;
	}

	public void setDr(double dr) {
		this.dr = dr;
	}
*/
	public void setRemovable(boolean removable) {
		this.removable = removable;
	}
    // ----------------------------------------------------------------- Autres méthodes
    
    public boolean isRemovable() {
		return removable;
	}
    
    // Met à jour les coordonnées de l'avatar
	public void updateCoords() {
		x += dx;
		y += dy;
//		r += dr;
	}
    // Permet de déplacer l'avatar si il en a le droit
    public void move() {

		if(!movable)
			return;
		
		updateCoords();
	}
    
//****************************************************************** A revoir 
    
	public void move(Dimension rotateAroundPoint) {
		if(!movable)
			return;
			
		updateCoords();

        /*
		//Pour tourner un point "p" autour d'une origine "c",
		//on soustrait c à p, puis on tourne p et on ajoute c
		Circle circle = new Circle(); 
		circle.setCenterX(x+coords[0]);
		circle.setCenterY(y+coords[1]);
		circle.setRadius(10.0f);
		layer.getChildren().add(circle);
        */
/*
		coords[0] -= rotateAroundPoint.getWidth();
		coords[1] -= rotateAroundPoint.getHeight();
		//double diff = Math.toRadians(MathUtils.normalRelativeAngleDeg(dr));
		double new0 = coords[0] * Math.cos(diff) - coords[1] * Math.sin(diff);
		double new1 = coords[0] * Math.sin(diff) + coords[1] * Math.cos(diff);
		coords[0] = new0 + rotateAroundPoint.getWidth();
		coords[1] = new1 + rotateAroundPoint.getHeight();
*/
	}
    
    // Permet de faire "vivre" l'avatar à l'aide d'une série d'images animées
    public void rafraichirImage() {
        if (image.get(animationIndex).size() > 1 && System.currentTimeMillis() - temps > dureeImage) {
			temps = System.currentTimeMillis();
			imageIndex = (imageIndex + 1) % image.get(animationIndex).size();
		}
    }
    // Initialise l'index de l'image initiale voulue dans la liste d'images de l'avatar
    public void setAnimationIndex(int animationIndex) {
        if (animationIndex > 0 && animationIndex < image.size())
            this.animationIndex = animationIndex;
    }
    // Permet de dessiner l'avatar
    public void dessiner(Graphics g) {
        Graphics2D gg = (Graphics2D) g;
        gg.translate((int)(x + coords[0]), (int)(y + coords[1]));
//gg.rotate(r);
        gg.translate(-largeur/2,-hauteur*2./3.);
        gg.drawImage(image.get(animationIndex).get(imageIndex), 0, 0, null);
    }
}
