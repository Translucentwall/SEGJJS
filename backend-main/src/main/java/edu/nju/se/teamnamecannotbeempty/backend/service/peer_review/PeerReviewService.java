package edu.nju.se.teamnamecannotbeempty.backend.service.peer_review;

import edu.nju.se.teamnamecannotbeempty.backend.vo.InformationReviewed;
import edu.nju.se.teamnamecannotbeempty.backend.vo.ResponseVO;

import java.util.List;

/**
 * 评审人推荐相关接口
 * @author laiba
 */
public interface PeerReviewService {

    /**
     * 搜索前10个包含传入的字符串的研究方向，按相关性排序
     * @param prefix 传入的字符串
     * @return 最多10个
     */
    List<String> searchTerm(String prefix);

    /**
     * 根据用户输入的评审信息进行同行评审人推荐
     * @param inf 用户输入的评审表单信息
     * @return ResponseVo，当success=false时，content不包含内容，报错信息在message，
     *         当success=true时，content包含推荐的评审人列表，按评审人热度降序排序，message不包含内容
     */
    ResponseVO recommendReviewer(InformationReviewed inf);
}
