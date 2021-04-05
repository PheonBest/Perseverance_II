
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ProcessBuilder.Redirect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.awt.Image;

import javax.sound.sampled.AudioInputStream;
import javax.swing.CellEditor;
import javax.swing.SwingWorker;
import java.awt.MouseInfo;

public class Controleur {
    
    private Donnees donnees;
    private boolean estModifie = true;

    public Controleur(Donnees donnees) {
        this.donnees = donnees;
    }

    private boolean aucunJeuEnCours() {
        return (!donnees.obtenirEtatMiniJeuExtraction()
                && !donnees.obtenirEffacerMinijeuExtraction()
                && !donnees.obtenirEtatMiniJeuLaser()
                && !donnees.obtenirDemarrerMinijeuLaser()
                && !donnees.obtenirEffacerMiniJeuLaser());
    }
    // Effet quand on utilise le bras robot sur une case qui contient un symbole
    private void extraire(int ligne, int colonne) {
        donnees.majEtatMinijeuExtraction(true);
        donnees.notifierObserveur(TypeMisAJour.MinijeuExtraction);
    }

    // Effet quand on utilise le scanner sur une case qui contient un symbole
    private void scan(int ligne, int colonne) {
        donnees.majChronometreMinijeuLaser(System.currentTimeMillis());
        donnees.majTempsAvantChrono((int)(Math.random()*((7000-3000)+1)+3000));
        donnees.majEtatMinijeuLaser(true);
        donnees.notifierObserveur(TypeMisAJour.MinijeuLaser);
        
    }

    // Effet quand on marche sur une case
    private void effetCase(int[] coordonnees) {
        if (coordonnees != null) {
            TypeCase typeActif = donnees.obtenirCellules()[coordonnees[0]][coordonnees[1]].obtenirType();
            switch(typeActif){
                case VIDE:
                    break;
                case EAU:
                    break;
                case MONTAGNE:
                    break;
                case DESERT:
                    break;
                case SABLE_MOUVANTS :
                    break;
                case NEIGE:
                    break;
                case FORET:
                    break;
                default :
                    break;
            }
        }
    }

    private void placerJoueur(int ligne, int colonne) {
        int[] but = donnees.obtenirCellules()[ligne][colonne].obtenirCentre();
        int[] coordsJoueur = donnees.obtenirJoueur().obtenirCoordonnees();
        
        final int DELTA_X = coordsJoueur[0]-but[0];
        final int DELTA_Y = coordsJoueur[1]-but[1];
        for (int i=0; i < donnees.obtenirCellules().length; i++) {
            for (Cellule c: donnees.obtenirCellules()[i])
                c.translate(DELTA_X, DELTA_Y);
        }
        donnees.obtenirArrierePlan().translate(DELTA_X, DELTA_Y);
        donnees.obtenirJoueur().majCase(ligne, colonne);
    }
	public void jouer(InputStream carte) {

        donnees.majCellules(CSV.lecture(carte, -donnees.obtenirLargeur()/2, -donnees.obtenirHauteur()/2));
        donnees.obtenirArrierePlan().majCoords(-donnees.obtenirLargeur()/2, -donnees.obtenirHauteur()/2);
        
        donnees.majJoueur(new Robot(donnees.getImagesJoueur(), 0, 0));
        donnees.majScene("Jeu");
        LinkedList<int[]> buts = new LinkedList<int[]>();
        
        
        placerJoueur(0,0);
        buts.add(donnees.obtenirCellules()[0][1].obtenirCentre());
        buts.add(donnees.obtenirCellules()[0][2].obtenirCentre());
        buts.add(donnees.obtenirCellules()[0][3].obtenirCentre());
        buts.add(donnees.obtenirCellules()[0][4].obtenirCentre());
        donnees.obtenirJoueur().definirBut(buts);
        
        donnees.notifierObserveur(TypeMisAJour.Scene);
        donnees.notifierObserveur(TypeMisAJour.Cellules);
        donnees.notifierObserveur(TypeMisAJour.ArrierePlan);
	}

