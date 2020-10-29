package unsw.gloriaromanus.backend;

import java.util.ArrayList;
import java.util.List;

public class Province {
    private String name;
    private int wealth;
    private double taxRate;
    private Faction faction;
    private List<Unit> units;

    public Province(String name) {
        this.name = name;
        this.wealth = 0;

        // assume the default tax is normal tax (0.15)
        this.taxRate = 0.15;

        this.units = new ArrayList<Unit>();
    }

    
    /** 
     * recruit soldier for a province
     * @param type soldier type
     */
    public void recruit(String type) {
        // parameter may be changed later
        switch (type) {
            // implement later after fininshing soldier class
            default:
                break;
        }
    }

    public List<Unit> getUnits() {
        return units;
    }
    
    /** 
     * for a Army from the given units
     * @param units
     * @return Army
     */
    public Army generateArmy(List<Unit> units) {
        // assume all Units in param is located at current province for now
        return new Army(units, this);
    }

    
    /** 
     * @param u
     */
    public void removeUnit(Unit u) {
        if (this.units.contains(u)) {
            this.units.remove(u);
        }
    }

    
    /** 
     * @param u
     */
    public void addUnit(Unit u) {
        this.units.add(u);
        u.setProvince(this);
    }

    public Faction getFraction() {
        return faction;
    }

    public void setFaction(Faction faction) {
        this.faction = faction;
    }

    public void setUnits(List<Unit> units) {
        this.units = units;
    }

    public int getNumOfSoldiers(){
        // used later for displaying number of soldiers in frontend
        int count = 0;
        if (units.size() == 0) {
            return 0;
        }

        for (Unit u : units) {
            count += u.getNumOfTroops();
        }

        return count;
    }
    

}
