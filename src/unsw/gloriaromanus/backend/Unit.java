package unsw.gloriaromanus.backend;

import java.util.ArrayList;
import java.util.List;

public class Unit {
    private List<Troop> troops;
    private int movementPoints;
    private Province province;

    /**
     *
     * @param troops
     * @param province
     */
    public Unit(List<Troop> troops, Province province) {
        this.troops = new ArrayList<Troop>();
        int minMovementPoints = troops.get(0).getMovementPoints();

        for (Troop s : troops) {
            this.troops.add(s);
            if (s.getMovementPoints() < minMovementPoints) {
                minMovementPoints = s.getMovementPoints();
            }
        }

        this.movementPoints = minMovementPoints;
        this.province = province;
    }
 
    /**
     * assign a troop to an occupied province
     * 
     * @param destination player's occupied province
     */
    public void moveTo(Province destination) {
        // move

        // after arriving
        Province oldProvince = this.province;
        for (Troop t : this.troops) {
            oldProvince.removeTroop(t);
            destination.addTroop(t);
        }

    }

    /**
     * assign a troop to invade a unoccupied province
     */
    public void invade(Province destination) {
        // move

        // after arrivingm, start battle
        // Implement later
    

    }

    public Province getProvince() {
        return province;
    }

}
