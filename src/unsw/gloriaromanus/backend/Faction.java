package unsw.gloriaromanus.backend;

import java.util.ArrayList;
import java.util.List;

public class Faction {
    private String name;
    private int balance;
    private List<Province> provinces;

    public Faction(String name, Province startingProvince) {
        this.name = name;
        this.balance = 100;
        this.provinces = new ArrayList<Province>();
        provinces.add(startingProvince);
    }

    public void addProvince(Province p) {
        provinces.add(p);
    }

    public void removeProvince(Province p) {
        provinces.remove(p);
    }

}
