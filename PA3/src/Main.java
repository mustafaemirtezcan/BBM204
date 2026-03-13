import java.io.File;

public class Main {
    public static void main(String[] args) {
        AlienFlora alienFlora = new AlienFlora(new File(args[0]));
        alienFlora.readGenomes();
        alienFlora.evaluateEvolutions();
        alienFlora.evaluateAdaptations();
    }
}
