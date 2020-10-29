package unsw.gloriaromanus.backend;

import java.util.List;

public class BattleResolver {
    private Army attacker;
    private Army defender;
    private Army winner;

    public BattleResolver(Army attacker, Army defender) {
        this.attacker = attacker;
        this.defender = defender;

        this.winner = startBattle(attacker, defender);
    }

    private Army startBattle(Army attacker, Army defender) {
        List<Unit> attackerUnits = attacker.getUnits();
        List<Unit> defenderUnits = defender.getUnits();
        Faction attackFaction = attacker.getFaction();

        Army winner = null;

        // counter for number of engagements
        int engCounter = 0;
        int i = 0;
        int j = 0;

        outerloop:
        while (i < attacker.getNumOfUnits() && j < defender.getNumOfUnits()) {
            Unit attackUnit = attackerUnits.get(i);
            for (; j < defender.getNumOfUnits(); j++) {
                Unit defendUnit = defenderUnits.get(j);
                Engagement e = new Engagement(attackUnit, defendUnit);
                if (e.getWinnerFaction().equals(attackFaction)) {
                    // attacker faction win -> battle with next defender unit
                    j++;
                } else {
                    // defender faction win -> move to next attacker unit
                    i++;
                }
                engCounter++;
                if (engCounter == 200) {
                    // Draw: The invading army in a draw should return to the province it invaded from.
                    break outerloop;
                }
            }
        }

        if (engCounter == 200) {
            // Draw: The invading army in a draw should return to the province it invaded from.
            winner = null;
            attacker.updateAfterDraw();
            
        } else if (j == defender.getNumOfUnits() && i < attacker.getNumOfUnits()) {
            winner = attacker;
        } else {
            winner = defender;
        }

        return winner;
    }
}
