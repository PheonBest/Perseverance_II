import java.awt.Dimension;
import java.awt.Image;
import java.util.ArrayList;
import java.util.LinkedList;
import java.awt.*;

public class Robot extends Avatar {
    
    //-------------------------------------------------------------------------------------------------- Attributs
    
    // Paramètres vitaux du robots
    private int batterie;
    private int nbRecharges;
    // 3 voyants principaux: Jambes, Bras Mécatro, Capteurs
    private Voyants[] voyantsPrincipaux = new Voyants[3]; 
    // Plusieurs éléments désignés par ces voyants
    private ComposantRobot[] jambes = new ComposantRobot[2];
    private ComposantRobot[] bras = new ComposantRobot[2];
    private ComposantRobot[] capteurs = new ComposantRobot[3];
    private double kmTot;
    private double comptKm;
    private int[] derniereCase = null;
    
    // Trajectoire
    /* Pour aller d'un point à un autre sur la carte, on utilise une liste de cases ciblées, appelées but (à atteindre).
     * Chaque but est un tableau d'entier*/ 
    private LinkedList<int[]> but = new LinkedList<int[]>();
    
    //  Position du robot dans la matrice de la carte
    private int ligne = 0;
    private int colonne = 0;
    // On mémorise sa position précédente
    private int lignePrecedente = 0;
    private int colonnePrecedente = 0;
    
    // Position réélle du robot sur la carte, dont l'origine se rouve en haut à gauche
    private int xFictif = 0;
    private int yFictif = 0;
    
    //--------------------------------------------------------------------------------------------------- Constructeurs
    
    // Constructeur complet 
    public Robot(int nivBatterie, int nbR, int listeEtats[], int listeUsures[],int nbKmParcourus, int compteurKm , int animationIndex, int dureeImage, ArrayList<ArrayList<Image>> image, int[] coords, int x, int y, double r, int dx, int dy, double dr) {
        // Le robot est un avatar, il hérite donc de son constructeur et de ses conditions d'avatar
        super(animationIndex, dureeImage, image, coords, x, y, r, dx, dy, dr);
        xFictif = x;
        yFictif = y;
        this.batterie = nivBatterie;
        this.nbRecharges = nbR;
        this.kmTot = nbKmParcourus;
        this.comptKm = compteurKm;
        
        // Construction des composants - listeEtats et listeUsures doivent être de listes de 7 int
        this.jambes[0] = new ComposantRobot("Jambe droite",listeEtats[0],listeUsures[0]);
        this.jambes[1] = new ComposantRobot("Jambe gauche",listeEtats[1],listeUsures[1]);
        this.bras[0] = new ComposantRobot("Bras droit",listeEtats[2],listeUsures[2]);
        this.bras[1] = new ComposantRobot("Bras gauche",listeEtats[3],listeUsures[3]);
        this.capteurs[0] = new ComposantRobot("Température interne",listeEtats[4],listeUsures[4]);
        this.capteurs[1] = new ComposantRobot("Caméras",listeEtats[5],listeUsures[5]);
        this.capteurs[2] = new ComposantRobot("Capteurs de pression",listeEtats[6],listeUsures[6]);
        
        voyantsPrincipaux[0] = new Voyants("Jambes");
        voyantsPrincipaux[1] = new Voyants("Bras");
        voyantsPrincipaux[2] = new Voyants("Capteurs");
        actualiseVP();
    }
    
    // Constructeur "Robot neuf"
    public Robot(ArrayList<ArrayList<Image>> image, int x, int y) {
        this(Options.BATTERIE_MAX,0, new int[] {Options.ALERTE_MIN,Options.ALERTE_MIN,Options.ALERTE_MIN,Options.ALERTE_MIN,Options.ALERTE_MIN,Options.ALERTE_MIN,Options.ALERTE_MIN}, new int[] {Options.USURE_MIN,Options.USURE_MIN,Options.USURE_MIN,Options.USURE_MIN,Options.USURE_MIN,Options.USURE_MIN,Options.USURE_MIN},0,0,0, Options.JOUEUR_DUREE_ANIMATION, image, new int[] {0,0}, x, y, .0, 0, 0, .0);
    }
    
