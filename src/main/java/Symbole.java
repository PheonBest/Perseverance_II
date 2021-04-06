import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.BasicStroke;
import java.awt.geom.AffineTransform;
import java.awt.Image;

public class Symbole extends Polygon {
	
	public TypeSymbole type;
	public Image image;
	public boolean estVisible = false;
	
	public Symbole(TypeSymbole type, Image image, boolean estVisible){
		this.estVisible = estVisible;
		this.type=type;
		this.image=image;
		//this.image=getImage();
	}
	public Symbole() {
		this(null, null, false);
	}

	//Dans Symbole:On rajoute la méthode
	public void majSymbole(Image img) {	
		this.image = img;
	}

	public Image obtenirImage() {
		return image;
	}

	public void majEstVisible(boolean estVisible) {
		this.estVisible = estVisible;
	}
	
	public boolean obtenirEstVisible() {
		return estVisible;
	}
	
	
}

