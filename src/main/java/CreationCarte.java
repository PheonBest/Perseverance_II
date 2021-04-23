import java.util.HashMap;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.io.InputStream;
import java.io.IOException;
import java.util.List;

public class CreationCarte extends JFrame {
	int lx=300;
	int ly=250;
	int largeur;
	int hauteur;
	private Controleur controleur;
	private JTextField nombre;
	private JTextField nombre2;
	
	public CreationCarte(Controleur controleur,int largeur ,int hauteur) {
		super();
		this.controleur = controleur;
		setLayout(null);
        setLocation(largeur, hauteur);
        setSize(lx+20,ly);
        setTitle("CREATION CARTE");
        
        // Quand on ferme la fenêtre, on la cache pour ne pas avoir à l'initialiser à nouveau
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		
		JPanel fond= new JPanel();
        fond.setBackground(new Color(220,220,220));
        fond.setBounds(0,0,lx,ly);
        fond.setLayout(null);
        this.add(fond);
        
        JLabel texteChoix1= new JLabel("Veuillez saisir un nombre de lignes: ");
        texteChoix1.setBounds(0,10,lx,15);
        texteChoix1.setHorizontalAlignment(SwingConstants.CENTER);
        fond.add(texteChoix1);
        
        nombre= new JTextField();
        nombre.setBounds(50,30, 200, 50);
        fond.add(nombre);
        
        JLabel texteChoix2= new JLabel("Veuillez saisir un nombre de colonnes: ");
        texteChoix2.setBounds(0,100,lx,15);
        texteChoix2.setHorizontalAlignment(SwingConstants.CENTER);
        fond.add(texteChoix2);
		
		nombre2= new JTextField();
        nombre2.setBounds(50,120, 200, 50);
        fond.add(nombre2);
		
	}
}

