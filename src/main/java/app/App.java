package app;

import javax.swing.SwingUtilities;

import app.controleur.Controleur;
import app.donnees.Donnees;
import app.vue.Affichage;

import java.awt.Dimension;
import java.awt.Toolkit;

public class App 
{
    /* On utuilise un EDT: Event Dispatch Thread
     * La plupart des méthodes des objets Swing ne sont pas "thread safe"
     * Si on les invoque depuis plusieurs threads, on risque d'avoir
     * des interférences de thread.h
     * 
     * Une partie des tâches sont événementielles (Actionlistener)
     * Une autre partie est planifiée (repaint, invokeLater, invokeAndWait)
     * Si les tâches à distirbuer ne son pas prises en charge,
     * les événements non gérés sont sauvegardés et l'interface
     * Utilisateur ne répond plus.
     */
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Dimension tailleEcran = Toolkit.getDefaultToolkit().getScreenSize();
                int largeur = (int) (tailleEcran.getWidth() * 3 / 4);
                int hauteur = (int) (tailleEcran.getHeight() * 3 / 4);

                Donnees donnees = new Donnees(largeur, hauteur);
                Controleur controleur = new Controleur(donnees);
                Affichage affichage = new Affichage(largeur, hauteur, controleur);
                donnees.addObserver(affichage);
            }
        });
    }
}
