import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FullControlPanel extends JFrame{
    
    //------------------------------------------------------------------ Attributs
    
    // taille de la fenêtre
    private int lx = 1200;
    private int ly = 1000;
    private Robot joueur;
    
    // Panneaux principaux de la fenêtre
    private JPanel cg; // panneau principal de gauche
    private JPanel cd; // panneau principal de droite
    private JPanel cp; // panneau principal global
    
    //---- Panneaux de gauche 
    private JLabel titre;
    int ecartY = 20; // écart entre les panneaux de gauche
    
    // Panneau de la batterie
    private JPanel panneauBat;
    int xb = 20;
    int yb = 100;
    private JLabel titreBat;
    private JLabel nivBat;
    private JLabel nbRecharges;
    
    // Panneau des voyants
    private JPanel panneauVoyants;
    int ypv;
    private JLabel titreVoyant;
    
    // Panneau des statistiques
    private JPanel stat;
    int ys;
    private JLabel titreStat;
    private JLabel nbKmTot;
    private JLabel nbCasesExp;
    
    //---- Panneaux de droite
    Image image;
   
    //------------------------------------------------------------------ Constructeur

    public FullControlPanel(int x, int y, Robot r){
        // Fenêtre
        super();
        setLayout(null);
        setLocation(x,y);
        setSize(lx,ly);
        setTitle("Fenêtre de contrôle");
        joueur = r;
        
        // Panneaux principaux
        cp = new JPanel();
        cp.setLayout(null);
        cp.setLocation(0,0);
        cp.setSize(lx,ly);
        cp.setBackground(Color.black);
        
        cg = new JPanel();
        cg.setLayout(null);
        cg.setLocation(5,5);
        cg.setSize(lx/2,ly -50);
        cg.setBackground(new Color(175,175,175));
        
        cd = new JPanel();
        cd.setLayout(null);
        cd.setLocation(lx/2 +10,5);
        cd.setSize(lx/2 -30,ly-50);
        cd.setBackground(Color.white);
        
        titre = new JLabel("Donnnées en temps réel");
        titre.setLayout(null);
        titre.setLocation(140,10);
        titre.setSize(500,50);
        titre.setFont(new Font("Courier", Font.BOLD + Font.ITALIC, 24));
        cg.add(titre);
        
        //-------- Panneau de gauche
        
        // Panneau de la batterie
        panneauBat = new JPanel();
        panneauBat.setLayout(null);
        panneauBat.setLocation(xb,yb);
        panneauBat.setSize(lx/2 -2*xb,200);
        panneauBat.setBackground(new Color(220,220,220));
        
        titreBat = new JLabel("Batterie");
        titreBat.setLayout(null);
        titreBat.setLocation(220,10);
        titreBat.setSize(200,30);
        titreBat.setFont(Options.police);
        panneauBat.add(titreBat);
        
        nivBat = new JLabel("Niveau : "+this.joueur.getBatterie()+" %");
        nivBat.setLayout(null);
        nivBat.setLocation(30,80);
        nivBat.setSize(300,30);
        nivBat.setFont(new Font("Courier", Font.BOLD + Font.ITALIC, 16));
        panneauBat.add(nivBat);
        
        nbRecharges = new JLabel("Nombres de recharges : "+this.joueur.getNbRecharges());
        nbRecharges.setLayout(null);
        nbRecharges.setLocation(30,130);
        nbRecharges.setSize(400,30);
        nbRecharges.setFont(new Font("Courier", Font.BOLD + Font.ITALIC, 16));
        panneauBat.add(nbRecharges);
        
        // Panneau des voyants
        ypv = yb + panneauBat.getHeight() + ecartY;
        
        panneauVoyants = new JPanel();
        panneauVoyants.setLayout(null);
        panneauVoyants.setLocation(xb,ypv);
        panneauVoyants.setSize(lx/2 -2*xb,400);
        panneauVoyants.setBackground(new Color(220,220,220));
        
        titreVoyant = new JLabel("Etats des composants");
        titreVoyant.setLayout(null);
        titreVoyant.setLocation(150,10);
        titreVoyant.setSize(300,30);
        titreVoyant.setFont(Options.police);
        panneauVoyants.add(titreVoyant);
        
        // Panneaux des statistiques
        ys = ypv + panneauVoyants.getHeight() + ecartY;
        
        stat = new JPanel();
        stat.setLayout(null);
        stat.setLocation(xb,ys);
        stat.setSize(lx/2 -2*xb,200);
        stat.setBackground(new Color(220,220,220));
        
        titreStat = new JLabel("Statistiques");
        titreStat.setLayout(null);
        titreStat.setLocation(200,10);
        titreStat.setSize(300,30);
        titreStat.setFont(Options.police);
        stat.add(titreStat);
        
        nbKmTot = new JLabel("Distance totale parcourus : "+this.joueur.getKmParcourus()+" km");
        nbKmTot.setLayout(null);
        nbKmTot.setLocation(30,80);
        nbKmTot.setSize(400,30);
        nbKmTot.setFont(new Font("Courier", Font.BOLD + Font.ITALIC, 16));
        stat.add(nbKmTot);
        
        nbCasesExp = new JLabel("Exploration de planète : 0 %");
        nbCasesExp.setLayout(null);
        nbCasesExp.setLocation(30,130);
        nbCasesExp.setSize(400,30);
        nbCasesExp.setFont(new Font("Courier", Font.BOLD + Font.ITALIC, 16));
        stat.add(nbCasesExp);
        
        //-------- Panneau de droite
        
        image = Toolkit.getDefaultToolkit().getImage("Perseverance II.png");
               
        // add finaux
        cg.add(panneauBat);
        cg.add(panneauVoyants);
        cg.add(stat);
        cp.add(cg);
        cp.add(cd);
        this.add(cp);
        setVisible(true);
    }
    
    //------------------------------------------------------------------ Méthodes
    
    public void paint(Graphics g){
        super.paint(g);
        
        // batterie : origine (xb,yb)
        int Xb = xb + 300;
        int Yb = yb + 100;
        g.setColor(Color.black);
        g.fillRect(Xb+3,Yb+3,230,90);
        g.setColor(Color.white);
        g.fillRect(Xb+6,Yb+6,224,84);
        g.setColor(Color.black);
        g.fillRect(Xb+12,Yb+12,212,72);
        g.fillRect(Xb+12,Yb+38,218,20);
        // Jauge de batterie de couleur ajustable
        g.setColor(new Color((float)(1.0-joueur.getBatterie()*0.01), (float)(0.0+joueur.getBatterie()*0.01), (float)(0.0)));
        g.fillRect(Xb+18,Yb+18,joueur.getBatterie()*2,60);
        
        // Voyants
        int xv = xb + 90;
        int yv = ypv + 170;
        for(int i=0; i<joueur.getJambes().length; i++){
            joueur.getJambes()[i].voyant.setPositionR(xv+i*200,yv,35); 
            joueur.getJambes()[i].voyant.dessinerVoyant(g);
        }
        yv += 100;
        for(int i=0; i<joueur.getBras().length; i++){
            joueur.getBras()[i].voyant.setPositionR(xv+i*200,yv,35); 
            joueur.getBras()[i].voyant.dessinerVoyant(g);
        }
        yv += 100;
        for(int i=0; i<joueur.getCapteurs().length; i++){
            joueur.getCapteurs()[i].voyant.setPositionR(xv+i*200,yv,35); 
            joueur.getCapteurs()[i].voyant.dessinerVoyant(g);
        }
        
        // Warnings robots;
        g.drawImage(image,lx/2, 200, this);
    }
    
}
