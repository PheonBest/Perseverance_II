import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.TexturePaint;

import javax.swing.JPanel;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.awt.RenderingHints;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.FontMetrics;

public class Dessiner extends JPanel {
    private int rayonDeSelection = 0;
    private Cellule[][] cellules = {{}};
    private double largeurEcran;
    private double hauteurEcran;
    private Robot joueur;
    private ControlPanel panneauDeControle;
    private double zoom = 1.;
    private Point centreZoom = new Point(0,0);
    private boolean enJeu = false;
    private int[] tailleMinimap = {100,100};
    private int ligneTmp;
    private int colonneTmp;
    private List<BoutonCercle> competences = new LinkedList<BoutonCercle>();
    private boolean affichagePanneauDeControle;
    
    // Mini-jeu "Extraction"
    private boolean etatMinijeuExtraction = false;
    private int largeurRectangle;
    private int hauteurRectangle;
    private double positionCurseur = 0;
    private boolean sensVariationCurseur = true;
    private final Color[] COULEURS_DIAGONALES = {  
        new Color(229,229,229),  
        new Color(222,222,222)
    };
    private final double MINIJEU_CLICK_LARGEUR_ECRAN = 3.; // La largeur du rectangle correspond au tiers de la largeur de la fenêtre
    private final double MINIJEU_CLICK_HAUTEUR_ECRAN = 3.; // La largeur du rectangle correspond au tiers de la largeur de la fenêtre
    private final int ESPACE = 40;
    private final int LARGEUR_LIGNE = ESPACE/2;
    
    private final int RAYON = 20;
    private final Color[] COULEURS_MINIJEU = {
        new Color(46, 204, 113), // vert
        new Color(241, 196, 15), // jaune
        new Color(230, 126, 34), // orange
        new Color(231, 76, 60), // rouge
    };
    private final double MULTIPLICATEUR_DIFFERENCE_LONGUEUR = 2;
    private final int HAUTEUR_CLAVETTE = 50;
    private final int LARGEUR_TOTALE_CLAVETTE = (int) (HAUTEUR_CLAVETTE*Math.pow(MULTIPLICATEUR_DIFFERENCE_LONGUEUR,COULEURS_MINIJEU.length-1));
    private final int LARGEUR_CURSEUR = 3;
    private final int RAYON_CURSEUR = HAUTEUR_CLAVETTE/3;
    private final int PLAGE_DE_VALEURS = LARGEUR_TOTALE_CLAVETTE-RAYON_CURSEUR*2;
    private final double INCREMENT_CURSEUR = 0.05;
    private int coinMinijeuX;
    private int coinMinijeuY;

    // Mini-jeu "Laser"
    private boolean etatMinijeuLaser = false;
    private boolean demarrerMinijeuLaser = false;
    private long tempsDepuisChrono = 0;

    public Dessiner(boolean affichagePanneauDeControle){
        this.affichagePanneauDeControle = affichagePanneauDeControle;
        if (affichagePanneauDeControle) {
            panneauDeControle = new ControlPanel(10,10);
            add(panneauDeControle);
        }
    }
    public Dessiner(){
        this(false);
    }

