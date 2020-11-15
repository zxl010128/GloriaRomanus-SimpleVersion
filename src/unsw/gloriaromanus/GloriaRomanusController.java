package unsw.gloriaromanus;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import unsw.gloriaromanus.backend.GameSystem;
import unsw.gloriaromanus.backend.Province;
import unsw.gloriaromanus.backend.TurnTracker;
import unsw.gloriaromanus.backend.Army;
import unsw.gloriaromanus.backend.Faction;
import unsw.gloriaromanus.backend.Unit;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.FeatureTable;
import com.esri.arcgisruntime.data.GeoPackage;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
// import com.esri.arcgisruntime.geometry.Unit;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.IdentifyLayerResult;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol.HorizontalAlignment;
import com.esri.arcgisruntime.symbology.TextSymbol.VerticalAlignment;
import com.esri.arcgisruntime.data.Feature;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.geojson.FeatureCollection;
import org.geojson.LngLatAlt;
import org.json.JSONArray;
import org.json.JSONObject;

import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.ChoiceBox;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import static java.util.Map.entry;

public class GloriaRomanusController{
  /*
  @FXML
  private Button treasuryTest;
  @FXML
  private Button wealthTest;
  */
  @FXML
  private MapView mapView;
  @FXML
  private TextField invading_province;
  @FXML
  private TextField opponent_province;
  @FXML
  private TextArea output_terminal;
  @FXML
  private Button quitButton;
  @FXML
  private Button playerNumButton;
  @FXML
  private Label factionLabel;
  @FXML
  private Label victoryConditionLabel;
  @FXML
  private Label victoryCondition;
  @FXML
  private Label treasuryLabel;
  @FXML
  private Label wealthLabel;
  @FXML
  private Label yearLabel;
  @FXML
  private Label turnLabel;
  @FXML
  private Label provincesLabel;
  @FXML
  private Label armyLabel;
  @FXML
  private VBox menu;
  @FXML
  private Button endTurnButton;
  @FXML
  private Button saveButton;
  @FXML
  private Button invadeButton;
  @FXML
  private Button setTaxButton;
  @FXML
  private ChoiceBox<String> occupiedProvinces;
  @FXML
  private ChoiceBox<String> recruitableUnits;
  @FXML
  private Button recruitButton;
  @FXML
  private Button formArmyButton;
  @FXML
  private Button ArmyDisbandButton;
  @FXML
  private Button myProvinceButton;
  @FXML
  private Button destinationButton;
  @FXML
  private Button unitListButton;
  @FXML
  private ChoiceBox<String> availableUnits;

  private List<String> ArmyActiveProvince = new ArrayList<String>();

  private List<List<Unit>> unitCount = new ArrayList<List<Unit>>();

  private String FirstPlayerFaction;

  private Integer turnPlayerCount;

  private ArcGISMap map;

  private Map<String, String> provinceToOwningFactionMap;

  private Map<String, Integer> provinceToNumberTroopsMap;

  private String humanFaction;

  private Feature currentlySelectedHumanProvince;
  private Feature currentlySelectedDestination;
  private Feature currentlySelectedProvince;

  private FeatureLayer featureLayer_provinces;

  private Stage stage;
  private Scene mainMenuScene;

  private GameSystem gameSystem;
  private TurnTracker turnTracker;
  private Faction currFaction;
  private int playerNum;

  private static String NEWGAME = "NEWGAME";
  private static String LOADGAME = "LOADGAME";
  private String gameType;
  private String fileToLoad = null;

  private String[] recruitableUnitsList = {
    "Roman legionary",
    "Gallic berserker",
    "Celtic Briton berserker",
    "Germanic berserker",
    "melee cavalry",
    "pikemen",
    "hoplite",
    "javelin-skirmisher",
    "elephant",
    "horse-archer",
    "druid",
    "melee infantry",
    "heavy infantry",
    "missile infantry",
    "chariots"
  };

  private Map<String, Integer> unitTurns = Map.ofEntries(
    entry("Roman legionary", 3),
    entry("Gallic berserker", 2),
    entry("Celtic Briton berserker", 2),
    entry("Germanic berserker", 2),
    entry("melee cavalry", 1),
    entry("pikemen", 1),
    entry("hoplite", 1),
    entry("javelin-skirmisher", 1),
    entry("elephant", 3),
    entry("horse-archer", 1),
    entry("druid", 2),
    entry("melee infantry", 1),
    entry("heavy infantry", 1),
    entry("missile infantry", 1),
    entry("chariots", 2)
  );

  public GloriaRomanusController(String gameType, String fileToLoad) {
    this.gameType = gameType;
    if (gameType.equals("LOADGAME")) {
      this.fileToLoad = fileToLoad;
    }

  }

  public void setGameType(String gameType) {
    this.gameType = gameType;
  }
  
