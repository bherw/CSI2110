import java.util.Iterator;
import java.util.NoSuchElementException;

public class BinarySearchTree<K extends Comparable<K>, V> {
    private static class Node<K, V> {
        protected K key;
        protected V value;
        protected Node<K, V> parent, left, right;

        Node(Node<K, V> parent, K key, V value) {
            this.parent = parent;
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return String.format("(%s,%s)", key, value);
        }
    }

    private Node<K, V> root;

    public void put(K key, V val) {
        root = put(root, null, key, val);
    }

    private Node<K, V> put(Node<K, V> cur, Node<K, V> parent, K key, V val) {
        if (cur == null) return new Node<>(parent, key, val);

        int cmp = key.compareTo(cur.key);
        if (cmp < 0) {
            cur.left = put(cur.left, cur, key, val);
        }
        else if (cmp == 0) {
            cur.value = val;
        }
        else {
            cur.right = put(cur.right, cur, key, val);
        }

        return cur;
    }

    public void prettyPrint() {
        prettyPrint(root, 0);
    }

    private void prettyPrint(Node<K, V> p, int depth) {
        if (p == null) return;

        for (int i = 0; i < depth; i++) {
            System.out.print("   ");
        }
        System.out.println(p);
        prettyPrint(p.left, depth + 1);
        prettyPrint(p.right, depth + 1);
    }

    public Iterator<V> getPreorderIterator() {
        return new PreorderIterator();
    }

    public Iterator<V> getInorderIterator() {
        return new InorderIterator();
    }

    public Iterator<V> getPostorderIterator() {
        return new PostorderIterator();
    }

    private abstract class BSTIterator implements Iterator<V> {
        private Node<K, V> last;
        private Node<K, V> next;

        protected abstract Node<K, V> findNext(Node<K, V> last);

        public boolean hasNext() {
            if (root == null) return false;
            next = findNext(last);
            return next != null;
        }

        public V next() {
            if (!hasNext()) throw new NoSuchElementException();
            last = next;
            next = null;
            return last.value;
        }
    }

    private class PreorderIterator extends BSTIterator {
        @Override
        protected Node<K, V> findNext(Node<K, V> last) {
            if (last == null) {
                return root;
            }

            if (last.left != null) {
                return last.left;
            }
            if (last.right != null) {
                return last.right;
            }

            Node<K, V> parent = last.parent;
            Node<K, V> child = last;
            while (true) {
                if (parent == null) return null;

                if (child == parent.left && parent.right != null) {
                    return parent.right;
                }
                else {
                    child = parent;
                    parent = parent.parent;
                }
            }

        }
    }

    private class InorderIterator extends BSTIterator {

        @Override
        protected Node<K, V> findNext(Node<K, V> last) {
            // First time
            if (last == null) {
                Node<K, V> next = root;
                while (next.left != null) {
                    next = next.left;
                }
                return next;
            }

            // Left side of right subtree
            if (last.right != null) {
                Node<K, V> next = last.right;
                while (next.left != null) {
                    next = next.left;
                }
                return next;
            }

            // Finished this subtree, go up
            Node<K, V> parent = last.parent;
            Node<K, V> child = last;

            while (true) {
                if (parent == null) return null;

                if (child == parent.right) {
                    child = parent;
                    parent = parent.parent;
                }
                else {
                    // child == parent.left
                    return parent;
                }

            }


        }
    }

    private class PostorderIterator extends BSTIterator {

        @Override
        protected Node<K, V> findNext(Node<K, V> last) {
            // First time
            if (last == null) {
                return goDown(root);
            }

            // Last node
            if (last.parent == null) return null;

            // Just finished right subtree of parent or no right subtree and we are left subtree
            if (last.parent.right == last || last.parent.right == null) {
                return last.parent;
            }

            // Finished left subtree, do right
            return goDown(last.parent.right);
        }

        private Node<K, V> goDown(Node<K, V> next) {
            while (next.left != null || next.right != null) {
                while (next.left != null) {
                    next = next.left;
                }
                while (next.right != null) {
                    next = next.right;
                }
            }
            return next;
        }
    }
}
