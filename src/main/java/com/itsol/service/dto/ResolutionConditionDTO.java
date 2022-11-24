package com.itsol.service.dto;

import com.itsol.config.Constants;
import com.itsol.domain.enumeration.CommonStatus;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

public class ResolutionConditionDTO {

    private UUID id;
    @NotNull
    @Pattern(regexp = Constants.CODE_REGEX, message = Constants.MessValidate.ERR_003)
    private String code;
    @NotNull
    @Size(max = 255)
    @Pattern(regexp = Constants.NAME_REGEX, message = Constants.MessValidate.ERR_004)
    private String name;
    @NotNull
    private CommonStatus status;

    private Timestamp createdDate;
    @Size(max = 255)
    private String createdUser;

    private Timestamp updatedDate;
    @Size(max = 255)
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CommonStatus getStatus() {
        return status;
    }

    public void setStatus(CommonStatus status) {
        this.status = status;
    }

    public String getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    public String getUpdatedUser() {
        return updatedUser;
    }

    public void setUpdatedUser(String updatedUser) {
        this.updatedUser = updatedUser;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }
}
