package unsw.gloriaromanus.backend;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class tempTest {
    
    
    public static void main(String[] args) {
        GameSystem gs = new GameSystem();
        System.out.println(gs.toJSON().toString(2));
        try {
            String content = Files.readString(Paths.get("src/unsw/gloriaromanus/backend/UnitsInfo.json"));
            JSONObject fullJSON = new JSONObject(content);
            JSONObject unitData1 = fullJSON.getJSONObject("Gallic berserker");

            JSONObject unitData2 = fullJSON.getJSONObject("Gallic berserker");
           
            TurnTracker tt = new TurnTracker();
            ProvincesTracker pt = new ProvincesTracker();
            FactionsTracker ft = new FactionsTracker();

            // create a faction
            Faction f = new Faction("Rome", pt, ft);
            System.out.println(f.toJSON().toString(2));

            // create a proinve "XI"
            Province p = new Province("XI", f, tt);

            Unit u1 = new Unit(unitData1, p);
            Unit u2 = new Unit(unitData2, p);
            p.addUnit(u1);
            p.addUnit(u2);
            System.out.println(p.toJSON().toString(2));

            System.out.println(f.toJSON().toString(2));
            


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
