import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class FullControlPanel extends JFrame implements ActionListener{
    
    //------------------------------------------------------------------ Attributs
    
    // taille de la fenêtre
    private int lx = 900;
    private int ly = 700;
    private Robot joueur;
    public Timer t;
    private int cpt;
    
    // Panneaux principaux de la fenêtre
    private JPanel cg; // panneau principal de gauche
    private AntiBugPanel cp; // panneau principal global
    
    // Partie de gauche - Panneaux de données
    private JLabel titre;
    private int ecartY = 20; // écart entre les panneaux de gauche
    
    // Panneau de la batterie
    private JPanel panneauBat;
    private int xb = 20;
    private int yb = 60;
    private JLabel titreBat;
    private JLabel nivBat;
    private JLabel nbRecharges;
    
    // Panneau des voyants
    private JPanel panneauVoyants;
    private int ypv;
    private JLabel titreVoyant;
    
    // Panneau des statistiques
    private JPanel stat;
    private int ys;
    private JLabel titreStat;
    private JLabel nbKmTot;
    private JLabel nbCasesExp;
   
    // Partie de droite - Images
    private Image imageP;
    private Image imageR;
    
    //------------------------------------------------------------------ Constructeur

    public FullControlPanel(int x, int y, Robot r, Image imageP, Image imageR){
        // Fenêtre
        
        super();
        setLayout(null);
        setLocation(x,y);
        setSize(lx,ly);
        setTitle("Fenêtre de contrôle");
        joueur = r;
        t = new Timer(500,this);
        t.start();
        
        // Images
        this.imageP = imageP;
        this.imageR = imageR;
        
        // Panneaux principaux
        cp = new AntiBugPanel(xb, yb, yb+150+ecartY, lx, ly, joueur, imageP, imageR);
        
        cg = new JPanel();
        cg.setLayout(null);
        cg.setLocation(5,5);
        cg.setSize(lx/2,ly -50);
        cg.setBackground(new Color(175,175,175));
        
        titre = new JLabel("Donnnées en temps réel");
        titre.setLayout(null);
        titre.setLocation(80,15);
        titre.setSize(300,20);
        titre.setFont(new Font("Courier", Font.BOLD + Font.ITALIC, 18));
        cg.add(titre);
        
        //-------- Panneau de gauche
        
        // Panneau de la batterie
        panneauBat = new JPanel();
        panneauBat.setLayout(null);
        panneauBat.setLocation(xb,yb);
        panneauBat.setSize(lx/2 -2*xb,150);
        panneauBat.setBackground(new Color(220,220,220));
        
        titreBat = new JLabel("Batterie");
        titreBat.setLayout(null);
        titreBat.setLocation(150,10);
        titreBat.setSize(200,30);
        titreBat.setFont(new Font("Courier", Font.BOLD, 18));
        panneauBat.add(titreBat);
        
        nbRecharges = new JLabel("Nombre de recharges : "+this.joueur.getNbRecharges());
        nbRecharges.setLayout(null);
        nbRecharges.setLocation(30,50);
        nbRecharges.setSize(400,30);
        nbRecharges.setFont(new Font("Courier", Font.BOLD + Font.ITALIC, 16));
        panneauBat.add(nbRecharges);
        
        nivBat = new JLabel("Niveau : "+this.joueur.getBatterie()+" %");
        nivBat.setLayout(null);
        nivBat.setLocation(30,90);
        nivBat.setSize(200,30);
        nivBat.setFont(new Font("Courier", Font.BOLD + Font.ITALIC, 16));
        panneauBat.add(nivBat);
        
        // Panneau des voyants
        ypv = yb + panneauBat.getHeight() + ecartY;
        
        panneauVoyants = new JPanel();
        panneauVoyants.setLayout(null);
        panneauVoyants.setLocation(xb,ypv);
        panneauVoyants.setSize(lx/2 -2*xb,250);
        panneauVoyants.setBackground(new Color(220,220,220));
        
        titreVoyant = new JLabel("Etats des composants");
        titreVoyant.setLayout(null);
        titreVoyant.setLocation(100,10);
        titreVoyant.setSize(300,30);
        titreVoyant.setFont(new Font("Courier", Font.BOLD, 18));
        panneauVoyants.add(titreVoyant);
        
        // Panneaux des statistiques
        ys = ypv + panneauVoyants.getHeight() + ecartY;
        
        stat = new JPanel();
        stat.setLayout(null);
        stat.setLocation(xb,ys);
        stat.setSize(lx/2 -2*xb,130);
        stat.setBackground(new Color(220,220,220));
        
        titreStat = new JLabel("Statistiques");
        titreStat.setLayout(null);
        titreStat.setLocation(140,10);
        titreStat.setSize(300,30);
        titreStat.setFont(new Font("Courier", Font.BOLD, 18));
        stat.add(titreStat);
        
        nbKmTot = new JLabel("Distance totale parcourue : "+(int)(this.joueur.getKmParcourus())+" km");
        nbKmTot.setLayout(null);
        nbKmTot.setLocation(30,50);
        nbKmTot.setSize(400,30);
        nbKmTot.setFont(new Font("Courier", Font.BOLD + Font.ITALIC, 16));
        stat.add(nbKmTot);
        
        nbCasesExp = new JLabel("Exploration de planète : "+joueur.obtenirPExploration()+" %");
        nbCasesExp.setLayout(null);
        nbCasesExp.setLocation(30,80);
        nbCasesExp.setSize(400,30);
        nbCasesExp.setFont(new Font("Courier", Font.BOLD + Font.ITALIC, 16));
        stat.add(nbCasesExp);
        
        // add finaux
        cg.add(panneauBat);
        cg.add(panneauVoyants);
        cg.add(stat);
        cp.add(cg);
        this.add(cp);
        setVisible(true);
    }
    
    //------------------------------------------------------------------ Méthodes
    
    public void actionPerformed(ActionEvent e){
        cpt++;
        switch(cpt%4){
            case 0 : 
                titre.setText("Donnnées en temps réel ");
                break;
            case 1 : 
                titre.setText("Donnnées en temps réel .");
                break;
            case 2 : 
                titre.setText("Donnnées en temps réel ..");
                break;
            case 3 : 
                titre.setText("Donnnées en temps réel ...");
                break;
        }
        nbRecharges.setText("Nombre de recharges : "+this.joueur.getNbRecharges());
        nivBat.setText("Niveau : "+this.joueur.getBatterie()+" %");
        nbKmTot.setText("Distance totale parcourue : "+(int)(this.joueur.getKmParcourus())+" km");
        nbCasesExp.setText("Exploration de planète : "+joueur.obtenirPExploration()+" %");
        this.repaint();
        cp.repaint();
    }
}
