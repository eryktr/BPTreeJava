import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.Socket;

public class Main extends Application {

    private Label typeLabel, operationLabel;
    private ToggleGroup types;
    private RadioButton IntegerType, StringType, DoubleType;
    private Button createTreeButton, addButton, removeButton, searchButton;
    private TextField argumentTextField;
    private HBox mainHBox;
    private Pane container;
    private VBox mainVBox, typeVbox, operationVbox;
    private StackPane drawingPane;



    public void start(Stage primaryStage) throws Exception {

        //SOCKET
        //Socket socket = new Socket("localhost", 4402);
        //COMPONENTS
        container = new Pane();
        typeLabel = new Label("Type of the tree: ");
        operationLabel = new Label("Element: ");
        types = new ToggleGroup();
        IntegerType = new RadioButton("Integer");
        StringType = new RadioButton("String");
        DoubleType = new RadioButton("Double");
        createTreeButton = new Button("Create tree");
        addButton = new Button("add");
        removeButton = new Button("remove");
        searchButton = new Button("search");
        argumentTextField = new TextField();
        mainHBox = new HBox();
        mainVBox = new VBox();
        typeVbox = new VBox();
        operationVbox = new VBox();
        drawingPane = new StackPane();

        //ADDING CHILDREN
        types.getToggles().addAll(IntegerType, StringType, DoubleType);
        container.getChildren().addAll(mainHBox);
        mainHBox.getChildren().addAll(mainVBox, drawingPane);
        mainVBox.getChildren().addAll(typeVbox, operationVbox);
        typeVbox.getChildren().addAll(typeLabel, IntegerType, StringType, DoubleType, createTreeButton);
        operationVbox.getChildren().addAll(operationLabel, argumentTextField, addButton, removeButton, searchButton);

        //FUNCTIONALITY
        primaryStage.setTitle("BTree");
        primaryStage.setResizable(false);
        addButton.setDisable(true);
        removeButton.setDisable(true);
        searchButton.setDisable(true);
        argumentTextField.setDisable(true);

        //STYLE

        typeLabel.setStyle("-fx-font-size: 20; -fx-font-family: 'Berlin Sans FB'");
        operationLabel.setStyle("-fx-font-size: 20; -fx-font-family: 'Berlin Sans FB'");
        IntegerType.setStyle("-fx-font-size: 16;");
        StringType.setStyle("-fx-font-size: 16;");
        DoubleType.setStyle("-fx-font-size: 16;");

        operationVbox.setStyle("-fx-font-size: 14");
        mainVBox.setSpacing(20);
        argumentTextField.setMaxWidth(100);

        drawingPane.setPrefSize(640, 400);

        //DISPLAYING
        Scene scene = new Scene(container);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        Tree<Integer> t = new Tree<>(3);
        t.addElement(4);
        t.addElement(5);
        t.addElement(6);
        t.addElement(7);
        t.addElement(8);
        t.addElement(9);
        t.addElement(0);
        System.out.println(t.getRoot().getValue(0));
        System.out.println(t.getRoot().getValue(1));
        System.out.println(t.getRoot().getValue(2));

    }
}
