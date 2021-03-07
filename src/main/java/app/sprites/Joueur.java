package app.sprites;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import java.awt.Image;

import app.Options;

public class Joueur extends SpriteBase {
    
    private double sante;
    private double batterie;
    
    public Joueur(int sante, int batterie, int animationIndex, int dureeImage, ArrayList<ArrayList<Image>> image, int[] coords, double x, double y, double r, double dx, double dy, double dr) {
        super(animationIndex, dureeImage, image, coords, x, y, r, dx, dy, dr);
        this.sante = sante;
        this.batterie = batterie;
    }
    public Joueur(ArrayList<ArrayList<Image>> image, double x, double y) {
        this(Options.SANTE_MAX, Options.BATTERIE_MAX, 0, Options.JOUEUR_DUREE_ANIMATION, image, new int[] {0,0}, x, y, .0, .0, .0, .0);
    }
}
