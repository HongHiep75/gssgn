package com.itsol.service;

import com.itsol.domain.CollateralType;

import java.util.List;
import java.util.UUID;

public interface CollateralTypeService {
    List<UUID> checkCodeAndName(String collateralTypeCode, String collateralTypeName);
    List<CollateralType> getCodeAndName();
    List<CollateralType> getCollateralTypeNotFrequency();
}
