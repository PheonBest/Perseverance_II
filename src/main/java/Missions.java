import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.InputStream;
import javax.swing.ImageIcon;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Missions extends JFrame {
    
    private int lx = 650;
    private int ly = 650;
    private JPanel panneauTitre;
    private JLabel titre;
    
    // Constructeur

    public Missions(int x, int y){
        // Fenêtre
        super();
        setLayout(null);
        setLocation(x,y);
        setSize(lx+20,ly+40);
        setTitle("MISSIONS");
        
        JPanel panneauFond= new JPanel();
        panneauFond.setBounds(0,0,lx,ly);
		panneauFond.setBackground(new Color(175,175,175));
		panneauFond.setLayout(null);
		this.add(panneauFond);
		
		panneauTitre= new JPanel();
		panneauTitre.setBounds(10,10,lx-20,50);
		panneauTitre.setBackground(new Color(220,220,220));
        panneauTitre.setLayout(null);
        panneauFond.add(panneauTitre);
        
        titre = new JLabel("------------------ MISSIONS ------------------");
        titre.setBounds(0,0,lx-20,40);
        titre.setFont(new Font("Courier",Font.BOLD,18));
        titre.setHorizontalAlignment(SwingConstants.CENTER);
        titre.setVerticalAlignment(SwingConstants.CENTER);
        panneauTitre.add(titre);
        
		JPanel panneauObjectif= new JPanel();
		panneauObjectif.setBounds(10,70,lx-20,75);
		panneauObjectif.setBackground(new Color(220,220,220));
		panneauObjectif.setLayout(null);
		panneauFond.add(panneauObjectif);
       
        JLabel texteMission= new JLabel("  N'oubliez pas: l'arrivee des futurs colons depend de la reussite de vos missions!" );
        texteMission.setBounds(0,20,lx-20,20);
        texteMission.setFont(new Font("Courier",Font.BOLD,12));
        texteMission.setHorizontalAlignment(SwingConstants.CENTER);
        //texteMission.setVerticalAlignment(SwingConstants.CENTER);
        panneauObjectif.add(texteMission);
        
        JPanel mission1= new JPanel();
        mission1.setBounds(10,155,lx-20,200);
		mission1.setBackground(new Color(220,220,220));
        mission1.setLayout(null);
        panneauFond.add(mission1);
        
        JPanel mission2= new JPanel();
        mission2.setBounds(10,355+10,lx-20,200);
		mission2.setBackground(new Color(220,220,220));
        mission2.setLayout(null);
        panneauFond.add(mission2);
       
		JLabel premMission= new JLabel("MISSION 1");
		premMission.setFont(new Font("Courier",Font.BOLD+Font.ITALIC,18));
		premMission.setBounds(5,5,400,20);
		mission1.add(premMission);
		
		JLabel deuMission= new JLabel("MISSION 2");
		deuMission.setFont(new Font("Courier",Font.BOLD+Font.ITALIC,18));
		deuMission.setBounds(5,5,400,20);
		mission2.add(deuMission);
		
		
	}
	
}
        
        

