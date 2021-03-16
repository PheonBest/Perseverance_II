import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Editeur extends JPanel {
    private JPanel carte = new Dessiner();
    private JPanel menu = new Menu();
    private double largeurEcran;
    private double hauteurEcran;

    private final String[] infoLabels = {   "Taille de pinceau",
                                            "Type de case",
                                            "Symboles"};
    private final int[] yLabels = {   0,
                                      180,
                                      500};
    private JLabel[] labels = new JLabel[infoLabels.length];
    private Cellule[] boutonsType = new Cellule[TypeCase.values().length];

    public Editeur() {
        setLayout(null);
        menu.setLayout(null);
        carte.setLayout(null);
        carte.setBackground(Color.DARK_GRAY);
        menu.setBackground(Color.LIGHT_GRAY);
        
        add(carte);
        add(menu);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        carte.repaint();
    }

    public void initialiser(int largeur, int hauteur) {
        int quartLargeur = (int)(largeur*1./4.);
        carte.setBounds(0, 0, quartLargeur*3, hauteur);
        menu.setBounds(quartLargeur*3, 0, quartLargeur, hauteur);

        // Initialisation du menu

        //JLabel
        for (int i=0; i<infoLabels.length; i++) {
            final JLabel label = new JLabel(infoLabels[i]);
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setFont(Options.police);
            label.setBounds(0, yLabels[i], quartLargeur, 50);
            labels[i] = label;
            menu.add(label);
        }

        //BoutonsCercles
        final int NOMBRE_BOUTONS_CIRCULAIRES = 3;
        // Longueur totale des cercles:
        // Somme de i=1 à n de (R+0,25*R*i) = nR+0,25*R*n*(n+1)/2 = n*R*(1+(n+1)/8)
        // où R est le rayon initiale du cercle
        final int LONGUEUR_ESPACEMENTS = NOMBRE_BOUTONS_CIRCULAIRES*Options.LARGEUR_BOUTON_CASE*(1+(NOMBRE_BOUTONS_CIRCULAIRES+1)/8);
        final int LONGUEUR_BOUTONS = (NOMBRE_BOUTONS_CIRCULAIRES+1)*Options.ESPACE_INTER_BOUTON;
        final int LONGUEUR_TOTALE = LONGUEUR_ESPACEMENTS + LONGUEUR_BOUTONS;
        
        ArrayList<BoutonCercle> taillePinceau = new ArrayList<BoutonCercle>();
        for (int i=0; i<NOMBRE_BOUTONS_CIRCULAIRES; i++) {
            final int longueuBoutons = i*Options.LARGEUR_BOUTON_CASE*(1+(i+1)/8);
            final int x = (int)((quartLargeur-LONGUEUR_TOTALE)/2+(i+1)*Options.ESPACE_INTER_BOUTON+longueuBoutons);
            final int y = 100;
            taillePinceau.add(new BoutonCercle(x,y,Options.RAYON_BOUTON_CERCLE*(1+i*0.25)));
        }
        ((Menu)menu).majBoutons(taillePinceau);

        //BoutonsHex
        final TypeCase[] types = TypeCase.values();
        
        final double TAILLE = 0.5;
        final int NOMBRE_COLONNES = (int) Math.ceil(types.length/(double)Options.NOMBRE_BOUTONS_TYPE_PAR_LIGNE);
        final int LONGUEUR_ESPACEMENTS_CASE = (Options.NOMBRE_BOUTONS_TYPE_PAR_LIGNE+1)*Options.ESPACE_INTER_CASE;
        final int LONGUEUR_CASES = (int)(Options.NOMBRE_BOUTONS_TYPE_PAR_LIGNE*Options.LARGEUR_BOUTON_CASE*TAILLE + (Options.NOMBRE_BOUTONS_TYPE_PAR_LIGNE+1)*Options.ESPACE_INTER_CASE);
        final int LONGUEUR_TOTALE_CASES = LONGUEUR_ESPACEMENTS_CASE + LONGUEUR_CASES;
        int index = 0;
        for (int i=0; i<NOMBRE_COLONNES; i++) {
            for (int j=0; j<Options.NOMBRE_BOUTONS_TYPE_PAR_LIGNE; j++) {
                System.out.println(index);
                if (index < types.length) {
                    boutonsType[index] = new Cellule(types[index], i, j, TAILLE);
                    boutonsType[index].translate((quartLargeur-LONGUEUR_TOTALE_CASES)/2, yLabels[1]+80);
                }
                index++;
            }
        }
        
        ((Menu)menu).majTypeCase(boutonsType);
    }

    public void majCellules(Cellule[][] cellules) {
        ((Dessiner)carte).majCellules(cellules);
    }
}