  @FXML
  public void initialize() throws JsonParseException, JsonMappingException, IOException {
    if (gameType.equals(NEWGAME)) {
      // All factions will start with no soldiers
      provinceToOwningFactionMap = getProvinceToOwningFactionMap();

      provinceToNumberTroopsMap = new HashMap<String, Integer>();

      for (String provinceName : provinceToOwningFactionMap.keySet()) {
        provinceToNumberTroopsMap.put(provinceName, 0);
      }

      // select in loading screen)

      currentlySelectedHumanProvince = null;
      currentlySelectedDestination = null;

      // Enable all the button except PlayerNumButton
      endTurnButton.setDisable(true);
      saveButton.setDisable(true);
      invadeButton.setDisable(true);
      quitButton.setDisable(true);
      recruitButton.setDisable(true);
      setTaxButton.setDisable(true);
      formArmyButton.setDisable(true);
      ArmyDisbandButton.setDisable(true);
      occupiedProvinces.setDisable(true);
      recruitableUnits.setDisable(true);
      availableUnits.setDisable(true);
      myProvinceButton.setDisable(true);
      destinationButton.setDisable(true);
      unitListButton.setDisable(true);

      playerNumButton.setOnAction(e -> {
        showStage();
        setTaxButton.setDisable(false);
        playerNumButton.setDisable(true);
        endTurnButton.setDisable(false);
        saveButton.setDisable(false);
        invadeButton.setDisable(false);
        quitButton.setDisable(false);
        recruitButton.setDisable(false);
        formArmyButton.setDisable(false);
        occupiedProvinces.setDisable(false);
        recruitableUnits.setDisable(false);
        availableUnits.setDisable(false);
        myProvinceButton.setDisable(false);
        destinationButton.setDisable(false);
      });
    } else if (gameType.equals(LOADGAME)) {
      provinceToOwningFactionMap = getProvinceToOwningFactionMap();

      provinceToNumberTroopsMap = new HashMap<String, Integer>();

      for (String provinceName : provinceToOwningFactionMap.keySet()) {
        provinceToNumberTroopsMap.put(provinceName, 0);
      }

      currentlySelectedHumanProvince = null;
      currentlySelectedDestination = null;

      gameSystem = new GameSystem();
      // load the previously saved file
      this.loadFile(fileToLoad);

      playerNumButton.setDisable(true);

      // update province info on map
      for (Province p : gameSystem.getProvinces()) {
        provinceToOwningFactionMap.put(p.getName(), p.getFactionName());
      }

      // update unit number on map
      for (Province p : gameSystem.getProvinces()) {
        provinceToNumberTroopsMap.put(p.getName(), p.getNumOfSoldiers());
      }

      initializeProvinceLayers();


      quitButton.setOnAction(e -> {
        stage.setScene(mainMenuScene);
      });

      saveButton.setOnAction(e -> {
        if (ArmyActiveProvince.size() != 0) {
          printMessageToTerminal(String.format("You could not end your turn because you have active army in the province %s", ArmyActiveProvince.toString()));
        } else {
          this.save();
          printMessageToTerminal("Game has been saved!");
        }
      });

      provincesLabel.setText("Provinces Conquered: " + String.valueOf(currFaction.getProvinces().size()) +  " / " + "53");
      if (gameSystem.conditionToString().contains("WEALTH")) {
        wealthLabel.setText("Wealth: " + String.valueOf(currFaction.getTotalWealth()) + " / 400,000");
      } else {
        wealthLabel.setText("Wealth: " + String.valueOf(currFaction.getTotalWealth()));
      }
  
      if (gameSystem.conditionToString().contains("TREASURY")) {
        treasuryLabel.setText("Gold: " + String.valueOf(currFaction.getBalance()) + " / 100,000");
      } else {
        treasuryLabel.setText("Gold: " + String.valueOf(currFaction.getBalance()));
      }

      // add a Listview to display occupied provinces
      occupiedProvinces.setPrefWidth(200);
      for (Province p : currFaction.getProvinces()) {
        occupiedProvinces.getItems().add(p.getName());
      }

      // recruit button won't be available until a province is selected
      recruitButton.setDisable(true);
      recruitableUnits.setDisable(true);
      availableUnits.setDisable(true);
      formArmyButton.setDisable(true);
      ArmyDisbandButton.setDisable(true);
      occupiedProvinces.setOnAction(e -> {
        if (occupiedProvinces.getSelectionModel().getSelectedItem() != null) {
          // display units to availableUnits
          String provinceName = occupiedProvinces.getSelectionModel().getSelectedItem();
          // Province selectedProvince = gameSystem.getProvincesTracker().getProvince(provinceName);
          Province selectedProvince = gameSystem.checkStringinProvince(provinceName);
          List<Unit> provinceUnitList = selectedProvince.getUnits();
          
          // availableUnits = new ChoiceBox<String>();
          // availableUnits.setPrefWidth(125);
          availableUnits.getItems().clear();
          unitCount.clear();
          if (provinceUnitList != null) {
            for (Unit u : provinceUnitList) {
              if (!availableUnits.getItems().contains(u.getName())) {
                availableUnits.getItems().add(u.getName());
              }
              
              if (unitCount.size() == 0) {
                unitCount.add(new ArrayList<Unit>());
                unitCount.get(0).add(u);
              } else {
  
                boolean is_found = false;
                for (int i = 0; i < unitCount.size(); i++) {
                  if (unitCount.get(i).get(0).getName().equals(u.getName())) {
                    unitCount.get(i).add(u);
                    is_found = true;
                  } 
                }
  
                if (is_found == false) {
                  List<Unit> newUnit = new ArrayList<>();
                  newUnit.add(u);
                  unitCount.add(newUnit);
                }
              }
            }
          }
  
          if (ArmyActiveProvince.contains(selectedProvince.getName())) {
            armyLabel.setText("Army Status: active");
            ArmyDisbandButton.setDisable(false);
          } else {
            armyLabel.setText("Army Status: Inactive");
            ArmyDisbandButton.setDisable(true);
          }
          unitListButton.setDisable(false);
          recruitableUnits.setDisable(false);
          availableUnits.setDisable(false);
        }
      });
  
      availableUnits.setOnAction(e -> {
        formArmyButton.setDisable(false);
      });
  
      // add a Listview to display recruitable soldiers
      recruitableUnits.setPrefWidth(200);
      for (String s : recruitableUnitsList) {
        recruitableUnits.getItems().add(s);
      }
      recruitableUnits.setOnAction(e -> {
        recruitButton.setDisable(false);
      });
  
      output_terminal.setWrapText(true);
      recruitButton.setOnAction(e -> {
        // recruit a selected unit to selected province
        String provinceName = occupiedProvinces.getSelectionModel().getSelectedItem();
        String unitName = recruitableUnits.getValue();
        Province selectedProvince = gameSystem.checkStringinProvince(provinceName);
        
        if (selectedProvince.recruit(unitName, turnTracker.getCurrTurn())) {
          int finishTurn = turnTracker.getCurrTurn() + unitTurns.get(unitName);
          printMessageToTerminal(String.format("%s: %s trains a %s (will be available on %d)", 
            currFaction.getName(), provinceName, unitName, finishTurn));
        } else {
          printMessageToTerminal(String.format("%s: %s can't train a %s unit now, because insuffient gold or full training)", 
            currFaction.getName(), provinceName, unitName));
        }
  
        if (gameSystem.conditionToString().contains("TREASURY")) {
          treasuryLabel.setText("Gold: " + String.valueOf(currFaction.getBalance()) + " / 100,000");
        } else {
          treasuryLabel.setText("Gold: " + String.valueOf(currFaction.getBalance()));
        }
        // manage button and choicebox
        recruitButton.setDisable(true);
        //occupiedProvinces.getSelectionModel().clearSelection();
        recruitableUnits.getSelectionModel().clearSelection();
        //recruitableUnits.setDisable(true);
      });

    }
    
  }

  // A pop up window to be able for player to choose player number
  public void showStage(){
    
    Stage newStage = new Stage();

    VBox box = new VBox();

    Label title = new Label();
    title.setText("Please Select the player number!\n");
    title.setTextAlignment(TextAlignment.CENTER);
    title.setFont(new Font(20));
    title.setStyle("-fx-text-fill: white");
    
    ChoiceBox<Integer> cb = new ChoiceBox<>();
    cb.getItems().addAll(2,3,4,5,6,7,8,9,10,11,12,13,14,15,16);

    Button ok = new Button();
    ok.setText("Confirm");
    ok.setOnAction(e -> {
      try {
        if (cb.getSelectionModel().getSelectedItem() == null) {
          printMessageToTerminal("Please select the player number!");         
        } else {
          playerNum = cb.getSelectionModel().getSelectedItem();
          initialMap(playerNum);
          newStage.close();
        }
    } catch (Exception exception) {
      throw new RuntimeException(exception);

    }});

    box.getChildren().addAll(title, cb, ok);
    box.setAlignment(Pos.CENTER);
    box.setStyle("-fx-background-color: transparent");
    box.setSpacing(40.0);

    Scene stageScene = new Scene(box, 500, 300);
    stageScene.setFill(Color.BLACK);
    newStage.initStyle(StageStyle.TRANSPARENT);
    newStage.setOpacity(0.85);
    newStage.setScene(stageScene);
    newStage.show();
}

