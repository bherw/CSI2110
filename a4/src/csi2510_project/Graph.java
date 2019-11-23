/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csi2510_project;

import java.util.*;

/**
 * @author Yahya Alaa
 */
public class Graph {
    private Node[] nodes;
    private Edge[] edges = new Edge[0];

    public Graph(List<Integer> nodes, Map<Integer, List<Integer>> edges) {
        this.nodes = new Node[nodes.size()];
        List<Edge> edgeList = new LinkedList<>();
        int i = 0;
        HashMap<Integer, Node> nodeMap = new HashMap<>();

        for (Map.Entry<Integer, List<Integer>> e : edges.entrySet()) {
            Integer from = e.getKey();
            Node fromNode = nodeMap.computeIfAbsent(from, x -> new Node(from));

            for (Integer to : e.getValue()) {
                Node toNode = nodeMap.computeIfAbsent(to, x -> new Node(to));

                Edge edge = new Edge(fromNode, toNode, i);
                edgeList.add(edge);
                i++;
                fromNode.outgoingEdges.add(edge);
                toNode.incomingEdges.add(edge);
            }
        }
        this.edges = edgeList.toArray(this.edges);

        i = 0;
        for (Node node : nodeMap.values()) {
            node.index = i;
            this.nodes[i++] = node;
        }
    }

    public Node[] getNodes() {
        return this.nodes;
    }

    public Edge[] getEdges() {
        return this.edges;
    }

    class Node {
        List<Edge> outgoingEdges = new LinkedList<>();
        List<Edge> incomingEdges = new LinkedList<>();
        int id;
        int index;
        int pageRank;

        Node(int id) {
            this.id = id;
        }

        public int outDegree() {
            return outgoingEdges.size();
        }

        public int inDegree() {
            return incomingEdges.size();
        }
    }

    class Edge {
        Node from;
        Node to;
        int index;

        Edge(Node from, Node to, int i) {
            this.from = from;
            this.to = to;
            this.index = i;
        }
    }
}
