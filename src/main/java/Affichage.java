import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Point;

public class Affichage extends JFrame implements Observer, ActionListener, KeyListener, MouseWheelListener, MouseMotionListener, MouseListener {
    private Controleur controleur;
    private CardLayout cardLayout = new CardLayout();
    private String scene = "";

    private JPanel extraction = new JPanel();
    private JPanel contenu = new JPanel();
    private JPanel modeDeJeu;
    private JPanel chargement = new Chargement();
    private JPanel editeur;
    private Dessiner jeu = new Dessiner(true);

    private double largeur;
    private double hauteur;

    private Timer timer;

    public Affichage(int largeur, int hauteur, Controleur controleur) {
        
        this.controleur = controleur;
        modeDeJeu = new ModeDeJeu(controleur);
        editeur = new Editeur(controleur);
        jeu.setLayout(null);

        
        // Gestion des évènements
        super.setFocusable(true);
        this.addKeyListener(this);

        contenu.addMouseWheelListener(this);
        contenu.addMouseListener(this);
        contenu.addMouseMotionListener(this);
        contenu.addKeyListener(this);
        contenu.setLayout(cardLayout);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        this.setTitle("Perseverance II");
        this.setSize(largeur, hauteur);
        this.setLocationRelativeTo(null);
        
        contenu.setLayout(cardLayout);
        contenu.add(modeDeJeu, "Choix du mode");
        contenu.add(jeu, "Jeu");
        contenu.add(chargement, "Chargement");
        contenu.add(editeur, "Editeur de carte");
        jeu.setBackground(Color.DARK_GRAY);

        

        this.setContentPane(contenu); // On définit le contenu de "JFrame" comme un "JPanel"
        this.setVisible(true);

        Point coinEnHautAGauche = getLocation();
        Insets bordures = getInsets();
        Point coinEnHautAGaucheSansBordures = new Point((int)(coinEnHautAGauche.getX()+bordures.left),(int)(coinEnHautAGauche.getY()+bordures.top));
        controleur.majPositionFenetre(coinEnHautAGaucheSansBordures);

        // On définit la largeur et la hauteur du contenu du "JFrame" après l'avoir
        // affiché
        // Si on obtient sa taille avant son affichage, la dimension retournée est (0,0)
        this.largeur = this.getContentPane().getSize().getWidth();
        this.hauteur = this.getContentPane().getSize().getHeight();
        ((Dessiner)jeu).majLargeur(this.largeur);
        ((Dessiner)jeu).majHauteur(this.hauteur);
        ((Dessiner)jeu).initialiser();
        ((Dessiner)jeu).majEnJeu(true);
        ((Editeur)editeur).initialiser((int)this.largeur, (int)this.hauteur);
        
        ((Chargement)chargement).majTailleBar((int)(this.largeur/2.), (int)(this.hauteur/2.), (int)(this.largeur*2./5.), (int)(this.hauteur/20.));
        
    }

    public void initialiser() {
        //System.out.println("Initialisation");

        
        // Chargement et exécution du jeu
        cardLayout.show(contenu, "Chargement");
        controleur.charger();
        
        /*
        // Exécuter l'éditeur
        cardLayout.show(contenu, "Editeur");
        controleur.editer();
        timer = new Timer(Options.DELAI_ANIMATION, this);
        timer.start();
        */
        
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        controleur.interactionClavier(code);
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
            case DemarrerMinijeuLaser:
                ((Dessiner)jeu).demarrerMinijeuLaser((boolean) nouveau);
                break;
            case MinijeuLaser:
                ((Dessiner)jeu).majEtatMinijeuLaser((boolean) nouveau);
                break;
            case MinijeuExtraction:
                ((Dessiner)jeu).majEtatMinijeuExtraction((boolean) nouveau);
                break;
            case RayonDeSelection:
                ((Dessiner)jeu).majRayon((Integer) nouveau);
                break;
            case Competences:
                ((Dessiner)jeu).majCompetences((List<BoutonCercle>) nouveau);
                break;
            case BoutonsCercle:
                ((Editeur)editeur).majBoutonsCercle((BoutonCercle[]) nouveau);
                break;
            case BoutonsType:
                ((Editeur)editeur).majBoutonsType((Cellule[]) nouveau);
                break;
            case Cellules:
                if (scene.equals("Jeu"))
                    ((Dessiner)jeu).majCellules((Cellule[][]) nouveau);
                else if (scene.equals("Editeur de carte"))
                    ((Editeur)editeur).majCellules((Cellule[][]) nouveau);
                break;
            case Joueur:
                ((Dessiner)jeu).majJoueur((Robot) nouveau);
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
            case CentreZoom:
                if (scene.equals("Jeu"))
                    ((Dessiner) jeu).majCentreZoom((Point) nouveau);
                else if (scene.equals("Editeur de carte"))
                    ((Editeur) editeur).majCentreZoom((Point) nouveau);
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
            case Peindre:
                if (scene.equals("Jeu"))
                    jeu.repaint();  // Prévoit de mettre à jour le composant
                                    // n'appelle pas la méthode paint() !
                else if (scene.equals("Editeur de carte"))
                    editeur.repaint();
                break;
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        controleur.ajusterZoom(e.getWheelRotation(), e.getPoint());
    }

    @Override
    // On veut mettre à jour la position de la souris
    // Lorsque le clic est maintenu.
    // Cet évènement appelle mouseDragged, et non pas mouseMoved
    public void mouseDragged(MouseEvent ev) {
        controleur.majStatutSouris(ev);
    }

    @Override
    public void mouseMoved(MouseEvent ev) {
    }

    @Override
    public void mousePressed(MouseEvent ev) {
        if (scene.equals("Jeu") && ((Dessiner)jeu).obtenirEtatMinijeuExtraction())
            controleur.majPositionCurseurExtraction(((Dessiner)jeu).obtenirPositionCurseur());
        controleur.majStatutSouris(ev, true);
    }
    
    @Override
    public void mouseReleased(MouseEvent ev) {
        controleur.majStatutSouris(ev, false);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }
}
