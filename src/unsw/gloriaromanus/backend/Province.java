package unsw.gloriaromanus.backend;

import java.util.ArrayList;
import java.util.List;

public class Province {
    private String name;
    private int wealth;
    private double taxRate;
    // private Fraction fraction;
    private List<Soldier> soldiers;

    public Province(String name) {
        this.name = name;
        this.wealth = 0;

        // assume the default tax is normal tax (0.15)
        this.taxRate = 0.15;

        this.soldiers = new ArrayList<Soldier>();
    }

    public Province(String name, int wealth) {
        this.name = name;
        this.wealth = wealth;
        
        // assume the default tax is normal tax (0.15)
        this.taxRate = 0.15;

        this.soldiers = new ArrayList<>();
    }

    public void recruit(String type) {
        // parameter may be changed later
        switch (type) {
            // implement later after fininshing soldier class
            default:
                break;
        }
    }

    public Troop generateTroop(List<Soldier> soldiers) {
        // assume all soldiers in param is located at current province for now
        return new Troop(soldiers, this);
    }

    public void removeSoldier(Soldier s) {
        if (this.soldiers.contains(s)) {
            this.soldiers.remove(s);
        }
    }

    public void addSoldier(Soldier s) {
        this.soldiers.add(s);
    }


}
