import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BoutonMissions extends JPanel {
	private int lx = 100;
	private int ly = 100;
	private JButton mission;
	
	public BoutonMissions(int x0, int y0){
        super();
        setLayout(null);
        setSize(lx,ly);
        setLocation(x0,y0);
        
        mission = new JButton("MISSION");
        mission.setLocation(0,0);
        mission.setSize(100,100);
        mission.setBackground(Color.white);
        //mission.addActionListener(this);
        this.add(mission);
    }
	
}

