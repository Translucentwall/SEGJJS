package edu.nju.se.teamnamecannotbeempty.batch.job.worker;

import edu.nju.se.teamnamecannotbeempty.data.domain.Paper;
import edu.nju.se.teamnamecannotbeempty.data.domain.Ref;
import edu.nju.se.teamnamecannotbeempty.data.repository.PaperDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.RefDao;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Future;

/**
 * @author laiba
 */
@Component
public class CitedWorker {
    private final RefDao refDao;
    private final PaperDao paperDao;

    @Autowired
    public CitedWorker(RefDao refDao, PaperDao paperDao) {
        this.refDao = refDao;
        this.paperDao=paperDao;
    }

    public void generatePaperCitation(){
        List<Paper> paperList=paperDao.findAll();
        paperList.parallelStream().forEach(paper -> {
            paper.setCitation(refDao.countRefsByReferee(paper));
            paperDao.update(paper);
        });
    }
}
