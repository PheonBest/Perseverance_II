
import java.awt.Dimension;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import javax.swing.CellEditor;
import javax.swing.SwingWorker;

public class Controleur {
    
    private Donnees donnees;
    private boolean estModifie = true;

    public Controleur(Donnees donnees) {
        this.donnees = donnees;
    }

	public void jouer() {
        donnees.majJoueur(new Robot(donnees.getImagesJoueur(), donnees.obtenirLargeur()/2, donnees.obtenirHauteur()/2));
        donnees.majScene("Jeu");
        donnees.notifierObserveur(TypeMisAJour.Scene);
        LinkedList<Dimension> buts = new LinkedList<Dimension>();
        buts.add(new Dimension(100,100));
        buts.add(new Dimension(300,300));
        donnees.obtenirJoueur().definirBut(buts);
	}

	public void rafraichir() {
        if (donnees.getScene() == "Jeu") { // Si on est en jeu
            donnees.obtenirJoueur().move();
            donnees.obtenirJoueur().rafraichirImage();
            donnees.notifierObserveur(TypeMisAJour.Joueur);
            int dx = -donnees.obtenirJoueur().getDx();
            int dy = -donnees.obtenirJoueur().getDy();
            if (dx != 0 || dy != 0) {
                //System.out.println("dx : "+dx+" _ dy : "+dy);
                Cellule[][] cellules = donnees.obtenirCellules();
                for (int i=0; i < cellules.length; i++) {
                    for (int j=0; j < cellules[i].length; j++)
                        cellules[i][j].translate(dx, dy);
                }
                donnees.notifierObserveur(TypeMisAJour.Cellules);
            }
            donnees.notifierObserveur(TypeMisAJour.Peindre);
        } else {
            if (estModifie) {
                estModifie = false;
                donnees.notifierObserveur(TypeMisAJour.Cellules);
                donnees.notifierObserveur(TypeMisAJour.Peindre);
            }
        }
	}
    public void charger() {
        Charger threadWorkerChargement = new Charger();
        threadWorkerChargement.execute();
    }

    private class Charger extends SwingWorker {

        @Override
        protected ArrayList<ArrayList<Image>> doInBackground() throws Exception {
            // Chargement des images du joueur
            ArrayList<Collection<String>> fileNames = new ArrayList<Collection<String> >(4); 
            ArrayList<ArrayList<Image>> images = new ArrayList<ArrayList<Image>>(4);

            Pattern pattern;

            int avancementChargement = 0;
            int nombreImages = 4*21;
            
            for (int i = 0; i < 4; i++) {
                // On veut que le chemin vers le fichier
                // contienne le nom du dossier parent (OPTIONS.Options.JOUEURS_IMAGES[i])
                // On utilise les délimiteurs \b
                // ex: \bRun\b
                
                
                pattern = Pattern.compile("^.*\\b"+Options.JOUEURS_IMAGES[i]+"\\b.*\\.(?:jpg|gif|png)");
                final Collection<Image> tmp = ObtenirRessources.getImages(pattern, "res/robot/"+Options.JOUEURS_IMAGES[i]+"/");
                images.add(new ArrayList<Image>());
                for (Image img: tmp) {
                    images.get(i).add( TailleImage.resizeImage( img, Options.JOUEUR_LARGEUR, (int)((double)Options.JOUEUR_LARGEUR*((double)img.getHeight(null)/(double)img.getWidth(null)))) );
                }
                avancementChargement +=tmp.size();
                publish((int)(100.*avancementChargement/nombreImages)); // On publie l'avancement du chargement (résultat intermédiaire)
                
            }

            // On enregistre les images chargées dans la classe Données
            return images; // On renvoie un ArrayList<ArrayList<Image>> (résultat final du Worked Thread)
        }

        @Override
        protected void process(List historique) {
            // On définit ici ce que l'event dispatch thread
            // fait avec les résultats intermédiaires publiés
            // pendant que le thread s'exécute
            int avancement = (int) historique.get(historique.size()-1);
            donnees.majAvancement(avancement);
            donnees.notifierObserveur(TypeMisAJour.Avancement);
        }

