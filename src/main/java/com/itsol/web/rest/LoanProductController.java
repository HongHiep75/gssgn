package com.itsol.web.rest;

import com.itsol.config.Constants;
import com.itsol.domain.ResponseObject;
import com.itsol.service.LoanProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(Constants.Api.Path.PREFIX)
public class LoanProductController {
    private static final String API_NAME = "/loanProduct";
    private final LoanProductService loanProductService;

    public LoanProductController(LoanProductService loanProductService) {
        this.loanProductService = loanProductService;
    }

    @GetMapping(API_NAME +"/product-name-not-fequency")
    public ResponseEntity<ResponseObject> getCollateralTypeNotFrequency(){
        List<String> list = loanProductService.getListProductNameNotFrequency();
        return ResponseEntity.status(HttpStatus.OK)
            .body(new ResponseObject("ok","ok"
                ,list.size()));
    }

    @GetMapping(API_NAME +"/product-name")
    public ResponseEntity<ResponseObject> getAll(){
        return ResponseEntity.status(HttpStatus.OK)
            .body(new ResponseObject("ok","Thành công",loanProductService.getListProductType()));
    }
}
