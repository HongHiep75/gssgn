package com.itsol.service.dto;

public class ValCollateralFrequencyExDTO {

    private String collateralType;
    private String frequency;
    private String frequencyUnit;
    private String errMess;

    public String getCollateralType() {
        return collateralType;
    }

    public void setCollateralType(String collateralType) {
        this.collateralType = collateralType;
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

    public String getErrMess() {
        return errMess;
    }

    public void setErrMess(String errMess) {
        this.errMess = errMess;
    }

    @Override
    public String toString() {
        return "ValCollateralFrequencyExDTO{" +
            ", collateralType= " + collateralType +
            ", frequency= " + frequency +
            ", frequencyUnit= " + frequencyUnit +
            ", errMess='" + errMess + '\'' +
            '}';
    }
}
