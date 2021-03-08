package app.sprites;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import app.utils.MathUtils;

import java.awt.Graphics;
import java.awt.Graphics2D;

public abstract class SpriteBase {
    protected int imageIndex = 0;
    protected int animationIndex = 0;
	protected int dureeImage;
	protected ArrayList<ArrayList<Image>> image = new ArrayList<ArrayList<Image>>();

	protected double x;
	protected double y;
	protected double r; // angle d'orientation

	protected int dx;
	protected int dy;
	protected double dr;

	protected boolean removable = false;

	protected double largeur;
	protected double hauteur;

	protected boolean movable = true;
	protected double[] coords; // Coordonnées de l'image par rapport au centre de rotation

	protected long temps;

    public SpriteBase(int animationIndex, int dureeImage, ArrayList<ArrayList<Image>> image, int[] coords, double x, double y, double r, int dx, int dy, double dr) {
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

	public double getR() {
		return r;
	}

	public void setR(double r) {
		this.r = r;
	}

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

	public double getDr() {
		return dr;
	}

	public void setDr(double dr) {
		this.dr = dr;
	}

    public boolean isRemovable() {
		return removable;
	}

	public void setRemovable(boolean removable) {
		this.removable = removable;
	}

	public void updateCoords() {
		x += dx;
		y += dy;
		r += dr;
	}

    public void move() {

		if(!movable)
			return;
		
		updateCoords();

	}
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

		coords[0] -= rotateAroundPoint.getWidth();
		coords[1] -= rotateAroundPoint.getHeight();
		double diff = Math.toRadians(MathUtils.normalRelativeAngleDeg(dr));
		double new0 = coords[0] * Math.cos(diff) - coords[1] * Math.sin(diff);
		double new1 = coords[0] * Math.sin(diff) + coords[1] * Math.cos(diff);
		coords[0] = new0 + rotateAroundPoint.getWidth();
		coords[1] = new1 + rotateAroundPoint.getHeight();
	}

    public void rafraichirImage() {
        if (image.get(animationIndex).size() > 1 && System.currentTimeMillis() - temps > dureeImage) {
			temps = System.currentTimeMillis();
			imageIndex = (imageIndex + 1) % image.get(animationIndex).size();
		}
    }

    public void setAnimationIndex(int animationIndex) {
        if (animationIndex > 0 && animationIndex < image.size())
            this.animationIndex = animationIndex;
    }
    public void dessiner(Graphics g) {
        Graphics2D gg = (Graphics2D) g;
        gg.translate((int)(x + coords[0]), (int)(y + coords[1]));
        gg.rotate(r);
        gg.translate(-largeur/2,-hauteur/2);
        gg.drawImage(image.get(animationIndex).get(imageIndex), 0, 0, null);
    }
}
