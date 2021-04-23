import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.TexturePaint;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.awt.RenderingHints;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.FontMetrics;

import java.util.ArrayList;

public class Dessiner extends JPanel {
    private Controleur controleur;
    private ArrierePlan arrierePlan;
    private int rayonDeSelection = 0;
    private Cellule[][] cellules = {{}};
    private double largeurEcran;
    private double hauteurEcran;
    private Robot joueur;
    private ControlPanel panneauDeControle;
    private BoutonMissions panneauMission;
    private JButton panneauPause;
    private double zoom = 1.;
    private List<BoutonCercle> competences = new LinkedList<BoutonCercle>();
    private boolean affichagePanneauDeControle;
    private String informerJoueur = "";
    private boolean victoire = false;
    private boolean defaite = false;

    // Minimap
    private int[] tailleMinimap = {100,100};
    private TypeCase[] typeMoyen = {};
    private TypeSymbole[] symboleMoyen = {};
    private final double DELTA_LARGEUR = 9./4.*Options.DIMENSIONS_CASES[0];
    private final double DELTA_HAUTEUR = 9./4.*Options.DIMENSIONS_CASES[1];

    // Mini-jeu
    private int largeurRectangle;
    private int hauteurRectangle;
    private int hauteurRectangleLaser;
    private final double MINIJEU_EXTRACTION_LARGEUR_ECRAN = 2.7; // La largeur du rectangle correspond au tiers de la largeur de la fenêtre
    private final double MINIJEU_EXTRACTION_HAUTEUR_ECRAN = 3.; // La hauteur du rectangle correspond au tiers de la hauteur de la fenêtre
    private final double MINIJEU_LASER_HAUTEUR_ECRAN = 2.1;
    
    // Mini-jeu "Extraction"
    private String resultat = "";
    private Etat etatMinijeuExtraction = Etat.OFF;
    private double positionCurseurExtraction = 0;
    private boolean sensVariationExtraction = false;

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
    private int coinMinijeuX;
    private int coinMinijeuY;

    // Mini-jeu "Laser"
    private Etat etatMinijeuLaser = Etat.OFF;
    private int nombreErreursLaser = 0;
    private String chronometreMinijeuLaser = "";

    private final int DECALAGE_Y_TEXTE = 20;
    private final int DECALAGE_Y_FEUX = 30;
    private final int NOMBRE_FEUX = 3;
    private final int RAYON_FEU = 30;
    private final int ESPACE_INTRER_FEU = 30;
    private final int LARGEUR_CROIX = 30;
    private final int ESPACE_INTER_CROIX = 30;
    private final int NOMBRE_CROIX = 3;
    private final int LONGUEUR_TOTALE = NOMBRE_CROIX*LARGEUR_CROIX+(NOMBRE_CROIX-1)*ESPACE_INTER_CROIX;
    private int coinX; // Coordonnée x du coin où on dessine les croix
    private int coinY; // Coordonnée y du coin où on dessine les croix

    // Informer le joueur
    final int LARGEUR_RECTANGLE_NOTIFICATION = 350;
    final int HAUTEUR_RECTANGLE_NOTIFICATION = 100;
   

