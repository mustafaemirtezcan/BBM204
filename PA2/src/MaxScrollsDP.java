import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class MaxScrollsDP {
    private ArrayList<ArrayList<Integer>> safesDiscovered = new ArrayList<>();
    // Input format will be the same as following:
    // Number of safes
    // [Complexity,Scroll] Pair
    // [Complexity,Scroll] Pair
    // .
    // .
    // .
    // [Complexity,Scroll] Pair
    // See example provided below:
    // 3
    // [5,10]
    // [10,10]
    // [5,20]

    public MaxScrollsDP(ArrayList<ArrayList<Integer>> safesDiscovered) {
        this.safesDiscovered = safesDiscovered;
    }

    public ArrayList<ArrayList<Integer>> getSafesDiscovered() {
        return safesDiscovered;
    }

    public OptimalScrollSolution optimalSafeOpeningAlgorithm() {
        int T=safesDiscovered.size();
        int [][] dp=new int[T+1][5*T+1];
        boolean[][] scrollValue=new boolean[T+1][5*T+1];
        scrollValue[0][0]=true;
        for (int i = 1; i <= T; i++) {
            for (int k = 0; k <= 5*T; k++) {
                if (scrollValue[i-1][k]) {
                    dp[i][k] = Math.max(dp[i][k], dp[i - 1][k]);
                    scrollValue[i][k] = true;
                }
                if (k >= 5&&scrollValue[i-1][k-5]) {
                    dp[i][k] = Math.max(dp[i][k], dp[i - 1][k - 5]);
                    scrollValue[i][k] = true;
                }
                int C = safesDiscovered.get(i-1).get(0);
                int S =safesDiscovered.get(i-1).get(1);
                if (k + C <= 5*T && scrollValue[i-1][k+C] ) {
                    dp[i][k] = Math.max(dp[i][k], dp[i - 1][k + C] + S);
                    scrollValue[i][k] = true;
                }
            }
        }
        int maxScrolls = 0;
        for (int k = 0; k <= 5*T; k++) {
            maxScrolls = Math.max(maxScrolls, dp[T][k]);
        }
        return new OptimalScrollSolution(safesDiscovered, maxScrolls);
    }
}
