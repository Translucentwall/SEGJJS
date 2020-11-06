package edu.nju.se.teamnamecannotbeempty.data.repository;

import edu.nju.se.teamnamecannotbeempty.data.domain.Conference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConferenceDao extends JpaRepository<Conference, Long> {

    Optional<Conference> findByName(String name);

    /**
     * 通过作者的id来获取他参加过的会议
     *
     * @param id 作者的id
     * @return 参加过的会议的列表
     * @前置条件 id不为null
     * @后置条件 无
     */
    @Query(value = "SELECT DISTINCT c.id, c.c_name FROM se3.papers p " +
            "RIGHT JOIN se3.conferences c ON p.conference_id = c.id " +
            "INNER JOIN se3.paper_aa aa ON p.id = aa.paper_id " +
            "WHERE aa.author_id = ?1", nativeQuery = true)
    List<Conference> getConferencesByAuthor(Long id);

    /**
     * 通过机构的id来获取机构参加过的会议
     *
     * @param id 机构的id
     * @return 机构参加过的会议的列表
     * @前置条件 id不为null
     * @后置条件 无
     */
    @Query(value = "SELECT DISTINCT c.id, c.c_name FROM se3.papers p " +
            "RIGHT JOIN se3.conferences c ON p.conference_id = c.id " +
            "INNER JOIN se3.paper_aa aa ON p.id = aa.paper_id " +
            "WHERE aa.affiliation_id = ?1", nativeQuery = true)
    List<Conference> getConferencesByAffiliation(Long id);
}
