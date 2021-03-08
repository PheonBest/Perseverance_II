package app.sprites;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import java.awt.Image;
import java.awt.Dimension;

import app.Options;

public class Joueur extends SpriteBase {
    
    private double sante;
    private double batterie;
    private Dimension but;
    private int row = 0;
    private int column = 0;
    private int xFictif = 0;
    private int yFictif = 0;
    
    public Joueur(int sante, int batterie, int animationIndex, int dureeImage, ArrayList<ArrayList<Image>> image, int[] coords, int x, int y, double r, int dx, int dy, double dr) {
        super(animationIndex, dureeImage, image, coords, x, y, r, dx, dy, dr);
        xFictif = x;
        yFictif = y;
        this.sante = sante;
        this.batterie = batterie;
    }
    public Joueur(ArrayList<ArrayList<Image>> image, int x, int y) {
        this(Options.SANTE_MAX, Options.BATTERIE_MAX, 0, Options.JOUEUR_DUREE_ANIMATION, image, new int[] {0,0}, x, y, .0, 0, 0, .0);
    }

    public void definirBut(Dimension but) {
        this.but = but;
        float angle = (float) Math.atan2(but.getHeight() - y, but.getWidth() - x);
        if(angle < 0){
            angle += 360;
        }
        dx = (int) (-Math.cos(angle)*10);
        dy = (int) (-Math.cos(angle)*10);
    }

    @Override
    public void updateCoords() {

        r += dr;

        if (but != null) {
            xFictif += dx;
            yFictif += dy;
            
            if (Math.abs(xFictif - but.getWidth()) < Options.JOUERUR_TOLERANCE_DEPLACEMENT)
                dx = (int) but.getWidth() - xFictif;
            if (Math.abs(yFictif - but.getHeight()) < Options.JOUERUR_TOLERANCE_DEPLACEMENT)
                dy = (int) but.getHeight() - yFictif;
            if (dx == dy && dy == 0) {
                but = null;
            }
        }
    }
}
