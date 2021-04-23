import java.awt.Color;
import java.awt.Font;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
public class RegleJeu extends JFrame implements ActionListener {
	
		private int largeur;
		private int hauteur;
	
        private JScrollPane texteDeroulant;
        private JLabel texteRegle;
		private JButton quitter;
	
		
		public RegleJeu() {
			
			super();
			this.setSize(635,600);
			this.setLocation(largeur/2,hauteur/2);
			this.setLayout(null);
			this.setTitle("Règles du jeu");
			
			//panneau haut 
			
			JPanel panneau1= new JPanel();
			panneau1.setLayout(null);
			panneau1.setBounds(0,0,650,75);
			panneau1.setBackground(new Color(175,175,175));
			panneau1.setVisible(true); 
			
			this.add(panneau1);
			
			// texte règles du jeu
			
			JLabel regle= new JLabel("RÈGLES DU JEU");
			regle.setFont(new Font("Serif", Font.BOLD, 30));
			regle.setForeground(Color.BLACK);
			regle.setBounds(175, 5, 300, 70);
			regle.setHorizontalAlignment(SwingConstants.CENTER);
			panneau1.add(regle);
			
			//panneau bas
			
			JPanel panneau= new JPanel();
			panneau.setLayout(null);
			panneau.setBounds(0,75,650,800);
			panneau.setBackground(new Color(220,220,220));
			panneau.setVisible(true);
			
			this.add(panneau);
			
		
			
			texteRegle= new JLabel();
			texteRegle.setText("<html><center> Après le succès du robot PERSEVERANCE sur Mars, <br>l'humanité a décidé d’envoyer un <i>rover</i> sur une exoplanète : <font color='gray'>PERSEVERANCE II</font> <br>"
			+"Le meilleur technicien pour le controler est mis sur le coup : Vous ! <br>L’arrivée des futurs colons dépend de la réussite de ces missions. <br>L’objectif est donc de découvrir si les éléments indispensables à la vie <br>sont présents sur cette nouvelle planète. <br>"
			+"<br>"
			+"<hr>"
            +"<i><u>EXPLORATION</u></i> <br>"
            +"<br>"
			+"Au cours des aventures du robot, vous allez devoir diriger ses déplacements <br>pour explorer la planète en cliquant sur les cases alentour. Ce faisant, <br>vous découvrirez moultes paysages exotiques. Vous reconnaîtrez des paysages marins, <br> désertiques, montagneux, enneigés ou plats. Comme vous vous en doutez, <br>l'exploration sera difficile, les sables mouvants vous ralentissent, <br>et les mers et cours d’eau seront infranchissables, tout comme les montagnes.<br>De plus, vous devrez faire attention au relief et éviter les gouffres et les ravins. <br>"
			+"<br>"
            +"<hr>"
            +"<i><u>MISSIONS ET GESTION DU ROVER</u></i> <br>"
            +"<br>"
            +"Afin de verifier si la planete est habitable, vous devez vous assurez <br>que les <u>éléments indispensables à la vie </u> (minerais, bactéries) sont présents <br>sur la planète et parcourir au moins <u>70% de la carte</u>. Ces missions <br>vous seront visibles depuis le menu MISSIONS. Mais attention, l’exploration <br>sera périlleuse ! Surveillez bien l’état de la batterie et des composants <br>du robot : si la batterie tombe en rade ou si trop de composants sont défectueux, <br>vous perdez le contact avec le rover ! Dans le menu DETAIL, il <br>sera possible de suivre l'evolution de la partie et l'etat du robot. <br>" 
            +"<br>"
            +"<hr>"
            +"<i><u>MAINTANCE ET OBJETS</u></i> <br>"
            +"<br>"
			+"Rassurez-vous ! Vous allez bénéficier d’un peu d’aide ! Dans la capsule <br>dans laquelle a été envoyée le robot, nous avons chargé des caisses de composants <br>robotiques de rechanges, des batteries de rechanges ainsi que des chenilles <br>de montagnes et des ponts auto-dépliables. Mais il semble que la capsule se <br>soit détériorée à l’approche de l'exoplanète et que les constituants s’y <br>sont éparpillés à sa surface. Retrouvez-les vite si vous voulez maintenir le contact ! <br>"
            +"<br>"
			+"<hr>"
            +"<i><u>SYMBOLE</u></i> <br>"
            +"<br>"
            +"Les sources de vies bactériennes, minerais, objets et caisses de <br>ravitaillement sont présents sous forme de symboles ?. Pour découvrir de quel <br>symbole il s’agit, vous allez devoir les scanner à l’aide de l’outil scanner <br>puis utiliser le grappin pour les extraire. Ces deux outils sont disponibles sur la <br>gauche de l’écran de jeu et vous demanderont une concentration accrue <br>lorsque vous les utiliserez. </center> <br>");
            
			texteRegle.setFont(new Font("Serif", Font.BOLD, 15));
            texteRegle.setBounds(0,0,100,900);
			texteRegle.setForeground(Color.BLACK);
            
            texteDeroulant = new JScrollPane(texteRegle);
            texteDeroulant.setBounds(10,10,600,400);
			texteDeroulant.getVerticalScrollBar().setUnitIncrement(16); // On change la vitesse de déplacement vertical (vitesse de scroll)
			panneau.add(texteDeroulant);
			
			//bouton quitter 
			
			quitter= new JButton("QUITTER");
			quitter.setBounds(250,425,100,50);
			quitter.setBackground(Color.white);
			panneau.add(quitter);
			quitter.addActionListener(this);
			
			
		
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

