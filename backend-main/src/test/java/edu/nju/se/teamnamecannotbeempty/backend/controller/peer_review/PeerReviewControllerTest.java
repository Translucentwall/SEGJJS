package edu.nju.se.teamnamecannotbeempty.backend.controller.peer_review;

import edu.nju.se.teamnamecannotbeempty.backend.service.peer_review.PeerReviewService;
import edu.nju.se.teamnamecannotbeempty.backend.vo.ResponseVO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class PeerReviewControllerTest {
    private MockMvc mockMvc;
    @Mock
    private PeerReviewService peerReviewService;

    @InjectMocks
    private PeerReviewController peerReviewController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(peerReviewController).build();
    }

    @Test
    public void autoComplete(){
        peerReviewController=new PeerReviewController(peerReviewService);
        Mockito.when(peerReviewService.searchTerm(Mockito.anyString())).thenReturn(new ArrayList<>(0));
        assertEquals(0,peerReviewController.autoComplete("*").size());
    }

    @Test
    public void peerReviewerRecommend(){
        peerReviewController=new PeerReviewController(peerReviewService);
        Mockito.when(peerReviewService.recommendReviewer(null)).thenReturn(ResponseVO.fail());
        assertFalse(peerReviewController.peerReviewerRecommend(null).isSuccess());
    }
}
