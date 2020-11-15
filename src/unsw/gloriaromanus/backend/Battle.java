package unsw.gloriaromanus.backend;

import java.util.List;
import java.util.Random;
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

    public Army getWinner() {
        return winner;
    }

    private void startBattle(Army attackerArmy, Army defenderArmy) {
        // counter for number of engagements
        int engCounter = 0;

        // Finish this after implement attack function!!!!!!!!!
        while (attackerArmy.containAvalUnits() && defenderArmy.containAvalUnits()) {
            Unit attackerUnit = unitRandomPicker(attackerArmy);
            Unit defenderUnit = unitRandomPicker(defenderArmy);

            Skirmish s = new Skirmish(attackerUnit, defenderUnit);
            skirmishes.add(s);

            Unit unitWinner = s.getWinner();

            if (unitWinner.getProvinceName().equals(attackerUnit.getProvinceName())) {
                defenderArmy.removeUnit(defenderUnit);
                defenderArmy.getProvince().removeUnit(defenderUnit);
            } else {
                attackerArmy.removeUnit(attackerUnit);
                attackerArmy.getProvince().removeUnit(attackerUnit);
                attackerArmy.getProvince().getArmy().removeUnit(attackerUnit);
            }
            
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
        List<Unit> units = a.getUnits();
        List<Unit> aliveUnits = new ArrayList<Unit>();
        for (Unit u : units) {
            if (u.getHealth() > 0) {
                aliveUnits.add(u);
            }
        }

        // obtain a number between [0, aliveUnits.size())
        Random rand = new Random();
        int n = rand.nextInt(aliveUnits.size());

        return aliveUnits.get(n);
    }

    public void updateAfterBattle(Army attackerArmy, Army defenderArmy) {
        // remove dead unit from army
        List<Unit> attackerArmyUnits = attackerArmy.getUnits();
        List<Unit> attackerArmyNewUnits = new ArrayList<Unit>();
        Province attackerProvince = attackerArmy.getProvince();

        for (Unit u : attackerArmyUnits){
            if (u.getHealth() > 0) {
                attackerArmyNewUnits.add(u);
            } else {
                attackerProvince.getUnits().remove(u);
            }
        }
        attackerArmy.setUnits(attackerArmyNewUnits);

        List<Unit> defenderArmyUnits = defenderArmy.getUnits();
        List<Unit> defenderArmyNewUnits = new ArrayList<Unit>();
        Province defenderProvince = defenderArmy.getProvince();

        for (Unit u : defenderArmyUnits) {
            if (u.getHealth() > 0) {
                defenderArmyNewUnits.add(u);
            } else {
                defenderProvince.getUnits().remove(u);
            }
        }
        defenderArmy.setUnits(defenderArmyNewUnits);
       
    }

    
}
