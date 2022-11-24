package com.itsol.service.mapper;

import com.itsol.domain.LoanProduct;
import com.itsol.service.dto.LoanProductDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LoanProductMapper implements EntityMapper<LoanProductDTO, LoanProduct>{

    @Override
    public LoanProduct toEntity(LoanProductDTO dto) {
        if (dto == null) {
            return null;
        }

        LoanProduct entity = new LoanProduct();
        BeanUtils.copyProperties(dto, entity);

        return entity;
    }

    @Override
    public LoanProductDTO toDto(LoanProduct entity) {
        if (entity == null) {
            return null;
        }

        LoanProductDTO dto = new LoanProductDTO();
        BeanUtils.copyProperties(entity, dto);

        return dto;
    }

    @Override
    public List<LoanProduct> toEntity(List<LoanProductDTO> dtoList) {
        return dtoList.stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<LoanProductDTO> toDto(List<LoanProduct> entityList) {
        return entityList.stream().map(this::toDto).collect(Collectors.toList());
    }
}
