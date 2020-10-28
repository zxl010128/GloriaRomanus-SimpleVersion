package unsw.gloriaromanus.backend;

import java.util.Random;

public class Engagement {
    private Troop attacker;
    private Troop defender;
    private String type;

    public Engagement(Troop attacker, Troop defender) {
        this.attacker = attacker;
        this.defender = defender;

        String attackerType = attacker.getType();
        String defenderType = defender.getType();

        if (attackerType.equals("melee") && defenderType.equals("melee")) {
            this.type = "melee";
        } else if (attackerType.equals("missile") && defenderType.equals("missile")) {
            this.type = "missile";
        } else {
            Random r = new Random();
            int choice = r.nextInt(2);
            
        }
    }
}
