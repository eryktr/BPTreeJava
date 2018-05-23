import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.TreeSet;

/**
 * The overall structure of the tree, together with the server socket.
 * @param <T extends Comparable> Type of the tree.
 */
public class Tree<T extends Comparable> {
    /**
     * String variable storinng the current type of the tree
     */
    private static String currentType;
    private static ServerSocket serverSocket;
    private static Socket client;
    /**
     * Output stream of the server
     */
    private static PrintWriter out;
    /**
     * Input stream of the server
     */
    private static BufferedReader in;

    /**
     * Maximum number of element the tree can have in its nodes
     */
    private final int maxNumOfElems;
    /**
     * The ordered set of values the tree has
     */
    private TreeSet<T> values;
    /**
     * The root of the tree
     */
    private Node root;


    public Tree(int maxNumOfElems) {
        this.maxNumOfElems = maxNumOfElems;
        values = new TreeSet<>();
        root = new Node(maxNumOfElems);
    }

    /**
     * Adds an element to the tree
     * @param elementToAdd
     */
    public void addElement(T elementToAdd) {
        if (values.contains(elementToAdd)) {
            out.println("The value is already in the tree.");
            return;
        }
        values.add(elementToAdd);
        root = nodify(values);
        out.println(elementToAdd + " ADDED SUCCESSFULLY");
        print(root, "");
    }

    /**
     * Removes an element from the tree
     * @param elementToRemove
     */
    public void removeElement(T elementToRemove) {
        if(!values.contains(elementToRemove)) {
            out.println("Element is not in the tree.");
            return;
        }
        values.remove(elementToRemove);
        root = nodify(values);
        out.println(elementToRemove + " REMOVED SUCCESSFULLY.");
        print(root, "");
    }

    /**
     * Checks if the element is in the tre and displays an appropriate piece of information.
     * @param elementToFind
     */
    public void search(T elementToFind) {
        if(values.contains(elementToFind)) {
            out.println("THE TREE CONTAINS " + elementToFind);
        }
        else {
            out.println("The TREE DOES NOT CONTAIN " + elementToFind);
        }
    }

    /**
     * Displays the tree recursively.
     * @param arg
     * The element from which the printing process will start.
     * @param indentation Preferably an empty String. During recursive calls, the spacing will be increased.
     */
    public void print(Node arg, String indentation) {
        String outputLine = indentation + "[";
        for(Pair<T, Node> pair : (ArrayList<Pair<T, Node>>)arg.getFields()) {
            outputLine += (pair.getFirst());
            if(pair != arg.getFields().get(arg.getFields().size() - 1))
                outputLine += ", ";
        }
        outputLine += "]";
        out.println(outputLine);
        for(Pair<T, Node> pair : (ArrayList<Pair<T, Node>>)arg.getFields()) {
            if(pair.getSecond() != null) {
                print(pair.getSecond(), indentation + "     ");
            }
        }
    }

    /**
     * Creates the tree from the given set of values and then returns its root.
     * The tree is created in a bottom-up manner, with every so higher layers of nodes created in a loop.
     * The nodes are then populated using populateNodes().
     * @param values
     * Tree set of values the tree is to be created from.
     * @return
     */
    public Node nodify(TreeSet<T> values) {
        if(values.size() == 0) {
            return null;
        }
        int numOfNodesToCreate = (int)Math.ceil((double)values.size() / maxNumOfElems);
        ArrayList<Node<T>> previousLevel = new ArrayList<>();
        TreeSet<T> valuesToInsert = new TreeSet<>(values);
        do {
            ArrayList<Node<T>> newNodes = new ArrayList<>();
            for (int i = 0; i < numOfNodesToCreate; i++) {
                Node<T> newNode = new Node(maxNumOfElems);
                newNodes.add(newNode);
            }
            populateNodes(newNodes, previousLevel, valuesToInsert);
            previousLevel = new ArrayList<>(newNodes);
            valuesToInsert = updateValuesToInsert(previousLevel);
            numOfNodesToCreate = (numOfNodesToCreate == 1) ? 0 : (int)Math.ceil((double)valuesToInsert.size() / maxNumOfElems);
        } while (numOfNodesToCreate >= 1);
        return  previousLevel.get(0);
    }

