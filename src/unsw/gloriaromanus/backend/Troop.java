package unsw.gloriaromanus.backend;

public class Troop {
    private String type;
    private int trainingCost;
    private int trainingTurns;
    private boolean trainingFinished;
    // private SpecialAbility specialAbility;
    private int morale;
    private int attackDamage;
    private int defense;
    private int speed;
    private int movementPoints;

    public int getMovementPoints() {
        return movementPoints;
    };

    public String getType() {
        return type;
    }

    public int getSpeed() {
        return speed;
    }
}
