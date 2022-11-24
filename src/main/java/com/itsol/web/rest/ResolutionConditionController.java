package com.itsol.web.rest;

import com.itsol.domain.ResponseObject;
import com.itsol.service.ResolutionConditionService;
import com.itsol.service.dto.ResolutionConditionDTO;
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
@RequestMapping("/api/resolution-condition")
public class ResolutionConditionController {

    private final Logger log = LoggerFactory.getLogger(ResolutionConditionController.class);
    private final ResolutionConditionService resolutionConditionService;

    public ResolutionConditionController(ResolutionConditionService resolutionConditionService) {
        this.resolutionConditionService = resolutionConditionService;
    }

    @PutMapping ("/get-list")
    public ResponseEntity<ResponseObject> getAll(@RequestParam int page,@RequestParam int size
        , @RequestBody ResolutionConditionDTO searValue){
        page = page < 0?0:page;
        size = size < 0? 50:size;
        Pageable pageable = PageRequest.of(page, size);
        return resolutionConditionService.getResolutionCondition(pageable,searValue);
    }

    @PostMapping
    public ResponseEntity<ResponseObject> addResolutionCondition(@AuthenticationPrincipal UserDetails userDetails
        ,@Valid @RequestBody ResolutionConditionDTO resolutionConditionDTO){
        resolutionConditionDTO.setId(null);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        resolutionConditionDTO.setCreatedDate(timestamp);
        resolutionConditionDTO.setCreatedUser(userDetails.getUsername());
        resolutionConditionDTO.setUpdatedDate(timestamp);
        resolutionConditionDTO.setUpdatedUser(userDetails.getUsername());
        return resolutionConditionService.save(resolutionConditionDTO);
    }

    @PutMapping
    public ResponseEntity<ResponseObject> updateResolutionCondition(@AuthenticationPrincipal UserDetails userDetails
        , @Valid @RequestBody ResolutionConditionDTO resolutionConditionDTO){
        if(resolutionConditionDTO.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(new ResponseObject("false","Id not null",null));
        }
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        resolutionConditionDTO.setUpdatedDate(timestamp);
        resolutionConditionDTO.setUpdatedUser(userDetails.getUsername());
        return resolutionConditionService.save(resolutionConditionDTO);
    }

}
