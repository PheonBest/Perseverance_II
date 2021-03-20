import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

public class Dessiner extends JPanel {
    private Cellule[][] cellules = {{}};
    private double largeurEcran;
    private double hauteurEcran;
    private Robot joueur;
    private double zoom = 1.;
    private Point centreZoom = new Point(0,0);

    public Dessiner() {
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        AffineTransform at = ((Graphics2D) g).getTransform();
        // Application du zoom centré autour d'un point p:
        // 1. On veut que le point p soit la nouvelle origine
        //    Donc on translate le coin en haut à gauche vers le centre
        // 2. On aggrandit/rétrécit.

        at.translate(
            (largeurEcran/2),
            (hauteurEcran/2)
        );
        
        /*
        at.translate(
            (centreZoom.getX()),
            (centreZoom.getY())
        );
        */
        at.scale(zoom, zoom);
        
        ((Graphics2D) g).setTransform(at);
        for (int i=0; i < cellules.length; i++) {
            for (Cellule c: cellules[i]) {
                //if (c.estVisible(largeurEcran,hauteurEcran,zoom))
                    c.dessiner(g);
            }
        }
        if (joueur != null)
            joueur.dessiner(g);
    }
    public void majCellules(Cellule[][] cellules) {
        this.cellules = cellules;
    }

    public void majJoueur(Robot nouveau) {
        this.joueur = nouveau;
    }

    public void majLargeur(double largeurEcran) {
        this.largeurEcran = largeurEcran;
    }

    public void majHauteur(double hauteurEcran) {
        this.hauteurEcran = hauteurEcran;
    }

    public void majZoom(double zoom) {
        this.zoom = zoom;
    }

    public void majCentreZoom(Point centreZoom) {
        this.centreZoom = centreZoom;
    }

}
