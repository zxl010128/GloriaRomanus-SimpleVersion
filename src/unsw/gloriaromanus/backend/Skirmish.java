package unsw.gloriaromanus.backend;

import java.util.ArrayList;
import java.util.List;

public class Skirmish {
    private List<Engagement> engagements;
    private Unit attackUnit;
    private Unit defendUnit;
    private Unit winner;

    public Skirmish(Unit attackUnit, Unit defendUnit) {
        this.attackUnit = attackUnit;
        this.defendUnit = defendUnit;
        this.winner = null;
        this.engagements = new ArrayList<Engagement>();
        startBattle(attackUnit, defendUnit);
    }

    private void startBattle(Unit attackUnit, Unit defendUnit) {
        // During a skirmish, both units engage in a sequence of engagements against each other until a unit successfully routes 
        // (runs away from the battle permanently) or is defeated.
        while (attackUnit.getHealth() > 0 && defendUnit.getHealth() > 0) {
            if (engagements.size() > 200) {
                break;
            }
            Engagement e = new Engagement(attackUnit, defendUnit);
            engagements.add(e);
        }

        if (attackUnit.getHealth() == 0) {
            winner = defendUnit;
        } else if (defendUnit.getHealth() == 0) {
            winner = attackUnit;
        }

    }

    public int getNumOfEngagements() {
        return engagements.size();
    }

    public Unit getWinner() {
        return winner;
    }

}
