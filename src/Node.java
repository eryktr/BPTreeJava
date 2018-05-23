import java.util.ArrayList;

/**
 * This class represents the single Node of the tree
 * @param <T>
 */
public class Node<T extends Comparable> {
    /**
     * The maximum number of elements this Node is allowed to have.
     */
    private final int maxNumOfElements;
    /**
     * The vector of Pairs this node has.
     */
    private ArrayList<Pair<T, Node>> fields;
    /**
     * The current number of elements in this Node.
     */
    private int currentNumOfElems;

    public Node(int size) {
        maxNumOfElements = size;
        fields = new ArrayList<>();
    }

    /**
     * Checks if the current number of elements is greater than or equal to the maximum number of elements
     * this Node is allowed to have.
     * @return
     */
    public boolean isFull() {
        return currentNumOfElems >= maxNumOfElements;
    }

    /**
     * Inserts another (value, pointer) pair to the Node.
     * @param value
     * @param pointer
     */
    public void addField(T value, Node pointer) {
        fields.add(new Pair<T, Node>(value, pointer));
        currentNumOfElems++;
    }

    /**
     * Returns the vector of the fields.
     * @return
     */
    public ArrayList<Pair<T, Node>> getFields() {
        return fields;
    }

    /**
     * Returns the value at certain index
     * @param index
     * @return
     */
    public T getValue(int index) {
        return fields.get(index).getFirst();
    }

    /**
     * Returns the pointer at certain index
     * @param index
     * @return
     */
    public Node getPointer(int index) {
        return fields.get(index).getSecond();
    }

    /**
     * Returns the current number of elements in the Tree.
     */
    public int getCurrentNumOfElems() {
        return currentNumOfElems;
    }


}