  public void initialMap(Integer playerNumber) throws JsonParseException, JsonMappingException, IOException{
    // new code
    gameSystem = new GameSystem();
    gameSystem.setPlayerNum(playerNumber);
    gameSystem.allocateFaction();
    // assume current player is the first faction in factions list
    currFaction = gameSystem.getFactions().get(0);
    turnTracker = gameSystem.getTurnTracker();

    humanFaction = currFaction.getName();
    FirstPlayerFaction = currFaction.getName();
    turnPlayerCount = 0;
    initializeProvinceLayers();

    quitButton.setOnAction(e -> {
      stage.setScene(mainMenuScene);
    });

    saveButton.setOnAction(e -> {
      if (ArmyActiveProvince.size() != 0) {
        printMessageToTerminal(String.format("You could not end your turn because you have active army in the province %s", ArmyActiveProvince.toString()));
      } else {
        this.save();
        printMessageToTerminal("Game has been saved!");
      }
    });

    factionLabel.setText("Faction: " + currFaction.getName());
    yearLabel.setText(String.valueOf(gameSystem.getYear()));
    turnLabel.setText(String.valueOf(turnTracker.getCurrTurn()));
    victoryCondition.setText(gameSystem.conditionToString());
    provincesLabel.setText("Provinces Conquered: " + String.valueOf(currFaction.getProvinces().size()) +  " / " + "53");

    if (gameSystem.conditionToString().contains("WEALTH")) {
      wealthLabel.setText("Wealth: " + String.valueOf(currFaction.getTotalWealth()) + " / 400,000");
    } else {
      wealthLabel.setText("Wealth: " + String.valueOf(currFaction.getTotalWealth()));
    }

    if (gameSystem.conditionToString().contains("TREASURY")) {
      treasuryLabel.setText("Gold: " + String.valueOf(currFaction.getBalance()) + " / 100,000");
    } else {
      treasuryLabel.setText("Gold: " + String.valueOf(currFaction.getBalance()));
    }

    // add a Listview to display occupied provinces
    occupiedProvinces.setPrefWidth(200);
    for (Province p : currFaction.getProvinces()) {
      occupiedProvinces.getItems().add(p.getName());
    }

    // recruit button won't be available until a province is selected
    recruitButton.setDisable(true);
    recruitableUnits.setDisable(true);
    availableUnits.setDisable(true);
    formArmyButton.setDisable(true);
    ArmyDisbandButton.setDisable(true);

    occupiedProvinces.setOnAction(e -> {
      if (occupiedProvinces.getSelectionModel().getSelectedItem() != null) {
        // display units to availableUnits
        String provinceName = occupiedProvinces.getSelectionModel().getSelectedItem();
        // Province selectedProvince = gameSystem.getProvincesTracker().getProvince(provinceName);
        Province selectedProvince = currFaction.getProvinceByName(provinceName);
        List<Unit> provinceUnitList = selectedProvince.getUnits();
        
        // availableUnits = new ChoiceBox<String>();
        // availableUnits.setPrefWidth(125);
        availableUnits.getItems().clear();
        unitCount.clear();
        if (provinceUnitList != null) {
          for (Unit u : provinceUnitList) {
            if (!availableUnits.getItems().contains(u.getName())) {
              availableUnits.getItems().add(u.getName());
            }
            
            if (unitCount.size() == 0) {
              unitCount.add(new ArrayList<Unit>());
              unitCount.get(0).add(u);
            } else {

              boolean is_found = false;
              for (int i = 0; i < unitCount.size(); i++) {
                if (unitCount.get(i).get(0).getName().equals(u.getName())) {
                  unitCount.get(i).add(u);
                  is_found = true;
                } 
              }

              if (is_found == false) {
                List<Unit> newUnit = new ArrayList<>();
                newUnit.add(u);
                unitCount.add(newUnit);
              }
            }
          }
        }

        if (ArmyActiveProvince.contains(selectedProvince.getName())) {
          armyLabel.setText("Army Status: active");
          ArmyDisbandButton.setDisable(false);
        } else {
          armyLabel.setText("Army Status: Inactive");
          ArmyDisbandButton.setDisable(true);
        }
        unitListButton.setDisable(false);
        recruitableUnits.setDisable(false);
        availableUnits.setDisable(false);
      }
    });

    availableUnits.setOnAction(e -> {
      formArmyButton.setDisable(false);
    });

    // add a Listview to display recruitable soldiers
    recruitableUnits.setPrefWidth(200);
    for (String s : recruitableUnitsList) {
      recruitableUnits.getItems().add(s);
    }
    recruitableUnits.setOnAction(e -> {
      recruitButton.setDisable(false);
    });

    output_terminal.setWrapText(true);
    recruitButton.setOnAction(e -> {
      // recruit a selected unit to selected province
      String provinceName = occupiedProvinces.getSelectionModel().getSelectedItem();
      String unitName = recruitableUnits.getValue();
      Province selectedProvince = currFaction.getProvinceByName(provinceName);
      
      if (selectedProvince.recruit(unitName, turnTracker.getCurrTurn())) {
        int finishTurn = turnTracker.getCurrTurn() + unitTurns.get(unitName);
        printMessageToTerminal(String.format("%s: %s trains a %s (will be available on %d)", 
          currFaction.getName(), provinceName, unitName, finishTurn));
      } else {
        printMessageToTerminal(String.format("%s: %s can't train a %s unit now, because insuffient gold or full training)", 
          currFaction.getName(), provinceName, unitName));
      }

      if (gameSystem.conditionToString().contains("TREASURY")) {
        treasuryLabel.setText("Gold: " + String.valueOf(currFaction.getBalance()) + " / 100,000");
      } else {
        treasuryLabel.setText("Gold: " + String.valueOf(currFaction.getBalance()));
      }
      // manage button and choicebox
      recruitButton.setDisable(true);
      //occupiedProvinces.getSelectionModel().clearSelection();
      recruitableUnits.getSelectionModel().clearSelection();
      //recruitableUnits.setDisable(true);
    });

    printMessageToTerminal("Hello, A new game started!");
    // printMessageToTerminal("Your assigned faction is " + currFaction.getName());
    printMessageToTerminal("There are " + gameSystem.getPlayerNum() + " players");
    printMessageToTerminal("Fully utilise your turn to strengthen your faction so that you can win the game eventually");
    printMessageToTerminal("Have fun!");
    // printMessageToTerminal(currFaction.getName() + "'s turn");
    printMessageToTerminal(String.format("Turn %d: %s's turn", turnTracker.getCurrTurn(), currFaction.getName()));

  }

