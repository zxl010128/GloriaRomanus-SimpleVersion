package unsw.gloriaromanus.backend;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

import java.util.Objects;
import java.io.PrintWriter;

import org.json.*;

import java.io.File;

public class GameSystem implements Observer {

    private List<Faction> factions;
    private List<Province> provinces;
    private int turn;
    private int year;
    private int playerNum;
    private JSONObject victoryCondition;
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

        try {
            String allfactions = Files.readString(Paths.get("src/unsw/gloriaromanus/backend/factions_list.json"));
            JSONObject facList = new JSONObject(allfactions);
            String allprovinces = Files.readString(Paths.get("src/unsw/gloriaromanus/backend/provinces_list.json"));
            JSONArray proList = new JSONArray(allprovinces);

            relations = facList;

            for (int i = 0; i < proList.length(); i++) {
                Provinces_list.add(proList.getString(i));
            }
            List<String> facs = new ArrayList<String>(facList.keySet());
            Factions_list = facs;

        } catch(Exception e) {
            e.printStackTrace();
        }
        randomChooseCondition();

    }

    /** 
     * function to allocate Faction according to the num of player
     * All the Faction and Province will be newed 
     */
    public void allocateFaction() {

        for (int i = 1; i <= this.playerNum; i++) {
            Collections.shuffle(Factions_list);
            String FactionName = Factions_list.get(0);
            JSONArray provincesList = relations.getJSONArray(FactionName);

            ArrayList<Province> startProvinces = new ArrayList<Province>();
            ArrayList<String> String_startProvinces = new ArrayList<String>();

            for (int j = 0; j < provincesList.length(); j++) {
                String_startProvinces.add(provincesList.getString(j));
                Provinces_list.remove(provincesList.get(j));
            }

            for (int j = 0; j < String_startProvinces.size(); j++) {
                Province newProvince = new Province(String_startProvinces.get(j), null, turnTracker);
                startProvinces.add(newProvince);
                this.provinces.add(newProvince);
            }

            new Faction(FactionName, startProvinces, provincesTracker, factionsTracker);

            // for (int j = 0; j < startProvinces.size(); j++) {
            // startProvinces.get(j).setFactionsTracker(factionsTracker);
            // startProvinces.get(j).setFaction(newFaction);
            // }
            // for(Province province: startProvinces) {
            // province.setFaction(newFaction);
            // }

            Factions_list.remove(FactionName);

        }

        for (int i = 0; i < Provinces_list.size(); i++) {
            Province new_Province = new Province(Provinces_list.get(i), null, turnTracker);
            this.provinces.add(new_Province);
        }

        for (Faction faction: this.factions) {
            faction.setGamesys(this);
        }

    }

    /** 
     * method to randomly provide a Victory Condition
     */
    public void randomChooseCondition() {

        List<String> difficulty = new ArrayList<String>();
        difficulty.add("Hard");
        difficulty.add("Medium");
        difficulty.add("Easy");

        List<String> relations = new ArrayList<String>();
        relations.add("AND");
        relations.add("OR");

        List<String> conditions = new ArrayList<String>();
        conditions.add("CONQUEST");
        conditions.add("TREASURY");
        conditions.add("WEALTH");

        Collections.shuffle(difficulty);
        String diff = difficulty.get(0);

        if (diff.equals("Easy")) {
            
            Collections.shuffle(conditions);
            String vicCon = conditions.get(0);

            ConditionLeaf goal = new ConditionLeaf(vicCon);
            setVictoryCondtion(goal);

        } else if (diff.equals("Medium")) {
            
            Collections.shuffle(conditions);
            String vicCon1 = conditions.get(0);
            String vicCon2 = conditions.get(1);

            Collections.shuffle(relations);
            String relation = relations.get(0);

            ConditionLeaf goal1 = new ConditionLeaf(vicCon1);
            ConditionLeaf goal2 = new ConditionLeaf(vicCon2);

            ConditionComponent subgoal = new ConditionComponent(relation);
            subgoal.add(goal1);
            subgoal.add(goal2);
            setVictoryCondtion(subgoal);

        } else if (diff.equals("Hard")) {
            
            Collections.shuffle(conditions);
            String vicCon1 = conditions.get(0);
            String vicCon2 = conditions.get(1);
            String vicCon3 = conditions.get(2);

            Collections.shuffle(relations);
            String relation1 = relations.get(0);
            
            Collections.shuffle(relations);
            String relation2 = relations.get(0);

            ConditionLeaf goal1 = new ConditionLeaf(vicCon1);
            ConditionLeaf goal2 = new ConditionLeaf(vicCon2);
            ConditionLeaf goal3 = new ConditionLeaf(vicCon3);

            ConditionComponent subgoal = new ConditionComponent(relation1);
            subgoal.add(goal1);
            subgoal.add(goal2);

            ConditionComponent subgoal1 = new ConditionComponent(relation2);
            subgoal1.add(subgoal);
            subgoal1.add(goal3);

            setVictoryCondtion(subgoal1);
        }




    }


    public void NextTurn() {

        this.turn += 1;
        this.year += 1;

        turnTracker.incrementTurn();

        for (Faction faction : this.factions) {
            faction.update();
        }

    }

    
    /** 
     * @return FactionsTracker
     */
    public FactionsTracker getFactionsTracker() {
        return factionsTracker;
    }

    
    /** 
     * @return ProvincesTracker
     */
    public ProvincesTracker getProvincesTracker() {
        return provincesTracker;
    }

    
    /** 
     * @return TurnTracker
     */
    public TurnTracker getTurnTracker() {
        return turnTracker;
    }

    /** 
    /**
     * @param v
     */
    public void setVictoryCondtion(VictoryCondition v) {
        this.victoryCondition = v.getVictoryGoal();
    }

    /**
     * @return JSONObject
     */
    public JSONObject getVictoryCondition() {
        return this.victoryCondition;
    }

    /**
     * @param playerNum the number of player in this game
     * @return boolean
     */
    public boolean setPlayerNum(int playerNum) {

        // player number should be more than 2 and less than the total factions number
        // frontend should print a message to re enter the playernum
        if (playerNum > Factions_list.size() || playerNum < 2) {
            return false;
        } else {
            this.playerNum = playerNum;
        }

        return true;
    }

    public void reloadSavedGame() {

        try {
            String Backup = Files.readString(Paths.get("GameBackUp.json"));
            JSONObject dataSaved = new JSONObject(Backup);

            this.loadJSON(dataSaved);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param game
     */
    public void saveCurrentGame() {
        // saved game
        String fileName = "GameBackUp.json";
        File BackUpFile = new File(fileName);

        try {
            BackUpFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            PrintWriter myFile = new PrintWriter(BackUpFile, "UTF-8");
            JSONObject savedData = this.toJSON();
            myFile.println(savedData);
            myFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * @return JSONObject
     */
    public JSONObject toJSON() {

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
        output.put("turn", this.turn);
        output.put("year", this.year);
        output.put("playerNum", this.playerNum);
        output.put("victoryCondition", this.victoryCondition);
        output.put("turnTracker", turnTracker.toJSON());
        output.put("provincesTracker", provincesTracker.toJSON());
        output.put("factionsTracker", factionsTracker.toJSON());

        return output;
    }

    /**
     * @param json
     */
    public void loadJSON(JSONObject json) {
        this.turn = json.getInt("turn");
        this.year = json.getInt("year");
        this.playerNum = json.getInt("playerNum");
        this.victoryCondition = json.getJSONObject("victoryCondition");
        this.turnTracker = new TurnTracker(json.getJSONObject("turnTracker"));
        this.provincesTracker = new ProvincesTracker(json.getJSONObject("provincesTracker"));
        this.factionsTracker = new FactionsTracker(json.getJSONObject("factionsTracker"));
        this.provinces = provincesTracker.getProvinces();
        for (Province p : provinces) {
            p.setProvincesTracker(provincesTracker);
            p.setFactionsTracker(factionsTracker);
        }
        this.factions = factionsTracker.getFactions();
        for (Faction f : factions) {
            f.setProvincesTracker(provincesTracker);
            f.setFactionsTracker(factionsTracker);
            f.setGamesys(this);
        }
    }

    /**
     * check whether a player wins
     * 
     * @param v
     * @param OccupiedNum
     * @param treasury
     * @param wealth
     * @return boolean
     */
    public boolean VictoryCheck(JSONObject VicCon, int OccupiedNum, int treasury, int wealth) {

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
                    return ConditionCheck(goal1, OccupiedNum, treasury, wealth)
                            && ConditionCheck(goal2, OccupiedNum, treasury, wealth);

                } else if (relation.equals("OR")) {
                    return ConditionCheck(goal1, OccupiedNum, treasury, wealth)
                            || ConditionCheck(goal2, OccupiedNum, treasury, wealth);
                }

            } else if (is_found) {

                boolean subGoalCheck = false;

                if (relation2.equals("AND")) {
                    subGoalCheck = ConditionCheck(subgoal1, OccupiedNum, treasury, wealth)
                            && ConditionCheck(subgoal2, OccupiedNum, treasury, wealth);

                } else if (relation2.equals("OR")) {
                    subGoalCheck = ConditionCheck(subgoal1, OccupiedNum, treasury, wealth)
                            || ConditionCheck(subgoal2, OccupiedNum, treasury, wealth);
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

    /**
     * @param VicConName
     * @param OccupiedNum
     * @param treasury
     * @param wealth
     * @return boolean
     */
    public boolean ConditionCheck(String VicConName, int OccupiedNum, int treasury, int wealth) {

        switch (VicConName) {

            case "CONQUEST":
                return OccupiedNum == 53;

            case "TREASURY":
                return treasury >= 100000;

            case "WEALTH":
                return wealth >= 400000;

            default:
                break;
        }

        return false;

    }

    /**
     * @return List<Faction>
     */
    public List<Faction> getFactions() {
        return factionsTracker.getFactions();
    }

    /**
     * @return List<Province>
     */
    public List<Province> getProvinces() {
        return provinces;
    }

    public int getYear() {
        return year;
    }

    /**
     * @return int
     */
    public int getPlayerNum() {
        return playerNum;
    }

    public String conditionToString() {

        if (this.victoryCondition == null) {
            return "Condition Not Exist";
        }

        if (this.victoryCondition.length() == 1) {

            String ConditionString = this.victoryCondition.getString("goal");
            return ConditionString;

        } else if (this.victoryCondition.length() == 2) {

            String relation = this.victoryCondition.getString("goal");

            boolean is_found = false;
            String relation2 = null;
            String subgoal1 = null;
            String subgoal2 = null;

            for (int i = 0; i < 2; i++) {
                JSONObject Condition = this.victoryCondition.getJSONArray("subgoals").getJSONObject(i);
                if (Condition.getString("goal").equals("AND") || Condition.getString("goal").equals("OR")) {
                    is_found = true;
                    relation2 = Condition.getString("goal");
                    subgoal1 = Condition.getJSONArray("subgoals").getJSONObject(0).getString("goal");
                    subgoal2 = Condition.getJSONArray("subgoals").getJSONObject(1).getString("goal");
                    break;
                }

            }

            if (!is_found) {

                String goal1 = this.victoryCondition.getJSONArray("subgoals").getJSONObject(0).getString("goal");
                String goal2 = this.victoryCondition.getJSONArray("subgoals").getJSONObject(1).getString("goal");

                return goal1 + " " + relation + " " + goal2;

            } else if (is_found) {

                String subGoal = "(" + subgoal1 + " " + relation2 + " " + subgoal2 + ")";

                String goal = null;

                for (int i = 0; i < 2; i++) {
                    JSONObject Condition = this.victoryCondition.getJSONArray("subgoals").getJSONObject(i);

                    if (!Condition.getString("goal").equals("AND") && !Condition.getString("goal").equals("OR")) {
                        goal = Condition.getString("goal");
                    }
                }

                return goal + " " + relation + " " + subGoal;

            }
        }

        return "No valid Goal";

    }

    public boolean checkStringinFaction(String faction) {

        for (Faction f : this.factions) {
            if (f.getName().equals(faction)) {
                return true;
            }
        }

        return false;
    }

    public Province checkStringinProvince(String pronvince) {

        for (Province p : this.provinces) {
            if (p.getName().equals(pronvince)) {
                return p;
            }
        }

        return null;
    }

    /** 
     * @param obj
     * @return boolean
     */
    @Override
    public boolean equals(Object obj) {

        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;

        GameSystem l = (GameSystem) obj;

        return Objects.equals(l.turn, turn) && Objects.equals(l.year, year) && Objects.equals(l.playerNum, playerNum)
        && Objects.equals(l.victoryCondition.toString(), victoryCondition.toString());
    }

    
    /** 
     * @return String
     */
    @Override
    public String toString() {
        return "GameSystem [factions="
                + factions + ", factionsTracker=" + factionsTracker + ", playerNum=" + playerNum + ", provinces="
                + provinces + ", provincesTracker=" + provincesTracker + ", turn=" + turn
                + ", turnTracker=" + turnTracker + ", victoryCondition=" + victoryCondition + ", year=" + year + "]";
    }


    
    /** 
     * @param obj
     * @param FactionName
     * @param OccupiedNum
     * @param treasury
     * @param wealth
     */
    @Override
	public void update(Subject obj) {
		Display(obj);
	}

	
    /** 
     * to check the notifier whether it is win or not
     * if win savegame and check
     * @param FactionName
     * @param OccupiedNum
     * @param treasury
     * @param wealth
     */
    public void Display(Subject obj) {

        // If this Faction win this game
        if (VictoryCheck(this.victoryCondition, ((Faction) obj).getProvinces().size(), ((Faction) obj).getBalance(), ((Faction) obj).getTotalWealth()) == true) {
            System.out.println(((Faction) obj).getName() + "has won this game!");

            for(Faction faction: this.factions) {
                if (faction.getName().equals(((Faction) obj).getName())) {
                    faction.setIs_win(true);
                    break;
                }
            }

            this.saveCurrentGame();
        
        // If this Faction totallt lost
        } else if (((Faction) obj).getProvinces().size() == 0) {
            System.out.println(((Faction) obj).getName() + "has lost this game!");
            for(Faction faction: this.factions) {
                if (faction.getName().equals(((Faction) obj).getName())) {
                    faction.setIs_defeat(true);
                    break;
                }
            }
        }

    }
    
    public Faction getFactionByProvinceName(String name) {
        for (Province p : provinces) {
            if (p.getName().equals(name)) {
                return factionsTracker.getFaction(p.getFactionName());
            }
        }

        return null;
    }

    
}
