package com.itsol.repository;

import com.itsol.domain.CollateralType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CollateralTypeRepository extends JpaRepository<CollateralType, UUID>{

    @Query("SELECT c.id FROM CollateralType c LEFT JOIN ValCollateralFrequency v" +
        " ON v.collateralType = concat(c.collateralTypeCode, ' - ', c.collateralTypeName) " +
        " WHERE v.id is NULL and c.collateralTypeCode =?1 and c.collateralTypeName =?2")
    List<UUID> checkCodeAndName(String collateralTypeCode, String collateralTypeName);

    @Query("select new CollateralType(c.collateralTypeCode,c.collateralTypeName) from CollateralType c ")
    List<CollateralType> getCodeAndName();

    @Query(value = "SELECT new CollateralType(c.collateralTypeCode,c.collateralTypeName) FROM CollateralType c LEFT JOIN ValCollateralFrequency v\n" +
        "ON v.collateralType = concat(c.collateralTypeCode, ' - ', c.collateralTypeName)\n" +
        "WHERE v.id is NULL")
    List<CollateralType> getCollateralTypeNotFrequency();
}
