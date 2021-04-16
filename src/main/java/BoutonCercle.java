import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.BasicStroke;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

public class BoutonCercle implements Dessin {
    private boolean sourisDessus = false;
    private int x;
    private int y;
    private double rayon;
    private int taillePinceau;
    private Image image;
    private String effet;
    private boolean disponible = true;
    private final double FACTEUR_TAILLE = 1.13;
    
    public BoutonCercle(int x, int y, double rayon, int taillePinceau) {
        this.x = x - Options.EPAISSEUR_BORDURE_CERCLE;
        this.y = y - Options.EPAISSEUR_BORDURE_CERCLE;
        this.rayon = rayon;
        this.image = image;
        this.taillePinceau = taillePinceau;
    }

    public BoutonCercle(int x, int y, double rayon, String effet, Image image, boolean disponible) {
        this.x = x;
        this.y = y;
        this.rayon = rayon;
        this.image = image;
        this.effet = effet;
        this.disponible = disponible;
    }

    public void dessiner(Graphics g) {
        // On dessine 2 cercles:
        // Un premier en noir pour faire la bordure
        // Un deuxième en blanc par-dessus

        
        Graphics2D g2d = (Graphics2D) g;
        // On dessine le cercle "Bordure"
        
        g2d.setPaint(Color.BLACK);
        g2d.setStroke(new BasicStroke(Options.EPAISSEUR_BORDURE_CERCLE));
        Shape cercle = null;
        if (sourisDessus)
            cercle = new Ellipse2D.Double(x-FACTEUR_TAILLE*rayon, y-FACTEUR_TAILLE*rayon, 2*FACTEUR_TAILLE*rayon, 2*FACTEUR_TAILLE*rayon);
        else
            cercle = new Ellipse2D.Double(x-rayon,y-rayon,2*rayon,2*rayon);    
        g2d.draw(cercle);
        g2d.setPaint(Color.DARK_GRAY);
        // On dessine le deuxième cercle
       g2d.fill(cercle);
        // On dessine l'éventuelle image par-dessus le cercle
        if (image != null) {
            if (sourisDessus) {
                Image imageAgrandie = TailleImage.resizeImage(image, (int)(image.getWidth(null)*1.1), (int)(image.getHeight(null)*1.1), true);
                g2d.drawImage(imageAgrandie, (int)(x-imageAgrandie.getWidth(null)/2), (int)(y-imageAgrandie.getHeight(null)/2), null);
            } else
                g2d.drawImage(image, (int)(x-image.getWidth(null)/2), (int)(y-image.getHeight(null)/2), null);
        }

        if (!disponible) {
            // Si le bouton est indisponible, on souhaite appliquer un "filtre" gris au-dessus du bouton
            // Donc on dessine un rectangle de couleur transparente avec un clip sur le cercle
            final int ALPHA = 127; // 50% transparent
            g2d.setPaint(new Color(200, 200, 200, ALPHA));
            g2d.setClip(cercle);
            if (sourisDessus)
                g2d.fillRect(x-(int)(FACTEUR_TAILLE*rayon),y-(int)(FACTEUR_TAILLE*rayon),(int)(2*FACTEUR_TAILLE*rayon),(int)(2*FACTEUR_TAILLE*rayon));
            else
                g2d.fillRect(x-(int)rayon,y-(int)rayon,(int)(2*rayon),(int)(2*rayon));
            g2d.setClip(null);
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
            return (Math.pow(x-xEvent,2)+Math.pow(y-yEvent,2) < Math.pow(FACTEUR_TAILLE*rayon + Options.EPAISSEUR_BORDURE_CERCLE/2,2));
        return (Math.pow(x-xEvent,2)+Math.pow(y-yEvent,2) < Math.pow(rayon + Options.EPAISSEUR_BORDURE_CERCLE/2,2));
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

    public void majDisponible(boolean disponible) {
        this.disponible = disponible;
    }
}
