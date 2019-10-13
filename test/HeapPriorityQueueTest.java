import org.junit.Before;
import org.junit.Test;
import org.junit.internal.runners.statements.ExpectException;

import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.*;

public class HeapPriorityQueueTest {
    public static final int BASE_SIZE = 1000;

    private static final int[] testArray = new int[BASE_SIZE];
    private static final int[] testArrayAsc;
    private static final int[] testArrayDesc;


    static {
        Random rng = new Random(12345);
        for (int i = 0; i < BASE_SIZE; i++) {
            testArray[i] = rng.nextInt(10000);
        }

        testArrayAsc = Arrays.copyOf(testArray, BASE_SIZE);
        Arrays.sort(testArrayAsc);
        testArrayDesc = new int[BASE_SIZE];
        for (int i = 0; i < BASE_SIZE; i++) {
            testArrayDesc[i] = testArrayAsc[BASE_SIZE - i - 1];
        }
    }

    private PriorityQueue<Integer, Integer> pq;

    @Before
    public void setUp() throws Exception {
        pq = new HeapPriorityQueue<>(BASE_SIZE);
    }

    @Test
    public void size__removeMin() {
        for (int i = 0; i < BASE_SIZE; i++) {
            assertEquals(i, pq.size());
            pq.insert(i, i);
            assertEquals(i + 1, pq.size());
        }

        for (int i = BASE_SIZE; i > 0; i--) {
            assertEquals(i, pq.size());
            pq.removeMin();
            assertEquals(i - 1, pq.size());
        }
    }

    @Test
    public void size__removeMax() {
        for (int i = 0; i < BASE_SIZE; i++) {
            assertEquals(i, pq.size());
            pq.insert(i, i);
            assertEquals(i + 1, pq.size());
        }

        for (int i = BASE_SIZE; i > 0; i--) {
            assertEquals(i, pq.size());
            pq.removeMax();
            assertEquals(i - 1, pq.size());
        }
    }

    @Test
    public void isEmpty__removeMin() {
        assertTrue(pq.isEmpty());
        for (int i = 0; i < BASE_SIZE; i++) {
            pq.insert(i, i);
            assertFalse(pq.isEmpty());
        }

        for (int i = BASE_SIZE; i > 0; i--) {
            assertFalse(pq.isEmpty());
            pq.removeMin();
        }
        assertTrue(pq.isEmpty());
    }

    @Test
    public void isEmpty__removeMax() {
        assertTrue(pq.isEmpty());
        for (int i = 0; i < BASE_SIZE; i++) {
            pq.insert(i, i);
            assertFalse(pq.isEmpty());
        }

        for (int i = BASE_SIZE; i > 0; i--) {
            assertFalse(pq.isEmpty());
            pq.removeMax();
        }
        assertTrue(pq.isEmpty());
    }

    @Test
    public void insert__reverse__removeMin() {
        for (int i = BASE_SIZE - 1; i > -1; i--) {
            pq.insert(i, i);
        }

        for (int i = 0; i < BASE_SIZE; i++) {
            assertEquals(i, (int) pq.removeMin().getKey());
        }
    }

    @Test
    public void insert__reverse__removeMax() {
        for (int i = 0; i < BASE_SIZE; i++) {
            pq.insert(i, i);
        }

        for (int i = BASE_SIZE - 1; i > 0; i--) {
            assertEquals(i, (int) pq.removeMax().getKey());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void insert__IllegalArgumentException() {
        pq = new HeapPriorityQueue<>(2);
        for (int i = 0; i < 3; i++) {
            pq.insert(i, i);
        }
    }

    @Test
    public void min__removeMin() {
        for (int i = 0; i < BASE_SIZE; i++) {
            pq.insert(testArray[i], testArray[i]);
        }

        for (int i = 0; i < BASE_SIZE; i++) {
            assertEquals(testArrayAsc[i], pq.min().key.intValue());
            assertEquals(testArrayAsc[i], pq.removeMin().key.intValue());
        }

        assertEquals(null, pq.min());
        assertEquals(null, pq.removeMin());
    }

    @Test
    public void max__removeMax() {
        for (int i = 0; i < BASE_SIZE; i++) {
            pq.insert(testArray[i], testArray[i]);
        }

        for (int i = 0; i < BASE_SIZE; i++) {
            assertEquals(testArrayDesc[i], pq.max().key.intValue());
            assertEquals(testArrayDesc[i], pq.removeMax().key.intValue());
        }

        assertEquals(null, pq.min());
        assertEquals(null, pq.removeMin());
    }

    @Test
    public void print() {
        for (int i = 0; i < 4; i++) {
            ((HeapPriorityQueue) pq).print();
            pq.insert(i, i);
        }
    }
}
