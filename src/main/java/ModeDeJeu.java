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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.InputStream;
import javax.swing.ImageIcon;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

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
	private JButton aide;
	private RegleJeu maFenetre2;
	
    public ModeDeJeu(Controleur controleur) {
        super();
        this.controleur = controleur;
        this.setLayout(null);
        this.maFenetre2= new RegleJeu();
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
		if(e.getSource()==aide){
			maFenetre2.setVisible(true);
			
		}
		 if(liste.getSelectedValue()!=null){
			if (e.getSource()== jouer){
				controleur.jouer(cartes.get(liste.getSelectedValue()));
			}
			/*
			else if (e.getSource()== editer){
				controleur.editer(cartes.get(liste.getSelectedValue()));
			*/
			
		}
	}
	
	public void initialiser(){
		
		// fond d'ecran image
		JLabel presentation = new JLabel(new ImageIcon ("./res/images/planetes.jpg"));
		presentation.setLayout(null);
		presentation.setBounds(0,0,largeur,hauteur);
		presentation.setVisible(true);
		this.add(presentation);
		
		JLabel titre = new JLabel();
		titre.setText("PERSEVERANCE II");
		titre.setFont(new Font("Serif", Font.BOLD, 60));
        titre.setForeground(Color.WHITE);
		titre.setBounds(largeur/2-300, 20, 550, 70);
		presentation.add(titre);		
		
		JLabel titre2 = new JLabel();
		titre2.setText(" Pour plus d'informations concernant les regles du jeu, n'hesitez pas a cliquer sur ce bouton: ");
		titre2.setFont(new Font("Serif", Font.BOLD, 19));
        titre2.setForeground(Color.WHITE);
		titre2.setBounds(largeur/10, hauteur/5, 1000, 70);
		presentation.add(titre2);
		
		jouer = new JButton("Jouer");
		jouer.setBounds(largeur/3-100,2*hauteur/3,100,100);
		presentation.add(jouer);
		jouer.addActionListener(this);
		
		editer = new JButton("Editer");
		editer.setBounds(2*largeur/3, 2*hauteur/3,100,100);
		presentation.add(editer);
		editer.addActionListener(this);
		
		aide= new JButton("AIDE");
		aide.setBounds(largeur-100,hauteur/5,70,70);
		aide.setBackground(Color.orange);
		presentation.add(aide);
		aide.addActionListener(this);
		
		j=new JScrollPane(liste);
        final int LARGEUR_LISTE = 100;
        final int HAUTEUR_LISTE = 100;
        //j.setBounds(150,200,100,100);
        j.setBounds((largeur-LARGEUR_LISTE)/2,(hauteur-HAUTEUR_LISTE)/2,LARGEUR_LISTE,HAUTEUR_LISTE);
        j.setVisible(true);
        presentation.add(j);
		
		/*
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
		this.add(selection);
		
		
		//panneau bas 
		panneauBas= new JPanel();
		panneauBas.setBounds(0,300,1000,400);
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
		*/
		
        liste.setModel(modele);
        liste.setVisibleRowCount(5);
        
        
	}

 

}
