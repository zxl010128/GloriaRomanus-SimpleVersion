package unsw.gloriaromanus;

/**
 * Represents a basic unit of soldiers
 * 
 * incomplete - should have heavy infantry, skirmishers, spearmen, lancers, heavy cavalry, elephants, chariots, archers, slingers, horse-archers, onagers, ballista, etc...
 * higher classes include ranged infantry, cavalry, infantry, artillery
 * 
 * current version represents a heavy infantry unit (almost no range, decent armour and morale)
 */
public class Unit {
    private int numTroops;  // the number of troops in this unit (should reduce based on depletion)
    private int range;  // range of the unit
    private int armour;  // armour defense
    private int morale;  // resistance to fleeing
    private int speed;  // ability to disengage from disadvantageous battle
    private int attack;  // can be either missile or melee attack to simplify. Could improve implementation by differentiating!
    private int defenseSkill;  // skill to defend in battle. Does not protect from arrows!
    private int shieldDefense; // a shield

    public Unit(){
        // TODO = obtain these values from the file for the unit
        numTroops = 50;
        range = 1;
        armour = 5;
        morale = 10;
        speed = 10;
        attack = 6;
        defenseSkill = 10;
        shieldDefense = 3;
    }

    public int getNumTroops(){
        return numTroops;
    }

    
}