  @FXML
  public void clickedInvadeButton(ActionEvent e) throws IOException {
    if (currentlySelectedHumanProvince != null && currentlySelectedDestination != null){
      String humanProvince = (String)currentlySelectedHumanProvince.getAttributes().get("name");
      String enemyProvince = (String)currentlySelectedDestination.getAttributes().get("name");
      Faction myFaction = gameSystem.getFactionByProvinceName(humanProvince);
      Faction enemyFaction = gameSystem.getFactionByProvinceName(enemyProvince);
      Province myProvince = gameSystem.checkStringinProvince(humanProvince);
      Province destProvince = gameSystem.checkStringinProvince(enemyProvince);


      if (gameSystem.getProvincesTracker().getProvince(humanProvince).getArmy().getUnits().size() == 0) {
        printMessageToTerminal("You don't have any units to use to invade");
        return;
      }

      myProvince.getArmy().setMovementPoint();

      if (invadeButton.getText().equals("Move")) {

        if (destProvince.getArmy().getUnits().size() != 0) {
          printMessageToTerminal(String.format("%s has already formed a Army, please disband it firstly", enemyProvince));
          return;
        }

        if (myProvince.getArmy().isReachable(destProvince) == true) {

          int currentMovePts = myProvince.getArmy().getMovementPoints();
          int movementCons = myProvince.getArmy().movementConsumption(destProvince);

          for (Unit u : myProvince.getArmy().getUnits()) {
            destProvince.addUnit(u);
            myProvince.removeUnit(u);
            u.setProvince(destProvince);
          }

          destProvince.setArmy(myProvince.getArmy());
          destProvince.getArmy().setProvinceName(enemyProvince);

          if (currentMovePts == movementCons) {
            destProvince.getArmy().setMovementPoints(-1);            
          } else {
            destProvince.getArmy().setMovementPoints(currentMovePts - movementCons);
          }

          myProvince.setArmy(new Army(myProvince));
          armyLabel.setText("Army Status: Inactive");
          ArmyActiveProvince.remove(humanProvince);
          
          ArmyActiveProvince.add(enemyProvince);

          occupiedProvinces.getSelectionModel().clearSelection();

          provinceToNumberTroopsMap.put(humanProvince, myProvince.getNumOfSoldiers());
          provinceToNumberTroopsMap.put(enemyProvince, destProvince.getNumOfSoldiers());

          printMessageToTerminal(String.format("You successfully move troops from %s to %s.", humanProvince, enemyProvince));
        
        } else {
          printMessageToTerminal("You cannot reach this destination because of insufficient movement pts.");

          if (myProvince.getArmy().getMovementPoints() == 0) {
            myProvince.getArmy().setMovementPoints(-1);
          }
        }

      } else if (invadeButton.getText().equals("Occupy")) { 
        
        if (myProvince.getArmy().isReachable(destProvince) == true) {

          int currentMovePts = myProvince.getArmy().getMovementPoints();
          int movementCons = myProvince.getArmy().movementConsumption(destProvince);
          myFaction.addProvince(destProvince);

          for (Unit u : myProvince.getArmy().getUnits()) {
            destProvince.addUnit(u);
            myProvince.removeUnit(u);
            u.setProvince(destProvince);
          }
  
          destProvince.setArmy(myProvince.getArmy());
          destProvince.getArmy().setProvinceName(enemyProvince);

          if (currentMovePts == movementCons) {
            destProvince.getArmy().setMovementPoints(-1);            
          } else {
            destProvince.getArmy().setMovementPoints(currentMovePts - movementCons);
          }

          myProvince.setArmy(new Army(myProvince));
          armyLabel.setText("Army Status: Inactive");
          ArmyActiveProvince.remove(myProvince.getName());
          
          ArmyActiveProvince.add(enemyProvince);

          occupiedProvinces.getItems().add(enemyProvince);
          occupiedProvinces.getSelectionModel().clearSelection();

          provinceToNumberTroopsMap.put(myProvince.getName(), myProvince.getNumOfSoldiers());
          provinceToNumberTroopsMap.put(enemyProvince, destProvince.getNumOfSoldiers());
          provinceToOwningFactionMap.put(enemyProvince, myFaction.getName());

          provincesLabel.setText("Provinces Conquered: " + String.valueOf(currFaction.getProvinces().size()) +  " / " + "53");

          if (gameSystem.conditionToString().contains("WEALTH")) {
            wealthLabel.setText("Wealth: " + String.valueOf(currFaction.getTotalWealth()) + " / 400,000");
          } else {
            wealthLabel.setText("Wealth: " + String.valueOf(currFaction.getTotalWealth()));
          }      
          checkWinGame(myFaction);
          printMessageToTerminal(String.format("You successfully move troops from %s to %s and occupy this province.", myFaction.getName(), enemyProvince));
          
        } else {
          printMessageToTerminal("You cannot reach this destination because of insufficient movement pts or the destination is not adjecent to you.");

          if (myProvince.getArmy().getMovementPoints() == 0) {
            myProvince.getArmy().setMovementPoints(-1);
          }
        }

      } else if (invadeButton.getText().equals("Invade")){

        if (myProvince.getArmy().isReachable(destProvince) == true) {

          int currentMovePts = myProvince.getArmy().getMovementPoints();
          int movementCons = myProvince.getArmy().movementConsumption(destProvince);

          int invadeResult = myProvince.getArmy().invade(destProvince);
          
          switch (invadeResult) {
            
            case -1:

              for (Unit u : myProvince.getArmy().getUnits()) {
                myProvince.removeUnit(u);
              }

              myProvince.setArmy(new Army(myProvince));
              armyLabel.setText("Army Status: Inactive");
              ArmyActiveProvince.remove(humanProvince);

              occupiedProvinces.getSelectionModel().clearSelection();

              provinceToNumberTroopsMap.put(humanProvince, myProvince.getNumOfSoldiers());
              provinceToNumberTroopsMap.put(enemyProvince, destProvince.getNumOfSoldiers());
              // fail
              printMessageToTerminal(String.format("DEFEATED: %s: %s failed to invade %s", currFaction.getName(), humanProvince, enemyProvince));
              break;
            
            case 0:
              // draw
              printMessageToTerminal(String.format("DRAW: %s: %s failed to invade %s", currFaction.getName(), humanProvince, enemyProvince));
              break;
            
            case 1:

              myFaction.addProvince(destProvince);
              enemyFaction.removeProvince(destProvince);

              destProvince.getUnits().clear();

              for (Unit u : myProvince.getArmy().getUnits()) {
                destProvince.addUnit(u);
                myProvince.removeUnit(u);
                u.setProvince(destProvince);
              }

              destProvince.setArmy(myProvince.getArmy());
              destProvince.getArmy().setProvinceName(enemyProvince);

              if (currentMovePts == movementCons) {
                destProvince.getArmy().setMovementPoints(-1);            
              } else {
                destProvince.getArmy().setMovementPoints(currentMovePts - movementCons);
              }

              myProvince.setArmy(new Army(myProvince));
              armyLabel.setText("Army Status: Inactive");
              ArmyActiveProvince.remove(humanProvince);
              
              ArmyActiveProvince.add(enemyProvince);

              occupiedProvinces.getItems().add(enemyProvince);
              occupiedProvinces.getSelectionModel().clearSelection();

              provinceToNumberTroopsMap.put(humanProvince, myProvince.getNumOfSoldiers());
              provinceToNumberTroopsMap.put(enemyProvince, destProvince.getNumOfSoldiers());

              printMessageToTerminal(String.format("Win: %s: %s just invaded %s successfully", currFaction.getName(), humanProvince, enemyProvince));

              checkLossGame(enemyFaction);
              checkWinGame(myFaction);
              provinceToOwningFactionMap.put(enemyProvince, humanFaction);
              // win
              break;
          }

          provincesLabel.setText("Provinces Conquered: " + String.valueOf(currFaction.getProvinces().size()) +  " / " + "53");

          if (gameSystem.conditionToString().contains("WEALTH")) {
            wealthLabel.setText("Wealth: " + String.valueOf(currFaction.getTotalWealth()) + " / 400,000");
          } else {
            wealthLabel.setText("Wealth: " + String.valueOf(currFaction.getTotalWealth()));
          }      
        
        } else {
          printMessageToTerminal("You cannot reach this destination because of insufficient movement pts or the destination is not adjecent to you.");
          
          if (myProvince.getArmy().getMovementPoints() == 0) {
            myProvince.getArmy().setMovementPoints(-1);
          }
        }

      }

        resetSelections();  // reset selections in UI
        addAllPointGraphics(); // reset graphics

    }
  }

