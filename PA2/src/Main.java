import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("##Initiate Operation Safe-lock##");
        String scrollPath = args[0];
        ArrayList<ArrayList<Integer>> safesDiscovered = new ArrayList<>();
        Scanner sc1 = new Scanner(new File(scrollPath));
        int numberOfSafes = Integer.parseInt(sc1.nextLine().trim());
        for (int i = 0; i < numberOfSafes; i++) {
            String[] line = sc1.nextLine().trim().split(",");
            ArrayList<Integer> safe = new ArrayList<>();
            safe.add(Integer.parseInt(line[0]));
            safe.add(Integer.parseInt(line[1]));
            safesDiscovered.add(safe);
        }
        sc1.close();
        MaxScrollsDP maxScrollsDP = new MaxScrollsDP(safesDiscovered);
        OptimalScrollSolution optimalScrollSolution = maxScrollsDP.optimalSafeOpeningAlgorithm();
        optimalScrollSolution.printSolution(optimalScrollSolution);
        System.out.println("##Operation Safe-lock Completed##");
        System.out.println("##Initiate Operation Artifact##");
        String shipPath = args[1];
        ArrayList<Integer> artifactsFound = new ArrayList<>();
        Scanner sc2 = new Scanner(new File(shipPath));
        if (sc2.hasNextLine()) {
            String[] artifacts = sc2.nextLine().trim().split(",");
            for (String artifact : artifacts) {
                artifactsFound.add(Integer.parseInt(artifact));
            }
        }
        sc2.close();
        MinShipsGP minShipsGP = new MinShipsGP(artifactsFound);
        OptimalShipSolution optimalShipSolution = minShipsGP.optimalArtifactCarryingAlgorithm();
        optimalShipSolution.printSolution(optimalShipSolution);
        System.out.print("##Operation Artifact Completed##");
    }
}