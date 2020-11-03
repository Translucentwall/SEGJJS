package edu.nju.se.teamnamecannotbeempty.batch.parser.csv;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.opencsv.bean.CsvToBeanBuilder;
import edu.nju.se.teamnamecannotbeempty.batch.parser.csv.converters.*;
import edu.nju.se.teamnamecannotbeempty.batch.parser.csv.intermediate.PaperImd;
import edu.nju.se.teamnamecannotbeempty.data.domain.Paper;
import edu.nju.se.teamnamecannotbeempty.data.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Component
public class FromCSV {
    private final AuthorDao authorDao;
    private final AffiliationDao affiliationDao;
    private final TermDao termDao;
    private final ConferenceDao conferenceDao;
    private final AA_CooperateDao aa_cooperateDao;
    private final AuthorAffiliationYearDao authorAffiliationYearDao;

    private static final Logger logger = LoggerFactory.getLogger(FromCSV.class);

    @Autowired
    public FromCSV(AuthorDao authorDao, AffiliationDao affiliationDao,
                   TermDao termDao, ConferenceDao conferenceDao,
                   AA_CooperateDao aa_cooperateDao, AuthorAffiliationYearDao authorAffiliationYearDao) {
        this.authorDao = authorDao;
        this.affiliationDao = affiliationDao;
        this.termDao = termDao;
        this.conferenceDao = conferenceDao;
        this.aa_cooperateDao=aa_cooperateDao;
        this.authorAffiliationYearDao=authorAffiliationYearDao;
    }

    public Collection<Paper> convertJson(InputStream in) throws IOException {
        int len;
        StringBuilder sb=new StringBuilder();
        byte[] bytes=new byte[1024];
        while ((len=in.read(bytes))!=-1){
            sb.append(new String(bytes,0,len));
        }
        in.close();
        JSONArray jsonArray= JSONObject.parseArray(sb.toString());
        Iterator<Object> it=jsonArray.iterator();
        List<PaperImd> paperImdList=new ArrayList<>();
        Map<String, Paper> paperMap=new HashMap<>();
        while (it.hasNext()){
            String jsonString=it.next().toString();
            PaperImd paperImd=JSON.parseObject(jsonString,PaperImd.class);
            paperImdList.add(paperImd);
            paperMap.put(paperImd.getTitle(),paperImd.toPaper());
        }
        AddRef.addRef(paperImdList,paperMap);

        termDao.saveAll(ToTerm.getSaveCollection());
        affiliationDao.saveAll(ToAffiliation.getSaveCollection());
        authorDao.saveAll(ToAuthor.getSaveCollection());
        conferenceDao.saveAll(ToConference.getSaveCollection());

        aa_cooperateDao.saveAll(ToAuAuCoo.getSaveCollection());
        authorAffiliationYearDao.saveAll(ToAuAffiYear.getSaveCollection());

        logger.info("Done convert to paper POs");
        return paperMap.values();
    }
}
