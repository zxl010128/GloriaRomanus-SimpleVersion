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
import java.util.ArrayList;
import java.util.List;

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
        assertTrue(newGame.setPlayerNum(16));
        newGame.allocateFaction();
        assertEquals(newGame.getPlayerNum(), 16);
        assertEquals(newGame.getProvinces().size(), 52);
        assertEquals(newGame.getFactions().size(), 16);
    }

    @Test
    public void playerTest(){
        GameSystem newGame = new GameSystem();
        assertTrue(newGame.setPlayerNum(4));
        assertFalse(newGame.setPlayerNum(1));
        assertTrue(newGame.setPlayerNum(16));
        assertFalse(newGame.setPlayerNum(17));
        assertEquals(newGame.getPlayerNum(), 16);
    }

    @Test
    public void VicrotyConditionTest1(){
        GameSystem newGame = new GameSystem();
        
        ConditionLeaf goal = new ConditionLeaf("CONQUEST");
        newGame.setVictoryCondtion(goal);
        assertTrue(newGame.VictoryCheck(newGame.getVictoryCondition(), 52, 0, 0));
        assertFalse(newGame.VictoryCheck(newGame.getVictoryCondition(), 51, 0, 0));

        ConditionLeaf goal1 = new ConditionLeaf("TREASURY");
        newGame.setVictoryCondtion(goal1);
        assertFalse(newGame.VictoryCheck(newGame.getVictoryCondition(), 51, 0, 0));
        assertTrue(newGame.VictoryCheck(newGame.getVictoryCondition(), 51, 200000, 0));
        
        ConditionLeaf goal2 = new ConditionLeaf("TREASURY");
        newGame.setVictoryCondtion(goal2);
        assertFalse(newGame.VictoryCheck(newGame.getVictoryCondition(), 51, 0, 35564));
        assertTrue(newGame.VictoryCheck(newGame.getVictoryCondition(), 51, 200000, 400001));
    }

    @Test
    public void VicrotyConditionTest2(){
        GameSystem newGame = new GameSystem();
        ConditionLeaf goal = new ConditionLeaf("CONQUEST");
        ConditionLeaf goal1 = new ConditionLeaf("TREASURY");
        ConditionComponent comGoal = new ConditionComponent("AND");
        comGoal.add(goal);
        comGoal.add(goal1);
        newGame.setVictoryCondtion(comGoal);
        assertEquals(newGame.VictoryCheck(newGame.getVictoryCondition(), 51, 0, 0), false);
        assertEquals(newGame.VictoryCheck(newGame.getVictoryCondition(), 51, 200000, 0), false);
        assertEquals(newGame.VictoryCheck(newGame.getVictoryCondition(), 52, 200000, 0), true);
    }

    @Test
    public void VicrotyConditionTest3(){
        GameSystem newGame = new GameSystem();
        ConditionLeaf goal = new ConditionLeaf("WEALTH");
        ConditionLeaf goal1 = new ConditionLeaf("TREASURY");
        ConditionComponent comGoal = new ConditionComponent("OR");
        comGoal.add(goal);
        comGoal.add(goal1);
        newGame.setVictoryCondtion(comGoal);
        assertEquals(newGame.VictoryCheck(newGame.getVictoryCondition(), 51, 0, 0), false);
        assertEquals(newGame.VictoryCheck(newGame.getVictoryCondition(), 51, 200000, 0), true);
        assertEquals(newGame.VictoryCheck(newGame.getVictoryCondition(), 52, 99999, 0), false);
        assertEquals(newGame.VictoryCheck(newGame.getVictoryCondition(), 52, 0, 4399999), true);
        assertEquals(newGame.VictoryCheck(newGame.getVictoryCondition(), 17, 200000, 400001), true);
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
        newGame.setVictoryCondtion(comGoal1);
        assertEquals(newGame.VictoryCheck(newGame.getVictoryCondition(), 51, 0, 0), false);
        assertEquals(newGame.VictoryCheck(newGame.getVictoryCondition(), 51, 200000, 0), false);
        assertEquals(newGame.VictoryCheck(newGame.getVictoryCondition(), 52, 99999, 0), false);
        assertEquals(newGame.VictoryCheck(newGame.getVictoryCondition(), 52, 0, 4399999), false);
        assertEquals(newGame.VictoryCheck(newGame.getVictoryCondition(), 17, 200000, 400001), false);
        assertEquals(newGame.VictoryCheck(newGame.getVictoryCondition(), 52, 200000, 400001), true);
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
        newGame.setVictoryCondtion(comGoal1);
        assertEquals(newGame.VictoryCheck(newGame.getVictoryCondition(), 52, 0, 0), true);
        assertEquals(newGame.VictoryCheck(newGame.getVictoryCondition(), 51, 200000, 0), false);
        assertEquals(newGame.VictoryCheck(newGame.getVictoryCondition(), 52, 99999, 0), true);
        assertEquals(newGame.VictoryCheck(newGame.getVictoryCondition(), 52, 0, 4399999), true);
        assertEquals(newGame.VictoryCheck(newGame.getVictoryCondition(), 17, 200000, 400001), true);
    }

    @Test
    public void VicrotyConditionTest6(){
        ConditionLeaf goal = new ConditionLeaf("WEALTH");
        ConditionLeaf goal1 = new ConditionLeaf("TREASURY");
        ConditionLeaf goal2 = new ConditionLeaf("CONQUEST");
        ConditionComponent comGoal = new ConditionComponent("AND");
        comGoal.add(goal);
        comGoal.add(goal1);
        assertFalse(comGoal.add(goal2)); 
    }


    @Test
    public void SaveGame(){
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
        newGame.setVictoryCondtion(comGoal1);
        newGame.setPlayerNum(5);
        newGame.allocateFaction();

        newGame.saveCurrentGame();
        GameSystem newGame1 = new GameSystem();
        newGame1.reloadSavedGame();

        assertEquals(newGame.equals(newGame1), true);
    }

    @Test
    public void FactionRemove(){
        GameSystem newGame = new GameSystem();
        assertTrue(newGame.setPlayerNum(4));
        newGame.allocateFaction();

        Faction newF = newGame.getFactions().get(0);
        List<Province> provinces= newF.getProvinces();
        if (provinces.size() == 3) {
            newF.removeProvince(provinces.get(0));
            newF.removeProvince(provinces.get(0));
            newF.removeProvince(provinces.get(0));
        } else if (provinces.size() == 4) {
            newF.removeProvince(provinces.get(0));
            newF.removeProvince(provinces.get(0));
            newF.removeProvince(provinces.get(0));
            newF.removeProvince(provinces.get(0));
        }
        assertEquals(newGame.getFactions().size(), 3);
    }

    @Test
    public void FactionSYS(){
        GameSystem newGame = new GameSystem();
        assertTrue(newGame.setPlayerNum(4));
        newGame.allocateFaction();

        for(Faction faction: newGame.getFactions()) {
            assertTrue(faction.getGamesys().equals(newGame));
        }
    }

    @Test
    public void Victory(){
        GameSystem newGame = new GameSystem();
        ConditionLeaf goal = new ConditionLeaf("CONQUEST");
        newGame.setVictoryCondtion(goal);
        assertTrue(newGame.setPlayerNum(2));
        newGame.allocateFaction();

        Faction faction = newGame.getFactions().get(0);
        for(Province province: newGame.getProvinces()) {
            if (!faction.getProvinces().contains(province)) {
                faction.addProvince(province);
            }
        }
        assertTrue(faction.isIs_win());

    }

    @Test
    public void testGameSysToString(){
        GameSystem newGame = new GameSystem();
        
        assertTrue(newGame.toString() instanceof String);

    }

    @Test
    public void recruitTest() {
        try {
            GameSystem game = new GameSystem();
            Faction f1 = new Faction("Romans", game.getProvincesTracker(), game.getFactionsTracker());
            f1.setGamesys(game);
            Province p1 = new Province("Aegyptus", f1, game.getTurnTracker());
            Province p2 = new Province("Iudaea", f1, game.getTurnTracker());

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

            p2.setUnits(p1.getUnits());
            assertEquals(p2.getUnits().size(), 3);
            assertEquals(p2.getNumOfSoldiers(), 15);

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
        f1.setGamesys(game);
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

        p2.setTaxRate(0.25);

        game.NextTurn();
        assertEquals(p2.getWealth(), 60);

        game.NextTurn();
        assertEquals(p2.getWealth(), 30);

        game.NextTurn();
        assertEquals(p2.getWealth(), 0);

        game.NextTurn();
        assertEquals(p2.getWealth(), 0);
    }

    @Test
    public void unitAttackTest(){
        try {
            GameSystem game = new GameSystem();
            String content = Files.readString(Paths.get("src/unsw/gloriaromanus/backend/UnitsInfo.json"));
            JSONObject fullJSON = new JSONObject(content);
            JSONObject unitData1 = fullJSON.getJSONObject("Roman legionary");
            JSONObject unitData2 = fullJSON.getJSONObject("Germanic berserker");
            JSONObject unitData3 = fullJSON.getJSONObject("horse-archer");
            JSONObject unitData4 = fullJSON.getJSONObject("missile infantry");

            Unit melee1 = new Unit(unitData1, null);
            Unit melee2 = new Unit(unitData2, null);
            Unit missile3 = new Unit(unitData3, null);
            Unit missile4 = new Unit(unitData4, null);

            assertEquals(melee2.getHealth(), 30);
            assertEquals(melee2.getDefense(), 5);
            assertEquals(melee2.getTotalDefense(), 15);
            assertEquals(melee1.getAttackDamage(), 50);
            melee1.attack(melee2, 20);
            assertEquals(melee2.getHealth(), 25);

            melee1.attack(melee2, 50);
            assertEquals(melee2.getHealth(), 0);



            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void battleTest() {
        try {
            // create 2 factions
            GameSystem game = new GameSystem();
            Faction f1 = new Faction("Romans", game.getProvincesTracker(), game.getFactionsTracker());
            Faction f2 = new Faction("Carthaginians", game.getProvincesTracker(), game.getFactionsTracker());
            f1.setGamesys(game);
            f2.setGamesys(game);
            Province p1 = new Province("Aegyptus", f1, game.getTurnTracker());
            Province p2 = new Province("Africa Proconsularis", f2, game.getTurnTracker());

            String content = Files.readString(Paths.get("src/unsw/gloriaromanus/backend/UnitsInfo.json"));
            JSONObject fullJSON = new JSONObject(content);
            JSONObject unitData1 = fullJSON.getJSONObject("Roman legionary");
            JSONObject unitData2 = fullJSON.getJSONObject("Germanic berserker");
            JSONObject unitData3 = fullJSON.getJSONObject("Celtic Briton berserker");
            JSONObject unitData4 = fullJSON.getJSONObject("druid");
            JSONObject unitData5 = fullJSON.getJSONObject("melee infantry");
            JSONObject unitData6 = fullJSON.getJSONObject("chariots");

            Unit u1 = new Unit(unitData1, p1);
            Unit u2 = new Unit(unitData2, p2);
            Unit u3 = new Unit(unitData3, p1);
            Unit u4 = new Unit(unitData4, p2);
            Unit u5 = new Unit(unitData5, p1);
            Unit u6 = new Unit(unitData6, p2);
            
            p1.addUnit(u1);
            p1.addUnit(u3);
            p1.addUnit(u5);
            p2.addUnit(u2);
            p2.addUnit(u4);
            p2.addUnit(u6);

            List<Unit> units1 = new ArrayList<Unit>();
            units1.add(u1);
            units1.add(u3);
            units1.add(u5);

            // List<Unit> units2 = new ArrayList<Unit>();
            // units2.add(u2);
            // units2.add(u4);
            // units2.add(u6);
            assertEquals(units1.size(), 3);
            Army army1 = p1.generateArmy(units1);
            assertEquals(p2.getFactionName(), "Carthaginians");
            assertEquals(p2.getUnits().size(), 3);

            assertEquals(army1.getUnits().size(), 3);
            assertTrue(army1.getUnits().contains(u1));
            assertTrue(army1.getUnits().contains(u3));
            assertTrue(army1.getUnits().contains(u5));
            assertFalse(army1.getUnits().contains(u2));

            // army1.invade(p2);
            // CHANGE LATER
            // assertEquals(p2.getFactionName(), "Romans");
            // assertEquals(army1.getProvince().getName(), "Africa Proconsularis");
            // assertTrue(army1.containAvalUnits());
        } catch (IOException e) {
            assertEquals(1, 2);
            e.printStackTrace();
        }


    }
}

