package com.itsol.service;

import com.itsol.domain.ApplySanction;
import com.itsol.domain.ResponseObject;
import com.itsol.domain.enumeration.CommonStatus;
import com.itsol.service.dto.ApplySanctionDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;


public interface ApplySanctionService {

    ResponseEntity<ResponseObject> getApplySanction(Pageable pageable);
    ResponseEntity<ResponseObject> save(ApplySanctionDTO applySanctionDTO);
    int updateApplySanction(String applyDate, Integer applyMonth
        , CommonStatus status, Timestamp updatedDate, String updatedUser, UUID id,String ratingStatus,String interestAdjust);
}
