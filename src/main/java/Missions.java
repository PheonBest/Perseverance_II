import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Missions extends JFrame {
    
    private int lx = 650;
    private int ly = 600;
    private JPanel panneauTitre;
    private JLabel titre;
    //private Robot joueur;
    //------------------------------------------------------------------ Constructeur

    public Missions(int x, int y){
        // Fenêtre
        super();
        //joueur = r;
        setLayout(null);
        setLocation(x,y);
        setSize(lx+20,ly+40);
        setTitle("MISSIONS");
        
        JPanel panneauFond= new JPanel();
        panneauFond.setBounds(0,0,lx,ly);
		panneauFond.setBackground(new Color(175,175,175));
		panneauFond.setLayout(null);
		this.add(panneauFond);
		
		panneauTitre= new JPanel();
		panneauTitre.setBounds(10,10,lx-20,50);
		panneauTitre.setBackground(new Color(220,220,220));
        panneauTitre.setLayout(null);
        panneauFond.add(panneauTitre);
        
        titre = new JLabel("------------------ MISSIONS ------------------");
        titre.setBounds(0,0,lx-20,40);
        titre.setFont(new Font("Courier",Font.BOLD,18));
        titre.setHorizontalAlignment(SwingConstants.CENTER);
        titre.setVerticalAlignment(SwingConstants.CENTER);
        panneauTitre.add(titre);
        
		JPanel panneauObjectif= new JPanel();
		panneauObjectif.setBounds(10,70,lx-20,75);
		panneauObjectif.setBackground(new Color(220,220,220));
		panneauObjectif.setLayout(null);
		panneauFond.add(panneauObjectif);
       
        JLabel texteMission= new JLabel("  N'oubliez pas: l'arrivee des futurs colons depend de la reussite de vos missions!" );
        texteMission.setBounds(0,20,lx-20,20);
        texteMission.setFont(new Font("Courier",Font.BOLD,12));
        texteMission.setHorizontalAlignment(SwingConstants.CENTER);
        //texteMission.setVerticalAlignment(SwingConstants.CENTER);
        panneauObjectif.add(texteMission);
        
        JPanel mission1= new JPanel();
        mission1.setBounds(10,155,lx-20,200);
		mission1.setBackground(new Color(220,220,220));
        mission1.setLayout(null);
        panneauFond.add(mission1);
        
        JPanel mission2= new JPanel();
        mission2.setBounds(10,355+10,lx-20,200);
		mission2.setBackground(new Color(220,220,220));
        mission2.setLayout(null);
        panneauFond.add(mission2);
       
		JLabel premMission= new JLabel("MISSION 1: decouvrir les symboles suivants: minerais et bacteries");
		premMission.setFont(new Font("Courier",Font.BOLD+Font.ITALIC,15));
		premMission.setBounds(5,5,lx,20);
		mission1.add(premMission);
		
		final Image IMG = TailleImage.resizeImage(Donnees.imagesSymboles.get(TypeSymbole.MINERAI.name()),100,100,true);
		JLabel monImage1 = new JLabel(new ImageIcon(IMG));
		monImage1.setBounds(160,50,100,100);
		mission1.add(monImage1);
		
		final Image IMG2 = TailleImage.resizeImage(Donnees.imagesSymboles.get(TypeSymbole.BACTERIE.name()),100,100,true);
		JLabel monImage2 = new JLabel(new ImageIcon(IMG2));
		monImage2.setBounds(370,50,100,100);
		mission1.add(monImage2);
		
		
		
		JLabel deuMission= new JLabel("MISSION 2: parcourir au moins 70% de la carte ");
		deuMission.setFont(new Font("Courier",Font.BOLD+Font.ITALIC,15));
		deuMission.setBounds(5,5,lx,20);
		mission2.add(deuMission);
		/*
		JLabel nbKmTot = new JLabel("Distance totale parcourue : "+(joueur.obtenirPExploration()+" km"));
        nbKmTot.setLayout(null);
        nbKmTot.setLocation(30,50);
        nbKmTot.setSize(400,30);
        nbKmTot.setFont(new Font("Courier", Font.BOLD + Font.ITALIC, 16));
        mission2.add(nbKmTot);
        */
	}
	
}
        
        