    public void initialiser() {
        largeurRectangle = (int) (largeurEcran/MINIJEU_CLICK_LARGEUR_ECRAN);
        hauteurRectangle = (int) (hauteurEcran/MINIJEU_CLICK_HAUTEUR_ECRAN);
        coinMinijeuX = (int)(largeurEcran/2.-largeurRectangle/2.); // Coordonnée X du coin en haut à gauche du rectangle
        coinMinijeuY = (int)(hauteurEcran/2.-hauteurRectangle/2.); // Coordonnée Y du coin en haut à gauche du rectangle
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();
        AffineTransform transformationInitiale = g2d.getTransform();
        AffineTransform transformationMinimap = g2d.getTransform();
        // Affichage de la carte
        AffineTransform at = g2d.getTransform();
        // Application du zoom centré autour d'un point p:
        // 1. On veut que le point p soit la nouvelle origine
        //    Donc on translate le coin en haut à gauche vers le centre
        // 2. On aggrandit/rétrécit.

        at.translate(
            (largeurEcran/2),
            (hauteurEcran/2)
        );
        
        /*
        at.translate(
            (centreZoom.getX()),
            (centreZoom.getY())
        );
        */
        at.scale(zoom, zoom);
        
        // Obtention de la cellule sur laquelle le joueur est
        Cellule[] voisins = {};
        if (joueur != null && rayonDeSelection > 0) {
            int[] caseJoueur = joueur.obtenirCase();
            voisins = Voisins.obtenirVoisins(cellules, caseJoueur[0], caseJoueur[1], rayonDeSelection);
        }
        // On dessine les cellules de la carte
        g2d.setTransform(at);
        for (int i=0; i < cellules.length; i++) {
            for (Cellule c: cellules[i]) {
                //if (c.estVisible(largeurEcran,hauteurEcran,zoom))

                // Si les cellules font partie des voisins du joueur, on les dessine en bleu
                int j = 0;
                while (j < voisins.length && voisins[j] != c)
                    j++;
                if (j < voisins.length) {
                    TypeCase oldType = c.obtenirType();
                    c.majType(TypeCase.DESERT);
                    c.dessiner(g2d);
                    c.majType(oldType);
                } else
                    c.dessiner(g2d);
            }
        }

        
        if (enJeu && joueur != null) {
            // Affichage du joueur
            joueur.dessiner(g2d);

            // Affichage de la minimap
            g2d.setTransform(transformationMinimap);

            // On dessine les compétences utilisables par le joueur
            for (BoutonCercle c: competences)
                c.dessiner(g);

            // On dessine la minimap sur un rectangle
            g2d.setColor(Color.gray);
            
            g2d.fillRoundRect(  (int)(Options.POSITION_X_MINIMAP*largeurEcran - (largeurEcran/2 + Options.LARGEUR_CASE/4)*Options.ZOOM_MINIMAP - 2*Options.DIMENSIONS_CASES[0]),
                                (int)(Options.POSITION_Y_MINIMAP*hauteurEcran - (hauteurEcran/2)*Options.ZOOM_MINIMAP - 2*Options.DIMENSIONS_CASES[1]),
                                (int)(tailleMinimap[0] + 9./4.*Options.DIMENSIONS_CASES[0]),
                                (int)(tailleMinimap[1] + 9./4.*Options.DIMENSIONS_CASES[1]),
                                15,
                                15);

            // On dessine la minimap
            
            // POSITION X DE LA MINIMAP
            // Sa position en x est la même que celle du rectangle ci-dessus.
            // Cependant, les cellules de la carte sont déplacées en même temps que le joueur.
            // Donc on annule ce déplacement. Or ce déplacement est le même pour toutes les cases. On s'intérésse donc ici à la case en [0,0]
            // Lorsque le joueur n'a pas bougé, le déplacement doit être nul.
            // Les coordonnées du coin en haut à gauche de la première cellule valent [xpoints[0] + Options.LARGEUR_CASE/4,ypoints[0]]
            // On prend en compte le décalage (demi largeur d'écran)
            // D'où le déplacement -(cellules[0][0].xpoints[0] + largeurEcran/2 + Options.LARGEUR_CASE/4)
            // Or on applique un facteur de zoom à ce décalage
            // D'où l'annulation du décalage -(cellules[0][0].xpoints[0] + largeurEcran/2 + Options.LARGEUR_CASE/4)*Options.ZOOM_MINIMAP)

            // POSITION Y DE LA MINIMAP
            // Sa position en y est la même que celle du rectangle ci-dessus.
            // De la même manière que pour la coordonnée x, on annule le décalage en y: -(cellules[0][0].ypoints[0] + hauteurEcran/2)*Options.ZOOM_MINIMAP
            transformationMinimap.translate(
                (Options.POSITION_X_MINIMAP*largeurEcran - (cellules[0][0].xpoints[0] + largeurEcran/2 + Options.LARGEUR_CASE/4)*Options.ZOOM_MINIMAP),
                (Options.POSITION_Y_MINIMAP*hauteurEcran - (cellules[0][0].ypoints[0] + hauteurEcran/2)*Options.ZOOM_MINIMAP)
            );

            transformationMinimap.scale(Options.ZOOM_MINIMAP, Options.ZOOM_MINIMAP);
            ((Graphics2D) g2d).setTransform(transformationMinimap);
            for (int i=0; i < cellules.length; i+=Options.INCREMENT_MINIMAP) {
                for (int j=0; j < cellules[i].length; j+=Options.INCREMENT_MINIMAP) {
                    TypeCase typeRepresentatif = obtenirCelluleRepresentative(Voisins.obtenirVoisins(cellules, i, j, 2));
                    if (cellules[i][j].obtenirType() == typeRepresentatif)
                        cellules[i][j].dessiner(g2d, Options.AGRANDISSEMENT_CELLULE_MINICARTE); // La cellule sera agrandie autour de son centre
                    else {
                        TypeCase ancienType = cellules[i][j].obtenirType();
                        cellules[i][j].majType(typeRepresentatif);
                        cellules[i][j].dessiner(g2d, Options.AGRANDISSEMENT_CELLULE_MINICARTE);
                        cellules[i][j].majType(ancienType);
                    }
                    
                }
            }

            // Affichage du mini-jeu
            if (etatMinijeuExtraction) {
                
                ((Graphics2D) g2d).setTransform(transformationInitiale);
                dessinerRectangle(g2d, coinMinijeuX, coinMinijeuY, largeurRectangle, hauteurRectangle, RAYON, RAYON);

                int largeur = LARGEUR_TOTALE_CLAVETTE;
                for (int i=COULEURS_MINIJEU.length-1; i > -1 ; i--) {
                    g2d.setPaint(COULEURS_MINIJEU[i]);
                    g2d.fillRoundRect((int)(largeurEcran/2-largeur/2), (int)(hauteurEcran/2-HAUTEUR_CLAVETTE/2), largeur, HAUTEUR_CLAVETTE, HAUTEUR_CLAVETTE, HAUTEUR_CLAVETTE);
                    largeur /= MULTIPLICATEUR_DIFFERENCE_LONGUEUR;
                }

                double angleDepart = -Math.PI/2.;
                double angleFin = Math.PI/2.;
                int sens = -1;
                if (sensVariationCurseur) { // On déplace le curseur vers la droite
                    positionCurseur += INCREMENT_CURSEUR;
                    if (positionCurseur > 1) {
                        positionCurseur = 1;
                        sensVariationCurseur = false;
                    }
                } else { // On déplace le curseur vers la gauche
                    positionCurseur -= INCREMENT_CURSEUR;
                    angleDepart += Math.PI;
                    angleFin += Math.PI;
                    sens *= -1;
                    if (positionCurseur < 0) {
                        positionCurseur = 0;
                        sensVariationCurseur = true;
                    }
                }

                // On dessine les lignes qui suivent le curseur (effet de vitesse)
                int xCurseur = (int) ((largeurEcran-LARGEUR_TOTALE_CLAVETTE)/2 + PLAGE_DE_VALEURS*positionCurseur);
                final int NOMBRE_LIGNES = 20;
                final int LONGUEUR_LIGNES = 10;
                Color c1 = new Color(223, 228, 234); // blanc
                Color c2 = new Color(30, 144, 255); // bleu
                double mixCouleurs = 0.;
                double deltaMix = 1./NOMBRE_LIGNES;
                g2d.setStroke(new BasicStroke(1));
                double deltaTheta = Math.PI/NOMBRE_LIGNES;
                // On fait varier l'angle de Pi/2 à 3Pi/2 (vers la gauche) OU de -Pi/2 à Pi/2
                for (double theta = angleDepart; theta <= angleFin; theta += deltaTheta) {
                    mixCouleurs = (mixCouleurs+deltaMix)%1;
                    if (sens == -1)
                        g2d.setColor(mixColors(c2, c1, mixCouleurs));
                    else
                        g2d.setColor(mixColors(c1, c2, mixCouleurs));
                    int xLine = (int)(xCurseur+RAYON_CURSEUR-RAYON_CURSEUR*Math.cos(theta));
                    int yLine = (int)(hauteurEcran/2-RAYON_CURSEUR+RAYON_CURSEUR-RAYON_CURSEUR*Math.sin(theta));
                    g2d.drawLine(xLine, yLine, xLine+LONGUEUR_LIGNES*sens, yLine);
                }
                // On dessine le curseur

                

                g2d.setPaint(Color.WHITE);
                g2d.fillOval(xCurseur, (int)(hauteurEcran/2-RAYON_CURSEUR), RAYON_CURSEUR*2, RAYON_CURSEUR*2);
                g2d.setPaint(new Color(222,222,222));
                g2d.setStroke(new BasicStroke(LARGEUR_CURSEUR));
                g2d.drawOval(xCurseur, (int)(hauteurEcran/2-RAYON_CURSEUR), RAYON_CURSEUR*2, RAYON_CURSEUR*2);
            }
            if (etatMinijeuLaser) {
                System.out.println(etatMinijeuLaser);
                ((Graphics2D) g2d).setTransform(transformationInitiale);
                dessinerRectangle(g2d, coinMinijeuX, coinMinijeuY, largeurRectangle, hauteurRectangle, RAYON, RAYON);
                g2d.setStroke(new BasicStroke(10));
                final int NOMBRE_FEUX = 3;
                final int RAYON_FEU = 30;
                final int ESPACE_INTRER_FEU = 30;
                int largeurTotale = NOMBRE_FEUX*(RAYON_FEU*2)+(NOMBRE_FEUX-1)*ESPACE_INTRER_FEU;
                for (int i=0; i < 3; i++) {
                    if (demarrerMinijeuLaser)
                        g2d.setPaint(new Color(231, 76, 60)); // rouge clair
                    else
                        g2d.setPaint(new Color(46, 204, 113)); // vert clair
                    g2d.fillOval((int)((largeurEcran-largeurTotale)/2. + i*(2*RAYON_FEU + ESPACE_INTRER_FEU)), (int)(hauteurEcran/2. - RAYON_FEU), RAYON_FEU*2, RAYON_FEU*2);
                    if (demarrerMinijeuLaser)
                        g2d.setPaint(new Color(214,46,27)); // rouge foncé
                    else
                        g2d.setPaint(new Color(38,168,94)); // vert foncé
                    g2d.drawOval((int)((largeurEcran-largeurTotale)/2. + i*(2*RAYON_FEU + ESPACE_INTRER_FEU)), (int)(hauteurEcran/2. - RAYON_FEU), RAYON_FEU*2, RAYON_FEU*2);
                }
                String str = new DecimalFormat("0.0000").format(System.currentTimeMillis()-tempsDepuisChrono)+" secondes"; // on arrondie le temps au millième
                g2d.setPaint(Color.BLACK);
                dessinerTexteCentre(g2d, str, (int)(largeurEcran/2), (int)(hauteurEcran/2+3*RAYON_FEU), Options.police);
                dessinerTexteCentre(g2d, "Lorsque les lumières deviennent vertes, appuyez sur [espace]\nle plus vite possible pour renvoyer les données du scan sur Terre.", (int)(largeurEcran/2), (int)(hauteurEcran/2-2*RAYON_FEU), Options.POLICE_PLAIN);
            }
        }

    }

