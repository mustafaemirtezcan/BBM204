import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileIO {
    public static List<Integer> readThirdColumn(String csvFile, int inputSize) {
        List<Integer> columnData = new ArrayList<>();
        String line;
        String csvSplitBy = ",";
        int count = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            br.readLine();
            while ((line = br.readLine()) != null && count < inputSize) {
                String[] data = line.split(csvSplitBy);
                if (data.length > 2) {
                    columnData.add(Integer.parseInt(data[2]));
                }
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return columnData;
    }
}
