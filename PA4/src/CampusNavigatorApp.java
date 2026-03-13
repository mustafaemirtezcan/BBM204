import java.io.Serializable;
import java.util.*;

public class CampusNavigatorApp implements Serializable {
    static final long serialVersionUID = 99L;

    public HashMap<Station, Station> predecessors = new HashMap<>();
    public HashMap<Set<Station>, Double> times = new HashMap<>();

    public CampusNavigatorNetwork readCampusNavigatorNetwork(String filename) {
        CampusNavigatorNetwork network = new CampusNavigatorNetwork();
        network.readInput(filename);
        return network;
    }

    /**
     * Calculates the fastest route from the user's selected starting point to 
     * the desired destination, using the campus golf cart network and walking paths.
     * @return List of RouteDirection instances
     */
    public List<RouteDirection> getFastestRouteDirections(CampusNavigatorNetwork network) {
        List<RouteDirection> routeDirections = new ArrayList<>();
        Station start = network.startPoint;
        Station destination = network.destinationPoint;
        Set<Station> allStations = new HashSet<>();
        for (CartLine line : network.lines) {
            allStations.addAll(line.cartLineStations);
        }
        allStations.add(start);
        allStations.add(destination);
        for (Station station1 : allStations) {
            for (Station station2 : allStations) {
                if (!station1.equals(station2)) {
                    boolean isCartRide = isCartLine(station1, station2, network.lines);
                    double timeBetween = timeBetween(station1, station2,isCartRide ? network.averageCartSpeed : network.averageWalkingSpeed);
                    Set<Station> tempPair = new HashSet<>(Arrays.asList(station1, station2));
                    times.put(tempPair, timeBetween);
                }
            }
        }
        Map<Station, Double> elapsedTime = new HashMap<>();
        for (Station station : allStations) {
            elapsedTime.put(station, -1.0);
        }
        elapsedTime.put(start, 0.0);
        PriorityQueue<Station> stationQueue = new PriorityQueue<>(Comparator.comparingDouble(elapsedTime::get));
        stationQueue.add(start);
        while (!stationQueue.isEmpty()) {
            Station current = stationQueue.remove();
            for (CartLine line : network.lines) {
                List<Station> stations = line.cartLineStations;
                for (int i = 0; i < stations.size(); i++) {
                    Station u = stations.get(i);
                    if (!u.equals(current)) {
                        continue;
                    }
                    if (i < stations.size() - 1) {
                        Station cartLineNeighbor = stations.get(i + 1);
                        updateTime(current, cartLineNeighbor, times.get(new HashSet<>(Arrays.asList(current, cartLineNeighbor))), elapsedTime, predecessors, stationQueue);
                    }
                    if (i > 0) {
                        Station cartLineNeighbor = stations.get(i - 1);
                        updateTime(current, cartLineNeighbor, times.get(new HashSet<>(Arrays.asList(current, cartLineNeighbor))), elapsedTime, predecessors, stationQueue);
                    }
                }
            }
            for (Station walkNeighbor : allStations) {
                if (walkNeighbor.equals(current)) continue;
                updateTime(current, walkNeighbor, times.get(new HashSet<>(Arrays.asList(current, walkNeighbor))), elapsedTime, predecessors, stationQueue);
            }
        }
        List<Station> route = new ArrayList<>();
        Station temp = destination;
        route.add(temp);
        while (predecessors.containsKey(temp)) {
            temp = predecessors.get(temp);
            route.add(temp);
        }
        Collections.reverse(route);
        for (int i = 0; i < route.size() - 1; i++) {
            Station from = route.get(i);
            Station to = route.get(i + 1);
            boolean isCartRide = isCartLine(from, to, network.lines);
            double duration = timeBetween(from, to, isCartRide ? network.averageCartSpeed : network.averageWalkingSpeed);
            routeDirections.add(new RouteDirection(from.description, to.description, duration, isCartRide));
        }
        return routeDirections;
    }
    private boolean isCartLine(Station a, Station b, List<CartLine> lines) {
        for (CartLine line : lines) {
            List<Station> stations = line.cartLineStations;
            for (int i = 0; i < stations.size() - 1; i++) {
                if ((stations.get(i).equals(a) && stations.get(i + 1).equals(b)) || (stations.get(i).equals(b) && stations.get(i + 1).equals(a))) {
                    return true;
                }
            }
        }
        return false;
    }
    private void updateTime(Station A, Station B, double timeBetween, Map<Station, Double> elapsedTime, Map<Station, Station> predecessors, PriorityQueue<Station> pq) {
        if (elapsedTime.get(B) == -1.0 || (elapsedTime.get(A) + timeBetween < elapsedTime.get(B))) {
            elapsedTime.put(B, elapsedTime.get(A) + timeBetween);
            predecessors.put(B, A);
            pq.remove(B);
            pq.add(B);
        }
    }
    private double timeBetween(Station A, Station B, double speed) {
        double  diffX=A.coordinates.x-B.coordinates.x;
        double  diffY=A.coordinates.y-B.coordinates.y;
        double  distance=Math.sqrt(diffX*diffX+diffY*diffY);
        return distance/speed;
    }

    /**
     * Function to print the route directions to STDOUT
     */
    public void printRouteDirections(List<RouteDirection> directions) {
        double minutes = 0.0;
        for (RouteDirection direction : directions) {
            minutes += direction.duration;
        }
        System.out.printf("The fastest route takes %d minute(s).%n", Math.round(minutes));
        System.out.println("Directions");
        System.out.println("----------");
        int count = 1;
        for (RouteDirection direction : directions) {
            String mode = direction.cartRide ? "Ride the cart" : "Walk";
            System.out.printf("%d. %s from \"%s\" to \"%s\" for %.2f minutes.%n", count++, mode, direction.startStationName, direction.endStationName, direction.duration);
        }
    }
}
