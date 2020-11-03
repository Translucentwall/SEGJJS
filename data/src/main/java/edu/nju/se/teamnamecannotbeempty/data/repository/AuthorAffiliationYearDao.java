package edu.nju.se.teamnamecannotbeempty.data.repository;

import edu.nju.se.teamnamecannotbeempty.data.domain.AuthorAffiliationYear;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorAffiliationYearDao extends JpaRepository<AuthorAffiliationYear,Long> {
}
