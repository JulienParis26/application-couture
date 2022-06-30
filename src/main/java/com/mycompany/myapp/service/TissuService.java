package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Tissu;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Tissu}.
 */
public interface TissuService {
    /**
     * Save a tissu.
     *
     * @param tissu the entity to save.
     * @return the persisted entity.
     */
    Tissu save(Tissu tissu);

    /**
     * Updates a tissu.
     *
     * @param tissu the entity to update.
     * @return the persisted entity.
     */
    Tissu update(Tissu tissu);

    /**
     * Partially updates a tissu.
     *
     * @param tissu the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Tissu> partialUpdate(Tissu tissu);

    /**
     * Get all the tissus.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Tissu> findAll(Pageable pageable);

    /**
     * Get the "id" tissu.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Tissu> findOne(Long id);

    /**
     * Delete the "id" tissu.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
