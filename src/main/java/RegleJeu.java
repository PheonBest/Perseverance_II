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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.InputStream;
import javax.swing.ImageIcon;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class RegleJeu extends JFrame {
		
		private JPanel panneau;
		
		
		public RegleJeu() {
			
			super();
			this.setSize(400,400);
			this.setLocation(600,600);
			this.setLayout(null);
			
			panneau= new JPanel();
			panneau= new JPanel();
			panneau.setLayout(null);
			panneau.setBounds(20,20,340,150);
			panneau.setBackground(Color.green);
			panneau.setVisible(true);
			
			this.add(panneau);
			
			
			

			
		}


}

