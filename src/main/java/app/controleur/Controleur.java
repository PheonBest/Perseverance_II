package app.controleur;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import java.awt.Dimension;
import java.awt.Image;
import app.Options;
import app.carte.Cellule;
import app.donnees.Donnees;
import app.sprites.Joueur;
import app.utils.ObtenirRessources;
import app.utils.TailleImage;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

public class Controleur {
    
    Donnees donnees;

    public Controleur(Donnees donnees) {
        this.donnees = donnees;
    }

	public void jouer() {
        ArrayList<ArrayList<Image>> images = new ArrayList<ArrayList<Image> >(4); 
        ArrayList<Collection<String>> fileNames = new ArrayList<Collection<String> >(4); 
        // initializing
        

        Pattern pattern;
        Collection<String> list;
        String mot;
        Image img;
        
        for (int i = 0; i < 4; i++) {
            // On veut que le chemin vers le fichier
            // contienne le nom du dossier parent (OPTIONS.Options.JOUEURS_IMAGES[i])
            // On utilise les délimiteurs \b
            // ex: \bRun\b
            
            pattern = Pattern.compile("^.*\\b"+Options.JOUEURS_IMAGES[i]+"\\b.*$");
            list = ObtenirRessources.getResources(pattern);
            /* System.out.println(pattern);
             * System.out.println(list.size());
             * for(final String name : list){
             *     System.out.println(name);
             * }
             * System.out.println();
             */
            fileNames.add(list);
        }
        for (int i = 0; i < fileNames.size(); i++) {
            images.add(new ArrayList<Image>()); // Il faut initialiser les valeurs de l'ArrayList 2D
            // Sinon: Index 0 out of bounds for length 0
            for (String name: fileNames.get(i)) {
                if (name != null) {
                    try {
                        //images.get(i).add(ImageIO.read(getClass().getResourceAsStream(name)));
                        img = ImageIO.read( new FileInputStream(name));
                        images.get(i).add( TailleImage.resizeImage( img, Options.JOUEUR_LARGEUR, (int)((double)Options.JOUEUR_LARGEUR*((double)img.getHeight(null)/(double)img.getWidth(null)))) );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        donnees.majJoueur(new Joueur(images, donnees.obtenirLargeur()/2, donnees.obtenirHauteur()/2));
        LinkedList<Dimension> buts = new LinkedList<Dimension>();
        buts.add(new Dimension(100,100));
        buts.add(new Dimension(300,300));
        donnees.getJoueur().definirBut(buts);
	}

	public void rafraichir() {
        donnees.getJoueur().move();
        donnees.getJoueur().rafraichirImage();
        int dx = -donnees.getJoueur().getDx();
        int dy = -donnees.getJoueur().getDy();
        //System.out.println("dx : "+dx+" _ dy : "+dy);
        Cellule[][] cellules = donnees.getCellules();
        for (int i=0; i < cellules.length; i++) {
            for (int j=0; j < cellules[i].length; j++)
                cellules[i][j].translate(dx, dy);
        }
        donnees.notifyObserver();
	}
    
}
