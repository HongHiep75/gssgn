package com.itsol.service;

import com.itsol.domain.ResponseObject;
import com.itsol.service.dto.ControlMethodDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ControlMethodService {

    ResponseEntity<ResponseObject> save(ControlMethodDTO controlMethodDTO);

    ResponseEntity<ResponseObject> getControlMethod(Pageable pageable, ControlMethodDTO searValue);

}
