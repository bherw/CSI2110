/*
 * Computes the PageRank (PR) of each node A in a directed graph using a recursive definition:
 * PR(A) = (1-d) + d (PR(T1)/C(T1) + ... + PR(Tn)/C(Tn))
 * Here d is a damping factor that we will set to 0.85. Nodes T1, T2, ..., Tn are the nodes that
 * connect to A (i.e. having a link going from Ti to A). The term C(Ti) is the number of links outgoing from Ti.
 *
 * @author Md Atiqur Rahman (mrahm021@uottawa.ca)
 */

package csi2510_project;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageRank {
    public static final double DAMPING_FACTOR = 0.85;    // damping factor
    public static final double STARTING_PAGE_RANK = 1;
    private double tolerance;                            // tolerance to stop
    private long maxIter;                                // max iterations to stop

    PageRank() {
        // default tolerance=0.000001, default maxIter=100
        this(0.000001, 100);
    }

    PageRank(double tolerance, long maxIter) {
        this.tolerance = tolerance;
        this.maxIter = maxIter;
    }

    /**
     * Computes the PageRank (PR) of each node in a graph in an iterative way.
     * Iteration stops as soon as this.maxIter or this.tolerance whichever is reached first.
     *
     * @param graph the Graph to compute PR for
     * @return returns a Map<Integer, Double> mapping each node to its PR
     */

    public Map<Integer, Double> computePageRank(Graph graph) {
        List<Integer> nodes = graph.getGraphNodes();
        Map<Integer, List<Integer>> edges = graph.getGraphEdges();
        Map<Integer, Double> pageRank = new HashMap<>(nodes.size());
        long startTime = System.currentTimeMillis();

        for (Integer node : nodes) {
            pageRank.put(node, STARTING_PAGE_RANK);
        }

        double meanChange = Double.MAX_VALUE;
        int iterations = 0;
        while (meanChange > tolerance && ++iterations <= maxIter) {
            meanChange = updatePageRankOneStep(graph, pageRank) / nodes.size();
        }

        // XXX: This really isn't the right place to be printing statistics,
        // but we were told not to modify TestPageRank.java.
        System.out.println("Computed page rank in " + iterations + " iterations after " + (System.currentTimeMillis() - startTime) + " ms");
        System.out.println("Mean change in page rank in last iteration: " + meanChange);

        return pageRank;
    }

    private double updatePageRankOneStep(Graph graph, Map<Integer, Double> pageRank) {
        List<Integer> nodes = graph.getGraphNodes();
        Map<Integer, List<Integer>> edges = graph.getGraphEdges();
        Map<Integer, Double> pageRankFromIncoming = new HashMap<>(nodes.size());

        for (Map.Entry<Integer, List<Integer>> entry : edges.entrySet()) {
            Integer fromNode = entry.getKey();
            List<Integer> outgoingEdges = entry.getValue();
            for (Integer toNode : outgoingEdges) {
                double incomingPrForNode = pageRankFromIncoming.computeIfAbsent(toNode, x -> 0.0);
                pageRankFromIncoming.put(toNode, incomingPrForNode + pageRank.get(fromNode) / outgoingEdges.size());
            }
        }

        double totalChange = 0.0;
        for (Integer node : nodes) {
            double oldPageRank = pageRank.get(node);
            double newPageRank = (1 - DAMPING_FACTOR) + DAMPING_FACTOR * pageRankFromIncoming.getOrDefault(node, 0.0);
            totalChange += Math.abs(oldPageRank - newPageRank);
            pageRank.put(node, newPageRank);
        }

        return totalChange;
    }
}
