import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.BasicStroke;

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
        g2d.setColor(Color.white);
        float[] fa = {10, 10, 10};       // Patterne en pointillés

        BasicStroke bs = new BasicStroke(5, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10, fa, 10);
        g2d.setStroke(bs);
        if (sourisDessus)
            g2d.fillOval(x-(int)(1.15*rayon),y-(int)(1.15*rayon),(int)(2*1.15*rayon),(int)(2*1.15*rayon));
        else
            g2d.fillOval(x-(int)rayon,y-(int)rayon,(int)(2*rayon),(int)(2*rayon));
    }
}
