package csi2510_project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * @author Yahya Alaa
 * @author Ben Herweyer
 */
public class GraphReader {
    List<Integer> nodes;
    Map<Integer, List<Integer>> edges;

    public Graph read(String edgesFilename) throws IOException {
        System.getProperty("user.dir");
        URL edgesPath = CSI2510.class.getResource(edgesFilename);
        BufferedReader csvReader = new BufferedReader(new FileReader(edgesPath.getFile()));
        String row;
        edges = new HashMap<>();

        boolean first = false;
        while ((row = csvReader.readLine()) != null) {
            if (!first) {
                first = true;
                continue;
            }

            String[] data = row.split(",");

            Integer u = Integer.parseInt(data[0]);
            Integer v = Integer.parseInt(data[1]);

            List<Integer> edgeList = edges.computeIfAbsent(u, x -> new ArrayList<>());
            if (!edgeList.contains(v)) {
                edgeList.add(v);
            }
            edges.computeIfAbsent(v, x -> new ArrayList<>());
        }

        Integer[] x = new Integer[0];
        nodes = Arrays.asList(edges.keySet().toArray(x));

        csvReader.close();
        System.out.println("Loaded graph");
        return new Graph(nodes, edges);
    }
}
