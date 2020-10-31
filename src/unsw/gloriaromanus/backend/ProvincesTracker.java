package unsw.gloriaromanus.backend;

import java.util.List;

import org.json.JSONObject;

import java.util.ArrayList;

public class ProvincesTracker {
    private List<Province> provinces; 

    public ProvincesTracker(){
        provinces = new ArrayList<Province>();

    }

    public void addProvince(Province p) {
        if (!provinces.contains(p)) {
            provinces.add(p);
        }
    }

    

    public void removeProvince(Province p) {
        provinces.remove(p);
    }

    

    public List<Province> getProvinces() {
        return provinces;
    }

    

    public Province getProvince(String provinceName) {
        for (Province p : provinces) {
            if (p.getName().equals(provinceName)) {
                return p;
            }
        }

        return null;
    }

    

    public JSONObject toJSON() {
        JSONObject output = new JSONObject();
        List<JSONObject> provincesJSON = new ArrayList<JSONObject>();
        // List<JSONObject> factionsJSON = new ArrayList<JSONObject>();
        for (Province p : provinces) {
            provincesJSON.add(p.toJSON());
        }

        // for (Faction f : factions) {
        //     factionsJSON.add(f.toJSON());
        // }

        output.put("provinces", provincesJSON);
        // output.put("factions", factionsJSON);

        return output;
    }


}
