package com.itsol.service;

import com.itsol.config.Constants;
import com.itsol.domain.LoanProductCondition;
import com.itsol.domain.ResponseObject;
import com.itsol.domain.enumeration.CommonStatus;
import com.itsol.repository.LoanProductConditionRepository;
import com.itsol.repository.LoanProductRepository;
import com.itsol.service.dto.LoanProductConditionDTO;
import com.itsol.service.dto.LoanProductConditionExDTO;
import com.itsol.service.mapper.LoanProductConditionMapper;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

@Service
@Transactional
public class LoanProductConditionService {
    private final Logger log = LoggerFactory.getLogger(LoanProductConditionService.class);
    private final LoanProductConditionRepository loanProductRepo;
    private final LoanProductRepository loanProductTypeRepo;
    private final LoanProductService loanProductService;
    private final LoanProductConditionMapper loanProductMapper;
    private final IStorageSevice storageSevice;

    private final int STT_CELL_NUMBER = 0;
    private final int CODE_NUMBER = 1;
    private final int PRODUCT_TYPE_NUMBER = 2;
    private final int SLIP_CODE_NUMBER = 3;
    private final int DAY_NUMBER = 4;
    private final int FREQUENCY_NUMBER = 5;
    private final int FREQUENCY_UNIT_NUMBER = 6;
    private final int NOTE_NUMBER = 7;
    private final int SANCTION_NUMBER = 8;
    private final int ERR_CELL_NUMBER = 9;
    private final int ROW_START = 5;

    public LoanProductConditionService(LoanProductConditionRepository loanProductRepo
                                     , LoanProductConditionMapper loanProductMapper,
                                       IStorageSevice storageSevice, LoanProductRepository loanProductTypeRepo
                                       , LoanProductService loanProductService) {
        this.loanProductRepo = loanProductRepo;
        this.loanProductMapper = loanProductMapper;
        this.storageSevice = storageSevice;
        this.loanProductTypeRepo = loanProductTypeRepo;
        this.loanProductService = loanProductService;
    }

