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

import java.awt.Color;

public class ModeDeJeu extends JPanel implements ActionListener {
	
    private Controleur controleur;
    private HashMap<String, InputStream> cartes;
    private JList<String> liste = new JList<>();
    DefaultListModel<String> modele = new DefaultListModel<>();
   
	
    private String carteActive = null;
    

    private JPanel panneauAffichage;
    private JPanel panneauBas;
    private JButton jouer;
    private JButton editer;
    private int largeur;
    private int hauteur;
	private JLabel selection;
	private JScrollPane j;
	
    public ModeDeJeu(Controleur controleur) {
        super();
        this.controleur = controleur;
        this.setLayout(null);
    }

    public void majCartes(HashMap<String, InputStream> cartesEtNoms) {
		System.out.println(cartesEtNoms.values().size());
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
    
    public void majTaille(int largeur, int hauteur){
		this.setSize(largeur,hauteur);
		this.largeur=largeur;
		this.hauteur=hauteur;
	}
	
	
	 public void actionPerformed(ActionEvent e) {
		 if(liste.getSelectedValue()!=null){
			if (e.getSource()== jouer){
				controleur.jouer(cartes.get(liste.getSelectedValue()));
				
			}
			/*
			else if (e.getSource()== editer){
				controleur.editer(cartes.get(liste.getSelectedValue()));
			}
			*/
		}
	}
	
	public void initialiser(){
		
		//panneau haut
		panneauAffichage= new JPanel();
		panneauAffichage.setBounds(0,0,largeur,50);
		panneauAffichage.setBackground(Color.gray);
		panneauAffichage.setVisible(true);
		this.add(panneauAffichage);
		
		//selection de la carte 
		selection= new JLabel();
		selection.setText("SELECTION DE LA CARTE");
		selection.setBounds(400,20,320,50);
		panneauAffichage.add(selection);
		
		//panneau bas 
		panneauBas= new JPanel();
		panneauBas.setBounds(0,300,largeur,400);
		panneauBas.setBackground(Color.gray);
		panneauBas.setVisible(true);
		panneauBas.setLayout(null);
		this.add(panneauBas);
		
		//bouton jouer
		jouer = new JButton("Jouer");
		jouer.setBounds(300,50,100,100);
		panneauBas.add(jouer);
		jouer.addActionListener(this);
		
		//bouton éditer
		editer= new JButton("Editer");
		editer.setBounds(600,50,100,100);
		panneauBas.add(editer);
		editer.addActionListener(this);
		
		
        liste.setModel(modele);
        liste.setVisibleRowCount(5);
        
        j=new JScrollPane(liste);
        final int LARGEUR_LISTE = 100;
        final int HAUTEUR_LISTE = 100;
        j.setBounds((largeur-LARGEUR_LISTE)/2,(hauteur-HAUTEUR_LISTE)/2,LARGEUR_LISTE,HAUTEUR_LISTE);
        j.setVisible(true);
        this.add(j);
	}

 

}
