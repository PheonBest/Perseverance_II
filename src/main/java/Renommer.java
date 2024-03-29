
import java.util.HashMap;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.io.InputStream;
import java.io.IOException;
import java.util.List;

public class Renommer extends JFrame implements ActionListener{
	int x=100;
	int y=100;
	int lx=300;
	int ly=220;
	int largeur;
	int hauteur;
	private JButton valider;
	private JButton effacer;
	private JTextField nom;
	private Controleur controleur;
	private String nomCarte ;
	private List<String[]> carte; 
	private boolean cloner;
	
	public Renommer(Controleur controleur, int largeur, int hauteur){
		this(controleur, largeur, hauteur, false);
	}
	public Renommer(Controleur controleur, int largeur, int hauteur, boolean cloner){
		super();

		this.controleur = controleur;
		this.cloner = cloner;
        setLayout(null);
        setLocation(largeur, hauteur);
        setSize(lx+20,ly);
        setTitle("RENOMMER");
		
		// Quand on ferme la fenêtre, on la cache pour ne pas avoir à l'initialiser à nouveau
		setDefaultCloseOperation(HIDE_ON_CLOSE);
        
        JLabel texteChoix= new JLabel("Veuillez saisir un nom");
        texteChoix.setBounds(0,20,lx,15);
        texteChoix.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(texteChoix);
        
        JPanel fond= new JPanel();
        fond.setBackground(new Color(220,220,220));
        fond.setBounds(0,0,lx,ly);
        fond.setLayout(null);
        this.add(fond);
        
        nom= new JTextField();
        nom.setBounds(10,50, 280, 50);
        fond.add(nom);
		
		effacer= new JButton("EFFACER");
		effacer.setBounds(10,110,135,50);
		effacer.setBackground(new Color(175,175,175));
		fond.add(effacer);
		effacer.addActionListener(this);
		
		valider= new JButton("VALIDER");
		valider.setBounds(155,110,135,50);
		valider.setBackground(new Color(175,175,175));
		valider.addActionListener(this);
		fond.add(valider);
	}
	
	 public void actionPerformed(ActionEvent e) {
			if (e.getSource()== effacer){
				nom.setText("");
			} else if (e.getSource() == valider) {
				try {
					if (cloner)
						controleur.cloner(nom.getText(), carte);
					else
						controleur.renommer(nomCarte, nom.getText(), carte);
					setVisible(false);
				} catch(IOException ex){
					ex.printStackTrace();
				}
			}
	}
	
	public void initialiser(String nomCarte, List<String[]> carte){
		this.nomCarte = nomCarte;
		this.carte = carte;
	}
	/*
	public void majTaille(int largeur, int hauteur){
		this.setSize(largeur,hauteur);
		this.largeur=largeur;
		this.hauteur=hauteur;
		
	}
	*/
}

