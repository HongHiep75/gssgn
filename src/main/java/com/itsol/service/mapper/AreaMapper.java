package com.itsol.service.mapper;


import com.itsol.domain.Area;
import com.itsol.service.dto.AreaDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for the entity {@link Area} and its DTO {@link AreaDTO}.
 */
@Component
public class AreaMapper implements EntityMapper<AreaDTO, Area> {


    @Override
    public Area toEntity(AreaDTO dto) {
        if (dto == null) {
            return null;
        }

        Area entity = new Area();
        BeanUtils.copyProperties(dto, entity);

        return entity;
    }

    @Override
    public AreaDTO toDto(Area entity) {
        if (entity == null) {
            return null;
        }

        AreaDTO dto = new AreaDTO();
        BeanUtils.copyProperties(entity, dto);

        return dto;
    }

    @Override
    public List<Area> toEntity(List<AreaDTO> dtoList) {
        return dtoList.stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<AreaDTO> toDto(List<Area> entityList) {
        return entityList.stream().map(this::toDto).collect(Collectors.toList());
    }
}