    // Daniel Kvist, https://stackoverflow.com/questions/27706197/how-can-i-center-graphics-drawstring-in-java
    private void dessinerTexteCentre(Graphics g, String text, int coordX, int coordY, Font font) {
        // Get the FontMetrics
        FontMetrics metrics = g.getFontMetrics(font);
        final int NOMBRE_LIGNES_TEXTE = text.split("\n").length;
        int index = 0;
        g.setFont(font);
        for (String ligne : text.split("\n")) {
            // Determine the X coordinate for the text
            int x = coordX - metrics.stringWidth(ligne) / 2;
            // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
            int y = coordY - metrics.getHeight() / 2 + metrics.getAscent() - (int)((NOMBRE_LIGNES_TEXTE/2.-index)*metrics.getHeight());
            // Set the font
            // Draw the String
            g.drawString(ligne, x, y);
            index++;
        }
    }

    private void dessinerRectangle(Graphics2D g2d, int coinX, int coinY, int largeur, int hauteur, int rayonX, int rayonY) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setPaint(COULEURS_DIAGONALES[0]);

        Shape rectangle = new RoundRectangle2D.Float(coinX, coinY, largeur - 1f, hauteur -1f, RAYON, RAYON);
        g2d.fill(rectangle);
        g2d.setClip(rectangle);
        g2d.setStroke(new BasicStroke(LARGEUR_LIGNE));

