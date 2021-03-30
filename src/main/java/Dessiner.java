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

import javax.swing.JPanel;

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

    public Dessiner(boolean affichagePanneauDeControle){
        this.affichagePanneauDeControle = affichagePanneauDeControle;
        if (affichagePanneauDeControle) {
            panneauDeControle = new ControlPanel(10,10);
            panneauDeControle.setLayout(null);
            add(panneauDeControle);
        }
    }
    public Dessiner(){
        this(false);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();
        AffineTransform ancienneTransformation = g2d.getTransform();
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
            Dimension caseJoueur = joueur.obtenirCase();
            voisins = Voisins.obtenirVoisins(cellules, (int)caseJoueur.getWidth(), (int)caseJoueur.getHeight(), rayonDeSelection);
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
            g2d.setTransform(ancienneTransformation);

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
            ancienneTransformation.translate(
                (Options.POSITION_X_MINIMAP*largeurEcran - (cellules[0][0].xpoints[0] + largeurEcran/2 + Options.LARGEUR_CASE/4)*Options.ZOOM_MINIMAP),
                (Options.POSITION_Y_MINIMAP*hauteurEcran - (cellules[0][0].ypoints[0] + hauteurEcran/2)*Options.ZOOM_MINIMAP)
            );

            ancienneTransformation.scale(Options.ZOOM_MINIMAP, Options.ZOOM_MINIMAP);
            ((Graphics2D) g2d).setTransform(ancienneTransformation);
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
        }

    }
    public void majCellules(Cellule[][] cellules) {
        this.cellules = cellules;
        majTailleMinimap();

        // Déboggage de l'obtention des voisins (pour déterminer la cellule représentative)
        /*
        int i = 0;
        int j = 10;
        for (int ligne = 0; ligne < voisins.length; ligne++) {
            if (j%2==0) {
                ligneTmp = i+indexVoisinsColonnePaire[ligne][0];
                colonneTmp = j+indexVoisinsColonnePaire[ligne][1];
                if (ligneTmp > -1 && ligneTmp < cellules.length && colonneTmp > -1 && colonneTmp < cellules[0].length) {
                    voisins[ligne] = cellules[ligneTmp][colonneTmp];
                    System.out.println(ligneTmp+" "+colonneTmp);
                }
            } else {
                ligneTmp = i+indexVoisinsColonneImpaire[ligne][0];
                colonneTmp = j+indexVoisinsColonneImpaire[ligne][1];
                if (ligneTmp > -1 && ligneTmp < cellules.length && colonneTmp > -1 && colonneTmp < cellules[0].length) {
                    voisins[ligne] = cellules[ligneTmp][colonneTmp];
                    System.out.println(ligneTmp+" "+colonneTmp);
                }
            }
        }
        System.out.println();
        System.out.println();
        TypeCase typeRepresentatif = obtenirCelluleRepresentative(voisins);
        */
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
}
