import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.regex.Pattern;

public class ControlPanel extends JPanel implements ActionListener{
    
    private int lx = 600;
    private int ly = 100;
    private Robot joueur;
    private JButton details;
    private FullControlPanel panneauComplet;
    private Image imageP;
    private Image imageR;
    
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

        // Images
        Pattern pattern = Pattern.compile("^.*\\b"+Options.NOM_DOSSIER_DETAILS+"\\b.*\\.(?:jpg|gif|png)");
        try {
            HashMap<String, Image> images = ObtenirRessources.getImagesAndFilenames(pattern, "res/"+Options.NOM_DOSSIER_DETAILS+"/");
            imageP = images.get("Mars9");
            imageR = images.get("PerseveranceII");
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
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
            float c1 = (float) (1.0-joueur.getBatterie()*0.01);
            float c2 = (float)(joueur.getBatterie()*0.01);
            if (c1 < 0) c1 = 0;
            else if (c1 > 1) c1 = 1;
            if (c2 < 0) c2 = 0;
            else if (c2 > 1) c2 = 1;
            
            g.setColor(new Color(c1, c2, (float)(0.0)));
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
        panneauComplet = new FullControlPanel(getWidth()/2,getHeight()/2, joueur, imageP, imageR);
    }
}



