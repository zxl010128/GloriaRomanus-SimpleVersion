package unsw.gloriaromanus.backend;

import java.util.ArrayList;
import java.util.List;

public class Province {
    private String name;
    private int wealth;
    private double taxRate;
    private List<Troop> troops;
    private int soldiersNum;

    public Province(String name) {
        this.name = name;
        this.wealth = 0;

        // assume the default tax is normal tax (0.15)
        this.taxRate = 0.15;

        this.troops = new ArrayList<Troop>();
    }

    
    /** 
     * @param type the type of the soldier recruiting
     */
    public void recruit(String type) {
        // parameter may be changed later
        switch (type) {
            // implement later after fininshing soldier class
            default:
                break;
        }
    }

    
    /** 
     * form a Unit from the given troops
     * @param troops 
     * @return Unit
     */
    public Unit generateTroop(List<Troop> troops) {
        // assume all troops in param is located at current province for now
        return new Unit(troops, this);
    }

    
    /** 
     * @param s the troop wanna remove
     */
    public void removeTroop(Troop s) {
        if (this.troops.contains(s)) {
            this.troops.remove(s);
        }
    }

    
    /** 
     * @param s the troop wanna add
     */
    public void addTroop(Troop s) {
        this.troops.add(s);
    }


}
