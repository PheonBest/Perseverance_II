package app;

import javax.swing.SwingUtilities;

import app.controleur.Controleur;
import app.donnees.Donnees;
import app.vue.Affichage;

import java.awt.Dimension;
import java.awt.Toolkit;

public class App 
{
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Dimension tailleEcran = Toolkit.getDefaultToolkit().getScreenSize();
                int largeur = (int) (tailleEcran.getWidth() * 3 / 4);
                int hauteur = (int) (tailleEcran.getHeight() * 3 / 4);

                Donnees donnees = new Donnees();
                Controleur controleur = new Controleur(donnees);
                Affichage affichage = new Affichage(largeur, hauteur, controleur);
                donnees.addObserver(affichage);
            }
        });
    }
}