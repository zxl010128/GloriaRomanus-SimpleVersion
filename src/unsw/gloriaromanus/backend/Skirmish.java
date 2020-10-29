package unsw.gloriaromanus.backend;

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
        startBattle(attackUnit, defendUnit);
    }

    private void startBattle(Unit attackUnit, Unit defendUnit) {
        // During a skirmish, both units engage in a sequence of engagements against each other until a unit successfully routes 
        // (runs away from the battle permanently) or is defeated.
        while (attackUnit.getAval() && defendUnit.getAval()) {
            Engagement e = new Engagement(attackUnit, defendUnit);
        }
        
        if (!attackUnit.getAval()) {
            winner = defendUnit;
        } else if (!defendUnit.getAval()) {
            winner = attackUnit;
        }

    }

}
