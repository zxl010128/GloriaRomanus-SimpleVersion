package unsw.gloriaromanus.backend;

import java.util.ArrayList;
import java.util.List;

public class Province {
    private String name;
    private int wealth;
    private double taxRate;
    // private Fraction fraction;
    private List<Troop> troops;

    public Province(String name) {
        this.name = name;
        this.wealth = 0;

        // assume the default tax is normal tax (0.15)
        this.taxRate = 0.15;

        this.troops = new ArrayList<Troop>();
    }

    public Province(String name, int wealth) {
        this.name = name;
        this.wealth = wealth;
        
        // assume the default tax is normal tax (0.15)
        this.taxRate = 0.15;

        this.troops = new ArrayList<>();
    }

    public void recruit(String type) {
        // parameter may be changed later
        switch (type) {
            // implement later after fininshing soldier class
            default:
                break;
        }
    }

    public Unit generateTroop(List<Troop> troops) {
        // assume all troops in param is located at current province for now
        return new Unit(troops, this);
    }

    public void removeTroop(Troop s) {
        if (this.troops.contains(s)) {
            this.troops.remove(s);
        }
    }

    public void addTroop(Troop s) {
        this.troops.add(s);
    }


}
