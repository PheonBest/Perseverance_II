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
    private JLabel labelBacterie;
    private JLabel labelMinerai;
    private JLabel labelExploration;
    //private Robot joueur;
    //------------------------------------------------------------------ Constructeur

    public Missions(int x, int y){
        // Fenêtre
        super();
        //this.joueur = r;
        setLayout(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
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
       
        JLabel texteMission= new JLabel("  N'oubliez pas: l'arrivée des futurs colons dépend de la réussite de vos missions!" );
        texteMission.setBounds(0,20,lx-20,20);
        texteMission.setFont(new Font("Courier",Font.BOLD,12));
        texteMission.setHorizontalAlignment(SwingConstants.CENTER);
        //texteMission.setVerticalAlignment(SwingConstants.CENTER);
        panneauObjectif.add(texteMission);
        
        final int BORDURE = 10;
        final int LARGEUR_PANNEAU = (int)((lx-BORDURE)/2-BORDURE); //Largeur = lx/2 -largeurGauche - largeurDroite
        JPanel missionBacterie= new JPanel();
        missionBacterie.setBounds(BORDURE,155,LARGEUR_PANNEAU,200);
		missionBacterie.setBackground(new Color(220,220,220));
        missionBacterie.setLayout(null);
        panneauFond.add(missionBacterie);

        JPanel missionMinerai= new JPanel();
        missionMinerai.setBounds((int)((lx+BORDURE)/2),155,LARGEUR_PANNEAU,200);
		missionMinerai.setBackground(new Color(220,220,220));
        missionMinerai.setLayout(null);
        panneauFond.add(missionMinerai);
        
        JPanel missionExploration= new JPanel();
        missionExploration.setBounds(10,155+200+BORDURE,lx-2*BORDURE,200);
		missionExploration.setBackground(new Color(220,220,220));
        missionExploration.setLayout(null);
        panneauFond.add(missionExploration);
       
		labelBacterie= new JLabel("<html><font color='red'>X</font> MISSION 1:<br>Découvrir une forme<br>de vie bactérienne :</html>");
		labelBacterie.setFont(new Font("Courier",Font.BOLD+Font.ITALIC,15));
		labelBacterie.setBounds(5,10,lx,60);
		missionBacterie.add(labelBacterie);

        final Image IMAGE_BACTERIE = TailleImage.resizeImage(Donnees.imagesSymboles.get(TypeSymbole.BACTERIE.name()),100,100,true);
		JLabel conteneurImageBacterie = new JLabel(new ImageIcon(IMAGE_BACTERIE));
		conteneurImageBacterie.setBounds((LARGEUR_PANNEAU-100)/2,65,100,100);
		missionBacterie.add(conteneurImageBacterie);

        labelMinerai= new JLabel("<html><font color='red'>X</font> MISSION 2:<br>Découvrir un minerai :</html>");
		labelMinerai.setFont(new Font("Courier",Font.BOLD+Font.ITALIC,15));
		labelMinerai.setBounds(5,10,lx,40);
		missionMinerai.add(labelMinerai);
		
		final Image IMAGE_MINERAI = TailleImage.resizeImage(Donnees.imagesSymboles.get(TypeSymbole.MINERAI.name()),100,100,true);
		JLabel conteneurImageMinerai = new JLabel(new ImageIcon(IMAGE_MINERAI));
		conteneurImageMinerai.setBounds((LARGEUR_PANNEAU-100)/2,65,100,100);
		missionMinerai.add(conteneurImageMinerai);

		labelExploration = new JLabel("<html><font color='red'>X</font> MISSION 3: parcourir au moins 70% de la carte<br>Exploration de la planète : <html><font style='font-weight: bold'>0 %</font></html>");
		labelExploration.setFont(new Font("Courier",Font.BOLD+Font.ITALIC,15));
		labelExploration.setBounds(5,10,lx,40);
		missionExploration.add(labelExploration);
		/*
		JLabel nbKmTot = new JLabel("Distance totale parcourue : "+(joueur.obtenirPExploration()+" km"));
        nbKmTot.setLayout(null);
        nbKmTot.setLocation(30,50);
        nbKmTot.setSize(400,30);
        nbKmTot.setFont(new Font("Courier", Font.BOLD + Font.ITALIC, 16));
        missionExploration.add(nbKmTot);
        */
	}
    public void majExploration(int pourcentage) {
        String color = "red";
        if (pourcentage >= 70)
            color = "green";
        labelExploration.setText("<html><font color='"+color+"'>✔</font> MISSION 3: parcourir au moins 70% de la carte<br>Exploration de la planète : <html><font style='font-weight: bold'>"+pourcentage+" %</font></html>");
    }
    public void majBacterie() {
        labelBacterie.setText("<html><font color='lime'>✔</font> MISSION 1:<br>Découvrir une forme<br>de vie bactérienne :</html>");
    }
    public void majMinerai() {
        labelMinerai.setText("<html><font color='lime'>✔</font> MISSION 2:<br>Découvrir un minerai :</html>");
    }
	
}
        
        

