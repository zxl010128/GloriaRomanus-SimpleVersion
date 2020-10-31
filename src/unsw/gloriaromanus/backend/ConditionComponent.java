package unsw.gloriaromanus.backend;

import org.json.*;
import java.util.ArrayList;

public class ConditionComponent implements VictoryCondition {
    
    // backend should limit the subgoals which only can have 2 goal inside
    private JSONObject condition;
    private ArrayList<VictoryCondition> subVictoryConditions;
    private JSONArray subgoals = new JSONArray();

    public ConditionComponent(String condition) {
        super();
        this.condition = new JSONObject();
        this.condition.put("goal", condition);
        this.subVictoryConditions = new ArrayList<VictoryCondition>();
    }
    
    public boolean add(VictoryCondition v) {
        if (subgoals.length() >= 2) {
            return false;
        }
        subVictoryConditions.add(v);
        this.subgoals.put(v.getVictoryGoal());
		return true;
	}

    @Override
    public JSONObject getVictoryGoal(){
        this.condition.put("subgoals", this.subgoals);
        return condition;
    }
}