    //---------------------------------------------------------------------------------------------------- Setters et getters
    
    public Dimension getCoordonnees() {
        return new Dimension(xFictif,yFictif);
    }
    
    public int getBatterie(){
        return this.batterie;
    }
    public int getNbRecharges(){
        return this.nbRecharges;
    }

    public Voyants[] getVP(){
        return this.voyantsPrincipaux;
    }
    
    public ComposantRobot[] getJambes(){
        return this.jambes;
    }
    public ComposantRobot[] getBras(){
        return this.bras;
    }
    public ComposantRobot[] getCapteurs(){
        return this.capteurs;
    }
    
    public double getKmParcourus(){
        return this.kmTot;
    }
   
    public void setBatterie(int nivBatterie){
        if(nivBatterie <= Options.BATTERIE_MIN){
            batterie = Options.BATTERIE_MIN;
        }
        else if(nivBatterie >= Options.BATTERIE_MAX){
            batterie = Options.BATTERIE_MAX;
        }
        else batterie = nivBatterie;
    }
    
    public void setCompteurkm(int nb){
        if(nb>=0){
            this.comptKm = nb;
        }else this.comptKm=0;
    }
    
    public void resetCompteurkm(){
        this.comptKm=0;
    }
    
    
    //---------------------------------------------------------------------------------------------------- Méthodes de fonctionnalités du robot
    
    // Actualise les voyants principaux
    public void actualiseVP(){
        // On calcul la somme des états possible pour un liste de composants de même types et la somme réelle des états dans une même liste
        int sEtatJambes = (Options.ALERTE_MAX-Options.ALERTE_MIN+1)*jambes.length;
        int sEtatBras = (Options.ALERTE_MAX-Options.ALERTE_MIN+1)*bras.length;
        int sEtatCapteurs = (Options.ALERTE_MAX-Options.ALERTE_MIN+1)*capteurs.length;
        int sj=0;
        int sb=0;
        int sc=0;
        for(int i=0; i<jambes.length; i++){ sj += jambes[i].voyant.getEtat();}
        for(int i=0; i<bras.length; i++){sb += bras[i].voyant.getEtat();}
        for(int i=0; i<capteurs.length; i++){sc += capteurs[i].voyant.getEtat();}
        
        // On définit le 1/3 de la somme des états d'une liste de composants
        double rj = (sEtatJambes/((double)(Options.ALERTE_MAX-Options.ALERTE_MIN+1)));
        double rb = (sEtatBras/((double)(Options.ALERTE_MAX-Options.ALERTE_MIN+1)));
        double rc = (sEtatCapteurs/((double)(Options.ALERTE_MAX-Options.ALERTE_MIN+1)));
        
        // On fait le ratio entre la somme des états actuels et la somme des états possibles pour que l'état du voyant principal correspondant soit représentatif de la réalité
        if(sj>=0 && sj<=rj) this.voyantsPrincipaux[0].setEtat(Options.ALERTE_MIN);
        if(sj>rj && sj<=2*rj) this.voyantsPrincipaux[0].setEtat(Options.ALERTE_MOY);
        if(sj>2*rj && sj<= sEtatJambes) this.voyantsPrincipaux[0].setEtat(Options.ALERTE_MAX);
        
        if(sb>=0 && sb<=rb) this.voyantsPrincipaux[1].setEtat(Options.ALERTE_MIN);
        if(sb>rb && sb<=2*rb) this.voyantsPrincipaux[1].setEtat(Options.ALERTE_MOY);
        if(sb>2*rb && sb<= sEtatBras) this.voyantsPrincipaux[1].setEtat(Options.ALERTE_MAX);
        
        if(sc>=0 && sc<=rc) this.voyantsPrincipaux[2].setEtat(Options.ALERTE_MIN);
        if(sc>rc && sc<=2*rc) this.voyantsPrincipaux[2].setEtat(Options.ALERTE_MOY);
        if(sc>2*rc && sc<= sEtatCapteurs) this.voyantsPrincipaux[2].setEtat(Options.ALERTE_MAX);
        
    }

