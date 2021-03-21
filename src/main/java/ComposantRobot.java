public class ComposantRobot{
    
    //------------------------------------------------------------------ Attributs
    
    public String nom;
    public Voyants voyant;
    
    // Pourcentage d'usure
    /* NB : un composant s'use en fonction des activitées menées qui lui sont propres
     * L'usure augmente la probabilité de dégrader siginificativement les composants */
    private int usure;
    
    //------------------------------------------------------------------ Constructeurs
    // Constructeur complet
    public ComposantRobot(String unNom, int unEtat, int uneUsure){
        this.nom = unNom;
        this.voyant = new Voyants(nom, unEtat);
        /* sécurité : si l'état est plus grand que le maximum autorisé,
         * alors on bénéficie de l'état max du voyant*/
        setUsure(uneUsure);
    }
    
    // Constructeur "composant neuf"
    public ComposantRobot(String unNom){
        this.nom = unNom;
        this.voyant = new Voyants(nom);
        /* sécurité : si l'état est plus grand que le maximum autorisé,
         * alors on bénéficie de l'état max du voyant*/
        setUsure(Options.USURE_MIN);
    }
    //------------------------------------------------------------------ Setters et getters
    
    public int getUsure(){
        return this.usure;
    }

    public void setUsure(int u ){
        if (u>=Options.USURE_MIN && u<=Options.USURE_MAX){
            this.usure = u;
        }
        if(u<Options.USURE_MIN) this.usure = Options.USURE_MIN;
        if(u>Options.USURE_MAX) this.usure = Options.USURE_MAX;
    }
    
    //------------------------------------------------------------------ Autres méthodes
    public void degraderC(){
        this.voyant.setEtat(this.voyant.getEtat()+1);
    }
    public void réparerC(){
        this.voyant.setEtat(Options.ALERTE_MIN);
        this.usure = Options.USURE_MIN;
    }

}