        // dessin des lignes diagonales
        g2d.setPaint(COULEURS_DIAGONALES[1]);
        
        for (int x = coinX, y = coinY; y < (coinY + largeur + hauteur); y += ESPACE)
            g2d.drawLine(x, y ,  x + (int) largeur , y  - (int) largeur);
        // On trace les lignes:
        // On part du coin en haut à gauche
        // On arrive tout à droite du rectangle en X, et on translate vers le bas en y à chaque itération
    }
    
    public void majCellules(Cellule[][] cellules) {
        this.cellules = cellules;
        majTailleMinimap();
    }

    public void majJoueur(Robot joueur) {
        this.joueur = joueur;
        if (affichagePanneauDeControle)
            panneauDeControle.majJoueur(joueur);
    }

    public void majLargeur(double largeurEcran) {
        this.largeurEcran = largeurEcran;
    }

    public void majHauteur(double hauteurEcran) {
        this.hauteurEcran = hauteurEcran;
    }

    public void majZoom(double zoom) {
        this.zoom = zoom;
    }

    public void majCentreZoom(Point centreZoom) {
        this.centreZoom = centreZoom;
    }

    public void majEnJeu(boolean enJeu) {
        this.enJeu = enJeu;
    }

    public void majCompetences(List<BoutonCercle> competences) {
        this.competences = (LinkedList<BoutonCercle>) competences;
    }

    private void majTailleMinimap() {
        if (cellules.length > 1) {
            tailleMinimap[0] = (int)((cellules[0][cellules[0].length-1].xpoints[0] - cellules[0][0].xpoints[0])*Options.ZOOM_MINIMAP);
            tailleMinimap[1] = (int)((cellules[cellules.length-1][0].ypoints[0]    - cellules[0][0].ypoints[0])*Options.ZOOM_MINIMAP);
        }
    }

    private TypeCase obtenirCelluleRepresentative(Cellule[] echantillon) {
        HashMap<TypeCase, Integer> observations = new HashMap<TypeCase, Integer>(echantillon.length);
        for (Cellule c: echantillon) {
            if (c != null) {
                if (observations.containsKey(c.obtenirType())) {
                    Integer nombreObservations = observations.get(c.obtenirType());
                    nombreObservations += 1;
                } else
                    observations.put(c.obtenirType(), 1);
            }
        }

        Entry<TypeCase, Integer> individuMax = null;
        for (Entry<TypeCase, Integer> individu : observations.entrySet()) {
            if (individuMax == null || individu.getValue().compareTo(individuMax.getValue()) > 0)
                individuMax = individu;
        };
        return individuMax.getKey();
    }
    
    public void majRayon(Integer rayonDeSelection) {
        this.rayonDeSelection = rayonDeSelection;
    }

    public double obtenirPositionCurseur() {
        return positionCurseur;
    }

    public void majEtatMinijeuExtraction(boolean etatMinijeuExtraction) {
        this.etatMinijeuExtraction = etatMinijeuExtraction;
        if (etatMinijeuExtraction)
            positionCurseur = 0;
    }
    public boolean obtenirEtatMinijeuExtraction() {
        return etatMinijeuExtraction;
    }

    public void majEtatMinijeuLaser(boolean etatMinijeuLaser) {
        
        this.etatMinijeuLaser = etatMinijeuLaser;
        System.out.println("majMinijeu "+this.etatMinijeuLaser+" "+etatMinijeuLaser);
    }
    public boolean obtenirEtatMinijeuLaser() {
        return etatMinijeuLaser;
    }

    // Par Martin Larsson, https://stackoverflow.com/questions/17544157/generate-n-colors-between-two-colors
    private Color mixColors(Color color1, Color color2, double percent){
        double inverse_percent = 1.0 - percent;
        int redPart = (int) (color1.getRed()*percent + color2.getRed()*inverse_percent);
        int greenPart = (int) (color1.getGreen()*percent + color2.getGreen()*inverse_percent);
        int bluePart = (int) (color1.getBlue()*percent + color2.getBlue()*inverse_percent);
        return new Color(redPart, greenPart, bluePart);
  }
    public void demarrerMinijeuLaser(boolean demarrerMinijeuLaser) {
        this.demarrerMinijeuLaser = demarrerMinijeuLaser;
    }
    

}
