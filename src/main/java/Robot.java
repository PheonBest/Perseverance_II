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
    private Voyants[] tabVoyants = new Voyants[3]; 
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
        
        tabVoyants[0] = new Voyants("Jambes mécatroniques");
        tabVoyants[1] = new Voyants("Bras mécatroniques");
        tabVoyants[2] = new Voyants("Capteurs");
        
        
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
     public int getEtatVoyanti(int indice){ // indice varie entre 0 et 2
        if(indice>=0 && indice<=2){
            return tabVoyants[indice].getEtat();
        }
        else{
            System.out.println("Erreur : les indices des voyants doivent être compris en 0 et 2");
            return 0;
        }
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
    public void setEtatVoyanti(int indice, int nivAlerte){
        if(indice>=0 && indice<=2){
            tabVoyants[indice].setEtat(nivAlerte);
        }
        else System.out.println("Erreur : les indices des voyants doivent être compris en 0 et 2");
    }
    public void setCompteurkm(int nb){
        if(nb>=0){
            this.comptKm = nb;
        }else this.comptKm=0;
    }
    
    
    //---------------------------------------------------------------------------------------------------- Méthodes de fonctionnalités du robot
    public void actualiseTabV(){
        int sEtatJambes = (Options.ALERTE_MAX-Options.ALERTE_MIN)*jambes.length;
        int sEtatBras = (Options.ALERTE_MAX-Options.ALERTE_MIN)*bras.length;
        int sEtatCapteurs = (Options.ALERTE_MAX-Options.ALERTE_MIN)*capteurs.length;
        int s=0;
        for(int i=0; i<jambes.length; i++){
            s += jambes[i].voyant.getEtat();
        }
        // TODO POUR CE VOYANT ET POUR LES AUTRES
        
        
    }
    
    public void recharger(){
        // TODO 
    }
    public void reparer(int indiceVoyant){
        // TODO quand on aura installé la logique de prix de réparation
    }
    public void actualiseCptKm(Dimension but){
        comptKm += Math.sqrt(but.getWidth()*but.getWidth()+but.getHeight()*but.getHeight());
        kmTot += comptKm;
        
        fatigue();
        
    }
    
    public void fatigue(){
        batterie = Options.BATTERIE_MAX - (int)(comptKm/Options.DELTA_BATTERIE_PAR_KM);
        //TODO : AJOUTER CAS EN FONCTION DU TYPE DE CASE ACTUEL
    }
    public void pbDeclare(int indiceVoyant){
        if(indiceVoyant>=0 && indiceVoyant<tabVoyants.length){
            int etatActuel = tabVoyants[indiceVoyant].getEtat();
            tabVoyants[indiceVoyant].setEtat(etatActuel+1);
        }
        // test GameOver
        int pannes =0;
        for(int i=0;i<tabVoyants.length;i++){
            if(tabVoyants[i].getEtat()==Options.ALERTE_MAX){
                pannes += 1;
            }
        }
        if(pannes>=Options.PANNES_MAX){
            this.GAME_OVER = true;
        }
    }///////////////////////////////////////////////////   REPRENDRE ICI
    /*public boolean pbAleatoire(){
        boolean pb = false;
        if(kmTot>=1000){
            int chance = (int)(Math.random()*100.0) + 1;
            if(chance<= 5
            
            // AJOUTER CAS EN FONCTION DU TYPE DE LA CASE ACTUELLE
        }
        return pb;
    }*/
    
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
    
    @Override
    public void move(){
		if(!movable){
			System.out.println("Le joueur ne peut pas être déplacé");
            return;
        }
		
		updateCoords();
	}

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
        for(int i=0;i<tabVoyants.length;i++){
           tabVoyants[i].setPositionR(Xv+i*100,Yv,25); 
           tabVoyants[i].dessinerVoyant(g);
        }
    }
}
