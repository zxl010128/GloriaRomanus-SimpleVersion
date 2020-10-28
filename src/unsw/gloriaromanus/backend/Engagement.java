package unsw.gloriaromanus.backend;

public class Engagement {
    private Unit attacker;
    private Unit defender;
    private String type;

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
            int meleeSpeed = (attackerType.equals("melee")) ? attacker.getSpeed() : defender.getSpeed();
            int missileSpeed = (defenderType.equals("missile")) ? defender.getSpeed() : attacker.getSpeed();

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

    }
}
