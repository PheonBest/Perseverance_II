import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.*;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Affichage extends JFrame implements Observateur, ActionListener, KeyListener {
    private Controleur controleur;
    private CardLayout cardLayout = new CardLayout();
    private String scene = "";

    private JPanel contenu = new JPanel();
    private JPanel modeDeJeu;
    private JPanel chargement;
    private JPanel editeur;
    private JPanel jeu;
   // private JPanel options = new JPanel();
	private PanneauPause panneauPause;
	
    private int largeur;
    private int hauteur;

    private Timer timer;

    private MouseAdapter clicJeuMaintenu = new MouseAdapter() {
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            controleur.ajusterZoom(e.getWheelRotation(), e.getPoint());
        }

        @Override
        // On veut mettre à jour la position de la souris
        // Lorsque le clic est maintenu.
        // Cet évènement appelle mouseDragged, et non pas mouseMoved
        public void mouseDragged(MouseEvent ev) {
            controleur.majStatutSouris(ev, true);
        }

        @Override
        public void mousePressed(MouseEvent ev) {
            controleur.majStatutSouris(ev, true);
        }
        
        @Override
        public void mouseReleased(MouseEvent ev) {
            controleur.majStatutSouris(ev, false);
        }
    };
    private MouseAdapter clicJeuSansMaintien = new MouseAdapter() {
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            controleur.ajusterZoom(e.getWheelRotation(), e.getPoint());
        }
        @Override
        public void mousePressed(MouseEvent ev) {
            controleur.click(ev.getX(), ev.getY(), false);
        }
    };
    private MouseAdapter clicMenu = new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent ev) {
            controleur.click(ev.getX(),ev.getY(), true); // True signifie qu'on clique depuis le menu
        }
    };

    public Affichage(int largeur, int hauteur, Controleur controleur) {
        
        this.controleur = controleur;

        // Mode fullscreen
        setUndecorated(true);
        setResizable(false);

        /* Vrai mode fullscreen
        GraphicsEnvironment graphics = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = graphics.getDefaultScreenDevice();
        device.setFullScreenWindow(this);
        */

        // Gestion des évènements
        super.setFocusable(true);
        this.addKeyListener(this);

        contenu.setLayout(cardLayout);

        this.setTitle("Perseverance II");
        this.setSize(largeur, hauteur);
        this.setLocationRelativeTo(null);

       // options.setBounds(100,100,300,300);
       // options.setBackground(Color.RED);
        
        contenu.setLayout(cardLayout);

        this.setContentPane(contenu); // On définit le contenu de "JFrame" comme un "JPanel"
        this.setVisible(true);

        // On définit la largeur et la hauteur du contenu du "JFrame" après l'avoir
        // affiché
        // Si on obtient sa taille avant son affichage, la dimension retournée est (0,0)
        this.largeur = (int) this.getContentPane().getSize().getWidth();
        this.hauteur = (int) this.getContentPane().getSize().getHeight();
        controleur.majLargeur(this.largeur);
        controleur.majHauteur(this.hauteur);


        // Chargement du jeu
        chargement = new Chargement();

        ((Chargement)chargement).majTailleBar((int)(this.largeur/2.), (int)(this.hauteur/2.), (int)(this.largeur*2./5.), 50);
        contenu.add(chargement, "Chargement");
        cardLayout.show(contenu, "Chargement");
    }

    public void initialiser() {

        modeDeJeu = new ModeDeJeu(controleur, this.largeur, this.hauteur);
        panneauPause = new PanneauPause(this.largeur, this.hauteur);
        editeur = new Editeur(controleur, this.largeur, this.hauteur);
        jeu = new Dessiner(controleur, true, this.largeur, this.hauteur);
        
        contenu.add(modeDeJeu, "Choix du mode");
        contenu.add(jeu, "Jeu");
        contenu.add(editeur, "Editeur de carte");
        
        jeu.addMouseWheelListener(clicJeuSansMaintien);
        jeu.addMouseListener(clicJeuSansMaintien);
        jeu.addMouseMotionListener(clicJeuSansMaintien);

        ((Editeur) editeur).obtenirPanneauJeu().addMouseWheelListener(clicJeuMaintenu);
        ((Editeur) editeur).obtenirPanneauJeu().addMouseListener(clicJeuMaintenu);
        ((Editeur) editeur).obtenirPanneauJeu().addMouseMotionListener(clicJeuMaintenu);

        ((Editeur) editeur).obtenirPanneauMenu().addMouseWheelListener(clicMenu);
        ((Editeur) editeur).obtenirPanneauMenu().addMouseListener(clicMenu);
        ((Editeur) editeur).obtenirPanneauMenu().addMouseMotionListener(clicMenu);
        
        contenu.addKeyListener(this);
        panneauPause.addKeyListener(this);
        panneauPause.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				controleur.interactionClavier(KeyEvent.VK_ESCAPE);
			}
		});
        
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                if (scene.equals("Jeu"))
                    controleur.enregistrer();
                System.exit(0);
            }
        });
        
        // Ajout du keyListener aux widgets
        for (JComponent j: panneauPause.obtenirComposants())
            j.addKeyListener(this);
        for (JComponent j: ((Dessiner) jeu).obtenirComposants())
            j.addKeyListener(this);

        // Lorsqu'on appuie sur le bouton "Retour" du panneauPause, on simule l'appui sur le clavier de la touche Echap
        ((JButton)(panneauPause.obtenirComposants().get(PanneauPause.RETOUR))).addActionListener(new AbstractAction("Pause") {
            @Override
            public void actionPerformed(ActionEvent e) {
                controleur.interactionClavier(KeyEvent.VK_ESCAPE);
            }
        });
        
        ((JButton)(panneauPause.obtenirComposants().get(PanneauPause.MENU))).addActionListener(new AbstractAction("Menu") {
            @Override
            public void actionPerformed(ActionEvent e) {
                controleur.interactionClavier(KeyEvent.VK_ESCAPE);
                controleur.enregistrer();
                controleur.retourMenu();
            }
        });
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyPressed(KeyEvent e) {
        controleur.interactionClavier(e.getKeyCode());
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
    public void mettreAJour(TypeMisAJour type, Object nouveau) {
        switch (type) {
            //Affichage
            case Initialisation:
                initialiser();
                break;
            //Carte
            case Cellules:
                if (scene.equals("Jeu"))
                    ((Dessiner)jeu).majCellules((Cellule[][]) nouveau);
                else if (scene.equals("Editeur de carte"))
                    ((Editeur)editeur).majCellules((Cellule[][]) nouveau);
                break;
            case Peindre:
                if (scene.equals("Jeu"))
                    jeu.repaint();  // Prévoit de mettre à jour le composant
                                    // n'appelle pas la méthode paint() !
                else if (scene.equals("Editeur de carte"))
                    editeur.repaint();
                break;
            case Avancement:
                ((Chargement)chargement).majChargement((int) nouveau);
                break;
            case Scene:
                scene = (String) nouveau;
                if (((String) nouveau).equals("Jeu")) {
                    // L'animation sera mise à jour
                    // Toutes les 15 milisecondes
                    timer = new Timer(Options.DELAI_ANIMATION, this);
                    timer.start();
                } else if (((String) nouveau).equals("Editeur de carte")) {
                    timer = new Timer(Options.DELAI_ANIMATION, this);
                    timer.start();
                } else if (timer != null && timer.isRunning()) {
                    timer.stop();
                }
                cardLayout.show(contenu, (String) nouveau);
                break;
            case Zoom:
                if (scene.equals("Jeu"))
                    ((Dessiner) jeu).majZoom((Double) nouveau);
                else if (scene.equals("Editeur de carte"))
                    ((Editeur) editeur).majZoom((Double) nouveau);
                break;
            case Cartes:
                ((ModeDeJeu) modeDeJeu).majCartes((HashMap<String, InputStream>) nouveau);
                break;
            case ArrierePlan:
                if (scene.equals("Jeu"))
                    ((Dessiner)jeu).majArrierePlan((ArrierePlan) nouveau);
                else if (scene.equals("Editeur de carte"))
                    ((Editeur)editeur).majArrierePlan((ArrierePlan) nouveau);
                break;
            case InformerJoueur:
                if (scene.equals("Jeu"))
                    ((Dessiner)jeu).majInformerJoueur((String) nouveau);
                break;
                
            // Joueur
            case Joueur:
                ((Dessiner)jeu).majJoueur((Robot) nouveau);
                break;
            case Competences:
                ((Dessiner)jeu).majCompetences((List<BoutonCercle>) nouveau);
                break;
            case RayonDeSelection:
                ((Dessiner)jeu).majRayon((Integer) nouveau);
                break;
            
            // Editeur de carte
            case BoutonsType:
                ((Editeur)editeur).majBoutonsType((Cellule[]) nouveau);
                break;
            case BoutonsCercle:
                ((Editeur)editeur).majBoutonsCercle((BoutonCercle[]) nouveau);
                break;
            case BoutonsSymbole:
                ((Editeur)editeur).majBoutonsSymbole((Cellule[]) nouveau);
                break;
            
            // Mini jeu extraction
            case MinijeuExtraction:
                ((Dessiner)jeu).majEtatMinijeuExtraction((Etat) nouveau);
                break;
            case PositionCurseurExtraction:
                ((Dessiner)jeu).majPositionCurseurExtraction((double) nouveau);
                break;
            case SensVariationExtraction:
                ((Dessiner)jeu).majSensVariationExtraction((boolean) nouveau);
                break;
            case Resultat:
                ((Dessiner)jeu).majResultat((String) nouveau);
                break;
            
            // Mini jeu laser
            case MinijeuLaser:
                ((Dessiner)jeu).majEtatMinijeuLaser((Etat) nouveau);
                break;
            case ChronometreLaser:
                ((Dessiner)jeu).majChronometreMinijeuLaser((String) nouveau);
                break;
            case NombreErreursLaser:
                ((Dessiner)jeu).majNombreErreursLaser((int) nouveau);
                break;
            
            // Menus
            case Options:
                if ((boolean)nouveau)
                   // cardLayout.show(contenu, "Options");
                   panneauPause.setVisible(true);
                else
                    //cardLayout.show(contenu, scene);
                    panneauPause.setVisible(false);
                break;
            case ImageMenu:
                ((ModeDeJeu) modeDeJeu).majImageMenu((Image)nouveau);
                break;
        }
    }
}
