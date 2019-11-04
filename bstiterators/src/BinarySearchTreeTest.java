import java.util.Iterator;

public class BinarySearchTreeTest {
    public static void main(String[] args) {
        BinarySearchTree<Integer,Integer> bst = new BinarySearchTree<>();
        for (String arg : args) {
            bst.put(Integer.parseInt(arg), Integer.parseInt(arg));
        }

        bst.prettyPrint();
System.out.println();

        Iterator<Integer> iter = bst.getInorderIterator();
        System.out.println("Inorder: ");
        printIter(iter);

        iter = bst.getPreorderIterator();
        System.out.println("Preorder: ");
        printIter(iter);

        iter = bst.getPostorderIterator();
        System.out.println("Postorder: ");
        printIter(iter);
    }

    public static void printIter(Iterator iter) {
        while (iter.hasNext()) {
            System.out.print(iter.next() + " ");
        }
        System.out.println();
    }
}
