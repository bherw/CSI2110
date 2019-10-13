/**
 * Array Heap implimentation of a priority queue
 *
 * @author Lachlan Plant
 */
public class HeapPriorityQueue<K extends Comparable<? super K>, V> implements PriorityQueue<K, V> {
    private static final int MIN_HEAP_OPERATION = 1;
    private static final int MAX_HEAP_OPERATION = -1;

    private Entry<K, V>[] minHeap;
    private Entry<K, V>[] maxHeap;
    private Entry<K, V> buffer;
    private int tail = -1;

    /**
     * Default constructor
     */
    public HeapPriorityQueue() {
        this(100);
    }


    /**
     * HeapPriorityQueue constructor with max storage of size elements
     */
    @SuppressWarnings("unchecked")
    public HeapPriorityQueue(int size) {
        minHeap = new Entry[size / 2];
        maxHeap = new Entry[size / 2];
    }


    /****************************************************
     *
     *             Priority Queue Methods
     *
     ****************************************************/

    /**
     * Returns the number of items in the priority queue.
     * O(1)
     *
     * @return number of items
     */
    public int size() {
        return 2 * (tail + 1) + (buffer == null ? 0 : 1);
    } /* size */


    /**
     * Tests whether the priority queue is empty.
     * O(1)
     *
     * @return true if the priority queue is empty, false otherwise
     */
    public boolean isEmpty() {
        return tail < 0 && buffer == null;
    } /* isEmpty */


    /**
     * Inserts a key-value pair and returns the entry created.
     * O(log(n))
     *
     * @param key   the key of the new entry
     * @param value the associated value of the new entry
     * @return the entry storing the new key-value pair
     * @throws IllegalArgumentException if the heap is full
     */
    public Entry<K, V> insert(K key, V value) throws IllegalArgumentException {
        if (tail == minHeap.length - 1)
            throw new IllegalArgumentException("Heap Overflow");

        Entry<K, V> e = new Entry<>(key, value);
        if (buffer == null) {
            buffer = e;
        }
        else {
            e.associate = buffer;
            buffer.associate = e;
            tail++;

            if (e.key.compareTo(buffer.key) < 0) {
                insert(minHeap, e, MIN_HEAP_OPERATION);
                insert(maxHeap, buffer, MAX_HEAP_OPERATION);
            }
            else {
                insert(minHeap, buffer, MIN_HEAP_OPERATION);
                insert(maxHeap, e, MAX_HEAP_OPERATION);
            }

            buffer = null;
        }


        return e;
    } /* insert */

    /**
     * Inserts an entry into the specified heap. O(log(n))
     *
     * @param heap      a heap
     * @param e         an entry
     * @param operation the operations this heap uses
     */
    private void insert(Entry<K, V>[] heap, Entry<K, V> e, int operation) {
        heap[tail] = e;
        e.setIndex(tail);
        upHeap(heap, tail, operation);
    }


    /**
     * Returns (but does not remove) an entry with minimal key.
     * O(1)
     *
     * @return entry having a minimal key (or null if empty)
     */
    public Entry<K, V> min() {
        return extremum(minHeap, MIN_HEAP_OPERATION);
    } /* min */


    /**
     * Returns (but does not remove) an entry with min/max key depending on the heap selected. O(1)
     *
     * @param heap      the min or max heap
     * @param operation the operation modifier used for this heap
     * @return entry having extreme key (or null if empty)
     */
    private Entry<K, V> extremum(Entry<K, V>[] heap, int operation) {
        if (isEmpty())
            return null;
        if (tail == -1)
            return buffer;
        if (buffer == null)
            return heap[0];
        if (buffer.key.compareTo(heap[0].key) * operation < 0)
            return buffer;
        return heap[0];
    }


