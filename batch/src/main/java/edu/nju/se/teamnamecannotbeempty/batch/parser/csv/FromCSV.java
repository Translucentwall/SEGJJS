package edu.nju.se.teamnamecannotbeempty.batch.parser.csv;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.opencsv.bean.CsvToBeanBuilder;
import edu.nju.se.teamnamecannotbeempty.batch.parser.csv.converters.ToAffiliation;
import edu.nju.se.teamnamecannotbeempty.batch.parser.csv.converters.ToAuthor;
import edu.nju.se.teamnamecannotbeempty.batch.parser.csv.converters.ToConference;
import edu.nju.se.teamnamecannotbeempty.batch.parser.csv.converters.ToTerm;
import edu.nju.se.teamnamecannotbeempty.batch.parser.csv.intermediate.PaperImd;
import edu.nju.se.teamnamecannotbeempty.data.domain.Paper;
import edu.nju.se.teamnamecannotbeempty.data.repository.AffiliationDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.AuthorDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.ConferenceDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.TermDao;
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

    private static final Logger logger = LoggerFactory.getLogger(FromCSV.class);

    @Autowired
    public FromCSV(AuthorDao authorDao, AffiliationDao affiliationDao, TermDao termDao, ConferenceDao conferenceDao) {
        this.authorDao = authorDao;
        this.affiliationDao = affiliationDao;
        this.termDao = termDao;
        this.conferenceDao = conferenceDao;
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

        logger.info("Done convert to paper POs");
        return paperMap.values();
    }
}
