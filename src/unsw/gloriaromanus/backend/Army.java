package unsw.gloriaromanus.backend;

import java.util.ArrayList;
import java.util.List;

public class Army {
    private List<Unit> units;
    private int movementPoints;
    private Province province;

    public Army(List<Unit> units, Province province) {
        this.units = new ArrayList<Unit>();
        int minMovementPoints = units.get(0).getMovementPoints();

        for (Unit s : units) {
            this.units.add(s);
            if (s.getMovementPoints() < minMovementPoints) {
                minMovementPoints = s.getMovementPoints();
            }
        }

        this.movementPoints = minMovementPoints;
        this.province = province;
    }

    /**
     * assign an army to an occupied province
     * 
     * @param destination player's occupied province
     */
    public void moveToOccupied(Province destination) {
        // implement later
        // Don't forget to update oldProvince after moving!
        // check if the destination is okay to move to

        // move

        // update after moving
        for (Unit u : units) {
            destination.addUnit(u);
            province.removeUnit(u);
        }
    }

    /**
     * assign an army to invade a unoccupied province
     */
    public void invade(Province destination) {
        // check if the destination is able to move to

        // move

        // after arrivingm, start battle
        // Don't forget to update oldProvince after moving!
        Army defenseArmy = new Army(destination.getUnits(), destination);
        Battle battleResolver = new Battle(this, defenseArmy);
        Army winner = battleResolver.getWinner();
        if (winner == null) {
            // Draw
            this.updateAfterDraw();

        } else if (winner.getFaction().equals(this.getFaction())) {
            // Win
            this.updateAfterWin(destination);

        } else {
            // Lose
            this.updateAfterLose();

        }
    
    }

    public List<Unit> getUnits() {
        return units;
    }

    public void setUnits(List<Unit> units) {
        this.units = units;
    }

    public Faction getFaction() {
        return province.getFraction();
    }

    public Province getProvince() {
        return province;
    }

    public int getNumOfUnits() {
        return units.size();
    }

    public boolean containAvalUnits(){
        for (Unit u : units) {
            if (u.getHealth() > 0) {
                return true;
            }
        }

        return false;
    }
    
    public void updateAfterDraw() {
        // moveTo(getProvince());
    }


    public void updateAfterWin(Province destination) {
        // moveTo(destination);
        
        // update the occupied province
        destination.setFaction(this.getFaction());
        destination.setUnits(this.getUnits());
    }

    public void updateAfterLose() {
        // do i need to remove unit here?
        // or the unit will be removed when they are defeated?
    }

}
