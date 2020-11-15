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
    private boolean is_defeat;
    private JSONObject moneyLending;

    public Faction(String name, ProvincesTracker provincesTracker, FactionsTracker factionsTracker) {
        this.name = name;
        this.balance = 100;
        this.provinces = new ArrayList<Province>();
        this.totalWealth = 0;
        this.gamesys = null;
        this.is_win = false;
        this.is_defeat = false;
        this.provincesTracker = provincesTracker;
        this.factionsTracker = factionsTracker;
        factionsTracker.addFaction(this);

        JSONObject moneyLend = new JSONObject();

        moneyLend.put("MoneyAvaliable", 500);
        moneyLend.put("is_Lending", false);
        moneyLend.put("Owning", 0);
        moneyLend.put("RatePerYear", 0.10);
        this.moneyLending = moneyLend;
    }

    public Faction(String name, ArrayList<Province> startingProvinces, ProvincesTracker provincesTracker, FactionsTracker factionsTracker) {
        this.name = name;
        this.balance = 100;
        this.provinces = startingProvinces;
        this.totalWealth = 0;
        this.gamesys = null;
        this.is_win = false;
        this.is_defeat = false;
        this.provincesTracker = provincesTracker;
        this.factionsTracker = factionsTracker;
        for (Province p : provinces) {
            totalWealth += p.getWealth();
            this.provincesTracker.addProvince(p);
            p.setProvincesTracker(provincesTracker);
            p.getArmy().setProvincesTracker(provincesTracker);
            p.getArmy().setFactionsTracker(factionsTracker);
            p.getArmy().setFactionName(this.name);
            p.setFaction(this);
        }
        
        JSONObject moneyLend = new JSONObject();

        moneyLend.put("MoneyAvaliable", 500);
        moneyLend.put("is_Lending", false);
        moneyLend.put("Owning", 0);
        moneyLend.put("RatePerYear", 0.10);
        this.moneyLending = moneyLend;

        factionsTracker.addFaction(this);       
    }

    public Faction(JSONObject json) {
        this.name = json.getString("name");
        this.balance = json.getInt("balance");
        this.totalWealth = json.getInt("totalWealth");
        this.provinces = new ArrayList<Province>();
        this.gamesys = null;
        this.is_win = json.getBoolean("is_win");
        this.is_defeat = json.getBoolean("is_defeat");
        this.provincesTracker = null;
        this.factionsTracker = null;
        this.moneyLending = json.getJSONObject("moneyLending");
        this.provinces = new ArrayList<Province>();
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
        p.setFactionName(this.getName());
        if (!provincesTracker.getProvinces().contains(p)) {
            p.setFactionsTracker(factionsTracker);
            p.setProvincesTracker(provincesTracker);
            p.getArmy().setProvincesTracker(provincesTracker);
            provincesTracker.addProvince(p);
        }
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

    public Province getProvinceByName(String name) {
        for (Province p : provinces) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
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
        output.put("is_defeat", is_defeat);
        JSONArray provincesJSON = new JSONArray();
        for (Province p : provinces) {
            provincesJSON.put(p.toJSON());
        }
        output.put("provinces", provincesJSON);

        output.put("provincesTracker", provincesTracker.toJSON());
        output.put("moneyLending", moneyLending);
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

    public boolean isIs_defeat() {
        return is_defeat;
    }

    public void setIs_defeat(boolean is_defeat) {
        this.is_defeat = is_defeat;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void setTotalWealth(int totalWealth) {
        this.totalWealth = totalWealth;
    }

    public JSONObject getMoneyLending() {
        return moneyLending;
    }

    public void setMoneyLending(JSONObject moneyLending) {
        this.moneyLending = moneyLending;
    }

    
}
