package com.lhduc.restaurantmanagementapi.common.mappers;

import java.util.Collection;

public interface BaseMapper<DTO, ENTITY> {
    DTO convertToDto(ENTITY entity);

    Collection<DTO> convertToDto(Collection<ENTITY> entity);

    ENTITY convertToEntity(DTO dto);

    Collection<ENTITY> convertToEntity(Collection<DTO> dto);
}
