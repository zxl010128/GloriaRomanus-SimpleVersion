package unsw.gloriaromanus.backend;
import java.util.List;

import org.json.JSONObject;

import java.util.ArrayList;

public class FactionsTracker {
    private List<Faction> factions;

    public FactionsTracker() {
        factions = new ArrayList<Faction>();
    }

    public void addFaction(Faction f) {
        if (!factions.contains(f)) {
            factions.add(f);
        }
    }

    public void removeFaction(Faction f) {
        factions.remove(f);
    }

    public List<Faction> getFactions() {
        return factions;
    }

    public Faction getFaction(String factionName) {
        for (Faction f : factions) {
            if (f.getName().equals(factionName)){
                return f;
            }
        }

        return null;
    }

    public void update(Faction faction, Province province) {
        for (Faction f : factions) {
            if (f.equals(faction)) {
                f.addProvince(province);
            }
        }
    }

    public JSONObject toJSON() {
        JSONObject output = new JSONObject();
        List<JSONObject> factionsJSON = new ArrayList<JSONObject>();

        for (Faction f : factions) {
            factionsJSON.add(f.toJSON());
        }

        output.put("factions", factionsJSON);

        return output;
    }
}
