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

    private List<Faction> factions;
    private List<Province> provinces;
    private int turn;
    private int year;
    private int playerNum;
    private VictoryCondition victoryCondition;
    private TurnTracker turnTracker;
    private ProvincesTracker provincesTracker;
    private FactionsTracker factionsTracker;

    // Only can be used in allocationFaction method
    List<String> Factions_list = new ArrayList<String>(); 
    List<String> Provinces_list = new ArrayList<String>();
    JSONObject relations = null; 
    
    // Create a new Game
    // 1. set player number
    // 2. victoryCondition
    // 3. randomly allocate faction
    // 4. create Factions and Province
    public GameSystem() {
        this.turn = 0;
        this.year = 200;
        this.playerNum = 0;
        this.victoryCondition = null;
        this.turnTracker = new TurnTracker();
        this.provincesTracker = new ProvincesTracker();
        this.factionsTracker = new FactionsTracker();
        this.factions = factionsTracker.getFactions();
        this.provinces = provincesTracker.getProvinces();

        try{
            String allfactions = Files.readString(Paths.get("bin/unsw/gloriaromanus/backend/factions_list.json"));
            JSONObject facList = new JSONObject(allfactions);
            String allprovinces = Files.readString(Paths.get("bin/unsw/gloriaromanus/backend/provinces_list.json"));
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
                Province newProvince = new Province(String_startProvinces.get(j), null, turnTracker);
                startProvinces.add(newProvince);
                this.provinces.add(newProvince);
                Provinces_list.remove(String_startProvinces.get(j));
            }

            Faction newFaction = new Faction(FactionName, startProvinces, provincesTracker, factionsTracker);
            
            for(Province province: startProvinces) {
                province.setFaction(newFaction);
            }
            
            this.factions.add(newFaction);
            Factions_list.remove(FactionName);

        }

        for(int i = 0; i < Provinces_list.size(); i++) {
            Province new_Province = new Province(Provinces_list.get(i), null, turnTracker);
            this.provinces.add(new_Province);
        }
    }


    public void NextTurn() {

        this.turn += 1;
        this.year += 1;

        turnTracker.incrementTurn();

        for(Faction faction: this.factions){
            faction.update();
        }

    }

    
    /** 
     * @param v
     */
    public void setVictoryCondtion(VictoryCondition v){
        this.victoryCondition = v;
    }

    
    /** 
     * @return JSONObject
     */
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

    public JSONObject toJSON() {
        // JSONObject out = new JSONObject();
        JSONObject output = new JSONObject();
        List<JSONObject> factionsJSON = new ArrayList<JSONObject>();
        for (Faction f : factions) {
            factionsJSON.add(f.toJSON());
        }
        output.put("factions", factionsJSON);

        List<JSONObject> provincesJSON = new ArrayList<JSONObject>();
        for (Province p : provinces) {
            provincesJSON.add(p.toJSON());
        }
        output.put("provinces", provincesJSON);
        output.put("turn", turn);
        output.put("year", year);
        output.put("playerNum", playerNum);
        output.put("victoryCondition", (victoryCondition == null) ? JSONObject.NULL : victoryCondition.getVictoryGoal());
        output.put("turnTracker", turnTracker.toJSON());
        output.put("provincesTracker", provincesTracker.toJSON());
        output.put("factionsTracker", factionsTracker.toJSON());

        // out.put("GameSystem", output);

        return output;
    }

    public void loadJSON(JSONObject json) {
        this.turn = json.getInt("turn");
        this.year = json.getInt("year");
        this.playerNum = json.getInt("playerNum");
        // this.victoryCondition
        this.turnTracker = new TurnTracker(json.getJSONObject("turnTracker"));
        this.provincesTracker = new ProvincesTracker(json.getJSONObject("provincesTracker"));
        this.factionsTracker = new FactionsTracker(json.getJSONObject("factionsTracker"));
        this.provinces = provincesTracker.getProvinces();
        this.factions = factionsTracker.getFactions(); 
    }



    public boolean VictoryCheck(VictoryCondition v, int OccupiedNum, int treasury, int wealth) {

        JSONObject VicCon = v.getVictoryGoal();
        
        if (VicCon == null) {
            return false;
        } 

        if (VicCon.length() == 1) {

            String ConditionString = VicCon.getString("goal");
            return ConditionCheck(ConditionString, OccupiedNum, treasury, wealth);
        
        } else if (VicCon.length() == 2) {
            
            String relation = VicCon.getString("goal");
            
            boolean is_found = false;
            String relation2 = null;
            String subgoal1 = null;
            String subgoal2 = null;

            for (int i = 0; i < 2; i++) {
                JSONObject Condition = VicCon.getJSONArray("subgoals").getJSONObject(i);
                if (Condition.getString("goal").equals("AND") || Condition.getString("goal").equals("OR")) {
                    is_found = true;
                    relation2 = Condition.getString("goal");
                    subgoal1 = Condition.getJSONArray("subgoals").getJSONObject(0).getString("goal");
                    subgoal2 = Condition.getJSONArray("subgoals").getJSONObject(1).getString("goal");
                    break;
                } 
                
            }
            
            if (!is_found) {
                
                String goal1 = VicCon.getJSONArray("subgoals").getJSONObject(0).getString("goal");
                String goal2 = VicCon.getJSONArray("subgoals").getJSONObject(1).getString("goal");

                if (relation.equals("AND")) {
                    return ConditionCheck(goal1, OccupiedNum, treasury, wealth) &&
                    ConditionCheck(goal2, OccupiedNum, treasury, wealth);

                } else if (relation.equals("OR")) {
                    return ConditionCheck(goal1, OccupiedNum, treasury, wealth) ||
                    ConditionCheck(goal2, OccupiedNum, treasury, wealth);
                }

            } else if (is_found) {
                
                boolean subGoalCheck = false;
                
                if (relation2.equals("AND")) {
                    subGoalCheck = ConditionCheck(subgoal1, OccupiedNum, treasury, wealth) &&
                    ConditionCheck(subgoal2, OccupiedNum, treasury, wealth);

                } else if (relation2.equals("OR")) {
                    subGoalCheck = ConditionCheck(subgoal1, OccupiedNum, treasury, wealth) ||
                    ConditionCheck(subgoal2, OccupiedNum, treasury, wealth);
                }
                
                String goal = null;
                
                for (int i = 0; i < 2; i++) {
                    JSONObject Condition = VicCon.getJSONArray("subgoals").getJSONObject(i);
                    
                    if (!Condition.getString("goal").equals("AND") && !Condition.getString("goal").equals("OR")) {
                        goal = Condition.getString("goal");
                    } 
                }
                
                if (relation.equals("AND")) {
                    return ConditionCheck(goal, OccupiedNum, treasury, wealth) && subGoalCheck;
                } else if (relation.equals("OR")) {
                    return ConditionCheck(goal, OccupiedNum, treasury, wealth) || subGoalCheck;
                }
                
                
            }
        }

        return false;

    }   

    public boolean ConditionCheck(String VicConName, int OccupiedNum, int treasury, int wealth) {
        
        switch(VicConName) {
            
            case "CONQUEST":
                return OccupiedNum == 52;

            case "TREASURY":
                return treasury >= 100000;

            case "WEALTH":
                return wealth >= 400000;

            default:
                break;
        }

        return false;

    }


    

    
}
