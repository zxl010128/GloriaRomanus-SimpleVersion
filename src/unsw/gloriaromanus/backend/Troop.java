package unsw.gloriaromanus.backend;

import java.util.ArrayList;
import java.util.List;

public class Troop {
    private List<Soldier> soldiers;
    private int movementPoints;
    private Province province;

    /**
     *
     * @param soldiers
     * @param province
     */
    public Troop(List<Soldier> soldiers, Province province) {
        this.soldiers = new ArrayList<Soldier>();
        int minMovementPoints = soldiers.get(0).getMovementPoints();

        for (Soldier s : soldiers) {
            this.soldiers.add(s);
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
        for (Soldier s : this.soldiers) {
            oldProvince.removeSoldier(s);
            destination.addSoldier(s);
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
