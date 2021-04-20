import java.awt.Dimension;
import java.awt.Image;
import java.util.ArrayList;
import java.util.LinkedList;

public class Reception{
	
	private Cellule[][] carte;
	private Robot joueur;
	
	public Reception (Cellule [][] map, int [] joueur, ArrayList<ArrayList<Image>> image){
		carte = map;
		int x = 0;
		int y = 0;
		this.joueur = new Robot (joueur[0], joueur[1], new int[] {Options.ALERTE_MIN,Options.ALERTE_MIN,Options.ALERTE_MIN,Options.ALERTE_MIN,Options.ALERTE_MIN,Options.ALERTE_MIN,Options.ALERTE_MIN}, new int[] {Options.USURE_MIN,Options.USURE_MIN,Options.USURE_MIN,Options.USURE_MIN,Options.USURE_MIN,Options.USURE_MIN,Options.USURE_MIN},0,0,0, Options.JOUEUR_DUREE_ANIMATION, image);
		this.joueur.majCase(joueur[2], joueur[3]);
	}
	
	//-------------------------------------------------getters et setters
	public Cellule[][] getCellule(){
		return carte;
	}

	public Robot getJoueur(){
		return joueur;
	}
	
	//-------------------------------------------------méthodes de MAJ
	public void majCarte (Cellule[][] map){
		this.carte=map;
	}
}

		
