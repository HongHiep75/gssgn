package com.itsol.service;



import com.itsol.domain.CollateralType;
import com.itsol.domain.ResponseObject;
import com.itsol.domain.ValCollateralFrequency;
import com.itsol.domain.enumeration.CommonStatus;
import com.itsol.repository.ValCollateralFrequencyRepository;
import com.itsol.service.dto.ValCollateralFrequencyDTO;
import com.itsol.service.dto.ValCollateralFrequencyExDTO;
import com.itsol.service.mapper.ValConllateralFrequencyMapper;
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
import java.util.stream.Collectors;


@Service
public class ValCollateralFrequencyServiceImp implements ValCollateralFrequencyService {

    private final Logger log = LoggerFactory.getLogger(ValCollateralFrequencyServiceImp.class);
    private final ValCollateralFrequencyRepository valCollateralFrequencyRepository;
    private final ValConllateralFrequencyMapper valConllateralFrequencyMapper;
    private final CollateralTypeService collateralTypeService;
    private final IStorageSevice storageSevice;

    private final int STT_CELL_NUMBER = 0;
    private final int COLLATERAL_TYPE_CELL_NUMBER = 1;
    private final int FREQUENCY_CELL_NUMBER = 2;
    private final int FREQUENCY_UNIT_TYPE_CELL_NUMBER = 3;
    private final int ERR_CELL_NUMBER = 4;
    private final int ROW_START = 5;

    public ValCollateralFrequencyServiceImp(ValCollateralFrequencyRepository valCollateralFrequencyRepository
        , ValConllateralFrequencyMapper valConllateralFrequencyMapper, CollateralTypeService collateralTypeService, IStorageSevice storageSevice) {
        this.valCollateralFrequencyRepository = valCollateralFrequencyRepository;
        this.valConllateralFrequencyMapper = valConllateralFrequencyMapper;
        this.collateralTypeService = collateralTypeService;
        this.storageSevice = storageSevice;
    }

    @Override
    public List<ValCollateralFrequency> getList() {
        return valCollateralFrequencyRepository.findAll();
    }

    @Override
    public ResponseEntity<ResponseObject> getList(Pageable pageable, ValCollateralFrequencyDTO searValue) {
        String status = "";
        Page<ValCollateralFrequency> list;
        if (searValue.getCollateralType() == null) searValue.setCollateralType("");
        if (searValue.getFrequency() == null) searValue.setFrequency(-1);
        if (searValue.getStatus() != null) status = searValue.getStatus().toString();
        list = valCollateralFrequencyRepository
            .getList(pageable, searValue.getCollateralType(), searValue.getFrequency(), status);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok", "Thanh cong", list));
    }

