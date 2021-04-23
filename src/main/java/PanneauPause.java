import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.LinkedList;

import javax.swing.*;


import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class PanneauPause extends JDialog {

	public static final int RETOUR = 0;
	public static final int VOLUME_MUSIQUE = 1;
	public static final int VOLUME_EFFETS = 2;
	public static final int MENU = 3;
	public static final int PAUSE = 4;
	public static final int SUIVANT = 5;
	public static final int PRECEDENT = 6;

	private int lx=600;
	private int ly=600;
	private int x0=250;
	private int y0=50;
	private JPanel rouge;
	private JLabel monImage;
	private JButton retour;
	private JButton menu;
	private JButton pause;
	private JButton suivant;
	private JButton precedent;
	private JSlider volume;
	private JSlider volumeEffet;
	private JPanel panneauBas;
	private JLabel son;
	private JLabel titreSon;
	private JLabel titreEffet;
	private int largeur;
	private int hauteur;
	private final Color COULEUR_BOUTONS = new Color(255,174,33);
	
	public PanneauPause(int largeur, int hauteur) {
		super();
		//this.setSize(largeur,hauteur);
		this.largeur=largeur;
		this.hauteur=hauteur;
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setLayout(null);
        setSize(lx,ly+40);
        setLocation((largeur-lx)/2,(hauteur-ly-40)/2);
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
        monImage = new JLabel(new ImageIcon(Donnees.imagesMenu.get("pause")));
        monImage.setVisible(true);
		monImage.setBounds(lx/2-500/2,150,500,250);
		rouge.add(monImage);
		//retour
		retour = new JButton("RETOUR");
        retour.setLocation(lx-140,ly-60);
        retour.setSize(120,50);
        retour.setBackground(COULEUR_BOUTONS);
        retour.setFont(new Font("Courier",Font.ITALIC+Font.BOLD,16));
        rouge.add(retour);
        //menu principal
        menu= new JButton("MENU ");
        menu.setLocation(lx-140,ly-160);
        menu.setSize(120,90);
        menu.setBackground(COULEUR_BOUTONS);
        menu.setFont(new Font("Courier",Font.ITALIC+Font.BOLD,16));
        menu.setVisible(true);
        rouge.add(menu);
      
        

		panneauBas= new JPanel();
        panneauBas.setLayout(null);
		panneauBas.setBounds(10,ly-160,150,150);
		panneauBas.setBackground(COULEUR_BOUTONS);
		panneauBas.setVisible(true);
		rouge.add(panneauBas);
		
		
        
        son = new JLabel(" --- VOLUMES SON ---");
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
		volume.setValue(Options.MUSIQUE_VOLUME_INIT);
        volume.setBounds(25,55,100,20);
        volume.setBackground(COULEUR_BOUTONS);
        panneauBas.add(volume);
        
		titreEffet= new JLabel("volume effets");
        titreEffet.setFont(new Font("Courier", Font.BOLD, 12));
		titreEffet.setBounds(26,80,150,15);
		titreEffet.setBackground(Color.BLACK);
		panneauBas.add(titreEffet);
		
		volumeEffet= new JSlider();
		volumeEffet.setValue(Options.EFFETS_VOLUME_INIT);
        volumeEffet.setBounds(25,100,100,20);
        volumeEffet.setBackground(COULEUR_BOUTONS);
        panneauBas.add(volumeEffet);
        
        JPanel panneauDroite= new JPanel();
        panneauDroite.setLayout(null);
		panneauDroite.setBounds(10+150+10,ly-160,280,150);
		panneauDroite.setBackground(COULEUR_BOUTONS);
		panneauDroite.setVisible(true);
		rouge.add(panneauDroite);
		
		JLabel son2 = new JLabel(" --- REGLAGES SON ---");
        son2.setFont(new Font("Serif", Font.BOLD, 13));
        son2.setBounds(75,5,150,15);
        son2.setBackground(Color.BLACK);
        panneauDroite.add(son2);
        
        
                
  
        pause= new JButton();
      
		try {
			Image img1 = TailleImage.resizeImage(ImageIO.read(getClass().getResource("./res/symboles/PAUSE.png")),50,50,true);
			pause.setIcon(new ImageIcon(img1
			));
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		pause.setBounds(110,45,60,60);
		panneauDroite.add(pause);
		
		
        suivant= new JButton();
        
        try {
			Image img = TailleImage.resizeImage(ImageIO.read(getClass().getResource("./res/symboles/SUIVANT.png")),50,50,true);
			suivant.setIcon(new ImageIcon(img));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		suivant.setBounds(180,45,60,60);
		panneauDroite.add(suivant);
		
        precedent=new JButton();
         try {
			Image img = TailleImage.resizeImage(ImageIO.read(getClass().getResource("./res/symboles/PRECEDENT.png")),50,50,true);
			precedent.setIcon(new ImageIcon(img));
			TailleImage.resizeImage(img,50,50,true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		precedent.setBounds(40,45,60,60);
		panneauDroite.add(precedent);
		
		 
	}
	
	private void setFocusTo(JComponent comp) {
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent ce) {
                comp.requestFocusInWindow();
            }
        });
    }

	public LinkedList<JComponent> obtenirComposants() {
		// Retourne la liste des composants
		// Auquel on doit ajouter un keylistener
		LinkedList<JComponent> composants = new LinkedList<JComponent>();
		composants.add(retour);
		composants.add(volume);
		composants.add(volumeEffet);
		composants.add(menu);
		composants.add(pause);
		composants.add(suivant);
		composants.add(precedent);
		return composants;
	}
	
}


