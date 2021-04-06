import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BoutonPause extends JPanel {
	private int lx = 100;
	private int ly = 50;
	private JButton pause;
	
	public BoutonPause(int x0, int y0){
        super();
        setLayout(null);
        setSize(lx,ly);
        setLocation(x0,y0);
        
        pause = new JButton("PAUSE");
        pause.setLocation(0,0);
        pause.setSize(100,50);
        pause.setBackground(Color.red);
        this.add(pause);
    }
	
}

