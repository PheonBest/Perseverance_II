import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class Dessiner extends JPanel {
    private Cellule[][] cellules = {{}};
    private double largeurEcran;
    private double hauteurEcran;
    private Robot joueur;
    public Dessiner() {
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        for (int i=0; i < cellules.length; i++) {
            for (Cellule c: cellules[i]) {
                if (c.estVisible(largeurEcran,hauteurEcran))
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

}
