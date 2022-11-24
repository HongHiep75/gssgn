package com.itsol.service;

import com.itsol.domain.ControlMethod;
import com.itsol.domain.ResolutionCondition;
import com.itsol.domain.ResponseObject;
import com.itsol.repository.ControlMethodRepository;
import com.itsol.repository.ResolutionConditionRepository;
import com.itsol.service.dto.ControlMethodDTO;
import com.itsol.service.mapper.ControlMapper;
import com.itsol.service.mapper.ResolutionConditionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ControlMethodServiceImp implements  ControlMethodService {

    private final Logger log = LoggerFactory.getLogger(ControlMethodServiceImp.class);
    private final ControlMethodRepository controlMethodRepository;
    private final ControlMapper controlMapper;

    public ControlMethodServiceImp(ControlMethodRepository controlMethodRepository, ControlMapper controlMapper) {
        this.controlMethodRepository = controlMethodRepository;
        this.controlMapper = controlMapper;
    }

    @Override
    public ResponseEntity<ResponseObject> save(ControlMethodDTO controlMethodDTO) {
        Optional<ControlMethod> checkCode = controlMethodRepository.findByCode(controlMethodDTO.getCode());

        // check code có tồn tại và khác với ControlMethod cần update
        if(checkCode.isPresent() && !checkCode.get().getId().equals(controlMethodDTO.getId())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("ERR_001","Cập nhật không thành công",null));
        }
        try{
            ControlMethod controlMethod = controlMethodRepository
                .save(controlMapper.toEntity(controlMethodDTO));
            return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("MSG_001","Cập nhật thành công",controlMethod));
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseObject("MSF_001","Cập nhật không thành công",""));

        }

    }

    @Override
    public ResponseEntity<ResponseObject> getControlMethod(Pageable pageable, ControlMethodDTO searValue) {
        String status = "";
        Page<ControlMethod> list;
        if(searValue.getCode() == null) searValue.setCode("");
        if(searValue.getName() == null) searValue.setName("");
        if(searValue.getStatus() != null) {
            status = searValue.getStatus().toString();
        }
        list = controlMethodRepository.getListControlMethod(pageable, searValue.getCode(),searValue.getName(),status);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok","Thành công",list));
    }
}
