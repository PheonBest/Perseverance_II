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

    // Daniel Kvist, https://stackoverflow.com/questions/27706197/how-can-i-center-graphics-drawstring-in-java
    public static void dessinerTexteCentre(Graphics g, String text, int coordX, int coordY, Font font) {
        // Get the FontMetrics
        FontMetrics metrics = g.getFontMetrics(font);
        final int NOMBRE_LIGNES_TEXTE = text.split("\n").length;
        int index = 0;
        g.setFont(font);
        for (String ligne : text.split("\n")) {
            // Determine the X coordinate for the text
            int x = coordX - metrics.stringWidth(ligne) / 2;
            // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
            int y = coordY - metrics.getHeight() / 2 + metrics.getAscent() - (int)((NOMBRE_LIGNES_TEXTE/2.-index)*metrics.getHeight());
            // Set the font
            // Draw the String
            g.drawString(ligne, x, y);
            index++;
        }
    }

}
