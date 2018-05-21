import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Main extends Application {

    private static Socket socket;
    private static PrintWriter out;
    private static BufferedReader in;
    private static BufferedReader sysIn;

    private Label typeLabel, operationLabel;
    private ToggleGroup types;
    private RadioButton IntegerType, StringType, DoubleType;
    private Button createTreeButton, addButton, removeButton, searchButton;
    private TextField argumentTextField, sizeTextField;
    private HBox mainHBox;
    private Pane container;
    private VBox mainVBox, typeVbox, operationVbox;
    private StackPane drawingPane;


    public void start(Stage primaryStage) throws Exception {

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
        sizeTextField = new TextField("size");
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
        typeVbox.getChildren().addAll(typeLabel, IntegerType, StringType, DoubleType, sizeTextField, createTreeButton);
        operationVbox.getChildren().addAll(operationLabel, argumentTextField, addButton, removeButton, searchButton);

        //FUNCTIONALITY
        primaryStage.setTitle("BTree");
        primaryStage.setResizable(false);
        addButton.setDisable(true);
        removeButton.setDisable(true);
        searchButton.setDisable(true);
        argumentTextField.setDisable(true);

        //STYLE
        types.selectToggle(IntegerType);
        typeLabel.setStyle("-fx-font-size: 20; -fx-font-family: 'Berlin Sans FB'");
        operationLabel.setStyle("-fx-font-size: 20; -fx-font-family: 'Berlin Sans FB'");
        IntegerType.setStyle("-fx-font-size: 16;");
        StringType.setStyle("-fx-font-size: 16;");
        DoubleType.setStyle("-fx-font-size: 16;");
        operationVbox.setStyle("-fx-font-size: 14");
        mainVBox.setSpacing(20);
        argumentTextField.setMaxWidth(100);
        drawingPane.setPrefSize(640, 400);

        //EVENTS
        createTreeButton.setOnAction( event -> {
            String type;
            String size = sizeTextField.getText();
            if(types.getSelectedToggle() == IntegerType) { type = "Integer"; }
            else if(types.getSelectedToggle() == StringType) { type = "String"; }
            else { type = "Double"; }
            out.println(type + " " + size);
            try { System.out.println(in.readLine()); }
            catch (IOException ex) {}
            createTreeButton.setDisable(true);
            sizeTextField.setDisable(true);
            IntegerType.setDisable(true);
            StringType.setDisable(true);
            DoubleType.setDisable(true);
            argumentTextField.setDisable(false);
            addButton.setDisable(false);
            removeButton.setDisable(false);
            searchButton.setDisable(false);

        });

        //DISPLAYING
        Scene scene = new Scene(container);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        try {
            socket = new Socket("localhost", 5703);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            sysIn = new BufferedReader(new InputStreamReader(System.in));

            launch(args);

        }
        catch (IOException e) {
            System.out.println("error from client side");
        }


    }
}