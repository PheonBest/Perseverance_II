import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.*;
import java.io.*;


public class CSV {
	
	
	
        private List<String[]> dataLines = new ArrayList<>(); // liste des lignes
        private String [][] carte; // peu utile, peu être supprimée
        
        
        
        
    public CSV (String [][] CARTE){ //constructeur prend une carte et remplit le fichier en .csv
		carte=CARTE;
		dataLines =this.dataLines(CARTE);
		try {this.givenDataArray_whenConvertToCSV_thenOutputCreated();
			}catch(Exception e){e.printStackTrace();}
		
	}
	
	
	
	//Méthodes de de rédaction du fichier CSV
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
	}
	
	public List<String[]> dataLines (String[][]carte){
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
	
	
	//Méthodes de lecture du fichier CSV
	
	




}
