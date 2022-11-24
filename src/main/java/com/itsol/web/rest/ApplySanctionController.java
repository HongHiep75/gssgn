package com.itsol.web.rest;

import com.itsol.config.Constants;
import com.itsol.domain.ResponseObject;
import com.itsol.service.ApplySanctionService;
import com.itsol.service.dto.ApplySanctionDTO;
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
public class ApplySanctionController {

    private final ApplySanctionService applySanctionService;

    public ApplySanctionController(ApplySanctionService applySanctionService) {
        this.applySanctionService = applySanctionService;
    }

    @GetMapping("/apply-sanction")
    public ResponseEntity<ResponseObject> getAll(@RequestParam int page,@RequestParam int size){
        page = page < 0?0:page;
        size = size < 0? 50:size;
        Pageable pageable = PageRequest.of(page, size);
        return applySanctionService.getApplySanction(pageable);
    }

    @PostMapping("/apply-sanction")
    public ResponseEntity<ResponseObject> addApplySanction(@AuthenticationPrincipal UserDetails userDetails
        ,@Valid @RequestBody ApplySanctionDTO applySanctionDTO){
        applySanctionDTO.setId(null);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        applySanctionDTO.setCreatedDate(timestamp);
        applySanctionDTO.setCreatedUser(userDetails.getUsername());
        applySanctionDTO.setUpdatedDate(timestamp);
        applySanctionDTO.setUpdatedUser(userDetails.getUsername());
        return applySanctionService.save(applySanctionDTO);
    }

    @PutMapping("/apply-sanction")
    public ResponseEntity<ResponseObject> updateApplySanction(@AuthenticationPrincipal UserDetails userDetails
        ,@Valid @RequestBody ApplySanctionDTO applySanctionDTO){
        if(applySanctionDTO.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseObject("false","Id not null",null));
        }
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        applySanctionDTO.setUpdatedDate(timestamp);
        applySanctionDTO.setUpdatedUser(userDetails.getUsername());
        int applySanction = applySanctionService
            .updateApplySanction(applySanctionDTO.getApplyDate(),applySanctionDTO.getApplyMonth()
                ,applySanctionDTO.getStatus(),timestamp,userDetails.getUsername()
                ,applySanctionDTO.getId(),applySanctionDTO.getRatingStatus(),applySanctionDTO.getInterestAdjust());
        if(applySanction != 0){
            return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("Ok","Cập nhật thành công",""));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ResponseObject("false","Cập nhật không thành công",null));
    }
}
