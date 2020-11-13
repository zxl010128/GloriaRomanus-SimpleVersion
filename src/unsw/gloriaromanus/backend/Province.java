package unsw.gloriaromanus.backend;

import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONObject;

public class Province{
    private String name;
    private int wealth;
    private double taxRate;
    private String factionName;
    private List<Unit> units;
    private List<TrainingRecord> unitsInTraining;
    private TurnTracker turnTracker;
    private ProvincesTracker provincesTracker;
    private FactionsTracker factionsTracker;
    private Army army;

    public Province(String name, Faction faction, TurnTracker turnTracker){
        this.name = name;
        this.wealth = 100;

        // assume the default tax is normal tax (0.15)
        this.taxRate = 0.15;

        this.factionName = (faction == null) ? "null" : faction.getName();
        this.units = new ArrayList<Unit>();
        this.unitsInTraining = new ArrayList<TrainingRecord>();
        // this.observers = new ArrayList<>();
        this.turnTracker = turnTracker;
        if (faction != null) {
            this.provincesTracker = faction.getProvincesTracker();
            provincesTracker.addProvince(this);

            this.factionsTracker = faction.getFactionsTracker();
            this.factionsTracker.update(faction, this);    
        } else {
            this.provincesTracker = null;
            this.factionsTracker = null;
        }
        this.army = new Army(this);

    }

    public Province(JSONObject json) {
        this.name = json.getString("name");
        this.wealth = json.getInt("wealth");
        this.taxRate = json.getDouble("taxRate");
        this.factionName = json.getString("factionName");
        this.units = new ArrayList<Unit>();
        JSONArray unitsJSON = json.getJSONArray("units");
        for (int i = 0; i < unitsJSON.length(); i++) {
            units.add(new Unit(unitsJSON.getJSONObject(i)));
        }
        
        this.unitsInTraining = new ArrayList<TrainingRecord>();
        JSONArray unitsInTrainingJSON = json.getJSONArray("unitsInTraining");
        for (int i = 0; i < unitsInTrainingJSON.length(); i++) {
            unitsInTraining.add(new TrainingRecord(unitsInTrainingJSON.getJSONObject(i)));
        }
        
        this.turnTracker = new TurnTracker(json.getJSONObject("turnTracker"));
        this.factionsTracker = null;
        this.provincesTracker = null;
        this.army = new Army(this);
    }

    public void setProvincesTracker(ProvincesTracker provincesTracker) {
        this.provincesTracker = provincesTracker;
    }

    public void setFactionsTracker(FactionsTracker factionsTracker) {
        this.factionsTracker = factionsTracker;
    }

    public ProvincesTracker getProvincesTracker() {
        return provincesTracker;
    }

    public FactionsTracker getFactionsTracker() {
        return factionsTracker;
    }
    
