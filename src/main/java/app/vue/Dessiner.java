package app.vue;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import app.carte.Cellule;
import app.sprites.Joueur;

public class Dessiner extends JPanel {
    private Cellule[][] cellules = {{}};
    private double largeurEcran;
    private double hauteurEcran;
    private Joueur joueur;
    public Dessiner() {
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D gg = (Graphics2D) g.create();
        for (int i=0; i < cellules.length; i++) {
            for (Cellule c: cellules[i]) {
                if (c.estVisible(largeurEcran,hauteurEcran))
                    c.dessiner(gg);
            }
        }
        
        joueur.dessiner(g);
    }
    public void majCellules(Cellule[][] cellules) {
        this.cellules = cellules.clone();
    }
    public void setLargeur(double largeurEcran) {
        this.largeurEcran = largeurEcran;
    }
    public void setHauteur(double hauteurEcran) {
        this.hauteurEcran = hauteurEcran;
    }

    public void majJoueur(Joueur nouveau) {
        this.joueur = nouveau;
    }

}
