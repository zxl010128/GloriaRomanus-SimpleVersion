package unsw.gloriaromanus.backend;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

import org.json.*;

import java.io.File;

public class GameSystem {

    private ArrayList<Faction> factions;
    private ArrayList<Province> provinces;
    private int turn;
    private int year;
    private int playerNum;
    private VictoryCondition victoryCondition;

    List<String> Factions_list = new ArrayList<String>(); 
    List<String> Provinces_list = new ArrayList<String>();
    JSONObject relations = null; 
    // Create a new Game
    // 1. set player number
    // 2. victoryCondition
    // 3. randomly allocate faction
    // 4. create Factions and Province
    
    public GameSystem() {
        this.factions = new ArrayList<Faction>();
        this.provinces = new ArrayList<Province>();
        this.turn = 0;
        this.year = 200;
        this.playerNum = 0;
        this.victoryCondition = null;

        try{
            String allfactions = Files.readString(Paths.get("src/unsw/gloriaromanus/backend/factions_list.json"));
            JSONObject facList = new JSONObject(allfactions);
            String allprovinces = Files.readString(Paths.get("src/unsw/gloriaromanus/backend/provinces_list.json"));
            JSONArray proList = new JSONArray(allprovinces);      
            
            relations = facList;

            for(int i = 0; i < proList.length(); i++) {
                Provinces_list.add(proList.getString(i));
            }
            List<String> facs = new ArrayList<>(facList.keySet());
            Provinces_list = facs;

        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    public void allocateFaction(){        
        
        for(int i = 1; i <= this.playerNum; i++) {
            Collections.shuffle(Factions_list);
            String FactionName = Factions_list.get(0);
            JSONArray provincesList = relations.getJSONArray(FactionName);

            ArrayList<Province> startProvinces = new ArrayList<Province>();
            ArrayList<String> String_startProvinces = new ArrayList<String>();

            for (int j = 0; j < provincesList.length(); j++) {
                String_startProvinces.add(provincesList.optString(j));
                Provinces_list.remove(provincesList.opt(j));
            }

            for (int j = 0; j < String_startProvinces.size(); j++) {
                Province newProvince = new Province(String_startProvinces.get(j));
                startProvinces.add(newProvince);
                this.provinces.add(newProvince);
                Provinces_list.remove(String_startProvinces.get(j));
            }

            Faction newFaction = new Faction(FactionName, startProvinces);
            this.factions.add(newFaction);
            Factions_list.remove(FactionName);

        }

        for(int i = 0; i < Provinces_list.size(); i++) {
            Province new_Province = new Province(Provinces_list.get(i));
            this.provinces.add(new_Province);
        }
    }


    public void setVictoryCondtion(VictoryCondition v){
        this.victoryCondition = v;
    }

    public JSONObject getVictoryCondition(){
        return this.victoryCondition.getVictoryGoal();
    }

    /** 
     * @param playerNum the number of player in this game
     * @return boolean
     */
    public boolean setPlayerNum(int playerNum) {
        
        // player number should be more than 2 and less than the total factions number
        if (playerNum > factions.size() || playerNum < 2) {
            return false;
        } else {
            this.playerNum = playerNum;
        }    
        
        return true;
    }

    
    /** 
     * check the saved file existence
     * @return boolean
     */
    public boolean isSavedFileExist(){
        String FileName = "DataSaved";
        File logFile = new File(FileName);

        if(!logFile.exists()) {
            return false;
        }
        return true;
    }

    
    /** 
     * @param game
     */
    public void saveCurrentGame(GameSystem game){
        // saved game
        
    }



    

    
}
