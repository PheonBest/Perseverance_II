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
			this.setSize(700,1000);
			this.setLocation(largeur/2,hauteur/2);
			this.setLayout(null);
			this.setTitle("Regles du jeu");
			
			//panneau haut 
			
			panneau1= new JPanel();
			panneau1.setLayout(null);
			panneau1.setBounds(0,0,700,75);
			panneau1.setBackground(new Color(175,175,175));
			panneau1.setVisible(true); 
			
			this.add(panneau1);
			
			// texte règles du jeu
			
			regle= new JLabel("REGLES DU JEU");
			regle.setFont(new Font("Serif", Font.BOLD, 30));
			regle.setForeground(Color.BLACK);
			regle.setBounds(100, 5, 300, 70);
			panneau1.add(regle);
			
			//panneau bas
			
			panneau= new JPanel();
			panneau.setLayout(null);
			panneau.setBounds(0,75,700,900);
			panneau.setBackground(new Color(220,220,220));
			panneau.setVisible(true);
			
			this.add(panneau);
			
		
			
			JLabel texteRegle= new JLabel(" ");
			texteRegle.setText("<html> <center> Après le succès du robot PERSEVERANCE sur Mars, l'humanité a décidé d’envoyer un <i>rover</i> sur une exoplanète : <font color='gray'>PERSEVERANCE II</font> <br>"
			+"Le meilleur technicien pour le controler est mis sur le coup : Vous ! L’arrivée des futurs colons dépend de la réussite de ces missions. L’objectif est donc de découvrir si les éléments indispensables à la vie sont présents sur cette nouvelle planète. <br>"
			+"<br>"
			+"<hr>"
            +"<i><u>EXPLORATION</u></i> <br>"
            +"<br>"
			+"Au cours des aventures du robot, vous allez devoir diriger ses déplacements pour explorer la planète en cliquant sur les cases alentour. Ce faisant, vous découvrirez moultes paysages exotiques. Vous reconnaîtrez des paysages marins, désertiques, montagneux, enneigés ou plats. Comme vous vous en doutez, l'exploration sera difficile, les sables mouvants vous ralentissent, et les mers et cours d’eau seront infranchissables, tout comme les montagnes. De plus, vous devrez faire attention au relief et éviter les gouffres et les ravins. <br>"
			+"<br>"
            +"<hr>"
            +"<i><u>MISSIONS ET GESTION DU ROVER</u></i> <br>"
            +"<br>"
            +"Afin de verifier si la planete est habitable, vous devez vous assurez que les <u>éléments indispensables à la vie </u> (minerais, bactéries) sont présents sur la planète et parcourir au moins <u>70% de la carte</u>. Ces missions vous seront visibles depuis le menu MISSIONS. Mais attention, l’exploration sera périlleuse ! Surveillez bien l’état de la batterie et des composants du robot : si la batterie tombe en rade ou si trop de composants sont défectueux, vous perdez le contact avec le rover ! Dans le menu DETAIL, il sera possible de suivre l'evolution de la partie et l'etat du robot. <br>" 
            +"<br>"
            +"<hr>"
            +"<i><u>MAINTANCE ET OBJETS</u></i> <br>"
            +"<br>"
			+"Rassurez-vous ! Vous allez bénéficier d’un peu d’aide ! Dans la capsule dans laquelle a été envoyée le robot, nous avons chargé des caisses de composants robotiques de rechanges, des batteries de rechanges ainsi que des chenilles de montagnes et des ponts auto-dépliables. Mais il semble que la capsule se soit détériorée à l’approche de l'exoplanète et que les constituants s’y sont éparpillés à sa surface. Retrouvez-les vite si vous voulez maintenir le contact ! <br>"
            +"<br>"
			+"<hr>"
            +"<i><u>SYMBOLE</u></i> <br>"
            +"<br>"
            +"Les sources de vies bactériennes, minerais, objets et caisses de ravitaillement sont présents sous forme de symboles “?”. Pour découvrir de quel symbole il s’agit, vous allez devoir les scanner à l’aide de l’outil scanner puis utiliser le grappin pour les extraire. Ces deux outils sont disponibles sur la gauche de l’écran de jeu et vous demanderont une concentration accrue lorsque vous les utiliserez. </center>");
            
			texteRegle.setFont(new Font("Serif", Font.BOLD, 15));
			texteRegle.setForeground(Color.BLACK);
			texteRegle.setBounds(10, 10, 650, 850);
			panneau.add(texteRegle);
			
			//bouton quitter 
			
			quitter= new JButton("QUITTER");
			quitter.setBounds(500,15,100,50);
			quitter.setBackground(Color.white);
			panneau1.add(quitter);
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

