import java.awt.Dimension;
import java.awt.Image;
import java.util.ArrayList;
import java.util.LinkedList;
import java.awt.Graphics;

public class Robot extends Avatar {
    
    //------------------------------------------------------------------ Attributs
    
    // Paramètres vitaux du robots
    private double sante;
    private double batterie;
    
    // Trajectoire
    /* La classe dimension est une classe qui comporte comme attribut une hauteur et une largeur,
     * elle est utilisée ici pour référencer la position d'un point à atteindre sur la carte par rapport au robot.
     * Pour aller d'un point à un autre sur la carte, on utilise une liste de cases ciblées, appelées but (à atteindre).*/ 
     
    private LinkedList<Dimension> but = new LinkedList<Dimension>();
    
    //  Position du robot dans la matrice de la carte
    private int row = 0;
    private int column = 0;
    
    // Position réélle du robot sur la carte, dont l'origine se rouve en haut à gauche
    private int xFictif = 0;
    private int yFictif = 0;
    
    //------------------------------------------------------------------ Constructeur
    
    public Robot(int sante, int batterie, int animationIndex, int dureeImage, ArrayList<ArrayList<Image>> image, int[] coords, int x, int y, double r, int dx, int dy, double dr) {
        // Le robot est un avatar, il hérite donc de son constructeur et de ses conditions d'avatar
        super(animationIndex, dureeImage, image, coords, x, y, r, dx, dy, dr);
        xFictif = x;
        yFictif = y;
        this.sante = sante;
        this.batterie = batterie;
    }
    
    // Place un robot neuf sur la carte
    public Robot(ArrayList<ArrayList<Image>> image, int x, int y) {
        this(Options.SANTE_MAX, Options.BATTERIE_MAX, 0, Options.JOUEUR_DUREE_ANIMATION, image, new int[] {0,0}, x, y, .0, 0, 0, .0);
    }

    public void definirBut(LinkedList<Dimension> liste) {
        this.but = liste;
        definirDirection(but.getFirst());
        animationIndex = 2; //  Image qui montre le robot marcher 
    }

    private void definirDirection(Dimension caseCiblee) {
        float angle = (float) Math.atan2(caseCiblee.getHeight() - yFictif, caseCiblee.getWidth() - xFictif);
        //System.out.println(Math.toDegrees(angle));
        
        dx = (int) (Math.cos(angle)*10); // 10 est la vitesse (pixel/itération de la boucle du jeu)
        dy = (int) (Math.sin(angle)*10);
        animationIndex = 2; // Image qui montre le robot marcher 
    }

    @Override
    public void updateCoords() {

        r += dr;

        if (!but.isEmpty()) {
            xFictif += dx;
            yFictif += dy;
            Dimension d = but.getFirst(); // On prend une case à cibler 
            if (Math.abs(xFictif - d.getWidth()) < Options.JOUERUR_TOLERANCE_DEPLACEMENT)
                dx = (int) d.getWidth() - xFictif;
            if (Math.abs(yFictif - d.getHeight()) < Options.JOUERUR_TOLERANCE_DEPLACEMENT)
                dy = (int) d.getHeight() - yFictif;
            if (dx == 0 && dy == 0) {
                but.removeFirst();
                if (but.isEmpty()) // Si il ne reste plus de cases à parcourir, le robot est arrivé à la case demandée
                    animationIndex = 0; // On passe l'animation du joueur en mode "pause" (Idle)
                else {
                    definirDirection(but.getFirst());
                    //System.out.println("Coords joueur : "+xFictif +" _ "+ yFictif);
                    //System.out.println("Coords but : "+ but.getFirst().getWidth() +" _ "+ but.getFirst().getHeight());
                }
                
            }
        }
    }

    public void dessiner(Graphics g) {
        super.dessiner(g);
    }
}
