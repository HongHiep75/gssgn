package com.itsol.repository;

import com.itsol.domain.LoanProductCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
@Repository
public interface LoanProductConditionRepository extends JpaRepository<LoanProductCondition, UUID> {

    @Query(value = "SELECT pc from LoanProductCondition pc where" +
        " (lower(pc.code) like '%' || lower(:code) || '%')  " +
        "and (lower(pc.productType) like '%' || lower(:productType) || '%' ) " +
        "and (lower(pc.status) = lower(:status) or :status = '' ) order by pc.updateDate desc")
    Page<LoanProductCondition> getListLoanProductCondition(Pageable pageable,
                                                          @Param("code") String code,
                                                          @Param("productType") String productType,
                                                          @Param("status") String status);

    @Query("SELECT pc from LoanProductCondition pc where lower(pc.code) like lower(:code)")
    Optional<LoanProductCondition> getLoanProductConditionByCode(@Param("code") String code);

    @Query("SELECT pc.code from LoanProductCondition pc")
    Set<String> getAllCode();

}
