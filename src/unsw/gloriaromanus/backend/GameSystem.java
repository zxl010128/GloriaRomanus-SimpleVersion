package unsw.gloriaromanus.backend;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GameSystem {

    private ArrayList<Faction> factions;
    private int turn;
    private int year;
    private int playerNum;
    List<Faction> factions_list = new List<Faction>();

    public GameSystem() {
        this.factions = new ArrayList<Faction>();
        this.turn = 0;
        this.year = 200;
        this.playerNum = 0;
    }
    
    /** 
     * @param playerNum the number of player in this game
     * @return boolean
     */
    public boolean setPlayerNum(int playerNum) {
        
        // player number should be more than 2 and less than the total factions number
        if (playerNum > factions.size() || playerNum < 2) {
            return false;
        } else {
            this.playerNum = playerNum;
        }    
        
        return true;
    }

    
    /** 
     * check the saved file existence
     * @return boolean
     */
    public boolean isSavedFileExist(){
        String FileName = "DataSaved";
        File logFile = new File(FileName);

        if(!logFile.exists()) {
            return false;
        }
        return true;
    }

    
    /** 
     * @param game
     */
    public void saveCurrentGame(GameSystem game){
        // saved game
        
    }



    

    
}
