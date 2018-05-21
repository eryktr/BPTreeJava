import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.TreeSet;

public class Tree<T> {
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
            System.out.println("The value is already in the tree.");
            return;
        }
        values.add(elementToAdd);
        root = nodify(values);
        System.out.println(elementToAdd + "added");
    }

    public Node nodify(TreeSet<T> values) {
        int numOfNodesToCreate = (int)Math.ceil((double)values.size() / maxNumOfElems);
        ArrayList<Node<T>> previousLevel = new ArrayList<>();
        TreeSet<T> valuesToInsert = new TreeSet<>(values);
        do {
            System.out.println("prev level originally null? " + (previousLevel.isEmpty()) );
            ArrayList<Node<T>> newNodes = new ArrayList<>();
            for (int i = 0; i < numOfNodesToCreate; i++) {
                Node<T> newNode = new Node(maxNumOfElems);
                newNodes.add(newNode);
            }
            populateNodes(newNodes, previousLevel, valuesToInsert);
            previousLevel = new ArrayList<>(newNodes);
            System.out.println("And now? "+ (previousLevel.isEmpty()));
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
                    System.out.println("They entered me");
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

    public static void main(String[] args) {
        Tree t;
        try {
            ServerSocket serverSocket = new ServerSocket(5703);
            Socket client = serverSocket.accept();
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            String inputLine;
            while((inputLine = in.readLine()) != null) {
                String[] params = inputLine.split(" ");
                if (params[0].equals("Integer") || params[0].equals("String") || params[0].equals("Double")) {
                    int size = Integer.parseInt(params[1]);
                    if(params[0].equals("Integer")) {
                        t = new Tree<Integer>(size);
                        out.println("Integer tree created");
                    }
                    else if(params[0].equals("String")) {
                        t = new Tree<String>(size);
                        out.println("String tree created");
                    }
                    else {
                        t = new Tree<Double>(size);
                        out.println("Double tree created");
                    }
                }
                else if(params[0].equals("add") || params[0].equals("remove") || params[0].equals("search")) {

                }
            }
        }
        catch (IOException e) {
            System.out.println("error from server side");
        }

    }


}

