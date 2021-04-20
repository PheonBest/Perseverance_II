
import java.util.HashMap;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.io.InputStream;
import java.io.IOException;

public class Renommer extends JFrame implements ActionListener{
	int x=100;
	int y=100;
	int lx=300;
	int ly=220;
	//int largeur;
	//int hauteur;
	private JButton valider;
	private JButton effacer;
	private JTextField nom;
	private Controleur controleur;
	private String nomCarte ;
	private InputStream carte; 
	
	public Renommer(){
		
		super();
        setLayout(null);
        setLocation(400,400);
        setSize(lx+20,ly);
        setTitle("RENOMMER");
        
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
			if(e.getSource()== effacer){
				nom.setText("");
				
			}else if(e.getSource()==valider){
				try{
					System.out.println(nomCarte+" "+nom.getText());
					controleur.renommer(nomCarte, nom.getText(), carte);
				}catch(IOException ex){
					ex.printStackTrace();
				}
				

			}
	}
	
	public void initialiser(String nomCarte, InputStream carte){
		System.out.println(nomCarte);
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

