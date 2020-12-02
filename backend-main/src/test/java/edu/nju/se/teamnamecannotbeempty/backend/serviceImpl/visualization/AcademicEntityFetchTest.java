package edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.visualization;

import edu.nju.se.teamnamecannotbeempty.backend.config.parameter.EntityMsg;
import edu.nju.se.teamnamecannotbeempty.backend.vo.AcademicEntityVO;
import edu.nju.se.teamnamecannotbeempty.data.domain.Affiliation;
import edu.nju.se.teamnamecannotbeempty.data.domain.Conference;
import edu.nju.se.teamnamecannotbeempty.data.domain.Term;
import edu.nju.se.teamnamecannotbeempty.data.repository.*;
import edu.nju.se.teamnamecannotbeempty.data.repository.duplication.DuplicateAffiliationDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.duplication.DuplicateAuthorDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.popularity.PaperPopDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.popularity.TermPopDao;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

import java.util.AbstractList;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class AcademicEntityFetchTest {

    @Autowired
    private AffiliationDao affiliationDao;
    @Autowired
    private AuthorDao authorDao;
    @Autowired
    private ConferenceDao conferenceDao;
    @Autowired
    private PaperDao paperDao;
    @Autowired
    private TermPopDao termPopDao;
    @Autowired
    private EntityMsg entityMsg;
    @Autowired
    private PaperPopDao paperPopDao;
    @Autowired
    private TermDao termDao;
    @Autowired
    private FetchForCache fetchForCache;

    private AcademicEntityFetch academicEntityFetch;

//    @Before
//    public void setUp() {
//       affiliationDao=mock(AffiliationDao.class);
//       when(affiliationDao.getAffiliationsWithPopByAuthor(any(Long.class))).thenReturn(new ArrayList<Affiliation>());
//       authorDao=mock(AuthorDao.class);
//       conferenceDao=mock(ConferenceDao.class);
//       when(conferenceDao.getConferencesByAuthor(any(Long.class))).thenReturn(new ArrayList<Conference>());
//       paperDao=mock(PaperDao.class);
//       termDao=mock(TermDao.class);
//       termPopDao=mock(TermPopDao.class);
//       when(termPopDao.getTermPopByAuthorID(any(Long.class))).thenReturn(new ArrayList<Term.Popularity>());
//       entityMsg=mock(EntityMsg.class);
//       when(entityMsg.getAuthorType()).thenReturn(1);
//       when(entityMsg.getAffiliationType()).thenReturn(2);
//       paperDao=mock(PaperDao.class);
//       fetchForCache=mock(FetchForCache.class);
//    }

    @Test
    void getAcademicEntity() {
        EntityMsg msg=new EntityMsg();
        msg.setAuthorType(1);
        academicEntityFetch=new AcademicEntityFetch(affiliationDao,authorDao,conferenceDao,paperDao,
        msg,termPopDao,paperPopDao,termDao,fetchForCache,null);
        AcademicEntityVO result=academicEntityFetch.getAcademicEntity(8954,1);
        assertNotNull(result);
    }

    @Test
    void getAllAliasIdsOfAuthor() {
    }

    @Test
    void getAllAliasIdsOfAffi() {
    }
}