package com.itsol.service.mapper;


import com.itsol.domain.ApplySanction;
import com.itsol.domain.Area;
import com.itsol.service.dto.ApplySanctionDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for the entity {@link Area} and its DTO {@link ApplySanctionDTO}.
 */
@Component
public class ApplySanctionMapper implements EntityMapper<ApplySanctionDTO, ApplySanction> {


    @Override
    public ApplySanction toEntity(ApplySanctionDTO dto) {
        if (dto == null) {
            return null;
        }
        ApplySanction entity = new ApplySanction();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }

    @Override
    public ApplySanctionDTO toDto(ApplySanction entity) {
        if (entity == null) {
            return null;
        }
        ApplySanctionDTO dto = new ApplySanctionDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    @Override
    public List<ApplySanction> toEntity(List<ApplySanctionDTO> dtoList) {
        return dtoList.stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<ApplySanctionDTO> toDto(List<ApplySanction> entityList) {
        return entityList.stream().map(this::toDto).collect(Collectors.toList());
    }
}