    @Override
    public ResponseEntity<ResponseObject> save(ValCollateralFrequencyDTO value) {
        String codeAndNameCollateralType[] = value.getCollateralType().split(" - ");
        List<UUID> listCodeAndName = collateralTypeService
            .checkCodeAndName(codeAndNameCollateralType[0], codeAndNameCollateralType[1]);
        if (listCodeAndName.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseObject("false", "Mã tài sản không đúng", null));
        }
        return ResponseEntity.status(HttpStatus.OK)
            .body(new ResponseObject("Ok", "Thành công"
                , valCollateralFrequencyRepository.save(valConllateralFrequencyMapper.toEntity(value))));
    }

    @Override
    public List<ValCollateralFrequency> saveAll(List<ValCollateralFrequency> valCollateralFrequencies) {
        return valCollateralFrequencyRepository.saveAll(valCollateralFrequencies);
    }

    @Override
    public Optional<ValCollateralFrequency> findById(UUID id) {
        return valCollateralFrequencyRepository.findById(id);
    }

    @Override
    public ResponseEntity<ResponseObject> update(ValCollateralFrequencyDTO value) {
        Optional<ValCollateralFrequency> valCollateralFrequency = this.findById(value.getId());
        if (valCollateralFrequency.isPresent()
            && valCollateralFrequency.get().getCollateralType().equals(value.getCollateralType())) {
            return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("ok", "Thanh cong"
                    , valCollateralFrequencyRepository.save(valConllateralFrequencyMapper.toEntity(value))));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ResponseObject("false", "Id không đúng hoặc Mã tài sản không được thay đổi", null));
    }

    @Override
    public ResponseEntity<ResponseObject> importEx(MultipartFile file, String userName) {
        ResponseEntity<ResponseObject> validate = validateFileEx(file);
        if (validate != null) return validate;
        List<CollateralType> collateralTypeNotFrequency = this.collateralTypeService.getCollateralTypeNotFrequency();
        if (collateralTypeNotFrequency.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseObject("false", "Không có tài sản chưa định tần suất ", ""));
        }
        try {
            Workbook wb;
            String filename = file.getOriginalFilename();
            if (filename.endsWith(".xlsx")) wb = new XSSFWorkbook(file.getInputStream());
            else wb = new HSSFWorkbook(file.getInputStream());
            Sheet sheet = wb.getSheetAt(0);
            int lenghtSheet = sheet.getLastRowNum();
            int halfLenght = lenghtSheet / 2;
            List<ValCollateralFrequencyExDTO> dtoList = new LinkedList<>();

            ExecutorService executor = Executors.newFixedThreadPool(10);
            // tao 2 luong doc du lieu ex
            CompletableFuture<List<ValCollateralFrequencyExDTO>> list1 =
                CompletableFuture.supplyAsync(() -> getListValFormEx(sheet, ROW_START, halfLenght), executor);
            CompletableFuture<List<ValCollateralFrequencyExDTO>> list2 =
                CompletableFuture.supplyAsync(() -> getListValFormEx(sheet, halfLenght + 1, lenghtSheet), executor);
            executor.shutdown();
            list1.get();
            list2.get();
            list1.thenAccept(value -> dtoList.addAll(value));
            list2.thenAccept(value -> dtoList.addAll(value));
            List<ValCollateralFrequencyExDTO> listErr = getSheetErr(collateralTypeNotFrequency, dtoList, userName);
            if (listErr.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "ok", null));
            }
            return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("waring", "Improt thành công,có dữ liệu import lỗi vui lòng kiểu tra lại", listErr));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseObject("Exception", "Có lỗi xẩy ra vui lòng thử lại", null));
        }
    }

    @Override
    public Set<String> getCollateralType() {return valCollateralFrequencyRepository.getCollateralType(); }

    private List<ValCollateralFrequencyExDTO> getSheetErr(List<CollateralType> collateralTypeNotFrequency
        , List<ValCollateralFrequencyExDTO> dtoList, String userName) {
        // valideate list ValCollateralFrequencyExDTO
        dtoList = validateListValEx(dtoList, collateralTypeNotFrequency);
        List<ValCollateralFrequency> listSuccess = new LinkedList<>();
        List<ValCollateralFrequencyExDTO> listErr = new LinkedList<>();
        // loc cac validateListValEx hop le luu vao listSuccess nguoc lai luu vao listErr
        dtoList.forEach(value -> {
            if (value.getErrMess().equals("")) listSuccess.add(createValCollateralFrequency(value,userName));
            else listErr.add(value);
        });
//         co valCollateralFrequency hop le luu vao database
        if (!listSuccess.isEmpty()) saveAll(listSuccess);
//         co valCollateralFrequency khong hop le return dtoList else return null
        return listErr;
    }

    private ValCollateralFrequency createValCollateralFrequency(ValCollateralFrequencyExDTO value,String userName){
        ValCollateralFrequency valCollateralFrequency = new ValCollateralFrequency();
        valCollateralFrequency.setCreatedUser(userName);
        valCollateralFrequency.setUpdatedUser(userName);
        valCollateralFrequency.setStatus(CommonStatus.ACTIVE);
        String frequencyUnit = value.getFrequencyUnit().equals("D") ? "DAY" : "MONTH";
        valCollateralFrequency.setCollateralType(value.getCollateralType());
        valCollateralFrequency.setFrequency(Integer.parseInt(value.getFrequency()));
        valCollateralFrequency.setFrequencyUnit(frequencyUnit);
        valCollateralFrequency.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        valCollateralFrequency.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
        return  valCollateralFrequency;
    }


    private List<ValCollateralFrequencyExDTO> validateListValEx(List<ValCollateralFrequencyExDTO> dtoList, List<CollateralType> collateralTypeNotFrequency) {
        // tạo danh sách code and name chưa định giá
        Map<String, String> mapCollateralTypeNotFrequency = collateralTypeNotFrequency.stream().collect(
            Collectors.toMap(CollateralType::getCollateralTypeCode, CollateralType::getCollateralTypeName));
//        danh sách mã đã định giá
        Set<String> listCollateralTypeFrequency = valCollateralFrequencyRepository.getCollateralType()
            .stream().map(value -> value.split(" - ")[0]).collect(Collectors.toSet());

        dtoList.forEach(value -> {
            String valueCollateralType = value.getCollateralType();
            String valueFrequency = value.getFrequency();
            String valueFrequencyUnit = value.getFrequencyUnit();
            //vailedate cell
            String errMess = "";
            String errCode = validateNumber(valueCollateralType, "Mã tài sản");
            String errFrequency = validateNumber(valueFrequency, "Tần xuất");
            String errFrequencyUnit = validateFrequencyUnit(valueFrequencyUnit);
            if (errCode == null) {
                // kiểm tra xem code đã được định giá hay chưa nếu chưa tiếp tực kiểu tra code có tồn tại không
                if (listCollateralTypeFrequency.contains(valueCollateralType)) {
                    errMess += "Mã tài sản đã được định giá. ";
                } else {
                    String nameCollateralType = mapCollateralTypeNotFrequency.get(valueCollateralType);
                    if (nameCollateralType == null) errMess += "Mã tài sản không tồn tại. ";
                    else {
                        value.setCollateralType(valueCollateralType + " - " + nameCollateralType);
                        listCollateralTypeFrequency.add(valueCollateralType);
                        mapCollateralTypeNotFrequency.remove(valueCollateralType);
                    }
                }
            }else  errMess += errCode;
            if (errFrequency != null) errMess += errFrequency;
            if (errFrequencyUnit != null) errMess += errFrequencyUnit;
            if(!errMess.equals("")) value.setCollateralType(valueCollateralType);
            value.setErrMess(errMess);
        });
        return dtoList;
    }

    private String validateNumber(String value, String elementValidate) {
        if (value == null) return elementValidate + " không được để trống.";
        Pattern checkNumber = Pattern.compile("[0-9]");
        if (checkNumber.matcher(value).find()) return null;
        return elementValidate + " gồm các số 0-9.";
    }

    private String validateFrequencyUnit(String value) {
        if (value == null) return "Đơn vị tần xuất không được để trống.";
        if (value.equals("D") || value.equals("M")) return null;
        return "Đơn vị tần xuất chỉ nhập D (ngày) hoặc M (tháng).";
    }

    private List<ValCollateralFrequencyExDTO> getListValFormEx(Sheet sheet, int skip, int limit) {
        List<ValCollateralFrequencyExDTO> dtoList = new LinkedList<>();
        for (int i = skip; i <= limit; i++) {
            ValCollateralFrequencyExDTO exDTO = new ValCollateralFrequencyExDTO();
            Cell cellCollateralTypeCode = sheet.getRow(i).getCell(COLLATERAL_TYPE_CELL_NUMBER);
            Cell cellFrequency = sheet.getRow(i).getCell(FREQUENCY_CELL_NUMBER);
            Cell cellFrequencyUnit = sheet.getRow(i).getCell(FREQUENCY_UNIT_TYPE_CELL_NUMBER);

            boolean check = cellCollateralTypeCode == null || cellFrequency == null || cellFrequencyUnit == null;
            if(check) continue;

            // get value of cell
            String codeVale = getCellValue(cellCollateralTypeCode);
            String frequencyVale = getCellValue(cellFrequency);
            String frequencyUnitVale = getCellValue(cellFrequencyUnit);
            // row khong co value bo qua
            boolean isEmpty = codeVale == null && frequencyVale == null && frequencyUnitVale == null;
            if (isEmpty) continue;
            exDTO.setCollateralType(codeVale);
            exDTO.setFrequency(frequencyVale);
            exDTO.setFrequencyUnit(frequencyUnitVale);
            dtoList.add(exDTO);
        }
        return dtoList;
    }

    private ResponseEntity<ResponseObject> validateFileEx(MultipartFile file) {
        List<String> messErr = new LinkedList<>();
        if (storageSevice.validateTypeFile(file, new String[]{".xls,.xlsx"})) messErr.add("only file .xls,.xlsx");
        if (storageSevice.validateSizeFile(file, 25.0f)) messErr.add("file <= 25mb");
        if (!messErr.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseObject("false", "import không thành công", messErr));
        }
        return null;
    }

    // Get cell value
    private static String getCellValue(Cell cell) {
        CellType cellType = cell.getCellType();
        Object cellValue = null;
        switch (cellType) {
            case NUMERIC:
                cellValue = new BigDecimal((double) cell.getNumericCellValue()).intValue() + "";
                break;
            case FORMULA:
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
        if (cellValue == null) return null;
        return cellValue.toString();
    }

    @Override
    public ResponseEntity<InputStreamResource> exportFileErrEx(String fileName, List<ValCollateralFrequencyExDTO> exDTO) {
        try {
            Workbook workbook = getWorkBook(fileName);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Sheet sheet = workbook.getSheetAt(0);
            int size = exDTO.size();
            // tao style cho workbook
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

            for (int i = 0; i < size; i++) {
                Row row = sheet.getRow(ROW_START +i);
                Cell cellStt   = row.createCell(STT_CELL_NUMBER);
                Cell cellCollateralType   = row.createCell(COLLATERAL_TYPE_CELL_NUMBER);
                Cell cellFrequency   = row.createCell(FREQUENCY_CELL_NUMBER);
                Cell cellFrequencyUnit   = row.createCell(FREQUENCY_UNIT_TYPE_CELL_NUMBER);
                Cell cellErrMess   = row.createCell(ERR_CELL_NUMBER);
                ValCollateralFrequencyExDTO dto = exDTO.get(i);
                // xet value cho cell
                cellStt.setCellValue(1 + i);
                cellCollateralType.setCellValue(dto.getCollateralType());
                cellFrequency.setCellValue(dto.getFrequency());
                cellFrequencyUnit.setCellValue(dto.getFrequencyUnit());
                cellErrMess.setCellValue(dto.getErrMess());
                // xet style cell
                cellStt.setCellStyle(style);
                cellCollateralType.setCellStyle(style);
                cellFrequency.setCellStyle(style);
                cellFrequencyUnit.setCellStyle(style);
                cellErrMess.setCellStyle(style);
            }
            workbook.write(out);
            ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
            workbook.close();
            return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(new InputStreamResource(in));
        } catch (IOException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(null);
        }
    }

    private Workbook getWorkBook(String fileName) throws IOException {
        if (fileName.endsWith(".xlsx")) return new XSSFWorkbook(storageSevice.getResource(fileName).getInputStream());
            return new HSSFWorkbook(storageSevice.getResource(fileName).getInputStream());
    }




}
