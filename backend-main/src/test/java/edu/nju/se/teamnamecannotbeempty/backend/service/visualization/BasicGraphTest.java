package edu.nju.se.teamnamecannotbeempty.backend.service.visualization;

import edu.nju.se.teamnamecannotbeempty.backend.config.parameter.EntityMsg;
import edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.visualization.BasicGraphFetch;
import edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.visualization.FetchForCache;
import edu.nju.se.teamnamecannotbeempty.backend.vo.GraphVO;
import edu.nju.se.teamnamecannotbeempty.data.domain.*;
import edu.nju.se.teamnamecannotbeempty.data.repository.*;
import edu.nju.se.teamnamecannotbeempty.data.repository.popularity.AffiPopDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.popularity.AuthorPopDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.popularity.PaperPopDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.popularity.TermPopDao;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class BasicGraphTest {

    @Mock
    private AffiliationDao affiliationDao;
    @Mock
    private AuthorDao authorDao;
    @Mock
    private ConferenceDao conferenceDao;
    @Mock
    private PaperDao paperDao;
    @Mock
    private TermDao termDao;
    @Mock
    private TermPopDao termPopDao;
    @Mock
    private EntityMsg entityMsg;
    @Mock
    private PaperPopDao paperPopDao;
    @Mock
    private AuthorPopDao authorPopDao;
    @Mock
    private AffiPopDao affiPopDao;
    @Mock
    private FetchForCache fetchForCache;
    @InjectMocks
    private BasicGraphFetch basicGraphFetch;

    @Before
    public void setup(){
        when(entityMsg.getAuthorType()).thenReturn(1);
        when(entityMsg.getAffiliationType()).thenReturn(2);
        when(entityMsg.getConferenceType()).thenReturn(3);
        when(entityMsg.getTermType()).thenReturn(4);
        when(entityMsg.getPaperType()).thenReturn(5);
    }

    @Test
    public void authorBasicGraphTest() {
        Author author1 = new Author();
        author1.setId(1L);
        author1.setName("");
        Optional<Author> optionalAuthor = Optional.of(author1);
        when(authorDao.findById(1L)).thenReturn(optionalAuthor);
        Author author2=new Author();
        author2.setId(2L); author2.setName("");
        Author author3=new Author();
        author3.setId(3L); author3.setName("");
        Author author4=new Author();
        author4.setId(4L); author4.setName("");
        when(authorDao.getAuthorByCoo(1L)).
                thenReturn(new HashSet<>(Arrays.asList(author2,author3)));
        when(authorDao.getAuthorByCoo(2L)).
                thenReturn(new HashSet<>(Arrays.asList(author1,author3,author4)));
        when(authorDao.getAuthorByCoo(3L)).
                thenReturn(new HashSet<>(Arrays.asList(author1,author2)));
        when(authorDao.getAuthorByCoo(4L)).
                thenReturn(new HashSet<>(Collections.singletonList(author2)));
        GraphVO graphVO=basicGraphFetch.getBasicGraph(1L,1);
        Assert.assertNotNull(graphVO);
        Assert.assertEquals(4,graphVO.getNodes().size());
        Assert.assertEquals(4,graphVO.getLinks().size());
    }

    @Test
    public void affiliationBasicGraphTest(){
        Affiliation affiliation1 = new Affiliation(); affiliation1.setId(1L); affiliation1.setName("");
        Optional<Affiliation> optionalAffiliation = Optional.of(affiliation1);

        Author author1 = new Author(); author1.setId(1L); author1.setName("author1");
        when(authorDao.getAuthorsByAffiliation(1L)).thenReturn(Collections.singletonList(author1));

        Term term1 = new Term(); term1.setContent("term1"); term1.setId(1L);
        Term.Popularity termPop = new Term.Popularity();
        termPop.setTerm(term1);
        when(termPopDao.getTermPopByAffiID(1L)).thenReturn(Collections.singletonList(termPop));
        when(paperPopDao.getWeightByAffiOnKeyword(1L,1L)).thenReturn(1.0);
        when(termPopDao.getTermPopByAuthorID(1L)).thenReturn(Collections.singletonList(termPop));
        when(paperPopDao.getWeightByAuthorOnKeyword(1L,1L)).thenReturn(1.0);

        when(affiliationDao.findById(1L)).thenReturn(optionalAffiliation);

        GraphVO graphVO = basicGraphFetch.getBasicGraph(1L,2);
        Assert.assertEquals(graphVO.getNodes().size(),3);
        Assert.assertEquals(graphVO.getLinks().size(),3);
        Assert.assertEquals(graphVO.getId(),"20000000001");
    }

    @Test
    public void conferenceBasicGraph(){
        Paper paper = new Paper(); paper.setId(1L); paper.setTitle("paper1");
        when(fetchForCache.getAllPapersByConference(1L)).thenReturn(Collections.singletonList(paper));

        Conference conference1 = new Conference(); conference1.setId(1L);
        Optional<Conference> optionalConference = Optional.of(conference1);
        when(conferenceDao.findById(1L)).thenReturn(optionalConference);

        GraphVO graphVO=basicGraphFetch.getBasicGraph(1L,3);
        Assert.assertEquals(graphVO.getNodes().size(),2);
        Assert.assertEquals(graphVO.getLinks().size(),1);
        Assert.assertEquals(graphVO.getId(),"30000000001");
    }

    @Test
    public void termBasicGraph(){
        Author author1 = new Author(); author1.setId(1L); author1.setName("author1");
        Author author2 = new Author(); author2.setId(2L); author2.setName("author2");
        when(authorDao.getAuthorsByKeyword(1L)).thenReturn(Arrays.asList(author1,author2));
        Paper paper1 = new Paper(); paper1.setId(1L); paper1.setTitle("paper1");
        Paper paper2 = new Paper(); paper2.setId(2L); paper2.setTitle("paper2");
        Paper.Popularity paperPop1 = new Paper.Popularity(); paperPop1.setPaper(paper1); paperPop1.setPopularity(1.0);
        Paper.Popularity paperPop2 = new Paper.Popularity(); paperPop2.setPaper(paper2); paperPop2.setPopularity(2.0);
        Author_Affiliation aa = new Author_Affiliation(); aa.setAuthor(author2);
        paper2.setAa(Collections.singletonList(aa));
        when(paperDao.getPapersByKeyword(1L)).thenReturn(Arrays.asList(paper1,paper2));
        when(paperPopDao.getByPaper_Id(1L)).thenReturn(Optional.of(paperPop1));
        when(paperPopDao.getByPaper_Id(2L)).thenReturn(Optional.of(paperPop2));

        Term term=new Term(); term.setId(1L); Optional<Term> optionalTerm = Optional.of(term);
        when(termDao.findById(1L)).thenReturn(optionalTerm);

        GraphVO graphVO = basicGraphFetch.getBasicGraph(1L, 4);
        Assert.assertEquals(graphVO.getNodes().size(),5);
        Assert.assertEquals(graphVO.getLinks().size(),5);
        Assert.assertEquals(graphVO.getId(),"40000000001");
    }
}
