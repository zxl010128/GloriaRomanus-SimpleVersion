package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
            String content = Files.readString(Paths.get("src/unsw/gloriaromanus/backend/UnitsInfo.json"));
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void GameStartTest1(){
        GameSystem newGame = new GameSystem();
        assertTrue(newGame.setPlayerNum(4));
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

    @Test
    public void recruitTest() {
        try {
            GameSystem game = new GameSystem();
            Faction f1 = new Faction("Romans", game.getProvincesTracker(), game.getFactionsTracker());
            Province p1 = new Province("Aegyptus", f1, game.getTurnTracker());
            assertEquals(p1.getFactionName(), f1.getName());
            assertEquals(p1.getNumOfSoldiers(), 0);
            assertFalse(p1.recruit("Minnie Mouse", game.getTurnTracker().getCurrTurn()));
            assertTrue(p1.recruit("Gallic berserker", game.getTurnTracker().getCurrTurn()));
            assertTrue(p1.recruit("Roman legionary", game.getTurnTracker().getCurrTurn()));
            assertEquals(p1.getUnitsInTraining().get(0).getUnit().getName(), "Gallic berserker");
            assertEquals(p1.getUnitsInTraining().get(1).getUnit().getName(), "Roman legionary");
            assertFalse(p1.recruit("Celtic Briton berserker", game.getTurnTracker().getCurrTurn()));
            game.NextTurn();
            game.NextTurn();
            assertEquals(p1.getUnitsInTraining().size(), 0);
            assertEquals(p1.getUnits().size(), 2);

            String content = Files.readString(Paths.get("src/unsw/gloriaromanus/backend/UnitsInfo.json"));
            JSONObject fullJSON = new JSONObject(content);
            JSONObject unitData1 = fullJSON.getJSONObject("Germanic berserker");

            Unit u1 = new Unit(unitData1, null);
            p1.addUnit(u1);
            assertEquals(p1.getUnits().size(), 3);
            assertEquals(p1.getNumOfSoldiers(), 15);
            p1.removeUnit(u1);
            assertEquals(p1.getUnits().size(), 2);
            assertEquals(p1.getNumOfSoldiers(), 10);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void taxAndWealthTest() {
        GameSystem game = new GameSystem();
        Faction f1 = new Faction("Romans", game.getProvincesTracker(), game.getFactionsTracker());
        Province p1 = new Province("Aegyptus", f1, game.getTurnTracker());
        Province p2 = new Province("Arabia", null, game.getTurnTracker());
        p2.setFaction(f1);

        assertEquals(p1.getTaxRate(), 0.15);
        assertEquals(p1.getWealth(), 100);
        assertEquals(p2.getTaxRate(), 0.15);
        assertEquals(p2.getWealth(), 100);
        assertEquals(f1.getTotalWealth(), 200);

        p1.setTaxRate(0.1);
        p2.setTaxRate(0.2);

        game.NextTurn();

        assertEquals(p1.getWealth(), 110);
        assertEquals(p2.getWealth(), 90);
        assertEquals(f1.getTotalWealth(), 200);

    }
}

