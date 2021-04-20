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
	
	public static void givenDataArray_whenConvertToCSV_thenOutputCreated(Cellule[][] carteCellules, String filename, boolean ecrireParDessus, Robot joueur, int[] celluleDepart) throws IOException {
		List<String[]> data = dataLines (carteCellules);//carte
		String [] robot;
		if (joueur!= null) {
			robot = new String []{Integer.toString(joueur.getBatterie()), Integer.toString(joueur.getNbRecharges()), Integer.toString(joueur.obtenirCase()[0]), Integer.toString(joueur.obtenirCase()[1]), 
				Integer.toString(joueur.getJambes()[0].voyant.getEtat()), Integer.toString(joueur.getJambes()[1].voyant.getEtat()), Integer.toString(joueur.getBras()[0].voyant.getEtat()), Integer.toString(joueur.getBras()[1].voyant.getEtat()), Integer.toString(joueur.getCapteurs()[0].voyant.getEtat()), Integer.toString(joueur.getCapteurs()[1].voyant.getEtat()), Integer.toString(joueur.getCapteurs()[2].voyant.getEtat()),
					Integer.toString(joueur.getJambes()[0].getUsure()), Integer.toString(joueur.getJambes()[1].getUsure()), Integer.toString(joueur.getBras()[0].getUsure()), Integer.toString(joueur.getBras()[1].getUsure()), Integer.toString(joueur.getCapteurs()[0].getUsure()), Integer.toString(joueur.getCapteurs()[1].getUsure()), Integer.toString(joueur.getCapteurs()[2].getUsure()),
						Integer.toString((int)(joueur.getKmParcourus())), Integer.toString((int)(joueur.getCompteurKm())), Integer.toString(joueur.obtenirNombrePont()), Integer.toString((joueur.obtenirSurChenilles())? 1 : 0)};
		} else {
			robot = new String []{Integer.toString(Options.BATTERIE_MAX),"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"};
		}

		if (celluleDepart != null) {
			robot[2] = Integer.toString(celluleDepart[0]);
			robot[3] = Integer.toString(celluleDepart[1]);
		}

		data.add(robot);

		String dossier = CSV.fichierExterne(Options.NOM_DOSSIER_CARTES, "res/"+Options.NOM_DOSSIER_IMAGES+"/", "./././");
		String chemin = dossier+"/"+filename+".csv";

		System.out.println(dossier);

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
	public static void givenDataArray_whenConvertToCSV_thenOutputCreated(Cellule[][] carteCellules, String filename, Robot joueur) throws IOException {
		givenDataArray_whenConvertToCSV_thenOutputCreated(carteCellules, filename, false, joueur, null);
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
	public static void ecrireFichierDepuisFlux(String filename, InputStream carte) {
		String dossier = CSV.fichierExterne(Options.NOM_DOSSIER_CARTES, "res/"+Options.NOM_DOSSIER_IMAGES+"/", "./././");
		String chemin = dossier+"/"+filename+".csv";
		File dir = new File(dossier);
		if (!dir.exists()) dir.mkdirs();
		File csvOutputFile = new File(chemin);
        try {
            csvOutputFile.createNewFile();
            System.out.println("Ecriture de "+filename+".csv");
            byte[] buffer = new byte[carte.available()];
            carte.read(buffer);
            OutputStream outStream = new FileOutputStream(csvOutputFile);
            outStream.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(!csvOutputFile.exists()){
            System.out.println("Erreur: Le fichier de destination ne peut pas être créé");
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
	public static Reception lecture(InputStream csv, int dx, int dy, HashMap<String, Image> images, ArrayList<ArrayList<Image>> imagesJoueur ){
		List<List<String>> records = new ArrayList<>();
		try {
			Scanner scanner = new Scanner(csv);
			while (scanner.hasNextLine()) {
				records.add(getRecordFromLine(scanner.nextLine()));
			}
			scanner.close();
		} catch(Exception e){e.printStackTrace();}
		
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

	public static Reception dataLines (List<List<String>> liste, HashMap<String, Image> imagesSymboles, ArrayList<ArrayList<Image>> imagesJoueur){ //methode surchargée qui change la liste de listes en un tableau 2D
		//carte
		Cellule[][] carte = new Cellule[liste.size()-1][];
		TypeCase type = null;
		TypeSymbole symbole = null;
		boolean estVisible = false;
		boolean symboleVisible = false;
		for(int i=0;i<liste.size()-1;i++){
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
		//robot
		int l = liste.size()-1;
		int [] joueur = new int[]{Integer.parseInt(liste.get(l).get(0)), Integer.parseInt(liste.get(l).get(1)), Integer.parseInt(liste.get(l).get(2)), Integer.parseInt(liste.get(l).get(3)),
			Integer.parseInt(liste.get(l).get(4)), Integer.parseInt(liste.get(l).get(5)), Integer.parseInt(liste.get(l).get(6)), Integer.parseInt(liste.get(l).get(7)), Integer.parseInt(liste.get(l).get(8)), Integer.parseInt(liste.get(l).get(9)), Integer.parseInt(liste.get(l).get(10)),
				Integer.parseInt(liste.get(l).get(11)), Integer.parseInt(liste.get(l).get(12)), Integer.parseInt(liste.get(l).get(13)), Integer.parseInt(liste.get(l).get(14)), Integer.parseInt(liste.get(l).get(15)), Integer.parseInt(liste.get(l).get(16)), Integer.parseInt(liste.get(l).get(17)),
					Integer.parseInt(liste.get(l).get(18)), Integer.parseInt(liste.get(l).get(19)), Integer.parseInt(liste.get(l).get(20)), Integer.parseInt(liste.get(l).get(21))};
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





