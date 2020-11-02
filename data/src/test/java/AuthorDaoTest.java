import edu.nju.se.teamnamecannotbeempty.data.DataConfig;
import edu.nju.se.teamnamecannotbeempty.data.data_transfer.AffiliationByYear;
import edu.nju.se.teamnamecannotbeempty.data.domain.Author;
import edu.nju.se.teamnamecannotbeempty.data.repository.AuthorDao;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.util.Streamable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@DataJpaTest
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("daotest")
@ContextConfiguration(classes = DataConfig.class)
public class AuthorDaoTest {
    @Autowired
    private AuthorDao authorDao;

    @Test
    public void findByName() {
        Optional<Author> result = authorDao.findByName("a");
        assertTrue(result.isPresent());
        assertEquals("a", result.get().getName());
    }

    @Test
    public void findByName_notExist() {
        Optional<Author> result = authorDao.findByName("x");
        assertFalse(result.isPresent());
    }

    @Test
    public void getAuthorsByAffiliation() {
        List<Author> authors = authorDao.getAuthorsByAffiliation(2L);
        assertNotNull(authors);
        assertEquals(2, authors.size());
        MatcherAssert.assertThat(authors, allOf(
                hasItem(hasProperty("id", is(2L))),
                hasItem(hasProperty("id", is(3L)))
        ));
    }

    @Test
    public void getAuthorsByConference() {
        List<Author> authors = authorDao.getAuthorsByConference(2L);
        assertNotNull(authors);

    }

    @Test
    public void getAffiliationsOfAuthorByYear(){
        List<AffiliationByYear> affiliationByYearList=authorDao.getAffiliationsOfAuthorByYear(1L);
        assertNotNull(affiliationByYearList);
        assertEquals(Integer.valueOf(2012),affiliationByYearList.get(0).getYear());
        assertEquals(Integer.valueOf(2020),affiliationByYearList.get(1).getYear());
    }
}