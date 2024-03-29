import java.awt.Dimension;
import java.awt.Image;
import java.util.ArrayList;
import java.util.LinkedList;

public class Robot extends Avatar {
    
    //-------------------------------------------------------------------------------------------------- Attributs
    
    // Paramètres vitaux du robots
    private int batterie;
    private int nbRecharges;
    // 3 voyants principaux: Jambes, Bras Mécatro, Capteurs
    private Voyants[] voyantsPrincipaux = new Voyants[3]; 
    // Composants
    private ComposantRobot[] jambes = new ComposantRobot[2];
    private ComposantRobot[] bras = new ComposantRobot[2];
    private ComposantRobot[] capteurs = new ComposantRobot[3];
    // Statistiques
    private double kmTot;
    private double comptKm;
    private int nbCasesExplorees = 0;  
    private int nbCasesTotales = Options.HAUTEUR_CARTE*Options.LARGEUR_CARTE;
    private int[] derniereCase = null;
    //Compétences
    private boolean surChenilles = false;
    private int nombrePonts = 0;
    
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
    public Robot(int nivBatterie, int nbR, int listeEtats[], int listeUsures[],int nbKmParcourus, int compteurKm , int animationIndex, int dureeImage, ArrayList<ArrayList<Image>> image) {
        // Le robot est un avatar, il hérite donc de son constructeur et de ses conditions d'avatar
        super(animationIndex, dureeImage, image, new int[]{0,0}, 0, 0, 0, 0, 0, 0);
        xFictif = 0;
        yFictif = 0;
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
        this(Options.BATTERIE_MAX,0, new int[] {Options.ALERTE_MIN,Options.ALERTE_MIN,Options.ALERTE_MIN,Options.ALERTE_MIN,Options.ALERTE_MIN,Options.ALERTE_MIN,Options.ALERTE_MIN}, new int[] {Options.USURE_MIN,Options.USURE_MIN,Options.USURE_MIN,Options.USURE_MIN,Options.USURE_MIN,Options.USURE_MIN,Options.USURE_MIN},0,0,0, Options.JOUEUR_DUREE_ANIMATION, image);
    }
    
    //---------------------------------------------------------------------------------------------------- Setters et getters

