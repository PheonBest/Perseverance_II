import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PanneauPause extends JDialog {
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
        setLayout(null);
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
		
		this.setModal(true);
	}
/*
	public void showDialog() {

    JDialog dialog = new JDialog(this, Dialog.ModalityType.APPLICATION_MODAL);
    //OR, you can do the following...
    //JDialog dialog = new JDialog();
    //dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
	dialog.setLayout(null);
    dialog.setBounds(350, 350, 600, 600);
    dialog.setVisible(true);
    setTitle("panneau pause");
    
	}*/
}


