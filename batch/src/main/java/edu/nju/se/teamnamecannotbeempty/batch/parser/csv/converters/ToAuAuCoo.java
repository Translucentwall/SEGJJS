package edu.nju.se.teamnamecannotbeempty.batch.parser.csv.converters;

import edu.nju.se.teamnamecannotbeempty.batch.parser.csv.intermediate.AuAfCooImd;
import edu.nju.se.teamnamecannotbeempty.data.domain.AA_Cooperate;
import edu.nju.se.teamnamecannotbeempty.data.domain.Author;

import java.util.Collection;
import java.util.HashMap;

/**
 * @author laiba
 */
public class ToAuAuCoo {
    private static final HashMap<AuAfCooImd, AA_Cooperate> saveMap=new HashMap<>();

    public static void saveAA_Cooperate(AuAfCooImd auAfCooImd){
        if(saveMap.get(auAfCooImd)==null){
            saveMap.put(auAfCooImd,auAfCooImd.toAA_Cooperate());
        }
    }

    public static Collection<AA_Cooperate> getSaveCollection() {
        return saveMap.values();
    }
}
