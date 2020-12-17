package edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.visualization;

import edu.nju.se.teamnamecannotbeempty.backend.vo.CooperatorVO;
import edu.nju.se.teamnamecannotbeempty.backend.vo.TermItem;
import edu.nju.se.teamnamecannotbeempty.data.domain.AA_Cooperate;
import edu.nju.se.teamnamecannotbeempty.data.domain.Affiliation;
import edu.nju.se.teamnamecannotbeempty.data.domain.Author;
import edu.nju.se.teamnamecannotbeempty.data.domain.Term;
import edu.nju.se.teamnamecannotbeempty.data.repository.AA_CooperateDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.AffiliationDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.AuthorDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.TermDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author laiba
 */
@Component
public class CooperatorsPrediction {

    private final AA_CooperateDao aa_cooperateDao;
    private final TermDao termDao;
    private final AuthorDao authorDao;
    private final AffiliationDao affiliationDao;

    @Autowired
    public CooperatorsPrediction(AA_CooperateDao aa_cooperateDao, TermDao termDao,
                                 AuthorDao authorDao, AffiliationDao affiliationDao) {
        this.aa_cooperateDao = aa_cooperateDao;
        this.termDao = termDao;
        this.authorDao = authorDao;
        this.affiliationDao = affiliationDao;
    }

    @Cacheable(value = "getPossibleCoos", key = "#p0", unless = "#result=null")
    public List<CooperatorVO> getPossibleCoos(long id) {
        List<AA_Cooperate> aa_cooperateList = aa_cooperateDao.getAA_CooperatesByAuthor1Id(id);
        Map<Author, List<Integer>> authorYears = new HashMap<>();
        aa_cooperateList.forEach(
                aac -> {
                    List<Integer> tmpList = authorYears.get(aac.getAuthor2());
                    if (tmpList == null) {
                        tmpList = new ArrayList<>();
                    }
                    tmpList.add(aac.getYear());
                    authorYears.put(aac.getAuthor2(), tmpList);
                }
        );
        Map<Author, Integer> authorWithYear = new HashMap<>(authorYears.keySet().size());
        authorYears.forEach((key, tmpList) -> {
            tmpList.sort((o1, o2) -> o2 - o1);
            authorWithYear.put(key, tmpList.get(0));
        });
        Map<Author, List<TermItem>> authorWithTerms = new HashMap<>();
        List<Term> terms = termDao.getByAuthor(id);
        Calendar cal = Calendar.getInstance();
        int curYear = cal.get(Calendar.YEAR);
        terms.forEach(
                term -> {
                    List<Author> tmpAuList = authorDao.getAuthorsByKeyword(term.getId());
                    tmpAuList.stream().filter(author -> id != author.getId()
                            && !authorWithTerms.containsKey(author)).forEach(
                            author -> {
                                List<Term> tmpTermList = termDao.getByAuthor(author.getId());
                                tmpTermList.retainAll(terms);
                                List<TermItem> termItems = tmpTermList.stream().map(
                                        t -> new TermItem(t.getId(), t.getContent(), 0)
                                ).collect(Collectors.toList());
                                authorWithTerms.put(author, termItems);
                            }
                    );
                }
        );
        List<CooperatorVO> ansList = new ArrayList<>();
        authorWithYear.forEach((key, year) -> {
            CooperatorVO tmpCoo = generateVoWithoutPossAndTerms(key);
            double poss = (1.2 / (curYear - year + 1)) + 0.8;
            if (authorWithTerms.containsKey(key)) {
                List<TermItem> tmpTerms = authorWithTerms.get(key);
                poss = poss * (tmpTerms.size() + 1);
                tmpCoo.setTermList(tmpTerms);
            } else {
                tmpCoo.setTermList(new ArrayList<>(0));
            }
            tmpCoo.setPossibility(poss);
            ansList.add(tmpCoo);
        });
        authorWithTerms.forEach((key, termItems) -> {
            if (!authorWithYear.containsKey(key)) {
                CooperatorVO tmpCoo = generateVoWithoutPossAndTerms(key);
                double poss = 0.8 * (termItems.size() + 1);
                tmpCoo.setTermList(termItems);
                tmpCoo.setPossibility(poss);
                ansList.add(tmpCoo);
            }
        });
        ansList.sort((o1, o2) -> (int)((o2.getPossibility()-o1.getPossibility())*1000));
        List<CooperatorVO> finalList=new ArrayList<>(ansList);
        return finalList.size()>20? finalList.subList(0,20): finalList;
    }

    /**
     * 生成没有可能性数值和研究方向列表的合作者VO
     *
     * @param key
     * @return
     */
    private CooperatorVO generateVoWithoutPossAndTerms(Author key) {
        CooperatorVO tmpCoo = new CooperatorVO();
        tmpCoo.setId(key.getId());
        tmpCoo.setName(key.getName());
        Affiliation tmpAffi = affiliationDao.getNewestAffiliationByAuthor(key.getId());
        if (tmpAffi == null) {
            tmpCoo.setAffiliationId(-1L);
            tmpCoo.setAffiliation("");
        } else {
            tmpCoo.setAffiliationId(tmpAffi.getId());
            tmpCoo.setAffiliation(tmpAffi.getName());
        }
        return tmpCoo;
    }
}
