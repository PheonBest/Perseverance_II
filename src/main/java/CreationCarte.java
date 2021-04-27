import java.util.HashMap;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.io.InputStream;
import java.io.IOException;
import java.util.List;

public class CreationCarte extends JFrame implements ActionListener{
	int lx=300;
	int ly=395;
	int largeur;
	int hauteur;
	private Controleur controleur;
    private JTextField inputNom;
	private JTextField inputLignes;
	private JTextField inputColonnes;
	private JButton valider;
	private JButton effacer;
	
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

        JLabel nomCarte= new JLabel("Veuillez saisir un nom");
        nomCarte.setBounds(0,20,lx,15);
        nomCarte.setHorizontalAlignment(SwingConstants.CENTER);
        fond.add(nomCarte);

        inputNom= new JTextField();
        inputNom.setBounds(50,50, 200, 50);
        fond.add(inputNom);
        
        JLabel nbLignes= new JLabel("Veuillez saisir un nombre de lignes: ");
        nbLignes.setBounds(0,110,lx,15);
        nbLignes.setHorizontalAlignment(SwingConstants.CENTER);
        fond.add(nbLignes);
        
        inputLignes= new JTextField();
        inputLignes.setBounds(50,140, 200, 50);
        fond.add(inputLignes);
        
        JLabel nbColonnes= new JLabel("Veuillez saisir un nombre de colonnes: ");
        nbColonnes.setBounds(0,205,lx,15);
        nbColonnes.setHorizontalAlignment(SwingConstants.CENTER);
        fond.add(nbColonnes);
		
		inputColonnes= new JTextField();
        inputColonnes.setBounds(50,225, 200, 50);
        fond.add(inputColonnes);
        
        effacer= new JButton("EFFACER");
        effacer.setBounds(50,290,95,45);
        effacer.setBackground(new Color(175,175,175));
        fond.add(effacer);
        effacer.addActionListener(this);
        
        
        valider= new JButton("VALIDER");
        valider.setBounds(155,290,95,45);
        valider.setBackground(new Color(175,175,175));
        fond.add(valider);
        valider.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e) {
        if (e.getSource()== effacer){
            inputLignes.setText("");
            inputColonnes.setText("");
        } else if (e.getSource() == valider) {
            try {
                controleur.creerCarte(inputNom.getText(), Integer.parseInt(inputLignes.getText()), Integer.parseInt(inputColonnes.getText()));
                setVisible(false);
            } catch(IOException ex){
                ex.printStackTrace();
            }
        }
	}
}

