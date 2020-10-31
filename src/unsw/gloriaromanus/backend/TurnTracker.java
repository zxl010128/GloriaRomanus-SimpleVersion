package unsw.gloriaromanus.backend;

import com.google.gson.JsonObject;

import org.json.JSONObject;

public class TurnTracker {
    private int currTurn;
    
    public TurnTracker() {
        this.currTurn = 1;
    }

    public TurnTracker(JSONObject json) {
        this.currTurn = json.getInt("currTurn");
    }

    public void incrementTurn(){
        this.currTurn += 1;
    }

    public int getCurrTurn() {
        return currTurn;
    }

    public JSONObject toJSON() {
        JSONObject output = new JSONObject();
        output.put("currTurn", currTurn);

        return output;
    }
}
