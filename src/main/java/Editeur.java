import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Editeur extends JPanel {
    private JPanel carte = new Dessiner();
    private JPanel menu = new Menu();
    private Controleur controleur;

    public Editeur(Controleur controleur) {
        this.controleur = controleur;
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
        menu.repaint();
    }

    public void initialiser(int largeur, int hauteur) {
        final int LARGEUR_MENU = (int)(largeur/Options.RATIO_LARGEUR_MENU);
        final int LARGEUR_CARTE = (int)(LARGEUR_MENU*(Options.RATIO_LARGEUR_MENU-1));
        ((Dessiner)carte).majLargeur(largeur);
        ((Dessiner)carte).majHauteur(hauteur);
        carte.setBounds(0, 0, LARGEUR_CARTE, hauteur);
        menu.setBounds(LARGEUR_CARTE, 0, LARGEUR_MENU, hauteur);
        ((Menu) menu).initialiser(LARGEUR_MENU);
    }

    public void majCellules(Cellule[][] cellules) {
        ((Dessiner)carte).majCellules(cellules);
    }
    public void majBoutonsType(Cellule[] boutonsType) {
        ((Menu)menu).majBoutonsType(boutonsType);
    }
    public void majBoutonsCercle(BoutonCercle[] boutonsCercle) {
        ((Menu)menu).majBoutonsCercle(boutonsCercle);
    }

    public void majZoom(double zoom) {
        ((Dessiner)carte).majZoom(zoom);
    }

    public void majCentreZoom(Point centreZoom) {
        ((Dessiner)carte).majCentreZoom(centreZoom);
    }
}
