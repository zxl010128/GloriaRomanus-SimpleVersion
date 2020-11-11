package unsw.gloriaromanus;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleIntegerProperty;
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
import unsw.gloriaromanus.backend.TurnTracker;
import unsw.gloriaromanus.backend.Faction;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.FeatureTable;
import com.esri.arcgisruntime.data.GeoPackage;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
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

public class GloriaRomanusController{

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
  private Label victoryConditonLabel;
  @FXML
  private Label victoryConditon;
  @FXML
  private Label balanceLabel;
  @FXML
  private Label yearLabel;
  @FXML
  private Label turnLabel;
  @FXML
  private VBox menu;
  @FXML
  private Button endTurnButton;
  @FXML
  private Button saveButton;
  @FXML
  private Button invadeButton;

  private String FirstPlayerFaction;

  private Integer turnPlayerCount;

  private ArcGISMap map;

  private Map<String, String> provinceToOwningFactionMap;

  private Map<String, Integer> provinceToNumberTroopsMap;

  private String humanFaction;

  private Feature currentlySelectedHumanProvince;
  private Feature currentlySelectedEnemyProvince;

  private FeatureLayer featureLayer_provinces;

  private Stage stage;
  private Scene mainMenuScene;

  private GameSystem gameSystem;
  // private VictoryCondition victoryCondition;
  private TurnTracker turnTracker;
  public Faction currFaction;

  
  @FXML
  private void initialize() throws JsonParseException, JsonMappingException, IOException {
    // All factions will start with no soldiers
    provinceToOwningFactionMap = getProvinceToOwningFactionMap();

    provinceToNumberTroopsMap = new HashMap<String, Integer>();

    for (String provinceName : provinceToOwningFactionMap.keySet()) {
      provinceToNumberTroopsMap.put(provinceName, 0);
    }

    // TODO = load this from a configuration file you create (user should be able to
    // select in loading screen)

    currentlySelectedHumanProvince = null;
    currentlySelectedEnemyProvince = null;

    // Enable all the button except PlayerNumButton
    endTurnButton.setDisable(true);
    saveButton.setDisable(true);
    invadeButton.setDisable(true);
    quitButton.setDisable(true);

    playerNumButton.setOnAction(e -> {
      showStage(); 
      playerNumButton.setDisable(true);
      endTurnButton.setDisable(false);
      saveButton.setDisable(false);
      invadeButton.setDisable(false);
      quitButton.setDisable(false);
    });
    
  }

  // A pop up window to be able for player to choose player number
  public void showStage(){
    
    Stage newStage = new Stage();

    VBox box = new VBox();

    Label title = new Label();
    title.setText("Please Select the player number!\n");
    title.setTextAlignment(TextAlignment.CENTER);
    title.setFont(new Font(20));
    
    ChoiceBox<Integer> cb = new ChoiceBox<>();
    cb.getItems().addAll(2,3,4,5,6,7,8,9,10,11,12,13,14,15,16);

    Button ok = new Button();
    ok.setText("Start Playing!");
    ok.setOnAction(e -> {
      try {
        initialMap(cb.getSelectionModel().getSelectedItem());
        newStage.close();
    } catch (Exception exception) {
      throw new RuntimeException(exception);

    }});

    box.getChildren().addAll(title, cb, ok);
    box.setAlignment(Pos.CENTER);
    box.setSpacing(10.0);
    box.setOpacity(0.95);

    Scene stageScene = new Scene(box, 500, 300);
    stageScene.setFill(Color.TRANSPARENT);
    newStage.initStyle(StageStyle.TRANSPARENT);
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

    quitButton.setOnAction(e -> stage.setScene(mainMenuScene));

    // add a Listview to display recruitable soldiers
    ListView<String> listView = new ListView<>();
    for (int i = 0; i < 5; i++) {
      listView.getItems().add("Unit " + i);
    }
    menu.getChildren().add(listView);

    factionLabel.setText("Faction: " + currFaction.getName());
    yearLabel.setText(String.valueOf(gameSystem.getYear()));
    // yearLabel.textProperty().bind(new SimpleIntegerProperty(gameSystem.getYear()).asString());
    turnLabel.setText(String.valueOf(turnTracker.getCurrTurn()));
    victoryConditonLabel.setText("Victory Condition:");
    victoryConditon.setText(gameSystem.conditionToString());
  }

  @FXML
  public void clickedInvadeButton(ActionEvent e) throws IOException {
    if (currentlySelectedHumanProvince != null && currentlySelectedEnemyProvince != null){
      String humanProvince = (String)currentlySelectedHumanProvince.getAttributes().get("name");
      String enemyProvince = (String)currentlySelectedEnemyProvince.getAttributes().get("name");
      if (confirmIfProvincesConnected(humanProvince, enemyProvince)){
        // TODO = have better battle resolution than 50% chance of winning
        Random r = new Random();
        int choice = r.nextInt(2);
        if (choice == 0){
          // human won. Transfer 40% of troops of human over. No casualties by human, but enemy loses all troops
          int numTroopsToTransfer = provinceToNumberTroopsMap.get(humanProvince)*2/5;
          provinceToNumberTroopsMap.put(enemyProvince, numTroopsToTransfer);
          provinceToNumberTroopsMap.put(humanProvince, provinceToNumberTroopsMap.get(humanProvince)-numTroopsToTransfer);
          provinceToOwningFactionMap.put(enemyProvince, humanFaction);
          printMessageToTerminal("Won battle!");
        }
        else{
          // enemy won. Human loses 60% of soldiers in the province
          int numTroopsLost = provinceToNumberTroopsMap.get(humanProvince)*3/5;
          provinceToNumberTroopsMap.put(humanProvince, provinceToNumberTroopsMap.get(humanProvince)-numTroopsLost);
          printMessageToTerminal("Lost battle!");
        }
        resetSelections();  // reset selections in UI
        addAllPointGraphics(); // reset graphics
      }
      else{
        printMessageToTerminal("Provinces not adjacent, cannot invade!");
      }

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


          // TODO = handle all faction names, and find a better structure...
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
                String province = (String)f.getAttributes().get("name");

                if (provinceToOwningFactionMap.get(province).equals(humanFaction)){
                  // province owned by human
                  if (currentlySelectedHumanProvince != null){
                    featureLayer.unselectFeature(currentlySelectedHumanProvince);
                  }
                  currentlySelectedHumanProvince = f;
                  invading_province.setText(province);
                }
                else{
                  if (currentlySelectedEnemyProvince != null){
                    featureLayer.unselectFeature(currentlySelectedEnemyProvince);
                  }
                  currentlySelectedEnemyProvince = f;
                  opponent_province.setText(province);
                }

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
    featureLayer_provinces.unselectFeatures(Arrays.asList(currentlySelectedEnemyProvince, currentlySelectedHumanProvince));
    currentlySelectedEnemyProvince = null;
    currentlySelectedHumanProvince = null;
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
  
  public void setMainMenuScene(Scene mainMenuScene) {
    this.mainMenuScene = mainMenuScene;
  }

  @FXML
  public void handleEndTurnButton(ActionEvent e) throws IOException {
    
    turnPlayerCount += 1;

    if (turnPlayerCount == gameSystem.getFactions().size()) {
      turnPlayerCount = 0;
    }

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
    }
  }
}