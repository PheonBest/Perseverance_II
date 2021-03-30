import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.regex.Pattern;

import javax.swing.SwingUtilities;

public class App 
{
    /* On utuilise un EDT: Event Dispatch Thread
     * La plupart des méthodes des objets Swing ne sont pas "thread safe"
     * Si on les invoque depuis plusieurs threads, on risque d'avoir
     * des interférences de thread.h
     * 
     * Une partie des tâches sont événementielles (Actionlistener)
     * Une autre partie est planifiée (repaint, invokeLater, invokeAndWait)
     * Si les tâches à distribuer ne son pas prises en charge,
     * les événements non gérés sont sauvegardés et l'interface
     * Utilisateur ne répond plus.
     */
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Dimension tailleEcran = Toolkit.getDefaultToolkit().getScreenSize();
                int largeur = (int) (tailleEcran.getWidth() * 3 / 4);
                int hauteur = (int) (tailleEcran.getHeight() * 3 / 4);

                Donnees donnees = new Donnees(largeur, hauteur);
                Controleur controleur = new Controleur(donnees);
                Affichage affichage = new Affichage(largeur, hauteur, controleur);
                donnees.addObserver(affichage);
                affichage.initialiser();
                
                //Compétences
                List<BoutonCercle> competences = new LinkedList<BoutonCercle>();
                competences.add(new BoutonCercle(100, 200, 50, "Drone", donnees.getImagesSymboles().get("checkbox")));
                competences.add(new BoutonCercle(100, 320, 50, "Réparation", donnees.getImagesSymboles().get("checkbox")));
                donnees.majCompetences(competences);
                donnees.notifierObserveur(TypeMisAJour.Competences);
                }
        });
        
        
        //test
		/*List<String[]> dataLines = new ArrayList<>();
		dataLines.add(new String[]{ "Terre", "Sable", "Eau","Terre" });
		dataLines.add(new String[]{ "Sable", "Terre", "Eau", "Pierre" });
		CSV print = new CSV(dataLines);
		try {print.givenDataArray_whenConvertToCSV_thenOutputCreated();
			}catch(Exception e){e.printStackTrace();}*/
        /*
		String [][] carte = {{"Terre;coucou", "Sable;null", "Pierre;null"}, {"Sable;vent","Eau;null", "Sable;bacterie"}, {"Terre;null", "Eau;null","Sable;null"}};
		CSV c1 = new CSV(carte);
		
		String videtexture = "type;symbole";
		String [] ds = videtexture.split(";");
		System.out.println(ds[0]+"  "+ds[1]);
		

        Pattern pattern = Pattern.compile("^.*\\b"+Options.NOM_DOSSIER_CARTES+"\\b.*\\.(?:csv)");
        HashMap<String, InputStream> cartes;
        try {
            cartes = ObtenirRessources.getStreamsAndFilenames(pattern, "res/"+Options.NOM_DOSSIER_CARTES+"/");
            for (String i : cartes.keySet())
                System.out.println("Nom du CSV : " + i);

            String [][]records = c1.lecture(cartes.get("testCSV"));
            for(int i=0;i<records.length;i++){
                for(int j=0;j<records[i].length;j++){
                    System.out.print(" "+records[i][j]);
                }
                System.out.println();
            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        */
    }
    
}
