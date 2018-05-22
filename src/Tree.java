import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.TreeSet;

public class Tree<T> {
    private static String currentType;
    private static ServerSocket serverSocket;
    private static Socket client;
    private static PrintWriter out;
    private static BufferedReader in;

    private final int maxNumOfElems;
    private TreeSet<T> values;
    private Node root;

    public Tree(int maxNumOfElems) {
        this.maxNumOfElems = maxNumOfElems;
        values = new TreeSet<>();
        root = new Node(maxNumOfElems);
    }

    public void addElement(T elementToAdd) {
        if (values.contains(elementToAdd)) {
            out.println("The value is already in the tree.");
            return;
        }
        values.add(elementToAdd);
        root = nodify(values);
        out.println(elementToAdd + "added");
    }

    public void removeElement(T elementToRemove) {
        if(!values.contains(elementToRemove)) {
            out.println("Element is not in the tree.");
            return;
        }
        values.remove(elementToRemove);
        root = nodify(values);
        out.println(elementToRemove + " removed.");
    }

    public void search(T elementToFind) {
        if(values.contains(elementToFind)) {
            out.println("The tree contains " + elementToFind);
        }
        else {
            out.println("The tree does not contain " + elementToFind);
        }
    }

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

    Node findTargetNode(ArrayList<Node<T>> newNodes, int capacity) {
        for(Node node : newNodes) {
            if(node.getCurrentNumOfElems() < maxNumOfElems && node.getCurrentNumOfElems() < capacity) {
                return node;
            }
        }
        return null;
    }

    Node findPointer(ArrayList<Node<T>> previousNodes, T valueToInsert) {
        for(int i = 0; i < previousNodes.size(); i++) {
            if(i < previousNodes.get(i).getCurrentNumOfElems() && previousNodes.get(i).getValue(0) == valueToInsert) {
                return previousNodes.get(i);
            }
        }
        return null;
    }

    TreeSet<T> updateValuesToInsert(ArrayList<Node<T>> previousNodes) {
        TreeSet<T> newValues = new TreeSet<>();
        for(Node node : previousNodes) {
            if(node.getCurrentNumOfElems() > 0) {
                newValues.add((T) node.getValue(0));
            }
        }

        return newValues;
    }

    Node getRoot() {
        return root;
    }

    public static void performOperation(Tree t, String operation, Object value) {
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