    /** 
     * recruit unit for a province
     * @param name unit name
     */
    public boolean recruit(String name, int currTurn) {
        // Read the UnitsInfo.JSON
        if (unitsInTraining.size() == 2) {
            return false;
        }

        try {
            String content = Files.readString(Paths.get("src/unsw/gloriaromanus/backend/UnitsInfo.json"));
            JSONObject fullJSON = new JSONObject(content);
            
            boolean is_found = false;
            for (String SoldierName: fullJSON.keySet()) {
                if (SoldierName.equals(name)) {
                    is_found = true;
                    break;
                }
            }
            if (!is_found) return false;
            JSONObject unitData = fullJSON.getJSONObject(name);

            if (unitData.getInt("trainingCost") > this.getFaction(factionName).getBalance()) {
                return false;
            }

            Unit u = new Unit(unitData, this);
            getFaction(factionName).setBalance(this.getFaction(factionName).getBalance() - unitData.getInt("trainingCost"));
            unitsInTraining.add(new TrainingRecord(u, turnTracker.getCurrTurn() + u.getTrainingTurns()));
            
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<Unit> getUnits() {
        return units;
    }
    
    public List<TrainingRecord> getUnitsInTraining() {
        return unitsInTraining;
    }

    /** 
     * for a Army from the given units
     * @param units
     * @return Army
     */
    public void addToArmy(Unit unit) {
        // assume all Units in param is located at current province for now
        this.army.getUnits().add(unit);
        this.army.setMovementPoint();
    }

    
    /** 
     * @param u
     */
    public void removeUnit(Unit u) {
        if (this.units.contains(u)) {
            this.units.remove(u);
        }
    }

    
    /** 
     * @param u
     */
    public void addUnit(Unit u) {
        this.units.add(u);
        u.setProvince(this);
    }

    public String getFactionName() {
        return factionName;
    }

    public void setFaction(Faction faction) {
        if (this.factionsTracker == null) {
            this.factionsTracker = faction.getFactionsTracker();
        }
        this.factionName = faction.getName();
        this.factionsTracker.update(faction, this);
    }

    public void setUnits(List<Unit> units) {
        this.units = units;
    }

    public int getNumOfSoldiers(){
        // used later for displaying number of soldiers in frontend
        int count = 0;
        if (units.size() == 0) {
            return 0;
        }

        for (Unit u : units) {
            count += u.getNumOfTroops();
        }

        return count;
    }

    public String getName() {
        return name;
    }

    public void setTaxRate(double taxRate) {
        // initialize  a double list to store available taxRates
        List<Double> taxRates = new ArrayList<Double>();
        taxRates.add(0.1);
        taxRates.add(0.15);
        taxRates.add(0.2);
        taxRates.add(0.25);

        if (taxRates.contains(taxRate)) {
            this.taxRate = taxRate;
        } 
    }

    public int getTaxRevenue() {
        return (int) Math.rint(wealth * taxRate);
    }

    public int getWealth() {
        return wealth;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public void setUnitsInTraining(List<TrainingRecord> unitsInTraining) {
        this.unitsInTraining = unitsInTraining;
    }

    

    public JSONObject toJSON(){
        JSONObject output = new JSONObject();
        output.put("name", name);
        output.put("wealth", wealth);
        output.put("taxRate", taxRate);
        output.put("factionName", factionName);

        JSONArray unitsJSON = new JSONArray();
        if (units.size() != 0) {
            for (Unit u : units) {
                unitsJSON.put(u.toJSON());
            }
        }  
        output.put("units", unitsJSON);    
        

        JSONArray unitsInTrainingJSON = new JSONArray();
        for (TrainingRecord r : unitsInTraining) {
            unitsInTrainingJSON.put(r.toJSON());
        }
        output.put("unitsInTraining", unitsInTrainingJSON);

        output.put("turnTracker", turnTracker.toJSON());

        return output;
    }

    public void update() {
        // this function gets called at the end of each turn (After clicking endturn)
        // update townealth
        if (taxRate == 0.1) {
            wealth += 10;
        } else if (taxRate == 0.15) {
        } else if (taxRate == 0.2) {
            wealth -= 10;
        }  else if (taxRate == 0.25) {
            wealth -= 30;
        }

        if (wealth < 0) {
            wealth = 0;
        }

        // check units in training
        int nextTurn = turnTracker.getCurrTurn();
        if (!unitsInTraining.isEmpty()) {
            for (TrainingRecord r : unitsInTraining) {
                if (r.getFinishTurn() ==  nextTurn) {
                    // training finished!
                    units.add(r.getUnit());
                    if (r.getUnit().getProvincesTracker() == null) {
                        r.getUnit().setProvincesTracker(provincesTracker);
                    }
                    r.setFinished(true);
                }
            }

            List<TrainingRecord> newUnitsInTraining = new ArrayList<TrainingRecord>();
            for (TrainingRecord r : unitsInTraining) {
                if (!r.getFinished()) {
                    newUnitsInTraining.add(r);
                }
            }

            this.setUnitsInTraining(newUnitsInTraining);
        }
    }

    public Army getArmy() {
        return army;
    }

    public void setArmy(Army army) {
        this.army = army;
    }

    public void setFactionName(String factionName) {
        this.factionName = factionName;
    }

    public Faction getFaction(String f) {
        Faction faction = factionsTracker.getFaction(f);
        return faction;
    }
}
