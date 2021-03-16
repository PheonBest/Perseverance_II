package app;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import javax.swing.Timer;

import app.Controleur;
import app.TypeMisAJour;
import app.Options;
import app.Cellule;
import app.Robot;
import app.Observer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Affichage extends JFrame implements Observer, ActionListener, KeyListener {
    private Controleur controleur;
    private CardLayout cardLayout = new CardLayout();
    private boolean enJeu = false;

    private JPanel contenu = new JPanel();
    private JPanel jeu = new Dessiner();
    private JPanel modeDeJeu = new JPanel();
    private JPanel chargement = new Chargement();
    private JPanel editeur = new Editeur();

    private double largeur;
    private double hauteur;

    private Timer timer;

    public Affichage(int largeur, int hauteur, Controleur controleur) {
        this.controleur = controleur;
        super.setFocusable(true);
        this.addKeyListener(this);
        contenu.addKeyListener(this);
        contenu.setLayout(cardLayout);
        this.setTitle("Perseverance II");
        this.setSize(largeur, hauteur);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        contenu.setLayout(cardLayout);
        contenu.add(modeDeJeu, "Mode de Jeu");
        contenu.add(jeu, "Jeu");
        contenu.add(chargement, "Chargement");
        contenu.add(editeur, "Editeur");
        jeu.setBackground(Color.DARK_GRAY);

        this.setContentPane(contenu); // On définit le contenu de "JFrame" comme un "JPanel"
        this.setVisible(true);

        // On définit la largeur et la hauteur du contenu du "JFrame" après l'avoir
        // affiché
        // Si on obtient sa taille avant son affichage, la dimension retournée est (0,0)
        this.largeur = this.getContentPane().getSize().getWidth();
        this.hauteur = this.getContentPane().getSize().getHeight();
        ((Dessiner)jeu).majLargeur(this.largeur);
        ((Dessiner)jeu).majHauteur(this.hauteur);
        ((Editeur)editeur).initialiser((int)this.largeur, (int)this.hauteur);
        
        ((Chargement)chargement).majTailleBar((int)(this.largeur/2.), (int)(this.hauteur/2.), (int)(this.largeur*2./5.), (int)(this.hauteur/20.));
        initialiser();
    }

    private void initialiser() {
        //System.out.println("Initialisation");

        // Chargement et exécution du jeu
        /*
        cardLayout.show(contenu, "Chargement");
        controleur.charger();
        */
        
        // Exécuter l'éditeur
        cardLayout.show(contenu, "Editeur");
        timer = new Timer(Options.DELAI_ANIMATION, this);
        timer.start();
    }

    private void jouer() {
        enJeu = true;
        // L'animation de rotation de la matrice sera mise à jour
        // Toutes les 15 milisecondes
        timer = new Timer(Options.DELAI_ANIMATION, this);
        timer.start();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        controleur.rafraichir();
    }

    @Override
    public void update(TypeMisAJour type, Object nouveau) {
        switch (type) {
            case Cellules:
                if (enJeu)
                    ((Dessiner)jeu).majCellules((Cellule[][]) nouveau);
                else
                    ((Editeur)editeur).majCellules((Cellule[][]) nouveau);
                break;
            case Joueur:
                ((Dessiner)jeu).majJoueur((Robot) nouveau);
                break;
            case Avancement:
                //System.out.println("A : "+(int) nouveau);
                ((Chargement)chargement).majChargement((int) nouveau);
                break;
            case Scene:
                if (((String) nouveau).equals("Jeu"))
                    jouer();
                cardLayout.show(contenu, (String) nouveau);
                break;
            case Peindre:
                if (enJeu)
                    jeu.repaint();  // Prévoit de mettre à jour le composant
                                    // n'appelle pas la méthode paint() !
                else
                    editeur.repaint();
                break;
        }
        
    }
}
