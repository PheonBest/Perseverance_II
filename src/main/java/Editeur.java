import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class Editeur extends JPanel {
    private JPanel carte;
    private JPanel menu;

    public Editeur(Controleur controleur, int largeur, int hauteur) {

        carte = new Dessiner(controleur, false, largeur, hauteur);
        final int LARGEUR_MENU = (int)(largeur/Options.RATIO_LARGEUR_MENU);
        final int LARGEUR_CARTE = (int)(LARGEUR_MENU*(Options.RATIO_LARGEUR_MENU-1));
        menu = new Menu(controleur, LARGEUR_MENU);
        carte.setBounds(0, 0, LARGEUR_CARTE, hauteur);
        menu.setBounds(LARGEUR_CARTE, 0, LARGEUR_MENU, hauteur);
        
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
    }

    public void majCellules(Cellule[][] cellules) {
        ((Dessiner)carte).majCellules(cellules);
    }
    public void majBoutonsSymbole(Cellule[] boutonsSymbole) {
        ((Menu)menu).majBoutonsSymbole(boutonsSymbole);
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

    public void majArrierePlan(ArrierePlan arrierePlan) {
        ((Dessiner)carte).majArrierePlan(arrierePlan);
    }

    public JPanel obtenirPanneauMenu() {
        return menu;
    }

    public JPanel obtenirPanneauJeu() {
        return carte;
    }
}
