package unsw.gloriaromanus.backend;

import java.util.ArrayList;
import java.util.List;

public class Faction {
    private String name;
    private int balance;
    private List<Province> provinces;

    public Faction(String name, ArrayList<Province> startingProvinces) {
        this.name = name;
        this.balance = 100;
        this.provinces = startingProvinces;
    }

    public void addProvince(Province p) {
        provinces.add(p);
    }

    public void removeProvince(Province p) {
        provinces.remove(p);
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

}
