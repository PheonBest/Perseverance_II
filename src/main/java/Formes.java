import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class Formes {
    public static final Color[] COULEURS_DIAGONALES = {  
        new Color(229,229,229), // gris clair  
        new Color(222,222,222)  // gris
    };
    public static final int ESPACE_LIGNE = 40;
    public static final int LARGEUR_LIGNE = ESPACE_LIGNE/2;

    public static void dessinerRectangle(Graphics2D g2d, int coinX, int coinY, int largeur, int hauteur, int rayonX, int rayonY) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setPaint(COULEURS_DIAGONALES[0]);

        Shape rectangle = new RoundRectangle2D.Float(coinX, coinY, largeur - 1f, hauteur -1f, rayonX, rayonY);
        g2d.setClip(rectangle);
        g2d.fill(rectangle);
        

        // dessin des lignes diagonales
        g2d.setColor(COULEURS_DIAGONALES[1]);
        g2d.setStroke(new BasicStroke(LARGEUR_LIGNE));
        
        // On trace les lignes:
        // On part du coin en haut à gauche
        // On arrive tout à droite du rectangle en X, et on translate vers le bas en y à chaque itération
        for (int x = coinX, y = coinY; y < (coinY + largeur + hauteur); y += ESPACE_LIGNE)
            g2d.drawLine(x, y ,  x + (int) largeur , y  - (int) largeur);
    }

}