  /**
   * run this initially to update province owner, change feature in each
   * FeatureLayer to be visible/invisible depending on owner. Can also update
   * graphics initially
   */
  private void initializeProvinceLayers() throws JsonParseException, JsonMappingException, IOException {

    Basemap myBasemap = Basemap.createImagery();
    // myBasemap.getReferenceLayers().remove(0);
    map = new ArcGISMap(myBasemap);
    mapView.setMap(map);

    // note - tried having different FeatureLayers for AI and human provinces to
    // allow different selection colors, but deprecated setSelectionColor method
    // does nothing
    // so forced to only have 1 selection color (unless construct graphics overlays
    // to give color highlighting)
    GeoPackage gpkg_provinces = new GeoPackage("src/unsw/gloriaromanus/provinces_right_hand_fixed.gpkg");
    gpkg_provinces.loadAsync();
    gpkg_provinces.addDoneLoadingListener(() -> {
      if (gpkg_provinces.getLoadStatus() == LoadStatus.LOADED) {
        // create province border feature
        featureLayer_provinces = createFeatureLayer(gpkg_provinces);
        map.getOperationalLayers().add(featureLayer_provinces);

      } else {
        System.out.println("load failure");
      }
    });

    addAllPointGraphics();
  }

  private void addAllPointGraphics() throws JsonParseException, JsonMappingException, IOException {
    mapView.getGraphicsOverlays().clear();

    InputStream inputStream = new FileInputStream(new File("src/unsw/gloriaromanus/provinces_label.geojson"));
    FeatureCollection fc = new ObjectMapper().readValue(inputStream, FeatureCollection.class);

    GraphicsOverlay graphicsOverlay = new GraphicsOverlay();

    for (org.geojson.Feature f : fc.getFeatures()) {
      if (f.getGeometry() instanceof org.geojson.Point) {
        org.geojson.Point p = (org.geojson.Point) f.getGeometry();
        LngLatAlt coor = p.getCoordinates();
        Point curPoint = new Point(coor.getLongitude(), coor.getLatitude(), SpatialReferences.getWgs84());
        PictureMarkerSymbol s = null;
        String province = (String) f.getProperty("name");
        String faction = provinceToOwningFactionMap.get(province);
        
        if (!gameSystem.checkStringinFaction(faction)) {
          continue;
        }

        TextSymbol t = new TextSymbol(10,
            faction + "\n" + province + "\n" + provinceToNumberTroopsMap.get(province), 0xFFFF0000,
            HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM);

        switch (faction) {
          case "Gauls":
            // note can instantiate a PictureMarkerSymbol using the JavaFX Image class - so could
            // construct it with custom-produced BufferedImages stored in Ram
            // http://jens-na.github.io/2013/11/06/java-how-to-concat-buffered-images/
            // then you could convert it to JavaFX image https://stackoverflow.com/a/30970114

            // you can pass in a filename to create a PictureMarkerSymbol...
            s = new PictureMarkerSymbol(new Image((new File("images/FactionFlags/Celtic_Druid.png")).toURI().toString()));
            break;
          case "Romans":
            // you can also pass in a javafx Image to create a PictureMarkerSymbol (different to BufferedImage)
            s = new PictureMarkerSymbol("images/FactionFlags/legionary.png");
            break;
          
          case "Carthaginians":

            s = new PictureMarkerSymbol("images/FactionFlags/Carthaginians.png");
            break;

          case "Celtic Britons":

            s = new PictureMarkerSymbol("images/FactionFlags/Celtic_Britons.png");
            break;
            
          case "Spanish":

            s = new PictureMarkerSymbol("images/FactionFlags/Spanish.png");
            break;

          case "Numidians":

            s = new PictureMarkerSymbol("images/FactionFlags/Numidians.png");
            break;
          
          case "Egyptians":

            s = new PictureMarkerSymbol("images/FactionFlags/Egyptians.png");
            break;

          case "Seleucid Empire":
            
            s = new PictureMarkerSymbol("images/FactionFlags/Seleucid_Empire.png");
            break;

          case "Pontus":

            s = new PictureMarkerSymbol("images/FactionFlags/Pontus.png");
            break;

          case "Amenians":

            s = new PictureMarkerSymbol("images/FactionFlags/Amenians.png");
            break;

          case "Parthians":

            s = new PictureMarkerSymbol("images/FactionFlags/Parthians.png");
            break;

          case "Germanics":

            s = new PictureMarkerSymbol("images/FactionFlags/Germanics.png");
            break;

          case "Greek City States":

            s = new PictureMarkerSymbol("images/FactionFlags/GreekCityStates.png");
            break;

          case "Macedonians":

            s = new PictureMarkerSymbol("images/FactionFlags/Macedonians.png");
            break;
          
          case "Thracians":

            s = new PictureMarkerSymbol("images/FactionFlags/Thracians.png");
            break;

          case "Dacians":
            s = new PictureMarkerSymbol("images/FactionFlags/Dacians.png");
            break;


        }
        t.setHaloColor(0xFFFFFFFF);
        t.setHaloWidth(2);
        Graphic gPic = new Graphic(curPoint, s);
        Graphic gText = new Graphic(curPoint, t);
        graphicsOverlay.getGraphics().add(gPic);
        graphicsOverlay.getGraphics().add(gText);
      } else {
        System.out.println("Non-point geo json object in file");
      }

    }

    inputStream.close();
    mapView.getGraphicsOverlays().add(graphicsOverlay);
  }

