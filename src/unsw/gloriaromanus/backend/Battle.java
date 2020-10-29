package unsw.gloriaromanus.backend;

import java.util.List;

public class Battle {
    private Army attackerArmy;
    private Army defenderArmy;
    private Army winner;

    public Battle(Army attackerArmy, Army defenderArmy) {
        this.attackerArmy = attackerArmy;
        this.defenderArmy = defenderArmy;

        startBattle(attackerArmy, defenderArmy);
    }

    private void startBattle(Army attackerArmy, Army defenderArmy) {
        List<Unit> attackerUnits = attackerArmy.getUnits();
        List<Unit> defenderUnits = defenderArmy.getUnits();
        Faction attackFaction = attackerArmy.getFaction();

        // Finish this after implement attack function!!!!!!!!!

        // counter for number of engagements
        int engCounter = 0;
        int i = 0;
        int j = 0;

        outerloop:
        while (i < attackerArmy.getNumOfUnits() && j < defenderArmy.getNumOfUnits()) {
            Unit attackUnit = attackerUnits.get(i);
            for (; j < defenderArmy.getNumOfUnits(); j++) {
                Unit defendUnit = defenderUnits.get(j);
                Engagement e = new Engagement(attackUnit, defendUnit);
                if (e.getWinnerFaction().equals(attackFaction)) {
                    // attackerArmy faction win -> battle with next defenderArmy unit
                    j++;
                } else {
                    // defenderArmy faction win -> move to next attackerArmy unit
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
            winner = null;
        } else if (j == defenderArmy.getNumOfUnits() && i < attackerArmy.getNumOfUnits()) {
            winner = attackerArmy;
        } else {
            winner = defenderArmy;
        }

    }

    public Army getWinner() {
        return winner;
    }
}
