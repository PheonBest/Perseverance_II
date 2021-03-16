import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.*;
import java.io.*;


public class CSV {
	
        List<String[]> dataLines = new ArrayList<>(); //unique attribut : liste des lignes
        
    public CSV (List<String[]> dL){ //constructeur
		dataLines=dL;
	}
	
	
    public String convertToCSV(String[] data) {
		return Stream.of(data).map(this::escapeSpecialCharacters).collect(Collectors.joining(","));
	}
	
	public String escapeSpecialCharacters(String data) {
		String escapedData = data.replaceAll("\\R", " ");
		if (data.contains(",") || data.contains("\"") || data.contains("'")) {
			data = data.replace("\"", "\"\"");
			escapedData = "\"" + data + "\"";
		}
		return escapedData;
	}
	
	public void givenDataArray_whenConvertToCSV_thenOutputCreated() throws IOException {
		File csvOutputFile = new File("testCSV.csv");
		try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
			dataLines.stream().map(this::convertToCSV).forEach(pw::println);
		}
		if(!csvOutputFile.exists()){
			System.out.println("Erreur:Le fichier n'existe pas");
		}
		System.out.println("coucou");
	}

}
