package com.itsol.repository;

import com.itsol.domain.ResolutionCondition;
import com.itsol.domain.ValCollateralFrequency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;
import java.util.UUID;

public interface ValCollateralFrequencyRepository extends JpaRepository<ValCollateralFrequency, UUID> {


    @Query(value = "SELECT r FROM ValCollateralFrequency r WHERE (lower(r.collateralType) like '%' ||  lower(:collateralType) || '%' ) " +
        "and (r.frequency = :frequency or :frequency = -1)" +
        " and (lower(r.status) = lower(:status) or :status = '' ) order by r.updatedDate desc ")
    Page<ValCollateralFrequency> getList(Pageable pageable,
                                                         @Param("collateralType") String collateralType,
                                                         @Param("frequency") int frequency,
                                                         @Param("status") String status
    );

    @Query(value = "SELECT r.collateralType FROM ValCollateralFrequency r")
    Set<String> getCollateralType();

}
