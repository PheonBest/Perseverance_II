import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FullControlPanel extends JFrame{

    private int lx = 1200;
    private int ly = 1000;
    private Robot joueur;
    
    
    private JPanel stat;
    int xs = 20;
    int ys = 200;
    private JLabel titreStat;
    private JLabel nbTotKm;
    
    private JPanel panneauVoyants;
    int ecartY = 20;;
    int xpv;
    int ypv;
   
    private JPanel cp;
    JLabel titre;
    private JPanel ci;

    public FullControlPanel(int x, int y){
        super();
        setLayout(null);
        setLocation(x,y);
        setSize(lx,ly);
        setTitle("Fenêtre de contrôle");
        
        stat = new JPanel();
        stat.setLayout(null);
        stat.setLocation(xs,ys);
        stat.setSize(lx/2 -2*xs,200);
        stat.setBackground(new Color(100,100,100));
       
        xpv = xs;
        ypv = ys + stat.getHeight() + ecartY;
        panneauVoyants = new JPanel();
        panneauVoyants.setLayout(null);
        panneauVoyants.setLocation(xpv,ypv);
        panneauVoyants.setSize(lx/2 -2*xpv,400);
        panneauVoyants.setBackground(new Color(100,100,100));
        
        cp = new JPanel();
        cp.setLayout(null);
        cp.setLocation(0,0);
        cp.setSize(lx/2,ly);
        cp.setBackground(new Color(175,175,175));
        cp.add(stat);
        cp.add(panneauVoyants);
        add(cp);
        
        ci = new JPanel();
        ci.setLayout(null);
        ci.setLocation(lx/2,0);
        ci.setSize(lx/2,ly);
        ci.setBackground(Color.white);
        add(ci);
        
        setVisible(true);
    }
    
    public void majJoueur(Robot joueur) {
        this.joueur = joueur;
    }
}
