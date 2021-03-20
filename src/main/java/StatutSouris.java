public class StatutSouris {

    private int x;
    private int y;
    private boolean clicGauche;
    private boolean clicDroit;

    public int obtenirX() {
        return x;
    }
    public void majX(int x) {
        this.x = x;
    }
    public int obtenirY() {
        return y;
    }
    public void majY(int y) {
        this.y = y;
    }
    public boolean obtenirClicGauche() {
        return clicGauche;
    }
    public void majClicGauche(boolean clicGauche) {
        this.clicGauche = clicGauche;
    }
    public boolean obtenirClicDroit() {
        return clicDroit;
    }
    public void majClicDroit(boolean clicDroit) {
        this.clicDroit = clicDroit;
    }
}