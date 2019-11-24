/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csi2510_project;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author Ben Herweyer
 */
public class CSI2510 {
    public static final double DEFAULT_TOTAL_CHANGE_THRESHOLD = 0.001;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        double totalChangeThreshold = DEFAULT_TOTAL_CHANGE_THRESHOLD;
        if (args.length > 0) {
            try {
                totalChangeThreshold = Double.parseDouble(args[0]);
            }
            catch(NumberFormatException e) {
                usage();
            }
        }

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

        // Update page rank one step at a time
        int i = 1;
        double totalChange;
        do {
            totalChange = graph.updatePageRankOneStep();

            System.out.println("Step " + i++);
            System.out.println("Change in page rank (avg/total): " + totalChange / nodes.length + " / " + totalChange);
            System.out.println("Page rank (avg/total): " + graph.getAveragePageRank() + " / " + graph.getTotalPageRank());
            System.out.println("------------------------------------");
        } while (totalChange > totalChangeThreshold);

        // Sort by pageRank DESC
        Arrays.sort(nodes, (a, b) -> {
            if (a.pageRank == b.pageRank) return 0;
            if (a.pageRank < b.pageRank) return 1;
            return -1;
        });

        System.out.println("Most influential nodes:");
        for (int j = 0; j < 10; j++) {
            System.out.println("#" + (j + 1) + ": " + nodes[j].id + " with a pagerank of " + nodes[j].pageRank);
        }
    }

    private static void usage() {
        System.out.println("Usage: java csi2510_project.CSI2510 [<total-change-threshold>]");
        System.out.println();
        System.out.println("total-change-threshold: Pagerank calculation will stop after the total change in pagerank across all nodes is less than this value. Default: 0.001");
        System.exit(255);
    }
}
