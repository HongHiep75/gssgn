package com.itsol.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "tbl_bi_collateral_type")
public class CollateralType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    @Type(type="org.hibernate.type.UUIDCharType")
    UUID id;

    @Column(name = "bi_param_id")
    private String biPramId;

    @Column(name = "colla_type_code")
    private String collateralTypeCode;

    @Column(name = "colla_type_name")
    private String collateralTypeName;

    @Column(name = "created_date")
    private Timestamp createdDate;

    @Column(name = "created_user")
    private String createdUser;

    public CollateralType(String collateralTypeCode, String collateralTypeName) {
        this.collateralTypeCode = collateralTypeCode;
        this.collateralTypeName = collateralTypeName;
    }

    public CollateralType() { }
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getBiPramId() {
        return biPramId;
    }

    public void setBiPramId(String biPramId) {
        this.biPramId = biPramId;
    }

    public String getCollateralTypeCode() {
        return collateralTypeCode;
    }

    public void setCollateralTypeCode(String collateralTypeCode) {
        this.collateralTypeCode = collateralTypeCode;
    }

    public String getCollateralTypeName() {
        return collateralTypeName;
    }

    public void setCollateralTypeName(String collateralTypeName) {
        this.collateralTypeName = collateralTypeName;
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
}
