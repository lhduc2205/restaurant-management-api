package com.lhduc.restaurantmanagementapi.util;

import com.lhduc.restaurantmanagementapi.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public class RepositoryUtil {
    private RepositoryUtil() {
    }

    /**
     * Finds an entity by its ID in the given JpaRepository and throws a NotFoundException
     * with the specified error message if the entity is not found.
     *
     * @param id           The ID of the entity to be found.
     * @param repository   The JpaRepository used for entity retrieval.
     * @param errorMessage The error message to be used in the NotFoundException if the entity is not found.
     * @param <T>          The type of the entity
     * @param <K>          The type of the entity's ID
     * @return The found entity.
     * @throws NotFoundException If the entity with the given ID is not found.
     */
    public static <T, K> T findEntityByIdOrThrow(K id, JpaRepository<T, K> repository, String errorMessage) {
        return repository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(errorMessage));
    }
}
