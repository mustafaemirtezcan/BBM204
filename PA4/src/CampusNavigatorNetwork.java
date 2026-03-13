import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CampusNavigatorNetwork implements Serializable {
    static final long serialVersionUID = 11L;
    public double averageCartSpeed;
    public final double averageWalkingSpeed = 1000 / 6.0;
    public int numCartLines;
    public Station startPoint;
    public Station destinationPoint;
    public List<CartLine> lines;

    /**
     * Write the necessary Regular Expression to extract string constants from the fileContent
     * @return the result as String
     */
    public String getStringVar(String varName, String fileContent) {
        Pattern p = Pattern.compile("[\\t ]*" + varName + "[\\t ]*=[\\t ]*\"([^\"]*)\"");
        Matcher m = p.matcher(fileContent);
        m.find();
        String result = m.group(1);
        if (result != null) {
            return result;
        }
        return "";
    }

    /**
     * Write the necessary Regular Expression to extract floating point numbers from the fileContent
     * Your regular expression should support floating point numbers with an arbitrary number of
     * decimals or without any (e.g. 5, 5.2, 5.02, 5.0002, etc.).
     * @return the result as Double
     */
    public Double getDoubleVar(String varName, String fileContent) {
        Pattern p = Pattern.compile("[\\t ]*" + varName + "[\\t ]*=[\\t ]*([0-9]+\\.?[0-9]*)");
        Matcher m = p.matcher(fileContent);
        m.find();
        Double result = Double.parseDouble(m.group(1));
        if (result != null) {
            return result;
        }
        return 0.0;
    }

    public int getIntVar(String varName, String fileContent) {
        Pattern p = Pattern.compile("[\\t ]*" + varName + "[\\t ]*=[\\t ]*([0-9]+)");
        Matcher m = p.matcher(fileContent);
        m.find();
        return Integer.parseInt(m.group(1));
    }

    /**
     * Write the necessary Regular Expression to extract a Point object from the fileContent
     * points are given as an x and y coordinate pair surrounded by parentheses and separated by a comma
     * @return the result as a Point object
     */
    public Point getPointVar(String varName, String fileContent) {
        Point p = new Point(0, 0);
        Pattern p1 = Pattern.compile("[\\t ]*" + varName + "[\\t ]*=[\\t ]*\\(\\s*([0-9]+)\\s*,\\s*([0-9]+)\\s*\\)");
        Matcher m = p1.matcher(fileContent);
        if (m.find()) {
            String x = m.group(1);
            String y = m.group(2);
            if (x != null && y != null) {
                p.x = Integer.parseInt(x.trim());
                p.y = Integer.parseInt(y.trim());
            }
        }
        return p;
    }

    /**
     * Function to extract the cart lines from the fileContent by reading train line names and their
     * respective stations.
     * @return List of CartLine instances
     */
    public List<CartLine> getCartLines(String fileContent) {
        List<CartLine> cartLines = new ArrayList<>();
        Pattern p = Pattern.compile("cart_line_name\\s*=\\s*\"([^\"]+)\"\\s*cart_line_stations\\s*=\\s*((?:\\(\\s*\\d+\\s*,\\s*\\d+\\s*\\)\\s*)+)");
        Matcher m = p.matcher(fileContent);
        while (m.find()) {
            String lineContent = m.group(0);
            String lineName = getStringVar("cart_line_name", lineContent);
            String stationsGroup = m.group(2);
            List<Station> stations = new ArrayList<>();
            Pattern p1 = Pattern.compile("\\(\\s*(\\d+)\\s*,\\s*(\\d+)\\s*\\)");
            Matcher m1 = p1.matcher(stationsGroup);
            int count = 1;
            while (m1.find()) {
                int x = Integer.parseInt(m1.group(1));
                int y = Integer.parseInt(m1.group(2));
                Point point = new Point(x, y);
                Station s = new Station(point, lineName + " Station " + count);
                stations.add(s);
                count++;
            }
            cartLines.add(new CartLine(lineName, stations));
        }
        return cartLines;
    }

    /**
     * Function to populate the given instance variables of this class by calling the functions above.
     */
    public void readInput(String filename) {
        try {
            Scanner sc = new Scanner(new File(filename));
            StringBuilder fileContent = new StringBuilder();
            while (sc.hasNextLine()) {
                fileContent.append(sc.nextLine()).append("\n");
            }
            String content = fileContent.toString();
            averageCartSpeed = getDoubleVar("average_cart_speed", content) * 100 / 6.0;
            numCartLines = getIntVar("num_cart_lines", content);
            startPoint = (new Station(getPointVar("starting_point", content),"Starting Point" ));
            destinationPoint = (new Station(getPointVar("destination_point", content), "Final Destination"));
            lines = getCartLines(content);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
