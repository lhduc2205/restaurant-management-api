package com.lhduc.restaurantmanagementapi.model.mappers;

import java.util.List;

/**
 * The BaseMapper interface defines a contract for mapping between Data Transfer Objects (DTOs)
 * and entity objects. It provides methods for converting entity objects to DTOs, which is
 * useful for data transfer and presentation purposes.
 *
 * @param <D> The DTO type that represents the data for presentation.
 * @param <E> The entity type that represents the data in the underlying data storage.
 */
public interface BaseMapper<D, E> {
    /**
     * Converts an entity object to a corresponding Data Transfer Object (DTO).
     *
     * @param entity The entity object to convert.
     * @return A DTO representing the data from the entity.
     */
    D convertToDto(E entity);

    /**
     * Converts a list of entity objects to a list of corresponding Data Transfer Objects (DTOs).
     *
     * @param entities The list of entity objects to convert.
     * @return A list of DTOs representing the data from the entity list.
     */
    List<D> convertToDto(List<E> entities);
}