  private FeatureLayer createFeatureLayer(GeoPackage gpkg_provinces) {
    FeatureTable geoPackageTable_provinces = gpkg_provinces.getGeoPackageFeatureTables().get(0);

    // Make sure a feature table was found in the package
    if (geoPackageTable_provinces == null) {
      System.out.println("no geoPackageTable found");
      return null;
    }

    // Create a layer to show the feature table
    FeatureLayer flp = new FeatureLayer(geoPackageTable_provinces);

    // https://developers.arcgis.com/java/latest/guide/identify-features.htm
    // listen to the mouse clicked event on the map view
    mapView.setOnMouseClicked(e -> {
      // was the main button pressed?
      if (e.getButton() == MouseButton.PRIMARY) {
        // get the screen point where the user clicked or tapped
        Point2D screenPoint = new Point2D(e.getX(), e.getY());

        // specifying the layer to identify, where to identify, tolerance around point,
        // to return pop-ups only, and
        // maximum results
        // note - if select right on border, even with 0 tolerance, can select multiple
        // features - so have to check length of result when handling it
        final ListenableFuture<IdentifyLayerResult> identifyFuture = mapView.identifyLayerAsync(flp,
            screenPoint, 0, false, 25);

        // add a listener to the future
        identifyFuture.addDoneListener(() -> {
          try {
            // get the identify results from the future - returns when the operation is
            // complete
            IdentifyLayerResult identifyLayerResult = identifyFuture.get();
            // a reference to the feature layer can be used, for example, to select
            // identified features
            if (identifyLayerResult.getLayerContent() instanceof FeatureLayer) {
              FeatureLayer featureLayer = (FeatureLayer) identifyLayerResult.getLayerContent();
              // select all features that were identified
              List<Feature> features = identifyLayerResult.getElements().stream().map(f -> (Feature) f).collect(Collectors.toList());

              if (features.size() > 1){
                printMessageToTerminal("Have more than 1 element - you might have clicked on boundary!");
              }
              else if (features.size() == 1){
                // note maybe best to track whether selected...
                Feature f = features.get(0);

                if (currentlySelectedProvince != null) {
                  featureLayer.unselectFeature(currentlySelectedProvince);
                }
                
                currentlySelectedProvince = f;
                featureLayer.selectFeature(f);
                
              }

              
            }
          } catch (InterruptedException | ExecutionException ex) {
            // ... must deal with checked exceptions thrown from the async identify
            // operation
            System.out.println("InterruptedException occurred");
          }
        });
      }
    });
    return flp;
  }

  private Map<String, String> getProvinceToOwningFactionMap() throws IOException {
    String content = Files.readString(Paths.get("src/unsw/gloriaromanus/backend/factions_list.json"));
    JSONObject ownership = new JSONObject(content);
    Map<String, String> m = new HashMap<String, String>();
    for (String key : ownership.keySet()) {
      // key will be the faction name
      JSONArray ja = ownership.getJSONArray(key);
      // value is province name
      for (int i = 0; i < ja.length(); i++) {
        String value = ja.getString(i);
        m.put(value, key);
      }
    }
    return m;
  }

  private ArrayList<String> getHumanProvincesList() throws IOException {
    // https://developers.arcgis.com/labs/java/query-a-feature-layer/

    String content = Files.readString(Paths.get("src/unsw/gloriaromanus/backend/factions_list.json"));
    JSONObject ownership = new JSONObject(content);
    return ArrayUtil.convert(ownership.getJSONArray(humanFaction));
  }

  /**
   * returns query for arcgis to get features representing human provinces can
   * apply this to FeatureTable.queryFeaturesAsync() pass string to
   * QueryParameters.setWhereClause() as the query string
   */
  private String getHumanProvincesQuery() throws IOException {
    LinkedList<String> l = new LinkedList<String>();
    for (String hp : getHumanProvincesList()) {
      l.add("name='" + hp + "'");
    }
    return "(" + String.join(" OR ", l) + ")";
  }

  private boolean confirmIfProvincesConnected(String province1, String province2) throws IOException {
    String content = Files.readString(Paths.get("src/unsw/gloriaromanus/province_adjacency_matrix_fully_connected.json"));
    JSONObject provinceAdjacencyMatrix = new JSONObject(content);
    return provinceAdjacencyMatrix.getJSONObject(province1).getBoolean(province2);
  }

  private void resetSelections(){
    invading_province.setText("");
    opponent_province.setText("");
  }

  private void printMessageToTerminal(String message){
    output_terminal.appendText(message+"\n");
  }

  /**
   * Stops and releases all resources used in application.
   */
  void terminate() {

    if (mapView != null) {
      mapView.dispose();
    }
  }

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  @FXML
  public void handleQuitButton(ActionEvent e) throws IOException {

  }

  @FXML
  public void handleSetProvinceButton(ActionEvent e) throws IOException {

    List<String> occupied = new ArrayList<String>();
    for (Province p : currFaction.getProvinces()) {
      occupied.add(p.getName());
    }

    if (currentlySelectedProvince == null) {
      printMessageToTerminal("Please select a province.");
      return;
    }

    String humanProvince = (String)currentlySelectedProvince.getAttributes().get("name");
    if (!occupied.contains(humanProvince)) {
      printMessageToTerminal("The province you selected is not your territory!");
      return;
    }

    currentlySelectedHumanProvince = currentlySelectedProvince;
    invading_province.setText((String)currentlySelectedProvince.getAttributes().get("name"));
  }

  @FXML
  public void handleDestinationButton(ActionEvent e) throws IOException {
    
    if (currentlySelectedProvince == null) {
      printMessageToTerminal("Please select a province.");
      return;
    }
    currentlySelectedDestination = currentlySelectedProvince;

    String province = (String)currentlySelectedDestination.getAttributes().get("name");
    Province p = gameSystem.checkStringinProvince(province);

    if (p.getFactionName().equals("null")) {
      invadeButton.setText("Occupy");

    } else if (p.getFactionName().equals(currFaction.getName())) {
      invadeButton.setText("Move");
    
    } else {
      invadeButton.setText("Invade");      
    }

    opponent_province.setText(province);
  }

