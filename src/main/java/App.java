import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.SwingUtilities;

public class App 
{
    /* On utuilise un EDT: Event Dispatch Thread
     * La plupart des méthodes des objets Swing ne sont pas "thread safe"
     * Si on les invoque depuis plusieurs threads, on risque d'avoir
     * des interférences de thread.h
     * 
     * Une partie des tâches sont événementielles (Actionlistener)
     * Une autre partie est planifiée (repaint, invokeLater, invokeAndWait)
     * Si les tâches à distribuer ne son pas prises en charge,
     * les événements non gérés sont sauvegardés et l'interface
     * Utilisateur ne répond plus.
     */
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Dimension tailleEcran = Toolkit.getDefaultToolkit().getScreenSize();

                // Mode:  Partie de l'écran
                // int largeur = (int) (tailleEcran.getWidth() * 3 / 4);
                // int hauteur = (int) (tailleEcran.getHeight() * 3 / 4);

                // Mode: Fullscreen
             
                int largeur = (int) tailleEcran.getWidth();
                int hauteur = (int) tailleEcran.getHeight();

                Donnees donnees = new Donnees();
                Controleur controleur = new Controleur(donnees);
                Affichage affichage = new Affichage(largeur, hauteur, controleur);
                donnees.ajouterObservateur(affichage);
                controleur.charger();
            }
        });
    }
    
}
