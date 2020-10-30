package unsw.gloriaromanus.backend;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Province {
    private String name;
    private int wealth;
    private double taxRate;
    private Faction faction;
    private List<Unit> units;
    private List<Double> taxRates;

    public Province(String name, Faction faction) {
        this.name = name;
        this.wealth = 100;

        // assume the default tax is normal tax (0.15)
        this.taxRate = 0.15;

        this.faction = faction;
        this.units = new ArrayList<Unit>();
    }

    
    /** 
     * recruit soldier for a province
     * @param type soldier type
     */
    public void recruit(String type, int currTurn) {
        // Read the UnitsInfo.JSON

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

    public String getName() {
        return name;
    }

    public void setTaxRate(double taxRate) {
        // initialize  a double list to store available taxRates
        this.taxRates = new ArrayList<Double>();
        taxRates.add(0.1);
        taxRates.add(0.15);
        taxRates.add(0.2);
        taxRates.add(0.25);

        if (taxRates.contains(taxRate)) {
            this.taxRate = taxRate;
        } else {
            System.out.println("invalid tax rate!");
        }
    }

    public int getTaxRevenue() {
        return (int) Math.rint(wealth * taxRate);
    }

    public int getWealth() {
        return wealth;
    }

    public void update() {
        // this function gets called each turn
        // update townealth
        switch ((int) (taxRate * 100)) {
            case 10: 
                // low tax
                wealth += 10;
                break;
            case 15: 
                // normal tax
                break;
            case 20: 
                // high tax
                wealth -= 10;
                break;
            case 25:
                // very high tax
                wealth -= 30;
                break;
        }

        if (wealth < 0) {
            wealth = 0;
        }
    }

}
