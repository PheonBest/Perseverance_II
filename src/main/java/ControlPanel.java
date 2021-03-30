import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ControlPanel extends JPanel implements ActionListener{
    
    private int lx = 600;
    private int ly = 100;
    private Robot joueur;
    private JButton details;
    private FullControlPanel panneauComplet;
    
    public ControlPanel(int x0, int y0){
        super();
        setLayout(null);
        setSize(lx,ly);
        setLocation(x0,y0);
        
        details = new JButton("Détails");
        details.setLocation(lx-120,ly-60);
        details.setSize(100,40);
        details.setBackground(Color.white);
        details.addActionListener(this);
        add(details);
    }
    
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if (joueur != null) {
            // Panneaux de contrôle
            g.setColor(new Color(175,175,175));
            g.fill3DRect(0,0,lx,ly,true);
            g.setColor(Color.black);
            g.drawString("# --------------------------------------------------| PANNEAU DE CONTRÔLE |-------------------------------------------------- #",10,15);
            
            //Batterie : Origine(Xb,Yb)
            int Xb = 10;
            int Yb = 40;
            g.setColor(Color.black);
            g.fillRect(Xb,Yb,118,48);
            g.setColor(Color.white);
            g.fillRect(Xb+3,Yb+3,112,42);
            g.setColor(Color.black);
            g.fillRect(Xb+6,Yb+6,106,36);
            g.fillRect(Xb+6,Yb+19,112,10);
            // Jauge de batterie de couleur ajustable
            g.setColor(new Color((float)(1.0-joueur.getBatterie()*0.01), (float)(0.0+joueur.getBatterie()*0.01), (float)(0.0)));
            g.fillRect(Xb+9,Yb+9,joueur.getBatterie(),30);
            
            // Voyants : Origine(Xv,Yv) ( centre du 1er voyant ) 
            int Xv = Xb + 200;
            int Yv = Yb + 24;
            for(int i=0; i<joueur.getVP().length; i++){
                joueur.getVP()[i].setPositionR(Xv+i*100,Yv,25); 
                joueur.getVP()[i].dessinerVoyant(g);
            }
        }
    }

    public void majJoueur(Robot joueur) {
        this.joueur = joueur;
    }
    
    public void actionPerformed(ActionEvent e){
        panneauComplet = new FullControlPanel(getWidth()/2,getHeight()/2);
        panneauComplet.majJoueur(this.joueur);
    }
}



