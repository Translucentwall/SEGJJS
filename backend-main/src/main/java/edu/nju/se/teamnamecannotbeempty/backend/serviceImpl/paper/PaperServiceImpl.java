package edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.paper;

import edu.nju.se.teamnamecannotbeempty.backend.AppContextProvider;
import edu.nju.se.teamnamecannotbeempty.backend.config.parameter.PaperMsg;
import edu.nju.se.teamnamecannotbeempty.backend.service.paper.PaperService;
import edu.nju.se.teamnamecannotbeempty.backend.service.search.SearchMode;
import edu.nju.se.teamnamecannotbeempty.backend.service.search.SearchService;
import edu.nju.se.teamnamecannotbeempty.backend.service.search.SortMode;
import edu.nju.se.teamnamecannotbeempty.backend.vo.*;
import edu.nju.se.teamnamecannotbeempty.data.domain.Author_Affiliation;
import edu.nju.se.teamnamecannotbeempty.data.domain.Conference;
import edu.nju.se.teamnamecannotbeempty.data.domain.Paper;
import edu.nju.se.teamnamecannotbeempty.data.domain.Term;
import edu.nju.se.teamnamecannotbeempty.data.repository.PaperDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaperServiceImpl implements PaperService {

    private final SearchService searchService;
    private final PaperDao paperDao;
    private final PaperMsg paperMsg;

    @Autowired
    public PaperServiceImpl(PaperDao paperDao, PaperMsg paperMsg, SearchService searchService) {
        this.paperDao = paperDao;
        this.paperMsg = paperMsg;
        this.searchService = searchService;
    }

    @Override
    public List<SimplePaperVO> search(String text, String mode, Integer pageNumber,
                                      String sortMode, int perPage) {
        if (pageNumber == null || pageNumber <= 0) {
            pageNumber = 1;
        }
        SearchMode searchMode = (SearchMode) AppContextProvider.getBean(mode);
        Pageable pageable = PageRequest.of(pageNumber - 1, perPage);
        SortMode sort = (SortMode) AppContextProvider.getBean(sortMode);
        Page<Paper> paperPage = searchService.search(text, searchMode, pageable, sort);
        List<Paper> paperList = paperPage.getContent();
        List<SimplePaperVO> simplePaperVOList = new ArrayList<>();
        for (Paper paper : paperList) {
            simplePaperVOList.add(new SimplePaperVO(paper));
        }
        return simplePaperVOList;
    }


    @Override
    public ResponseVO getPaper(long id) {
        Optional<Paper> optionalPaper = paperDao.findById(id);
        ResponseVO responseVO;
        if (!optionalPaper.isPresent()) {
            responseVO = ResponseVO.fail();
            responseVO.setMessage(paperMsg.getMismatchId());
            return responseVO;
        }
        Paper paper = optionalPaper.get();
        List<Author_AffiliationVO> authorAffiliationVOS = new ArrayList<>();
        List<Author_Affiliation> authorAffiliations = paper.getAa();
        for (Author_Affiliation authorAffiliation : authorAffiliations) {
            authorAffiliationVOS.add(new Author_AffiliationVO(authorAffiliation.getAuthor().getName(),
                    authorAffiliation.getAuthor().getId(),
                    new AffiliationVO(authorAffiliation.getAffiliation().getName(),
                            authorAffiliation.getAffiliation().getId())));
        }
        List<Term> termListKeywords = paper.getAuthor_keywords();

        List<String> keywords;
        keywords = termListKeywords.stream().map(Term::getContent).collect(Collectors.toList());

        PaperVO paperVO=new PaperVO(paper.getId(), paper.getTitle(),
                authorAffiliationVOS, paper.getYear_highlight(),
                paper.getSummary(), paper.getDoi(),keywords, paper.getCitation(),
                paper.getReference());
        Conference conference;
        if((conference=paper.getConference())!=null){
            paperVO.setConferenceId(conference.getId());
            paperVO.setConferenceTitle(conference.getName());
        }
        responseVO = ResponseVO.success();
        responseVO.setContent(paperVO);
        return responseVO;
    }
}
