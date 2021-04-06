import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class Chargement extends JPanel {
    private JProgressBar barreChargement;
    public Chargement() {
        barreChargement = new JProgressBar(0, 100);
        barreChargement.setValue(0);
        barreChargement.setPreferredSize(new Dimension(100,100));

        barreChargement.setStringPainted(true);

        this.setLayout(null);
        // On fixe la position et les coordonnées
        // des composants avec des valeurs en pixels
        
        this.add(barreChargement);
        
    }

    public void majChargement(int avancement) {
        barreChargement.setValue(avancement);
    }

    public void majTailleBar(double xCenter, double yCenter, double largeur, double hauteur) {
        barreChargement.setBounds((int)(xCenter-largeur/2.), (int)(yCenter-largeur/2.), (int)largeur, (int)hauteur);
        //barreChargement.setPreferredSize(new Dimension((int)largeur,(int)60));
        //barreChargement.setMinimumSize(new Dimension((int)largeur,(int)60));
        this.revalidate();
        this.repaint();
    }
}