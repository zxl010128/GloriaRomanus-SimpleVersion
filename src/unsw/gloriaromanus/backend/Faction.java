package unsw.gloriaromanus.backend;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class Faction implements Subject{
    private String name;
    private int balance;
    private int totalWealth;
    private List<Province> provinces;
    private ProvincesTracker provincesTracker;
    private FactionsTracker factionsTracker;
    private GameSystem gamesys;
    private boolean is_win;

    public Faction(String name, ProvincesTracker provincesTracker, FactionsTracker factionsTracker) {
        this.name = name;
        this.balance = 100;
        this.provinces = new ArrayList<Province>();
        this.totalWealth = 0;
        this.gamesys = null;
        this.is_win = false;
        this.provincesTracker = provincesTracker;
        this.factionsTracker = factionsTracker;
        factionsTracker.addFaction(this);
    }

    public Faction(String name, ArrayList<Province> startingProvinces, ProvincesTracker provincesTracker, FactionsTracker factionsTracker) {
        this.name = name;
        this.balance = 100;
        this.provinces = startingProvinces;
        this.totalWealth = 0;
        this.gamesys = null;
        this.is_win = false;
        this.provincesTracker = provincesTracker;
        this.factionsTracker = factionsTracker;
        for (Province p : provinces) {
            totalWealth += p.getWealth();
            this.provincesTracker.addProvince(p);
            p.setFaction(this);
        }

        
        factionsTracker.addFaction(this);       
    }

    public Faction(JSONObject json) {
        this.name = json.getString("name");
        this.balance = json.getInt("balance");
        this.totalWealth = json.getInt("totalWealth");
        this.provinces = new ArrayList<Province>();
        this.gamesys = null;
        this.is_win = json.getBoolean("is_win");
        JSONArray provincesJSON = json.getJSONArray("provinces");
        for (int i = 0; i < provincesJSON.length(); i++) {
            this.provinces.add(new Province(provincesJSON.getJSONObject(i)));
        }

        this.provincesTracker = new ProvincesTracker(json.getJSONObject("provincesTracker"));
        for (Province p : this.provinces) {
            p.setProvincesTracker(this.provincesTracker);
        }
    }

    public ProvincesTracker getProvincesTracker() {
        return provincesTracker;
    }

    public FactionsTracker getFactionsTracker() {
        return factionsTracker;
    }

    public int getTotalWealth() {
        return totalWealth;
    }

    public int getBalance() {
        return balance;
    }

    public void addProvince(Province p) {
        provinces.add(p);
        totalWealth += p.getWealth();
        provincesTracker.addProvince(p);
        notifyObservers();
    }

    public void removeProvince(Province p) {
        provinces.remove(p);
        totalWealth -= p.getWealth();
        notifyObservers();
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

    public void setFactionsTracker(FactionsTracker factionsTracker) {
        this.factionsTracker = factionsTracker;
    }

    public void setProvincesTracker(ProvincesTracker provincesTracker) {
        this.provincesTracker = provincesTracker;
    }

    public JSONObject toJSON() {
        JSONObject output = new JSONObject();
        output.put("name", name);
        output.put("balance", balance);
        output.put("totalWealth", totalWealth);
        output.put("is_win", is_win);
        JSONArray provincesJSON = new JSONArray();
        for (Province p : provinces) {
            provincesJSON.put(p.toJSON());
        }
        output.put("provinces", provincesJSON);

        output.put("provincesTracker", provincesTracker.toJSON());
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
        notifyObservers();
    }

    public GameSystem getGamesys() {
        return gamesys;
    }

    public void setGamesys(GameSystem gamesys) {
        this.gamesys = gamesys;
    }

    @Override
	public void notifyObservers() {
		this.gamesys.update(this);
    }

    public void setIs_win(boolean is_win) {
        this.is_win = is_win;
    }

    public boolean isIs_win() {
        return is_win;
    }
}
