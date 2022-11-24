package com.itsol.service.mapper;

import com.itsol.domain.ValCollateralFrequency;
import com.itsol.service.dto.ValCollateralFrequencyDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ValConllateralFrequencyMapper implements EntityMapper<ValCollateralFrequencyDTO, ValCollateralFrequency> {
    @Override
    public ValCollateralFrequency toEntity(ValCollateralFrequencyDTO dto) {
        if (dto == null) {
            return null;
        }
        ValCollateralFrequency entity = new ValCollateralFrequency();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }

    @Override
    public ValCollateralFrequencyDTO toDto(ValCollateralFrequency entity) {
        if (entity == null) {
            return null;
        }

        ValCollateralFrequencyDTO dto = new ValCollateralFrequencyDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    @Override
    public List<ValCollateralFrequency> toEntity(List<ValCollateralFrequencyDTO> dtoList) {

        return dtoList.stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<ValCollateralFrequencyDTO> toDto(List<ValCollateralFrequency> entityList) {

        return entityList.stream().map(this::toDto).collect(Collectors.toList());
    }
}
