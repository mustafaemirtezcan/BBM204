import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.util.*;

public class AlienFlora {
    private File xmlFile;

    public AlienFlora(File xmlFile) {
        this.xmlFile = xmlFile;
    }
    List<GenomeCluster> clusters = new ArrayList<>();
    public void readGenomes() {
        try {
            System.out.println("##Start Reading Flora Genomes##");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document alienFlora = builder.parse(xmlFile);

            NodeList genomes = alienFlora.getElementsByTagName("genome");
            Map<String, Genome> allGenomes = new HashMap<>();

            for (int i=0;i<genomes.getLength();i++) {
                Element genomeInfo = (Element) genomes.item(i);
                String id = genomeInfo.getElementsByTagName("id").item(0).getTextContent();
                int evolutionFactor = Integer.parseInt(genomeInfo.getElementsByTagName("evolutionFactor").item(0).getTextContent());
                Genome genome = new Genome(id, evolutionFactor);
                allGenomes.put(id, genome);
            }
            for (int i=0;i<genomes.getLength();i++) {
                Element genomeInfo = (Element) genomes.item(i);
                String id = genomeInfo.getElementsByTagName("id").item(0).getTextContent();
                Genome genome = allGenomes.get(id);
                NodeList links = genomeInfo.getElementsByTagName("link");
                for (int j = 0; j < links.getLength(); j++) {
                    Element linkElement = (Element) links.item(j);
                    String target = linkElement.getElementsByTagName("target").item(0).getTextContent();
                    int adaptationFactor = Integer.parseInt(linkElement.getElementsByTagName("adaptationFactor").item(0).getTextContent());
                    genome.addLink(target, adaptationFactor);
                    if (allGenomes.containsKey(target)) {
                        Genome targetGenome = allGenomes.get(target);
                        targetGenome.addLink(id, adaptationFactor);
                    }
                }
            }
            Set <String> visited = new HashSet<>();
            for (String id : allGenomes.keySet()) {
                if(!visited.contains(id)){
                    GenomeCluster cluster = new GenomeCluster();
                    dfs(id,allGenomes,visited,cluster);
                    clusters.add(cluster);
                }
            }
            List<List<String>> clustersArray = new ArrayList<>();
            for (GenomeCluster cluster : clusters) {
                List<String> genomeIds = new ArrayList<>();
                for (String genomeId : cluster.genomeMap.keySet()) {
                    genomeIds.add(genomeId);
                }
                clustersArray.add(genomeIds);
            }
            System.out.println("Number of Genome Clusters: " + clusters.size()+"\nFor the Genomes: "+ clustersArray+"\n##Reading Flora Genomes Completed##");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dfs(String genomeId, Map<String, Genome> genomes, Set<String> visited, GenomeCluster cluster) {
        visited.add(genomeId);
        Genome genome = genomes.get(genomeId);
        cluster.addGenome(genome);
        for (Genome.Link neighbor : genome.links) {
            if (!visited.contains(neighbor.target)) {
                dfs(neighbor.target,genomes, visited, cluster);
            }
        }
    }


    public void evaluateEvolutions() {
        try {
            System.out.println("##Start Evaluating Possible Evolutions##");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document alienFlora = builder.parse(xmlFile);

            NodeList evolution = alienFlora.getElementsByTagName("possibleEvolutionPairs");
            Element evolutionElement = (Element) evolution.item(0);
            NodeList pairs = evolutionElement.getElementsByTagName("pair");
            int numPairs = pairs.getLength();
            System.out.println("Number of Possible Evolutions: "+ numPairs);
            List<Double> evolutionPairs = new ArrayList<>();
            for (int i = 0; i < pairs.getLength(); i++) {
                Element pairElement = (Element) pairs.item(i);
                String genomeId1 = pairElement.getElementsByTagName("firstId").item(0).getTextContent();
                String genomeId2 = pairElement.getElementsByTagName("secondId").item(0).getTextContent();

                double min1=0.0;
                double min2=0.0;
                boolean notCertified = false;
                for(GenomeCluster cluster : clusters) {
                    if(cluster.contains(genomeId1) && cluster.contains(genomeId2)){
                        evolutionPairs.add(-1.0);
                        notCertified = true;
                        break;
                    }
                    else if(cluster.contains(genomeId1)){
                        min1=cluster.getMinEvolutionGenome().evolutionFactor;
                    }
                    else if (cluster.contains(genomeId2)){
                        min2=cluster.getMinEvolutionGenome().evolutionFactor;
                    }
                }
                if (notCertified) {
                    numPairs--;
                }
                else{
                evolutionPairs.add((min1+min2)/2.0);}
            }
            System.out.println("Number of Certified Evolution: "+ numPairs+"\nEvolution Factor for Each Evolution Pair: "+ evolutionPairs);
            System.out.println("##Evaluated Possible Evolutions##");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void evaluateAdaptations() {
        try {
            System.out.println("##Start Evaluating Possible Adaptations##");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document alienFlora = builder.parse(xmlFile);

            NodeList adaptation = alienFlora.getElementsByTagName("possibleAdaptationPairs");
            Element adaptationElement = (Element) adaptation.item(0);
            NodeList pairs = adaptationElement.getElementsByTagName("pair");
            int numPairs = pairs.getLength();
            System.out.println("Number of Possible Adaptations: "+ numPairs);
            List<Integer> adaptationPairs = new ArrayList<>();
            for (int i = 0; i < pairs.getLength(); i++) {
                Element pairElement = (Element) pairs.item(i);
                NodeList firstIdList = pairElement.getElementsByTagName("firstId");
                NodeList secondIdList = pairElement.getElementsByTagName("secondId");

                if (firstIdList.getLength() > 0 && secondIdList.getLength() > 0) {
                    String genomeId1 = firstIdList.item(0).getTextContent();
                    String genomeId2 = secondIdList.item(0).getTextContent();

                boolean certified = false;
                for (GenomeCluster cluster : clusters) {
                    if (cluster.contains(genomeId1) && cluster.contains(genomeId2)) {
                        adaptationPairs.add(cluster.dijkstra(genomeId1, genomeId2));
                        certified = true;
                        break;
                    }
                }
                if (!certified) {
                    adaptationPairs.add(-1);
                    numPairs--;
                }
                }
            }
        System.out.println("Number of Certified Adaptations: "+ numPairs+"\nAdaptation Factor for Each Adaptation Pair: "+ adaptationPairs);
        System.out.println("##Evaluated Possible Adaptations##");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
