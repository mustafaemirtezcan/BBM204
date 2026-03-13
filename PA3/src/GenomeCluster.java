import java.util.*;

public class GenomeCluster {
    public Map<String, Genome> genomeMap = new HashMap<>();

    public void addGenome(Genome genome) {
        genomeMap.put(genome.id, genome);
    }

    public boolean contains(String genomeId) {
        if (genomeMap.containsKey(genomeId)) {
            return true;
        }
        return false;
    }

    public Genome getMinEvolutionGenome() {
        int minValue=-1;
        Genome minGenome = null;
        for (Genome genome : genomeMap.values()) {
            if (minValue == -1 || genome.evolutionFactor < minValue) {
                minValue = genome.evolutionFactor;
                minGenome = genome;
            }
        }
        return minGenome;
    }

    public int dijkstra(String startId, String endId) {
        Map<String, Integer> adaptationPathDistances = new HashMap<>();
        for (String genomeId : genomeMap.keySet()) {
            adaptationPathDistances.put(genomeId,-1);
        }
        adaptationPathDistances.put(startId, 0);
        PriorityQueue<Genome.Link> adaptationQueue = new PriorityQueue<>(Comparator.comparingInt(link -> link.adaptationFactor));
        adaptationQueue.add(new Genome.Link(startId, 0));

        Set <String> visited = new HashSet<>();

        while (!adaptationQueue.isEmpty()) {
            Genome.Link currentLink = adaptationQueue.remove();
            String currentGenomeId = currentLink.target;
            if (visited.contains(currentGenomeId)) {
                continue;
            }
            visited.add(currentGenomeId);

            Genome currentGenome = genomeMap.get(currentGenomeId);
            for (Genome.Link link : currentGenome.links) {
                if (!visited.contains(link.target)) {
                    int newAdaptationFactor = adaptationPathDistances.get(currentGenomeId) + link.adaptationFactor;
                    if (newAdaptationFactor < adaptationPathDistances.get(link.target)|| adaptationPathDistances.get(link.target) == -1) {
                        adaptationPathDistances.put(link.target, newAdaptationFactor);
                        adaptationQueue.add(new Genome.Link(link.target, newAdaptationFactor));
                    }
                }
            }
        }
        if(adaptationPathDistances.containsKey(endId)) {
            return adaptationPathDistances.get(endId);
        }
        return -1;
    }
}
