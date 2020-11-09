package unsw.gloriaromanus;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainMenuController {
    @FXML
    private Button startButton;

    @FXML
    private Button loadButton;

    private Stage stage;
    private Scene gameScene;

    @FXML
    public void initialize() {
        startButton.setOnAction(e -> stage.setScene(gameScene));

    }

    @FXML 
    public void handleStartButton(ActionEvent e) throws IOException {
        
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setGameScene(Scene gameScene) {
        this.gameScene = gameScene;
    }
}
