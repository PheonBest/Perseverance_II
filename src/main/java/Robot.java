import java.awt.Dimension;
import java.awt.Image;
import java.util.ArrayList;
import java.util.LinkedList;
import java.awt.*;

public class Robot extends Avatar {
    
    //-------------------------------------------------------------------------------------------------- Attributs
    
    // Paramètres vitaux du robots
    private int batterie;
    // 3 voyants principaux: Jambes, Bras Mécatro, Capteurs
    private Voyants[] voyantsPrincipaux = new Voyants[3]; 
    // Plusieurs éléments désignés par ces voyants
    private ComposantRobot[] jambes = new ComposantRobot[2];
    private ComposantRobot[] bras = new ComposantRobot[2];
    private ComposantRobot[] capteurs = new ComposantRobot[3];
    private double kmTot;
    private double comptKm;
    public boolean GAME_OVER = false;
    
    // Trajectoire
    /* La classe dimension est une classe qui comporte comme attribut une hauteur et une largeur,
     * elle est utilisée ici pour référencer la position d'un point à atteindre sur la carte par rapport au robot.
     * Pour aller d'un point à un autre sur la carte, on utilise une liste de cases ciblées, appelées but (à atteindre).*/ 
     
    private LinkedList<Dimension> but = new LinkedList<Dimension>();
    
    //  Position du robot dans la matrice de la carte
    private int row = 0;
    private int column = 0;
    
    // Position réélle du robot sur la carte, dont l'origine se rouve en haut à gauche
    private int xFictif = 0;
    private int yFictif = 0;
    
    //--------------------------------------------------------------------------------------------------- Constructeurs
    
    // Constructeur complet 
    public Robot(int nivBatterie, int listeEtats[], int listeUsures[],int nbKmParcourus, int compteurKm , int animationIndex, int dureeImage, ArrayList<ArrayList<Image>> image, int[] coords, int x, int y, double r, int dx, int dy, double dr) {
        // Le robot est un avatar, il hérite donc de son constructeur et de ses conditions d'avatar
        super(animationIndex, dureeImage, image, coords, x, y, r, dx, dy, dr);
        xFictif = x;
        yFictif = y;
        this.batterie = nivBatterie;
        this.kmTot = nbKmParcourus;
        this.comptKm = compteurKm;
        
        voyantsPrincipaux[0] = new Voyants("Jambes mécatroniques");
        voyantsPrincipaux[1] = new Voyants("Bras mécatroniques");
        voyantsPrincipaux[2] = new Voyants("Capteurs");
        
        
        for(int i=0; i<(jambes.length + bras.length + capteurs.length); i++){
            if(i<jambes.length){
                this.jambes[i]= new ComposantRobot("Jambes mécatronique"+i, listeEtats[i],listeUsures[i]);
            }else if(i>=jambes.length && i<bras.length){
                this.bras[i-jambes.length]= new ComposantRobot("Bras mécatronique"+(i-jambes.length), listeEtats[i-jambes.length],listeUsures[i-jambes.length]);
            }else this.capteurs[i-jambes.length-bras.length]= new ComposantRobot("Bras mécatronique"+(i-jambes.length-bras.length) ,listeEtats[i-jambes.length-bras.length],listeUsures[i-jambes.length-bras.length]);
        }
        
    }
    
    // Constructeur "Robot neuf"
    public Robot(ArrayList<ArrayList<Image>> image, int x, int y) {
        this(Options.BATTERIE_MAX, new int[] {Options.ALERTE_MIN,Options.ALERTE_MIN,Options.ALERTE_MIN,Options.ALERTE_MIN,Options.ALERTE_MIN,Options.ALERTE_MIN,Options.ALERTE_MIN}, new int[] {Options.USURE_MIN,Options.USURE_MIN,Options.USURE_MIN,Options.USURE_MIN,Options.USURE_MIN,Options.USURE_MIN,Options.USURE_MIN},0,0,0, Options.JOUEUR_DUREE_ANIMATION, image, new int[] {0,0}, x, y, .0, 0, 0, .0);
    }
    
    //---------------------------------------------------------------------------------------------------- Setters et getters
    
    public int getBatterie(){
        return batterie;
    }
    
    public double getKmParcourus(){
        return this.kmTot;
    }
   