  @FXML
  public void handleSetTaxButton(ActionEvent e) throws IOException {
    
    Stage newStage = new Stage();

    if (occupiedProvinces.getSelectionModel().getSelectedItem() == null) {
      printMessageToTerminal("You must select a province before pressing setTax.");
      return;
    }
    String p = occupiedProvinces.getSelectionModel().getSelectedItem();

    Province curr = gameSystem.checkStringinProvince(p);
    
    VBox box = new VBox();

    Label title = new Label();
    title.setText("Tax Rate Introduction\n");
    title.setTextAlignment(TextAlignment.CENTER);
    title.setFont(new Font(20));
    title.setStyle("-fx-text-fill: white");
    
    Label intro = new Label();
    intro.setText(" Low tax = +10 town-wealth growth per turn for the province, tax rate = 10%\n Normal tax = No effect on per turn town-wealth growth, tax rate = 15%\n High tax = -10 town-wealth growth per turn for the province (i.e. 10 gold loss to wealth per turn), tax rate = 20%\n Very high tax = -30 town-wealth growth per turn for the province, tax rate = 25%, -1 morale for all soldiers residing in the province");
    intro.setWrapText(true);
    intro.setTextAlignment(TextAlignment.LEFT);
    intro.setStyle("-fx-text-fill: white");

    Label status = new Label();
    status.setText("Currently, your province " + p +"'s" + " tax rate is " + String.valueOf(curr.getTaxRate()) + ". Current wealth of this province is " + String.valueOf(curr.getWealth()) + ". Please select the new tax rate below.");
    status.setWrapText(true);
    status.setTextAlignment(TextAlignment.LEFT);
    status.setStyle("-fx-text-fill: white");

    ChoiceBox<Double> cb = new ChoiceBox<>();
    cb.getItems().addAll(0.1, 0.15, 0.2, 0.25);

    Button ok = new Button();
    ok.setText("Change Tax Rate");
    ok.setOnAction(event -> {
      curr.setTaxRate(cb.getSelectionModel().getSelectedItem());
      printMessageToTerminal("Successfully set " + p + "'s" + " tax rate to " + String.valueOf(cb.getSelectionModel().getSelectedItem()) + ".");
      newStage.close();
    });

    box.getChildren().addAll(title, intro, status, cb, ok);
    box.setAlignment(Pos.CENTER);
    box.setSpacing(25.0);
    box.setPadding(new Insets(10,10,10,10));
    box.setStyle("-fx-background-color: transparent");

    Scene stageScene = new Scene(box, 600, 400);
    stageScene.setFill(Color.BLACK);
    newStage.initStyle(StageStyle.TRANSPARENT);
    newStage.setOpacity(0.90);
    newStage.setScene(stageScene);
    newStage.show();
  }

  public void setMainMenuScene(Scene mainMenuScene) {
    this.mainMenuScene = mainMenuScene;
  }

  @FXML
  public void handleEndTurnButton(ActionEvent e) throws IOException {
    
    if (ArmyActiveProvince.size() != 0) {
      printMessageToTerminal(String.format("You could not end your turn because you have active army in the province %s", ArmyActiveProvince.toString()));
    
    } else {

      turnPlayerCount += 1;

      if (turnPlayerCount == gameSystem.getFactions().size()) {
        turnPlayerCount = 0;
      }

      if (currentlySelectedProvince != null){
        featureLayer_provinces.unselectFeatures(Arrays.asList(currentlySelectedProvince));
        currentlySelectedProvince = null;
      }
    
      resetSelections();
      unitListButton.setDisable(true);
      occupiedProvinces.getSelectionModel().clearSelection();
      occupiedProvinces.getItems().clear();

      currFaction = gameSystem.getFactions().get(turnPlayerCount);
      humanFaction = currFaction.getName();
      factionLabel.setText("Faction: " + humanFaction);

      if (humanFaction.equals(FirstPlayerFaction)) {
        gameSystem.NextTurn();
        yearLabel.setText(String.valueOf(gameSystem.getYear()));
        turnLabel.setText(String.valueOf(turnTracker.getCurrTurn()));      
      }
      
      if (currFaction.isIs_defeat() == true) {
        printMessageToTerminal(humanFaction + " " + "has already lost the game. Switch to next player!");
        endTurnButton.fire();

      } else {

        for (Province p : currFaction.getProvinces()) {
          occupiedProvinces.getItems().add(p.getName());
          provinceToNumberTroopsMap.put(p.getName(), p.getNumOfSoldiers());
        }
        
        ArmyDisbandButton.setDisable(true);
        availableUnits.getItems().clear();
        provincesLabel.setText("Provinces Conquered: " + String.valueOf(currFaction.getProvinces().size()) +  " / " + "53");
    
        if (gameSystem.conditionToString().contains("WEALTH")) {
          wealthLabel.setText("Wealth: " + String.valueOf(currFaction.getTotalWealth()) + " / 400,000");
        } else {
          wealthLabel.setText("Wealth: " + String.valueOf(currFaction.getTotalWealth()));
        }
    
        if (gameSystem.conditionToString().contains("TREASURY")) {
          treasuryLabel.setText("Gold: " + String.valueOf(currFaction.getBalance()) + " / 100,000");
        } else {
          treasuryLabel.setText("Gold: " + String.valueOf(currFaction.getBalance()));
        }
        checkWinGame(currFaction);
        printMessageToTerminal(String.format("Turn %d: %s's turn", turnTracker.getCurrTurn(), currFaction.getName()));
      }



      addAllPointGraphics();

    }
  }

  public void clearScene() {
    // output_terminal.clear();
    
  }

  @FXML
  public void armyDisbandButton(ActionEvent e) throws IOException {
    
    String selectedProvince = occupiedProvinces.getSelectionModel().getSelectedItem();
    if (selectedProvince != null) {
      Province p = gameSystem.checkStringinProvince(selectedProvince);
      p.setArmy(new Army(p));
      armyLabel.setText("Army Status: Inactive");
      ArmyActiveProvince.remove(selectedProvince);
      ArmyDisbandButton.setDisable(true);

    }
  }

  @FXML
  public void armyButton(ActionEvent e) throws IOException {
    
    String Unit = availableUnits.getSelectionModel().getSelectedItem();
    String selectedProvince = occupiedProvinces.getSelectionModel().getSelectedItem();


    if (Unit != null && selectedProvince != null) {
      Province p = gameSystem.checkStringinProvince(selectedProvince);

      for (int i = 0; i < unitCount.size(); i++) {
        if (unitCount.get(i).get(0).getName().equals(Unit)) {
          int unitSize = unitCount.get(i).size();
          int armySize = p.getArmy().numberOfSpecificUnit(Unit);

          if (armySize < unitSize) {
            p.addToArmy(unitCount.get(i).get(armySize));
          } else {
            printMessageToTerminal("This type of Unit is insufficient to add in army.");
          }
        }
      }
      armyLabel.setText("Army Status: active");
      ArmyDisbandButton.setDisable(false);
      
      if (!ArmyActiveProvince.contains(selectedProvince)) {
        ArmyActiveProvince.add(selectedProvince);
      }

      availableUnits.getSelectionModel().clearSelection();
    }
  }

