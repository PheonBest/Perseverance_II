import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.BasicStroke;
import java.awt.Image;

public class BoutonCercle implements Dessin {
    private boolean sourisDessus = false;
    private int x;
    private int y;
    private double rayon;
    private int taillePinceau;
    private Image image;
    private String effet;
    
    public BoutonCercle(int x, int y, double rayon, int taillePinceau) {
        this.x = x;
        this.y = y;
        this.rayon = rayon;
        this.image = image;
        this.taillePinceau = taillePinceau;
    }

    public BoutonCercle(int x, int y, double rayon, String effet, Image image) {
        this.x = x;
        this.y = y;
        this.rayon = rayon;
        this.image = image;
        this.effet = effet;
    }

    public void dessiner(Graphics g) {
        // On dessine 2 cercles:
        // Un premier en noir pour faire la bordure
        // Un deuxième en blanc par-dessus

        
        Graphics2D g2d = (Graphics2D) g;
        // On dessine le cercle "Bordure"
        final int EPAISSEUR_BORDURE = 12;
        
        g2d.setPaint(Color.BLACK);
        g2d.setStroke(new BasicStroke(EPAISSEUR_BORDURE));
        if (sourisDessus)
            g2d.drawOval(x-(int)(1.15*rayon),y-(int)(1.15*rayon),(int)(2*1.15*rayon),(int)(2*1.15*rayon));
        else
            g2d.drawOval(x-(int)rayon,y-(int)rayon,(int)(2*rayon),(int)(2*rayon));
        g2d.setPaint(Color.DARK_GRAY);
        // On dessine le deuxième cercle
        if (sourisDessus)
            g2d.fillOval(x-(int)(1.15*rayon),y-(int)(1.15*rayon),(int)(2*1.15*rayon),(int)(2*1.15*rayon));
        else
            g2d.fillOval(x-(int)rayon,y-(int)rayon,(int)(2*rayon),(int)(2*rayon));

        // On dessine l'éventuelle image par-dessus le cercle
        if (image != null) {
            if (sourisDessus) {
                Image imageAgrandie = TailleImage.resizeImage(image, (int)(image.getWidth(null)*1.1), (int)(image.getHeight(null)*1.1), true);
                g2d.drawImage(imageAgrandie, (int)(x-imageAgrandie.getWidth(null)/2), (int)(y-imageAgrandie.getHeight(null)/2), null);
            } else
                g2d.drawImage(image, (int)(x-image.getWidth(null)/2), (int)(y-image.getHeight(null)/2), null);
        }
        
        
    }

    public void majSourisDessus(boolean sourisDessus) {
        this.sourisDessus = sourisDessus;
    }
    
    public boolean contains(int xEvent, int yEvent) {
        // Le point est contenu dans le cercle ssi
        // distance^2 < rayon^2
        // (x-xEvent)^2+(y-yEvent)^2 < rayon^2
        if (sourisDessus)
            return (Math.pow(x-xEvent,2)+Math.pow(y-yEvent,2) < Math.pow(1.15*rayon,2));
        return (Math.pow(x-xEvent,2)+Math.pow(y-yEvent,2) < Math.pow(rayon,2));
    }

    public boolean equals(BoutonCercle b) {
        if (b == null)
            return false;
        return (x == b.x && y == b.y && rayon == b.rayon);
    }

    public String obtenirEffet() {
        return effet;
    }

    public int obtenirTaillePinceau() {
        return taillePinceau;
    }
}
