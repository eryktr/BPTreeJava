import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    private Label typeLabel;
    private ToggleGroup types;
    private RadioButton IntegerType, StringType, DoubleType;
    private Button createTreeButton, addButton, removeButton, searchButton;
    private TextField argumentTextField;
    private HBox mainHBox;
    private Pane container;
    private VBox mainVBox;
    private StackPane drawingPane;


    public void start(Stage primaryStage) throws Exception {

        container = new Pane();
        typeLabel = new Label();
        types = new ToggleGroup();
        IntegerType = new RadioButton("Integer");
        StringType = new RadioButton("String");
        DoubleType = new RadioButton("Double");
        createTreeButton = new Button("Create tree");
        addButton = new Button("add");
        removeButton = new Button("remove");
        searchButton = new Button("serach");
        argumentTextField = new TextField();
        mainHBox = new HBox();
        mainVBox = new VBox();
        drawingPane = new StackPane();


        primaryStage.setTitle("BTree");
        primaryStage.setResizable(false);
        typeLabel.setStyle("-fx-font-size: 20; -fx-font-family: 'Berlin Sans FB'");
        typeLabel.setText("Type of the tree:");

        container.getChildren().add(typeLabel);

        Scene scene = new Scene(container);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
