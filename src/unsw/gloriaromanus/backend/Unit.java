package unsw.gloriaromanus.backend;

import java.util.ArrayList;
import java.util.List;

public class Unit {
    private List<Troop> troops;
    private int movementPoints;
    private Province province;
    private int speed;
    private String type;

    /**
     *
     * @param troops
     * @param province
     */
    public Unit(List<Troop> troops, Province province) {
        this.troops = new ArrayList<Troop>();

        // A unit is a bunch of troops of the same type
        this.movementPoints = troops.get(0).getMovementPoints();
        this.speed = troops.get(0).getSpeed();
        this.type = troops.get(0).getType();

        this.province = province;
    }

    
    /** 
     * getter method to extract movement points
     * @return int
     */
    public int getMovementPoints() {
        return movementPoints;
    }

    
    /** 
     * getter method to extract speed
     * @return int
     */
    public int getSpeed() {
        return speed;
    }

    
    /** 
     * getter method to extract type
     * @return String
     */
    public String getType() {
        return type;
    }
}