    public int getBatterie(){
        return this.batterie;
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
    public void setCompteurkm(int nb){
        if(nb>=0){
            this.comptKm = nb;
        }else this.comptKm=0;
    }
    public double getCompteurKm(){
		return this.comptKm;
	}
    public void resetCompteurkm(){
        this.comptKm=0;
    }
    
    public Dimension getCoordonnees() {
        return new Dimension(xFictif,yFictif);
    }
    public int[] obtenirCoordonnees() {
        return new int[]{xFictif, yFictif};
    }
    public LinkedList<int[]> obtenirTrajectoire() {
        return but;
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
    public int[] obtenirCasePrecedente() {
        return new int[]{lignePrecedente, colonnePrecedente};
    }
    public int[] obtenirDerniereCase() {
        return derniereCase;
    }
    public void majDerniereCase(int[] derniereCase) {
        this.derniereCase = derniereCase;
    }
    
    public int obtenirCasesExplorees(){
        return this.nbCasesExplorees;
    }
    public void majCasesExplorees(){
        if(this.nbCasesExplorees>=0 && this.nbCasesExplorees<nbCasesTotales)this.nbCasesExplorees += 1;
    }
    public void majCasesExplorees(int nbCasesExplorees2){
        if(nbCasesExplorees2>=0 && nbCasesExplorees2<nbCasesTotales) this.nbCasesExplorees = nbCasesExplorees2;
    }
    public int obtenirPExploration(){
        return ((int)((((double)this.nbCasesExplorees)/((double)nbCasesTotales))*100));
    }
    public void majCasesTotales(int nbCasesTotales) {
        this.nbCasesTotales = nbCasesTotales;
    }
    
    public int obtenirNombrePont() {
        return nombrePonts;
    }

    public void majNombrePonts(int nombrePonts) {
        this.nombrePonts = nombrePonts;
    }
    

    
    
    //---------------------------------------------------------------------------------------------------- Méthodes de fonctionnalités du robot
    
    public void actualiseBatterie(){
        setBatterie(Options.BATTERIE_MAX - (int)(comptKm*Options.CONSO_BATTERIE_PAR_KM));
    }
    public void rechargerBat(){
        resetCompteurkm();
        setBatterie(Options.BATTERIE_MAX);
        this.nbRecharges += 1;
    }
    public void malusBatterie(TypeCase t){
        switch(t){
            case EAU :
                setCompteurkm((int)(comptKm + 5000));
                actualiseBatterie();
                break;
            case NEIGE :
                setCompteurkm((int)(comptKm + 5000));
                actualiseBatterie();
                break;
            case SABLE_MOUVANTS : 
                setCompteurkm((int)(comptKm + 10000));
                actualiseBatterie();
                break;
            default :
                break;
        }
    }
    
    // Actualise les voyants principaux
    public void actualiseVP(){
        // On calcul la somme des états possible pour un liste de composants de même types et la somme réelle des états dans une même liste
        int sEtatJambes = (Options.ALERTE_MAX-Options.ALERTE_MIN)*jambes.length;
        int sEtatBras = (Options.ALERTE_MAX-Options.ALERTE_MIN)*bras.length;
        int sEtatCapteurs = (Options.ALERTE_MAX-Options.ALERTE_MIN)*capteurs.length;
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

    public void actualiseCptKm(Dimension but){
        double d = Math.sqrt(but.getWidth()*but.getWidth()+but.getHeight()*but.getHeight())*Options.KM_PAR_PIXELS;
        comptKm += d;
        kmTot += d;
    }
    
    public void usureJambes(double nbKm){
        // définition du tiers d'usure pour adapter les voyants des composants à leur usure en fonction d'un facteur chance
        double r3u = (Options.USURE_MAX-Options.USURE_MIN)/3.0;
        int chance;
        // à partir d'une certaine usure, les jambes risquent de disfonctionner ( indépendamment entre elles)
        for(int i=0; i<jambes.length; i++){
            jambes[i].setUsure(jambes[i].getUsure() + (int)(nbKm*Options.USURE_PAR_KM));
            chance = (int)(Math.random()*100 + 1);
        
            if (jambes[i].getUsure()>2*r3u && chance<=Options.CHANCE_DEGRADATION_JAMBES) jambes[i].degraderC();
        }
    }
    
    public void usureBras(){
        // définition du tiers d'usure pour adapter les voyants des composants à leur usure en fonction d'un facteur chance
        double r3u = (Options.USURE_MAX-Options.USURE_MIN)/3.0;
        int chance;
        // à partir d'une certaine usure, les bras risquent de disfonctionner ( indépendamment entre eux)
        for(int i=0; i<jambes.length; i++){
            bras[i].setUsure(bras[i].getUsure() + Options.USURE_PAR_UTILISATION);
            chance = (int)(Math.random()*100 + 1);
        
            if (bras[i].getUsure()>2*r3u && chance<=Options.CHANCE_DEGRADATION_AUTRES) bras[i].degraderC();
        }
    }
    public void usureCapteurs(){
         // définition du tiers d'usure pour adapter les voyants des composants à leur usure en fonction d'un facteur chance
        double r3u = (Options.USURE_MAX-Options.USURE_MIN)/3.0;
        int chance;
        // à partir d'une certaine usure, les capteurs risquent de disfonctionner ( indépendamment entre eux)
        for(int i=0; i<capteurs.length; i++){
            capteurs[i].setUsure(capteurs[i].getUsure() + Options.USURE_PAR_UTILISATION);
            chance = (int)(Math.random()*100 + 1);
        
            if (capteurs[i].getUsure()>2*r3u && chance<=Options.CHANCE_DEGRADATION_AUTRES)capteurs[i].degraderC();
        }
    }
    
    public void maintenance(TypeSymbole s){
        switch(s){
            case JAMBE : 
                for(int i=0; i<jambes.length; i++){ jambes[i].reparerC();} 
                break;
            case BRAS :
                for(int i=0; i<bras.length; i++){ bras[i].reparerC();} 
                break;
            case CAPTEUR : 
                for(int i=0; i<capteurs.length; i++){ capteurs[i].reparerC();} 
                break;
            case ENERGIE :
                rechargerBat();
                break;
            default : 
                System.out.println("Erreur : la maintenance n'a pas lieu ! ");
                break;
        }
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
        
        dx = (int) (Math.cos(angle)*Options.VITESSE_DEPLACEMENT_ROBOT); 
        dy = (int) (Math.sin(angle)*Options.VITESSE_DEPLACEMENT_ROBOT);
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
  
    public void majSurChenilles(boolean surChenilles) {
        this.surChenilles = surChenilles;
    }
    
    public boolean obtenirSurChenilles(){
		return this.surChenilles;
	}
}
