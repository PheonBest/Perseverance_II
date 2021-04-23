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
	
	public static final String[] extensionsFichierCompresse = {".jar",".exe",".dmg"};
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
	
	public static List<String[]> givenDataArray_whenConvertToCSV_thenOutputCreated(Cellule[][] carteCellules, String filename, boolean ecrireParDessus, Robot joueur, int[] celluleDepart, HashMap<TypeSymbole, Boolean> obtenirSymbolesDecouverts) throws IOException {
		List<String[]> data = dataLines (carteCellules);//carte
		String [] robot;
		if (joueur!= null) {
			robot = new String []{Integer.toString(joueur.getBatterie()), Integer.toString(joueur.getNbRecharges()), Integer.toString(joueur.obtenirCase()[0]), Integer.toString(joueur.obtenirCase()[1]), 
				Integer.toString(joueur.getJambes()[0].voyant.getEtat()), Integer.toString(joueur.getJambes()[1].voyant.getEtat()), Integer.toString(joueur.getBras()[0].voyant.getEtat()), Integer.toString(joueur.getBras()[1].voyant.getEtat()), Integer.toString(joueur.getCapteurs()[0].voyant.getEtat()), Integer.toString(joueur.getCapteurs()[1].voyant.getEtat()), Integer.toString(joueur.getCapteurs()[2].voyant.getEtat()),
					Integer.toString(joueur.getJambes()[0].getUsure()), Integer.toString(joueur.getJambes()[1].getUsure()), Integer.toString(joueur.getBras()[0].getUsure()), Integer.toString(joueur.getBras()[1].getUsure()), Integer.toString(joueur.getCapteurs()[0].getUsure()), Integer.toString(joueur.getCapteurs()[1].getUsure()), Integer.toString(joueur.getCapteurs()[2].getUsure()),
						Integer.toString((int)(joueur.getKmParcourus())), Integer.toString((int)(joueur.getCompteurKm())), Integer.toString(joueur.obtenirNombrePont()), Integer.toString((joueur.obtenirSurChenilles())? 1 : 0), Integer.toString((obtenirSymbolesDecouverts.get("BACTERIE"))? 1 : 0), Integer.toString((obtenirSymbolesDecouverts.get("MINERAI"))? 1 : 0)};
		} else {
			robot = new String []{Integer.toString(Options.BATTERIE_MAX),"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"};
		}

		if (celluleDepart != null) {
			robot[2] = Integer.toString(celluleDepart[0]);
			robot[3] = Integer.toString(celluleDepart[1]);
		}

		data.add(robot);
		CSV.ecrireFichierDepuisCache(filename, data, ecrireParDessus);
		return data;
	}

	public static List<String[]> changeDataFormat(List<List<String>> data) {
		List<String[]> resultat = new ArrayList<>();
		for (List<String> s: data)
			resultat.add(s.toArray(new String[0])); // équivalent à new String[list.size()], mais est plus rapide
		return resultat;
	}

	public static void givenDataArray_whenConvertToCSV_thenOutputCreated(Cellule[][] carteCellules, String filename, Robot joueur, HashMap<TypeSymbole, Boolean> obtenirSymbolesDecouverts) throws IOException {
		givenDataArray_whenConvertToCSV_thenOutputCreated(carteCellules, filename, false, joueur, null, obtenirSymbolesDecouverts);
	}

	// nom: nom du fichier cherché
	// fichierInterne: n'importe quel fichier étant contenu dans le fichier compressé (.exe, .jar, .dmg)
	// cheminVersRoot: Préfixe pour retourner dans le root, ex: "./././"
	public static String fichierExterne(String nom, String fichierInterne, String cheminVersRoot) {
		String url = Thread.currentThread().getContextClassLoader().getResource(fichierInterne).toExternalForm();
		for (String extension: extensionsFichierCompresse) {
			if (url.contains(extension))
				return nom; // Si on est dans un fichier compressé, on retourne le nom
		}
		return cheminVersRoot+nom; // Si on est pas dans un fichier compressé, on retourne au niveau du root
	}
	public static void ecrireFichierDepuisCache(String filename, List<String[]> carte, boolean ecrireParDessus) {
		String dossier = CSV.fichierExterne(Options.NOM_DOSSIER_CARTES, "res/"+Options.NOM_DOSSIER_IMAGES+"/", "src/main/java/");
		String chemin = dossier+"/"+filename+".csv";
		File dir = new File(dossier);
		if (!dir.exists()) dir.mkdirs();
		File csvOutputFile = new File(chemin);
		if (!csvOutputFile.exists() || ecrireParDessus) {
			try {
				csvOutputFile.createNewFile();
			
				PrintWriter pw = new PrintWriter(csvOutputFile);
				carte.stream().map(CSV::convertToCSV).forEach(pw::println);
				pw.close();
				if (!csvOutputFile.exists())
					System.out.println("Erreur: Le fichier de destination ne peut pas être créé");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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
	public static List<String[]> cacheInputStream(InputStream csv) {
		List<List<String>> records = new ArrayList<>();
		try {
			Scanner scanner = new Scanner(csv);
			while (scanner.hasNextLine()) {
				records.add(getRecordFromLine(scanner.nextLine()));
			}
			scanner.close();
		} catch(Exception e){e.printStackTrace();}
		return CSV.changeDataFormat(records);
	}	

	public static Reception lecture(List<String[]> records, int dx, int dy, HashMap<String, Image> images, ArrayList<ArrayList<Image>> imagesJoueur ){

		Reception jeu = dataLines(records,images,imagesJoueur);
		Cellule[][] cellules = jeu.getCellule();
		for (int i=0; i < cellules.length; i++) {
			for (Cellule c: cellules[i])
				c.translate(dx, dy); // Décalage
		}
		jeu.majCarte(cellules);
		return jeu;
	}
	
	private static List<String> getRecordFromLine(String line) {
		List<String> values = new ArrayList<String>();
		try (Scanner rowScanner = new Scanner(line)) {
			rowScanner.useDelimiter(",");
			while (rowScanner.hasNext()) {
				values.add(rowScanner.next());
			}
			rowScanner.close();
		}
		return values;
	}

	public static Reception dataLines (List<String[]> liste, HashMap<String, Image> imagesSymboles, ArrayList<ArrayList<Image>> imagesJoueur){ //methode surchargée qui change la liste de listes en un tableau 2D
		//carte
		Cellule[][] carte = new Cellule[liste.size()-1][];
		TypeCase type = null;
		TypeSymbole symbole = null;
		boolean estVisible = false;
		boolean symboleVisible = false;
		for(int i=0;i<liste.size()-1;i++){
			Cellule[] cellules = new Cellule[liste.get(i).length];
			for(int j=0;j<liste.get(i).length;j++){
				String [] ds = liste.get(i)[j].split(";");
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
		//robot
		int l = liste.size()-1;
		int [] joueur = new int[]{Integer.parseInt(liste.get(l)[0]), Integer.parseInt(liste.get(l)[1]), Integer.parseInt(liste.get(l)[2]), Integer.parseInt(liste.get(l)[3]),
			Integer.parseInt(liste.get(l)[4]), Integer.parseInt(liste.get(l)[5]), Integer.parseInt(liste.get(l)[6]), Integer.parseInt(liste.get(l)[7]), Integer.parseInt(liste.get(l)[8]), Integer.parseInt(liste.get(l)[9]), Integer.parseInt(liste.get(l)[10]),
				Integer.parseInt(liste.get(l)[11]), Integer.parseInt(liste.get(l)[12]), Integer.parseInt(liste.get(l)[13]), Integer.parseInt(liste.get(l)[14]), Integer.parseInt(liste.get(l)[15]), Integer.parseInt(liste.get(l)[16]), Integer.parseInt(liste.get(l)[17]),
					Integer.parseInt(liste.get(l)[18]), Integer.parseInt(liste.get(l)[19]), Integer.parseInt(liste.get(l)[20]), Integer.parseInt(liste.get(l)[21]), Integer.parseInt(liste.get(l)[22]), Integer.parseInt(liste.get(l)[23])};
		Reception jeu = new Reception (carte, joueur, imagesJoueur);
		return jeu;
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





