package edu.nju.se.teamnamecannotbeempty.data.repository;

import edu.nju.se.teamnamecannotbeempty.data.domain.AA_Cooperate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AA_CooperateDao extends JpaRepository<AA_Cooperate, Long> {

    /**
     * 返回跟该作者合作过的所有作者及最新的合作年份
     * @param author1Id
     * @return
     */
    @Query("select aac from AA_Cooperate aac where aac.author1.id=?1")
    List<AA_Cooperate> getAA_CooperatesByAuthor1Id(Long author1Id);


}
