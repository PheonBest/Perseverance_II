import java.awt.Graphics2D;
import java.awt.Graphics;

public class BoutonCercle implements Dessin {
    private boolean sourisDessus = false;
    private int x;
    private int y;
    private double rayon;
    
    public BoutonCercle(int x, int y, double rayon) {
        this.x = x;
        this.y = y;
        this.rayon = rayon;
    }

    public void dessiner(Graphics g) {
        // On dessine le cercle
        Graphics2D g2d = (Graphics2D) g;
        if (sourisDessus)
            g2d.drawOval(x-(int)(1.15*rayon),y-(int)(1.15*rayon),(int)(2*1.15*rayon),(int)(2*1.15*rayon));
        else
            g2d.drawOval(x-(int)rayon,y-(int)rayon,(int)(2*rayon),(int)(2*rayon));
    }
}
