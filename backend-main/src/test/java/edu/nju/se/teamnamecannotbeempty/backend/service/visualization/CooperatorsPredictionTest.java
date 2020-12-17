package edu.nju.se.teamnamecannotbeempty.backend.service.visualization;

import edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.visualization.CooperatorsPrediction;
import edu.nju.se.teamnamecannotbeempty.backend.vo.CooperatorVO;
import edu.nju.se.teamnamecannotbeempty.data.domain.AA_Cooperate;
import edu.nju.se.teamnamecannotbeempty.data.domain.Affiliation;
import edu.nju.se.teamnamecannotbeempty.data.domain.Author;
import edu.nju.se.teamnamecannotbeempty.data.domain.Term;
import edu.nju.se.teamnamecannotbeempty.data.repository.AA_CooperateDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.AffiliationDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.AuthorDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.TermDao;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.after;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class CooperatorsPredictionTest {
    @Mock
    AA_CooperateDao aa_cooperateDao;
    @Mock
    TermDao termDao;
    @Mock
    AuthorDao authorDao;
    @Mock
    AffiliationDao affiliationDao;

    @InjectMocks
    CooperatorsPrediction cooperatorsPrediction;

    @Test
    public void getPossibleCoosTest(){
        Author author1=new Author(); author1.setId(1L);
        Author author2=new Author(); author2.setId(2L);
        Author author3=new Author(); author3.setId(3L);
        Author author4=new Author(); author4.setId(4L);

        AA_Cooperate aa_cooperate1=new AA_Cooperate();
        aa_cooperate1.setAuthor1(author1); aa_cooperate1.setAuthor2(author2);
        aa_cooperate1.setYear(2010);

        AA_Cooperate aa_cooperate2=new AA_Cooperate();
        aa_cooperate2.setAuthor1(author1); aa_cooperate2.setAuthor2(author3);
        aa_cooperate2.setYear(2020);

        when(aa_cooperateDao.getAA_CooperatesByAuthor1Id(1L)).
                thenReturn(Arrays.asList(aa_cooperate1,aa_cooperate2));
        Term term1=new Term(); term1.setId(1L); term1.setContent("term1");
        Term term2=new Term(); term2.setId(2L); term2.setContent("term2");
        when(termDao.getByAuthor(1L)).thenReturn(Arrays.asList(term1,term2));
        when(termDao.getByAuthor(2L)).thenReturn(Collections.singletonList(term1));
        when(termDao.getByAuthor(3L)).thenReturn(new ArrayList<>(0));
        when(termDao.getByAuthor(4L)).thenReturn(Arrays.asList(term1,term2));

        when(authorDao.getAuthorsByKeyword(1L)).thenReturn(Arrays.asList(author1,author2,author4));
        when(authorDao.getAuthorsByKeyword(2L)).thenReturn(Arrays.asList(author1,author4));

        Affiliation affiliation1=new Affiliation(); affiliation1.setId(1L); affiliation1.setName("af1");
        Affiliation affiliation2=new Affiliation(); affiliation1.setId(2L); affiliation1.setName("af2");

        when(affiliationDao.getNewestAffiliationByAuthor(1L)).thenReturn(affiliation1);
        when(affiliationDao.getNewestAffiliationByAuthor(2L)).thenReturn(affiliation2);
        when(affiliationDao.getNewestAffiliationByAuthor(3L)).thenReturn(null);
        when(affiliationDao.getNewestAffiliationByAuthor(4L)).thenReturn(null);

        List<CooperatorVO> cooperatorVOList=cooperatorsPrediction.getPossibleCoos(1);
        Assert.assertNotNull(cooperatorVOList);
        Assert.assertEquals(3,cooperatorVOList.size());
    }
}
