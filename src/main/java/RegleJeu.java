import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class RegleJeu extends JFrame implements ActionListener {
	
		private int largeur;
		private int hauteur;
		private JPanel panneau1;
		private JPanel panneau;
		private JLabel regle;
		private JButton quitter;
		private JLabel monImage;
		
		public RegleJeu() {
			
			super();
			this.setSize(500,640);
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
			panneau.setBounds(0,75,500,525);
			panneau.setBackground(new Color(220,220,220));
			panneau.setVisible(true);
			
			this.add(panneau);
			
		
			
			JLabel texteRegle= new JLabel(" ");
			texteRegle.setText("<html> <center> L'humanite a decide d envoyer un <i>rover</i> sur une exoplanete : <font color='gray'>PERSEVERANCE II</font> <br>"
			+"Le meilleur technicien pour le controler est mis sur le coup : Vous! <br>"
			+"<br>"
			+"<hr>"
			+"Afin de verifier si la planete est habitable, vous devez: </center> <br>"
			+" - vous assurez que les <u>elements indispensables a la vie </u> (minerais, bacteries et...) sont presents sur la planete <br>" 
			+" - parcourir au moins <u>70% de la carte</u> <br>"
			+"<br>"
			+"<center>Pour se deplacer, il suffit de cliquer sur les cases a proximite du robot. Attention toutefois a ne pas trop se precipiter: des pieges sont presents ( ravins...) et ils peuvent grandement deteriorer les composantes du robot. Heureusement, des mini-jeux sont la pour vous aider a regagner des points de vie! <br>"
			+"Dans le menu DETAIL, il sera possible de suivre l'evolution de la partie et l'etat du robot. <br>" 
			+"<hr>"
			+"<br>"
			+"Si vous souhaitez commencer une nouvelle partie, selectionnez une carte dans le menu deroulant puis cliquez sur JOUER, sinon cliquez sur EDITER pour creer votre propre carte.</center>");
			texteRegle.setFont(new Font("Serif", Font.BOLD, 15));
			texteRegle.setForeground(Color.BLACK);
			texteRegle.setBounds(10, 10, 450, 450);
			panneau.add(texteRegle);
			
			//bouton quitter 
			
			quitter= new JButton("QUITTER");
			quitter.setBounds(360,460,100,50);
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