    /**
     * Takes in the vector of new nodes and previous nodes and populates them with appropriate values and pointers.
     * @param newNodes
     * @param previousNodes
     * @param valuesToInsert
     */
    public void populateNodes(ArrayList<Node<T>> newNodes, ArrayList<Node<T>> previousNodes,
                              TreeSet<T> valuesToInsert) {

        final int initialAvailableNodes = newNodes.size();
        ArrayList<Node<T>> newNodesCopy = new ArrayList<>(newNodes);
        int availableNodes = initialAvailableNodes;
        int availableNumOfElems = valuesToInsert.size();
        for(int i = 0; i < initialAvailableNodes; i++) {
            int numOfElemsToAdd = (int)Math.floor(availableNumOfElems / availableNodes);

            for(int j = 0; j < numOfElemsToAdd; j++) {
                T valueToInsert = valuesToInsert.first();
                Node targetNode = findTargetNode(newNodesCopy, numOfElemsToAdd);
                if(previousNodes.isEmpty()) {
                    targetNode.addField(valueToInsert, null);
                    availableNumOfElems--;
                    if(targetNode.getCurrentNumOfElems() >= numOfElemsToAdd) {
                        availableNodes--;
                        newNodesCopy.remove(0);
                    }
                    valuesToInsert.remove(valueToInsert);
                }
                else {
                    Node<T> pointer = findPointer(previousNodes, valueToInsert);
                    targetNode.addField(valueToInsert, pointer);
                    availableNumOfElems--;
                    if(targetNode.getCurrentNumOfElems() >= numOfElemsToAdd) {
                        availableNodes--;
                        newNodesCopy.remove(0);
                    }
                    valuesToInsert.remove(valueToInsert);
                }
            }
        }
    }

    /**
     * Returns the target node where the element is to be inserted.
     * @param newNodes
     * @param capacity
     * @return
     */
    Node findTargetNode(ArrayList<Node<T>> newNodes, int capacity) {
        for(Node node : newNodes) {
            if(node.getCurrentNumOfElems() < maxNumOfElems && node.getCurrentNumOfElems() < capacity) {
                return node;
            }
        }
        return null;
    }

    /**
     * Searches through the list of previous nodes, finds the Node containing the input value and returns this Node.
     * @param previousNodes
     * @param valueToInsert
     * @return
     */
    Node findPointer(ArrayList<Node<T>> previousNodes, T valueToInsert) {
        for(int i = 0; i < previousNodes.size(); i++) {
            if(previousNodes.get(i).getCurrentNumOfElems() > 0 && previousNodes.get(i).getValue(0) == valueToInsert) {
                return previousNodes.get(i);
            }
        }
        return null;
    }

    /**
     * Updates the values vector so that it is suitable for the next layer.
     * @param previousNodes
     * @return
     */
    TreeSet<T> updateValuesToInsert(ArrayList<Node<T>> previousNodes) {
        TreeSet<T> newValues = new TreeSet<>();
        for(Node node : previousNodes) {
            if(node.getCurrentNumOfElems() > 0) {
                newValues.add((T) node.getValue(0));
            }
        }

        return newValues;
    }

    /**
     * Returns the root of the tree.
     * @return
     */
    Node getRoot() {
        return root;
    }

    /**
     * Validates the client request and performs a certain operation on the input tree
     * @param t
     * The tree on which the operation is to be performed
     * @param operation
     * @param value
     */
    public static void performOperation(Tree t, String operation, Comparable value) {
        if (operation.equals("add")) {
            t.addElement(value);;
        }
        else if(operation.equals("remove")) {
            t.removeElement(value);
        }
        else {
            t.search(value);
        }
    }

    public static void main(String[] args) {
        Tree t = null;
        try {
            serverSocket = new ServerSocket(5703);
            client = serverSocket.accept();
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            String inputLine;
            while((inputLine = in.readLine()) != null) {
                String[] params = inputLine.split(" ");
                if (params[0].equals("Integer") || params[0].equals("String") || params[0].equals("Double")) {
                    int size = Integer.parseInt(params[1]);
                    if(params[0].equals("Integer")) {
                        t = new Tree<Integer>(size);
                        currentType = "Integer";
                        out.println("Integer tree created");
                    }
                    else if(params[0].equals("String")) {
                        t = new Tree<String>(size);
                        currentType = "String";
                        out.println("String tree created");
                    }
                    else {
                        t = new Tree<Double>(size);
                        currentType = "Double";
                        out.println("Double tree created");
                    }
                }
                else if(params[0].equals("add") || params[0].equals("remove") || params[0].equals("search")) {
                    if(currentType.equals("Integer")) {
                        Integer value = Integer.parseInt(params[1]);
                        performOperation(t, params[0], value);
                    }

                    else if(currentType.equals("String")) {
                        String value = params[1];
                        for(int i = 2; i < params.length; i++) {
                            value += params[i];
                        }
                        performOperation(t, params[0], value);
                    }

                    else {
                        Double value = Double.parseDouble(params[1]);
                        performOperation(t, params[0], value);
                    }
                }
            }
        }
        catch (IOException e) {
            System.out.println("error from server side");
        }
    }
}

