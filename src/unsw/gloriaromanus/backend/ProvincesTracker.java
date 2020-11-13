package unsw.gloriaromanus.backend;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProvincesTracker {
    private List<Province> provinces; 

    public ProvincesTracker(){
        this.provinces = new ArrayList<Province>();

    }

    /**
     * constructor when loading
     */
    public ProvincesTracker(JSONObject json) {
        JSONArray provincesJSON = json.getJSONArray("provinces");
        this.provinces = new ArrayList<Province>();

        for (int i = 0; i < provincesJSON.length(); i++) {
            provinces.add(new Province(provincesJSON.getJSONObject(i)));
        }
    }

    public void addProvince(Province p) {
        if (!provinces.contains(p)) {
            provinces.add(p);
            // p.setProvincesTracker(this);
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
        JSONArray provincesJSON = new JSONArray();
        for (Province p : provinces) {
            provincesJSON.put(p.toJSON());
        }
        output.put("provinces", provincesJSON);

        return output;
    }


}
