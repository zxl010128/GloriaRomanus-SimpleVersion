package unsw.gloriaromanus.backend;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class Faction {
    private String name;
    private int balance;
    private int totalWealth;
    private List<Province> provinces;

    public Faction(String name, ArrayList<Province> startingProvinces) {
        this.name = name;
        this.balance = 100;
        this.provinces = startingProvinces;
        this.totalWealth = 0;
        for (Province p : provinces) {
            totalWealth += p.getWealth();
        }
    }

    public void addProvince(Province p) {
        provinces.add(p);
        totalWealth += p.getWealth();
    }

    public void removeProvince(Province p) {
        provinces.remove(p);
        totalWealth -= p.getWealth();

    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }
        
        if (getClass() != obj.getClass()) {
            return false;
        }

        Faction f = (Faction) obj;
        
        return this.name.equals(f.getName());
    }

    public List<Province> getProvinces() {
        return provinces;
    }

    public JSONObject toJSON() {
        JSONObject output = new JSONObject();

        return output;
    }

    public void update() {
        // this is function get called at the end of each turn (after clicking button)

        // First, update all occupied provinces
        // Maybe delete this later!
        for (Province p : provinces) {
            p.update();
        }

        int totalTaxRevenue = 0;
        int totalWealth = 0;

        for (Province p : provinces) {
            totalTaxRevenue += p.getTaxRevenue();
            totalWealth += p.getWealth();
        }

        this.balance += totalTaxRevenue;
        this.totalWealth = totalWealth;
    }
}
