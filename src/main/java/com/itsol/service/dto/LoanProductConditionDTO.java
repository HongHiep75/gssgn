package com.itsol.service.dto;

import com.itsol.config.Constants;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

public class LoanProductConditionDTO implements Serializable {

    private UUID id;
    @NotNull(message = Constants.MessValidate.ERR_002)
    @Pattern(regexp = Constants.CODE_REGEX, message = Constants.MessValidate.ERR_003)
    private String code;

    @NotNull(message = Constants.MessValidate.ERR_002)
    @Pattern(regexp = Constants.NUMBER_BIGGER_O, message = Constants.MessValidate.ERR_005)
    @Min(1)
    private int day;

    @NotNull(message = Constants.MessValidate.ERR_002)
    @Min(0)
    @Max(1000000000)
    private int frequency;

    @NotNull(message = Constants.MessValidate.ERR_002)
    @Pattern(regexp = "DAY|MOUNTH")
    private String frequencyUnit;

    @NotNull(message = Constants.MessValidate.ERR_002)
    private String productType;

    @Size(max = 1000)
    private String sanction;

    @Size(max = 1000)
    private String note;

    @NotNull(message = Constants.MessValidate.ERR_002)
    @Size(max = 4000)
    private String slipCode;

    @NotNull(message = Constants.MessValidate.ERR_002)
    private String status;
    private Timestamp createdDate;
    private String createUser;
    private Timestamp updateDate;
    private String updateUser;

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

    public Timestamp getCreateDate() {
        return createdDate;
    }

    public void setCreateDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public String getFrequencyUnit() {
        return frequencyUnit;
    }

    public void setFrequencyUnit(String frequencyUnit) {
        this.frequencyUnit = frequencyUnit;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getSanction() {
        return sanction;
    }

    public void setSanction(String sanction) {
        this.sanction = sanction;
    }

    public String getSlipCode() {
        return slipCode;
    }

    public void setSlipCode(String slipCode) {
        this.slipCode = slipCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
}
