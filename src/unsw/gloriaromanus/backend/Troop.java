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

    
    /** 
     * getter method to extract movement points
     * @return int
     */
    public int getMovementPoints() {
        return movementPoints;
    };

    
    /** 
     * getter method to extract type
     * @return String
     */
    public String getType() {
        return type;
    }

    
    /** 
     * getter method to extract speed
     * @return int
     */
    public int getSpeed() {
        return speed;
    }
}
