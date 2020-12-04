package edu.nju.se.teamnamecannotbeempty.backend.hibernate_search;

import edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.search.SearchServiceHibernateImpl;
import edu.nju.se.teamnamecannotbeempty.data.domain.Paper;
import edu.nju.se.teamnamecannotbeempty.data.domain.Term;
import org.hibernate.*;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.batchindexing.impl.SimpleIndexingProgressMonitor;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import javax.persistence.EntityManager;

@Controller
public class HibernateSearchInit {
    private final EntityManager entityManager;
    private final Indexer indexer;

    @Autowired
    public HibernateSearchInit(EntityManager entityManager, Indexer indexer) {
        this.entityManager = entityManager;
        hibernateSearchInit();
        this.indexer = indexer;
    }

    public void hibernateSearchInit() {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        try {
            fullTextEntityManager.createIndexer()
                    .idFetchSize(Integer.MIN_VALUE)
                    .progressMonitor(new SimpleIndexingProgressMonitor(2000))
                    .startAndWait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void flushIndexes() {
        indexer.flushIndexes();
    }

    @Component
    public static class Indexer {
        private final Searchable searchable;
        private final EntityManager entityManager;

        public Indexer(Searchable searchable, EntityManager entityManager) {
            this.searchable = searchable;
            this.entityManager = entityManager;
        }

        @Async
        public void flushIndexes() {
            long startTime = System.currentTimeMillis();
            final long DEADLINE = 2000 * 60 * 15;
            searchable.startIndexing();
            while (!searchable.importOK()) {
                try {
                    //noinspection BusyWait
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (System.currentTimeMillis() - startTime > DEADLINE) {
                    logger.error("Data import job seen to be failed. Abort indexes flushing.");
                    searchable.endIndexing();
                    return;
                }
            }
            try {
                FullTextSession fullTextSession =
                        org.hibernate.search.Search.getFullTextSession(entityManager.unwrap(Session.class));
                fullTextSession.setHibernateFlushMode(FlushMode.MANUAL);
                fullTextSession.setCacheMode(CacheMode.IGNORE);

                final int batchSize = 300;
                //noinspection deprecation
                ScrollableResults scrollableResults = fullTextSession
                        .createCriteria(Paper.class)
                        .setFetchSize(batchSize).scroll(ScrollMode.FORWARD_ONLY);

                ScrollableResults scrollableResults1=fullTextSession.
                        createCriteria(Term.class)
                        .setFetchSize(batchSize).scroll(ScrollMode.FORWARD_ONLY);

                Transaction tx = fullTextSession.beginTransaction();
                int index = 0;
                while(scrollableResults.next()) {
                    index++;
                    fullTextSession.index(scrollableResults.get(0)); //index each element
                    if (index % batchSize == 0) {
                        fullTextSession.flushToIndexes(); //apply changes to indexes
                        fullTextSession.clear(); //free memory since the queue is processed
                    }
                }
                index=0;
                while(scrollableResults1.next()) {
                    index++;
                    fullTextSession.index(scrollableResults1.get(0)); //index each element
                    if (index % batchSize == 0) {
                        fullTextSession.flushToIndexes(); //apply changes to indexes
                        fullTextSession.clear(); //free memory since the queue is processed
                    }
                }
                tx.commit();

                logger.info("Index finished.");
            } catch (Exception e) {
                logger.error("Index procedure failed!");
            } finally {
                System.gc();
                searchable.endIndexing();
            }
        }
        private final Logger logger = LoggerFactory.getLogger(SearchServiceHibernateImpl.class);
    }
}
