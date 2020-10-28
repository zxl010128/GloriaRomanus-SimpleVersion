package unsw.gloriaromanus.backend;

import java.util.ArrayList;
import java.util.List;

public class Province {
    private String name;
    private int wealth;
    private double taxRate;
    // private Fraction fraction;
    private List<Unit> units;

    public Province(String name) {
        this.name = name;
        this.wealth = 0;

        // assume the default tax is normal tax (0.15)
        this.taxRate = 0.15;

        this.units = new ArrayList<Unit>();
    }

    public void recruit(String type) {
        // parameter may be changed later
        switch (type) {
            // implement later after fininshing soldier class
            default:
                break;
        }
    }

    public Army generateArmy(List<Unit> units) {
        // assume all Units in param is located at current province for now
        return new Army(units);
    }

    public void removeUnit(Unit s) {
        if (this.units.contains(s)) {
            this.units.remove(s);
        }
    }

    public void addUnit(Unit s) {
        this.units.add(s);
    }


}
