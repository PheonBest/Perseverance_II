public class ComposantRobot{
    
    //------------------------------------------------------------------ Attributs
    
    public String nom;
    public Voyants voyant;
    
    // Pourcentage d'usure
    private int usure;
    
    //------------------------------------------------------------------ Constructeurs
    
    public ComposantRobot(String unNom, int unEtat, int uneUsure){
        this.nom = unNom;
        this.voyant = new Voyants(nom, unEtat);
        /* sécurité : si l'état est plus grand que le maximum autorisé,
         * alors on bénéficie de l'état max du voyant*/
        setUsure(uneUsure);
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

}
