import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

public class ModeDeJeu extends JPanel implements ActionListener {
	
    private Controleur controleur;
    private HashMap<String, List<String[]>> cartes;
    private JList<String> liste = new JList<>();
    DefaultListModel<String> modele = new DefaultListModel<>();
	private Image imageMenu;
   
	
    private String carteActive = null;
    

    private JPanel panneauAffichage;
    private JPanel panneauBas;
    private JButton jouer;
    private JButton creer;
    private JButton editer;
    private JButton cloner;
    private int largeur;
    private int hauteur;
	private JLabel selection;
	private JScrollPane j;
	private JButton aide;
	private RegleJeu regleJeu;
	private Renommer fenetreRenommer;
	private Renommer fenetreCloner;
	private CreationCarte fenetreCreer;
	private JLabel presentation;
	private JButton renommer;
	private JButton supprimer;
	private JButton quitter;
	
	
    public ModeDeJeu(Controleur controleur, int largeur, int hauteur) {
        super();
		this.setSize(largeur,hauteur);
		this.largeur = largeur;
		this.hauteur = hauteur;
        this.controleur = controleur;
        this.setLayout(null);
        // creation des nouvelles fenetres qui vont apparaitre quand on clique sur les boutons
        this.regleJeu = new RegleJeu();
        this.fenetreRenommer = new Renommer(controleur, 2*largeur/5-270,3*hauteur/5);
        this.fenetreCloner = new Renommer(controleur, 3*largeur/5-20, 3*hauteur/5, true);
        this.fenetreCreer= new CreationCarte(controleur, 3*largeur/5-20, 3*hauteur/5);

		// fond d'ecran image
		presentation = new JLabel();
		presentation.setLayout(null);
		presentation.setBounds(0,0,largeur,hauteur);
		presentation.setVisible(true);
		this.add(presentation);
		
		JLabel titre = new JLabel();
		titre.setText("PERSEVERANCE II");
		titre.setFont(new Font("Serif", Font.BOLD, 60));
        titre.setForeground(Color.WHITE);
		titre.setBounds(0, hauteur/20, largeur, 70);
		titre.setHorizontalAlignment(SwingConstants.CENTER);
		presentation.add(titre);		
		
		JLabel titre2 = new JLabel();
		titre2.setText(" Pour plus d'informations concernant les règles du jeu, n'hésitez pas à cliquer sur le bouton AIDE");
		titre2.setFont(new Font("Serif", Font.BOLD+Font.ITALIC, 19));
        titre2.setForeground(Color.WHITE);
		titre2.setBounds(0, hauteur/5, largeur, 70);
		titre2.setHorizontalAlignment(SwingConstants.CENTER);
		presentation.add(titre2);
		
		jouer = new JButton("JOUER");
		jouer.setBounds(2*largeur/5-250,3*hauteur/5,250,70);
		jouer.setBackground(Color.gray);
		jouer.setForeground(Color.white);
		presentation.add(jouer);
		jouer.addActionListener(this);
		
		renommer = new JButton("Renommer");
		renommer.setBounds(2*largeur/5-250,3*hauteur/5+80,120,70);
		renommer.setBackground(Color.WHITE);
		renommer.setForeground(Color.gray);
		presentation.add(renommer);
		renommer.addActionListener(this);
		
		supprimer = new JButton("Supprimer");
		supprimer.setBounds(2*largeur/5-250+130,3*hauteur/5+80,120,70);
		supprimer.setBackground(Color.WHITE);
		supprimer.setForeground(Color.gray);
		presentation.add(supprimer);
		supprimer.addActionListener(this);
		
		
		creer = new JButton("CRÉER UNE CARTE");
		creer.setBounds(3*largeur/5, 3*hauteur/5,250,70);
		creer.setBackground(Color.GRAY);
		creer.setForeground(Color.WHITE);
		presentation.add(creer);
		creer.addActionListener(this);
		
		editer = new JButton("Éditer");
		editer.setBounds(3*largeur/5, 3*hauteur/5+80,120,70);
		editer.setBackground(Color.WHITE);
		editer.setForeground(Color.gray);
		presentation.add(editer);
		editer.addActionListener(this);
		
		cloner = new JButton("Cloner");
		cloner.setBounds(3*largeur/5+130, 3*hauteur/5+80,120,70);
		cloner.setBackground(Color.WHITE);
		cloner.setForeground(Color.gray);
		presentation.add(cloner);
		cloner.addActionListener(this);
		
		aide= new JButton("AIDE");
		aide.setBounds(largeur-130,10,120,70);
		aide.setBackground(Color.gray);
		aide.setForeground(Color.white);
		presentation.add(aide);
		aide.addActionListener(this);
		
		quitter= new JButton("QUITTER");
		//quitter.setBounds(largeur-130,90,120,70);
		quitter.setBounds(10,10,120,70);
		quitter.setBackground(Color.gray);
		quitter.setForeground(Color.white);
		presentation.add(quitter);
		quitter.addActionListener(this);
		
		JLabel selectionCarte= new JLabel("SÉLECTION DE LA CARTE");
		selectionCarte.setBounds(0, hauteur/3, largeur, 60);
		selectionCarte.setForeground(new Color(220,220,220));
		selectionCarte.setFont(new Font("Courier",Font.BOLD+Font.ITALIC,12));
		selectionCarte.setHorizontalAlignment(SwingConstants.CENTER);
		presentation.add(selectionCarte);
		
		
		j=new JScrollPane(liste);
        final int LARGEUR_LISTE = 150;
        final int HAUTEUR_LISTE = 75;
        //j.setBounds(150,200,100,100);
	    j.setBounds((largeur-LARGEUR_LISTE)/2,(hauteur-HAUTEUR_LISTE)/2,LARGEUR_LISTE,HAUTEUR_LISTE);
        j.setVisible(true);
        presentation.add(j);
		
				
        liste.setModel(modele);
        liste.setVisibleRowCount(5);
    }

