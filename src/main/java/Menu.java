import java.util.ArrayList;

import javax.swing.JPanel;

import java.awt.Graphics;

public class Menu extends JPanel {
    private ArrayList<BoutonCercle> taillePinceau;
    private Cellule[] boutonsType;

    public Menu() {
        super();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!taillePinceau.isEmpty()) {
            for (BoutonCercle b: taillePinceau)
                b.dessiner(g);
        }
        for (Cellule c: boutonsType) {
            if (c != null)
                c.dessiner(g);
        }
    }

    public void majBoutons(ArrayList<BoutonCercle> taillePinceau) {
        this.taillePinceau = taillePinceau;
    }

    public void majTypeCase(Cellule[] boutonsType) {
        this.boutonsType = boutonsType;
    }

}
