import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.InputStream;

public class ModeDeJeu extends JPanel {
    private Controleur controleur;
    private HashMap<String, InputStream> cartes;
    private JList<String> liste = new JList<>();
    DefaultListModel<String> modele = new DefaultListModel<>();
    private JButton jouer = new JButton("Jouer");
    private Action actionJouer = new AbstractAction("Jouer") {
        @Override
        public void actionPerformed(ActionEvent e) {
            controleur.jouer(cartes.get(liste.getSelectedValue()));
        }
    };
    private JButton editer = new JButton("Editer");
    private Action actionEditer = new AbstractAction("Editer") {
        @Override
        public void actionPerformed(ActionEvent e) {
            controleur.editer(cartes.get(liste.getSelectedValue()));
        }
    };
    private String carteActive = null;
    

    public ModeDeJeu(Controleur controleur) {
        super();

        this.controleur = controleur;

        liste.setModel(modele);
        liste.setVisibleRowCount(5);
        /*
        liste.getSelectionModel().addListSelectionListener(e -> {
            carteActive = liste.getSelectedValue();
        });
        */
        add(new JScrollPane(liste));
        jouer.addActionListener(actionJouer);
        add(jouer);
        editer.addActionListener(actionEditer);
        add(editer);

    }

    public void majCartes(HashMap<String, InputStream> cartesEtNoms) {
        // On veut trier les cartes par ordre alphabétique
        List<String> nomCartes = cartesEtNoms.entrySet()
                    .stream()
                    .sorted((p1,p2) -> p1.getKey().compareTo(p2.getKey()))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        cartes = cartesEtNoms;
        //JLabel
        modele.clear();
        for (String s: nomCartes)
            modele.addElement(s);
        
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

}
