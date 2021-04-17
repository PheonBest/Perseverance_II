import java.awt.Image;
import java.awt.Polygon;

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
		this(TypeSymbole.VIDE, null, false);
	}

	//Dans Symbole:On rajoute la méthode
	public void majSymbole(TypeSymbole type, Image img) {
		this.type = type;
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

