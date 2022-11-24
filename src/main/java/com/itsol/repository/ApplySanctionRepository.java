package com.itsol.repository;

import com.itsol.domain.ApplySanction;
import com.itsol.domain.enumeration.CommonStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public interface ApplySanctionRepository extends JpaRepository<ApplySanction, UUID> {

    Optional<ApplySanction> findByCode(String code);


    @Query(value = "SELECT a FROM ApplySanction a order by a.updatedDate desc")
    Page<ApplySanction> getApplySanction(Pageable pageable);

    @Modifying
    @Query(value = "update ApplySanction a " +
        "set  a.applyDate = ?1, a.applyMonth = ?2, a.status = ?3, a.updatedDate = ?4, a.updatedUser = ?5,a.ratingStatus =?7,a.interestAdjust=?8 " +
        "where a.id = ?6")
    int updateApplySanction(String applyDate,Integer applyMonth
         , CommonStatus status, Timestamp updatedDate,String updatedUser,UUID id,String ratingStatus,String interestAdjust);

}
