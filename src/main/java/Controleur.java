
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ByteArrayInputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.sound.sampled.AudioInputStream;
import javax.swing.SwingWorker;

public class Controleur {
    
    public Donnees donnees;

    public Controleur(Donnees donnees) {
        this.donnees = donnees;
    }

    private boolean aucunJeuEnCours() {
        return (donnees.obtenirEtatMiniJeuExtraction().equals(Etat.OFF) && donnees.obtenirEtatMiniJeuLaser().equals(Etat.OFF));
    }
    // Effet quand on utilise le bras robot sur une case qui contient un symbole (use les bras)
    private void extraire(int ligne, int colonne) {
        Cellule c = donnees.obtenirCellules()[ligne][colonne]; // Cellule sélectionnée
        // On ne peut extraire que du bois et des ponts si la case et le sybmole sont visibles
        if (c.estDecouverte() && c.obtenirSymbole().obtenirEstVisible()
            && (c.obtenirSymbole().type == TypeSymbole.BOIS ||
                c.obtenirSymbole().type == TypeSymbole.PONT ||
                c.obtenirSymbole().type == TypeSymbole.BACTERIE ||
                c.obtenirSymbole().type == TypeSymbole.MINERAI ||
                c.obtenirSymbole().type == TypeSymbole.CHENILLES)) {
            donnees.majDerniereCelluleMinijeu(c);
            donnees.majSensVariationExtraction(true);
            donnees.majPositionCurseurExtraction(0);
            donnees.majEtatMinijeuExtraction(Etat.IN);
            donnees.notifierObservateur(TypeMisAJour.SensVariationExtraction);
            donnees.notifierObservateur(TypeMisAJour.PositionCurseurExtraction);
            donnees.notifierObservateur(TypeMisAJour.MinijeuExtraction);
            donnees.obtenirJoueur().usureBras();
        } else {
            notifierJoueur("On ne peut que extraire les\nsymboles de bois, ponts, bactéries,\nminerais et chenilles !");
            desactiverCompetence();
        }
    }

    // Effet quand on utilise le scanner sur une case qui contient un symbole (une les capteurs)
    private void scan(int ligne, int colonne) {
        Cellule c = donnees.obtenirCellules()[ligne][colonne]; // Cellule sélectionnée
        // On ne peut scanner que les cases qui ont un symbole non visible sur une case visibele
        if (c.estDecouverte() && !c.obtenirSymbole().obtenirEstVisible() && c.obtenirSymbole().type != null && c.obtenirSymbole().type != TypeSymbole.VIDE) {
            donnees.majDerniereCelluleMinijeu(c);
            donnees.majRepereMinijeuLaser(System.currentTimeMillis());
            donnees.majTempsAvantChrono((int)(Math.random()*((7000-3000)+1)+3000));
            donnees.majChronometreMinijeuLaser("0.0000 secondes"); // On met à jour l'affichage du temps
            donnees.majNombreErreursLaser(0);
            donnees.majEtatMinijeuLaser(Etat.ON);
            donnees.notifierObservateur(TypeMisAJour.NombreErreursLaser);
            donnees.notifierObservateur(TypeMisAJour.ChronometreLaser);
            donnees.notifierObservateur(TypeMisAJour.MinijeuLaser);
            donnees.obtenirJoueur().usureCapteurs();
        } else {
            notifierJoueur("On ne peut que scanner les cases avec\nun symbole de point d'interrogation !");
            desactiverCompetence();
        }
    }

    private void notifierJoueur(String notification) {
        jouerEffet("notification");
        donnees.majInformerJoueur(notification);
        donnees.majChronometreNotification(System.currentTimeMillis());
        donnees.notifierObservateur(TypeMisAJour.InformerJoueur);
    }

