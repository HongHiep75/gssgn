package com.itsol.web.rest;

import com.itsol.config.Constants;
import com.itsol.domain.ResponseObject;
import com.itsol.service.ControlMethodService;
import com.itsol.service.dto.ControlMethodDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;

@RestController
@RequestMapping(Constants.Api.Path.PREFIX)
public class ControlMethodController {

    private final Logger log = LoggerFactory.getLogger(ControlMethodController.class);
    private final ControlMethodService controlMethodService;

    public ControlMethodController(ControlMethodService controlMethodService) {
        this.controlMethodService = controlMethodService;
    }

    @PutMapping("control-method/get-list")
    public ResponseEntity<ResponseObject> getAll(@RequestParam int page, @RequestParam int size
                                                                       , @RequestBody ControlMethodDTO searValue){
        page = page < 0?0:page;
        size = size < 0? 50:size;
        Pageable pageable = PageRequest.of(page, size);
        return controlMethodService.getControlMethod(pageable,searValue);
    }

    @PostMapping("/control-method")
    public ResponseEntity<ResponseObject> addResolutionCondition(@AuthenticationPrincipal UserDetails userDetails
                                                               , @Valid @RequestBody ControlMethodDTO controlMethodDTO){
        controlMethodDTO.setId(null);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        controlMethodDTO.setCreatedDate(timestamp);
        controlMethodDTO.setCreatedUser(userDetails.getUsername());
        controlMethodDTO.setUpdatedDate(timestamp);
        controlMethodDTO.setUpdatedUser(userDetails.getUsername());
        return controlMethodService.save(controlMethodDTO);
    }

    @PutMapping("/control-method")
    public ResponseEntity<ResponseObject> updateResolutionCondition(@AuthenticationPrincipal UserDetails userDetails
                                                                   ,@Valid @RequestBody ControlMethodDTO controlMethodDTO){
        if(controlMethodDTO.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseObject("false","Id not null",null));
        }
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        controlMethodDTO.setUpdatedDate(timestamp);
        controlMethodDTO.setUpdatedUser(userDetails.getUsername());
        return controlMethodService.save(controlMethodDTO);
    }



}
