package com.itsol.service.mapper;

import com.itsol.domain.LoanProductCondition;
import com.itsol.service.dto.LoanProductConditionDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
@Component
public class LoanProductConditionMapper implements EntityMapper<LoanProductConditionDTO, LoanProductCondition> {

    @Override
    public LoanProductCondition toEntity(LoanProductConditionDTO dto) {
        if (dto == null) {
            return null;
        }

        LoanProductCondition entity = new LoanProductCondition();
        BeanUtils.copyProperties(dto, entity);

        return entity;
    }

    @Override
    public LoanProductConditionDTO toDto(LoanProductCondition entity) {
        if (entity == null) {
            return null;
        }

        LoanProductConditionDTO dto = new LoanProductConditionDTO();
        BeanUtils.copyProperties(entity, dto);

        return dto;
    }

    @Override
    public List<LoanProductCondition> toEntity(List<LoanProductConditionDTO> dtoList) {
        return dtoList.stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<LoanProductConditionDTO> toDto(List<LoanProductCondition> entityList) {
        return entityList.stream().map(this::toDto).collect(Collectors.toList());
    }
}
