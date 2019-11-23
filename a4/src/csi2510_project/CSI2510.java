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
        Graph graph = new GraphReader().read(edgesFilename);
        Graph.Node[] nodes = graph.getNodes();
        
        System.out.println("Number of nodes in the Graph: " + nodes.length);
        
        for(Graph.Node node : nodes) {
            System.out.println("Node number: " + node.id);
            System.out.print("Adjacent Nodes: ");
            for (Graph.Edge edge : node.outgoingEdges) {
                System.out.print(edge.to.id + " ");
            }
            System.out.println();
            System.out.println("------------------------------------");
        }
    }
}
