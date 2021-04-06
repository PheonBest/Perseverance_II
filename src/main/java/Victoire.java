import java.util.LinkedList; 
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Victoire extends JFrame{
	
	private LinkedList<Symbole> mesVictoires;
	
	public Victoire(LinkedList<Symbole> maListeVictoires){
		mesVictoires=maListeVictoires;
		this.setTitle("Victoire");
		this.setSize(400,500);
		this.setLocation(200,60);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	
	
	
}
