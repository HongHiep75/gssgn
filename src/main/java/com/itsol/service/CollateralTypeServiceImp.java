package com.itsol.service;

import com.itsol.domain.CollateralType;
import com.itsol.repository.CollateralTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class CollateralTypeServiceImp implements CollateralTypeService{

    private final Logger log = LoggerFactory.getLogger(CollateralTypeServiceImp.class);
    private final CollateralTypeRepository collateralTypeRepository;

    public CollateralTypeServiceImp(CollateralTypeRepository collateralTypeRepository) {
        this.collateralTypeRepository = collateralTypeRepository;
    }

    @Override
    public List<UUID> checkCodeAndName(String collateralTypeCode, String collateralTypeName) {
        return collateralTypeRepository.checkCodeAndName(collateralTypeCode,collateralTypeName);
    }

    @Override
    public List<CollateralType> getCodeAndName() {
        return collateralTypeRepository.getCodeAndName();
    }

    @Override
    public List<CollateralType> getCollateralTypeNotFrequency() {
        return collateralTypeRepository.getCollateralTypeNotFrequency();
    }
}
