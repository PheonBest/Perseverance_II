import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BoutonPause extends JPanel implements ActionListener {
	private int lx = 100;
	private int ly = 50;
	private JButton pause;
	private PanneauPause panneauPause;
	private int largeur;
	private int hauteur;
	
	public BoutonPause(int x0, int y0){
        super();
        setLayout(null);
        setSize(lx,ly);
        setLocation(x0,y0);
        
        pause = new JButton("PAUSE");
        pause.setLocation(0,0);
        pause.setSize(100,50);
        pause.setBackground(Color.red);
        pause.addActionListener(this);
        this.add(pause);
        
        panneauPause = new PanneauPause();
    }
    
    public void actionPerformed(ActionEvent e){
       panneauPause.setVisible(true);
        //panneauPause.showDialog();
    }
    
     public void majTaille(int largeur, int hauteur){
			this.setSize(largeur,hauteur);
			this.largeur=largeur;
			this.hauteur=hauteur;
		
	}
	
}