    public ResponseEntity save(String userName, LoanProductConditionDTO productConditionDTO) {
        log.debug("Request to save LoanProductCondition : {}", productConditionDTO);
        Optional<LoanProductCondition> checkCode = loanProductRepo.getLoanProductConditionByCode(productConditionDTO.getCode());
        if (checkCode.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("ERR_001", "Trường thông tin đã tồn tại", null));
        }
        LoanProductCondition productCondition = loanProductMapper.toEntity(productConditionDTO);
        productCondition.setCreateUser(userName);
        productCondition.setUpdateUser(userName);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        productCondition.setCreateDate(timestamp);
        productCondition.setUpdateDate(timestamp);
        productCondition = loanProductRepo.save(productCondition);
        return ResponseEntity.status(HttpStatus.OK)
            .body(new ResponseObject("200", "Cập nhật thành công", loanProductMapper.toDto(productCondition)));
    }

    public ResponseEntity saveUpdate(String userName, LoanProductConditionDTO productConditionDTO) {
        Optional<LoanProductCondition> checkCode = loanProductRepo.getLoanProductConditionByCode(productConditionDTO.getCode());
        if (checkCode.isPresent() && !checkCode.get().getId().equals(productConditionDTO.getId())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("ERR_001", "Trường thông tin đã tồn tại", null));
        }
        LoanProductCondition productCondition = loanProductMapper.toEntity(productConditionDTO);
        productCondition.setUpdateUser(userName);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        productCondition.setUpdateDate(timestamp);
        productCondition = loanProductRepo.save(productCondition);
        return ResponseEntity.status(HttpStatus.OK)
            .body(new ResponseObject("200", "Cập nhật thành công", loanProductMapper.toDto(productCondition)));
    }

    @Transactional(readOnly = true)
    public Page<LoanProductConditionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LoanProductCondition");
        return loanProductRepo.findAll(pageable)
            .map(loanProductMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<LoanProductConditionDTO> findOne(UUID id) {
        log.debug("Request to get LoanProductCondition : {}", id);
        return loanProductRepo.findById(id).map(loanProductMapper::toDto);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ResponseObject> getLoanProductCondition(Pageable pageable, LoanProductConditionDTO searchValue) {
        String status = "";
        Page<LoanProductCondition> list;
        if (searchValue.getCode() == null) searchValue.setCode("");
        if (searchValue.getProductType() == null) searchValue.setProductType("");
        if (searchValue.getStatus() != null) status = searchValue.getStatus();
        list = loanProductRepo.getListLoanProductCondition(pageable, searchValue.getCode(), searchValue.getProductType(), status);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok", "Thành công", list));
    }

    private Workbook getWorkBook(String fileName) throws IOException {
            if (fileName.endsWith(".xlsx")) return new XSSFWorkbook(storageSevice.getResource(fileName).getInputStream());
            return new HSSFWorkbook(storageSevice.getResource(fileName).getInputStream());
    }

    // Get cell value
    private static String getCellValue(Cell cell) {
        CellType cellType = cell.getCellType();
        Object cellValue = null;
        switch (cellType) {
            case NUMERIC:
                cellValue = new BigDecimal((int) cell.getNumericCellValue()).intValue() + "";
                break;
            case BOOLEAN:
            case STRING:
                cellValue = cell.getStringCellValue();
                break;
            case _NONE:
            case BLANK:
            case ERROR:
                break;
            default:
                break;
        }
        if(cellValue == null) return  null;
        return cellValue.toString();
    }

    //Validate file
    private ResponseEntity<ResponseObject> validateFileEx(MultipartFile file) {
        List<String> messErr = new LinkedList<>();
        if (storageSevice.validateTypeFile(file, new String[]{".xls,.xlsx"})) messErr.add("only file .xls,.xlsx");
        if (storageSevice.validateSizeFile(file, 25.0f)) messErr.add("file <= 25mb");
        if (messErr.isEmpty()) return null;
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ResponseObject("false", "import không thành công", messErr));
    }

    // Validate number
    private String validateNumber(String value,String element,Boolean biggerO) {
        if (value == null) return element + " không được để trống. ";
        Pattern checkNumber;
         String messAdd = "";
         if(biggerO) {
             checkNumber =  Pattern.compile(Constants.NUMBER_BIGGER_O);
             messAdd = ",lớn hơn 0";
         }
         else checkNumber = Pattern.compile(Constants.NUMBER);
        if (checkNumber.matcher(value).find()) return null;
        return element + " là gồm các số 0-9" + messAdd + " tối đa 10 ký tự. ";
    }
    //Validate đơn vị tần suất
    private String validateFrequencyUnit(String value){
        if(value == null)return "Đơn vị tần xuất không được để trống. ";
        if(value.equals("DAY") || value.equals("MONTH")) return null;
        return "Đơn vị tần xuất chỉ nhập DAY (ngày) hoặc MONTH (tháng). ";
    }

    private String validateString(String value, String element,int size, Boolean isNull){
        if(isNull && value == null) return  element + " không được để trống. ";
        if(value != null && value.length() > size)  return  element + "không vượt quá " + size + " ký tự. ";
        return null;
    }
    //Validate Code
    private String validateCode(String value){
        if (value == null) return  "Mã điều kiện không được để trống. ";
        Pattern checkCode = Pattern.compile(Constants.CODE_REGEX);
        if (checkCode.matcher(value).find()) return null;
        return "Mã điều kiện chỉ gồm các ký tự tự in hoa A-Z, số 0-9, _, - tối đa 255 ký tự. ";
    }
    //Validate productType cần chỉnh sửa
    private String validateProductType(String value,Set<String> listProductType){
        if (value == null) return "Loại sản phẩm không được để trống. ";
        if(listProductType.contains(value)) return null;
        return "Loại sản phẩm không có trong hệ thống. ";
    }

    //Get list productCondition
    private List<LoanProductConditionExDTO> getListFromExcel(Sheet sheet, int skip, int limit) {
        List<LoanProductConditionExDTO> dtoList = new LinkedList<>();
        for (int i = skip; i < limit; i++) {
            LoanProductConditionExDTO exDto = new LoanProductConditionExDTO();
            Cell cellCode = sheet.getRow(i).getCell(CODE_NUMBER);
            Cell cellProductType = sheet.getRow(i).getCell(PRODUCT_TYPE_NUMBER);
            Cell cellSlipCode = sheet.getRow(i).getCell(SLIP_CODE_NUMBER);
            Cell cellDay = sheet.getRow(i).getCell(DAY_NUMBER);
            Cell cellFrequency = sheet.getRow(i).getCell(FREQUENCY_NUMBER);
            Cell cellFrequencyUnit = sheet.getRow(i).getCell(FREQUENCY_UNIT_NUMBER);
            Cell cellNote = sheet.getRow(i).getCell(NOTE_NUMBER);
            Cell cellSanction = sheet.getRow(i).getCell(SANCTION_NUMBER);

            boolean check = cellCode == null || cellProductType == null || cellSlipCode == null
                || cellDay == null || cellFrequency == null || cellFrequencyUnit == null || cellNote == null || cellSanction == null;
            if(check) continue;

            //get value from cell
            String codeValue = getCellValue(cellCode);
            String productTypeValue = getCellValue(cellProductType);
            String slipCodeValue = getCellValue(cellSlipCode);
            String dayValue = getCellValue(cellDay);
            String frequencyValue = getCellValue(cellFrequency);
            String frequencyUnitValue = getCellValue(cellFrequencyUnit);
            String noteValue = getCellValue(cellNote);
            String sanctionValue = getCellValue(cellSanction);

            // row khong co value bo qua
            boolean isEmpty = codeValue == null && frequencyValue == null && frequencyUnitValue == null && productTypeValue == null;
            if (isEmpty) continue;
            exDto.setCode(codeValue);
            exDto.setProductType(productTypeValue);
            exDto.setSlipCode(slipCodeValue);
            exDto.setDay(dayValue);
            exDto.setFrequency(frequencyValue);
            exDto.setFrequencyUnit(frequencyUnitValue);
            exDto.setNote(noteValue);
            exDto.setSanction(sanctionValue);
            dtoList.add(exDto);
        }
        return dtoList;
    }

    private List<LoanProductConditionExDTO> getSheetError(List<LoanProductConditionExDTO> dtoList, String userName){
        List<LoanProductCondition> listSuccess = new LinkedList<>();
        List<LoanProductConditionExDTO> listError = new LinkedList<>();
        dtoList = validateListDTO(dtoList);
        dtoList.forEach(value ->{
            if(value.getErrMess().equals("")) listSuccess.add(createLoanProductCondition(value,userName));
            else listError.add(value);
        });
        if(!listSuccess.isEmpty()) saveAll(listSuccess);
        return  listError;
    }

    private LoanProductCondition createLoanProductCondition(LoanProductConditionExDTO value,String userName){
        LoanProductCondition loanProductCondition = new LoanProductCondition();
        loanProductCondition.setCode(value.getCode());
        loanProductCondition.setProductType(value.getProductType());
        loanProductCondition.setSlipCode(value.getSlipCode());
        loanProductCondition.setDay(Integer.parseInt(value.getDay()));
        loanProductCondition.setFrequency(Integer.parseInt(value.getFrequency()));
        loanProductCondition.setFrequencyUnit(value.getFrequencyUnit());
        loanProductCondition.setNote(value.getNote());
        loanProductCondition.setSanction(value.getSanction());
        loanProductCondition.setCreateUser(userName);
        loanProductCondition.setUpdateUser(userName);
        loanProductCondition.setCreateDate(new Timestamp(System.currentTimeMillis()));
        loanProductCondition.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        loanProductCondition.setStatus(CommonStatus.ACTIVE.toString());
        return loanProductCondition;
    }

    public List<LoanProductCondition> saveAll(List<LoanProductCondition> loanProductConditions){
        return loanProductRepo.saveAll(loanProductConditions);
    }

    public ResponseEntity<ResponseObject> importExcel(MultipartFile file, String userName){
        ResponseEntity<ResponseObject> validate = validateFileEx(file);
        if(validate  != null) return validate;
        try {
            Workbook wb;
            String filename = file.getOriginalFilename();
            if(filename.endsWith(".xlsx")) wb = new XSSFWorkbook(file.getInputStream());
            else wb = new HSSFWorkbook(file.getInputStream());
            Sheet sheet = wb.getSheetAt(0);
            int lenghtSheet = sheet.getLastRowNum();
            int halfLenght = lenghtSheet/2;
            List<LoanProductConditionExDTO> dtoList = new LinkedList<>();
            // tao 2 luong doc file ex gan value vao dtolist
            ExecutorService executor = Executors.newFixedThreadPool(10);
            CompletableFuture<List<LoanProductConditionExDTO>> list1 =
                CompletableFuture.supplyAsync(() -> getListFromExcel( sheet,ROW_START,halfLenght), executor);
            CompletableFuture<List<LoanProductConditionExDTO>> list2 =
                CompletableFuture.supplyAsync(() -> getListFromExcel( sheet,halfLenght+1,lenghtSheet), executor);
            executor.shutdown();
            list1.get();
            list2.get();
            list1.thenAccept(value ->  dtoList.addAll(value));
            list2.thenAccept(value ->  dtoList.addAll(value));
            List<LoanProductConditionExDTO> listErr = getSheetError( dtoList, userName);
            if (listErr.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "ok", null));
            }
            return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("waring", "Import thành công,có dữ liệu import lỗi vui lòng kiểu tra lại. ", listErr));
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseObject("Exception",e.getMessage() , null));
        }
    }

    public ResponseEntity<InputStreamResource> exportFileErrEx(String fileName, List<LoanProductConditionExDTO> listErrDto){
        try {
            Workbook workbook = getWorkBook(fileName);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Sheet sheet = workbook.getSheetAt(0);
            int size = listErrDto.size();

            CellStyle style = workbook.createCellStyle();
            style.setBorderBottom(BorderStyle.THIN);
            style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            style.setBorderRight(BorderStyle.THIN);
            style.setRightBorderColor(IndexedColors.BLACK.getIndex());
            style.setBorderTop(BorderStyle.THIN);
            style.setTopBorderColor(IndexedColors.BLACK.getIndex());
            style.setBorderLeft(BorderStyle.THIN);
            style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            style.setWrapText(true);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);

            for(int i = 0; i < size; i++){
                Row row = sheet.getRow(ROW_START + i);
                LoanProductConditionExDTO dto = listErrDto.get(i);
                Cell cellStt =  row.createCell(STT_CELL_NUMBER);
                Cell cellCode = row.createCell(CODE_NUMBER);
                Cell cellProductType = row.createCell(PRODUCT_TYPE_NUMBER);
                Cell cellSlipCode = row.createCell(SLIP_CODE_NUMBER);
                Cell cellDay = row.createCell(DAY_NUMBER);
                Cell cellFrequency = row.createCell(FREQUENCY_NUMBER);
                Cell cellFrequencyUnit = row.createCell(FREQUENCY_UNIT_NUMBER);
                Cell cellNote = row.createCell(NOTE_NUMBER);
                Cell cellSanction =  row.createCell(SANCTION_NUMBER);
                Cell cellError = row.createCell(ERR_CELL_NUMBER);

                cellStt.setCellValue( i + 1);
                cellCode.setCellValue(dto.getCode());
                cellProductType.setCellValue(dto.getProductType());
                cellSlipCode.setCellValue(dto.getSlipCode());
                cellDay.setCellValue(dto.getDay());
                cellFrequency.setCellValue(dto.getFrequency());
                cellFrequencyUnit.setCellValue(dto.getFrequencyUnit());
                cellNote.setCellValue(dto.getNote());
                cellSanction.setCellValue(dto.getSanction());
                cellError.setCellValue(dto.getErrMess());

                cellStt.setCellStyle(style);
                cellCode.setCellStyle(style);
                cellProductType.setCellStyle(style);
                cellSlipCode.setCellStyle(style);
                cellDay.setCellStyle(style);
                cellFrequency.setCellStyle(style);
                cellFrequencyUnit.setCellStyle(style);
                cellNote.setCellStyle(style);
                cellSanction.setCellStyle(style);
                cellError.setCellStyle(style);
            }
            workbook.write(out);
            ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
            workbook.close();
            return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(new InputStreamResource(in));
        }catch(Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(null);
        }
    }

    public Set<String> getAllCode(){return loanProductRepo.getAllCode();}

    private List<LoanProductConditionExDTO> validateListDTO(List<LoanProductConditionExDTO> listDTO){
        Set<String> getListProductName = loanProductService.getListProductType();
        Set<String> getAllCode = getAllCode();
        listDTO.forEach(value -> {
            String errMess = "";
            String errCode = validateCode(value.getCode());
            String errProductType = validateProductType(value.getProductType(),getListProductName);
            String errSlipCode = validateString(value.getSlipCode(),"Tên chứng từ ",4000,true);
            String errDay = validateNumber(value.getDay(), "Số ngày sau ngày gải ngân đầu tiên. ",true);
            String errFrequency = validateNumber(value.getFrequency(), "Tần suất",false);
            String errFrequencyUnit = validateFrequencyUnit(value.getFrequencyUnit());
            String errNote = validateString(value.getNote(), "Ghi chú",1000,false);
            String errSanction = validateString(value.getSanction(), "Chế tài",1000,false);

            if(errCode != null ) errMess += errCode;
            if(errCode == null ) {
                if(getAllCode.contains(value.getCode()))errMess += "Mã điều kiện đã tồn tại.";
                else getAllCode.add(value.getCode());
            }
            if(errProductType != null) errMess += errProductType;
            if(errSlipCode != null) errMess += errSlipCode;
            if(errDay != null) errMess += errDay;
            if(errFrequency != null) errMess += errFrequency;
            if(errFrequencyUnit != null) errMess += errFrequencyUnit;
            if(errNote != null) errMess += errNote;
            if(errSanction != null) errMess += errSanction;
            value.setErrMess(errMess);
        });
        return listDTO;
    }
}
