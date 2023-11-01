package com.lhduc.restaurantmanagementapi.model.mappers;

import java.util.List;

/**
 * @param <D> meaning Dto
 * @param <E> meaning Entity
 */
public interface BaseMapper<D, E> {
    D convertToDto(E entity);

    List<D> convertToDto(List<E> entity);
}
