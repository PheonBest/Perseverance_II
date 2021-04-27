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
        // On dessine plusieurs images l'une à côté de l'autre
        // En effet, la texture de l'arrière plan est 'seamless'. On peut la répéter.

        // Dimensions de l'image à dupliquer
        final int LARGEUR_IMAGE = (int)(largeur * zoom);
        final int HAUTEUR_IMAGE = (int)(hauteur * zoom);

        // On détermine le nombre d'images en X et Y
        // On arrondit vers le haut car si il nous faut 4,1 images pour remplir l'écran en largeur
        // cela veut dire qu'il nous en faut 5.
        final int NOMBRE_IMAGES_X = (int)Math.ceil((largeurEcran)/LARGEUR_IMAGE+1);
        final int NOMBRE_IMAGES_Y = (int)Math.ceil((hauteurEcran)/HAUTEUR_IMAGE+1);

        // On ajoute une image en X et en Y:
        // Dans le cas où on remplit parfaitement l'écran, si on décale l'image (horizontalement par exemple)
        // le vide dû au décalage doit être comblé. D'où l'ajout d'une image
        // Or, si on a un facteur de zoom de 0.5, l'image est 1/0.5=2 fois moins grande.
        // On doit donc ajouter 1/zoom images au lieu d'une
        // Comme au-dessus, on arrondit vers le haut
        final int UNE_IMAGE = (int) Math.ceil(1/zoom);

        // Prise en compte du zoom:
        // le zoom est automatiquement appliqué grâce à l'objet Graphics
        // Mais on veut que les images se déplacent en même temps que le joueur (dans le sens opposé à son déplacement)
        // On a donc une position (x,y)
        // Or si l'image est en (1000,1000) et qu'on est a un zoom de 2, elle est affichée comme 2 fois plus distante
        // Pour être cohérent par rapport aux dimensions de l'écran, on doit donc appliquer le facteur de zoom à ces coordonnées
        final int DELTA_IMAGES_X = (int)Math.ceil( - ( (x*zoom+largeurEcran/2)/LARGEUR_IMAGE + UNE_IMAGE));
        final int DELTA_IMAGES_Y = (int)Math.ceil( - ( (y*zoom+hauteurEcran/2)/HAUTEUR_IMAGE + UNE_IMAGE));
        
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
