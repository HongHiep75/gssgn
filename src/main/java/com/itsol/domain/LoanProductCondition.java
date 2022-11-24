package com.itsol.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "tbl_loan_product_condition")
public class LoanProductCondition implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID id;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "created_date")
    private Timestamp createdDate;

    @Column(name = "created_user")
    private String createUser;

    @Column(name = "days")
    private int day;

    @Column(name = "frequency")
    private int frequency;

    @Column(name = "frequency_unit")
    private String frequencyUnit;

    @Column(name = "note")
    private String note;

    @Column(name = "product_type")
    private String productType;

    @Column(name = "sanction")
    private String sanction;

    @Column(name = "slip_code")
    private String slipCode;

    @Column(name = "status")
    private String status;

    @Column(name = "updated_date")
    private Timestamp updateDate;

    @Column(name = "updated_user")
    private String updateUser;

    public LoanProductCondition() {
    }

    public LoanProductCondition(UUID id, String code, Timestamp createDate, String createUser, int day, int frequency, String frequencyUnit, String note, String productType, String sanction, String slipCode, String status, Timestamp updateDate, String updateUser) {
        this.id = id;
        this.code = code;
        this.createdDate = createDate;
        this.createUser = createUser;
        this.day = day;
        this.frequency = frequency;
        this.frequencyUnit = frequencyUnit;
        this.note = note;
        this.productType = productType;
        this.sanction = sanction;
        this.slipCode = slipCode;
        this.status = status;
        this.updateDate = updateDate;
        this.updateUser = updateUser;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

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