    public Dessiner(Controleur controleur, boolean affichagePanneauDeControle, int largeur, int hauteur){
        setLayout(null);
        largeurEcran = largeur;
        hauteurEcran = hauteur;
        this.controleur = controleur;
        this.affichagePanneauDeControle = affichagePanneauDeControle;
        if (affichagePanneauDeControle) {

            panneauDeControle = new ControlPanel(10,10);
            add(panneauDeControle);

            // Panneau Missions
            panneauMission = new BoutonMissions(625,10);
            add(panneauMission);

            panneauPause = new JButton("PAUSE");
            panneauPause.setBounds(625,60, 100, 50);
            panneauPause.addActionListener(new AbstractAction("Pause") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    controleur.interactionClavier(KeyEvent.VK_ESCAPE);
                }
            });
			add(panneauPause);

           
        }

        largeurRectangle = (int) (largeurEcran/MINIJEU_EXTRACTION_LARGEUR_ECRAN);
        hauteurRectangle = (int) (hauteurEcran/MINIJEU_EXTRACTION_HAUTEUR_ECRAN);
        hauteurRectangleLaser = (int) (hauteurEcran/MINIJEU_LASER_HAUTEUR_ECRAN);
        coinMinijeuX = (int)(largeurEcran/2.-largeurRectangle/2.); // Coordonnée X du coin en haut à gauche du rectangle
        coinMinijeuY = (int)(hauteurEcran/2.-hauteurRectangle/2.); // Coordonnée Y du coin en haut à gauche du rectangle
        coinX = (int) ((largeurEcran-LONGUEUR_TOTALE)/2); // Coordonnée x du coin où on dessine les croix
        coinY = (int) ((hauteurEcran-LARGEUR_CROIX)/2+RAYON_FEU*4); // Coordonnée y du coin où on dessine les croix
        

        
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();
        AffineTransform transformationInitiale = g2d.getTransform();
        AffineTransform transformationMinimap = g2d.getTransform();
        
        AffineTransform at = g2d.getTransform();
        // Application du zoom centré autour d'un point "p"
        // (le point p est le centre de l'écran ici)
        // 1. On veut que le point p soit la nouvelle origine
        //    Donc on translate le coin en haut à gauche vers le centre
        // 2. On aggrandit/rétrécit.

        
        at.translate(
            (largeurEcran/2),
            (hauteurEcran/2)
        );
        
        
        at.scale(zoom, zoom);
        g2d.setTransform(at);
        
        // Affichage de l'arrière plan
        if (arrierePlan != null)
            arrierePlan.dessiner(g2d, largeurEcran, hauteurEcran, zoom);

        // Affichage de la carte
        // Obtention de la cellule sur laquelle le joueur est
        Cellule[] voisins = {};
        if (joueur != null && rayonDeSelection > 0) {
            int[] caseJoueur = joueur.obtenirCase();
            voisins = Voisins.obtenirVoisins(cellules, caseJoueur[0], caseJoueur[1], rayonDeSelection);
        }
        // On dessine les cellules de la carte
        for (int i=0; i < cellules.length; i++) {
            for (Cellule c: cellules[i]) {
                if (c.estVisible(largeurEcran,hauteurEcran,zoom)) {

                    // Si les cellules font partie des voisins du joueur, on les dessine en bleu
                    int j = 0;
                    while (j < voisins.length && voisins[j] != c)
                        j++;
                    if (j < voisins.length) {
                        //TypeCase oldType = c.obtenirType();
                        //c.majType(TypeCase.DESERT);
                        c.majSelectionne(true);
                        c.dessiner(g2d);
                        c.majSelectionne(false);
                        //c.majType(oldType);
                    } else
                        c.dessiner(g2d);

                }
            }
        }

        if (affichagePanneauDeControle && joueur != null) {
            // Affichage du joueur
            joueur.dessiner(g2d);

            // Affichage de la minimap
            g2d.setTransform(transformationMinimap);

            // On dessine les compétences utilisables par le joueur
            for (BoutonCercle c: competences)
                c.dessiner(g);

            // On dessine la minimap sur un rectangle
            g2d.setColor(Color.gray);

            Formes.dessinerRectangle(g2d,
                (int)(Options.POSITION_X_MINIMAP*largeurEcran - DELTA_LARGEUR/2),
                (int)(Options.POSITION_Y_MINIMAP*hauteurEcran - DELTA_HAUTEUR/2),
                (int)(tailleMinimap[0] + DELTA_LARGEUR),
                (int)(tailleMinimap[1] + DELTA_HAUTEUR),
                15,
                15
            );

            // On dessine la minimap
            
            // POSITION X DE LA MINIMAP
            // Sa position en x est la même que celle du rectangle ci-dessus.
            // Cependant, les cellules de la carte sont déplacées en même temps que le joueur.
            // Donc on annule ce déplacement. Or ce déplacement est le même pour toutes les cases. On s'intérésse donc ici à la case en [0,0]
            // Lorsque le joueur n'a pas bougé, le déplacement doit être nul.
            // Les coordonnées du coin en haut à gauche de la première cellule valent [xpoints[0] + Options.LARGEUR_CASE/4,ypoints[0]]
            // D'où le déplacement -(cellules[0][0].xpoints[0] + Options.LARGEUR_CASE/4)
            // Or on applique un facteur de zoom à ce décalage
            // D'où l'annulation du décalage -(cellules[0][0].xpoints[0] + Options.LARGEUR_CASE/4)*Options.ZOOM_MINIMAP)

            // POSITION Y DE LA MINIMAP
            // Sa position en y est la même que celle du rectangle ci-dessus.
            // De la même manière que pour la coordonnée x, on obtient le déplacement en y: -(cellules[0][0].ypoints[0])*Options.ZOOM_MINIMAP
            transformationMinimap.translate(
                (Options.POSITION_X_MINIMAP*largeurEcran - (cellules[0][0].xpoints[0] + Options.LARGEUR_CASE/4)*Options.ZOOM_MINIMAP),
                (Options.POSITION_Y_MINIMAP*hauteurEcran - (cellules[0][0].ypoints[0])*Options.ZOOM_MINIMAP)
            );

            transformationMinimap.scale(Options.ZOOM_MINIMAP, Options.ZOOM_MINIMAP);
            ((Graphics2D) g2d).setTransform(transformationMinimap);
            if (typeMoyen.length > 0) {
                int index = 0;
                TypeCase ancienType;
                TypeSymbole ancienSymbole;
                for (int i=0; i < cellules.length; i+=Options.INCREMENT_MINIMAP) {
                    for (int j=0; j < cellules[i].length; j+=Options.INCREMENT_MINIMAP) {
                        ancienType = cellules[i][j].obtenirType();
                        ancienSymbole = cellules[i][j].obtenirSymbole().type;

                        if (ancienType != typeMoyen[index])
                            cellules[i][j].majType(typeMoyen[index]);
                        //if (ancienSymbole != symboleMoyen[index])
                        //    cellules[i][j].obtenirSymbole().majSymbole(symboleMoyen[index], Donnees.imagesSymboles.get(symboleMoyen[index].name()));
                        if (ancienSymbole != TypeSymbole.VIDE)
                            cellules[i][j].obtenirSymbole().majSymbole(TypeSymbole.VIDE, null);
                        cellules[i][j].dessiner(g2d, Options.AGRANDISSEMENT_CELLULE_MINICARTE); // La cellule sera agrandie autour de son centre
                        if (ancienType != typeMoyen[index])
                            cellules[i][j].majType(ancienType);
                        //if (ancienSymbole != symboleMoyen[index])
                        //    cellules[i][j].obtenirSymbole().majSymbole(ancienSymbole, Donnees.imagesSymboles.get(ancienSymbole.name()));
                        if (ancienSymbole != TypeSymbole.VIDE)
                            cellules[i][j].obtenirSymbole().majSymbole(ancienSymbole, Donnees.imagesSymboles.get(ancienSymbole.name()));

                        index++;
                    }
                }
            }

            if (!victoire && !defaite) {
            ((Graphics2D) g2d).setTransform(transformationInitiale);
            
            // MINI-JEU EXTRACTION -------------
            if (!etatMinijeuExtraction.equals(Etat.OFF)) {
                Formes.dessinerRectangle(g2d, coinMinijeuX, coinMinijeuY, largeurRectangle, hauteurRectangle, RAYON, RAYON);

                int largeur = LARGEUR_TOTALE_CLAVETTE;
                for (int i=COULEURS_MINIJEU.length-1; i > -1 ; i--) {
                    g2d.setPaint(COULEURS_MINIJEU[i]);
                    g2d.fillRoundRect((int)(largeurEcran/2-largeur/2), (int)(hauteurEcran/2-HAUTEUR_CLAVETTE/2), largeur, HAUTEUR_CLAVETTE, HAUTEUR_CLAVETTE, HAUTEUR_CLAVETTE);
                    largeur /= MULTIPLICATEUR_DIFFERENCE_LONGUEUR;
                }

                int xCurseur = (int) ((largeurEcran-LARGEUR_TOTALE_CLAVETTE)/2 + PLAGE_DE_VALEURS*positionCurseurExtraction);
                if (etatMinijeuExtraction.equals(Etat.IN)) {

                    // Si on se déplace vers la gauche, on dessigne des lignes
                    // représentant le mouvement du curseur
                    // sur le demi-cercle droit du curseur
                    double angleDepart = -Math.PI/2.;
                    double angleFin = Math.PI/2.;
                    if (!sensVariationExtraction) { // Si on se déplace vers la gauche
                        angleDepart += Math.PI;
                        angleFin += Math.PI;
                    }
                    // On dessine les lignes qui suivent le curseur (effet de vitesse)
                    xCurseur = (int) ((largeurEcran-LARGEUR_TOTALE_CLAVETTE)/2 + PLAGE_DE_VALEURS*positionCurseurExtraction);
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
                        int xLine = (int)(xCurseur+RAYON_CURSEUR-RAYON_CURSEUR*Math.cos(theta));
                        int yLine = (int)(hauteurEcran/2-RAYON_CURSEUR+RAYON_CURSEUR-RAYON_CURSEUR*Math.sin(theta));
                        if (sensVariationExtraction) { // Si on se déplace vers la droite
                            g2d.setColor(mixColors(c2, c1, mixCouleurs)); // on inverse les couleurs
                            g2d.drawLine(xLine, yLine, xLine - LONGUEUR_LIGNES, yLine); // On trace les lignes vers la gauche (elles représentent le souffle du mouvement)
                        } else {
                            g2d.setColor(mixColors(c1, c2, mixCouleurs));
                            g2d.drawLine(xLine, yLine, xLine + LONGUEUR_LIGNES, yLine);
                        }
                    }
                }

                // On dessine le curseur
                g2d.setPaint(Color.WHITE);
                g2d.fillOval(xCurseur, (int)(hauteurEcran/2-RAYON_CURSEUR), RAYON_CURSEUR*2, RAYON_CURSEUR*2);
                g2d.setPaint(new Color(222,222,222));
                g2d.setStroke(new BasicStroke(LARGEUR_CURSEUR));
                g2d.drawOval(xCurseur, (int)(hauteurEcran/2-RAYON_CURSEUR), RAYON_CURSEUR*2, RAYON_CURSEUR*2);

                g2d.setPaint(Color.BLACK);

                Formes.dessinerTexteCentre(g2d, "Cliquez pour contrôler\nle bras mécanique avec précision", (int)(largeurEcran/2), (int)(hauteurEcran/2-HAUTEUR_CLAVETTE), Options.POLICE_PLAIN);
                if (etatMinijeuExtraction.equals(Etat.OUT)) // On affiche le score
                    Formes.dessinerTexteCentre(g2d, resultat, (int)(largeurEcran/2), (int)(hauteurEcran/2+2*HAUTEUR_CLAVETTE), Options.police);
                }
            }
            // MINI-JEU EXTRACTION -------------

            // MINI-JEU LASER -------------
            if (!etatMinijeuLaser.equals(Etat.OFF)) {
                Formes.dessinerRectangle(g2d, coinMinijeuX, coinMinijeuY, largeurRectangle, hauteurRectangleLaser, RAYON, RAYON);
                g2d.setStroke(new BasicStroke(10));
                
                int largeurTotale = NOMBRE_FEUX*(RAYON_FEU*2)+(NOMBRE_FEUX-1)*ESPACE_INTRER_FEU;
                Color couleurFondFeux = null;
                Color couleurBordureFeux = null;
                String str;
                switch (etatMinijeuLaser) {
                    case ON: // Lancement
                        couleurFondFeux = new Color(231, 76, 60);       // rouge clair
                        couleurBordureFeux = new Color(214, 46, 27);    // rouge foncé
                        break;
                    case IN: // En cours
                        couleurFondFeux = new Color(46, 204, 113);      // vert clair
                        couleurBordureFeux = new Color(38, 168, 94);    // vert foncé
                        break;
                    case OUT: // Affichage du score
                        couleurFondFeux = new Color(52, 152, 219);      // bleu clair
                        couleurBordureFeux = new Color(34, 129, 191);   // bleu foncé
                        break;
                    case OFF:
                        break;
                }
                for (int i=0; i < 3; i++) {
                    g2d.setPaint(couleurFondFeux);
                    g2d.fillOval((int)((largeurEcran-largeurTotale)/2. + i*(2*RAYON_FEU + ESPACE_INTRER_FEU)), (int)(hauteurEcran/2. - RAYON_FEU + DECALAGE_Y_FEUX), RAYON_FEU*2, RAYON_FEU*2);
                    g2d.setPaint(couleurBordureFeux);
                    g2d.drawOval((int)((largeurEcran-largeurTotale)/2. + i*(2*RAYON_FEU + ESPACE_INTRER_FEU)), (int)(hauteurEcran/2. - RAYON_FEU + DECALAGE_Y_FEUX), RAYON_FEU*2, RAYON_FEU*2);
                }

                g2d.setPaint(Color.BLACK);
                Formes.dessinerTexteCentre(g2d, chronometreMinijeuLaser, (int)(largeurEcran/2), (int)(hauteurEcran/2+3*RAYON_FEU+DECALAGE_Y_FEUX), Options.police);
                Formes.dessinerTexteCentre(g2d, "Lorsque les lumières deviennent vertes,\ncliquez le plus vite possible\npour renvoyer les données\ndu scan sur Terre.", (int)(largeurEcran/2), (int)(hauteurEcran/2-2*RAYON_FEU+DECALAGE_Y_TEXTE), Options.POLICE_PLAIN);
                
                // On dessine les éventuelles erreurs SI il y a au moins une erreur
                if (nombreErreursLaser > 0) {
                    g2d.setStroke(new BasicStroke(10));
                    for (int i=0; i< NOMBRE_CROIX; i++) {
                        if (i < nombreErreursLaser) // Dessin de croix pleines
                            g2d.setPaint(new Color(231, 76, 60)); // rouge clair
                        else // Dessin de croix vides
                            g2d.setPaint(Color.DARK_GRAY);  // gris
                        // Ligne allant d'en bas à gauche à en haut à droite
                        g2d.drawLine(coinX+i*(LARGEUR_CROIX+ESPACE_INTER_CROIX), coinY+LARGEUR_CROIX+DECALAGE_Y_FEUX, coinX+LARGEUR_CROIX*(i+1)+i*ESPACE_INTER_CROIX, coinY+DECALAGE_Y_FEUX);
                        // Ligne allant d'en haut à gauche à en bas à droite
                        g2d.drawLine(coinX+i*(LARGEUR_CROIX+ESPACE_INTER_CROIX), coinY+DECALAGE_Y_FEUX, coinX+LARGEUR_CROIX*(i+1)+i*ESPACE_INTER_CROIX, coinY+LARGEUR_CROIX+DECALAGE_Y_FEUX);
                    }
                }
            }
            // MINI-JEU LASER -------------

            // On affiche un message pour informer le joueur SI il n'est pas vide
            if (!informerJoueur.equals("")) {
                Formes.dessinerRectangle(g2d, (int)((largeurEcran-LARGEUR_RECTANGLE_NOTIFICATION)/2.), (int)(hauteurEcran*4./5. - HAUTEUR_RECTANGLE_NOTIFICATION/2), LARGEUR_RECTANGLE_NOTIFICATION, HAUTEUR_RECTANGLE_NOTIFICATION, RAYON, RAYON);
                g2d.setColor(new Color(231, 76, 60)); // rouge clair
                Formes.dessinerTexteCentre(g2d, informerJoueur, (int)(largeurEcran/2), (int)(hauteurEcran*4./5.), Options.POLICE_PLAIN);
            }
        }

        if (victoire) {
            // Affichage du panneau de victoire (appelé une unique fois)
        }
        else if (defaite) {
            // Affichage du panneau de défaite (appelé une unique fois)
        }

    }

    public void majCellules(Cellule[][] cellules) {
        this.cellules = cellules;
        majMinimap();
    }

    public void majJoueur(Robot joueur) {
        this.joueur = joueur;
        if (affichagePanneauDeControle)
            panneauDeControle.majJoueur(joueur);
    }

    public void majZoom(double zoom) {
        this.zoom = zoom;
    }

    public void majCompetences(List<BoutonCercle> competences) {
        this.competences = (LinkedList<BoutonCercle>) competences;
    }

    private void majMinimap() {
        // On met à jour la taille de la minimap
        if (cellules.length > 1) {
            tailleMinimap[0] = (int)((cellules[0][cellules[0].length-1].xpoints[0] - cellules[0][0].xpoints[0])*Options.ZOOM_MINIMAP);
            tailleMinimap[1] = (int)((cellules[cellules.length-1][0].ypoints[0]    - cellules[0][0].ypoints[0])*Options.ZOOM_MINIMAP);
        }
        // On met à jour les cellules sélectionnées pour être affichées dans la minimap
        final int NOMBRE_ECHANTILLONS = (int)(Math.ceil(cellules.length/(double)Options.INCREMENT_MINIMAP) * Math.ceil(cellules[0].length/(double)Options.INCREMENT_MINIMAP));
        typeMoyen = new TypeCase[NOMBRE_ECHANTILLONS];
        symboleMoyen = new TypeSymbole[NOMBRE_ECHANTILLONS];
        int index = 0;
        Cellule[] voisins;
        for (int i=0; i < cellules.length; i+=Options.INCREMENT_MINIMAP) {
            for (int j=0; j < cellules[i].length; j+=Options.INCREMENT_MINIMAP) {
                voisins = Voisins.obtenirVoisins(cellules, i, j, 2);
                typeMoyen[index] = obtenirTypeRepresentatif(voisins);
                symboleMoyen[index] = obtenirSymboleRepresentatif(voisins);
                
                index ++;
            }
        }
    }

    private TypeCase obtenirTypeRepresentatif(Cellule[] echantillon) {
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

    private TypeSymbole obtenirSymboleRepresentatif(Cellule[] echantillon) {
        HashMap<TypeSymbole, Integer> observations = new HashMap<TypeSymbole, Integer>(echantillon.length);
        for (Cellule c: echantillon) {
            if (c != null) {
                if (observations.containsKey(c.obtenirSymbole().type)) {
                    Integer nombreObservations = observations.get(c.obtenirSymbole().type);
                    nombreObservations += 1;
                } else
                    observations.put(c.obtenirSymbole().type, 1);
            }
        }

        Entry<TypeSymbole, Integer> individuMax = null;
        for (Entry<TypeSymbole, Integer> individu : observations.entrySet()) {
            if (individuMax == null || individu.getValue().compareTo(individuMax.getValue()) > 0)
                individuMax = individu;
        };
        return individuMax.getKey();
    }
    
    public void majRayon(Integer rayonDeSelection) {
        this.rayonDeSelection = rayonDeSelection;
    }

    // Minijeu extraction
    public void majEtatMinijeuExtraction(Etat etatMinijeuExtraction) {
        this.etatMinijeuExtraction = etatMinijeuExtraction;
    }
    public void majPositionCurseurExtraction(double positionCurseurExtraction) {
        this.positionCurseurExtraction = positionCurseurExtraction;
    }
	public void majSensVariationExtraction(boolean sensVariationExtraction) {
        this.sensVariationExtraction = sensVariationExtraction;
	}
    public void majResultat(String resultat) {
        this.resultat = resultat;
    }

    // Minijeu laser
    public void majEtatMinijeuLaser(Etat etatMinijeuLaser) {
        this.etatMinijeuLaser = etatMinijeuLaser;
    }
    public void majChronometreMinijeuLaser(String chronometreMinijeuLaser) {
        this.chronometreMinijeuLaser = chronometreMinijeuLaser;
    }
    public void majNombreErreursLaser(int nombreErreursLaser) {
        this.nombreErreursLaser = nombreErreursLaser;
    }

    // Par Martin Larsson, https://stackoverflow.com/questions/17544157/generate-n-colors-between-two-colors
    private Color mixColors(Color color1, Color color2, double percent){
        double inverse_percent = 1.0 - percent;
        int redPart = (int) (color1.getRed()*percent + color2.getRed()*inverse_percent);
        int greenPart = (int) (color1.getGreen()*percent + color2.getGreen()*inverse_percent);
        int bluePart = (int) (color1.getBlue()*percent + color2.getBlue()*inverse_percent);
        return new Color(redPart, greenPart, bluePart);
    }
    public void majArrierePlan(ArrierePlan arrierePlan) {
        this.arrierePlan = arrierePlan;
    }
    public LinkedList<JComponent> obtenirComposants() {
        LinkedList<JComponent> composants = new LinkedList<JComponent>();
        composants.add(panneauPause);
        composants.add(panneauMission.obtenirBouton());
        composants.add(panneauDeControle.obtenirBouton());
        return composants;
    }

    public void majInformerJoueur(String informerJoueur) {
        this.informerJoueur = informerJoueur;
    }
    public void majVictoire(boolean victoire) {
        this.victoire = victoire;
    }
    public void majDefaite(boolean defaite) {
        this.defaite = defaite;
    }
}