    // Effet quand on marche sur une case
    private void effetCase(int[] coordonnees) {
        if (coordonnees != null) {
            TypeCase typeActif = donnees.obtenirCellules()[coordonnees[0]][coordonnees[1]].obtenirType();
            Symbole symboleActif = donnees.obtenirCellules()[coordonnees[0]][coordonnees[1]].obtenirSymbole();
            boolean aChangeDeCase = (donnees.obtenirJoueur().obtenirCase()[0] != donnees.obtenirJoueur().obtenirDerniereCase()[0] && donnees.obtenirJoueur().obtenirCase()[1] != donnees.obtenirJoueur().obtenirDerniereCase()[1]);
            switch(typeActif){
                case VIDE:
                    break;
                case EAU:
                    if(aChangeDeCase)donnees.obtenirJoueur().malusBatterie(TypeCase.EAU);
                    break;
                case MONTAGNE:
                    break;
                case DESERT:
                    break;
                case SABLE_MOUVANTS:
                    if(aChangeDeCase)donnees.obtenirJoueur().malusBatterie(TypeCase.SABLE_MOUVANTS);
                    break;
                case NEIGE:
                    if(aChangeDeCase)donnees.obtenirJoueur().malusBatterie(TypeCase.NEIGE);
                    break;
                case FORET:
                    break;
                default :
                    break;
            }
            TypeSymbole s = symboleActif.type;
            if(symboleActif != null && symboleActif.estVisible && (s==TypeSymbole.JAMBE || s==TypeSymbole.BRAS || s==TypeSymbole.CAPTEUR || s==TypeSymbole.ENERGIE)){
                 donnees.obtenirJoueur().maintenance(s);
                 donnees.obtenirJoueur().actualiseVP();
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
	public void jouer(String nomCarte, List<String[]> carte) {
        donnees.majNomCarte(nomCarte);
        Reception jeu = CSV.lecture(carte, -donnees.obtenirLargeur()/2, -donnees.obtenirHauteur()/2, Donnees.imagesSymboles, donnees.getImagesJoueur());
        donnees.majCellules(jeu.getCellule());
        donnees.majJoueur(jeu.getJoueur());
        jeu.getJoueur().majDimensionsCarte(jeu.getCellule().length, jeu.getCellule()[0].length);

        int nbCasesExplorees = 0;
        for (int i=0; i < jeu.getCellule().length; i++) {
            for (Cellule c: jeu.getCellule()[i]) {
                if (c.estDecouverte())
                    nbCasesExplorees++;
            }
        }
        jeu.getJoueur().majCasesExplorees(nbCasesExplorees);
        placerJoueur(jeu.getJoueur().obtenirCase()[0], jeu.getJoueur().obtenirCase()[1]);

        //Compétences
        final int X_INIT = 100;
        final int Y_INIT = 200;
        final int DISTANCE_INTER_COMPETENCE = 120;
        List<BoutonCercle> competences = new LinkedList<BoutonCercle>();
        competences.add(new BoutonCercle(X_INIT, Y_INIT, 50, "Grappin", Donnees.imagesSymboles.get(TypeSymbole.GRAPPIN.name()), true));
        competences.add(new BoutonCercle(X_INIT, Y_INIT+DISTANCE_INTER_COMPETENCE, 50, "Scanner", Donnees.imagesSymboles.get(TypeSymbole.SCANNER.name()), true));
        // La compétence "Pont" est disponible si le joueur au moins un pont
        competences.add(new BoutonCercle(X_INIT, Y_INIT+2*DISTANCE_INTER_COMPETENCE, 50, "Pont", Donnees.imagesSymboles.get(TypeSymbole.PONT.name()), jeu.getJoueur().obtenirNombrePont() > 0));
        donnees.majCompetences(competences);

        donnees.obtenirArrierePlan().majCoords(-donnees.obtenirLargeur()/2, -donnees.obtenirHauteur()/2);
        donnees.majScene("Jeu");
        
        // Découverte des cellules autour du joueur
        Cellule[] voisins = Voisins.obtenirVoisins(donnees.obtenirCellules(), donnees.obtenirJoueur().obtenirDerniereCase()[0], donnees.obtenirJoueur().obtenirDerniereCase()[1], Options.RAYON_DECOUVERTE);
        for (int i=0; i < voisins.length; i++) {
            if (!voisins[i].estDecouverte()){
                voisins[i].majEstDecouverte(true);
                donnees.obtenirJoueur().majCasesExplorees();
            }
        }
        if (donnees.obtenirJoueur().obtenirPExploration() >= 70) {
            donnees.majEtatExploration(true);
            rafraichirVictoire();
        }

        donnees.notifierObservateur(TypeMisAJour.Scene);
        donnees.notifierObservateur(TypeMisAJour.Joueur);   // On transmet une unique fois la référence à l'objet Joueur (la valeur de l'objet joueur est un pointeur)
        donnees.notifierObservateur(TypeMisAJour.Cellules); // On transmet une unique fois la référence du tableau de cellules
        donnees.notifierObservateur(TypeMisAJour.Competences); // On transmet une unique fois la référence du tableau de cellules
        donnees.notifierObservateur(TypeMisAJour.ArrierePlan);
	}

    private void rafraichirVictoire() {
        if (!donnees.obtenirVictoire()) {
            if ( donnees.obtenirSymbolesDecouverts().get(TypeSymbole.BACTERIE) && donnees.obtenirSymbolesDecouverts().get(TypeSymbole.MINERAI) && donnees.obtenirEtatExploration()) {
                jouerEffet("win_game");
                donnees.majVictoire(true);
                donnees.majEtatMinijeuExtraction(Etat.OFF);
                donnees.majEtatMinijeuLaser(Etat.OFF);
                donnees.notifierObservateur(TypeMisAJour.MinijeuExtraction);
                donnees.notifierObservateur(TypeMisAJour.MinijeuLaser);
                donnees.notifierObservateur(TypeMisAJour.Victoire);
            } else {
                jouerEffet("achievement");
            }
        }
    }

	public void rafraichir() {
        // Si le joueur vient de perdre, on affiche le panneau de défaite
        if (donnees.getScene().equals("Jeu") && !donnees.obtenirDefaite()) {
            if (donnees.obtenirJoueur().isDead()) {
                jouerEffet("loose_game");
                donnees.majDefaite(true);
                donnees.majEtatMinijeuExtraction(Etat.OFF);
                donnees.majEtatMinijeuLaser(Etat.OFF);
                donnees.notifierObservateur(TypeMisAJour.MinijeuExtraction);
                donnees.notifierObservateur(TypeMisAJour.MinijeuLaser);
                donnees.notifierObservateur(TypeMisAJour.Defaite);
                donnees.notifierObservateur(TypeMisAJour.Peindre);
            }
        }
        if (!donnees.obtenirEtatOptions() && !donnees.obtenirDefaite()) {
            // Si on est dans le jeu ou dans la carte de l'éditeur de carte (pas dans le menu)
            // On maintient le clic
            if (donnees.getScene().equals("Jeu") || (donnees.getScene().equals("Editeur de carte") && donnees.obtenirStatutSouris().obtenirX() < donnees.obtenirLargeur()*(Options.RATIO_LARGEUR_MENU-1)/Options.RATIO_LARGEUR_MENU)) {
                if (donnees.obtenirStatutSouris().obtenirClicGauche())
                    click(donnees.obtenirStatutSouris().obtenirX(), donnees.obtenirStatutSouris().obtenirY(), false);
            }
            if (donnees.getScene().equals("Jeu")) { // Si on est en jeu

                /* Recharge au fur et à mesure
                Cellule caseJoueur = donnees.obtenirCellules()[donnees.obtenirJoueur().obtenirDerniereCase()[0]][donnees.obtenirJoueur().obtenirDerniereCase()[1]];
                if (caseJoueur.obtenirSymbole().type == TypeSymbole.ENERGIE && caseJoueur.obtenirSymbole().obtenirEstVisible()) {
                    donnees.obtenirJoueur().maintenance(caseJoueur.obtenirSymbole().type);
                    donnees.obtenirJoueur().actualiseVP();
                }
                */

                // Si on est en train d'afficher une notification au joueur
                // Et qu'il est temps de supprimer la notification:
                if (!donnees.obtenirInformerJoueur().equals("")
                    && System.currentTimeMillis() - donnees.obteniChronometreNotification() > Options.DUREE_NOTIFICATION) {
                    donnees.majInformerJoueur("");
                    donnees.notifierObservateur(TypeMisAJour.InformerJoueur);
                }
                
                switch (donnees.obtenirEtatMiniJeuExtraction()) {
                    case ON:
                        break;
                    case IN: // On fait varier la position du curseur entre 0 et 1
                        
                        if (donnees.obtenirSensVariationExtraction()) { //On déplace le curseur vers la droite
                            donnees.majPositionCurseurExtraction(donnees.obtenirPositionCurseurExtraction() + Options.INCREMENT_CURSEUR);
                            if (donnees.obtenirPositionCurseurExtraction() > 1) {
                                donnees.majPositionCurseurExtraction(1);
                                donnees.majSensVariationExtraction(false);
                                donnees.notifierObservateur(TypeMisAJour.SensVariationExtraction);
                            }
                        } else { // On déplace le curseur vers la gauche
                            donnees.majPositionCurseurExtraction(donnees.obtenirPositionCurseurExtraction() - Options.INCREMENT_CURSEUR);
                            if (donnees.obtenirPositionCurseurExtraction() < 0) {
                                donnees.majPositionCurseurExtraction(0);
                                donnees.majSensVariationExtraction(true);
                                donnees.notifierObservateur(TypeMisAJour.SensVariationExtraction);
                            }
                        }
                        donnees.notifierObservateur(TypeMisAJour.PositionCurseurExtraction);
                        break;
                    case OUT: // Si on est en train d'afficher le score
                        if (System.currentTimeMillis() - donnees.obtenirChronometreSuppresion() > Options.TEMPS_AVANT_SUPPRESSION_MINIJEU) { // Si on doit fermer la fenêtre du minijeu
                            donnees.majEtatMinijeuExtraction(Etat.OFF);
                            donnees.notifierObservateur(TypeMisAJour.MinijeuExtraction);
                            desactiverCompetence();
                        }
                        break;
                    case OFF:
                        break;
                }

                switch (donnees.obtenirEtatMiniJeuLaser()) {
                    case ON:
                        
                        // Si il est temps de démarrer le mini-jeu
                        if (System.currentTimeMillis() - donnees.obtenirRepereMinijeuLaser() > donnees.obtenirTempsAvantChrono()) {
                            donnees.majEtatMinijeuLaser(Etat.IN);
                            donnees.majRepereMinijeuLaser(System.currentTimeMillis()); // On met à jour le repère de temps
                            donnees.notifierObservateur(TypeMisAJour.MinijeuLaser);
                        }
                        // On met à jour l'affichage du temps
                        donnees.majChronometreMinijeuLaser(new DecimalFormat("0.0000").format((System.currentTimeMillis() - donnees.obtenirRepereMinijeuLaser()) / 1000.) + " secondes");
                        donnees.notifierObservateur(TypeMisAJour.ChronometreLaser);
                        break;
                    case IN:
                        // On arrondie le temps (en seconde) au millième
                        donnees.majChronometreMinijeuLaser(new DecimalFormat("0.0000").format((System.currentTimeMillis() - donnees.obtenirRepereMinijeuLaser()) / 1000.) + " secondes");
                        donnees.notifierObservateur(TypeMisAJour.ChronometreLaser);
                        break;
                    case OUT:
                        if (System.currentTimeMillis() - donnees.obtenirChronometreSuppresion() > Options.TEMPS_AVANT_SUPPRESSION_MINIJEU) {
                            donnees.majEtatMinijeuLaser(Etat.OFF);
                            donnees.notifierObservateur(TypeMisAJour.MinijeuLaser);
                            desactiverCompetence();
                        }
                        break;
                    case OFF:
                        break;
                }
                donnees.obtenirJoueur().move();

                // Si on a changé de case
                if (donnees.obtenirJoueur().obtenirCasePrecedente()[0] != donnees.obtenirJoueur().obtenirCase()[0] || donnees.obtenirJoueur().obtenirCasePrecedente()[1] != donnees.obtenirJoueur().obtenirCase()[1]) {
                    String fichierAudio = "footstep_grass_00"+(int)(Math.random()*5);
                    jouerEffet(fichierAudio);
                    effetCase(donnees.obtenirJoueur().obtenirDerniereCase());
                    Cellule[] voisins = Voisins.obtenirVoisins(donnees.obtenirCellules(), donnees.obtenirJoueur().obtenirDerniereCase()[0], donnees.obtenirJoueur().obtenirDerniereCase()[1], Options.RAYON_DECOUVERTE);
                    for (int i=0; i < voisins.length; i++) {
                        if (!voisins[i].estDecouverte()) {
                            voisins[i].majEstDecouverte(true);
                            donnees.obtenirJoueur().majCasesExplorees();
                        }
                    }
                    if (!donnees.obtenirEtatExploration() && donnees.obtenirJoueur().obtenirPExploration() >= 70) {
                        donnees.majEtatExploration(true);
                        rafraichirVictoire();
                    }
                }
                    

                donnees.obtenirJoueur().rafraichirImage();
                int dx = -donnees.obtenirJoueur().getDx();
                int dy = -donnees.obtenirJoueur().getDy();
                if (dx != 0 || dy != 0) {
                    donnees.obtenirArrierePlan().translate(dx, dy);
                    for (int i=0; i < donnees.obtenirCellules().length; i++) {
                        for (int j=0; j < donnees.obtenirCellules()[i].length; j++)
                            donnees.obtenirCellules()[i][j].translate(dx, dy);
                    }
                    donnees.notifierObservateur(TypeMisAJour.ArrierePlan);
                }
                donnees.notifierObservateur(TypeMisAJour.Peindre);
            }
        }
	}
    
    public void charger() {

        // Chargement des cartes
        Pattern pattern = Pattern.compile("^.*\\b"+Options.NOM_DOSSIER_CARTES+"\\b.*\\.(?:csv)");
        try {
            HashMap<String, InputStream> cartes = ObtenirRessources.getStreamsAndFilenames(pattern, Options.NOM_DOSSIER_CARTES);
            HashMap<String, List<String[]>> cartesEnCache = new HashMap<String, List<String[]>>();

            for (Map.Entry<String, InputStream> paire: cartes.entrySet())
                cartesEnCache.put(paire.getKey(), CSV.cacheInputStream(paire.getValue()));
            
            donnees.majCartes(cartesEnCache);
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }

        // Ajout éventuel des cartes par défaut
        pattern = Pattern.compile("^.*\\b"+Options.NOM_DOSSIER_CARTES_PAR_DEFAUT+"\\b.*\\.(?:csv)");
        try {
            HashMap<String, InputStream> cartes = ObtenirRessources.getStreamsAndFilenames(pattern, "res/"+Options.NOM_DOSSIER_CARTES_PAR_DEFAUT+"/");
            HashMap<String, List<String[]>> cartesEnCache = new HashMap<String, List<String[]>>();
            for (Map.Entry<String, InputStream> paire: cartes.entrySet()) {
                if (!donnees.obtenirCartes().containsKey(paire.getKey())) {
                    donnees.obtenirCartes().put(paire.getKey(), CSV.cacheInputStream(paire.getValue()));
                    CSV.ecrireFichierDepuisCache(paire.getKey(), donnees.obtenirCartes().get(paire.getKey()), false);
                    cartesEnCache.put(paire.getKey(), donnees.obtenirCartes().get(paire.getKey()));
                }
                donnees.majCartesParDefaut(cartesEnCache);
            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }

        // Chargement des images et de l'arrière plan
        pattern = Pattern.compile("^.*\\b"+Options.NOM_DOSSIER_IMAGES+"\\b.*\\.(?:jpg|gif|png)");
        try {
            HashMap<String, Image> images = ObtenirRessources.getImagesAndFilenames(pattern, "res/"+Options.NOM_DOSSIER_IMAGES+"/");
            donnees.majArrierePlan(new ArrierePlan(images.get("surface_texture")));
            donnees.notifierObservateur(TypeMisAJour.ArrierePlan);
            images.put("planetes", TailleImage.resizeImage(images.get("planetes"), donnees.obtenirLargeur(), donnees.obtenirHauteur(), true));
            donnees.majImages(images);
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
        // Chargement des effets
        pattern = Pattern.compile("^.*\\b"+Options.NOM_DOSSIER_EFFETS+"\\b.*\\.(?:aifc|aiff|au|snd|wav)");
        try {
            HashMap<String, AudioInputStream> effets = ObtenirRessources.getAudioAndFilenames(pattern, "res/"+Options.NOM_DOSSIER_EFFETS+"/");
            donnees.majListeEffets(effets);
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }

        // Chargement des symboles
        pattern = Pattern.compile("^.*\\b"+Options.NOM_DOSSIER_SYMBOLE+"\\b.*\\.(?:jpg|gif|png)");
        final int LARGEUR = (int) (Options.LARGEUR_CASE*0.7);
        final int HAUTEUR = (int) (Options.LARGEUR_CASE*0.7);
        try {
            HashMap<String, Image> imagesSymboles = ObtenirRessources.getImagesAndFilenames(pattern, "res/"+Options.NOM_DOSSIER_SYMBOLE+"/");
            for (String i : imagesSymboles.keySet())
                imagesSymboles.put(i, TailleImage.resizeImage(imagesSymboles.get(i), LARGEUR, HAUTEUR, true));
            donnees.majImagesSymboles(imagesSymboles);
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }

        // Chargement des images du joueur=
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
                final List<Image> tmp = ObtenirRessources.getImagesAndFilenames(pattern, "res/robot/"+Options.JOUEURS_IMAGES[i]+"/").entrySet()
                                                                                                                                    .stream()
                                                                                                                                    .sorted(Map.Entry.<String,Image>comparingByKey())
                                                                                                                                    .map(Map.Entry::getValue)
                                                                                                                                    .collect(Collectors.toList());
                images.add(new ArrayList<Image>());
                for (Image img: tmp)
                    images.get(i).add( TailleImage.resizeImage( img, Options.JOUEUR_LARGEUR, (int)((double)Options.JOUEUR_LARGEUR*((double)img.getHeight(null)/(double)img.getWidth(null))), false) );                avancementChargement +=tmp.size();
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
            donnees.notifierObservateur(TypeMisAJour.Avancement);
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

                // Les variables statiques ont été envoyées
                // Elles sont constantes, et n'impliquent donc pas un couplage fort
                // entre l'affichage et les données

                // On peut alors initialiser l'Affichage
                donnees.notifierObservateur(TypeMisAJour.Initialisation);

                // On envoie aux objets crées lors de l'initialisation les données chargées
                donnees.notifierObservateur(TypeMisAJour.ImageMenu);
                donnees.notifierObservateur(TypeMisAJour.Cartes);
                donnees.notifierObservateur(TypeMisAJour.Scene);

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public void majScene(String scene) {
        donnees.majScene(scene);
    }

    public void click(int x, int y, boolean estDansMenu) {
        if (!donnees.obtenirEtatOptions() && !donnees.obtenirDefaite()) { // Si on est pas en pause et que le jeu n'est pas terminé
            boolean estModifie = false;

            if (!aucunJeuEnCours()) {
                estModifie = true;
                // Si le mini-jeu extraction est en cours
                if (donnees.obtenirEtatMiniJeuExtraction().equals(Etat.IN)) {
                    donnees.majChronometreSuppression(System.currentTimeMillis());
                    donnees.majEtatMinijeuExtraction(Etat.OUT);

                    // Pourcentage de précision final
                    double score = -200*Math.abs(0.5 - donnees.obtenirPositionCurseurExtraction()) + 100;
                    String resultat = new DecimalFormat("0.0").format(score)+" % de précision";
                    if (score < Options.PRECISION_MIN) {
                        resultat = "Echec ! "+resultat;
                        jouerEffet("loose");
                    } else {
                        resultat = "Réussite ! "+resultat;
                        jouerEffet("break");
                        if (donnees.obtenirDerniereCelluleMinijeu().obtenirSymbole().type == TypeSymbole.BACTERIE || donnees.obtenirDerniereCelluleMinijeu().obtenirSymbole().type == TypeSymbole.MINERAI) {
                            if (!donnees.obtenirSymbolesDecouverts().get(donnees.obtenirDerniereCelluleMinijeu().obtenirSymbole().type)) {
                                donnees.obtenirSymbolesDecouverts().put(donnees.obtenirDerniereCelluleMinijeu().obtenirSymbole().type, true); // On ajoute le symbole à la liste des symboles découverts
                                rafraichirVictoire();
                            }
                        } else {
                            jouerEffet("win");
                            donnees.obtenirSymbolesDecouverts().put(donnees.obtenirDerniereCelluleMinijeu().obtenirSymbole().type, true); // On ajoute le symbole à la liste des symboles découverts
                            switch (donnees.obtenirDerniereCelluleMinijeu().obtenirSymbole().type) {
                                case PONT: // Si on extrait 1 pont, c'est comme si on extrayait du bois
                                case BOIS:
                                    // On a extrait 1 de bois, donc on peut construire 1 pont
                                    int i = 0;
                                    while (i < donnees.obtenirCompetences().size() && !donnees.obtenirCompetences().get(i).obtenirEffet().equals("Pont")) {
                                        i++;
                                    }
                                    if (i < donnees.obtenirCompetences().size())
                                        donnees.obtenirCompetences().get(i).majDisponible(true);
                                    donnees.obtenirJoueur().majNombrePonts(donnees.obtenirJoueur().obtenirNombrePont()+1);
                                    break;
                                case CHENILLES:
                                    donnees.obtenirJoueur().majSurChenilles(true);
                                    break;
                                default:
                                    break;
                            }
                        }
                        donnees.obtenirDerniereCelluleMinijeu().obtenirSymbole().majSymbole(TypeSymbole.VIDE, null); // On enlève le symbole
                    }
                    donnees.majResultat(resultat);
                    donnees.notifierObservateur(TypeMisAJour.Resultat);
                    donnees.notifierObservateur(TypeMisAJour.MinijeuExtraction);
                }
                // Si le mini-jeu laser est en cours
                else if (donnees.obtenirEtatMiniJeuLaser().equals(Etat.IN)) {
                    donnees.majChronometreSuppression(System.currentTimeMillis());

                    // Mise à jour du temps de réaction final en MILLISECONDES
                    // on arrondie le temps au millième
                    double score = System.currentTimeMillis() - donnees.obtenirRepereMinijeuLaser();
                    String resultat = new DecimalFormat("0.0").format(score)+" millisecondes";
                    if (score > Options.TEMPS_DE_REACTION_MIN) {
                        resultat = "Echec ! "+resultat;
                        jouerEffet("loose");
                    } else {
                        resultat = "Réussite ! "+resultat;
                        jouerEffet("win");
                        donnees.obtenirDerniereCelluleMinijeu().obtenirSymbole().majEstVisible(true);
                    }
                    donnees.majChronometreMinijeuLaser(resultat);
                    donnees.majEtatMinijeuLaser(Etat.OUT);
                    donnees.notifierObservateur(TypeMisAJour.MinijeuLaser);
                    donnees.notifierObservateur(TypeMisAJour.ChronometreLaser);
                }
                // Si le mini-jeu laser est en lancement (mais pas en cours !)
                // Si on on clique avant le début, on reçoit une croix. Au bout de 3 croix, le joueur reçoit un malus
                else if (donnees.obtenirEtatMiniJeuLaser().equals(Etat.ON)) { 
                    if (System.currentTimeMillis() - donnees.obtenirRepereMinijeuLaser() < donnees.obtenirTempsAvantChrono()) {
                        donnees.majEffet("loose");
                        donnees.majRepereMinijeuLaser(System.currentTimeMillis()); // On réinitialise le repère
                        donnees.majChronometreMinijeuLaser("0.0000 secondes"); // On réinitalise l'affichage du temps écoulé
                        if (donnees.obtenirNombreErreursLaser() != Options.NOMBRE_ERREURS_LASER) { // Si on a pas déjà atteint le maximum de croix
                            donnees.majNombreErreursLaser(donnees.obtenirNombreErreursLaser()+1); // On ajoute une croix
                            donnees.notifierObservateur(TypeMisAJour.NombreErreursLaser);
                            if (donnees.obtenirNombreErreursLaser() == Options.NOMBRE_ERREURS_LASER) { // Si on atteint le maximum de croix
                                // MALUS
                                donnees.obtenirJoueur().malusBatterie(TypeCase.NEIGE);
                            }
                        }
                    }
                }
            } else {
            
                //Gestion de la carte

                // Si on est en jeu ou dans l'éditeur de carte, et qu'on a cliqué sur la carte
                if (donnees.getScene() != null && (donnees.getScene().equals("Jeu") || (donnees.getScene().equals("Editeur de carte") && !estDansMenu))) {
                    
                    // Gestion des compétences dans le jeu
                    boolean clicSurCompetence = false;
                    List<BoutonCercle> competences = donnees.obtenirCompetences();
                    for (BoutonCercle b: competences) {
                        if (b.contains(x, y)) {
                            clicSurCompetence = true;
                            // Si on a cliqué sur la compétence mais qu'on n'a pas de ponts en résèrve, on ne poursuit pas la requête
                            if (!(b.obtenirEffet().equals("Pont") && donnees.obtenirJoueur().obtenirNombrePont() < 1)) {
                                // Si on a déjà sélectionné la compétence, on la désélectionne
                                if (donnees.obtenirDerniereCompetence() == b) // On compare les pointeurs (références) des 2 objets
                                    desactiverCompetence();
                                else { // On sélectionne la compétence
                                    // On désélectionne l'ancienne compétence
                                    if (donnees.obtenirDerniereCompetence() != null)
                                        donnees.obtenirDerniereCompetence().majSourisDessus(false);
                                    // On sélectionne la nouvelle compétence
                                    donnees.majDerniereCompetence(b);
                                    b.majSourisDessus(true);
                                    if (donnees.obtenirRayonDeSelection() != 0) {
                                        donnees.majRayonDeSelection(0);
                                        donnees.notifierObservateur(TypeMisAJour.RayonDeSelection);
                                    }
                                    switch (b.obtenirEffet()) {
                                        case "Grappin":
                                            donnees.majRayonDeSelection(4);
                                            donnees.notifierObservateur(TypeMisAJour.RayonDeSelection);
                                            break;
                                        case "Scanner":
                                            donnees.majRayonDeSelection(2);
                                            donnees.notifierObservateur(TypeMisAJour.RayonDeSelection);
                                            break;
                                        case "Pont":
                                            donnees.majRayonDeSelection(2);
                                            donnees.notifierObservateur(TypeMisAJour.RayonDeSelection);
                                            break;
                                        case "Réparation":
                                            break;
                                    }
                                }
                            } else
                                notifierJoueur("Vous devez collecter des ponts pour en poser !");

                            estModifie = true;
                            donnees.notifierObservateur(TypeMisAJour.Competences);
                            break;
                        }
                    }

                    if (!clicSurCompetence) {
                        x -= donnees.obtenirLargeur()/2;
                        y -= donnees.obtenirHauteur()/2;

                        // Gestion des cellules
                        Cellule[][] cellules = donnees.obtenirCellules();
                        int[] indexRobot = new int[2];
                        if (donnees.obtenirJoueur() != null) 
                            indexRobot = donnees.obtenirJoueur().obtenirCase(); // Ligne et colonne du centre de la case sur lequel se trouve le joueur
                        for (int i=0; i < cellules.length; i++) {
                            for (int j=0; j < cellules[i].length; j++) {
                                if (cellules[i][j].contains(x/donnees.obtenirZoom(),y/donnees.obtenirZoom())) {
                                    estModifie = true;
                                    if (donnees.obtenirDerniereCellule() == cellules[i][j]) { // On compare les pointeurs (références) des 2 objets
                                        donnees.majDerniereCellule(null);
                                    } else {
                                        //System.out.println("Case "+i+" "+j);
                                        /*
                                        cellules[i][j].majSourisDessus(true);
                                        if (donnees.obtenirDerniereCellule() != null)
                                            donnees.obtenirDerniereCellule().majSourisDessus(false);
                                        */
                                        
                                        if (donnees.getScene().equals("Jeu")) {  // Calcul et transmission de la trajectoire
                                            // Si on a une compétence de sélectionnée ET que le robot n'est pas en train de se déplacer
                                            if (donnees.obtenirRayonDeSelection() > 0 && donnees.obtenirJoueur().obtenirTrajectoire().isEmpty()) {
                                                // Si la case est dans le rayon d'action de la compétence:
                                                Cellule[] voisins = Voisins.obtenirVoisins(cellules, indexRobot[0], indexRobot[1], donnees.obtenirRayonDeSelection());
                                                int index = 0;
                                                while (index < voisins.length && voisins[index] != donnees.obtenirCellules()[i][j])
                                                    index ++;
                                                if (index < voisins.length) {
                                                    switch (donnees.obtenirDerniereCompetence().obtenirEffet()) {
                                                        case "Grappin":
                                                            extraire(i, j);
                                                            break;
                                                        case "Scanner":
                                                            scan(i, j);
                                                            break;
                                                        case "Pont":
                                                            if (donnees.obtenirJoueur().obtenirNombrePont() > 0) {
                                                                // On ne peut placer des ponts que sur une case eau
                                                                if (donnees.obtenirCellules()[i][j].obtenirType().equals(TypeCase.EAU)) {
                                                                    jouerEffet("build");
                                                                    donnees.obtenirCellules()[i][j].obtenirSymbole().majSymbole(TypeSymbole.PONT, Donnees.imagesSymboles.get(TypeSymbole.PONT.name()));
                                                                    donnees.obtenirJoueur().majNombrePonts(donnees.obtenirJoueur().obtenirNombrePont()-1);
                                                                    if (donnees.obtenirJoueur().obtenirNombrePont() == 0)
                                                                        donnees.obtenirDerniereCompetence().majDisponible(false);
                                                                } else
                                                                    notifierJoueur("Un pont ne peut être posé que sur de l'eau !");
                                                                desactiverCompetence();
                                                            }
                                                            break;
                                                        default:
                                                            break;
                                                    }
                                                } else // Si on a pas cliqué sur une case dans le rayon de sélection, on déselectionne la compétence
                                                    desactiverCompetence();
                                            } else {
                                                
                                                if (i == indexRobot[0] && j == indexRobot[1]) {
                                                    donnees.obtenirJoueur().definirBut(new LinkedList<int[]>());
                                                } else {
                                                    
                                                    // Si le joueur n'a pas de chenilles, on l'empêche d'aller sur une case montagne
                                                    // Si il n'y a pas de pont sur une case eau, on empêche le jour d'y aller
                                                    if (cellules[i][j].obtenirType() == TypeCase.MONTAGNE && !donnees.obtenirJoueur().obtenirSurChenilles())
                                                        notifierJoueur("Impossible d'escalader\nIl faudrait des chenilles !");
                                                    else if (cellules[i][j].obtenirType() == TypeCase.EAU && cellules[i][j].obtenirSymbole().type != TypeSymbole.PONT)
                                                        notifierJoueur("Impossible de nager\nIl faudrait poser un pont !");
                                                    else {
                                                        // On vérifie que la cellule cliquée fait bien partie des voisins dans le rayon de sélection autorisé
                                                        Cellule[] casesVoisines = Voisins.obtenirVoisins(cellules, indexRobot[0], indexRobot[1], Options.RAYON_JOUEUR);
                                                        int index = 0;
                                                        while (index < casesVoisines.length && casesVoisines[index] != cellules[i][j])
                                                            index ++;
                                                        if (index < casesVoisines.length) { // Si la case fait partie des voisins, alors elle est bien dans le rayon de sélection autorisé

                                                            final int[] INDEX_CELLULE = cellules[i][j].obtenirPositionTableau();
                                                            final int[] COORDS_CELLULE = cellules[i][j].obtenirCentre();
                                                            LinkedList<int[]> listeBut = new LinkedList<int[]>();
                                                            HashMap<Cellule, Double> distances = new HashMap<Cellule, Double>();
                                                            Cellule celluleOptimale = null;

                                                            int[] centreVoisins = indexRobot;

                                                            int nombreIterations = 0;
                                                            // On vérifie qu'on a pas atteint un nombre maximal de tentatives de recherche d'une trajectoire vers un point (car un point peut ne pas être atteint)
                                                            // On arrête la boucle dans le cas où la dernière cellule du but est celle sur laquelle l'utilisateur a cliqué
                                                            // Si il n'y a pas de dernière cellule (liste de buts vide), on ne vérifie pas la correspondence entre la dernière cellule et celle cliquée
                                                            while( nombreIterations < Options.NOMBRE_MAX_ITERATIONS_TRAJECTOIRE && (listeBut.isEmpty() || !(INDEX_CELLULE[0] == celluleOptimale.obtenirPositionTableau()[0] && INDEX_CELLULE[1] == celluleOptimale.obtenirPositionTableau()[1])) ) { // On réitère le processecus pour chaque déplacement jusqu'à l'arrivée
                                                                nombreIterations++;
                                                                casesVoisines = Voisins.obtenirVoisins(cellules, centreVoisins[0], centreVoisins[1], 2);
                                                                
                                                                int k = 0;
                                                                distances.clear();
                                                                while (k < casesVoisines.length){
                                                                    if (casesVoisines[k] != cellules[centreVoisins[0]][centreVoisins[1]]) {
                                                                        // Si le joueur n'a pas de chenilles, on l'empêche d'aller sur une case montagne
                                                                        // Joueur a des chenilles = a
                                                                        // La case est une montagne = b
                                                                        // On ajoute la case ssi   !(!a&&b)
                                                                        //                          a||!b
                                                                        // Si la case est de l'eau et qu'il n'y a pas un pont dessus, on l'empêche d'aller sur la case
                                                                        // La case est de l'eau = a
                                                                        // Il y a un pont sur la case = b
                                                                        // On ajoute la case ssi !(a&&!b) càd !a||b
                                                                        if ((donnees.obtenirJoueur().obtenirSurChenilles() || casesVoisines[k].obtenirType() != TypeCase.MONTAGNE)
                                                                            && (casesVoisines[k].obtenirType() != TypeCase.EAU || casesVoisines[k].obtenirSymbole().type == TypeSymbole.PONT)) {
                                                                            distances.put(casesVoisines[k], Math.pow((double)(COORDS_CELLULE[0]-casesVoisines[k].obtenirCentre()[0]),2)+Math.pow((double)(COORDS_CELLULE[1]-casesVoisines[k].obtenirCentre()[1]),2)); // Distance au carré
                                                                        }
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
                                                                
                                                                final int[] DELTA_POSITION_JOUEUR = donnees.obtenirJoueur().obtenirCoordonnees();
                                                                int[] deltaPosition = {celluleOptimale.obtenirCentre()[0] + DELTA_POSITION_JOUEUR[0], celluleOptimale.obtenirCentre()[1] + DELTA_POSITION_JOUEUR[1], INDEX_CASE_OPTIMALE[0], INDEX_CASE_OPTIMALE[1]};
                                                                listeBut.add(deltaPosition);
                                                                centreVoisins = celluleOptimale.obtenirPositionTableau();
                                                            
                                                            }
                                                            donnees.obtenirJoueur().definirBut(listeBut);
                                                        }
                                                    }
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
                                                Cellule[] voisins = Voisins.obtenirVoisins(cellules, i, j, taillePinceau);
                                                for (Cellule c: voisins)
                                                    c.majType(type);
                                            }
                                            if (donnees.obtenirDerniereCaseSymbole() != null) {
                                                final Image image = donnees.obtenirDerniereCaseSymbole().obtenirSymbole().image;
                                                final TypeSymbole type = donnees.obtenirDerniereCaseSymbole().obtenirSymbole().type;
                                                
                                                int taillePinceau = 1;
                                                if (donnees.obtenirDernierBouton() != null)
                                                    taillePinceau = donnees.obtenirDernierBouton().obtenirTaillePinceau();
                                                
                                                if (type == TypeSymbole.FUSEE) { // Si on place le point d'apparition du joueur (symbole de fusée)
                                                    if (donnees.obtenirCelluleDepart() != null) // Si un point d'apparition a déjà été fixé
                                                        cellules[donnees.obtenirCelluleDepart()[0]][donnees.obtenirCelluleDepart()[1]].obtenirSymbole().majSymbole(TypeSymbole.VIDE, null); // On enlève le symbole de la case
                                                    taillePinceau = 1; // On place uniquement 1 symbole de point d'apparition
                                                    donnees.majCelluleDepart(new int[]{i,j});
                                                }
                                                
                                                Cellule[] voisins = Voisins.obtenirVoisins(cellules, i, j, taillePinceau);
                                                for (Cellule c: voisins) {
                                                    c.obtenirSymbole().majSymbole(type, image);
                                                    if (!c.obtenirSymbole().obtenirEstVisible())
                                                        c.obtenirSymbole().majEstVisible(true);
                                                }
                                            } 
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            // Gestion du menu de l'éditeur de carte
            // Si on est dans l'éditeur de carte, et qu'on a cliqué sur le menu
            if (donnees.getScene() != null && donnees.getScene().equals("Editeur de carte") && estDansMenu) {
                Cellule[] boutonsSymbole = donnees.obtenirBoutonsSymbole();
                for (int i=0; i < boutonsSymbole.length; i++) {
                    if (boutonsSymbole[i].contains(x,y)) {
                        if (donnees.obtenirDerniereCaseSymbole() == boutonsSymbole[i]) { // On compare les pointeurs (références) des 2 objets
                            boutonsSymbole[i].majSourisDessus(false);
                            donnees.majDerniereCaseSymbole(null);
                        } else {
                            boutonsSymbole[i].majSourisDessus(true);
                            if (donnees.obtenirDerniereCaseSymbole() != null)
                                donnees.obtenirDerniereCaseSymbole().majSourisDessus(false);
                            donnees.majDerniereCaseSymbole(boutonsSymbole[i]);
                        }
                        estModifie = true;
                        break;
                    }
                }
                
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
                        break;
                    }
                }
            }
            if (estModifie)
                donnees.notifierObservateur(TypeMisAJour.Peindre);
        }
    }

    public void editer(String nomCarte, List<String[]> carte) {

        donnees.majNomCarte(nomCarte);
        final int LARGEUR_MENU = (int)(donnees.obtenirLargeur()/Options.RATIO_LARGEUR_MENU);
        
        //BoutonsCercles
        final int NOMBRE_BOUTONS_CIRCULAIRES = 3;
        // Longueur totale des cercles: Suite arithmétique de raison 0.25*R
        // Un+1 = Un+INCREMENT_TAILLE*R où R est le rayon du bouton
        // Un = Up + (n-p)*r où r est la raison de la suite
        // U(n-1) = R + (n-1)*INCREMENT_TAILLE*R = R*(1 + (n-1)*INCREMENT_TAILLE)
        // Sn = 3 * (R + R*(1 + (n-1)*INCREMENT_TAILLE))/2 = 3R/2 *(2 + (n-1)*INCREMENT_TAILLE))
        
        final double INCREMENT_TAILLE = 0.25;
        // On multiplie par 2 la somme des rayons pour obtenir la longueur totale des boutons
        final int LONGUEUR_BOUTONS = (int) (2*(3*Options.RAYON_BOUTON_CERCLE/2 *(2 + (NOMBRE_BOUTONS_CIRCULAIRES-1)*INCREMENT_TAILLE)));
        final int LONGUEUR_ESPACEMENTS = (NOMBRE_BOUTONS_CIRCULAIRES-1)*Options.ESPACE_INTER_BOUTON;;
        final int LONGUEUR_TOTALE = LONGUEUR_ESPACEMENTS + LONGUEUR_BOUTONS;
        
        int rayonBouton = Options.RAYON_BOUTON_CERCLE;
        int longueurBoutons = 0;
        BoutonCercle[] boutonsCercle = new BoutonCercle[NOMBRE_BOUTONS_CIRCULAIRES];
        for (int i=0; i<NOMBRE_BOUTONS_CIRCULAIRES; i++) {
            final int x = (int)((LARGEUR_MENU - LONGUEUR_TOTALE + Options.RAYON_BOUTON_CERCLE)/2 + Options.EPAISSEUR_BORDURE_CERCLE + i*(Options.ESPACE_INTER_BOUTON+Options.EPAISSEUR_BORDURE_CERCLE)+longueurBoutons);
            final int y = 100;
            boutonsCercle[i] = new BoutonCercle(x,y,rayonBouton, i+1);
            longueurBoutons += rayonBouton*2;
            rayonBouton += Options.RAYON_BOUTON_CERCLE*INCREMENT_TAILLE;
        }

        //BoutonsHex
        final TypeCase[] TYPES = TypeCase.values();
        
        final double TAILLE = 0.5;
        final int NOMBRE_COLONNES = (int) Math.ceil(TYPES.length/(double)Options.NOMBRE_BOUTONS_TYPE_PAR_LIGNE);
        final int LONGUEUR_ESPACEMENTS_CASE = (Options.NOMBRE_BOUTONS_TYPE_PAR_LIGNE+1)*Options.ESPACE_INTER_CASE_BOUTON;
        final int LONGUEUR_CASES = (int)(Options.NOMBRE_BOUTONS_TYPE_PAR_LIGNE*Options.LARGEUR_BOUTON_CASE*TAILLE);
        final int LONGUEUR_TOTALE_CASES = LONGUEUR_ESPACEMENTS_CASE + LONGUEUR_CASES;
        //final int HAUTEUR_TOTALE_CASES = (int)((NOMBRE_COLONNES-1)*Options.ESPACE_INTER_CASE_BOUTON + NOMBRE_COLONNES*Options.LARGEUR_BOUTON_CASE*Options.RATIO_LARGEUR_HAUTEUR*TAILLE);
        int index = 0;
        Cellule[] boutonsType = new Cellule[TYPES.length];
        for (int i=0; i<NOMBRE_COLONNES; i++) {
            for (int j=0; j<Options.NOMBRE_BOUTONS_TYPE_PAR_LIGNE; j++) {
                if (index < TYPES.length) {
                    boutonsType[index] = new Cellule(TYPES[index], i, j, TAILLE, Options.ESPACE_INTER_CASE_BOUTON, true);
                    boutonsType[index].translate((LARGEUR_MENU-LONGUEUR_TOTALE_CASES)/2, Options.yLabels[1]+60);
                }
                index++;
            }
        }

        //BoutonsSymboles
        final TypeSymbole[] SYMBOLES = TypeSymbole.values();
        index = 0;
        final int NOMBRE_COLONNES_SYMBOLE = (int) Math.ceil(SYMBOLES.length/(double)Options.NOMBRE_BOUTONS_TYPE_PAR_LIGNE);
        Cellule[] boutonsSymbole = new Cellule[SYMBOLES.length];
        for (int i=0; i<NOMBRE_COLONNES_SYMBOLE; i++) {
            for (int j=0; j<Options.NOMBRE_BOUTONS_TYPE_PAR_LIGNE; j++) {
                if (index < SYMBOLES.length) {
                    //System.out.println("Image: "+SYMBOLES[index].name()+" "+Donnees.imagesSymboles.get(SYMBOLES[index].name()));
                    boutonsSymbole[index] = new Cellule(TypeCase.VIDE, i, j, TAILLE, Options.ESPACE_INTER_CASE_BOUTON, true, new Symbole(SYMBOLES[index], Donnees.imagesSymboles.get(SYMBOLES[index].name()), true));
                    boutonsSymbole[index].translate((LARGEUR_MENU-LONGUEUR_TOTALE_CASES)/2, Options.yLabels[2]+60);
                }
                index++;
            }
        }
        


        // On rend toutes les cases visibles
        Reception jeu = CSV.lecture(carte, -donnees.obtenirLargeur()/2, -donnees.obtenirHauteur()/2, Donnees.imagesSymboles, donnees.getImagesJoueur());

        donnees.majCellules(jeu.getCellule());

        for (int i=0; i< donnees.obtenirCellules().length; i++) {
            for (Cellule c: donnees.obtenirCellules()[i]) {
                if (c.obtenirSymbole() != null) {
                    if (c.obtenirSymbole().type == TypeSymbole.FUSEE) // On réinitialise le point d'apparition
                        c.obtenirSymbole().majSymbole(TypeSymbole.VIDE, null);
                    c.obtenirSymbole().estVisible = true;
                }
                c.majEstDecouverte(true);
            }
        }
        // On fixe le point d'apparition à celui spécifié dans le CSV
        Cellule pointApparition = donnees.obtenirCellules()[jeu.getJoueur().obtenirCase()[0]][jeu.getJoueur().obtenirCase()[1]];
        pointApparition.obtenirSymbole().majSymbole(TypeSymbole.FUSEE, Donnees.imagesSymboles.get(TypeSymbole.FUSEE.name()));
        donnees.majCelluleDepart(jeu.getJoueur().obtenirCase());

        donnees.majBoutonsCercle(boutonsCercle);
        donnees.majBoutonsType(boutonsType);
        donnees.majBoutonsSymbole(boutonsSymbole);
        donnees.majScene("Editeur de carte");

        donnees.obtenirArrierePlan().majCoords(-donnees.obtenirLargeur()/2, -donnees.obtenirHauteur()/2);

        donnees.notifierObservateur(TypeMisAJour.Scene);
        donnees.notifierObservateur(TypeMisAJour.ArrierePlan);
        donnees.notifierObservateur(TypeMisAJour.Cellules);       // On transmet une unique fois la référence à la liste
        donnees.notifierObservateur(TypeMisAJour.BoutonsCercle);  // On transmet une unique fois la référence à la liste
        donnees.notifierObservateur(TypeMisAJour.BoutonsType);    // On transmet une unique fois la référence à la liste
        donnees.notifierObservateur(TypeMisAJour.BoutonsSymbole); // On transmet une unique fois la référence à la liste
        donnees.notifierObservateur(TypeMisAJour.Peindre);
    }
    
    public void majLargeur(int largeur) {
        donnees.majLargeur(largeur);
    }

    public void majHauteur(int hauteur) {
        donnees.majHauteur(hauteur);
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
                    case KeyEvent.VK_ESCAPE:
                        donnees.majEtatOptions(!donnees.obtenirEtatOptions());
                        if (!donnees.obtenirEtatOptions()) // Si on sort des options
                            jouerEffet("minimize");
                        else // Si on ouvre les options
                            jouerEffet("maximize");

                        // Si on sort des options et qu'on joue au mini-jeu laser (le jeu allait se lancer)
                        if (!donnees.obtenirEtatOptions() && donnees.obtenirEtatMiniJeuLaser().equals(Etat.ON)) {
                            if (System.currentTimeMillis() - donnees.obtenirRepereMinijeuLaser() < donnees.obtenirTempsAvantChrono()) {
                                donnees.majEffet("swoosh");
                                donnees.majRepereMinijeuLaser(System.currentTimeMillis()); // On réinitialise le repère
                                donnees.majChronometreMinijeuLaser("0.0000 secondes"); // On réinitalise l'affichage du temps écoulé
                                donnees.notifierObservateur(TypeMisAJour.ChronometreLaser);
                                donnees.notifierObservateur(TypeMisAJour.NombreErreursLaser);
                            }
                        }

                        donnees.notifierObservateur(TypeMisAJour.Options);
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

        donnees.notifierObservateur(TypeMisAJour.ArrierePlan);
        donnees.notifierObservateur(TypeMisAJour.Cellules);
        donnees.notifierObservateur(TypeMisAJour.Peindre);
    }

    public void ajusterZoom(int notches, Point point) {
        if (aucunJeuEnCours() && !donnees.obtenirEtatOptions() && !donnees.obtenirDefaite()) {
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
            donnees.notifierObservateur(TypeMisAJour.Zoom);
            donnees.notifierObservateur(TypeMisAJour.Peindre);
        }
    }

    public void majStatutSouris(MouseEvent ev, boolean clic) {
        // Si on arrête de cliquer
        if (!clic)
            donnees.majStatutSouris(ev, clic);
        else
            donnees.majStatutSouris(ev, clic);
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
    
    public void enregistrer(){
		enregistrer(false);
	}

    public void enregistrer(boolean reinitialiserExploration) {
        if(reinitialiserExploration==true){
			for (int i=0; i< donnees.obtenirCellules().length; i++) {
				for (Cellule c: donnees.obtenirCellules()[i]) {
					if (c.obtenirSymbole() != null) {

                        if (c.obtenirSymbole().type == TypeSymbole.BACTERIE ||
                            c.obtenirSymbole().type == TypeSymbole.MINERAI) {
                            c.obtenirSymbole().estVisible = false; 
                        } else if (
                            c.obtenirSymbole().type == TypeSymbole.JAMBE ||
                            c.obtenirSymbole().type == TypeSymbole.BRAS ||
                            c.obtenirSymbole().type == TypeSymbole.CAPTEUR ||
                            c.obtenirSymbole().type == TypeSymbole.ENERGIE ||
                            c.obtenirSymbole().type == TypeSymbole.CHENILLES
                        ) {
                            c.obtenirSymbole().estVisible = Math.random() > Options.PROBABILITE_CELLULE_VISIBLE; // ex: 40% de rendre le symbole visible
                        } else {
                            c.obtenirSymbole().estVisible = true; 
                        }
						c.majEstDecouverte(false);
                    }
                }
			}
            HashMap<TypeSymbole, Boolean> symbolesDecouverts = new HashMap<TypeSymbole, Boolean>();
            for (TypeSymbole type: TypeSymbole.values())
                symbolesDecouverts.put(type, false);
            donnees.majSymbolesDecouverts(symbolesDecouverts);
        }
        try {
            List<String[]> inputStream = CSV.givenDataArray_whenConvertToCSV_thenOutputCreated(donnees.obtenirCellules(), donnees.obtenirNomCarte(), true, donnees.obtenirJoueur(), donnees.obtenirCelluleDepart(), donnees.obtenirSymbolesDecouverts());
            // On charge la carte enregistrée
            donnees.obtenirCartes().put(donnees.obtenirNomCarte(), inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void retourMenu() {
        donnees.majScene("Choix du mode");
        donnees.notifierObservateur(TypeMisAJour.Scene);
        donnees.majCelluleDepart(null);
        donnees.majVictoire(false);
        donnees.majDefaite(false);
        donnees.majEtatExploration(false);
    }

    public void majPositionCurseurExtraction(double positionCurseurExtraction) {
        donnees.majPositionCurseurExtraction(positionCurseurExtraction);
    }

    private void desactiverCompetence() {
        donnees.obtenirDerniereCompetence().majSourisDessus(false);
        donnees.majDerniereCompetence(null);
        if (donnees.obtenirRayonDeSelection() != 0) {
            donnees.majRayonDeSelection(0);
            donnees.notifierObservateur(TypeMisAJour.RayonDeSelection);
        }
    }

    // Gestion des cartes

    public void cloner(String nouveauNom, List<String[]> carte) throws IOException {
        String dossier = CSV.fichierExterne(Options.NOM_DOSSIER_CARTES, "res/"+Options.NOM_DOSSIER_IMAGES+"/", "src/main/java/");
        nouveauNom = Formes.stripAccents(nouveauNom);
        File file = new File(dossier+"/"+nouveauNom+".csv");
        if (file.exists()) {
            jouerEffet("error");
            throw new java.io.IOException("Ce nom est déjà utilisé !");
        }
        CSV.ecrireFichierDepuisCache(nouveauNom, carte, false);
        jouerEffet("confirmation");
        donnees.obtenirCartes().put(nouveauNom, carte);
        donnees.notifierObservateur(TypeMisAJour.Cartes);
    }

    public void creerCarte(String nom, int nombreLignes, int nombreColonnes) throws IOException {
        String dossier = CSV.fichierExterne(Options.NOM_DOSSIER_CARTES, "res/"+Options.NOM_DOSSIER_IMAGES+"/", "src/main/java/");
        nom = Formes.stripAccents(nom);
        File file = new File(dossier+"/"+nom+".csv");
        if (file.exists()) {
            jouerEffet("error");
            throw new java.io.IOException("Ce nom est déjà utilisé !");
        }
        if (nombreLignes < 1 || nombreColonnes < 1 || nombreLignes > Options.NOMBRE_MAX_LIGNES || nombreColonnes > Options.NOMBRE_MAX_COLONNES) {
            jouerEffet("error");
            throw new IOException("Les dimensions spécifiées sont trop petites !");
        }
        Cellule[][] cellules = new Cellule[nombreLignes][nombreColonnes];
        for (int i=0; i < cellules.length; i++) {
            for (int j=0; j < cellules[i].length; j++)
                cellules[i][j] = new Cellule(i,j);
        } try {
            /* On ne recharge pas les cartes avec en obtenant les ressources avec ObtenirRessources.java
            * Car cette méthode utilise getResourceAsStream()
            * qui permet de chercher des ressources présentes dans le classpath (ressource statique)
            * Or ici on a des fichiers générés dynamiquement
            * On modifie donc directement la liste de cartes
            */
            List<String[]> csv = CSV.givenDataArray_whenConvertToCSV_thenOutputCreated(cellules, nom, true, null, null, donnees.obtenirSymbolesDecouverts());
            jouerEffet("confirmation");
            donnees.obtenirCartes().put(nom, csv);
            donnees.notifierObservateur(TypeMisAJour.Cartes);
        } catch (IOException e) {
            jouerEffet("error");
            e.printStackTrace();
        }
    }
    
    public void renommer(String ancienNom, String nouveauNom, List<String[]> carte) throws IOException {
        String dossier = CSV.fichierExterne(Options.NOM_DOSSIER_CARTES, "res/"+Options.NOM_DOSSIER_IMAGES+"/", "src/main/java/");
        nouveauNom = Formes.stripAccents(nouveauNom);
        File file = new File(dossier+"/"+nouveauNom+".csv");
        if (file.exists()) {
            jouerEffet("error");
            throw new java.io.IOException("Ce nom est déjà utilisé !");
        }

        Path source = Paths.get(dossier+"/"+ancienNom+".csv");
        Files.move(source, source.resolveSibling(nouveauNom+".csv"));

        jouerEffet("confirmation");

        donnees.obtenirCartes().put(nouveauNom, carte);
        donnees.obtenirCartes().remove(ancienNom);
        donnees.notifierObservateur(TypeMisAJour.Cartes);
    }

    public void supprimer(String nom, List<String[]> carte) throws IOException {

        String chemin = CSV.fichierExterne(Options.NOM_DOSSIER_CARTES+"/"+nom+".csv", "res/"+Options.NOM_DOSSIER_IMAGES+"/", "src/main/java/");
        File fichier = new File(chemin);

        if (!fichier.delete()) {
            jouerEffet("error");
            throw new java.io.IOException("Le fichier n'a pas pu être supprimé !");
        }
        jouerEffet("confirmation");
        donnees.obtenirCartes().remove(nom);
        donnees.notifierObservateur(TypeMisAJour.Cartes);
    }
}