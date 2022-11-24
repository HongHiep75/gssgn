package com.itsol.service;

import com.itsol.domain.ResponseObject;
import com.itsol.repository.LoanProductRepository;
import com.itsol.service.mapper.LoanProductMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class LoanProductService {

    private final LoanProductRepository loanProductRepository;
    private final LoanProductMapper loanProductMapper;

    public LoanProductService(LoanProductRepository loanProductRepository, LoanProductMapper loanProductMapper) {
        this.loanProductRepository = loanProductRepository;
        this.loanProductMapper = loanProductMapper;
    }

    public List<String> getListProductNameNotFrequency(){return loanProductRepository.getListProductNameNotFrequency();}
    public Set<String> getListProductType(){ return loanProductRepository.getListProductName();}

}
