package com.itsol.service.mapper;

import com.itsol.domain.ControlMethod;
import com.itsol.service.dto.ControlMethodDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ControlMapper implements EntityMapper<ControlMethodDTO, ControlMethod> {

    @Override
    public ControlMethod toEntity(ControlMethodDTO dto) {
        if (dto == null) {
            return null;
        }
        ControlMethod entity = new ControlMethod();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }

    @Override
    public ControlMethodDTO toDto(ControlMethod entity) {
        if (entity == null) {
            return null;
        }
        ControlMethodDTO dto = new ControlMethodDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    @Override
    public List<ControlMethod> toEntity(List<ControlMethodDTO> dtoList) {
        return dtoList.stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<ControlMethodDTO> toDto(List<ControlMethod> entityList) {
        return entityList.stream().map(this::toDto).collect(Collectors.toList());
    }
}
