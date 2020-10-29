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
    private Province province;
    // private boolean avalibility; // MAYBE USELESS!!!!!!
    private int attackDamage;
    private int armor;
    private int shield;
    private int morale;
    

    /**
     *
     * @param troops
     * @param province
     */
    public Unit(Province province) {
        // Implement later
        // user factory pattern to read from JSON file

        // A unit is a bunch of troops of the same type
        // this.movementPoints = troops.get(0).getMovementPoints();
        // this.type = troops.get(0).getType();
        // this.attackDamage = troops.get(0).getAttackDamage();

        this.province = province;
        // this.avalibility = true;
    }

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

    public Faction getFaction(){
        return province.getFraction();
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public int getNumOfTroops() {
        return numOfTroops;
    }

    // public boolean getAval(){
    //     return avalibility;
    // }

    // public void setAval(boolean b) {
    //     avalibility = b;
    // }

    public int getAttackDamage() {
        return attackDamage;
    }

    public int getArmor() {
        return armor;
    }

    public int getShield() {
        return shield;
    }

    public int getTotalDefense() {
        return armor + shield;
    }

    public int getHealth() {
        return health;
    }

    public void updateAfterDefeated() {
        // MAYBE BE USELESS
        // remove the defeated unit from its original province
        // province.removeUnit(this); 

        // remove the defeated unit from its army


    }

    public void attack(Unit defender, int damage) {
        int totalDefense = defender.getTotalDefense();
        int actualDamage = damage - totalDefense;
        if (actualDamage < 0) {
            actualDamage = 0;
        }

        defender.removeHealthBy(actualDamage);
    }
}
