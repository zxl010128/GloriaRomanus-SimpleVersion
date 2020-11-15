package unsw.gloriaromanus.backend;

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
    // add new specialAbility
    private String specialAbility;

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
        this.specialAbility = input.getString("specialAbility");
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
        if (this.provinceName.equals("null")) this.provinceName = null;
        this.factionName = json.getString("factionName");
        if (this.factionName.equals("null")) this.factionName = null;
        this.attackDamage = json.getInt("attackDamage");
        this.armor = json.getInt("armor");
        this.defense = json.getInt("defense");
        this.shield = json.getInt("shield");
        this.morale = json.getInt("morale");
        this.provincesTracker = null;
        this.specialAbility = json.getString("specialAbility");
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

    public String getSpecialAbilityName() {
        return specialAbility;
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

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setMorale(int morale) {
        this.morale = morale;
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

    public void addMoraleBy(int i) {
        this.morale += i;
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
        output.put("provinceName", (provinceName == null) ? "null" : provinceName);
        output.put("factionName", (factionName == null) ? "null" : factionName);
        output.put("attackDamage", attackDamage);
        output.put("armor", armor);
        output.put("defense", defense);
        output.put("shield", shield);
        output.put("morale", morale);
        output.put("specialAbility", specialAbility);

        return output;
    }

    public void useAbility(Unit opponent) {
        switch (specialAbility) {
            case "Legionary eagle":
                for (Unit u : this.getProvince().getUnits()) {
                    u.addMoraleBy(1);
                }
                break;
            case "Berserker rage":
                this.morale = 9999;
                this.armor = 0;
                this.shield = 0;
                this.attackDamage *= 2;
                break;
            case "Heroic charge":
                this.attackDamage *= 2;
                this.morale += (int) (this.morale * 0.5);
                break;
            case "Phalanx":
                this.defense *= 2;
                this.movementPoints -= (int) (this.movementPoints/2);
                break;
            case "Elephants running amok":
                Random rand = new Random();
                int n = rand.nextInt(10);
                if (n == 0) {
                    for (Unit u : this.getProvince().getUnits()) {
                        u.removeHealthBy(this.attackDamage);
                    }
                }
                break;
            case "Cantabrian circle":
                int newDamage = (int) (opponent.getAttackDamage() * 0.5);
                opponent.setAttackDamage((newDamage));
                
                break;
            case "Druidic fervour":
                List<Unit> alliedUnits = this.getProvince().getUnits();
                for (Unit u : alliedUnits) {
                    u.addMoraleBy((int)(u.getMorale()/10));
                }
                break;
            case "Shield charge" :
                this.setAttackDamage(this.attackDamage + this.defense);
                break;
        }
    }

    public void setFactionName(String factionName) {
        this.factionName = factionName;
    }

    public String getSpecialAbility() {
        return specialAbility;
    }

}
