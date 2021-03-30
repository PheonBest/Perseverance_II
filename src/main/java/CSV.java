import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;


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
	
	public static void givenDataArray_whenConvertToCSV_thenOutputCreated(List<String[]> data, String filename) throws IOException {
		File dir = new File(Options.NOM_DOSSIER_CARTES);
    	if (!dir.exists()) dir.mkdirs();
		File csvOutputFile = new File(Options.NOM_DOSSIER_CARTES+"/"+filename+".csv");
		csvOutputFile.createNewFile();
		try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
			data.stream().map(CSV::convertToCSV).forEach(pw::println);
		}
		if(!csvOutputFile.exists()){
			System.out.println("Erreur:Le fichier de destination ne peut pas être créé");
		}
	}
	
	public static List<String[]> dataLines (String[][]carte){ // convertit la carte
		List<String[]> dataLines=new ArrayList<>();
		for(int i=0; i<carte.length; i++){
			String[] s= new String[carte[i].length];
			for(int j=0; j<carte[i].length; j++){
				s[j]=carte[i][j];
			}
			dataLines.add(s);
		}
		return dataLines;
	}
	
	
/////////////////////////////////////////////////////////////////////////////////////////////Méthodes de lecture du fichier CSV
	public static String[][] lecture(InputStream csv){
		List<List<String>> records = new ArrayList<>();
		try {
			Scanner scanner = new Scanner(csv);
			while (scanner.hasNextLine()) {
				records.add(getRecordFromLine(scanner.nextLine()));
			}
			scanner.close();
		}catch(Exception e){e.printStackTrace();}
		return dataLines(records);	
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

	public static String[][] dataLines (List<List<String>> liste){ //methode surchargée qui change la liste de listes en un tableau 2D
		String[][] carte = new String[liste.size()][];
		for(int i=0;i<liste.size();i++){
			String[] s = new String[liste.get(i).size()];
			for(int j=0;j<liste.get(i).size();j++){
				s[j]=liste.get(i).get(j);
			}
			carte[i]=s;
		}
		return carte;
	}
}





