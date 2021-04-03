import java.awt.Image;
import java.awt.Graphics;

public class ArrierePlan {
    private Image image;
    private int largeur;
    private int hauteur;
    private double x = 0;
    private double y = 0;

    public ArrierePlan(Image image) {
        this.image = image;
        largeur = image.getWidth(null);
        hauteur = image.getHeight(null);
    }
    
    public void dessiner(Graphics g, double largeurEcran, double hauteurEcran, double zoom) {
        // On détermine le nombre d'images
        
        final int LARGEUR_IMAGE = (int)(largeur * zoom);
        final int HAUTEUR_IMAGE = (int)(hauteur * zoom);
        final int NOMBRE_IMAGES_X = (int)Math.ceil((largeurEcran)/LARGEUR_IMAGE+1);
        final int NOMBRE_IMAGES_Y = (int)Math.ceil((hauteurEcran)/HAUTEUR_IMAGE+1); // On arrondit vers le haut
        final int UNE_IMAGE = (int) Math.ceil(1/zoom);
        final int DELTA_IMAGES_X = (int)Math.ceil( - ( (x*zoom+largeurEcran/2)/LARGEUR_IMAGE + UNE_IMAGE));
        final int DELTA_IMAGES_Y = (int)Math.ceil( - ( (y*zoom+hauteurEcran/2)/HAUTEUR_IMAGE + UNE_IMAGE));
        //System.out.println(DELTA_IMAGES_Y);
        //System.out.println(y+" "+hauteurEcran/2);
        
        for (int i=0; i < NOMBRE_IMAGES_Y + UNE_IMAGE; i++) {
            for (int j=0; j < NOMBRE_IMAGES_X + UNE_IMAGE; j++)
                
                g.drawImage(image, (int)x+largeur*(j+DELTA_IMAGES_X), (int)y+hauteur*(i+DELTA_IMAGES_Y), largeur, hauteur, null);
        }
    }

    public void translate(int dx, int dy) {
        x += dx;
        y += dy;
    }

    public void majCoords(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
