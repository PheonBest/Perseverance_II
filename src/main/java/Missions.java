import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.regex.Pattern;

public class Missions extends JFrame {
    
    private int lx = 700;
    private int ly = 700;
    //------------------------------------------------------------------ Constructeur

    public Missions(int x, int y){
        // Fenêtre
        super();
        setLayout(null);
        setLocation(x,y);
        setSize(lx,ly);
        setTitle("MISSIONS");
        this.setVisible(true);
	}
	
}
        
        