    /**
     * Removes and returns an entry with minimal key.
     * O(n)
     *
     * @return the removed entry (or null if empty)
     */
    public Entry<K, V> removeMin() {
        if (isEmpty())
            return null;

        // Buffer is min -> return buffer
        if (buffer != null && (tail == -1 || buffer.key.compareTo(minHeap[0].key) < 0)) {
            Entry<K, V> ret = buffer;
            buffer = null;
            return ret;
        }

        Entry<K, V> ret = minHeap[0];
        Entry<K, V> associate = ret.associate;

        ret.associate = null;
        associate.associate = null;

        if (buffer == null) {
            buffer = associate;

            // Remove associated element from maxHeap.
            // Worst case: we have to shift the entire maxHeap left by one
            for (int i = associate.index; i < tail; i++) {
                maxHeap[i] = maxHeap[i + 1];
                maxHeap[i].index = i;
            }
            maxHeap[tail] = null;

            if (tail != 0) {
                minHeap[0] = minHeap[tail];
            }
            minHeap[tail] = null;
            tail--;

            // Fix maxHeap ordering by rebuilding it from the bottom up.
            for (int i = tail - 1; i >= associate.index; i--) {
                downHeap(maxHeap, i, MAX_HEAP_OPERATION);
            }

            if (tail != -1) {
                downHeap(minHeap, 0, MIN_HEAP_OPERATION);
            }
        }
        else {
            // Form a new pair
            buffer.associate = associate;
            associate.associate = buffer;

            // Buffer is smaller -> replace the removed entry in minHeap
            if (buffer.key.compareTo(associate.key) < 0) {
                minHeap[0] = buffer;
                buffer.index = 0;
                downHeap(minHeap, 0, MIN_HEAP_OPERATION);
            }

            // Buffer is larger -> associate moves to minHeap, buffer replaces associate in maxHeap
            else {
                maxHeap[associate.index] = buffer;
                minHeap[0] = associate;
                buffer.index = associate.index;
                associate.index = 0;

                downHeap(minHeap, 0, MIN_HEAP_OPERATION);
                upDownHeap(maxHeap, buffer.index, MAX_HEAP_OPERATION);
            }

            buffer = null;
        }

        return ret;
    } /* removeMin */


    /****************************************************
     *
     *           Methods for Heap Operations
     *
     ****************************************************/

    /**
     * Algorithm to fix element position after placement in an arbitrary position
     * in the list. O(log(n))
     */
    private void upDownHeap(Entry<K, V>[] heap, int location, int operation) {
        if (location > 0) {
            int parent = parent(location);

            if (heap[parent].key.compareTo(heap[location].key) * operation > 0) {
                swap(heap, location, parent);
                upHeap(heap, parent, operation);
                return;
            }
        }

        downHeap(heap, location, operation);
    }

    /**
     * Algorithm to place element after insertion at the tail.
     * O(log(n))
     */
    private void upHeap(Entry<K, V>[] heap, int location, int operation) {
        if (location == 0) return;

        int parent = parent(location);

        if (heap[parent].key.compareTo(heap[location].key) * operation > 0) {
            swap(heap, location, parent);
            upHeap(heap, parent, operation);
        }
    } /* upHeap */


    /**
     * Algorithm to place element after removal of root and tail element placed at root.
     * O(log(n))
     */
    private void downHeap(Entry<K, V>[] heap, int location, int operation) {
        int left = (location * 2) + 1;
        int right = (location * 2) + 2;

        //Both children null or out of bound
        if (left > tail) return;

        //left in right out;
        if (left == tail) {
            if (heap[location].key.compareTo(heap[left].key) * operation > 0)
                swap(heap, location, left);
            return;
        }

        int toSwap = (heap[left].key.compareTo(heap[right].key) * operation < 0) ?
                left : right;

        if (heap[location].key.compareTo(heap[toSwap].key) * operation > 0) {
            swap(heap, location, toSwap);
            downHeap(heap, toSwap, operation);
        }
    } /* downHeap */


    /**
     * Find parent of a given location,
     * Parent of the root is the root
     * O(1)
     */
    private int parent(int location) {
        return (location - 1) / 2;
    } /* parent */


    /**
     * Inplace swap of 2 elements, assumes locations are in array
     * O(1)
     */
    private void swap(Entry<K, V>[] heap, int location1, int location2) {
        Entry<K, V> temp = heap[location1];
        heap[location1] = heap[location2];
        heap[location2] = temp;

        heap[location1].index = location1;
        heap[location2].index = location2;
    } /* swap */

    public void print() {
        if (buffer != null) {
            System.out.println("\nBUFFER:");
            System.out.print(buffer);
        }

        System.out.println("\nMIN HEAP");
        for (int i = 0; i <= tail; i++) {
            System.out.print(minHeap[i] + ", ");
        }
        System.out.println();

        System.out.println("\nMAX HEAP");
        for (int i = 0; i <= tail; i++) {
            System.out.print(maxHeap[i] + ", ");
        }
        System.out.println();
    }
}
