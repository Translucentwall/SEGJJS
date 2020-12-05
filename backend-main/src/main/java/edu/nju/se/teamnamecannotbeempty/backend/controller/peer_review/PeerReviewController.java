package edu.nju.se.teamnamecannotbeempty.backend.controller.peer_review;

import edu.nju.se.teamnamecannotbeempty.backend.service.peer_review.PeerReviewService;
import edu.nju.se.teamnamecannotbeempty.backend.vo.InformationReviewed;
import edu.nju.se.teamnamecannotbeempty.backend.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author laiba
 */
@RestController
public class PeerReviewController {

    private final PeerReviewService peerReviewService;

    @Autowired
    public PeerReviewController(PeerReviewService peerReviewService) {
        this.peerReviewService = peerReviewService;
    }

    /**
     * 当研究方向框变化时进行自动补全推荐
     * @param prefix 用户输入的研究方向字段
     * @return 最多10个研究方向，可能为空列表
     */
    @RequestMapping(value ="/peerReview/complete/{prefix}", method = RequestMethod.GET)
    public List<String> autoComplete(@PathVariable String prefix){
        return peerReviewService.searchTerm(prefix);
    }

    /**
     * 根据用户输入的评审信息进行同行评审人推荐
     * @param inf 用户输入的评审表单信息,区分大小写
     * @return ResponseVo，当success=false时，content不包含内容，报错信息在message，
     *         当success=true时，content包含推荐的评审人列表，按评审人热度降序排序，message不包含内容
     */
    @RequestMapping(value ="/peerReview/recommend",method = RequestMethod.POST)
    public ResponseVO peerReviewerRecommend(@RequestBody InformationReviewed inf){
        return peerReviewService.recommendReviewer(inf);
    }

}
