package unsw.gloriaromanus.backend;

import java.util.ArrayList;
import java.util.List;

public class Unit {
    private List<Troop> troops;
    private int movementPoints;
    private Province province;
    private String type;
    private boolean isDefeated;

    /**
     *
     * @param troops
     * @param province
     */
    public Unit(List<Troop> troops, Province province) {
        this.troops = new ArrayList<Troop>();

        // A unit is a bunch of troops of the same type
        this.movementPoints = troops.get(0).getMovementPoints();
        this.type = troops.get(0).getType();

        this.province = province;
        this.isDefeated = false;
    }

    
    /** 
     * getter method to extract movement points
     * @return int
     */
    public int getMovementPoints() {
        return movementPoints;
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

    public void attack(Unit u) {

    }
}
