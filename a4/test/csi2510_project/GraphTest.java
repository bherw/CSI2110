package csi2510_project;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;

public class GraphTest {
    private static String edgesFilename = "email-dnc.edges";
    private GraphReader reader;
    private Graph graph;

    @Before
    public void setup() throws IOException {
        reader = new GraphReader();
        graph = reader.read(edgesFilename);
    }

    @Test
    public void instantiation() {
        Graph.Node[] nodes = graph.getNodes();

        Map<Integer, List<Integer>> actual = new HashMap<>();
        List<Integer> edges;
        for (Graph.Node node : nodes) {
            edges = actual.computeIfAbsent(node.id, x -> new LinkedList<>());
            for (Graph.Edge edge : node.outgoingEdges) {
                edges.add(edge.to.id);
            }
        }

        assertEquals(reader.edges, actual);
    }

    private void assertEquals(Map<Integer, List<Integer>> expected, Map<Integer, List<Integer>> actual) {
        Integer[] a = new Integer[0];
        for (Map.Entry<Integer, List<Integer>> entry : expected.entrySet()) {
            Integer[] expectedEdges = entry.getValue().toArray(a);
            Integer[] actualEdges = actual.get(entry.getKey()).toArray(a);
            assertArrayEquals(expectedEdges, actualEdges);
        }
    }
}