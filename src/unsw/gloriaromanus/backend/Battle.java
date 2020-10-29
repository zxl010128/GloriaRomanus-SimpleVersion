package unsw.gloriaromanus.backend;

import java.util.List;
import java.util.ArrayList;

public class Battle {
    private Army attackerArmy;
    private Army defenderArmy;
    private Army winner;
    private List<Skirmish> skirmishes;

    public Battle(Army attackerArmy, Army defenderArmy) {
        this.attackerArmy = attackerArmy;
        this.defenderArmy = defenderArmy;
        this.skirmishes = new ArrayList<Skirmish>();

        startBattle(attackerArmy, defenderArmy);
    }

    private void startBattle(Army attackerArmy, Army defenderArmy) {
        List<Unit> attackerUnits = attackerArmy.getUnits();
        List<Unit> defenderUnits = defenderArmy.getUnits();
        Faction attackFaction = attackerArmy.getFaction();

        // counter for number of engagements
        int engCounter = 0;

        // Finish this after implement attack function!!!!!!!!!
        while (attackerArmy.containAvalUnits() && defenderArmy.containAvalUnits()) {
            Unit attackerUnit = unitRandomPicker(attackerArmy);
            Unit defenderUnit = unitRandomPicker(defenderArmy);

            Skirmish s = new Skirmish(attackerUnit, defenderUnit);
            skirmishes.add(s);
            engCounter += s.getNumOfEngagements();

            if (engCounter >= 200) {
                break;
            }


        }

        if (engCounter >= 200) {
            winner = null;
        } else if (attackerArmy.containAvalUnits()) {
            winner = attackerArmy;
        } else {
            winner = defenderArmy;
        }

    }

    private Unit unitRandomPicker(Army a) {
        return null;
    }
    public Army getWinner() {
        return winner;
    }
}
