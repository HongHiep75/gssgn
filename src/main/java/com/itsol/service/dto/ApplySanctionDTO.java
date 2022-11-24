package com.itsol.service.dto;

import com.itsol.config.Constants;
import com.itsol.domain.enumeration.CommonStatus;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;
import java.util.UUID;

public class ApplySanctionDTO {

    private UUID id;
    @NotNull
    @Pattern(regexp = Constants.CODE_REGEX, message = Constants.MessValidate.ERR_003)
    private String code;
    @NotNull
    @Pattern(regexp = Constants.DAY_REGEX)
    private String applyDate;
    @NotNull
    private Integer applyMonth;
    @NotNull
    private String interestAdjust;
    @NotNull
    private String ratingStatus;
    @NotNull
    private String reportDate;
    private CommonStatus status;
    private Timestamp createdDate;
    private String createdUser;
    private Timestamp updatedDate;
    private String updatedUser;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
    }

    public Integer getApplyMonth() {
        return applyMonth;
    }

    public String getInterestAdjust() {
        return interestAdjust;
    }

    public void setInterestAdjust(String interestAdjust) {
        this.interestAdjust = interestAdjust;
    }

    public String getRatingStatus() {
        return ratingStatus;
    }

    public void setRatingStatus(String ratingStatus) {
        this.ratingStatus = ratingStatus;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public CommonStatus getStatus() {
        return status;
    }

    public void setStatus(CommonStatus status) {
        this.status = status;
    }

    public void setApplyMonth(Integer applyMonth) {
        this.applyMonth = applyMonth;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getUpdatedUser() {
        return updatedUser;
    }

    public void setUpdatedUser(String updatedUser) {
        this.updatedUser = updatedUser;
    }
}
