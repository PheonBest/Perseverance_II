import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.Graphics;

public class Menu extends JPanel {
    private BoutonCercle[] boutonsCercle;
    private Cellule[] boutonsType = new Cellule[TypeCase.values().length];

    public Menu() {
        super();
    }

    public void initialiser(int largeurMenu) {
        //JLabel
        for (int i=0; i<Options.infoLabels.length; i++) {
            final JLabel label = new JLabel(Options.infoLabels[i]);
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setFont(Options.police);
            label.setBounds(0, Options.yLabels[i], largeurMenu, 50);
            add(label);
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //System.out.println("Repaint");
        if (boutonsCercle != null) {
            for (BoutonCercle b: boutonsCercle) {
                if (b != null)
                    b.dessiner(g);
            }
        }
        if (boutonsType != null) {
            for (Cellule c: boutonsType) {
                if (c != null)
                    c.dessiner(g);
            }
        }
    }

    public void majBoutonsType(Cellule[] boutonsType) {
        this.boutonsType = boutonsType;
    }

    public void majBoutonsCercle(BoutonCercle[] boutonsCercle) {
        this.boutonsCercle = boutonsCercle;
    }

}
