package unsw.gloriaromanus;

import java.io.IOException;
import java.io.File;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.print.PrintColor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.geometry.Insets;

public class MainMenuController {
    @FXML
    private Button startButton;

    @FXML
    private Button loadButton;

    @FXML
    private Button ok;

    @FXML
    private Button startGame;

    @FXML
    public ImageView background;

    private Stage stage;
    private Scene scene;
    private Scene gameScene;
    private GloriaRomanusController gameSceneController;

    @FXML
    public void initialize() {

        // Set Background Picture
        Image image = new Image(new File("images/Background.png").toURI().toString());
        background.setImage(image);

        // Set button
        startButton.setOnAction(e -> showStage());
        loadButton.setOnAction(e -> showLoadStage());

    }

    @FXML
    public void handleStartButton(ActionEvent e) throws IOException {
        showStage();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setGameScene(Scene gameScene) {
        this.gameScene = gameScene;
    }

    public void setGameSceneController(GloriaRomanusController gameSceneController) {
        this.gameSceneController = gameSceneController;
    }

    public Stage getStage() {
        return stage;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public Scene getScene() {
        return scene;
    }

    // A pop up window which shows the info of this game and a start playing button.
    public void showStage() {
        Stage newStage = new Stage();

        VBox box = new VBox();

        Label title = new Label();
        title.setText("Notice!\n");
        title.setTextAlignment(TextAlignment.CENTER);
        title.setFont(new Font(24));
        title.setStyle("-fx-text-fill: white");

        Label text = new Label();
        text.setText("        Welcome to Gloria Romanus! This a grand-strategy game, set in the time of Ancient Rome (around 200 BC), where the player can play as a faction of the time, with the overall goal of conquering all provinces in the game map (or succeeding at another grand victory objective). The game is also a turn-based game - each faction engages in actions during sequential turns, where they perform all actions to manage their armies and provinces for a single year in their turn, before clicking End turn, which will result in all other factions performing their turns sequentially.\n");
        text.setWrapText(true);
        text.setFont(new Font(15));
        text.setStyle("-fx-text-fill: white");

        Label condition = new Label();
        condition.setText("    Victory Conditions:\n    1. Conquering all territories (CONQUEST goal)\n    2. Accumulating a treasury balance of 100,000 gold (TREASURY goal)\n    3. Accumulating faction wealth of 400,000 gold (WEALTH goal)");
        condition.setFont(new Font(15));
        condition.setStyle("-fx-text-fill: white");

        Button ok = new Button();
        ok.setText("Start Playing!");
        ok.setOnAction(e -> {
            stage.setScene(gameScene);
            // gameSceneController = new GloriaRomanusController();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
            try {
                // set up the controller for loader
                gameSceneController = new GloriaRomanusController("NEWGAME");
                loader.setController(gameSceneController);
                Parent root = loader.load();

                gameScene = new Scene(root);
                this.setGameScene(gameScene);
                this.setGameSceneController(gameSceneController);
                int playerNum = gameSceneController.getGameSystem().getPlayerNum();
                gameSceneController.setStage(this.getStage());
                gameSceneController.setMainMenuScene(this.getScene());

            } catch (Exception exception) {
                // exception.printStackTrace();
            }
 
            newStage.close();
        });
        ok.setStyle("-fx-background-color: white");

        box.getChildren().addAll(title, text, condition, ok);
        box.setAlignment(Pos.CENTER);
        box.setSpacing(25.0);
        box.setPadding(new Insets(10,10,10,10));
        box.setStyle("-fx-background-color: transparent");

        Scene stageScene = new Scene(box, 600, 370);
        stageScene.setFill(Color.BLACK);
        newStage.initStyle(StageStyle.TRANSPARENT);
        newStage.setOpacity(0.95);
        newStage.setScene(stageScene);
        newStage.show();
    }

    public void showLoadStage() {
        Stage newStage = new Stage();
        // Pane pane = new Pane();
        Label title = new Label();
        VBox box = new VBox();
        Button loadGameButton = new Button();
        loadGameButton.setDisable(true);

        // text: Saved games:
        title.setText("Saved games:");
        title.setAlignment(Pos.TOP_LEFT);
        title.setFont(new Font(24));
        title.setStyle("-fx-text-fill: white");
        
        // listview to store saved games
        ListView<String> savedGames = new ListView<String>();
        String[] savedFiles = new File("SavedData/").list();
        int savedGamesNum = savedFiles.length;
        
        for (int i = 0; i < savedGamesNum; i++) {
            savedGames.getItems().add(savedFiles[i]);
        }

        savedGames.setOnMouseClicked(e -> {
            loadGameButton.setDisable(false);
        });

        // Load button
        loadGameButton.setText("Load");
        loadGameButton.setPrefWidth(150);
        loadGameButton.setOnAction(e -> {
            // load the game
            stage.setScene(gameScene);
            String fileToLoad = savedGames.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
            try {
                // setup the controller for loader
                gameSceneController = new GloriaRomanusController("LOADGAME");
                loader.setController(gameSceneController);
                Parent root = loader.load();
                

                gameScene = new Scene(root);
                this.setGameScene(gameScene);
                this.setGameSceneController(gameSceneController);

                // activate all disabled buttons
                gameSceneController.setStage(this.getStage());
                gameSceneController.setMainMenuScene(this.getScene());

                // gameSceneController.loadFile(fileToLoad);
                // gameSceneController.getPlayerNumButton().fire();
                // gameSceneController.initialMap(gameSceneController.getGameSystem().getPlayerNum());
                
                newStage.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });

        box.getChildren().addAll(title, savedGames, loadGameButton);
        box.setSpacing(25.0);
        box.setPadding(new Insets(10,10,10,10));
        box.setStyle("-fx-background-color: transparent");

        Scene stageScene = new Scene(box, 600, 370);
        stageScene.setFill(Color.BLACK);
        newStage.initStyle(StageStyle.TRANSPARENT);
        newStage.setOpacity(0.95);
        newStage.setScene(stageScene);
        newStage.show();
    }
    
}
