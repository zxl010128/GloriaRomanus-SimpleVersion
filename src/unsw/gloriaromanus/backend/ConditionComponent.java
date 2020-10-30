package unsw.gloriaromanus.backend;

import org.json.*;

public class ConditionComponent implements VictoryCondition {
    
    private JSONObject condition;
    private JSONArray subgoals = new JSONArray();

    public ConditionComponent(String condition) {
        this.condition = new JSONObject();
        this.condition.put("goal", condition);

    }

    public void addSubgoals(JSONObject goal) {
        this.subgoals.put(goal);
    }

    public void formGoal(){
        this.condition.put("subgoals", this.subgoals);
    }
    
    @Override
    public JSONObject getVictoryGoal(){
        return condition;
    }
}
