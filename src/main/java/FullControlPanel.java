import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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
    private JPanel cp; // panneau principal global
    
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

    public FullControlPanel(int x, int y, Robot r){
        // Fenêtre
        super();
        setLayout(null);
        setLocation(x,y);
        setSize(lx,ly);
        setTitle("Fenêtre de contrôle");
        joueur = r;
        t = new Timer(500,this);
        t.start();
        
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
        
        nbCasesExp = new JLabel("Exploration de planète : 0 %");
        nbCasesExp.setLayout(null);
        nbCasesExp.setLocation(30,80);
        nbCasesExp.setSize(400,30);
        nbCasesExp.setFont(new Font("Courier", Font.BOLD + Font.ITALIC, 16));
        stat.add(nbCasesExp);
        
        // Images
        
        imageP = Toolkit.getDefaultToolkit().getImage("Mars9.png");
        imageR = Toolkit.getDefaultToolkit().getImage("Perseverance II.png");
               
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
        this.repaint();
    }
    
    public void paint(Graphics g){
        super.paint(g);
        
        // batterie : origine (xb,yb)
        int Xb = xb + 210;
        int Yb = yb + 120;
        g.setColor(Color.black);
        g.fillRect(Xb,Yb,118,48);
        g.setColor(Color.white);
        g.fillRect(Xb+3,Yb+3,112,42);
        g.setColor(Color.black);
        g.fillRect(Xb+6,Yb+6,106,36);
        g.fillRect(Xb+6,Yb+19,112,10);
        // Jauge de batterie de couleur ajustable
        float c1 = (float) (1.0-joueur.getBatterie()*0.01);
        float c2 = (float)(joueur.getBatterie()*0.01);
        if (c1 < 0) c1 = 0;
        else if (c1 > 1) c1 = 1;
        if (c2 < 0) c2 = 0;
        else if (c2 > 1) c2 = 1;
        g.setColor(new Color(c1, c2, (float)(0.0)));
        g.fillRect(Xb+9,Yb+9,joueur.getBatterie(),30);
        
        // Voyants
        int xv = xb + 100;
        int yv = ypv + 120;
        for(int i=0; i<joueur.getJambes().length; i++){
            joueur.getJambes()[i].voyant.setPositionR(xv+i*120,yv,20); 
            joueur.getJambes()[i].voyant.dessinerVoyant(g);
        }
        yv += 65;
        for(int i=0; i<joueur.getBras().length; i++){
            joueur.getBras()[i].voyant.setPositionR(xv+i*120,yv,20); 
            joueur.getBras()[i].voyant.dessinerVoyant(g);
        }
        yv += 65;
        for(int i=0; i<joueur.getCapteurs().length; i++){
            joueur.getCapteurs()[i].voyant.setPositionR(xv+i*120,yv,20); 
            joueur.getCapteurs()[i].voyant.dessinerVoyant(g);
        }
        
        // Images
        g.drawImage(imageP,lx/2 +17,37,this);
        g.drawImage(imageR,lx/2 -115, 100, this);
        
        // Warnings robots;
        int xc;
        int yc;
        //Jambe droite
        if(joueur.getJambes()[0].voyant.getEtat()>Options.ALERTE_MIN){
            xc = lx/2 + 175;
            yc = 600;
            g.setColor(Color.black);
            g.fillPolygon(new int[]{xc,xc+30,xc-30}, new int[]{yc-40,yc+20,yc+20}, 3);
            g.setColor(Color.orange);
            g.fillPolygon(new int[]{xc,xc+23,xc-23}, new int[]{yc-30,yc+15,yc+15}, 3);
            g.setColor(Color.black);
            g.setFont(new Font("Times New Roman",Font.BOLD,40));
            g.drawString("!",xc-5,yc+10);
        }
        //Jambe gauche
        if(joueur.getJambes()[1].voyant.getEtat()>Options.ALERTE_MIN){
            xc = lx/2 + 255;
            yc = 600;
            g.setColor(Color.black);
            g.fillPolygon(new int[]{xc,xc+30,xc-30}, new int[]{yc-40,yc+20,yc+20}, 3);
            g.setColor(Color.orange);
            g.fillPolygon(new int[]{xc,xc+23,xc-23}, new int[]{yc-30,yc+15,yc+15}, 3);
            g.setColor(Color.black);
            g.setFont(new Font("Times New Roman",Font.BOLD,40));
            g.drawString("!",xc-5,yc+10);
        }
        // Bras droit xc = 125, yc = 500
        if(joueur.getBras()[0].voyant.getEtat()>Options.ALERTE_MIN){
            xc = lx/2 + 140;
            yc = 450;
            g.setColor(Color.black);
            g.fillPolygon(new int[]{xc,xc+30,xc-30}, new int[]{yc-40,yc+20,yc+20}, 3);
            g.setColor(Color.orange);
            g.fillPolygon(new int[]{xc,xc+23,xc-23}, new int[]{yc-30,yc+15,yc+15}, 3);
            g.setColor(Color.black);
            g.setFont(new Font("Times New Roman",Font.BOLD,40));
            g.drawString("!",xc-5,yc+10);
        }
        // Bras gauche 
        if(joueur.getBras()[1].voyant.getEtat()>Options.ALERTE_MIN){
            xc = lx/2 + 285;
            yc = 450;
            g.setColor(Color.black);
            g.fillPolygon(new int[]{xc,xc+30,xc-30}, new int[]{yc-40,yc+20,yc+20}, 3);
            g.setColor(Color.orange);
            g.fillPolygon(new int[]{xc,xc+23,xc-23}, new int[]{yc-30,yc+15,yc+15}, 3);
            g.setColor(Color.black);
            g.setFont(new Font("Times New Roman",Font.BOLD,40));
            g.drawString("!",xc-5,yc+10);
        }
        // capteurs températures
        if(joueur.getCapteurs()[0].voyant.getEtat()>Options.ALERTE_MIN){
            xc = lx/2 + 310;
            yc = 350;
            g.setColor(Color.black);
            g.fillPolygon(new int[]{xc,xc+30,xc-30}, new int[]{yc-40,yc+20,yc+20}, 3);
            g.setColor(Color.orange);
            g.fillPolygon(new int[]{xc,xc+23,xc-23}, new int[]{yc-30,yc+15,yc+15}, 3);
            g.setColor(Color.black);
            g.setFont(new Font("Times New Roman",Font.BOLD,40));
            g.drawString("!",xc-5,yc+10);
        }
        if(joueur.getCapteurs()[1].voyant.getEtat()>Options.ALERTE_MIN){
        // Caméras
            xc = lx/2 + 150;
            yc = 350;
            g.setColor(Color.black);
            g.fillPolygon(new int[]{xc,xc+30,xc-30}, new int[]{yc-40,yc+20,yc+20}, 3);
            g.setColor(Color.orange);
            g.fillPolygon(new int[]{xc,xc+23,xc-23}, new int[]{yc-30,yc+15,yc+15}, 3);
            g.setColor(Color.black);
            g.setFont(new Font("Times New Roman",Font.BOLD,40));
            g.drawString("!",xc-5,yc+10);
        }
        // Capteurs pressions
        if(joueur.getCapteurs()[1].voyant.getEtat()>Options.ALERTE_MIN){
            xc = lx/2 + 320;
            yc = 515;
            g.setColor(Color.black);
            g.fillPolygon(new int[]{xc,xc+30,xc-30}, new int[]{yc-40,yc+20,yc+20}, 3);
            g.setColor(Color.orange);
            g.fillPolygon(new int[]{xc,xc+23,xc-23}, new int[]{yc-30,yc+15,yc+15}, 3);
            g.setColor(Color.black);
            g.setFont(new Font("Times New Roman",Font.BOLD,40));
            g.drawString("!",xc-5,yc+10);
        }
    }
    
}