    public void majCartes(HashMap<String, List<String[]>> cartesEtNoms) {
		//System.out.println(cartesEtNoms.values().size());
        // On veut trier les cartes par ordre alphabétique
        List<String> nomCartes = cartesEtNoms.entrySet()
                    .stream()
                    .sorted((p1,p2) -> p1.getKey().compareTo(p2.getKey()))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        cartes = cartesEtNoms;
        //JLabel
        modele.clear();
        for (String s: nomCartes)
            modele.addElement(s);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
	
	 public void actionPerformed(ActionEvent e) {
		if(e.getSource()==aide){
			regleJeu.setVisible(true);
		}
		if(e.getSource()==quitter){
			System.exit(0);
		}
		if(e.getSource()==creer){
			controleur.jouerEffet("maximize");
			fenetreCreer.setVisible(true);
		}
		if(liste.getSelectedValue()!=null){
			if (e.getSource() == jouer)
				controleur.jouer(liste.getSelectedValue(), cartes.get(liste.getSelectedValue()));
			else if (e.getSource() == editer)
				controleur.editer(liste.getSelectedValue(), cartes.get(liste.getSelectedValue()));
			else if (e.getSource() == renommer) {
				controleur.jouerEffet("maximize");
				fenetreRenommer.initialiser(liste.getSelectedValue(), cartes.get(liste.getSelectedValue()));
				fenetreRenommer.setVisible(true);
			} else if (e.getSource() == supprimer) {
				try {
					controleur.supprimer(liste.getSelectedValue(), cartes.get(liste.getSelectedValue()));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} else if (e.getSource() == cloner) {
				controleur.jouerEffet("maximize");
				fenetreCloner.initialiser(liste.getSelectedValue(), cartes.get(liste.getSelectedValue()));
				fenetreCloner.setVisible(true);
			}
		}
	}

	public void majImageMenu(Image imageMenu) {
		this.imageMenu = imageMenu;
		presentation.setIcon(new ImageIcon (imageMenu));
	}
}
