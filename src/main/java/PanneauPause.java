import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PanneauPause extends JFrame {
	private int lx=600;
	private int ly=600;
	private int x0=250;
	private int y0=50;
	private JPanel rouge;
	private JLabel monImage;
	
	public PanneauPause(){
		super();
        setLayout(null);
        setSize(lx,ly);
        setLocation(x0,y0);
        setVisible(true);
        this.setLayout(null);
        setTitle("panneau pause");
        
        rouge= new JPanel();
        rouge.setLayout(null);
		rouge.setBounds(0,0,lx,ly);
		rouge.setBackground(Color.red);
		rouge.setVisible(true);
		this.add(rouge);
        
        monImage = new JLabel(new ImageIcon("./res/images/pause.png"));
        monImage.setVisible(true);
		monImage.setBounds(lx/2-500/2,150,500,250);
		
	
		rouge.add(monImage);
	}
}


