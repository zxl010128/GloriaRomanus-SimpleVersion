package unsw.gloriaromanus.backend;

import java.util.ArrayList;
import java.util.List;

public class Army {
    private List<Unit> units;
    private int movementPoints;

    public Army(List<Unit> units) {
        this.units = new ArrayList<Unit>();
        int minMovementPoints = units.get(0).getMovementPoints();

        for (Unit s : units) {
            this.units.add(s);
            if (s.getMovementPoints() < minMovementPoints) {
                minMovementPoints = s.getMovementPoints();
            }
        }

        this.movementPoints = minMovementPoints;
    }

    /**
     * assign an army to an occupied province
     * 
     * @param destination player's occupied province
     */
    public void moveTo(Province destination) {
        // implement later
        // Don't forget to update oldProvince after moving!
    }

    /**
     * assign an army to invade a unoccupied province
     */
    public void invade(Province destination) {
        // move

        // after arrivingm, start battle
        // Implement later
        // Don't forget to update oldProvince after moving!
    
    }


}