    public void setBatterie(int nivBatterie){
        if(nivBatterie <= Options.BATTERIE_MIN){
            batterie = Options.BATTERIE_MIN;
            GAME_OVER = true;
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
        batterie = Options.BATTERIE_MAX - (int)(comptKm*Options.CONSO_BATTERIE_PAR_KM);
        //TODO : AJOUTER CAS EN FONCTION DU TYPE DE CASE ACTUEL
    }
    
    public void actualiseCptKm(Dimension but){
        comptKm += Math.sqrt(but.getWidth()*but.getWidth()+but.getHeight()*but.getHeight());
        kmTot += comptKm;
    }
    
    public void rechargerBat(){
        setBatterie(Options.BATTERIE_MAX);
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
    
    
    //---------------------------------------------------------------------------------------------------- Méthodes pour les déplacments du robot
    
    public void definirBut(LinkedList<Dimension> liste) {
        this.but = liste;
        definirDirection(but.getFirst());
        animationIndex = 2; //  Image qui montre le robot marcher 
    }

    private void definirDirection(Dimension caseCiblee) {
        float angle = (float) Math.atan2(caseCiblee.getHeight() - yFictif, caseCiblee.getWidth() - xFictif);
        //System.out.println(Math.toDegrees(angle));
        
        dx = (int) (Math.cos(angle)*10); // 10 est la vitesse (pixel/itération de la boucle du jeu)
        dy = (int) (Math.sin(angle)*10);
        animationIndex = 2; // Image qui montre le robot marcher 
    }
    
    // Cette méthode permet le déplacement du robot si il en a le droit, et actualise les composants en rapport avec le déplacement.
    @Override
    public void move(){
		if(!movable){
			System.out.println("Le joueur ne peut pas être déplacé");
            return;
        }
		updateCoords();
        actualiseBatterie();
	}

    // Cette méthode est utilisée dans move(), elle actualise les coordonées, les km parcourus et l'usure des jambes
    @Override
    public void updateCoords() {

        r += dr;

        if (!but.isEmpty()) {
            xFictif += dx;
            yFictif += dy;
            Dimension d = but.getFirst(); // On prend une case à cibler 
            if (Math.abs(xFictif - d.getWidth()) < Options.JOUERUR_TOLERANCE_DEPLACEMENT)
                dx = (int) d.getWidth() - xFictif;
            if (Math.abs(yFictif - d.getHeight()) < Options.JOUERUR_TOLERANCE_DEPLACEMENT)
                dy = (int) d.getHeight() - yFictif;
            if (dx == 0 && dy == 0) {
                actualiseCptKm(d);
                usureJambes(comptKm);
                but.removeFirst();
                if (but.isEmpty()) // Si il ne reste plus de cases à parcourir, le robot est arrivé à la case demandée
                    animationIndex = 0; // On passe l'animation du joueur en mode "pause" (Idle)
                else {
                    definirDirection(but.getFirst());
                    //System.out.println("Coords joueur : "+xFictif +" _ "+ yFictif);
                    //System.out.println("Coords but : "+ but.getFirst().getWidth() +" _ "+ but.getFirst().getHeight());
                }
                
            }
        }
    }
    
    // ----------------------------------------------------------------------------------------------- Méthodes graphiques en rapport avec le robot
    
    public void paintControlPanel(Graphics g, int Xp, int Yp) {
        super.dessiner(g);
        // Panneau de contrôle : Origine(Xp,Yp)
        g.setColor(new Color(175,175,175));
        g.fill3DRect(Xp,Yp,500,100,true);
        g.setColor(Color.black);
        g.drawString("---------------------------------- PANNEAU DE CONTRÔLE ----------------------------------",Xp+30,Yp+15);
        
        //Batterie : Origine(Xb,Yb)
        int Xb = Xp+10;
        int Yb = Yp+40;
        g.setColor(Color.black);
        g.fillRect(Xb,Yb,118,48);
        g.setColor(Color.white);
        g.fillRect(Xb+3,Yb+3,112,42);
        g.setColor(Color.black);
        g.fillRect(Xb+6,Yb+6,106,36);
        g.fillRect(Xb+6,Yb+19,112,10);
        // Jauge de batterie de couleur ajustable
        g.setColor(new Color((float)(1.0-batterie*0.01), (float)(0.0+batterie*0.01), (float)(0.0)));
        g.fillRect(Xb+9,Yb+9,batterie,30);
        
        // Voyants : Origine(Xv,Yv) ( centre du 1er voyant ) 
        int Xv = Xb + 200;
        int Yv = Yb + 24;
        for(int i=0;i<voyantsPrincipaux.length;i++){
           voyantsPrincipaux[i].setPositionR(Xv+i*100,Yv,25); 
           voyantsPrincipaux[i].dessinerVoyant(g);
        }
    }
}
