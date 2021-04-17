import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class AntiBugPanel extends JPanel{
    
    //------------------------------------------------------------------ Attributs
    private Robot joueur;
    private int lx;
    private int ly;
    private int xb;
    private int yb;
    private int ypv;
    private Image imageP;
    private Image imageR;
    
    public AntiBugPanel(int xbat, int ybat, int yPanVoy, int longx, int largy, Robot unRobot, Image uneImageP, Image uneImageR){
        super();
        this.lx = longx;
        this.ly = largy;
        this.xb = xbat -20;
        this.yb = ybat -30;
        this.ypv = yPanVoy -30;
        this.imageP = uneImageP;
        this.imageR = uneImageR;
        this.joueur = unRobot;
         
        
        setLayout(null);
        setLocation(0,0);
        setSize(lx,ly);
        setBackground(Color.black);
        
        
    }
    
    public void paint(Graphics g){
        super.paint(g);
        
        // batterie : origine (xb,yb)
        int Xb = xb + 240;
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
        g.drawImage(imageP,lx/2 +10,7,this);
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


