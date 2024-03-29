package unsw.gloriaromanus.backend;

import org.json.JSONObject;

public class ConditionLeaf implements VictoryCondition {
    
    private JSONObject condition;

    public ConditionLeaf(String condition) {
        super();
        this.condition = new JSONObject();
        this.condition.put("goal", condition);
    }
    
    @Override
    public JSONObject getVictoryGoal(){
        return condition;
    }

    // @Override
    // public String toString() {
    //     return condition.getString("goal");
    // }

}
