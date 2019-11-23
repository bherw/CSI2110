/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csi2510_project;

import java.io.IOException;

/**
 * @author Administrator
 */
public class CSI2510 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        String edgesFilename = "email-dnc.edges";
        List<Integer>               nodes = graph.getGraphNodes();
        Collections.sort(nodes);
        Map<Integer, List<Integer>> edges = graph.getGraphEdges();
        Graph graph = new GraphReader().read(edgesFilename);
        
        System.out.println("Number of nodes in the Graph: " + nodes.size());
        
        for(Integer node : nodes) {
            System.out.println("Node number: " + node);
            System.out.print("Adjacent Nodes: ");
            if (edges.containsKey(node)) {
                for(Integer edge : edges.get(node)) {
                    System.out.print(edge + " ");
                }
            }
            System.out.println();
            System.out.println("------------------------------------");
        }
    }
}
