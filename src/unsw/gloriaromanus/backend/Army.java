package unsw.gloriaromanus.backend;

import java.util.ArrayList;
import java.util.List;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.*;

import org.json.*;

public class Army {
    private List<Unit> units;
    private int movementPoints;
    private String provinceName;
    private String factionName;
    private ProvincesTracker provincesTracker;
    private FactionsTracker factionsTracker;

    public Army(Province province) {
        this.units = new ArrayList<Unit>();
        this.movementPoints = 0;
        this.provinceName = province.getName();
        this.factionName = province.getFactionName();
        this.provincesTracker = province.getProvincesTracker();
        this.factionsTracker = province.getFactionsTracker();
    }

    public void setMovementPoint() {
        int minMovementPoints = units.get(0).getMovementPoints();
        for (Unit s : units) {
            // this.units.add(s);
            if (s.getMovementPoints() < minMovementPoints) {
                minMovementPoints = s.getMovementPoints();
            }
        }
        this.movementPoints = minMovementPoints;
    }

    public Province getProvince() {
        return provincesTracker.getProvince(provinceName);
    }

    public String getFactionName() {
        return factionName;
    }

    public Faction getFaction() {
        return factionsTracker.getFaction(factionName);
    }

    /**
     * assign an army to an occupied province
     * 
     * @param destination player's occupied province
     */
    public void moveToOccupied(Province destination) {
        // implement later
        // Don't forget to update oldProvince after moving!
        // check if the destination is okay to move to

        // move

        // update after moving
        for (Unit u : units) {
            destination.addUnit(u);
            provincesTracker.getProvince(provinceName).removeUnit(u);
        }
    }

    /**
     * assign an army to invade a unoccupied province
     */
    public int invade(Province destination) {
        // check if the destination is able to move to

        // move

        // after arrivingm, start battle
        // Don't forget to update oldProvince after moving!
        Army defenseArmy = new Army(destination);
        Battle battleResolver = new Battle(this, defenseArmy);
        Army winner = battleResolver.getWinner();
        if (winner == null) {
            // Draw
            this.updateAfterDraw();
            return 0;

        } else if (winner.getFactionName().equals(this.getFactionName())) {
            // Win
            this.updateAfterWin(destination);
            return 1;

        } else {
            // Lose
            this.updateAfterLose();
            return -1;

        }
    
    }

    
    /** 
     * BFS to check the shortest path
     * @return boolean
     */
    public boolean isReachable(Province destination){
        
        // read a json file and check the adjecent
        try {

            String content = Files.readString(Paths.get("src/unsw/gloriaromanus/province_adjacency_matrix_fully_connected.json"));
            JSONObject Matrix = new JSONObject(content);
            String provinces = Files.readString(Paths.get("src/unsw/gloriaromanus/backend/provinces_list.json"));
            JSONArray proList = new JSONArray(provinces);

            List<String> province_list = new ArrayList<String>();
            for(int i = 0; i < proList.length(); i++) {
                province_list.add(proList.getString(i));
            }

            String startPoint = this.getProvince().getName();  

            // situation when current = dest
            if (startPoint == destination.getName()) {
                return true;
            }

            String temp_city;

            // if visited, it will show 1 of this place ID
            Map<String, Integer> visited = new HashMap<String, Integer>();

            for(String province: province_list){
                visited.put(province, 0);
            }

            // this array will record the connected place ID

            Map<String, String> last_position = new HashMap<String, String>();

            for(String province: province_list){
                last_position.put(province, "-1");
            }

            // Queue created
            Queue<String> q = new LinkedList<>();
            q.add(startPoint);

            // check whether the dest is founded
            boolean is_Found = false;
            
            List<String> OccupiedProvince = new ArrayList<String>();
            
            for(Province province : this.getFaction().getProvinces()) {
                OccupiedProvince.add(province.getName());
            }

            while (!q.isEmpty() && !is_Found) {
	
                temp_city = q.remove();
        
                // if we have visited this city, we need to skip it
                // there should be the situation the place connect more than once
                if (visited.get(temp_city) == 1) {
                    continue;
                }
        
                // set visited of this city
                visited.put(temp_city, 1);
        
                // get all the reachable locations
                int num_locs = 0;
                JSONObject connectList = Matrix.getJSONObject(temp_city);


                List<String> TotalConnectedProvince = new ArrayList<String>();

                for(String key : connectList.keySet()) {
                    if(connectList.get(key).equals(true)){
                        TotalConnectedProvince.add(key);
                    }
                }

                List<String> ConnectedProvince = new ArrayList<String>();

                for(String province: TotalConnectedProvince){
                    if (!destination.getName().equals(province) && !OccupiedProvince.contains(province)) {
                        continue;
                    } else {
                        ConnectedProvince.add(province);
                        num_locs += 1;
                    }
                }

                // join the reachable place to Queue
                for (int i = 0; i < num_locs; i++) {
                    
                    // the place must be non-visted
                    if (visited.get(ConnectedProvince.get(i)) == 0 && last_position.get(ConnectedProvince.get(i)) == "-1") {
                        
                        last_position.put(ConnectedProvince.get(i), temp_city);
                        // if this location is the dest, break the loop
                        if (destination.getName().equals(ConnectedProvince.get(i))) {
                            is_Found = true;
                            break;
                        }
        
                        // Join this place to the Queue
                        q.add(ConnectedProvince.get(i));

                    }
        
                }
        
            }

            q.clear();

           	// situation when there is no dest in the map
            if (is_Found == false) {
                return false;
            }

            // count the length of the path
            int Path_length = 0;
            for (String position = destination.getName(); position != startPoint; position = last_position.get(position)) Path_length++;
            
            if (Path_length > this.movementPoints) {
                return false;
            }

            return true;
        
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return false;
    }

    
    /** 
     * @return List<Unit>
     */
    public List<Unit> getUnits() {
        return units;
    }

    public void setUnits(List<Unit> units) {
        this.units = units;
    }

    
    /** 
     * @return int
     */
    public int getNumOfUnits() {
        return units.size();
    }

    
    /** 
     * @return boolean
     */
    public boolean containAvalUnits(){
        for (Unit u : units) {
            if (u.getHealth() > 0) {
                return true;
            }
        }

        return false;
    }
    
    public void updateAfterDraw() {
        // moveTo(getProvince());
    }


    
    /** 
     * @param destination
     */
    public void updateAfterWin(Province destination) {
        // moveTo(destination);
        
        // update the occupied province
        destination.setFaction(this.getFaction());
        destination.setUnits(this.getUnits());
    }

    public void updateAfterLose() {
        // do i need to remove unit here?
        // or the unit will be removed when they are defeated?
        // already implemented in Battle.updateAferBattle()
    }

    public int getMovementPoints() {
        return movementPoints;
    }

    public ProvincesTracker getProvincesTracker() {
        return provincesTracker;
    }

    public void setProvincesTracker(ProvincesTracker provincesTracker) {
        this.provincesTracker = provincesTracker;
    }

    
}
