/**
 * Array Heap implimentation of a priority queue
 *
 * @author Lachlan Plant
 */
public class HeapPriorityQueue<K extends Comparable, V> implements PriorityQueue<K, V> {
    private static final int MIN_HEAP_OPERATION = 1;
    private static final int MAX_HEAP_OPERATION = -1;

    private Entry[] minHeap; //The Heap itself in array form
    private int tail;    //Index of last element in the heap

    /**
     * Default constructor
     */
    public HeapPriorityQueue() {
        this(100);
    }


    /**
     * HeapPriorityQueue constructor with max storage of size elements
     */
    public HeapPriorityQueue(int size) {
        minHeap = new Entry[size];
        tail = -1;
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
        return tail + 1;
    } /* size */


    /**
     * Tests whether the priority queue is empty.
     * O(1)
     *
     * @return true if the priority queue is empty, false otherwise
     */
    public boolean isEmpty() {
        return tail < 0;
    } /* isEmpty */


    /**
     * Inserts a key-value pair and returns the entry created.
     * O(log(n))
     *
     * @param key   the key of the new entry
     * @param value the associated value of the new entry
     * @param ref   a reference to the associated entry in the othe heap
     * @return the entry storing the new key-value pair
     * @throws IllegalArgumentException if the heap is full
     */
    public Entry<K, V> insert(K key, V value) throws IllegalArgumentException {
        if (tail == minHeap.length - 1)
            throw new IllegalArgumentException("Heap Overflow");

        Entry<K, V> e = new Entry<>(key, value);
        minHeap[++tail] = e;
        e.setIndex(tail);
        upHeap(tail, MIN_HEAP_OPERATION);
        return e;
    } /* insert */


    /**
     * Returns (but does not remove) an entry with minimal key.
     * O(1)
     *
     * @return entry having a minimal key (or null if empty)
     */
    public Entry<K, V> min() {
        if (isEmpty())
            return null;
        return minHeap[0];
    } /* min */


    /**
     * Removes and returns an entry with minimal key.
     * O(log(n))
     *
     * @return the removed entry (or null if empty)
     */
    public Entry<K, V> removeMin() {
        if (isEmpty())
            return null;

        Entry<K, V> ret = minHeap[0];

        if (tail == 0) {
            tail = -1;
            minHeap[0] = null;
            return ret;
        }

        minHeap[0] = minHeap[tail];
        minHeap[tail--] = null;

        downHeap(0, MIN_HEAP_OPERATION);

        return ret;
    } /* removeMin */


    /****************************************************
     *
     *           Methods for Heap Operations
     *
     ****************************************************/

    /**
     * Algorithm to place element after insertion at the tail.
     * O(log(n))
     */
    private void upHeap(int location, int operation) {
        if (location == 0) return;

        int parent = parent(location);

        if (minHeap[parent].key.compareTo(minHeap[location].key) * operation > 0) {
            swap(location, parent);
            upHeap(parent, operation);
        }
    } /* upHeap */


    /**
     * Algorithm to place element after removal of root and tail element placed at root.
     * O(log(n))
     */
    private void downHeap(int location, int operation) {
        int left = (location * 2) + 1;
        int right = (location * 2) + 2;

        //Both children null or out of bound
        if (left > tail) return;

        //left in right out;
        if (left == tail) {
            if (minHeap[location].key.compareTo(minHeap[left].key) * operation > 0)
                swap(location, left);
            return;
        }

        int toSwap = (minHeap[left].key.compareTo(minHeap[right].key) * operation < 0) ?
                left : right;

        if (minHeap[location].key.compareTo(minHeap[toSwap].key) *operation > 0) {
            swap(location, toSwap);
            downHeap(toSwap, operation);
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
    private void swap(int location1, int location2) {
        Entry<K, V> temp = minHeap[location1];
        minHeap[location1] = minHeap[location2];
        minHeap[location2] = temp;

        minHeap[location1].index = location1;
        minHeap[location2].index = location2;
    } /* swap */

    public void print() {

        for (Entry<K, V> e : minHeap)
            System.out.println("(" + e.key.toString() + "," + e.value.toString() + ":" + e.index + "), ");
    }
}
