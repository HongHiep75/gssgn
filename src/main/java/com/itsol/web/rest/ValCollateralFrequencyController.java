package com.itsol.web.rest;

import com.itsol.config.Constants;
import com.itsol.domain.ResponseObject;
import com.itsol.service.IStorageSevice;
import com.itsol.service.ValCollateralFrequencyService;
import com.itsol.service.dto.ValCollateralFrequencyDTO;
import com.itsol.service.dto.ValCollateralFrequencyExDTO;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping(Constants.Api.Path.PREFIX)
public class ValCollateralFrequencyController {

    private final ValCollateralFrequencyService valCollateralFrequencyService;

    public ValCollateralFrequencyController(ValCollateralFrequencyService valCollateralFrequencyService) {
        this.valCollateralFrequencyService = valCollateralFrequencyService;
    }

    @Autowired
    private IStorageSevice storageSevice;

    @PutMapping("/val-collateral-frequency/get-list")
    public ResponseEntity<ResponseObject> getAll(@RequestParam int page, @RequestParam int size
                                                , @RequestBody ValCollateralFrequencyDTO searValue){
        page = page < 0?0:page;
        size = size < 0? 50:size;
        Pageable pageable = PageRequest.of(page, size);
        return valCollateralFrequencyService.getList(pageable,searValue);
    }


    @PostMapping("/val-collateral-frequency")
    public ResponseEntity<ResponseObject> addResolutionCondition(@AuthenticationPrincipal UserDetails userDetails
        ,  @RequestBody ValCollateralFrequencyDTO value){
        value.setId(null);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        value.setCreatedDate(timestamp);
        value.setCreatedUser(userDetails.getUsername());
        value.setUpdatedDate(timestamp);
        value.setUpdatedUser(userDetails.getUsername());
        return valCollateralFrequencyService.save(value);
    }

    @PutMapping("/val-collateral-frequency")
    public ResponseEntity<ResponseObject> updateResolutionCondition(
        @AuthenticationPrincipal UserDetails userDetails
                                                ,  @RequestBody ValCollateralFrequencyDTO value){
        if(value.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseObject("false","Id not null",null));
        }
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        value.setUpdatedDate(timestamp);
        value.setUpdatedUser(userDetails.getUsername());
        return valCollateralFrequencyService.update(value);
    }

    @PostMapping("/val-collateral-frequency/ex")
    public ResponseEntity<ResponseObject> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
             String generatedFileName = storageSevice.storaFile(file,new String[]{"xls","xlsx"},25f,false);
            return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("ok", "Thanh cong", generatedFileName));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                .body(new ResponseObject("false", e.getMessage(), ""));

        }
    }

    @PostMapping("/val-collateral-frequency/import-ex")
    public ResponseEntity<ResponseObject> importFileEx( @RequestParam("file") MultipartFile file
        ,@AuthenticationPrincipal UserDetails userDetails) {
            return valCollateralFrequencyService.importEx(file,userDetails.getUsername());
    }

    @GetMapping("/val-collateral-frequency/template-tsdb-ex/{fileName}")
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
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(null);
        }
    }

    @PostMapping("/val-collateral-frequency/template-err-ex/{fileName}")
    public ResponseEntity<InputStreamResource>  exportFileErrEx(@PathVariable String fileName
        ,@RequestBody List<ValCollateralFrequencyExDTO> exDTO) {
       return valCollateralFrequencyService.exportFileErrEx(fileName,exDTO);
    }
}
