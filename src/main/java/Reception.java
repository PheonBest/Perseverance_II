import java.awt.Dimension;
import java.awt.Image;
import java.util.ArrayList;
import java.util.LinkedList;

public class Reception{
	
	private Cellule[][] carte;
	private Robot joueur;
	
	public Reception (Cellule [][] map, int [] joueur, ArrayList<ArrayList<Image>> image){
		carte = map;
		this.joueur = new Robot (joueur[0], joueur[1], new int[] {joueur[4],joueur[5],joueur[6],joueur[7],joueur[8],joueur[9],joueur[10]}, new int[] {joueur[11],joueur[12],joueur[13],joueur[14],joueur[15],joueur[16],joueur[17]},joueur[18],joueur[19],0, Options.JOUEUR_DUREE_ANIMATION, image);
		this.joueur.majCase(joueur[2], joueur[3]);
		this.joueur.majNombrePonts(joueur[20]);
		if(joueur[21]==1){
			this.joueur.majSurChenilles(true);
		}else{
			this.joueur.majSurChenilles(false);
		}
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

		
