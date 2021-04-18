import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.*;
import java.util.regex.Pattern;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.awt.Image;


public class CSV {
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////Méthodes de rédaction du fichier CSV
	
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
		URL url = Thread.currentThread().getContextClassLoader().getResource("res/"+Options.NOM_DOSSIER_IMAGES+"/");
		
		// Si on est dans le jar ou exe ou dmg, on obtient directement la carte
		String dossier = Options.NOM_DOSSIER_CARTES;
		String chemin = Options.NOM_DOSSIER_CARTES+"/"+filename+".csv";
        if (!url.toExternalForm().contains(".jar") && !url.toExternalForm().contains(".exe") && !url.toExternalForm().contains(".dmg")) {// Si on n'est pas dans le jar, on obtient la carte en retournant à la racine du projet
			chemin = "./././"+Options.NOM_DOSSIER_CARTES+"/"+filename+".csv";
			dossier = "./././"+Options.NOM_DOSSIER_CARTES;
		}
		System.out.println(url);
		System.out.println(chemin);

		File dir = new File(dossier);
		if (!dir.exists()) dir.mkdirs();

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
	public static Cellule[][] lecture(InputStream csv, int dx, int dy, HashMap<String, Image> images ){
		List<List<String>> records = new ArrayList<>();
		try {
			Scanner scanner = new Scanner(csv);
			while (scanner.hasNextLine()) {
				records.add(getRecordFromLine(scanner.nextLine()));
			}
			//scanner.close(); // On veut pouvoir lire le stream une nouvelle fois, donc on ne ferme pas le scanner (fermer le scanner revient à fermer le stream)
		} catch(Exception e){e.printStackTrace();}
		
		Cellule[][] cellules = dataLines(records, images);
		for (int i=0; i < cellules.length; i++) {
			for (Cellule c: cellules[i])
				c.translate(dx, dy); // Décalage
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

	public static Cellule[][] dataLines (List<List<String>> liste, HashMap<String, Image> imagesSymboles){ //methode surchargée qui change la liste de listes en un tableau 2D
		Cellule[][] carte = new Cellule[liste.size()][];
		TypeCase type = null;
		TypeSymbole symbole = null;
		boolean estVisible = false;
		boolean symboleVisible = false;
		for(int i=0;i<liste.size();i++){
			Cellule[] cellules = new Cellule[liste.get(i).size()];
			for(int j=0;j<liste.get(i).size();j++){
				String [] ds = liste.get(i).get(j).split(";");
				symbole = null;
				type = null;
				for (TypeCase t : TypeCase.values()) {   //lecture du type cellule
					if (t.name().equals(ds[0])) {
						type = t;
						break;
					}
				}
				for(TypeSymbole s : TypeSymbole.values()){  // lecture du type symbole
					if(s.name().equals(ds[1])) {
						symbole = s;
						break;
					}
				}
				
				estVisible = Boolean.parseBoolean(ds[2]);
				symboleVisible = Boolean.parseBoolean(ds[3]);
				Image imageSym = imagesSymboles.get(symbole.name());
			
				//Image imageSym = obtenirImageSymbole (""+symbole, imagesSymboles);
				cellules[j] = new Cellule(type, i, j, 1, Options.ESPACE_INTER_CASE, estVisible, new Symbole(symbole, imageSym, symboleVisible));
			}
			carte[i]=cellules;
		}
		return carte;
	}
	
	public static Image obtenirImageSymbole(String nomSymbole, HashMap<String, Image> images){  //trouver l'image associee a chaque symbole
		switch( nomSymbole){
			case "BACTERIE":
				return images.get("BACTERIE");
			case "MINERAI":
				return images.get("MINERAI"); // correspondance type de hashmap
			case "RAVIN":
				return images.get("RAVIN");
			case "SCANNER":
				return images.get("SCANNER");
			case "GRAPPIN":
				return images.get("GRAPPIN");
			case "INCONNUE":
				return images.get("INCONNUE");
			case "JAMBE":
				return images.get("JAMBE");
			case "BRAS":
				return images.get("BRAS");
			case "CAPTEUR":
				return images.get("CAPTEUR");
			case "BOIS":
				return images.get("BOIS");
			case "PONT":
				return images.get("PONT");
			case "ENERGIE":
				return images.get("ENERGIE");
			case "CHENILLES":
				return images.get("CHENILLES");
			case "MONTAGNE":
				return images.get("MONTAGNE");
			case "FUSEE":
				return images.get("FUSEE");
			default:
				return null;
		}
	}
}





