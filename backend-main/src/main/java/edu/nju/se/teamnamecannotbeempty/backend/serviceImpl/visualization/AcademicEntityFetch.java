package edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.visualization;

import edu.nju.se.teamnamecannotbeempty.backend.config.parameter.EntityMsg;
import edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.paper.TitleAndId;
import edu.nju.se.teamnamecannotbeempty.backend.vo.*;
import edu.nju.se.teamnamecannotbeempty.data.data_transfer.AffiliationByYear;
import edu.nju.se.teamnamecannotbeempty.data.domain.*;
import edu.nju.se.teamnamecannotbeempty.data.repository.*;
import edu.nju.se.teamnamecannotbeempty.data.repository.popularity.PaperPopDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.popularity.TermPopDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class AcademicEntityFetch {

    private final AffiliationDao affiliationDao;
    private final AuthorDao authorDao;
    private final ConferenceDao conferenceDao;
    private final PaperDao paperDao;
    private final TermPopDao termPopDao;
    private final EntityMsg entityMsg;
    private final PaperPopDao paperPopDao;
    private final TermDao termDao;
    private final FetchForCache fetchForCache;
    private final RefDao refDao;

    @Autowired
    public AcademicEntityFetch(AffiliationDao affiliationDao, AuthorDao authorDao, ConferenceDao conferenceDao,
                               PaperDao paperDao, EntityMsg entityMsg,
                               TermPopDao termPopDao, PaperPopDao paperPopDao, TermDao termDao,
                               FetchForCache fetchForCache,RefDao refDao) {
        this.affiliationDao = affiliationDao;
        this.authorDao = authorDao;
        this.conferenceDao = conferenceDao;
        this.paperDao = paperDao;
        this.entityMsg = entityMsg;
        this.termPopDao = termPopDao;
        this.paperPopDao = paperPopDao;
        this.termDao = termDao;
        this.fetchForCache = fetchForCache;
        this.refDao=refDao;
    }

    @Cacheable(value = "getAcademicEntity", key = "#p0+'_'+#p1")
    public AcademicEntityVO getAcademicEntity(long id, int type) {
        AcademicEntityVO academicEntityVO = null;
        if (type == entityMsg.getAuthorType()) {
            academicEntityVO = authorsEntity(id);
        } else if (type == entityMsg.getAffiliationType()) {
            academicEntityVO = affiliationEntity(id);
        } else if (type == entityMsg.getConferenceType()) {
            academicEntityVO = conferenceEntity(id);
        }
        return academicEntityVO;
    }

    private AcademicEntityVO authorsEntity(long id) {

        List<Affiliation> affiliationList = affiliationDao.getAffiliationsWithPopByAuthor(id);
        List<Conference> conferenceList = conferenceDao.getConferencesByAuthor(id);

        List<AcademicEntityItem> affiEntityItems = generateAffiEntityItems(affiliationList);
        List<AcademicEntityItem> conferenceEntityItems = generateConferenceEntityItems(conferenceList);

        //生成研究方向云图
        List<Term.Popularity> termList=termPopDao.getTermPopByAuthorID(id);

        List<TermItem> termItems = termList.stream().map(
                termPop -> new TermItem(termPop.getTerm().getId(),
                        termPop.getTerm().getContent(), termPop.getPopularity())
        ).collect(Collectors.toList());

        //生成按年份的研究方向列表
        List<Paper> allPapers = fetchForCache.getAllPapersByAuthor(id);
        List<YearlyTerm> yearlyTerms = getYearlyTermList(allPapers);

        //生成引用和被引用关系
        List<TitleAndId> refers=getRefers(allPapers);
        List<TitleAndId> referees=getReferees(allPapers);

        //获取关键词
        Map<Integer,KeywordsYear> year_keywords=new HashMap();
        for(int i=0;i<allPapers.size();i++){
            Paper paper=allPapers.get(i);
            Integer year=paper.getYear();
            if(year==null)
                continue;
            List<Term> terms=paper.getAuthor_keywords();
            if(terms.size()==0)
                continue;
            List<String> keywords=new ArrayList<>();
            for(int j=0;j<terms.size();j++){
                keywords.add(terms.get(j).getContent());
            }
            if(year_keywords.containsKey(year))
                year_keywords.get(year).addKeywords(keywords);
            else
                year_keywords.put(year,new KeywordsYear(year,keywords));
        }
        List<KeywordsYear> keywordsYears=new ArrayList<>();
        for(Integer key:year_keywords.keySet()){
            keywordsYears.add(year_keywords.get(key));
        }
        List<String> keywordPre=new ArrayList<>();
        String finalkeyword="";
        Integer value=0;
        for(KeywordsYear keywordsYear:keywordsYears){
            if((keywordsYear.getCount())>value){
                value=keywordsYear.getCount();
                finalkeyword=keywordsYear.getKeyword();
            }
        }
        keywordPre.add(finalkeyword);
        keywordsYears.add(new KeywordsYear(2021,keywordPre));


        //生成代表作
        List<SimplePaperVO> simplePaperVOS = allPapers.stream().map(
                SimplePaperVO::new).collect(Collectors.toList());
        if(simplePaperVOS.size()>12) {
            simplePaperVOS=simplePaperVOS.subList(0,12);
        }

        //生成总引用数
        int sumCitation = (int)paperDao.getCitationByAuthorId(id);

        //生成热度变化字符串
        List<PopByYear> popByYearList = authorDao.findById(id).orElseGet(Author::new).
                getPops().stream().filter(
                pop->pop.getYear()!=null).collect(
                Collectors.groupingBy(Author.Popularity::getYear)
        ).entrySet().stream().map(en -> new PopByYear(en.getKey(), en.getValue().stream().mapToDouble(
                Author.Popularity::getPopularity
        ).sum())).sorted(Comparator.comparing(PopByYear::getYear)).collect(Collectors.toList());
        String popTrend = generatePopTrend(popByYearList);

        //获得研究者在不同时间的单位情况
        List<AffiliationByYear> affiliationByYearList=authorDao.getAffiliationsOfAuthorByYear(id);
        List<YearlyAffiliation> yearlyAffiliationList=affiliationByYearList.stream().map(
                affiliationByYear -> new YearlyAffiliation(affiliationByYear.getName(),
                        affiliationByYear.getYear(),affiliationByYear.getId())
        ).sorted(Comparator.comparing(YearlyAffiliation::getYear)).collect(Collectors.toList());

        return new AcademicEntityVO(entityMsg.getAuthorType(), id,
                authorDao.findById(id).orElseGet(Author::new).getName(),
                sumCitation, null, affiEntityItems, conferenceEntityItems, termItems,
                simplePaperVOS, yearlyTerms, popTrend,yearlyAffiliationList,keywordsYears,refers,referees);
    }

    private AcademicEntityVO affiliationEntity(long id) {
        Affiliation affiliation=affiliationDao.getOne(id);
        if(affiliation.getName().equals("NA")) {
            return null;
        }

        //生成机构的别名列表
        List<Author> authorList = authorDao.getAuthorsByAffiliation(id);
        List<Conference> conferenceList = conferenceDao.getConferencesByAffiliation(id);

        List<AcademicEntityItem> authorEntityItems = generateAuthorEntityItems(authorList);
        List<AcademicEntityItem> conferenceEntityItems = generateConferenceEntityItems(conferenceList);

        //生成研究方向云图
        List<Term.Popularity> termList=termPopDao.getTermPopByAffiID(id);
        List<TermItem> termItems = termList.stream().map(
                termPop -> new TermItem(termPop.getTerm().getId(), termPop.getTerm().getContent(),
                        termPop.getPopularity())
        ).collect(Collectors.toList());

        //生成按年份的研究方向列表
        List<Paper> allPapers = fetchForCache.getAllPapersByAffi(id);
        List<YearlyTerm> yearlyTerms = getYearlyTermList(allPapers);

        //生成代表作
        List<SimplePaperVO> simplePaperVOS = allPapers.stream().map(
                SimplePaperVO::new).collect(Collectors.toList());
        if(simplePaperVOS.size()>12) {
            simplePaperVOS=simplePaperVOS.subList(0,12);
        }

        //生成总引用数
        int sumCitation = (int) paperDao.getCitationByAffiId(id);

        //生成热度变化字符串
        List<PopByYear> popByYearList = affiliationDao.findById(id).orElseGet(Affiliation::new)
                .getPops().stream()
                .filter(pop->pop.getYear()!=null).collect(
                        Collectors.groupingBy(Affiliation.Popularity::getYear)
                ).entrySet().stream().map(en -> new PopByYear(en.getKey(), en.getValue().stream().mapToDouble(
                        Affiliation.Popularity::getPopularity
                ).sum())).sorted(Comparator.comparing(PopByYear::getYear)).collect(Collectors.toList());
        String popTrend = generatePopTrend(popByYearList);

        return new AcademicEntityVO(entityMsg.getAffiliationType(), id, affiliationDao.findById(id).
                orElseGet(Affiliation::new).getName(),
                sumCitation, authorEntityItems, null, conferenceEntityItems, termItems,
                simplePaperVOS, yearlyTerms, popTrend,null,null);
    }

    private AcademicEntityVO conferenceEntity(long id) {
        Conference conference=conferenceDao.getOne(id);
        if(conference.getName().equals("NA")) {
            return null;
        }

        List<AcademicEntityItem> authorEntityItems = generateAuthorEntityItems(
                authorDao.getAuthorsByConference(id));
        List<AcademicEntityItem> affiEntityItems = generateAffiEntityItems(
                affiliationDao.getAffiliationsByConference(id));
        List<Term.Popularity> termPopularityList = termPopDao.getTermPopByConferenceID(id);

        List<TermItem> termItems = termPopularityList.stream().map(
                termPopularity -> new TermItem(termPopularity.getTerm().getId(),
                        termPopularity.getTerm().getContent(),
                        paperPopDao.getWeightByConferenceOnKeyword(id, termPopularity.getTerm().getId()))
        ).collect(Collectors.toList());

        List<Paper> allPapers = fetchForCache.getAllPapersByConference(id);
        List<YearlyTerm> yearlyTerms = getYearlyTermList(allPapers);

        List<SimplePaperVO> simplePaperVOS = fetchForCache.getAllPapersByConference(id)
                .stream().map(SimplePaperVO::new
                ).collect(Collectors.toList());
        if(simplePaperVOS.size()>12) {
            simplePaperVOS=simplePaperVOS.subList(0,12);
        }

        return new AcademicEntityVO(entityMsg.getConferenceType(), id, conferenceDao.findById(id).
                orElseGet(Conference::new).getName(), -1,
                authorEntityItems, affiEntityItems, null, termItems, simplePaperVOS,
                yearlyTerms, null,null,null);
    }

    private List<AcademicEntityItem> generateAuthorEntityItems(List<Author> authors) {
        List<AcademicEntityItem> academicEntityItems = authors.stream().map(
                author -> new AcademicEntityItem(entityMsg.getAuthorType(), author.getId(),
                        author.getName(), generatePopTrend(
                        author.getPops().stream()
                                .filter(pop->pop.getYear()!=null)
                                .map(pop -> new PopByYear(pop.getYear(), pop.getPopularity())).sorted(
                                Comparator.comparing(PopByYear::getYear)
                        ).collect(Collectors.toList())
                )))
                .collect(Collectors.toList());
        return academicEntityItems.size() > 15 ? academicEntityItems.subList(0, 15) : academicEntityItems;
    }

    private List<AcademicEntityItem> generateAffiEntityItems(List<Affiliation> affiliations) {
        List<AcademicEntityItem> academicEntityItems = affiliations.stream()
                .filter(affiliation -> !affiliation.getName().equals("NA"))
                .map(
                        affiliation -> new AcademicEntityItem(entityMsg.getAffiliationType(),
                                affiliation.getId(),
                                affiliation.getName(), generatePopTrend(
                                affiliation.getPops().stream()
                                        .filter(pop->pop.getYear()!=null)
                                        .map(pop -> new PopByYear(pop.getYear(),
                                                pop.getPopularity())).sorted(
                                        Comparator.comparing(PopByYear::getYear)
                                ).collect(Collectors.toList())
                        )))
                .collect(Collectors.toList());
        return academicEntityItems.size() > 15 ? academicEntityItems.subList(0, 15) : academicEntityItems;
    }

    private List<AcademicEntityItem> generateConferenceEntityItems(List<Conference> conferences) {
        List<AcademicEntityItem> academicEntityItems = conferences.stream()
                .filter(conference -> !conference.getName().equals("NA"))
                .map(
                        conference -> new AcademicEntityItem(entityMsg.getConferenceType(), conference.getId(),
                                conference.getName(), null)) //会议没有热度
                .collect(Collectors.toList());
        return academicEntityItems.size() > 15 ? academicEntityItems.subList(0, 15) : academicEntityItems;
    }

    private List<SimplePaperVO> generateTopPapers(List<Paper.Popularity> paperPopularityList) {
        List<SimplePaperVO> simplePaperVOS = paperPopularityList.stream()
                .map(paprePopularity -> new SimplePaperVO(paprePopularity.getPaper()))
                .collect(Collectors.toList());
        return simplePaperVOS.size() > 5 ? simplePaperVOS.subList(0, 5) : simplePaperVOS;
    }

    public List<Long> getAllAliasIdsOfAuthor(long id, List<Long> results) {
        results.add(id);
        List<Author> aliasList = authorDao.getByAlias_Id(id);
        if (aliasList == null || aliasList.isEmpty()) {
            return results;
        }
        for (Author author : aliasList) {
            results.addAll(getAllAliasIdsOfAffi(author.getId(), new ArrayList<>()));
        }
        return results;
    }

    public List<Long> getAllAliasIdsOfAffi(long id, List<Long> results) {
        results.add(id);
        List<Affiliation> aliasList = affiliationDao.getByAlias_Id(id);
        if (aliasList == null || aliasList.isEmpty()) {
            return results;
        }
        for (Affiliation affiliation : aliasList) {
            results.addAll(getAllAliasIdsOfAffi(affiliation.getId(), new ArrayList<>()));
        }
        return results;
    }

    private List<YearlyTerm> getYearlyTermList(List<Paper> allPapers) {
        Map<Integer, List<Paper>> paperByYear = allPapers.stream().distinct().
                filter(paper -> paper.getDoi()!=null)
                .collect(
                        Collectors.groupingBy(Paper::getYear));
        return paperByYear.entrySet().stream().map(
                en -> new YearlyTerm(en.getKey(), en.getValue().stream().flatMap(
                        paper -> fetchForCache.getTermPopByPaperID(paper.getId()).stream().map(
                                termPop -> new TermItem(termPop.getTerm().getId(),
                                        termPop.getTerm().getContent(), -1)
                        )
                ).distinct().collect(Collectors.toList()))
        ).collect(Collectors.toList());
    }

    private String generatePopTrend(List<PopByYear> popByYearList) {
        if (popByYearList.size() == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        int beginYear = popByYearList.get(0).getYear();
        sb.append(beginYear);
        sb.append(" ");
        sb.append(popByYearList.get(0).getPop());
        beginYear++;
        for (int i = 1; i < popByYearList.size(); beginYear++) {
            sb.append(" ");
            if (popByYearList.get(i).getYear() == beginYear) {
                sb.append(popByYearList.get(i).getPop());
                i++;
            } else {
                sb.append(0);
            }
        }
        return sb.toString();
    }

    static class PopByYear {
        int year;
        double pop;

        public PopByYear(int year, double pop) {
            this.year = year;
            this.pop = pop;
        }

        int getYear() {
            return year;
        }

        double getPop() {
            return pop;
        }
    }

    private List<TitleAndId> getRefers(List<Paper> papers){
        List<TitleAndId> answer=new ArrayList<TitleAndId>();
        List<String> authors=new ArrayList<>();
        for(Paper paper:papers){
            List<Ref> refs=refDao.findByReferer_Id(paper.getId());
            for(Ref ref:refs){
                Paper tmp=ref.getReferee();
                List<Author_Affiliation> author_affiliations=tmp.getAa();
                for(Author_Affiliation author_affiliation:author_affiliations){
                    Author author=author_affiliation.getAuthor();
                    if(author==null)
                        continue;
                    String name=author.getName();
                    if(authors.contains(name))
                        continue;
                    authors.add(name);

                    answer.add(new TitleAndId(author.getName(),author.getId()));
                }
            }
        }
        return answer;
    }

    private List<TitleAndId> getReferees(List<Paper> papers){
        List<TitleAndId> answer=new ArrayList<TitleAndId>();
        List<String> authors=new ArrayList<>();
        for(Paper paper:papers){
            List<Ref> refs=refDao.findByReferee_Id(paper.getId());
            for(Ref ref:refs){
                Paper tmp=ref.getReferee();
                List<Author_Affiliation> author_affiliations=tmp.getAa();
                for(Author_Affiliation author_affiliation:author_affiliations){
                    Author author=author_affiliation.getAuthor();
                    if(author==null)
                        continue;

                    String name=author.getName();
                    if(authors.contains(name))
                        continue;
                    authors.add(name);

                    answer.add(new TitleAndId(author.getName(),author.getId()));
                }
            }
        }
        return answer;
    }
}
