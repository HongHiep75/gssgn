package com.itsol.web.rest;

import com.itsol.domain.ResponseObject;
import com.itsol.service.IStorageSevice;
import com.itsol.service.LoanProductConditionService;
import com.itsol.service.LoanProductService;
import com.itsol.service.dto.LoanProductConditionDTO;
import com.itsol.service.dto.LoanProductConditionExDTO;
import com.itsol.web.rest.errors.BadRequestAlertException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;


@RestController
@RequestMapping("/api")
public class LoanProductConditionController {
    private final Logger log = LoggerFactory.getLogger(LoanProductConditionController.class);

    private static final String ENTITY_NAME = "LoanProductCondition";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LoanProductConditionService productConditionService;
    private final LoanProductService loanProductTypeService;

    @Autowired
    private IStorageSevice storageSevice;

    public LoanProductConditionController(LoanProductConditionService productConditionService, LoanProductService loanProductTypeService) {
        this.productConditionService = productConditionService;
        this.loanProductTypeService = loanProductTypeService;
    }

    @PostMapping("/product-condition")
    public ResponseEntity<LoanProductConditionDTO> createProductCondition(@RequestBody LoanProductConditionDTO loanProductConditionDTO
        , @AuthenticationPrincipal UserDetails userDetails) throws URISyntaxException {
        String userName = userDetails.getUsername();
        if (loanProductConditionDTO.getId() != null) {
            throw new BadRequestAlertException("A new LoanProductCondition cannot already have an ID", ENTITY_NAME, "idexists");
        }
       return productConditionService.save(userName, loanProductConditionDTO);
    }

    @PutMapping("/product-condition")
    public ResponseEntity<LoanProductConditionDTO> editProductCondition(@RequestBody LoanProductConditionDTO loanProductConditionDTO
        , @AuthenticationPrincipal UserDetails userDetails) throws URISyntaxException{
        String userName = userDetails.getUsername();
        if (loanProductConditionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "id null");
        }
        return productConditionService.saveUpdate(userName, loanProductConditionDTO);
    }

    @PutMapping("/product-condition/get-list-product-condition")
    public ResponseEntity<ResponseObject> getAll(@RequestParam int page, @RequestParam int size
                                               , @RequestBody LoanProductConditionDTO searchValue) {
        page = page < 0 ? 0 : page;
        size = size < 0 ? 50 : size;
        Pageable pageable = PageRequest.of(page, size);
        return productConditionService.getLoanProductCondition(pageable, searchValue);
    }

    @GetMapping("/product-condition/get-list-product-type")
    public ResponseEntity<ResponseObject> getListProductType(){
        return ResponseEntity.status(HttpStatus.OK)
            .body(new ResponseObject("ok", "Thanh cong", loanProductTypeService.getListProductType()));
    }

    @PostMapping("/product-condition/excel")
    public ResponseEntity<ResponseObject> uploadFile(@RequestParam("file") MultipartFile file){
        try {
            String generatedFileName = storageSevice.storaFile(file,new String[]{"xls","xlsx"},25f,false);
            return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("ok", "Thanh cong", generatedFileName));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                .body(new ResponseObject("false", e.getMessage(), ""));
        }
    }

    @PostMapping("/product-condition/import-excel")
    public ResponseEntity<ResponseObject> importFileExcel(@RequestParam("file") MultipartFile file
        ,@AuthenticationPrincipal UserDetails userDetails){
        return productConditionService.importExcel(file, userDetails.getUsername());
    }

    @GetMapping("/product-condition/template-dksp-ex/{fileName}")
    public ResponseEntity<InputStreamResource>  exportFileEx(@PathVariable String fileName) {
        try {
            Workbook workbook;
            if(fileName.endsWith(".xlsx"))workbook = new XSSFWorkbook(storageSevice.getResource(fileName).getInputStream());
            else workbook = new HSSFWorkbook(storageSevice.getResource(fileName).getInputStream());

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
            workbook.close();
            return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(new InputStreamResource(in));
        }catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(null);
        }
    }

    @PostMapping("/product-condition/template-err-ex/{fileName}")
    public ResponseEntity<InputStreamResource>  exportFileErrEx(@PathVariable String fileName
        ,@RequestBody List<LoanProductConditionExDTO> exDTO) {
        return productConditionService.exportFileErrEx(fileName,exDTO);
    }
}
