package com.itsol.repository;

import com.itsol.domain.ControlMethod;
import com.itsol.domain.ResolutionCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ControlMethodRepository extends JpaRepository<ControlMethod, UUID> {

    Optional<ControlMethod> findByCode(String code);

    @Query(value = "SELECT r FROM ControlMethod r WHERE (lower(r.code) like '%' ||  lower(:code) || '%' ) " +
        "and (lower(r.name) like '%' ||  lower(:name) || '%' ) and (lower(r.status) = lower(:status) or :status = '' ) order by r.updatedDate desc")
    Page<ControlMethod> getListControlMethod(Pageable pageable,
                                                         @Param("code") String code,
                                                         @Param("name") String name,
                                                         @Param("status") String status
    );

}
