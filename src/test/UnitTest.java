package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import unsw.gloriaromanus.backend.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.JSONArray;
import org.json.JSONObject;

public class UnitTest{
    @Test
    public void constructorTest(){
        try {
            String content = Files.readString(Paths.get("bin/unsw/gloriaromanus/backend/UnitsInfo.json"));
            JSONObject fullJSON = new JSONObject(content);
            JSONObject unitData = fullJSON.getJSONObject("Roman legionary");
            Unit u = new Unit(unitData, null);
            
            assertEquals(u.getName(), "Roman legionary");
            assertEquals(u.getType(), "melee");
            assertEquals(u.getMovementPoints(), 15);
            assertEquals(u.getHealth(), 100);
            assertEquals(u.getTrainingCost(), 30);
            assertEquals(u.getTrainingTurns(), 3);
            assertEquals(u.getMorale(), 10);
            assertEquals(u.getAttackDamage(), 50);
            assertEquals(u.getTotalDefense(), 25);
            assertEquals(u.getNumOfTroops(), 5);

            u.setName("JJ");
            assertEquals(u.getName(), "JJ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void GameStartTest1(){
        GameSystem newGame = new GameSystem();
        assertEquals(newGame.setPlayerNum(4), true);
        newGame.allocateFaction();
        assertEquals(newGame.getPlayerNum(), 4);
        assertEquals(newGame.getProvinces().size(), 52);
        assertEquals(newGame.getFactions().size(), 4);
    }

    @Test
    public void GameStartTest2(){
        GameSystem newGame = new GameSystem();
        assertEquals(newGame.setPlayerNum(16), true);
        newGame.allocateFaction();
        assertEquals(newGame.getPlayerNum(), 16);
        assertEquals(newGame.getProvinces().size(), 52);
        assertEquals(newGame.getFactions().size(), 16);
    }

    @Test
    public void playerTest(){
        GameSystem newGame = new GameSystem();
        assertEquals(newGame.setPlayerNum(4), true);
        assertEquals(newGame.setPlayerNum(1), false);
        assertEquals(newGame.setPlayerNum(16), true);
        assertEquals(newGame.setPlayerNum(17), false);
        assertEquals(newGame.getPlayerNum(), 16);
    }

    @Test
    public void VicrotyConditionTest1(){
        GameSystem newGame = new GameSystem();
        ConditionLeaf goal = new ConditionLeaf("CONQUEST");
        assertEquals(newGame.VictoryCheck(goal, 52, 0, 0), true);
        assertEquals(newGame.VictoryCheck(goal, 51, 0, 0), false);
        ConditionLeaf goal1 = new ConditionLeaf("TREASURY");
        assertEquals(newGame.VictoryCheck(goal1, 51, 0, 0), false);
        assertEquals(newGame.VictoryCheck(goal1, 51, 200000, 0), true);
        ConditionLeaf goal2 = new ConditionLeaf("TREASURY");
        assertEquals(newGame.VictoryCheck(goal2, 51, 0, 35564), false);
        assertEquals(newGame.VictoryCheck(goal2, 51, 200000, 400001), true);
    }

    @Test
    public void VicrotyConditionTest2(){
        GameSystem newGame = new GameSystem();
        ConditionLeaf goal = new ConditionLeaf("CONQUEST");
        ConditionLeaf goal1 = new ConditionLeaf("TREASURY");
        ConditionComponent comGoal = new ConditionComponent("AND");
        comGoal.add(goal);
        comGoal.add(goal1);
        assertEquals(newGame.VictoryCheck(comGoal, 51, 0, 0), false);
        assertEquals(newGame.VictoryCheck(comGoal, 51, 200000, 0), false);
        assertEquals(newGame.VictoryCheck(comGoal, 52, 200000, 0), true);
    }

    @Test
    public void VicrotyConditionTest3(){
        GameSystem newGame = new GameSystem();
        ConditionLeaf goal = new ConditionLeaf("WEALTH");
        ConditionLeaf goal1 = new ConditionLeaf("TREASURY");
        ConditionComponent comGoal = new ConditionComponent("OR");
        comGoal.add(goal);
        comGoal.add(goal1);
        assertEquals(newGame.VictoryCheck(comGoal, 51, 0, 0), false);
        assertEquals(newGame.VictoryCheck(comGoal, 51, 200000, 0), true);
        assertEquals(newGame.VictoryCheck(comGoal, 52, 99999, 0), false);
        assertEquals(newGame.VictoryCheck(comGoal, 52, 0, 4399999), true);
        assertEquals(newGame.VictoryCheck(comGoal, 17, 200000, 400001), true);
    }

    @Test
    public void VicrotyConditionTest4(){
        GameSystem newGame = new GameSystem();
        ConditionLeaf goal = new ConditionLeaf("WEALTH");
        ConditionLeaf goal1 = new ConditionLeaf("TREASURY");
        ConditionLeaf goal2 = new ConditionLeaf("CONQUEST");
        ConditionComponent comGoal = new ConditionComponent("AND");
        comGoal.add(goal);
        comGoal.add(goal1);
        ConditionComponent comGoal1 = new ConditionComponent("AND");
        comGoal1.add(comGoal);
        comGoal1.add(goal2);
        assertEquals(newGame.VictoryCheck(comGoal1, 51, 0, 0), false);
        assertEquals(newGame.VictoryCheck(comGoal1, 51, 200000, 0), false);
        assertEquals(newGame.VictoryCheck(comGoal1, 52, 99999, 0), false);
        assertEquals(newGame.VictoryCheck(comGoal1, 52, 0, 4399999), false);
        assertEquals(newGame.VictoryCheck(comGoal1, 17, 200000, 400001), false);
        assertEquals(newGame.VictoryCheck(comGoal1, 52, 200000, 400001), true);
    }

    @Test
    public void VicrotyConditionTest5(){
        GameSystem newGame = new GameSystem();
        ConditionLeaf goal = new ConditionLeaf("WEALTH");
        ConditionLeaf goal1 = new ConditionLeaf("TREASURY");
        ConditionLeaf goal2 = new ConditionLeaf("CONQUEST");
        ConditionComponent comGoal = new ConditionComponent("AND");
        comGoal.add(goal);
        comGoal.add(goal1);
        ConditionComponent comGoal1 = new ConditionComponent("OR");
        comGoal1.add(comGoal);
        comGoal1.add(goal2);
        assertEquals(newGame.VictoryCheck(comGoal1, 52, 0, 0), true);
        assertEquals(newGame.VictoryCheck(comGoal1, 51, 200000, 0), false);
        assertEquals(newGame.VictoryCheck(comGoal1, 52, 99999, 0), true);
        assertEquals(newGame.VictoryCheck(comGoal1, 52, 0, 4399999), true);
        assertEquals(newGame.VictoryCheck(comGoal1, 17, 200000, 400001), true);
    }

    @Test void RecruitTest() {
        
    }
}

