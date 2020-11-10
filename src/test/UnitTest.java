package test;

import static org.junit.Assert.assertSame;
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
        assertEquals(newGame.getProvinces().size(), 53);
        assertEquals(newGame.getFactions().size(), 4);
    }

    @Test
    public void GameStartTest2(){
        GameSystem newGame = new GameSystem();
        assertTrue(newGame.setPlayerNum(16));
        newGame.allocateFaction();
        assertEquals(newGame.getPlayerNum(), 16);
        assertEquals(newGame.getProvinces().size(), 53);
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
        assertTrue(newGame.VictoryCheck(newGame.getVictoryCondition(), 53, 0, 0));
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
        assertEquals(newGame.VictoryCheck(newGame.getVictoryCondition(), 53, 200000, 0), true);
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
        assertEquals(newGame.VictoryCheck(newGame.getVictoryCondition(), 53, 99999, 0), false);
        assertEquals(newGame.VictoryCheck(newGame.getVictoryCondition(), 53, 0, 4399999), true);
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
        assertEquals(newGame.VictoryCheck(newGame.getVictoryCondition(), 53, 99999, 0), false);
        assertEquals(newGame.VictoryCheck(newGame.getVictoryCondition(), 53, 0, 4399999), false);
        assertEquals(newGame.VictoryCheck(newGame.getVictoryCondition(), 17, 200000, 400001), false);
        assertEquals(newGame.VictoryCheck(newGame.getVictoryCondition(), 53, 200000, 400001), true);
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
        assertEquals(newGame.VictoryCheck(newGame.getVictoryCondition(), 53, 0, 0), true);
        assertEquals(newGame.VictoryCheck(newGame.getVictoryCondition(), 51, 200000, 0), false);
        assertEquals(newGame.VictoryCheck(newGame.getVictoryCondition(), 53, 99999, 0), true);
        assertEquals(newGame.VictoryCheck(newGame.getVictoryCondition(), 53, 0, 4399999), true);
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
            
            assertEquals(u1.getFactionName(), "Romans");
            assertEquals(u1.getProvinceName(), "Aegyptus");
    
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
            assertEquals(melee2.getShield(), 5);
            assertEquals(melee2.getArmor(), 5);
            assertEquals(melee2.getTotalDefense(), 15);
            assertEquals(melee1.getAttackDamage(), 50);
            melee1.setAttackDamage(100);
            assertEquals(melee1.getAttackDamage(), 100);
            melee1.setAttackDamage(50);


            melee1.attack(melee2, 2);
            assertEquals(melee2.getHealth(), 30);

            melee1.attack(melee2, 20);
            assertEquals(melee2.getHealth(), 25);

            melee1.attack(melee2, 50);
            assertEquals(melee2.getHealth(), 0);            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void engagementTest() {
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
            Unit melee3 = new Unit(unitData2, null);
            Unit melee4 = new Unit(unitData2, null);


            Unit missile1 = new Unit(unitData3, null);
            Unit missile2 = new Unit(unitData4, null);
            Unit missile3 = new Unit(unitData4, null);
            Unit missile4 = new Unit(unitData4, null);



            Engagement e1 = new Engagement(melee1, melee2);
            assertEquals(e1.getType(), "melee");

            Engagement e2 = new Engagement(missile1, missile2);
            assertEquals(e2.getType(), "missile");

            Engagement e3 = new Engagement(melee3, missile3);
            assertEquals(e3.getAttacker().getName(), "Germanic berserker");
            assertEquals(e3.getDefender().getName(), "missile infantry");

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

            assertEquals(units1.size(), 3);
            Army army1 = p1.generateArmy(units1);
            assertEquals(p2.getFactionName(), "Carthaginians");
            assertEquals(p2.getUnits().size(), 3);

            assertEquals(army1.getUnits().size(), 3);
            assertTrue(army1.getUnits().contains(u1));
            assertTrue(army1.getUnits().contains(u3));
            assertTrue(army1.getUnits().contains(u5));
            assertFalse(army1.getUnits().contains(u2));

            army1.invade(p2);
            assertTrue(army1.containAvalUnits());
        } catch (IOException e) {
            assertEquals(1, 2);
            e.printStackTrace();
        }
    }

    @Test
    public void isReachableTest() {
        try {
            // create 2 factions
            GameSystem game = new GameSystem();
            Faction f1 = new Faction("Romans", game.getProvincesTracker(), game.getFactionsTracker());
            f1.setGamesys(game);
            Province p1 = new Province("Aegyptus", f1, game.getTurnTracker());
            Province p2 = new Province("Arabia", f1, game.getTurnTracker());

            String content = Files.readString(Paths.get("src/unsw/gloriaromanus/backend/UnitsInfo.json"));
            JSONObject fullJSON = new JSONObject(content);
            JSONObject unitData1 = fullJSON.getJSONObject("Roman legionary");
            JSONObject unitData2 = fullJSON.getJSONObject("Germanic berserker");
            JSONObject unitData3 = fullJSON.getJSONObject("Celtic Briton berserker");

            Unit u1 = new Unit(unitData1, p1);
            Unit u2 = new Unit(unitData2, p2);
            Unit u3 = new Unit(unitData3, p1);

            p1.addUnit(u1);
            p1.addUnit(u2);
            p1.addUnit(u3);

            List<Unit> units1 = new ArrayList<Unit>();
            units1.add(u1);
            units1.add(u2);
            units1.add(u3);

            assertEquals(units1.size(), 3);
            Army army1 = p1.generateArmy(units1);

            assertEquals(army1.isReachable(p2), true);
        } catch (IOException e) {
            assertEquals(1, 2);
            e.printStackTrace();
        }

    }

    @Test
    public void isReachableTest2() {
        try {
            // create 2 factions
            GameSystem game = new GameSystem();
            Faction f1 = new Faction("Romans", game.getProvincesTracker(), game.getFactionsTracker());
            f1.setGamesys(game);
            Province p1 = new Province("Aegyptus", f1, game.getTurnTracker());
            Province p2 = new Province("Arabia", f1, game.getTurnTracker());
            Province p3 = new Province("Creta et Cyrene", f1, game.getTurnTracker());
            Province p4 = new Province("Africa Proconsularis", f1, game.getTurnTracker());
            Province p5 = new Province("Numidia", null, game.getTurnTracker()); 
                 

            String content = Files.readString(Paths.get("src/unsw/gloriaromanus/backend/UnitsInfo.json"));
            JSONObject fullJSON = new JSONObject(content);
            JSONObject unitData1 = fullJSON.getJSONObject("Roman legionary");
            JSONObject unitData2 = fullJSON.getJSONObject("Germanic berserker");
            JSONObject unitData3 = fullJSON.getJSONObject("Celtic Briton berserker");

            Unit u1 = new Unit(unitData1, p1);
            Unit u2 = new Unit(unitData2, p1);
            Unit u3 = new Unit(unitData3, p1);

            p1.addUnit(u1);
            p1.addUnit(u2);
            p1.addUnit(u3);

            List<Unit> units1 = new ArrayList<Unit>();
            units1.add(u1);
            units1.add(u2);
            units1.add(u3);

            assertEquals(units1.size(), 3);
            Army army1 = p1.generateArmy(units1);

            assertEquals(army1.isReachable(p2), true);
            assertEquals(army1.isReachable(p4), true);
            assertEquals(army1.isReachable(p5), true);

            Faction f2 = new Faction("Romans2", game.getProvincesTracker(), game.getFactionsTracker());
            f2.setGamesys(game);
            f2.addProvince(p5);  

            assertEquals(army1.isReachable(p5), true);

            army1.moveToOccupied(p2);
            assertEquals(p2.getUnits().size(), 3);

        } catch (IOException e) {
            assertEquals(1, 2);
            e.printStackTrace();
        }

    }

    @Test
    public void unitSaveLoadTest() {
        try {
            GameSystem game = new GameSystem();
            String content = Files.readString(Paths.get("src/unsw/gloriaromanus/backend/UnitsInfo.json"));
            JSONObject fullJSON = new JSONObject(content);
            JSONObject unitData1 = fullJSON.getJSONObject("Roman legionary");

            Unit melee1 = new Unit(unitData1, null);
            JSONObject json = melee1.toJSON();

            Unit melee2 = new Unit(json);
            assertEquals(melee1.getName(), melee2.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void specialAbilityTest() {
        try {
            GameSystem game = new GameSystem();
            Faction f1 = new Faction("Romans", game.getProvincesTracker(), game.getFactionsTracker());
            f1.setGamesys(game);
            Province p1 = new Province("Aegyptus", f1, game.getTurnTracker());
            Province p2 = new Province("Arabia", f1, game.getTurnTracker());
            Province p3 = new Province("Creta et Cyrene", f1, game.getTurnTracker());
            Province p4 = new Province("Africa Proconsularis", f1, game.getTurnTracker());
            Province p5 = new Province("Numidia", null, game.getTurnTracker()); 
                 

            String content = Files.readString(Paths.get("src/unsw/gloriaromanus/backend/UnitsInfo.json"));
            JSONObject fullJSON = new JSONObject(content);
            JSONObject unitData1 = fullJSON.getJSONObject("Roman legionary");
            JSONObject unitData2 = fullJSON.getJSONObject("Germanic berserker");
            JSONObject unitData3 = fullJSON.getJSONObject("Celtic Briton berserker");
            JSONObject unitData4 = fullJSON.getJSONObject("horse-archer");

            Unit u1 = new Unit(unitData1, p1);
            Unit u2 = new Unit(unitData2, p2);
            Unit u3 = new Unit(unitData3, p1);
            Unit u4 = new Unit(unitData4, p1);
            p1.addUnit(u1);
            p2.addUnit(u2);
            p1.addUnit(u3);
            p1.addUnit(u4);


            assertEquals(u1.getSpecialAbilityName(), "Legionary eagle");
            assertEquals(u2.getSpecialAbilityName(), "Berserker rage");
            assertEquals(u3.getSpecialAbilityName(), "Berserker rage");
            assertEquals(u4.getSpecialAbilityName(),"Cantabrian circle");

            assertEquals(u1.getProvince().getUnits().size(), 3);
            // legionionary eagle test
            assertEquals(u1.getMorale(), 10);
            assertEquals(u3.getMorale(), 6);
            assertEquals(u4.getMorale(), 7);
            u1.useAbility(null);
            assertEquals(u1.getMorale(), 11);
            assertEquals(u3.getMorale(), 7);
            assertEquals(u4.getMorale(), 8);

            // Berserker rage test
            assertEquals(u2.getMorale(), 6);
            assertEquals(u2.getAttackDamage(), 25);
            u2.useAbility(null);
            assertEquals(u2.getMorale(), 9999);
            assertEquals(u2.getArmor(), 0);
            assertEquals(u2.getShield(), 0);
            assertEquals(u2.getAttackDamage(), 50);

            // Heroic charge test
            JSONObject unitData5 = fullJSON.getJSONObject("melee cavalry");
            Unit u5 = new Unit(unitData5, p1);
            assertEquals(u5.getAttackDamage(), 10);
            assertEquals(u5.getMorale(), 3);
            u5.useAbility(null);
            assertEquals(u5.getAttackDamage(), 20);
            assertEquals(u5.getMorale(), 4);

            // Phalanx test
            JSONObject unitData6 = fullJSON.getJSONObject("pikemen");
            Unit u6 = new Unit(unitData6, p2);
            assertEquals(u6.getDefense(), 20);
            assertEquals(u6.getMovementPoints(), 10);
            u6.useAbility(null);
            assertEquals(u6.getDefense(), 40);
            assertEquals(u6.getMovementPoints(), 5);

            // Elephants running amok
            JSONObject unitData7 = fullJSON.getJSONObject("elephant");
            Unit u7 = new Unit(unitData7, p2);
            assertEquals(u7.getAttackDamage(), 10);
            assertEquals(u6.getHealth(), 50);
            assertEquals(u2.getHealth(), 30);
            p2.addUnit(u7);
            p2.addUnit(u6);
            
            for (int i = 0; i < 100; i++) {
                u2.setHealth(30);
                u6.setHealth(50);
                u7.useAbility(null);
                if (u6.getHealth() == 40) {
                    assertEquals(u2.getHealth(), 20);
                } else {
                    assertEquals(u2.getHealth(), 30);
                }
            }

            // Cantabrian circle test

            Unit horseArcher = new Unit(fullJSON.getJSONObject("horse-archer"), p2);
            horseArcher.setHealth(30);
            u6.setHealth(50);
            u6.setAttackDamage(20);
            assertEquals(horseArcher.getSpecialAbilityName(), "Cantabrian circle");
            assertEquals(u6.getAttackDamage(), 20);
            horseArcher.useAbility(u6);
            assertEquals(u6.getAttackDamage(), 10);
            u6.setAttackDamage(15);
            

            // Druidic fervour test
            JSONObject unitData8 = fullJSON.getJSONObject("druid");
            Unit u8 = new Unit(unitData8, p2);
            p2.addUnit(u8);
            // 6 7 2 8
            u6.setMorale(40);
            u7.setMorale(20);
            u2.setMorale(70);
            u8.setMorale(50);
            assertEquals(u6.getMorale(), 40);
            assertEquals(u7.getMorale(), 20);
            assertEquals(u2.getMorale(), 70);
            assertEquals(u8.getMorale(), 50);
            u8.useAbility(null);
            assertEquals(u6.getMorale(), 44);
            assertEquals(u7.getMorale(), 22);
            assertEquals(u2.getMorale(), 77);
            assertEquals(u8.getMorale(), 55);

            // Shield charge
            // melee infantry
            JSONObject unitData9 = fullJSON.getJSONObject("melee infantry");
            Unit u9 = new Unit(unitData9, p2);
            assertEquals(u9.getAttackDamage(), 30);
            assertEquals(u9.getDefense(), 15);
            u9.useAbility(null);
            assertEquals(u9.getAttackDamage(), 45);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

