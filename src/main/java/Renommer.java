import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.util.HashMap;

import javax.swing.*;

public class Renommer extends JFrame{
	int x=100;
	int y=100;
	int lx=300;
	int ly=220;
	private JButton valider;
	private JButton effacer;
	private JTextField nom;
	
	public Renommer(){
		
		super();
        setLayout(null);
        setLocation(x,y);
        setSize(lx,ly+20);
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
        
        JTextField nom= new JTextField();
        nom.setBounds(10,50, 280, 50);
        fond.add(nom);
		
		effacer= new JButton("EFFACER");
		effacer.setBounds(10,110,135,50);
		effacer.setBackground(new Color(175,175,175));
		fond.add(effacer);
		//effacer.addActionListener(this);
		
		valider= new JButton("VALIDER");
		valider.setBounds(155,110,135,50);
		valider.setBackground(new Color(175,175,175));
		fond.add(valider);
		
				
	
	}
	/*
	 public void actionPerformed(ActionEvent e) {
			if(e.getSource()==effacer){
				nom.setText(" ");
			}
		}
	*/
}

