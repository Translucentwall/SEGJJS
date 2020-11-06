package edu.nju.se.teamnamecannotbeempty.batch.job;

import edu.nju.se.teamnamecannotbeempty.api.IDataImportJob;
import edu.nju.se.teamnamecannotbeempty.batch.job.worker.*;
import edu.nju.se.teamnamecannotbeempty.batch.parser.csv.FromJSON;
import edu.nju.se.teamnamecannotbeempty.data.domain.Paper;
import edu.nju.se.teamnamecannotbeempty.data.repository.PaperDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.Future;

@Service
public class DataImportJob implements IDataImportJob {
    private final FromJSON fromJSON;
    private final BatchGenerator batchGenerator;
    private final PaperDao paperDao;

    private static final Logger logger = LoggerFactory.getLogger(DataImportJob.class);

    @Autowired
    public DataImportJob(FromJSON fromJSON, BatchGenerator batchGenerator, PaperDao paperDao) {
        this.fromJSON = fromJSON;
        this.batchGenerator = batchGenerator;
        this.paperDao=paperDao;
    }

    @Override
    public long trigger() {
        logger.info("Triggered import job");
        long total = 0;

        try {

            InputStream tse_json= getClass().getResourceAsStream("/datasource/tse.json");

            String name = "all together";
            total=readFile(name,tse_json);

            batchGenerator.trigger_init(total);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    private long readFile(String name, InputStream json) throws IOException {
        logger.info("Start import papers from " + name);
        Collection<Paper> papers= fromJSON.convertJson(json);
        long size = papers.size();
        try {
            json.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            paperDao.saveAll(papers);
            paperDao.flush();
            logger.info("Done saving data from " + name);
        } catch (Exception e) {
            logger.error("Fatal saving papers.");
            e.printStackTrace();
        }
        return size;
    }

    @Component
    static class BatchGenerator {
        private final AuthorPopWorker authorPopWorker;
        private final AffiPopWorker affiPopWorker;
        private final TermPopWorker termPopWorker;
        private final PaperPopWorker paperPopWorker;
        private final AuthorDupWorker authorDupWorker;
        private final AffiDupWorker affiDupWorker;
        private final EntityManager entityManager;
        private final CitedWorker citedWorker;

        @Autowired
        public BatchGenerator(AuthorPopWorker authorPopWorker, AffiPopWorker affiPopWorker, TermPopWorker termPopWorker,
                              AuthorDupWorker authorDupWorker, AffiDupWorker affiDupWorker, PaperPopWorker paperPopWorker,
                              EntityManager entityManager, CitedWorker citedWorker) {
            this.authorPopWorker = authorPopWorker;
            this.affiPopWorker = affiPopWorker;
            this.termPopWorker = termPopWorker;
            this.authorDupWorker = authorDupWorker;
            this.affiDupWorker = affiDupWorker;
            this.paperPopWorker = paperPopWorker;
            this.entityManager = entityManager;
            this.citedWorker=citedWorker;
        }

        public void trigger_init(long total) {
            citedWorker.generatePaperCitation();
            affiDupWorker.generateAffiDup();
            authorDupWorker.generateAuthorDup();
            long startTime = System.currentTimeMillis();
            final long DEADLINE = 1000 * 60 * 10;
            logger.info("Triggered, expect deadline is " + new Date(startTime + DEADLINE).toString());
            long count;
            while ((count = paperPopWorker.count()) != total) {
                try {
                    //noinspection BusyWait
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    logger.warn(e.getMessage());
                }
                if (System.currentTimeMillis() - startTime > DEADLINE) {
                    logger.error("Data import time exceed " + DEADLINE / 1000 + " second, and current count is " + count + ". Abort it");
                    return;
                }
            }
            logger.info("Done import papers. Start generating paper popularity...");
            paperPopWorker.generatePaperPop();
            // async 3 tasks
            Future<?> genau = authorPopWorker.generateAuthorPop();
            Future<?> genaf = affiPopWorker.generateAffiPop();
            Future<?> genterm = termPopWorker.generateTermPop();
            while (!(genau.isDone() && genaf.isDone() && genterm.isDone())) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            entityManager.clear();
            System.gc();
        }
    }
}
