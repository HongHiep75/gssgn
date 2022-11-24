package com.itsol.domain;

import com.itsol.domain.enumeration.CommonStatus;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "TBL_APPLY_SANCTION")
public class ApplySanction implements Serializable {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID id;
    @Column(name = "apply_date")
    private String applyDate;

    @Column(name = "apply_month")
    private Integer applyMonth;

    @Column(name = "code")
    private String code;

    @Column(name = "interest_adjust")
    private String interestAdjust;

    @Column(name = "rating_status")
    private String ratingStatus;

    @Column(name = "report_date")
    private String reportDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CommonStatus status;

    @Column(name = "created_date")
    private Timestamp createdDate;

    @Column(name = "created_user")
    @Size(max = 255)
    private String createUser;

    @Column(name = "updated_date")
    private Timestamp updatedDate;

    @Column(name = "updated_user")
    private String updatedUser;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public void setApplyMonth(Integer applyMonth) {
        this.applyMonth = applyMonth;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
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
