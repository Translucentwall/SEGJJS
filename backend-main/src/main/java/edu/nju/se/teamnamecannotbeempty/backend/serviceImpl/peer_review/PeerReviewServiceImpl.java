package edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.peer_review;

import edu.nju.se.teamnamecannotbeempty.backend.config.parameter.PeerReviewMsg;
import edu.nju.se.teamnamecannotbeempty.backend.service.peer_review.PeerReviewService;
import edu.nju.se.teamnamecannotbeempty.backend.vo.InformationReviewed;
import edu.nju.se.teamnamecannotbeempty.backend.vo.ResponseVO;

import edu.nju.se.teamnamecannotbeempty.data.domain.Affiliation;
import edu.nju.se.teamnamecannotbeempty.data.domain.Author;
import edu.nju.se.teamnamecannotbeempty.data.domain.Term;
import edu.nju.se.teamnamecannotbeempty.data.repository.AffiliationDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.AuthorDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.TermDao;
import edu.nju.se.teamnamecannotbeempty.data.repository.popularity.AuthorPopDao;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author laiba
 */
@Service
public class PeerReviewServiceImpl implements PeerReviewService {

    private final EntityManager entityManager;
    private final TermDao termDao;
    private final PeerReviewMsg peerReviewMsg;
    private final AuthorDao authorDao;
    private final AffiliationDao affiliationDao;
    private final AuthorPopDao authorPopDao;

    @Autowired
    public PeerReviewServiceImpl(EntityManager entityManager, TermDao termDao,
                                 PeerReviewMsg peerReviewMsg, AuthorDao authorDao,
                                 AffiliationDao affiliationDao, AuthorPopDao authorPopDao) {
        this.entityManager = entityManager;
        this.termDao=termDao;
        this.peerReviewMsg=peerReviewMsg;
        this.authorDao=authorDao;
        this.affiliationDao=affiliationDao;
        this.authorPopDao=authorPopDao;
    }


    @Override
    public List<String> searchTerm(String prefix) {
        FullTextEntityManager fullTextEntityManager=Search.getFullTextEntityManager(entityManager);
        QueryBuilder queryBuilder=fullTextEntityManager.getSearchFactory().buildQueryBuilder()
                .forEntity(Term.class).get();
        Query luceneQuery=queryBuilder.keyword().fuzzy().withEditDistanceUpTo(2)
                .withPrefixLength(0).onField("content").matching(prefix).createQuery();
        FullTextQuery fullTextQuery=fullTextEntityManager.createFullTextQuery(luceneQuery,Term.class);
        fullTextQuery.setSort(Sort.RELEVANCE);
        int total=fullTextQuery.getResultSize();
        int maxResult= Math.min(total, 10);
        fullTextQuery.setFirstResult(0);
        fullTextQuery.setMaxResults(maxResult);
        List<Term> termList=fullTextQuery.getResultList();
        return termList.stream().map(Term::getContent).collect(Collectors.toList());
    }

    @Override
    public ResponseVO recommendReviewer(InformationReviewed inf) {
        if(inf==null||inf.getTerm()==null||inf.getAuthor()==null||inf.getAffiliation()==null){
            return ResponseVO.fail(peerReviewMsg.getNullParam());
        }

        String termName=inf.getTerm();
        String authorName=inf.getAuthor();
        String affiliationName=inf.getAffiliation();

        Optional<Term> termOpt=termDao.findByContent(termName);
        if(!termOpt.isPresent()){
            return ResponseVO.fail(peerReviewMsg.getInvalidTerm());
        }
        Term term=termOpt.get();
        List<Author> authorList=authorDao.getAuthorsByKeyword(term.getId());
        Set<Author> authorSet=new HashSet<>(authorList);

        List<Author> blockAuthorsList=new ArrayList<>(0);



        Optional<Author> authorOpt=authorDao.findByName(authorName);
        if(authorOpt.isPresent()) {
            Author author=authorOpt.get();
            blockAuthorsList.add(author);
            Calendar date = Calendar.getInstance();
            int currentYear = date.get(Calendar.YEAR);
            blockAuthorsList.addAll(authorDao.
                    getAuthorByCooAndStartYear(author.getId(),currentYear-3));
        }
        Optional<Affiliation> affiliationOpt=affiliationDao.findByName(affiliationName);
        if(affiliationOpt.isPresent()){
            Affiliation affiliation=affiliationOpt.get();
            List<Author> tempBlockAuthor=authorDao.getAuthorByAffiWithoutPop(affiliation.getId());
            for(Author author:tempBlockAuthor){
                Affiliation tempAffi=affiliationDao.getNewestAffiliationByAuthor(author.getId());
                if(tempAffi==null||!tempAffi.getName().equals(affiliationName)){
                    blockAuthorsList.add(author);
                }
            }
        }
        authorSet.removeAll(blockAuthorsList);
        List<AuthorWithPop> authorWithPops=authorSet.stream().map(
                author -> {
                    Optional<Author.Popularity> apPop=authorPopDao.findByAuthor_Id(author.getId());
                    double pop=0;
                    if(apPop.isPresent()) {
                        pop=apPop.get().getPopularity();
                    }
                    return new AuthorWithPop(author,pop);
                }).sorted((o1, o2) -> (int)(o2.pop-o1.pop)*100).collect(Collectors.toList());
        ResponseVO responseVO=ResponseVO.success();
        responseVO.setContent(authorWithPops.stream().
                map(AuthorWithPop::getAuthor).collect(Collectors.toList()));
        return responseVO;
    }

    class AuthorWithPop{
        private Author author;
        private double pop;
        AuthorWithPop(Author author, double pop){
            this.author=author;
            this.pop=pop;
        }

        public Author getAuthor() {
            return author;
        }

        public void setAuthor(Author author) {
            this.author = author;
        }

        public double getPop() {
            return pop;
        }

        public void setPop(double pop) {
            this.pop = pop;
        }
    }

}
