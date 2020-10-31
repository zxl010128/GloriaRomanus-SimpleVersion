package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import unsw.gloriaromanus.backend.*;

public class UnitTest{
    @Test
    public void blahTest(){
        assertEquals("a", "a");
    }
    
    @Test
    public void blahTest2(){
        Unit u = new Unit();
        assertEquals(u.getNumTroops(), 50);
    }

    @Test
    public void GameStartTest3(){
        GameSystem newGame = new GameSystem();
        assertEquals(newGame.setPlayerNum(4), true);
        newGame.allocateFaction();
        assertEquals(newGame.getPlayerNum(), 4);
        assertEquals(newGame.getProvinces().size(), 52);
        assertEquals(newGame.getFactions().size(), 4);
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

}

