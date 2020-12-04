package edu.nju.se.teamnamecannotbeempty.backend.service.peer_review;

import edu.nju.se.teamnamecannotbeempty.backend.AppContextProvider;
import edu.nju.se.teamnamecannotbeempty.backend.config.HibernateSearchConfig;
import edu.nju.se.teamnamecannotbeempty.backend.vo.InformationReviewed;
import edu.nju.se.teamnamecannotbeempty.backend.vo.ResponseVO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertNotNull;

@DataJpaTest(includeFilters = {
        @ComponentScan.Filter(
                type = FilterType.REGEX,
                pattern = {"edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.peer_review.*",
                "edu.nju.se.teamnamecannotbeempty.backend.config.parameter.*"}
        ),
        @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {HibernateSearchConfig.class, AppContextProvider.class}
        ),
        @ComponentScan.Filter(
                type = FilterType.REGEX,
                pattern = "edu.nju.se.teamnamecannotbeempty.data.*"
        )
})
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public class PeerReviewTest {
    @Autowired
    private PeerReviewService peerReviewService;

    @Before
    public void setUp() {
        System.out.println("set up");
    }

    @Test
    public void searchTermTest(){
        List<String> result=peerReviewService.searchTerm("test");
        assertNotNull(result);
    }

    @Test
    public void recommendReviewer(){
        InformationReviewed inf=new InformationReviewed();
        inf.setAuthor("a");
        inf.setAffiliation("nju");
        inf.setTerm("mining");
        ResponseVO responseVO=peerReviewService.recommendReviewer(inf);
        assertNotNull(responseVO);
    }

}
