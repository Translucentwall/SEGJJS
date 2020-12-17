import edu.nju.se.teamnamecannotbeempty.data.DataConfig;
import edu.nju.se.teamnamecannotbeempty.data.domain.AA_Cooperate;
import edu.nju.se.teamnamecannotbeempty.data.repository.AA_CooperateDao;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@DataJpaTest
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("daotest")
@ContextConfiguration(classes = DataConfig.class)
public class AA_CooperateDaoTest {

    @Autowired
    AA_CooperateDao aa_cooperateDao;
    @Test
    public void getAA_CooperatesByAuthor1Id(){
        List<AA_Cooperate> aa_cooperates=aa_cooperateDao.
                getAA_CooperatesByAuthor1Id(1L);
        Assert.assertNotNull(aa_cooperates);
        Assert.assertEquals(2,aa_cooperates.size());
    }
}
