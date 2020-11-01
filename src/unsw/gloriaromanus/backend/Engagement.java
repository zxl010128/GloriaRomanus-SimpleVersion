package unsw.gloriaromanus.backend;

import java.util.Random;

public class Engagement {
    private Unit attacker;
    private Unit defender;
    private String type;
    // private Unit winner;

    public Engagement(Unit attacker, Unit defender) {
        this.attacker = attacker;
        this.defender = defender;

        String attackerType = attacker.getType();
        String defenderType = defender.getType();

        if (attackerType.equals("melee") && defenderType.equals("melee")) {
            this.type = "melee";
        } else if (attackerType.equals("missile") && defenderType.equals("missile")) {
            this.type = "missile";
        } else {
            // assume there are only two kinds of unit
            // 1. melee 2. missile
            double meleeProb = 0.5;
            int meleeSpeed = (attackerType.equals("melee")) ? attacker.getMovementPoints() : defender.getMovementPoints();
            int missileSpeed = (defenderType.equals("missile")) ? defender.getMovementPoints() : attacker.getMovementPoints();

            meleeProb += 0.1 * (meleeSpeed - missileSpeed);
            // the maximum chance for an engagement to be either a ranged or melee engagement is 95% in either case.
            if (meleeProb > 0.95) {
                meleeProb = 0.95;
            }
            if (meleeProb < 0.05) {
                meleeProb = 0.05;
            }

            var d = Math.random();
            if (d < meleeProb) {
                this.type = "melee";
            } else {
                this.type = "missile";
            }
        }

        startBattle(attacker, defender);

    }

    public Unit getAttacker() {
        return attacker;
    }

    public Unit getDefender() {
        return defender;
    }

    public String getType() {
        return type;
    }

    private void startBattle(Unit attacker, Unit defender) {
        // assume attacker attacks first
        // Province defendProvince = defender.getProvince();
        // First, calculate the damage
        int damage = calculateDamage(attacker, defender);
        attacker.attack(defender, damage);

        // defender will be removed if defeated
        // check if it has been defeated
        if (defender.getHealth() > 0) {
            damage = calculateDamage(defender, attacker);
            defender.attack(attacker, damage);
        }

        
    }

    public int calculateDamage(Unit attacker, Unit defender) {
        int damage = 0;
        int enemySize = defender.getNumOfTroops();
        int enemyArmor = defender.getArmor();
        int enemyShield = defender.getShield();
        double N = new Random().nextGaussian() + 1;

        switch (attacker.getType()) {
            case "melee" : {
                if (this.type.equals("missile")) {
                    damage = 0;

                } else {
                    // both melee
                    // implement cavalry/chariots/elephants charge later!
                    // implement berserker later!
                    int meleeAttackDamage = attacker.getAttackDamage(); 
                    damage = (int) Math.rint((enemySize * 0.1) * (meleeAttackDamage / (enemyArmor + enemyShield) * (N + 1)));
                
                }
            }
            case "missile" : {
                if (this.type.equals("missile")) {
                    // missile engagement
                    int missileAttackDamage = attacker.getAttackDamage();
                    
                    damage = (int) Math.rint((enemySize * 0.1) * (missileAttackDamage / (enemyArmor + enemyShield)) * (N + 1));    
                } else {
                    // melee engagement
                    int meleeAttackDamage = attacker.getAttackDamage(); 
                    damage = (int) Math.rint((enemySize * 0.1) * (meleeAttackDamage / (enemyArmor + enemyShield) * (N + 1)));

                }
                
            }
        }

        return damage;
    }

    // public String getWinnerFaction() {
    //     return winner.getFactionName();
    // }
}
