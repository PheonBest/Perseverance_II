import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Voisins {
    
    public static Cellule[] obtenirVoisins(Cellule[][] cellules, int i, int j, int rayon) {
        Cellule[] voisins;
        List<int[]> coordonnees = new LinkedList<int[]>();
        switch (rayon) {
            case 4:
                coordonnees.add(new int[] {i-3,j});
                coordonnees.add(new int[] {i+3,j});

                coordonnees.add(new int[] {i,j+3});
                coordonnees.add(new int[] {i+1,j+3});
                coordonnees.add(new int[] {i-1,j+3});
                coordonnees.add(new int[] {i,j-3});
                coordonnees.add(new int[] {i+1,j-3});
                coordonnees.add(new int[] {i-1,j-3});

                if (j%2 == 0) {
                    coordonnees.add(new int[] {i-2,j-2});
                    coordonnees.add(new int[] {i-2,j-1});
                    coordonnees.add(new int[] {i-2,j+2});
                    coordonnees.add(new int[] {i-2,j+1});
                    coordonnees.add(new int[] {i+2,j-2});
                    coordonnees.add(new int[] {i+3,j-1});
                    coordonnees.add(new int[] {i+2,j+2});
                    coordonnees.add(new int[] {i+3,j+1});
                    coordonnees.add(new int[] {i+2,j-3});
                    coordonnees.add(new int[] {i+2,j+3});
                } else {
                    coordonnees.add(new int[] {i+2,j-2});
                    coordonnees.add(new int[] {i+2,j-1});
                    coordonnees.add(new int[] {i+2,j+2});
                    coordonnees.add(new int[] {i+2,j+1});
                    coordonnees.add(new int[] {i-2,j-2});
                    coordonnees.add(new int[] {i-3,j-1});
                    coordonnees.add(new int[] {i-2,j+2});
                    coordonnees.add(new int[] {i-3,j+1});
                    coordonnees.add(new int[] {i-2,j-3});
                    coordonnees.add(new int[] {i-2,j+3});
                }
            
            case 3:
                coordonnees.add(new int[] {i-2,j});
                coordonnees.add(new int[] {i+2,j});

                coordonnees.add(new int[] {i,j+2});
                coordonnees.add(new int[] {i+1,j+2});
                coordonnees.add(new int[] {i-1,j+2});
                coordonnees.add(new int[] {i,j-2});
                coordonnees.add(new int[] {i+1,j-2});
                coordonnees.add(new int[] {i-1,j-2});
        
                // On inverse le signe des numéros de ligne
                if(j%2==0){
                    coordonnees.add(new int[] {i-1,j+1});
                    coordonnees.add(new int[] {i+2,j+1});
                    coordonnees.add(new int[] {i+2,j-1});
                    coordonnees.add(new int[] {i-1,j-1});
                } else {
                    coordonnees.add(new int[] {i+1,j+1});
                    coordonnees.add(new int[] {i-2,j+1});
                    coordonnees.add(new int[] {i-2,j-1});
                    coordonnees.add(new int[] {i+1,j-1});
                }
            case 2:
                coordonnees.add(new int[] {i-1,j});
                coordonnees.add(new int[] {i+1,j});
                coordonnees.add(new int[] {i,j+1});
                coordonnees.add(new int[] {i,j-1});
                if(j%2==0){
                    coordonnees.add(new int[] {i+1,j+1});
                    coordonnees.add(new int[] {i+1,j-1});
                } else {
                    coordonnees.add(new int[] {i-1,j+1});
                    coordonnees.add(new int[] {i-1,j-1});
                }
            case 1:
                coordonnees.add(new int[] {i,j});
                break;
        }

        for (Iterator<int[]> iterator = coordonnees.iterator(); iterator.hasNext();) {
            int[] cellule = iterator.next();
            if (cellule[0] < 0 || cellule[0] > cellules.length - 1 || cellule[1] < 0 || cellule[1] > cellules[0].length -1)
                // Remove the current element from the iterator and the list.
                iterator.remove();
        }

        voisins = new Cellule[coordonnees.size()];
        int index = 0;

        for (int[] coordsCellule: coordonnees) {
            voisins[index] = cellules[coordsCellule[0]][coordsCellule[1]];
            index ++;
        }

        return voisins;
    }
}
