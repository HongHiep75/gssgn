package com.itsol.service.mapper;


import com.itsol.domain.Area;
import com.itsol.domain.ResolutionCondition;
import com.itsol.service.dto.ResolutionConditionDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for the entity {@link Area} and its DTO {@link ResolutionConditionDTO}.
 */
@Component
public class ResolutionConditionMapper implements EntityMapper<ResolutionConditionDTO, ResolutionCondition> {


    @Override
    public ResolutionCondition toEntity(ResolutionConditionDTO dto) {
        if (dto == null) {
            return null;
        }

        ResolutionCondition entity = new ResolutionCondition();
        BeanUtils.copyProperties(dto, entity);

        return entity;
    }

    @Override
    public ResolutionConditionDTO toDto(ResolutionCondition entity) {
        if (entity == null) {
            return null;
        }

        ResolutionConditionDTO dto = new ResolutionConditionDTO();
        BeanUtils.copyProperties(entity, dto);

        return dto;
    }

    @Override
    public List<ResolutionCondition> toEntity(List<ResolutionConditionDTO> dtoList) {
        return dtoList.stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<ResolutionConditionDTO> toDto(List<ResolutionCondition> entityList) {
        return entityList.stream().map(this::toDto).collect(Collectors.toList());
    }
}
