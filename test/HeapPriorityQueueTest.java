import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * @Author Ben Herweyer <benjamin.herweyer@gmail.com>
 */
public class HeapPriorityQueueTest {
    public static final int BASE_SIZE = 100000;

    private static final int[] testArray = new int[BASE_SIZE];
    private static final int[] testArrayAsc;
    private static final int[] testArrayDesc;
    private static Random rng = new Random(12345);


    static {
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
    public void constructor__oddSize() {
        pq = new HeapPriorityQueue<>(3);
        for (int i = 0; i < 3; i++) {
            pq.insert(i, i);
        }
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
        for (int i = 0; i < 4; i++) {
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

        assertNull(pq.min());
        assertNull(pq.removeMin());
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

        assertNull(pq.max());
        assertNull(pq.removeMax());
    }

    @Test
    public void print() {
        for (int i = 0; i < 4; i++) {
            ((HeapPriorityQueue) pq).print();
            pq.insert(i, i);
        }
    }

    @Test
    // This test ensures the heap is rebuilt correctly after removing an element from the middle and then shifting the remaining elements left.
    public void testRemoveFromBothEnds() {
        List<Integer> seen = new ArrayList<>();
        List<Entry<Integer, Integer>> elementsList = new ArrayList<>();
        Deque<Entry<Integer, Integer>> dq;
        int min = Integer.MIN_VALUE, max = Integer.MAX_VALUE;
        int chunkSize = BASE_SIZE / 4;

        // Need to guarantee non-equality of keys for this test since we need the pq
        // to return values in the same order as the sorted Deque.
        for (int i = 0; i < BASE_SIZE; i++) {
            int randInt = i * BASE_SIZE / 10 + rng.nextInt(BASE_SIZE / 100);
            elementsList.add(pq.insert(randInt, randInt));
        }

        elementsList.sort(Comparator.comparingInt(Entry::getKey));
        dq = new ArrayDeque<>(elementsList);

        for (int i = 0; i < chunkSize; i++) {
            Entry<Integer, Integer> e = pq.removeMin();
            seen.add(e.key);
            assertEquals(seen.size(), BASE_SIZE - pq.size());
            assertTrue(e.key > min);
            assertEquals(dq.pollFirst(), e);
            min = e.key;
        }

        for (int i = 0; i < chunkSize; i++) {
            Entry<Integer, Integer> e = pq.removeMax();
            seen.add(e.key);
            assertEquals(seen.size(), BASE_SIZE - pq.size());
            assertTrue(e.key <= max);
            assertEquals(dq.pollLast(), e);
            max = e.key;
        }


        for (int i = 0; i < chunkSize; i++) {
            Entry<Integer, Integer> e = pq.removeMin();
            seen.add(e.key);
            assertEquals(seen.size(), BASE_SIZE - pq.size());
            assertTrue(e.key >= min);
            assertEquals(dq.pollFirst(), e);
            min = e.key;
        }

        while (!pq.isEmpty()) {
            Entry<Integer, Integer> e = pq.removeMax();
            seen.add(e.key);
            assertEquals(seen.size(), BASE_SIZE - pq.size());
            assertTrue(e.key <= max);
            assertEquals(dq.pollLast(), e);
            max = e.key;
        }
    }
}
