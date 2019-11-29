/*
 * Computes the PageRank (PR) of each node A in a directed graph using a recursive definition:
 * PR(A) = (1-d) + d (PR(T1)/C(T1) + ... + PR(Tn)/C(Tn))
 * Here d is a damping factor that we will set to 0.85. Nodes T1, T2, ..., Tn are the nodes that
 * connect to A (i.e. having a link going from Ti to A). The term C(Ti) is the number of links outgoing from Ti.
 *
 * @author Md Atiqur Rahman (mrahm021@uottawa.ca)
 */

package csi2510_project;

import java.util.Map;
import java.util.HashMap;

public class PageRank {
    public static final double DAMPING_FACTOR = 0.85;    // damping factor
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

        // replace this with your code
        return new HashMap<Integer, Double>();
    }
}
