package com.itsol.service;

import com.itsol.domain.ResolutionCondition;
import com.itsol.domain.ResponseObject;
import com.itsol.repository.ResolutionConditionRepository;
import com.itsol.service.dto.ResolutionConditionDTO;
import com.itsol.service.mapper.ResolutionConditionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Null;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResolutionConditionService {

    private final Logger log = LoggerFactory.getLogger(ResolutionConditionService.class);
    private final ResolutionConditionRepository resolutionConditionRepository;
    private final ResolutionConditionMapper resolutionConditionMapper;

    public ResolutionConditionService(ResolutionConditionRepository resolutionConditionRepository, ResolutionConditionMapper resolutionConditionMapper) {
        this.resolutionConditionRepository = resolutionConditionRepository;
        this.resolutionConditionMapper = resolutionConditionMapper;
    }

    public ResponseEntity<ResponseObject> save(ResolutionConditionDTO resolutionConditionDTO){
       List<ResolutionCondition> checkCode = resolutionConditionRepository
                                             .findByCode(resolutionConditionDTO.getCode());
       if(!checkCode.isEmpty()){
           List<ResolutionCondition> checkCodeUpdate = checkCode.stream()
                                                      .filter(resolution ->resolution.getId().equals(resolutionConditionDTO.getId()))
                                                      .collect(Collectors.toList());
           if(checkCodeUpdate.size() != 1){
               return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                   .body(new ResponseObject("ERR_001","Cập nhật không thành công",null));
           }

       }
       try{
           ResolutionCondition resolutionCondition = resolutionConditionRepository
               .save(resolutionConditionMapper.toEntity(resolutionConditionDTO));
           return ResponseEntity.status(HttpStatus.OK)
               .body(new ResponseObject("MSG_001","Cập nhật thành công",resolutionCondition));
       }catch (Exception e){
           log.error(e.getMessage());
           return ResponseEntity.status(HttpStatus.BAD_REQUEST)
               .body(new ResponseObject("MSF_001","Cập nhật không thành công",""));

       }
    }

    public ResponseEntity<ResponseObject> getResolutionCondition(Pageable pageable, ResolutionConditionDTO searValue){
        String status = "";
        Page<ResolutionCondition> list;
        if(searValue.getCode() == null) searValue.setCode("");
        if(searValue.getName() == null) searValue.setName("");
        if(searValue.getStatus() != null) status = searValue.getStatus().toString();

        list = resolutionConditionRepository.getListResolutionCondition(pageable, searValue.getCode(),searValue.getName(),status);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok","Thành công",list));
    }
}
