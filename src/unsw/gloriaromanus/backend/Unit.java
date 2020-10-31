package unsw.gloriaromanus.backend;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONObject;

public class Unit {
    private String name;
    private String type; // "missile" or "melee"
    private int health;
    private int trainingCost;
    private int trainingTurns;
    private int numOfTroops;
    private int movementPoints;
    private String provinceName;
    private String factionName;
    private int attackDamage;
    private int armor;
    private int defense;
    private int shield;
    private int morale;
    private ProvincesTracker provincesTracker;

    public Unit(JSONObject input, Province province) {
        this.name = input.getString("name");
        this.type = input.getString("type");
        this.movementPoints = input.getInt("movementPoints");
        this.health = input.getInt("health");
        this.trainingCost = input.getInt("trainingCost");
        this.trainingTurns = input.getInt("trainingTurns");
        // this.specialAbility = input.getString("specialAbility");
        this.morale = input.getInt("morale");
        this.attackDamage = input.getInt("attackDamage");
        this.defense = input.getInt("defense");
        this.numOfTroops = input.getInt("numOfTroops");

        this.provinceName = (province != null) ? province.getName() : null;
        this.factionName = (province != null) ? province.getFactionName() : null;

        this.shield = 5;
        this.armor = 5;

        this.provincesTracker =  (province != null) ? province.getProvincesTracker() : null;
    }

    public Unit(JSONObject json) {
        this.name = json.getString("name");
        this.type = json.getString("type");
        this.health = json.getInt("health");
        this.trainingCost = json.getInt("trainingCost");
        this.trainingTurns = json.getInt("trainingTurns");
        this.numOfTroops = json.getInt("numOfTroops");
        this.movementPoints = json.getInt("movementPoints");
        this.provinceName = json.getString("provinceName");
        this.factionName = json.getString("factionName");
        this.attackDamage = json.getInt("attackDamage");
        this.armor = json.getInt("armor");
        this.defense = json.getInt("defense");
        this.shield = json.getInt("shield");
        this.morale = json.getInt("morale");
        this.provincesTracker = null;
    }

    
    /** 
     * @return ProvincesTracker
     */
    public ProvincesTracker getProvincesTracker() {
        return provincesTracker;
    }

    
    /** 
     * @param provincesTracker
     */
    public void setProvincesTracker(ProvincesTracker provincesTracker) {
        this.provincesTracker = provincesTracker;
    }

    
    /** 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    
    /** 
     * @return String
     */
    public String getName() {
        return name;
    }
    
    /** 
     * getter method to extract movement points
     * @return int
     */
    public int getMovementPoints() {
        return movementPoints;
    }

    
    /** 
     * @param damage
     */
    public void removeHealthBy(int damage) {
        health -= damage;
        if (health < 0) {
            health = 0;
        }

        updateAfterDefeated();
    }
    
    /** 
     * getter method to extract type
     * @return String
     */
    public String getType() {
        return type;
    }

    
    /** 
     * @return String
     */
    public String getFactionName() {
        return factionName;
    }

    
    /** 
     * @return String
     */
    public String getProvinceName() {
        return provinceName;
    }

    
    /** 
     * @return int
     */
    public int getDefense() {
        return defense;
    }

    
    /** 
     * @return Province
     */
    // public Faction getFaction(){
    //     return getProvince().getFactionName();
    // }

    public Province getProvince() {
        return provincesTracker.getProvince(provinceName);
    }

    
    /** 
     * @param province
     */
    public void setProvince(Province province) {
        this.provinceName = province.getName();
        this.factionName = province.getFactionName();
    }

    
    /** 
     * @return int
     */
    public int getNumOfTroops() {
        return numOfTroops;
    }

    
    /** 
     * @return int
     */
    public int getTrainingCost() {
        return trainingCost;
    }

    
    /** 
     * @return int
     */
    public int getTrainingTurns() {
        return trainingTurns;
    }

    
    /** 
     * @return int
     */
    public int getAttackDamage() {
        return attackDamage;
    }

    
    /** 
     * @return int
     */
    public int getArmor() {
        return armor;
    }

    
    /** 
     * @return int
     */
    public int getShield() {
        return shield;
    }

    
    /** 
     * @return int
     */
    public int getTotalDefense() {
        return defense + armor + shield;
    }

    
    /** 
     * @return int
     */
    public int getHealth() {
        return health;
    }

    
    /** 
     * @return int
     */
    public int getMorale() {
        return morale;
    }

    public void updateAfterDefeated() {
        // MAYBE BE USELESS
        // remove the defeated unit from its original province
        // province.removeUnit(this); 

        // remove the defeated unit from its army


    }

    
    /** 
     * @param defender
     * @param damage
     */
    public void attack(Unit defender, int damage) {
        int totalDefense = defender.getTotalDefense();
        int actualDamage = damage - totalDefense;
        if (actualDamage < 0) {
            actualDamage = 0;
        }

        defender.removeHealthBy(actualDamage);
    }

    public JSONObject toJSON(){
        // JSONObject out = new JSONObject();
        JSONObject output = new JSONObject();
        output.put("name", name);
        output.put("type", type);
        output.put("health", health);
        output.put("trainingCost", trainingCost);
        output.put("trainingTurns", trainingTurns);
        output.put("numOfTroops", numOfTroops);
        output.put("movementPoints", movementPoints);
        output.put("provinceName", (provinceName == null) ? JSONObject.NULL : provinceName);
        output.put("factionName", (factionName == null) ? JSONObject.NULL : factionName);
        output.put("attackDamage", attackDamage);
        output.put("armor", armor);
        output.put("defense", defense);
        output.put("shield", shield);
        output.put("morale", morale);

        return output;
    }


}