        @Override
        protected void done() {
            // Cette méthode est appellée lorsque
            // la tâche du Worker Thread est terminée
            try {
                ArrayList<ArrayList<Image>> imagesJoueur = (ArrayList<ArrayList<Image>>) get();
                donnees.majImagesJoueur(imagesJoueur);
                jouer();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public void majScene(String scene) {
        donnees.majScene(scene);
    }

    private void majCelluleType(int ligne, int colonne, TypeCase type) {
        if (ligne > -1 && ligne < donnees.obtenirCellules().length && colonne > -1 && colonne < donnees.obtenirCellules()[0].length)
            donnees.obtenirCellules()[ligne][colonne].majType(type);
    }

    public void click(int x, int y) {
        boolean estModifie = false;
        //Gestion de la carte

        // Si on est dans le jeu
        // OU
        // Si on est en jeu ou dans l'éditeur de carte, et qu'on a cliqué sur la carte
        final double COIN_GAUCHE_MENU = donnees.obtenirLargeur()*(Options.RATIO_LARGEUR_MENU-1)/Options.RATIO_LARGEUR_MENU;
        if (donnees.getScene().equals("Jeu") || (donnees.getScene().equals("Editeur de carte") && x < COIN_GAUCHE_MENU)) {
            Cellule[][] cellules = donnees.obtenirCellules();
            for (int i=0; i < cellules.length; i++) {
                for (int j=0; j < cellules[i].length; j++) {
                    if (cellules[i][j].contains(x, y)) {
                        if (donnees.obtenirDerniereCellule() == cellules[i][j]) { // On compare les pointeurs (références) des 2 objets
                            //cellules[i][j].majSourisDessus(false);
                            donnees.majDerniereCellule(null);
                        } else {
                            System.out.println("Case "+i+" "+j);
                            /*
                            cellules[i][j].majSourisDessus(true);
                            if (donnees.obtenirDerniereCellule() != null)
                                donnees.obtenirDerniereCellule().majSourisDessus(false);
                            */
                            if (donnees.getScene().equals("Jeu")) { // Calcul et transmission de la trajectoire

                            } else { // Modification de la carte selon les outils d'édition sélectionnés
                                // Selon la taille du pinceau:
                                // Taille 1: on ne modifie que la case sélectionnée
                                // Taille 2: on modifie les 6 cases autour ET la case sélectionnée
                                // Taille 2: on modifie les 6 + 12 cases autour ET la case sélectionnée
                                if (donnees.obtenirDerniereCaseType() != null) {
                                    final TypeCase type = donnees.obtenirDerniereCaseType().obtenirType();
                                    int taillePinceau = 1;
                                    if (donnees.obtenirDernierBouton() != null)
                                        taillePinceau = donnees.obtenirDernierBouton().obtenirTaillePinceau();
                                    switch (taillePinceau) {
                                        case 3:
                                            majCelluleType(i-2,j, type);
                                            majCelluleType(i+2,j, type);
                                    
                                            majCelluleType(i,j+2, type);
                                            majCelluleType(i+1,j+2, type);
                                            majCelluleType(i-1,j+2, type);
                                            majCelluleType(i,j-2, type);
                                            majCelluleType(i+1,j-2, type);
                                            majCelluleType(i-1,j-2, type);
                                    
                                    
                                            if(j%2==0){
                                                majCelluleType(i-1,j+1, type);
                                                majCelluleType(i+2,j+1, type);
                                                majCelluleType(i+2,j-1, type);
                                                majCelluleType(i-1,j-1, type);
                                            } else {
                                                majCelluleType(i-2,j+1, type);
                                                majCelluleType(i+1,j+1, type);
                                                majCelluleType(i+1,j-1, type);
                                                majCelluleType(i-2,j-1, type);
                                            }
                                        case 2:
                                            majCelluleType(i-1, j, type);
                                            majCelluleType(i+1, j, type);
                                            majCelluleType(i, j+1, type);
                                            majCelluleType(i, j-1, type);
                                            if(j%2==0){
                                                majCelluleType(i+1,j+1,type);
                                                majCelluleType(i+1,j-1, type);
                                            } else {
                                                majCelluleType(i-1,j+1, type);
                                                majCelluleType(i-1,j-1, type);
                                            }
                                        case 1:
                                            majCelluleType(i, j, type);
                                            break;
                                    }
                                }
                            }
                        }
                        estModifie = true;
                        donnees.notifierObserveur(TypeMisAJour.Cellules);
                        break;
                    }
                }
            }
        }

        //Gestion du menu de l'éditeur de carte

        // Si on est dans l'éditeur de carte, et qu'on a cliqué sur le menu
        if (donnees.getScene().equals("Editeur de carte") && x >= COIN_GAUCHE_MENU) {
            x -= COIN_GAUCHE_MENU; // Les coordonnées des boutons sont relatives au coin en haut à gauche du menu. Donc on soustrait sa coordonnée x.
            Cellule[] boutonsType = donnees.obtenirBoutonsType();
            for (int i=0; i < boutonsType.length; i++) {
                if (boutonsType[i].contains(x,y)) {
                    if (donnees.obtenirDerniereCaseType() == boutonsType[i]) { // On compare les pointeurs (références) des 2 objets
                        boutonsType[i].majSourisDessus(false);
                        donnees.majDerniereCaseType(null);
                    } else {
                        boutonsType[i].majSourisDessus(true);
                        if (donnees.obtenirDerniereCaseType() != null)
                            donnees.obtenirDerniereCaseType().majSourisDessus(false);
                        donnees.majDerniereCaseType(boutonsType[i]);
                    }
                    estModifie = true;
                    donnees.notifierObserveur(TypeMisAJour.BoutonsType);
                    break;
                }
            }
            
            BoutonCercle[] boutonsCercle = donnees.obtenirBoutonsCercle();
            for (int i=0; i < boutonsCercle.length; i++) {
                if (boutonsCercle[i].contains(x,y)) {
                    if (donnees.obtenirDernierBouton() == boutonsCercle[i]) { // On compare les pointeurs (références) des 2 objets
                        donnees.obtenirDernierBouton().majSourisDessus(false);
                        donnees.majDernierBouton(null);
                    } else {
                        boutonsCercle[i].majSourisDessus(true);
                        if (donnees.obtenirDernierBouton() != null)
                            donnees.obtenirDernierBouton().majSourisDessus(false);
                        donnees.majDernierBouton(boutonsCercle[i]);
                    }
                    estModifie = true;
                    donnees.notifierObserveur(TypeMisAJour.BoutonsCercle);
                    break;
                }
            }
        }
        if (estModifie)
            donnees.notifierObserveur(TypeMisAJour.Peindre);
    }

    public void editer() {
        final int LARGEUR_MENU = (int)(donnees.obtenirLargeur()/Options.RATIO_LARGEUR_MENU);
        
        //BoutonsCercles
        final int NOMBRE_BOUTONS_CIRCULAIRES = 3;
        // Longueur totale des cercles:
        // Somme de i=1 à n de (R+0,25*R*i) = nR+0,25*R*n*(n+1)/2 = n*R*(1+(n+1)/8)
        // où R est le rayon initiale du cercle
        final int LONGUEUR_BOUTONS = NOMBRE_BOUTONS_CIRCULAIRES*Options.LARGEUR_BOUTON_CASE*(1+(NOMBRE_BOUTONS_CIRCULAIRES+1)/8);
        final int LONGUEUR_ESPACEMENTS = (NOMBRE_BOUTONS_CIRCULAIRES-1)*Options.ESPACE_INTER_BOUTON;
        final int LONGUEUR_TOTALE = LONGUEUR_ESPACEMENTS + LONGUEUR_BOUTONS;
        
        BoutonCercle[] boutonsCercle = new BoutonCercle[NOMBRE_BOUTONS_CIRCULAIRES];
        for (int i=0; i<NOMBRE_BOUTONS_CIRCULAIRES; i++) {
            final int longueuBoutons = i*Options.LARGEUR_BOUTON_CASE*(1+(i+1)/8);
            final int x = (int)((LARGEUR_MENU-LONGUEUR_TOTALE)/2+i*Options.ESPACE_INTER_BOUTON+longueuBoutons);
            final int y = 100;
            boutonsCercle[i] = new BoutonCercle(x,y,Options.RAYON_BOUTON_CERCLE*(1+i*0.25), i+1);
        }

        //BoutonsHex
        final TypeCase[] types = TypeCase.values();
        
        final double TAILLE = 0.5;
        final int NOMBRE_COLONNES = (int) Math.ceil(types.length/(double)Options.NOMBRE_BOUTONS_TYPE_PAR_LIGNE);
        final int LONGUEUR_ESPACEMENTS_CASE = (Options.NOMBRE_BOUTONS_TYPE_PAR_LIGNE+1)*Options.ESPACE_INTER_CASE_BOUTON;
        final int LONGUEUR_CASES = (int)(Options.NOMBRE_BOUTONS_TYPE_PAR_LIGNE*Options.LARGEUR_BOUTON_CASE*TAILLE + (Options.NOMBRE_BOUTONS_TYPE_PAR_LIGNE-1)*Options.ESPACE_INTER_CASE_BOUTON);
        final int LONGUEUR_TOTALE_CASES = LONGUEUR_ESPACEMENTS_CASE + LONGUEUR_CASES;
        int index = 0;
        Cellule[] boutonsType = new Cellule[types.length];
        for (int i=0; i<NOMBRE_COLONNES; i++) {
            for (int j=0; j<Options.NOMBRE_BOUTONS_TYPE_PAR_LIGNE; j++) {
                if (index < types.length) {
                    boutonsType[index] = new Cellule(types[index], i, j, TAILLE, Options.ESPACE_INTER_CASE_BOUTON);
                    boutonsType[index].translate((LARGEUR_MENU-LONGUEUR_TOTALE_CASES)/2, Options.yLabels[1]+80);
                }
                index++;
            }
        }

        donnees.majBoutonsCercle(boutonsCercle);
        donnees.majBoutonsType(boutonsType);
        donnees.majScene("Editeur de carte");

        donnees.notifierObserveur(TypeMisAJour.BoutonsCercle);
        donnees.notifierObserveur(TypeMisAJour.BoutonsType);
        donnees.notifierObserveur(TypeMisAJour.Peindre);
    }
}