    public void actualiseBatterie(){
        setBatterie(Options.BATTERIE_MAX - (int)(comptKm*Options.CONSO_BATTERIE_PAR_KM));
        //TODO : AJOUTER CAS EN FONCTION DU TYPE DE CASE ACTUEL
    }
    
    public void actualiseCptKm(Dimension but){
        comptKm += Math.sqrt(but.getWidth()*but.getWidth()+but.getHeight()*but.getHeight())*Options.KM_PAR_PIXELS;
        kmTot += comptKm;
    }
    
    public void rechargerBat(){
        setBatterie(Options.BATTERIE_MAX);
        this.nbRecharges += 1;
    }
    
    public void usureJambes(double nbKm){
        // définition du tiers d'usure pour adapter les voyants des composants à leur usure en fonction d'un facteur chance
        double r3u = (Options.USURE_MAX-Options.USURE_MIN)/3.0;
        int chance;
        // à partir d'une certaine usure, les jambes risquent de disfonctionner ( indépendamment entre elles)
        for(int i=0; i<jambes.length; i++){
            jambes[i].setUsure(jambes[i].getUsure() + (int)(nbKm*Options.USURE_PAR_KM));
            chance = (int)(Math.random()*100 + 1);
        
            if (jambes[i].getUsure()>2*r3u && chance<=Options.CHANCE_DEGRADATION) jambes[i].degraderC();
        }
    }
    
    public void usureBras(double nbKm){
         // TODO 
    }
    public void usureCapteurs(double nbKm){
         // TODO 
    }
    
    public boolean isDead(){
        boolean GAME_OVER = false;
        int nbVrouges;
        
        // GAME OVER si la batterie tombe à 0
        if(getBatterie()== Options.BATTERIE_MIN) GAME_OVER = true;
        
        // GAME OVER si 2 des voyants principaux deviennent rouges
        nbVrouges =0;
        for( int i=0; i<voyantsPrincipaux.length; i++){
            if(voyantsPrincipaux[i].getEtat()==Options.ALERTE_MAX) nbVrouges+=1;
        }
        if(nbVrouges >= Options.PANNES_MAX)  GAME_OVER = true;
        
        // GAME OVER si 2 composants même type sont rouges
        nbVrouges = 0;
        for( int i=0; i<jambes.length; i++){
            if(jambes[i].voyant.getEtat() == Options.ALERTE_MAX) nbVrouges+=1;
        }
        if(nbVrouges >= Options.PANNES_MAX)  GAME_OVER = true;
        
        nbVrouges =0;
        for( int i=0; i<bras.length; i++){
            if(bras[i].voyant.getEtat() == Options.ALERTE_MAX) nbVrouges+=1;
        }
        if(nbVrouges >= Options.PANNES_MAX)  GAME_OVER = true;
        
        nbVrouges =0;
        for( int i=0; i<jambes.length; i++){
            if(jambes[i].voyant.getEtat() == Options.ALERTE_MAX) nbVrouges+=1;
        }
        if(nbVrouges >= Options.PANNES_MAX)  GAME_OVER = true;
        
        return GAME_OVER;
    }
    
    //---------------------------------------------------------------------------------------------------- Méthodes pour les déplacments du robot
    
    // La case à atteindre est définie par une série de sous-but que l'on donne à cette méthode
    public void definirBut(LinkedList<int[]> liste) {
        but = liste;
        
        if (but.isEmpty()) {
            animationIndex = 0; // On passe l'animation du joueur en mode "pause" (Idle)
            dx = dy = 0;
        } else {
            // On prend une case à cibler, la première de la liste de buts
            int[] d = but.getFirst(); 
            definirDirection(new Dimension(d[0], d[1]));
            animationIndex = 2; //  Image qui montre le robot en marche
        }
    }
    
