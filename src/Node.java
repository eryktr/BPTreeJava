import java.util.ArrayList;

public class Node<T> {
    private final int maxNumOfElements;
    private ArrayList<Pair<T, Node>> fields;
    private int currentNumOfElems;

    public Node(int size) {
        maxNumOfElements = size;
        fields = new ArrayList<>();
    }

    public boolean isFull() {
        return currentNumOfElems >= maxNumOfElements;
    }

    public void addField(T value, Node pointer) {
        fields.add(new Pair<T, Node>(value, pointer));
        currentNumOfElems++;
    }
    public ArrayList<Pair<T, Node>> getFields() {
        return fields;
    }

    public T getValue(int index) {
        return fields.get(index).getFirst();
    }

    public Node getPointer(int index) {
        return fields.get(index).getSecond();
    }

    public int getCurrentNumOfElems() {
        return currentNumOfElems;
    }


}
