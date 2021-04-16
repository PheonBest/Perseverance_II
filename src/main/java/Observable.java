public interface Observable {
    public void ajouterObservateur(Observateur obs);
    public void enleverObservateur();
    public void notifierObservateur(TypeMisAJour type);
}