	public void rafraichir() {
        if (!donnees.obtenirEtatOptions()) {

            // Si on est dans l'éditeur de carte
            // et qu'on maintient le click sur la carte (par sur le menu !)
            // On veut continuer à dessiner
            if (donnees.getScene().equals("Editeur de carte") && donnees.obtenirStatutSouris().obtenirX() < donnees.obtenirLargeur()*(Options.RATIO_LARGEUR_MENU-1)/Options.RATIO_LARGEUR_MENU) {
                if (donnees.obtenirStatutSouris().obtenirClicGauche())
                    click(donnees.obtenirStatutSouris().obtenirX(), donnees.obtenirStatutSouris().obtenirY());
            }
            if (donnees.getScene().equals("Jeu")) { // Si on est en jeu
                // Si on doit effacer le minijeu de l'extraction
                if (donnees.obtenirEffacerMinijeuExtraction() && System.currentTimeMillis() - donnees.obtenirChronometreMinijeuExtraction() > Options.TEMPS_AVANT_SUPPRESSION_MINIJEU) {
                    donnees.majEffacerMinijeuExtraction(false);
                    donnees.notifierObserveur(TypeMisAJour.EffacerMinijeuExtraction);
                    donnees.notifierObserveur(TypeMisAJour.MinijeuExtraction);
                    System.out.println("Précision du bras robotique: "+donnees.obtenirScoreExtraction()+" %");
                }

                // Si le minijeu du laser est actif
                // ET si le le temps avant le départ du chronomètre est atteint
                // On envoie l'ordre d'allumer les feux du minijeu en vert
                if (donnees.obtenirEtatMiniJeuLaser() && System.currentTimeMillis() - donnees.obtenirChronometreMinijeuLaser() > donnees.obtenirTempsAvantChrono()) {
                    donnees.majEtatMinijeuLaser(false);
                    donnees.majDemarrerMinijeuLaser(true);
                    donnees.majChronometreMinijeuLaser(System.currentTimeMillis());
                    donnees.notifierObserveur(TypeMisAJour.DemarrerMinijeuLaser);
                } else if (donnees.obtenirEffacerMiniJeuLaser() && System.currentTimeMillis() - donnees.obtenirChronometreMinijeuLaser() > Options.TEMPS_AVANT_SUPPRESSION_MINIJEU) {
                    donnees.majEffacerMiniJeuLaser(false);
                    donnees.notifierObserveur(TypeMisAJour.MinijeuLaser);
                    System.out.println("Temps de réaction: "+donnees.obtenirTempsDeReaction()+" ms");
                }
                    
                donnees.obtenirJoueur().move();
                effetCase(donnees.obtenirJoueur().obtenirDerniereCase());
                donnees.obtenirJoueur().rafraichirImage();
                donnees.notifierObserveur(TypeMisAJour.Joueur);
                int dx = -donnees.obtenirJoueur().getDx();
                int dy = -donnees.obtenirJoueur().getDy();
                if (dx != 0 || dy != 0) {
                    donnees.obtenirArrierePlan().translate(dx, dy);
                    for (int i=0; i < donnees.obtenirCellules().length; i++) {
                        for (int j=0; j < donnees.obtenirCellules()[i].length; j++)
                            donnees.obtenirCellules()[i][j].translate(dx, dy);
                    }
                    donnees.notifierObserveur(TypeMisAJour.ArrierePlan);
                    donnees.notifierObserveur(TypeMisAJour.Cellules);
                }
                donnees.notifierObserveur(TypeMisAJour.Peindre);
            }
        }
	}
    
