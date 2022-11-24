package com.itsol.service;

import com.itsol.domain.ApplySanction;
import com.itsol.domain.ResponseObject;
import com.itsol.domain.enumeration.CommonStatus;
import com.itsol.repository.ApplySanctionRepository;
import com.itsol.service.dto.ApplySanctionDTO;
import com.itsol.service.mapper.ApplySanctionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class ApplySanctionServiceImp implements ApplySanctionService{

    private final Logger log = LoggerFactory.getLogger(ApplySanctionServiceImp.class);
    private final ApplySanctionRepository applySanctionRepository;
    private final ApplySanctionMapper applySanctionMapper;
    private final String reportDateValue = "Ngày cuối cùng của tháng";
    private final Set<String> interestAdjustList = new HashSet<>();
    private final Set<String> ratingStatusList = new HashSet<>();

    public ApplySanctionServiceImp(ApplySanctionRepository applySanctionRepository, ApplySanctionMapper applySanctionMapper) {
        this.applySanctionRepository = applySanctionRepository;
        this.applySanctionMapper = applySanctionMapper;
        this.setInterestAdjustList();
        this.setRatingStatusList();
    }

    @Override
    public ResponseEntity<ResponseObject> getApplySanction(Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(new ResponseObject("ok","Thành công"
                ,applySanctionRepository.getApplySanction(pageable)));
    }

    @Override
    public ResponseEntity<ResponseObject> save(ApplySanctionDTO applySanctionDTO) {
        Optional<ApplySanction> checkCode = applySanctionRepository.findByCode(applySanctionDTO.getCode());
        List<String> errMess = new LinkedList<>();
        if(checkCode.isPresent() ){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("ERR_001","Cập nhật không thành công",null));
        }
        if(!this.ratingStatusList.contains(applySanctionDTO.getRatingStatus())){
            errMess.add("Trạng thái đánh giá không dúng");
        }
        if(!this.interestAdjustList.contains(applySanctionDTO.getInterestAdjust())){
            errMess.add("Lãi xuất điều chỉnh không đúng");
        }
        if(!errMess.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseObject("fasle","Cập nhật không thành công",errMess));
        }
        try{
            applySanctionDTO.setReportDate(this.reportDateValue);
            ApplySanction applySanction = applySanctionRepository
                .save(applySanctionMapper.toEntity(applySanctionDTO));
            return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("MSG_001","Cập nhật thành công",applySanction));
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseObject("MSF_001","Cập nhật không thành công",""));
        }
    }

    @Override
    public int  updateApplySanction(String applyDate, Integer applyMonth, CommonStatus status
        , Timestamp updatedDate, String updatedUser, UUID id,String ratingStatus,String interestAdjust) {
        return applySanctionRepository.updateApplySanction(applyDate,applyMonth,status,updatedDate,updatedUser,id,ratingStatus,interestAdjust);
    }

    public void setInterestAdjustList(){
        this.interestAdjustList.add("Giảm");
        this.interestAdjustList.add("Tăng");
    }

    public void setRatingStatusList(){
        this.ratingStatusList.add("Không tuân thủ");
        this.ratingStatusList.add("Tuân thủ");
    }
}
