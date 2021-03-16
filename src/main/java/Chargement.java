import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class Chargement extends JPanel {
    private JProgressBar barreChargement;
    public Chargement() {
        barreChargement = new JProgressBar(0, 100);
        barreChargement.setValue(0);
        barreChargement.setStringPainted(true);

        this.setLayout(null);
        // On fixe la position et les coordonnées
        // des composants avec des valeurs en pixels
        
        this.add(barreChargement);
        
    }

    public void majChargement(int avancement) {
        barreChargement.setValue(avancement);
    }

    public void majTailleBar(double xCenter, double yCenter, double longueur, double largeur) {
        barreChargement.setBounds((int)(xCenter-longueur/2.), (int)(yCenter-largeur/2.), (int)longueur, (int)largeur);
        this.revalidate();
        this.repaint();
    }
}