    public void charger() {

        // Chargement des cartes
        chargerCartes();

        // Chargement de l'arrière plan
        Pattern pattern = Pattern.compile("^.*\\b"+Options.NOM_DOSSIER_IMAGES+"\\b.*\\.(?:jpg|gif|png)");
        try {
            HashMap<String, Image> images = ObtenirRessources.getImagesAndFilenames(pattern, "res/"+Options.NOM_DOSSIER_IMAGES+"/");
            donnees.majArrierePlan(new ArrierePlan(images.get("surface_texture")));
            donnees.notifierObserveur(TypeMisAJour.ArrierePlan);
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }

        // Chargement des musiques
        // Les formats supportés sont:
        // .aifc, .aiff, .au, .snd, .wav
        pattern = Pattern.compile("^.*\\b"+Options.NOM_DOSSIER_MUSIQUES+"\\b.*\\.(?:aifc|aiff|au|snd|wav)");
        try {
            HashMap<String, AudioInputStream> musiques = ObtenirRessources.getAudioAndFilenames(pattern, "res/"+Options.NOM_DOSSIER_MUSIQUES+"/");
            donnees.majListeMusiques(musiques);
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        pattern = Pattern.compile("^.*\\b"+Options.NOM_DOSSIER_EFFETS+"\\b.*\\.(?:aifc|aiff|au|snd|wav)");
        try {
            HashMap<String, AudioInputStream> effets = ObtenirRessources.getAudioAndFilenames(pattern, "res/"+Options.NOM_DOSSIER_EFFETS+"/");
            donnees.majListeEffets(effets);
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }

        

        // Chargement des images des symboles (rapide)
        pattern = Pattern.compile("^.*\\b"+Options.NOM_DOSSIER_SYMBOLE+"\\b.*\\.(?:jpg|gif|png)");

        try {
            HashMap<String, Image> imagesSymboles = ObtenirRessources.getImagesAndFilenames(pattern, "res/"+Options.NOM_DOSSIER_SYMBOLE+"/");
            donnees.majImagesSymboles(imagesSymboles);
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        
        // for (String i : donnees.getImagesSymboles().keySet())
        //     System.out.println("Nom de l'image : " + i + "\nimage: " + donnees.getImagesSymboles().get(i)+"\n");

        // Chargement des images du joueur
        ArrayList<ArrayList<Image>> images = new ArrayList<ArrayList<Image>>(4);
        Charger threadWorkerChargement = new Charger();
        threadWorkerChargement.execute();
    }

    private class Charger extends SwingWorker {

        @Override
        protected ArrayList<ArrayList<Image>> doInBackground() throws Exception {
            // Chargement des images du joueur
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
                majMusique(0);
                boucleMusique();
                donnees.majScene("Choix du mode");
                donnees.notifierObserveur(TypeMisAJour.Scene);
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
        if (!donnees.obtenirEtatOptions()) {
            //System.out.println("Clic en "+x/donnees.obtenirZoom()+" "+y/donnees.obtenirZoom());
            boolean estModifie = false;

            final double COIN_GAUCHE_MENU = donnees.obtenirLargeur()*(Options.RATIO_LARGEUR_MENU-1)/Options.RATIO_LARGEUR_MENU;
            if (!aucunJeuEnCours()) {
                if (donnees.obtenirEtatMiniJeuExtraction()) {
                    donnees.majEffet("win");
                    donnees.majEtatMinijeuExtraction(false);
                    donnees.majChronometreMinijeuExtraction(System.currentTimeMillis());
                    donnees.majEffacerMinijeuExtraction(true);
                    donnees.notifierObserveur(TypeMisAJour.EffacerMinijeuExtraction);
                } else if (donnees.obtenirEtatMiniJeuLaser()) { // Si on on clique avant le début, on reçoit une croix. Au bout de 3 croix, le joueur reçoit un malus
                    if (System.currentTimeMillis() - donnees.obtenirChronometreMinijeuLaser() < donnees.obtenirTempsAvantChrono()) {
                        donnees.majEffet("loose");
                        donnees.majChronometreMinijeuLaser(System.currentTimeMillis()); // On réinitialise le chronomètre
                        if (donnees.obtenirNombreErreursLaser() != Options.NOMBRE_ERREURS_LASER) {
                            donnees.majNombreErreursLaser(donnees.obtenirNombreErreursLaser()+1);
                            if (donnees.obtenirNombreErreursLaser() == Options.NOMBRE_ERREURS_LASER) {
                                // MALUS
                            }
                        }
                        donnees.notifierObserveur(TypeMisAJour.NombreErreursLaser);
                    }
                } else if (donnees.obtenirDemarrerMinijeuLaser()) {
                    donnees.majEffet("win");
                    donnees.majDemarrerMinijeuLaser(false);
                    donnees.notifierObserveur(TypeMisAJour.DemarrerMinijeuLaser);
                    donnees.majChronometreMinijeuLaser(System.currentTimeMillis());
                    donnees.majEffacerMiniJeuLaser(true);
                }
            } else {
            
                //Gestion de la carte

                // Si on est en jeu ou dans l'éditeur de carte, et qu'on a cliqué sur la carte
                if (donnees.getScene() != null && (donnees.getScene().equals("Jeu") || (donnees.getScene().equals("Editeur de carte") && x < COIN_GAUCHE_MENU))) {

                    // Gestion des compétences dans le jeu
                    List<BoutonCercle> competences = donnees.obtenirCompetences();
                    for (BoutonCercle b: competences) {
                        if (b.contains(x, y)) {
                            // Si on a déjà sélectionné la compétene, on la désélectionne
                            if (donnees.obtenirDerniereCompetence() == b) { // On compare les pointeurs (références) des 2 objets
                                donnees.obtenirDerniereCompetence().majSourisDessus(false);
                                donnees.majDerniereCompetence(null);
                                if (donnees.obtenirRayonDeSelection() != 0) {
                                    donnees.majRayonDeSelection(0);
                                    donnees.notifierObserveur(TypeMisAJour.RayonDeSelection);
                                }
                            } else { // On sélectionne la compétence
                                b.majSourisDessus(true);
                                if (donnees.obtenirRayonDeSelection() != 0) {
                                    donnees.majRayonDeSelection(0);
                                    donnees.notifierObserveur(TypeMisAJour.RayonDeSelection);
                                }
                                switch (b.obtenirEffet()) {
                                    case "Drone":
                                        donnees.majRayonDeSelection(4);
                                        donnees.notifierObserveur(TypeMisAJour.RayonDeSelection);
                                        break;
                                    case "Scanner":
                                        donnees.majRayonDeSelection(2);
                                        donnees.notifierObserveur(TypeMisAJour.RayonDeSelection);
                                        break;
                                    case "Réparation":
                                        break;
                                }
                                // On désélectionne l'ancienne compétence
                                if (donnees.obtenirDerniereCompetence() != null)
                                    donnees.obtenirDerniereCompetence().majSourisDessus(false);
                                donnees.majDerniereCompetence(b);
                            }
                            estModifie = true;
                            donnees.notifierObserveur(TypeMisAJour.Competences);
                            break;
                        }
                    }

                    // Gestion des cellules
                    Cellule[][] cellules = donnees.obtenirCellules();
                    for (int i=0; i < cellules.length; i++) {
                        for (int j=0; j < cellules[i].length; j++) {
                            if (cellules[i][j].contains((x - donnees.obtenirLargeur()/2 + 8)/donnees.obtenirZoom(), (y - donnees.obtenirHauteur()/2 + 20)/donnees.obtenirZoom())) {
                                if (donnees.obtenirDerniereCellule() == cellules[i][j]) { // On compare les pointeurs (références) des 2 objets
                                    //cellules[i][j].majSourisDessus(false);
                                    donnees.majDerniereCellule(null);
                                } else {
                                    //System.out.println("Case "+i+" "+j);
                                    /*
                                    cellules[i][j].majSourisDessus(true);
                                    if (donnees.obtenirDerniereCellule() != null)
                                        donnees.obtenirDerniereCellule().majSourisDessus(false);
                                    */
                                    
                                    if (donnees.getScene().equals("Jeu")) {  // Calcul et transmission de la trajectoire
                                        final int[] INDEX_ROBOT = donnees.obtenirJoueur().obtenirCase(); // Ligne et colonne du centre de la case sur lequel se trouve le joueur
                                        final int[] COORDS_ROBOT = cellules[INDEX_ROBOT[0]][INDEX_ROBOT[1]].obtenirCentre(); // Coordonnées du centre de la case sur lequel se trouve le joueur

                                        if (i == INDEX_ROBOT[0] && j == INDEX_ROBOT[1]) {
                                            donnees.obtenirJoueur().definirBut(new LinkedList<int[]>());
                                            donnees.notifierObserveur(TypeMisAJour.Joueur);
                                        } else {
                                            
                                            // On vérifie que la cellule cliquée fait bien partie des voisins dans le rayon de séléction autorisé
                                            Cellule[] casesVoisines = Voisins.obtenirVoisins(cellules, INDEX_ROBOT[0], INDEX_ROBOT[1], Options.RAYON_JOUEUR);
                                            int index = 0;
                                            while (index < casesVoisines.length && casesVoisines[index] != cellules[i][j])
                                                index ++;
                                            if (index < casesVoisines.length) { // Si la case fait partie des voisins, alors elle est bien dans le rayon de séléction autorisé

                                                final int[] INDEX_CELLULE = cellules[i][j].obtenirPositionTableau();
                                                final int[] COORDS_CELLULE = cellules[i][j].obtenirCentre();
                                                LinkedList<int[]> listeBut = new LinkedList<int[]>();
                                                HashMap<Cellule, Double> distances = new HashMap<Cellule, Double>();
                                                Cellule celluleOptimale = null;

                                                int[] centreVoisins = INDEX_ROBOT;

                                                while( listeBut.isEmpty() || !(INDEX_CELLULE[0] == celluleOptimale.obtenirPositionTableau()[0] && INDEX_CELLULE[1] == celluleOptimale.obtenirPositionTableau()[1]) ) { // On réitère le processecus pour chaque déplacement jusqu'à l'arrivée
                                                    
                                                    casesVoisines = Voisins.obtenirVoisins(cellules, centreVoisins[0], centreVoisins[1], 2);
                                                
                                                    int k = 0;
                                                    distances.clear();
                                                    while (k < casesVoisines.length){
                                                        if (casesVoisines[k] != cellules[centreVoisins[0]][centreVoisins[1]]) {
                                                            System.out.println(casesVoisines[k].obtenirPositionTableau()[0]+","+casesVoisines[k].obtenirPositionTableau()[1]);
                                                            distances.put(casesVoisines[k], Math.pow((double)(COORDS_CELLULE[0]-casesVoisines[k].obtenirCentre()[0]),2)+Math.pow((double)(COORDS_CELLULE[1]-casesVoisines[k].obtenirCentre()[1]),2)); // Distance au carré
                                                        }
                                                        k++;
                                                    }

                                                    // Déplacement sur la cellule où la distance restante est optimale = but1
                                                    double min = Collections.min(distances.values());
                                                    
                                                    for (Entry<Cellule, Double> entry : distances.entrySet()) {
                                                        if (entry.getValue()==min) {
                                                            celluleOptimale = entry.getKey();
                                                            break;
                                                        }
                                                    }

                                                    final int[] INDEX_CASE_OPTIMALE = celluleOptimale.obtenirPositionTableau();

                                                    System.out.println("Centre Robot: "+INDEX_ROBOT[0]+","+INDEX_ROBOT[1]);
                                                    System.out.println("Case à atteindre: "+INDEX_CELLULE[0]+","+INDEX_CELLULE[1]);
                                                    System.out.println("Case atteinte: "+INDEX_CASE_OPTIMALE[0]+","+INDEX_CASE_OPTIMALE[1]);
                                                    
                                                    final int[] DELTA_POSITION_JOUEUR = donnees.obtenirJoueur().obtenirCoordonnees();
                                                    int[] deltaPosition = {celluleOptimale.obtenirCentre()[0] + DELTA_POSITION_JOUEUR[0], celluleOptimale.obtenirCentre()[1] + DELTA_POSITION_JOUEUR[1], INDEX_CASE_OPTIMALE[0], INDEX_CASE_OPTIMALE[1]};
                                                    listeBut.add(deltaPosition);
                                                    centreVoisins = celluleOptimale.obtenirPositionTableau();
                                                
                                                }
                                                donnees.obtenirJoueur().definirBut(listeBut);
                                                donnees.notifierObserveur(TypeMisAJour.Joueur);
                                            }
                                        }
                                        
                                        
                                        // Si on a une compétence de sélectionnée ET que le robot n'est pas en train de se déplacer
                                        if (donnees.obtenirRayonDeSelection() > 0 && donnees.obtenirJoueur().obtenirTrajectoire().isEmpty()) {
                                            // Si la case est dans le rayon d'action de la compétence:
                                            Cellule[] voisins = Voisins.obtenirVoisins(cellules, INDEX_ROBOT[0], INDEX_ROBOT[1], donnees.obtenirRayonDeSelection());
                                            int index = 0;
                                            while (index < voisins.length && voisins[index] != donnees.obtenirCellules()[i][j])
                                                index ++;
                                            if (index < voisins.length) {
                                                switch (donnees.obtenirDerniereCompetence().obtenirEffet()) {
                                                    case "Drone":
                                                        extraire(i, j);
                                                        break;
                                                    case "Scanner":
                                                        scan(i, j);
                                                        break;
                                                    default:
                                                        break;
                                                }
                                            }

                                            // On désélectionne la compétence
                                            donnees.obtenirDerniereCompetence().majSourisDessus(false);
                                            donnees.majDerniereCompetence(null);
                                            if (donnees.obtenirRayonDeSelection() != 0) {
                                                donnees.majRayonDeSelection(0);
                                                donnees.notifierObserveur(TypeMisAJour.RayonDeSelection);
                                            }
                                        }

                                    } else { // Modification de la carte selon les outils d'édition sélectionnés
                                        
                                        // Si on a sélectionné le pinceau
                                        // Selon la taille du pinceau:
                                        // Taille 1: on ne modifie que la case sélectionnée
                                        // Taille 2: on modifie les 6 cases autour ET la case sélectionnée
                                        // Taille 3: on modifie les 6 + 12 cases autour ET la case sélectionnée
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
            }

            // Gestion du menu de l'éditeur de carte
            // Si on est dans l'éditeur de carte, et qu'on a cliqué sur le menu
            if (donnees.getScene() != null && donnees.getScene().equals("Editeur de carte") && x >= COIN_GAUCHE_MENU) {
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
    }

    public void editer(String nomCarte, InputStream carte) {

        donnees.majNomCarte(nomCarte);
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
        
        donnees.majCellules(CSV.lecture(carte, -donnees.obtenirLargeur()/2, -donnees.obtenirHauteur()/2));
        donnees.majBoutonsCercle(boutonsCercle);
        donnees.majBoutonsType(boutonsType);
        donnees.majScene("Editeur de carte");

        donnees.obtenirArrierePlan().majCoords(-donnees.obtenirLargeur()/2, -donnees.obtenirHauteur()/2);

        donnees.notifierObserveur(TypeMisAJour.Scene);
        donnees.notifierObserveur(TypeMisAJour.ArrierePlan);
        donnees.notifierObserveur(TypeMisAJour.Cellules);
        donnees.notifierObserveur(TypeMisAJour.BoutonsCercle);
        donnees.notifierObserveur(TypeMisAJour.BoutonsType);
        donnees.notifierObserveur(TypeMisAJour.Peindre);
    }

    public void interactionClavier(int code) {
        if (donnees.getScene() != null) {
            if (donnees.getScene().equals("Editeur de carte")) {
                switch (code) {
                    case KeyEvent.VK_UP:
                        bougerEcran(0, +1);
                        break;
                    case KeyEvent.VK_DOWN:
                        bougerEcran(0, -1);
                        break;
                    case KeyEvent.VK_LEFT:
                        bougerEcran(+1, 0);
                        break;
                    case KeyEvent.VK_RIGHT :
                        bougerEcran(-1, 0);
                        break;
                }
            } else if (donnees.getScene().equals("Jeu")) {
                switch (code) {
                    case KeyEvent.VK_ESCAPE :
                        donnees.majEtatOptions(!donnees.obtenirEtatOptions());

                        if (!donnees.obtenirEtatOptions() && donnees.obtenirEtatMiniJeuLaser()) {// Si on sort des options et qu'on joue au mini-jeu laser
                            if (System.currentTimeMillis() - donnees.obtenirChronometreMinijeuLaser() < donnees.obtenirTempsAvantChrono()) {
                                donnees.majEffet("swoosh");
                                donnees.majChronometreMinijeuLaser(System.currentTimeMillis()); // On réinitialise le chronomètre
                                donnees.notifierObserveur(TypeMisAJour.NombreErreursLaser);
                            }
                        }

                        donnees.notifierObserveur(TypeMisAJour.Options);
                        break;
                }
            }
        }
    }
    
    private void bougerEcran(int dx, int dy) {

        donnees.obtenirArrierePlan().translate((int)(dx*Options.INCREMENT_DE_DEPLACEMENT/donnees.obtenirZoom()), (int)(dy*Options.INCREMENT_DE_DEPLACEMENT/donnees.obtenirZoom()));

        Cellule[][] cellules = donnees.obtenirCellules();
        for (int i=0; i < cellules.length; i++) {
            for (int j=0; j < cellules[i].length; j++)
                cellules[i][j].translate((int)(dx*Options.INCREMENT_DE_DEPLACEMENT/donnees.obtenirZoom()), (int)(dy*Options.INCREMENT_DE_DEPLACEMENT/donnees.obtenirZoom()));
        }

        donnees.notifierObserveur(TypeMisAJour.ArrierePlan);
        donnees.notifierObserveur(TypeMisAJour.Cellules);
        donnees.notifierObserveur(TypeMisAJour.Peindre);
    }

    public void ajusterZoom(int notches, Point point) {
        // convert target coordinates to zoomTarget coordinates
        if (notches < 1) {
            double tmpZoom = donnees.obtenirZoom()/Options.MULTIPLICATEUR_ZOOM;
            if (donnees.getScene() != null) {
                if (donnees.getScene().equals("Editeur de carte")) {
                    if (tmpZoom < 0.01)
                        tmpZoom = 0.01; 
                } else if (donnees.getScene().equals("Jeu")) {
                    if (tmpZoom < 0.47)
                        tmpZoom = 0.47;
                }
                donnees.majZoom(tmpZoom);
            }
        } else
            donnees.majZoom(donnees.obtenirZoom()*Options.MULTIPLICATEUR_ZOOM);
        donnees.majCentreZoom(point);
        donnees.notifierObserveur(TypeMisAJour.CentreZoom);
        donnees.notifierObserveur(TypeMisAJour.Zoom);
        donnees.notifierObserveur(TypeMisAJour.Peindre);
    }

    public void majStatutSouris(MouseEvent ev, boolean clic) {
        // Si on est dans l'éditeur de carte
        if (donnees.getScene() == "Editeur de carte") {
            // Si on arrête de cliquer
            if (!clic)
                donnees.majStatutSouris(ev, clic);
            
                // Si on clique sur la carte (pas sur le menu)
            else if (ev.getX() < donnees.obtenirLargeur()*(Options.RATIO_LARGEUR_MENU-1)/Options.RATIO_LARGEUR_MENU)
                donnees.majStatutSouris(ev, clic);
            
            // Si on clique sur le menu (pas sur la carte)
            else
                click(ev.getX(),ev.getY());
        } else if (clic)
            click(ev.getX(),ev.getY());
            
    }

    public void majStatutSouris(MouseEvent ev) {
        donnees.majStatutSouris(ev);
    }

    public void majPositionFenetre(Point location) {
        donnees.majPositionFenetre(location);
    }

    // MUSIQUE
    public void majVolumeEffets(int volumeEffets) {
        donnees.majVolumeEffets( volumeEffets );
    }
    public void majVolumeMusique(int volumeMusique) {
        donnees.majVolumeMusique( volumeMusique );
    }
    public void majEtatMusique(boolean musiqueEtat) {
        donnees.majEtatMusique( musiqueEtat );
    }
    public void majEtatEffets(boolean etatEffets) {
        donnees.majEtatEffets( etatEffets );
    }
    public void majMusique(String musique) {
        if (donnees.obtenirEtatMusique()) {
            donnees.majMusique(musique);
        }
    }
    public void majMusique(int indexMusique) {
        if (donnees.obtenirEtatMusique()) {
            donnees.majMusique(indexMusique);
        }
    }
    public void boucleMusique() {
        if (donnees.obtenirEtatMusique()) {
            donnees.boucleMusique();
        }
    }
    public void jouerEffet(String effet) {
        if (donnees.obtenirEtatEffets()) {
            donnees.majEffet(effet);
        }
    }
    public void jouerEffet(int indexEffet) {
        if (donnees.obtenirEtatEffets()) {
            donnees.majEffet(indexEffet);
        }
    }
    public void musiqueSuivante() {
        donnees.musiqueSuivante();
        donnees.boucleMusique();
    }
    public Donnees getDonnees(){
        return this.donnees;
    }

    public void enregistrer() {
        try {
            System.out.println("Enregistrement de "+donnees.obtenirNomCarte()+".csv");
            CSV.givenDataArray_whenConvertToCSV_thenOutputCreated(donnees.obtenirCellules(), donnees.obtenirNomCarte(), true);
            chargerCartes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void retourMenu() {
        donnees.majScene("Choix du mode");
        donnees.notifierObserveur(TypeMisAJour.Scene);
        chargerCartes();
    }

    private void chargerCartes() {
        // Chargement des cartes
        try {
            Pattern pattern = Pattern.compile("^.*\\b"+Options.NOM_DOSSIER_CARTES+"\\b.*\\.(?:csv)");
            donnees.majCartes(ObtenirRessources.getStreamsAndFilenames(pattern, Options.NOM_DOSSIER_CARTES));
            donnees.notifierObserveur(TypeMisAJour.Cartes);
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    public void majPositionCurseurExtraction(double positionCurseurExtraction) {
        donnees.majPositionCurseurExtraction(positionCurseurExtraction);
    }
    
    public Image obtenirImageSymbole(TypeSymbole nomSymbole){
		switch( nomSymbole){
			case MINERAI:
				return donnees.getImagesSymboles().get("MINERAI");
			case BACTERIE:
				return donnees.getImagesSymboles().get("BACTERIE");
			case GRAPPIN:
				return donnees.getImagesSymboles().get("GRAPPIN");
			case SCANNER:
				return donnees.getImagesSymboles().get("SCANNER");
			case RAVIN:
				return donnees.getImagesSymboles().get("RAVIN");
			case INCONNUE:
				return donnees.getImagesSymboles().get("INCONNUE");
			default:
				return null;
		
		}
	
	}

    public void majTempsDeReaction(long tempsDeReaction) {
        donnees.majTempsDeReaction(tempsDeReaction);
    }
    public void majScoreExtraction(String scoreExtraction) {
        donnees.majScoreExtraction(scoreExtraction);
    }
}
