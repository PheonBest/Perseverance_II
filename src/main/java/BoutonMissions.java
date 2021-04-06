import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BoutonMissions extends JPanel implements ActionListener {
	private int lx = 100;
	private int ly = 50;
	private int largeur;
	private int hauteur;
	private JButton mission;
	private Missions panneauMission;
	
	public BoutonMissions(int x0, int y0){
        super();
        setLayout(null);
        setSize(lx,ly);
        setLocation(x0,y0);
        
        mission = new JButton("MISSION");
        mission.setLocation(0,0);
        mission.setSize(100,50);
        mission.setBackground(Color.white);
        mission.addActionListener(this);
        this.add(mission);
    }
    
    public void actionPerformed(ActionEvent e){
        panneauMission = new Missions(250,0);
    }
    
	public void majTaille(int largeur, int hauteur){
		this.setSize(largeur,hauteur);
		this.largeur=largeur;
		this.hauteur=hauteur;
		
	}
}

