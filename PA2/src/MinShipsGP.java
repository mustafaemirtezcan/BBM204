import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class MinShipsGP {
    private final ArrayList<Integer> artifactsFound = new ArrayList<>();
    // Weight of artifacts as list will be provided in the input file, and the list
    // should be populated using this format.
    // [3,2,3,4,5,4]

    public ArrayList<Integer> getArtifactsFound() {
        return artifactsFound;
    }

    MinShipsGP(ArrayList<Integer> artifactsFound) {
        this.artifactsFound.addAll(artifactsFound);
    }

    public OptimalShipSolution optimalArtifactCarryingAlgorithm()  {
        ArrayList<Integer> reverseSortedArtifactsFound = new ArrayList<>(artifactsFound);
        reverseSortedArtifactsFound.sort(Collections.reverseOrder());
        ArrayList<Integer> spaceShips = new ArrayList<>();
        int i=0;
        while(i<reverseSortedArtifactsFound.size()){
            boolean found = false;
            for (int j=0;j<spaceShips.size();j++){
                if (100-spaceShips.get(j) >= reverseSortedArtifactsFound.get(i)){
                    spaceShips.set(j,spaceShips.get(j)+reverseSortedArtifactsFound.get(i));
                    found = true;
                    break;
                }
            }
            if (!found){
                spaceShips.add(reverseSortedArtifactsFound.get(i));
            }
            i++;
        }
        int solution = spaceShips.size();
        return  new OptimalShipSolution(artifactsFound, solution);
    }
}
