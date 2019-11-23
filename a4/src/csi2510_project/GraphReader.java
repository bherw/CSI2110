package csi2510_project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphReader {
    List<Integer> nodes;
    Map<Integer, List<Integer>> edges;

    public Graph read(String edgesFilename) throws IOException {
        System.getProperty("user.dir");
        URL edgesPath = CSI2510.class.getResource(edgesFilename);
        BufferedReader csvReader = new BufferedReader(new FileReader(edgesPath.getFile()));
        String row;
        nodes = new ArrayList<>();
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

            if (!nodes.contains(u)) {
                nodes.add(u);
            }
            if (!nodes.contains(v)) {
                nodes.add(v);
            }

            if (!edges.containsKey(u)) {
                // Create a new list of adjacent nodes for the new node u
                List<Integer> l = new ArrayList<>();
                l.add(v);
                edges.put(u, l);
            }
            else {
                edges.get(u).add(v);
            }
        }

        for (Integer node : nodes) {
            if (!edges.containsKey(node)) {
                edges.put(node, new ArrayList<>());
            }
        }

        csvReader.close();
        return new Graph(nodes, edges);
    }
}
