package app.avatar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JLabel;
import java.awt.Image;
import java.awt.Dimension;

import app.Options;

public class Robot extends Avatar {
    
    //------------------------------------------------------------------ Attributs
    
    // Paramètres vitaux du robots
    private double sante;
    private double batterie;
    
    // Trajectoire
    private LinkedList<Dimension> but = new LinkedList<Dimension>();
    
    //  Position du robot dans la matrice de la carte
    private int row = 0;
    private int column = 0;
    
    // Position du robot sur la carte
    private int xFictif = 0;
    private int yFictif = 0;
    
    //------------------------------------------------------------------ Constructeur
    
    public Robot(int sante, int batterie, int animationIndex, int dureeImage, ArrayList<ArrayList<Image>> image, int[] coords, int x, int y, double r, int dx, int dy, double dr) {
        // Le robot est un avatar, il hérite donc de son constructeur et de ses conditions d'avatar
        super(animationIndex, dureeImage, image, coords, x, y, r, dx, dy, dr);
        xFictif = x;
        yFictif = y;
        this.sante = sante;
        this.batterie = batterie;
    }
    public Robot(ArrayList<ArrayList<Image>> image, int x, int y) {
        this(Options.SANTE_MAX, Options.BATTERIE_MAX, 0, Options.JOUEUR_DUREE_ANIMATION, image, new int[] {0,0}, x, y, .0, 0, 0, .0);
    }

    public void definirBut(LinkedList<Dimension> but) {
        this.but = but;
        definirDirection(but.getFirst());
        animationIndex = 2;
    }

    private void definirDirection(Dimension but) {
        float angle = (float) Math.atan2(but.getHeight() - yFictif, but.getWidth() - xFictif);
        //System.out.println(Math.toDegrees(angle));
        dx = (int) (Math.cos(angle)*10); // 10 est la vitesse (pixel/itération de la boucle du jeu)
        dy = (int) (Math.sin(angle)*10);
        animationIndex = 2; // Marcher
    }

    @Override
    public void updateCoords() {

        r += dr;

        if (!but.isEmpty()) {
            xFictif += dx;
            yFictif += dy;
            Dimension d = but.getFirst(); // On va jusqu'au centre de l'hexagone
            if (Math.abs(xFictif - d.getWidth()) < Options.JOUERUR_TOLERANCE_DEPLACEMENT)
                dx = (int) d.getWidth() - xFictif;
            if (Math.abs(yFictif - d.getHeight()) < Options.JOUERUR_TOLERANCE_DEPLACEMENT)
                dy = (int) d.getHeight() - yFictif;
            if (dx == 0 && dy == 0) {
                but.removeFirst();
                if (but.isEmpty()) // Si il ne reste plus de cases à parcourir
                    animationIndex = 0; // On passe l'animation du joueur en mode "pause" (Idle)
                else {
                    definirDirection(but.getFirst());
                    //System.out.println("Coords joueur : "+xFictif +" _ "+ yFictif);
                    //System.out.println("Coords but : "+ but.getFirst().getWidth() +" _ "+ but.getFirst().getHeight());
                }
                
            }
        }
    }
}