  public void loadFile(String fileName) {
    // gameSystem.reloadSavedGame(fileName);
    try {
      String data = Files.readString(Paths.get("SavedData/" + fileName));
      JSONObject json = new JSONObject(data);
      this.factionLabel.setText(json.getString("currFaction"));
      this.yearLabel.setText(json.getString("currYear"));
      this.turnLabel.setText(json.getString("currTurn"));
      this.victoryCondition.setText(json.getString("victoryCondition"));
      this.output_terminal.setText(json.getString("outputs"));
      this.FirstPlayerFaction = json.getString("firstPlayerFaction");
      this.turnPlayerCount = json.getInt("turnPlayerCount");
      this.gameSystem.loadJSON(json.getJSONObject("gameSystem"));

      this.humanFaction = this.factionLabel.getText().substring(this.factionLabel.getText().indexOf(' ')+1);
      this.playerNum = this.gameSystem.getPlayerNum();
      this.turnTracker = gameSystem.getTurnTracker();
      this.currFaction = this.gameSystem.getFactionByName(this.factionLabel.getText().substring(this.factionLabel.getText().indexOf(' ')+1));
    
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  public GameSystem getGameSystem() {
    return gameSystem;
  }

  public Button getPlayerNumButton() {
    return playerNumButton;
  }

  public void save() {
    int savedGamesNum = new File("SavedData/").list().length;
    savedGamesNum += 1;
    // String fileName = String.format("SavedData/GameBackUp.json", (savedGamesNum+1));
    String fileName = "SavedData/GameBackup-" + String.valueOf(savedGamesNum) + ".json";
    //String fileName = String.format("SavedData/GameBackUp-%d.json", 3);
    File backUpFile = new File(fileName);

    try {
      backUpFile.createNewFile();
    } catch (IOException e) {
      e.printStackTrace();
    }

    JSONObject data = new JSONObject();
    data.put("currFaction", factionLabel.getText());
    data.put("currYear", yearLabel.getText());
    data.put("currTurn", turnLabel.getText());
    data.put("victoryCondition", victoryCondition.getText());
    data.put("outputs", output_terminal.getText());
    data.put("firstPlayerFaction", FirstPlayerFaction);
    data.put("turnPlayerCount", turnPlayerCount);
    data.put("gameSystem", gameSystem.toJSON());

    try {
      PrintWriter myFile = new PrintWriter(backUpFile, "UTF-8");
      myFile.println(data);
      myFile.close();
    } catch (IOException e) {
        e.printStackTrace();
    }

  }

  @FXML
  public void UnitInArmyButton(ActionEvent e) throws IOException {
    
    if (occupiedProvinces.getSelectionModel().getSelectedItem() == null) {
      printMessageToTerminal("Please select a province.");      
      return;
    }

    if (!ArmyActiveProvince.contains(occupiedProvinces.getSelectionModel().getSelectedItem())) {
      printMessageToTerminal("The army of this province is empty");
      return;      
    }
    
    Stage newStage = new Stage();

    String p = occupiedProvinces.getSelectionModel().getSelectedItem();
    Province curr = gameSystem.checkStringinProvince(p);
    List<Unit> armyList = curr.getArmy().getUnits();
    Map<String, Integer> unitsCount = new HashMap<String, Integer>();

    VBox box = new VBox();

    Label title = new Label();
    title.setText(p + "'s Units In Current Army");
    title.setTextAlignment(TextAlignment.CENTER);
    title.setFont(new Font(18));
    title.setStyle("-fx-text-fill: white");
    
    ObservableList<String> count = FXCollections.observableArrayList();

    for (Unit u : armyList) {

      if (!unitsCount.keySet().contains(u.getName())) {
        unitsCount.put(u.getName(), 1);
      } else {
        unitsCount.replace(u.getName(), unitsCount.get(u.getName()) + 1);
      }

    }

    for (String unit : unitsCount.keySet()) {
      
      count.add(unit + "  X  " + String.valueOf(unitsCount.get(unit)));
    }

    ListView<String> listView = new ListView<String>(count);

    Button ok = new Button();
    ok.setText("GOT IT");
    ok.setOnAction(event -> {
      newStage.close();
    });

    box.getChildren().addAll(title, listView, ok);
    box.setAlignment(Pos.CENTER);
    box.setSpacing(20.0);
    box.setPadding(new Insets(10,10,10,10));
    box.setStyle("-fx-background-color: transparent");

    Scene stageScene = new Scene(box, 400, 400);
    stageScene.setFill(Color.BLACK);
    newStage.initStyle(StageStyle.TRANSPARENT);
    newStage.setOpacity(0.90);
    newStage.setScene(stageScene);
    newStage.show();
  }

  public void checkWinGame(Faction faction) {
    
    String winner = "";
    boolean winnerExist = false;
    for (Faction f: gameSystem.getFactions()) {
      if (f.isIs_win() == true) {
        winnerExist = true;
        winner = f.getName();
        if (f.getName().equals(faction.getName())) {
          printMessageToTerminal("You have already won this game!");               
        }
        break;
      }
    }

    if (winnerExist == false) {
      if (gameSystem.VictoryCheck(gameSystem.getVictoryCondition(), faction.getProvinces().size(), faction.getBalance(), faction.getTotalWealth()) == true) {
        printMessageToTerminal(faction.getName() + " has won this game!");
  
        for(Faction f: gameSystem.getFactions()) {
            if (f.getName().equals(faction.getName())) {
                f.setIs_win(true);
                break;
            }
        }
  
      this.save();
      printMessageToTerminal("Game has saved. Please continue playing!");      
      }

    } else {
      if (gameSystem.VictoryCheck(gameSystem.getVictoryCondition(), faction.getProvinces().size(), faction.getBalance(), faction.getTotalWealth()) == true && !faction.getName().equals(winner)) {
        printMessageToTerminal("Although you reach the victory condition, but someone was earlier than you!");     
      }

    }
    
  }
  public void checkLossGame(Faction faction) {

    if (faction.getProvinces().size() == 0) {
      printMessageToTerminal(faction.getName() + " has lost this game!");
        for(Faction f: gameSystem.getFactions()) {
            if (f.getName().equals(faction.getName())) {
                f.setIs_defeat(true);
                break;
            }
        }
    }
  }

  /*
  @FXML
  public void cheatingTreasury() {
    currFaction.setBalance(currFaction.getBalance() + 50000);

      provincesLabel.setText("Provinces Conquered: " + String.valueOf(currFaction.getProvinces().size()) +  " / " + "53");
    
    if (gameSystem.conditionToString().contains("WEALTH")) {
      wealthLabel.setText("Wealth: " + String.valueOf(currFaction.getTotalWealth()) + " / 400,000");
    } else {
      wealthLabel.setText("Wealth: " + String.valueOf(currFaction.getTotalWealth()));
    }

    if (gameSystem.conditionToString().contains("TREASURY")) {
      treasuryLabel.setText("Gold: " + String.valueOf(currFaction.getBalance()) + " / 100,000");
    } else {
      treasuryLabel.setText("Gold: " + String.valueOf(currFaction.getBalance()));
    }
    checkWinGame(currFaction);
  }

  @FXML
  public void cheatingWealth() {
    currFaction.setTotalWealth(currFaction.getTotalWealth() + 200000);
      provincesLabel.setText("Provinces Conquered: " + String.valueOf(currFaction.getProvinces().size()) +  " / " + "53");
    
    if (gameSystem.conditionToString().contains("WEALTH")) {
      wealthLabel.setText("Wealth: " + String.valueOf(currFaction.getTotalWealth()) + " / 400,000");
    } else {
      wealthLabel.setText("Wealth: " + String.valueOf(currFaction.getTotalWealth()));
    }

    if (gameSystem.conditionToString().contains("TREASURY")) {
      treasuryLabel.setText("Gold: " + String.valueOf(currFaction.getBalance()) + " / 100,000");
    } else {
      treasuryLabel.setText("Gold: " + String.valueOf(currFaction.getBalance()));
    }
    checkWinGame(currFaction);
  }
  */
}