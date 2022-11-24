package com.itsol.service.dto;

public class LoanProductConditionExDTO {
    private String stt;
    private String code;
    private String day;
    private String frequency;
    private String frequencyUnit;
    private String note;
    private String productType;
    private String sanction;
    private String slipCode;
    private String errMess;

    public String getStt() {
        return stt;
    }

    public void setStt(String stt) {
        this.stt = stt;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
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

    public String getErrMess() {
        return errMess;
    }

    public void setErrMess(String errMess) {
        this.errMess = errMess;
    }

    @Override
    public String toString() {
        return "LoanProductConditionExDTO{" +
            "stt='" + stt + '\'' +
            ", code='" + code + '\'' +
            ", day='" + day + '\'' +
            ", frequency='" + frequency + '\'' +
            ", frequencyUnit='" + frequencyUnit + '\'' +
            ", note='" + note + '\'' +
            ", productType='" + productType + '\'' +
            ", sanction='" + sanction + '\'' +
            ", slipCode='" + slipCode + '\'' +
            ", errMess='" + errMess + '\'' +
            '}';
    }
}
