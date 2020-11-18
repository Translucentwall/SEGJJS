package edu.nju.se.teamnamecannotbeempty.batch.parser.csv;

import edu.nju.se.teamnamecannotbeempty.batch.parser.csv.intermediate.PaperImd;
import edu.nju.se.teamnamecannotbeempty.batch.parser.csv.intermediate.RefImd;
import edu.nju.se.teamnamecannotbeempty.data.domain.Paper;
import edu.nju.se.teamnamecannotbeempty.data.domain.Ref;

import java.util.List;
import java.util.Map;

/**
 * @author laiba
 */
public class AddRef {

    public static void addRef(List<PaperImd> paperImdList, Map<String, Paper> paperMap){
        for(PaperImd paperImd:paperImdList){
            List<RefImd> refImdList=paperImd.getReferences();
            Paper paper=paperMap.get(paperImd.getTitle());
            for(RefImd refImd:refImdList){
                if(!refImd.isValidRef()){
                    continue;
                }
                Ref ref=new Ref(refImd.getTitle());
                Paper newPaper=paperMap.get(refImd.getTitle());
                if(newPaper==null){
                    newPaper=refImd.toPaper();
                    paperMap.put(refImd.getTitle(),newPaper);
                }
                ref.setReferee(newPaper);
                paper.addRef(ref);
            }
        }
    }
}
