import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.event.ActionEvent;
import java.awt.Graphics;

public class Menu extends JPanel {
    private BoutonCercle[] boutonsCercle;
    private Cellule[] boutonsType = new Cellule[TypeCase.values().length];
    private Cellule[] boutonsSymbole = new Cellule[TypeSymbole.values().length];
    private JButton enregistrer = new JButton("Enregistrer");
    private Action actionEnregistrer = new AbstractAction("Enregistrer") {
        @Override
        public void actionPerformed(ActionEvent e) {
            controleur.enregistrer();
        }
    };
    private JButton menu = new JButton("Menu");
    private Action actionMenu = new AbstractAction("Menu") {
        @Override
        public void actionPerformed(ActionEvent e) {
            controleur.retourMenu();
        }
    };
    private Controleur controleur;

    public Menu(Controleur controleur) {
        super();
        this.controleur = controleur;
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

        final int LARGEUR_BOUTON = largeurMenu/2;
        final int HAUTEUR_BOUTON = 50;
        // Par défaut, lorsqu'on clique sur le bouton,
        // on place le focus sur le bouton.
        // Or cela empêche la saisie des touches du clavier.
        // Donc on n'empêche le focus sur le bouton
        enregistrer.setFocusable(false);
        enregistrer.addActionListener(actionEnregistrer);
        enregistrer.setBounds((largeurMenu-LARGEUR_BOUTON)/2,(520)-HAUTEUR_BOUTON/2,LARGEUR_BOUTON,HAUTEUR_BOUTON);
        add(enregistrer);

        menu.addActionListener(actionMenu);
        menu.setBounds((largeurMenu-LARGEUR_BOUTON)/2,(520+HAUTEUR_BOUTON)-HAUTEUR_BOUTON/2,LARGEUR_BOUTON,HAUTEUR_BOUTON);
        add(menu);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
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
        if (boutonsSymbole != null) {
            for (Cellule c: boutonsSymbole) {
                if (c != null)
                    c.dessiner(g);
            }
        }
    }

    public void majBoutonsSymbole(Cellule[] boutonsSymbole) {
        this.boutonsSymbole = boutonsSymbole;
    }

    public void majBoutonsType(Cellule[] boutonsType) {
        this.boutonsType = boutonsType;
    }

    public void majBoutonsCercle(BoutonCercle[] boutonsCercle) {
        this.boutonsCercle = boutonsCercle;
    }

}
