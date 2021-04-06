import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PanneauPause extends JDialog implements ActionListener {
	private int lx=600;
	private int ly=600;
	private int x0=250;
	private int y0=50;
	private JPanel rouge;
	private JLabel monImage;
	private JButton retour;
	private JSlider volume;
	private JSlider volume2;
	private JPanel panneauBas;
	private JLabel son;
	private JLabel titreSon;
	private JLabel titreEffet;
	private int largeur;
	private int hauteur;
	
	public PanneauPause(){
		super();
        setLayout(null);
        setSize(largeur,hauteur);
        setLocation(x0,y0);
        setLayout(null);
        
        setTitle("panneau pause");
        // fenetre modale 
        this.setModal(true);
        //fond rouge
        rouge= new JPanel();
        rouge.setLayout(null);
		rouge.setBounds(0,0,lx,ly);
		rouge.setBackground(Color.red);
		rouge.setVisible(true);
		this.add(rouge);
        //image pause
        monImage = new JLabel(new ImageIcon("./res/images/pause.png"));
        monImage.setVisible(true);
		monImage.setBounds(lx/2-500/2,150,500,250);
		rouge.add(monImage);
		//retour
		retour = new JButton("RETOUR");
        retour.setLocation(lx-120,ly-60);
        retour.setSize(100,50);
        retour.setBackground(new Color(255,140,0));
        retour.addActionListener(this);
        this.add(retour);
        

		panneauBas= new JPanel();
        panneauBas.setLayout(null);
		panneauBas.setBounds(10,ly-160,150,150);
		panneauBas.setBackground(new Color(255,140,0));
		panneauBas.setVisible(true);
		rouge.add(panneauBas);
		
		
        
        son = new JLabel(" --- REGLAGES SON ---");
        son.setFont(new Font("Serif", Font.BOLD, 13));
        son.setBounds(10,5,150,15);
        son.setBackground(Color.BLACK);
        panneauBas.add(son);
        
        titreSon= new JLabel("volume musique");
        titreSon.setFont(new Font("Courier", Font.BOLD, 12));
		titreSon.setBounds(26,40,150,15);
		titreSon.setBackground(Color.BLACK);
		panneauBas.add(titreSon);
		
		volume= new JSlider();
        volume.setBounds(25,55,100,20);
        volume.setBackground(new Color(255,140,0));
        panneauBas.add(volume);
        
		titreEffet= new JLabel("volume effets");
        titreEffet.setFont(new Font("Courier", Font.BOLD, 12));
		titreEffet.setBounds(26,80,150,15);
		titreEffet.setBackground(Color.BLACK);
		panneauBas.add(titreEffet);
		
		volume2= new JSlider();
        volume2.setBounds(25,100,100,20);
        volume2.setBackground(new Color(255,140,0));
        panneauBas.add(volume2);
	}
		public void actionPerformed(ActionEvent e){
			this.setVisible(false);
        
    }
    
    public void majTaille(int largeur, int hauteur){
			this.setSize(largeur,hauteur);
			this.largeur=largeur;
			this.hauteur=hauteur;
		
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


