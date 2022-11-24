package com.itsol.web.rest;

import com.itsol.config.Constants;
import com.itsol.domain.ResponseObject;
import com.itsol.service.CollateralTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(Constants.Api.Path.PREFIX)
public class CollateralTypeController {

    @Autowired
    CollateralTypeService collateralTypeService;

    @GetMapping("collateral-type/code-name")
    public ResponseEntity<ResponseObject> getCodeAndName(){
        List<String> list =
            collateralTypeService.getCodeAndName().parallelStream()
                .map(value -> value.getCollateralTypeCode() + " - " + value.getCollateralTypeName()).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK)
            .body(new ResponseObject("ok","ok"
                ,list));
    }

    @GetMapping("collateral-type/code-name-not-fequency")
    public ResponseEntity<ResponseObject> getCollateralTypeNotFrequency(){
        List<String> list =
            collateralTypeService.getCollateralTypeNotFrequency().parallelStream()
                .map(value -> value.getCollateralTypeCode() + " - " + value.getCollateralTypeName()).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK)
            .body(new ResponseObject("ok","ok"
                ,list));
    }

}
