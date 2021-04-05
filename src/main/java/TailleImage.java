
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.AlphaComposite;
import java.awt.RenderingHints;
import java.awt.Dimension;
public class TailleImage {

    /*
    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }
    */

    // https://stackoverflow.com/questions/3967731/how-to-improve-the-performance-of-g-drawimage-method-for-resizing-images/11371387#11371387
    /**
     * we want the x and o to be resized when the JFrame is resized
     *
     * @param originalImage an x or an o. Use cross or oh fields.
     *
     * @param biggerWidth
     * @param biggerHeight
     */
    public static Image resizeImage(Image originalImage, int biggerWidth, int biggerHeight, boolean preserveAspectRatio) {
        int type = BufferedImage.TYPE_INT_ARGB;

        if (preserveAspectRatio) {
            if (biggerWidth > biggerHeight)
                biggerHeight = biggerHeight*originalImage.getWidth(null)/originalImage.getHeight(null);
            else
                biggerWidth = biggerWidth*originalImage.getHeight(null)/originalImage.getWidth(null);
        }
        BufferedImage resizedImage = new BufferedImage(biggerWidth, biggerHeight, type);
        Graphics2D g = resizedImage.createGraphics();

        g.setComposite(AlphaComposite.Src);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.drawImage(originalImage, 0, 0, biggerWidth, biggerHeight, null);
        g.dispose();

        return resizedImage;
    }
}
