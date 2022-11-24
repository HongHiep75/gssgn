package com.itsol.service;

import com.itsol.domain.ResponseObject;
import com.itsol.domain.ValCollateralFrequency;
import com.itsol.service.dto.ValCollateralFrequencyDTO;
import com.itsol.service.dto.ValCollateralFrequencyExDTO;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ValCollateralFrequencyService {
    List<ValCollateralFrequency> getList();
    ResponseEntity<ResponseObject> getList(Pageable pageable, ValCollateralFrequencyDTO valCollateralFrequencyDTO);
    ResponseEntity<ResponseObject> save(ValCollateralFrequencyDTO value);
    List<ValCollateralFrequency> saveAll(List<ValCollateralFrequency> valCollateralFrequencies);
    Optional<ValCollateralFrequency> findById(UUID id);
    ResponseEntity<ResponseObject> update(ValCollateralFrequencyDTO value);
    ResponseEntity<ResponseObject> importEx(MultipartFile file,String userName);
    Set<String> getCollateralType();
    ResponseEntity<InputStreamResource>  exportFileErrEx(String fileName,List<ValCollateralFrequencyExDTO> exDTO);



}
