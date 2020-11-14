package unsw.gloriaromanus;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class GloriaRomanusApplication extends Application {

  private static GloriaRomanusController controller;
  private static MainMenuController mainMenuController;
  @Override
  public void start(Stage stage) throws IOException {
    Media sound = new Media(new File("GameMusic.mp3").toURI().toString());
    MediaPlayer mp = new MediaPlayer(sound);
    
    
    // set up the scene
    FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
    controller = new GloriaRomanusController("NEWGAME", null);
    loader.setController(controller);
    Parent root = loader.load();
    // controller = loader.getController();
    Scene gameScene = new Scene(root);

    // load main menu
    FXMLLoader mainMenuLoader = new FXMLLoader(getClass().getResource("main_menu.fxml"));
    Parent mainMenuRoot = mainMenuLoader.load();
    Scene mainMenuScene = new Scene(mainMenuRoot);
    mainMenuController = mainMenuLoader.getController();
    mainMenuController.setStage(stage);
    mainMenuController.setGameScene(gameScene);
    mainMenuController.setGameSceneController(controller);
    mainMenuController.setScene(mainMenuScene);
    
    controller.setStage(stage);
    controller.setMainMenuScene(mainMenuScene);
    

    // set up the stage
    stage.setTitle("Gloria Romanus");
    // stage.setWidth(800);
    // stage.setHeight(700);
    stage.setWidth(1000);
    stage.setHeight(875);
    stage.setScene(mainMenuScene);
    stage.show();

    mp.play();

  }

  /**
   * Stops and releases all resources used in application.
   */
  @Override
  public void stop() {
    controller.terminate();
  }

  /**
   * Opens and runs application.
   *
   * @param args arguments passed to this application
   */
  public static void main(String[] args) {

    Application.launch(args);
  }
}