package edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.paper;

import edu.nju.se.teamnamecannotbeempty.backend.AppContextProvider;
import edu.nju.se.teamnamecannotbeempty.backend.config.parameter.PaperMsg;
import edu.nju.se.teamnamecannotbeempty.backend.service.paper.PaperService;
import edu.nju.se.teamnamecannotbeempty.backend.service.search.SearchMode;
import edu.nju.se.teamnamecannotbeempty.backend.service.search.SearchService;
import edu.nju.se.teamnamecannotbeempty.backend.service.search.SortMode;
import edu.nju.se.teamnamecannotbeempty.backend.vo.*;
import edu.nju.se.teamnamecannotbeempty.data.domain.*;
import edu.nju.se.teamnamecannotbeempty.data.repository.PaperDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.RefDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Struct;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaperServiceImpl implements PaperService {

    private final SearchService searchService;
    private final PaperDao paperDao;
    private final PaperMsg paperMsg;
    private final RefDao refDao;

    @Autowired
    public PaperServiceImpl(PaperDao paperDao, PaperMsg paperMsg, SearchService searchService,RefDao refDao) {
        this.paperDao = paperDao;
        this.paperMsg = paperMsg;
        this.searchService = searchService;
        this.refDao=refDao;
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
        List<Ref> refs=refDao.findByReferer_Id(paper.getId());
        List<TitleAndId> refers=new ArrayList<>();
        for(Ref ref:refs){
            Paper tmp=ref.getReferee();
            if(tmp==null)
                continue;
            refers.add(new TitleAndId(tmp.getTitle(),tmp.getId()));
        }
        refers.sort(Comparator.comparing(TitleAndId::getTitle));
        List<Ref> refees=refDao.findByReferee_Id(paper.getId());
        List<TitleAndId> referees=new ArrayList<>();
        for(Ref ref:refees){
            Paper tmp=ref.getReferer();
            if(tmp==null)
                continue;
            referees.add(new TitleAndId(tmp.getTitle(),tmp.getId()));
        }
        referees.sort(Comparator.comparing(TitleAndId::getTitle));
        List<Term> termListKeywords = paper.getAuthor_keywords();

        List<String> keywords;
        keywords = termListKeywords.stream().map(Term::getContent).collect(Collectors.toList());

        PaperVO paperVO=new PaperVO(paper.getId(), paper.getTitle(),
                authorAffiliationVOS, paper.getYear_highlight(),
                paper.getSummary(), paper.getDoi(),keywords, paper.getCitation(),
                paper.getReference(),refers,referees);
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