    // 
    private void definirDirection(Dimension caseCiblee) {
        float angle = (float) Math.atan2(caseCiblee.getHeight() - yFictif, caseCiblee.getWidth() - xFictif);
        //System.out.println(Math.toDegrees(angle));
        
        dx = (int) (Math.cos(angle)*10); // 10 est la vitesse (pixel/itération de la boucle du jeu)
        dy = (int) (Math.sin(angle)*10);
        animationIndex = 2; // Image qui montre le robot en marche
    }
    
    // Cette méthode permet le déplacement du robot si il en a le droit, et actualise les composants en rapport avec le déplacement.
    @Override
    public void move(){
		if(!movable){
			System.out.println("Le joueur ne peut pas être déplacé");
            return;
        }
		updateCoords();
	}

    // Cette méthode est utilisée dans move(), elle actualise les coordonées, les km parcourus et l'usure des jambes
    @Override
    public void updateCoords() {

        r += dr;
        lignePrecedente = ligne;
        colonnePrecedente = colonne;

        if (!but.isEmpty()) {
            xFictif += dx;
            yFictif += dy;
            int[] d = but.getFirst(); // On prend une case à cibler 
            /* On ne veut pas que le joueur puisse parcourir toute la carte d'un coupe de clic, 
             * il est donc obligé de  cliquer aux alentours du robots sur un rayon délimité par 
             * Options.JOUERUR_TOLERANCE_DEPLACEMENT*/
            if (Math.abs(xFictif - d[0]) < Options.JOUERUR_TOLERANCE_DEPLACEMENT)
                dx = (int) d[0] - xFictif;
            if (Math.abs(yFictif - d[1]) < Options.JOUERUR_TOLERANCE_DEPLACEMENT)
                dy = (int) d[1] - yFictif;
            if (dx == 0 && dy == 0) {
                
                ligne = d[2];
                colonne = d[3];
                derniereCase = new int[] {d[2],d[3]};
                actualiseCptKm(new Dimension(d[0], d[1]));
                usureJambes(comptKm);
                // usure Bras
                // usure Capteurs
                actualiseBatterie();
                actualiseVP();
                but.removeFirst();
                
                if (but.isEmpty()) // Si il ne reste plus de cases à parcourir, le robot est arrivé à la case demandée
                    animationIndex = 0; // On passe l'animation du joueur en mode "pause" (Idle)
                else {
                    d = but.getFirst(); // On prend une case à cibler 
                    definirDirection(new Dimension(d[0], d[1]));
                    //System.out.println("Coords joueur : "+xFictif +" _ "+ yFictif);
                    //System.out.println("Coords but : "+ but.getFirst().getWidth() +" _ "+ but.getFirst().getHeight());
                }
                
            }
        }
    }
    
    public int[] obtenirCasePrecedente() {
        return new int[]{lignePrecedente, colonnePrecedente};
    }
    public int[] obtenirCase() {
        return new int[]{ligne, colonne};
    }
    public void majCase(int ligne, int colonne) {
        derniereCase = new int[] {ligne,colonne};
        this.ligne = ligne;
        this.colonne = colonne;

        // On modifie l'état précédent des lignes/colonnes
        // Donc la case sur laquelle on place le joueur n'aura pas d'effet
        lignePrecedente = ligne;
        colonnePrecedente = colonne;
    }
    public int[] obtenirCoordonnees() {
        return new int[]{xFictif, yFictif};
    }
    public int[] obtenirDerniereCase() {
        return derniereCase;
    }
    public void majDerniereCase(int[] derniereCase) {
        this.derniereCase = derniereCase;
    }

    public LinkedList<int[]> obtenirTrajectoire() {
        return but;
    }
}
