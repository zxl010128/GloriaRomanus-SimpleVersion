package unsw.gloriaromanus;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.io.File;

public class MainMenuController {
    @FXML
    private Button startButton;

    @FXML
    private Button loadButton;
    
    @FXML
    public ImageView background;

    private Stage stage;
    private Scene gameScene;

    @FXML
    public void initialize() {
        Image image = new Image(new File("images/Background.png").toURI().toString());
        background.setImage(image);  
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
