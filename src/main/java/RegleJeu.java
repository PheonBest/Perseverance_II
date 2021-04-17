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

public class RegleJeu extends JFrame implements ActionListener {
	
		private int largeur;
		private int hauteur;
		private JPanel panneau1;
		private JPanel panneau;
		private JLabel regle;
		private JButton quitter;
		private JLabel regleJeu;
		private JLabel monImage;
		
		public RegleJeu() {
			
			super();
			this.setSize(500,540);
			this.setLocation(largeur/2,hauteur/2);
			this.setLayout(null);
			this.setTitle("Regles du jeu");
			
			//panneau haut 
			
			panneau1= new JPanel();
			panneau1.setLayout(null);
			panneau1.setBounds(0,0,500,75);
			panneau1.setBackground(new Color(175,175,175));
			panneau1.setVisible(true); 
			
			this.add(panneau1);
			
			// texte règles du jeu
			
			regle= new JLabel("REGLES DU JEU");
			regle.setFont(new Font("Serif", Font.BOLD, 30));
			regle.setForeground(Color.BLACK);
			regle.setBounds(120, 5, 300, 70);
			panneau1.add(regle);
			
			//panneau bas
			
			panneau= new JPanel();
			panneau.setLayout(null);
			panneau.setBounds(0,75,500,425);
			panneau.setBackground(new Color(220,220,220));
			panneau.setVisible(true);
			
			this.add(panneau);
			
			//regles du jeu
			
			regleJeu= new JLabel("BIENVENUE");
			regleJeu.setFont(new Font("Serif", Font.BOLD, 20));
			regleJeu.setHorizontalAlignment(SwingConstants.CENTER);
			regleJeu.setForeground(Color.BLACK);
			regleJeu.setBounds(0, 100, 475, 70);
			panneau.add(regleJeu);
			
			//bouton quitter 
			
			quitter= new JButton("QUITTER");
			quitter.setBounds(350,350,100,50);
			quitter.setBackground(Color.white);
			panneau.add(quitter);
			quitter.addActionListener(this);
			
			// IMAGE ROVER
			/*
			monImage = new JLabel(new ImageIcon("./res/images/rover.png"));
			monImage.setBounds(350,50,100,100);
			panneau.add(monImage);
			*/
	
			//monImage.setIcon(new ImageIcon(new ImageIcon("./res/images/rover.png").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT)));
			//panneau.add(monImage);
			
		
		}
		
		public void majTaille(int largeur, int hauteur){
			this.setSize(largeur,hauteur);
			this.largeur=largeur;
			this.hauteur=hauteur;
		
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==quitter){
			this.setVisible(false);
		}
	}

}

