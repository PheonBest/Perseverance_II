package app;

import javax.swing.SwingUtilities;
import java.awt.Dimension;
import java.awt.Toolkit;

public class App 
{
    public static void main( String[] args )
    {
        Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screensize.getWidth() * 3 / 4);
        int height = (int) (screensize.getHeight() * 3 / 4);

        
    }
}