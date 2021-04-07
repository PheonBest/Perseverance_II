import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.*;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.awt.Image;
import java.util.regex.Pattern;


public class CSV {
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////Méthodes de de rédaction du fichier CSV
	
    public static String convertToCSV(String[] data) {
		return Stream.of(data).map(CSV::escapeSpecialCharacters).collect(Collectors.joining(","));
	}
	
	public static String escapeSpecialCharacters(String data) {
		String escapedData = data.replaceAll("\\R", " ");
		if (data.contains(",") || data.contains("\"") || data.contains("'")) {
			data = data.replace("\"", "\"\"");
			escapedData = "\"" + data + "\"";
		}
		return escapedData;
	}
	
	public static void givenDataArray_whenConvertToCSV_thenOutputCreated(Cellule[][] carteCellules, String filename, boolean ecrireParDessus) throws IOException {
		List<String[]> data = dataLines (carteCellules);
		File dir = new File(Options.NOM_DOSSIER_CARTES);
    	if (!dir.exists()) dir.mkdirs();

		// Si on est dans le jar, on ne peut pas obtenir le chemin vers un dossier interne
		URL url = Thread.currentThread().getContextClassLoader().getResource(Options.NOM_DOSSIER_SYMBOLE);
		String chemin;
        if (url != null) // Si on n'est pas dans le jar, on obtient la carte en retournant à la racine du projet
			chemin = "./././"+Options.NOM_DOSSIER_CARTES+"/"+filename+".csv";
		else // Si on est dans le jar, on obtient directement la carte
			chemin = Options.NOM_DOSSIER_CARTES+"/"+filename+".csv";
		
		File csvOutputFile = new File(chemin);
		if (!csvOutputFile.exists() || ecrireParDessus) {
			csvOutputFile.createNewFile();
			System.out.println("Ecriture de "+filename+".csv");
			try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
				data.stream().map(CSV::convertToCSV).forEach(pw::println);
			}
			if(!csvOutputFile.exists()){
				System.out.println("Erreur: Le fichier de destination ne peut pas être créé");
			}
		}
	}
	public static void givenDataArray_whenConvertToCSV_thenOutputCreated(Cellule[][] carteCellules, String filename) throws IOException {
		givenDataArray_whenConvertToCSV_thenOutputCreated(carteCellules, filename, false);
	}
	
	public static List<String[]> dataLines (Cellule[][]carte){ // convertit la carte
		List<String[]> dataLines=new ArrayList<>();
		for(int i=0; i<carte.length; i++){
			String[] s= new String[carte[i].length];
			for(int j=0; j<carte[i].length; j++){
				s[j]=carte[i][j].toString();
			}
			dataLines.add(s);
		}
		return dataLines;
	}
	
	
/////////////////////////////////////////////////////////////////////////////////////////////Méthodes de lecture du fichier CSV
	public static Cellule[][] lecture(InputStream csv, int dx, int dy){
		List<List<String>> records = new ArrayList<>();
		try {
			Scanner scanner = new Scanner(csv);
			while (scanner.hasNextLine()) {
				records.add(getRecordFromLine(scanner.nextLine()));
			}
			//scanner.close(); // On veut pouvoir lire le stream une nouvelle fois, donc on ne ferme pas le scanner (fermer le scanner revient à fermer le stream)
		} catch(Exception e){e.printStackTrace();}
		
		Cellule[][] cellules = dataLines(records);
		for (int i=0; i < cellules.length; i++) {
			for (Cellule c: cellules[i]) {
				c.translate(dx, dy); // Décalage
			}
		}
		return cellules;
	}
	
	private static List<String> getRecordFromLine(String line) {
		List<String> values = new ArrayList<String>();
		try (Scanner rowScanner = new Scanner(line)) {
			rowScanner.useDelimiter(",");
			while (rowScanner.hasNext()) {
				values.add(rowScanner.next());
			}
		}
		return values;
	}

	public static Cellule[][] dataLines (List<List<String>> liste){ //methode surchargée qui change la liste de listes en un tableau 2D
		Cellule[][] carte = new Cellule[liste.size()][];
		//Cellule[] cellules = new Cellule[liste.get(0).size()];
		TypeCase type = null;
		TypeSymbole symbole = null;
		for(int i=0;i<liste.size();i++){
			carte[i] = new Cellule[liste.get(i).size()];
			for(int j=0;j<liste.get(i).size();j++){
				
				String [] ds = liste.get(i).get(j).split(";");
				type = null;
				symbole = null;
				for (TypeCase t : TypeCase.values()) {
                    if (t.name().equals(ds[0])) {
						type = t;
                        break;
                    }
                }
				for (TypeSymbole s : TypeSymbole.values()) {
                    if (s.name().equals(ds[1])) {
						symbole = s;
                        break;
                    }
                }
				
				if (symbole == null)
					carte[i][j] = new Cellule(type, i, j, 1, Options.ESPACE_INTER_CASE, Boolean.parseBoolean(ds[2]), new Symbole());
				else {
					//TEST
					// Chargement des symboles
					Pattern pattern = Pattern.compile("^.*\\b"+Options.NOM_DOSSIER_SYMBOLE+"\\b.*\\.(?:jpg|gif|png)");
					final int LARGEUR = (int) (Options.LARGEUR_CASE*0.7);
					final int HAUTEUR = (int) (Options.LARGEUR_CASE*0.7);
					try {
						HashMap<String, Image> imagesSymboles = ObtenirRessources.getImagesAndFilenames(pattern, "res/"+Options.NOM_DOSSIER_SYMBOLE+"/");
						for (String s : imagesSymboles.keySet())
							imagesSymboles.put(s, TailleImage.resizeImage(imagesSymboles.get(s), LARGEUR, HAUTEUR, true));
						carte[i][j] = new Cellule(type, i, j, 1, Options.ESPACE_INTER_CASE, Boolean.parseBoolean(ds[2]), new Symbole(symbole, imagesSymboles.get(TypeSymbole.BOIS.name()), Boolean.parseBoolean(ds[3])));
					} catch (URISyntaxException | IOException e) {
						e.printStackTrace();
					}
					//TEST
					
					//carte[i][j] = new Cellule(type, i, j, 1, Options.ESPACE_INTER_CASE, Boolean.parseBoolean(ds[2]), new Symbole(symbole, null, Boolean.parseBoolean(ds[3])));
				}
			}
		}
		return carte;
	}
}





