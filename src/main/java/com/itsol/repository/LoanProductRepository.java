package com.itsol.repository;


import com.itsol.domain.LoanProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface LoanProductRepository extends JpaRepository<LoanProduct, UUID> {
    @Query("SELECT p.productName FROM LoanProduct p ")
    Set<String> getListProductName();

    @Query("SELECT c.productName FROM LoanProduct c LEFT JOIN LoanProductCondition v" +
        " ON v.productType = c.productName WHERE v.id is NULL")
    List<String> getListProductNameNotFrequency();



}
