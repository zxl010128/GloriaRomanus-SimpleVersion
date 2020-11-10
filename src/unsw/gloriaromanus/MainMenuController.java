package unsw.gloriaromanus;

import java.io.IOException;
import java.io.File;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
    private Scene gameScene;

    @FXML
    public void initialize() {

        // Set Background Picture
        Image image = new Image(new File("images/Background.png").toURI().toString());
        background.setImage(image);

        // Set button
        startButton.setOnAction(e -> showStage());        

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

    // A pop up window which shows the info of this game and a start playing button.
    public void showStage(){
        Stage newStage = new Stage();

        VBox box = new VBox();

        Label title = new Label();
        title.setText("Notice!\n");
        title.setTextAlignment(TextAlignment.CENTER);
        title.setFont(new Font(20));

        Label text = new Label();
        text.setText("        Welcome to Gloria Romanus! This a grand-strategy game, set in the time of Ancient Rome (around 200 BC), where the player can play as a faction of the time, with the overall goal of conquering all provinces in the game map (or succeeding at another grand victory objective). The game is also a turn-based game - each faction engages in actions during sequential turns, where they perform all actions to manage their armies and provinces for a single year in their turn, before clicking End turn, which will result in all other factions performing their turns sequentially.\n");
        text.setWrapText(true);

        Label condition = new Label();
        condition.setText("        Victory Conditions: 1. Conquering all territories (CONQUEST goal) 2. Accumulating a treasury balance of 100,000 gold (TREASURY goal) 3. Accumulating faction wealth of 400,000 gold (WEALTH goal)");
        condition.setWrapText(true);

        Button ok = new Button();
        ok.setText("Start Playing!");
        ok.setOnAction(e -> {stage.setScene(gameScene); newStage.close();});

        box.getChildren().addAll(title, text, condition, ok);
        box.setAlignment(Pos.CENTER);
        box.setSpacing(10.0);
        box.setOpacity(0.85);

        Scene stageScene = new Scene(box, 500, 300);
        stageScene.setFill(Color.TRANSPARENT);
        newStage.initStyle(StageStyle.TRANSPARENT);
        newStage.setScene(stageScene);
        newStage.show();
    }

}
