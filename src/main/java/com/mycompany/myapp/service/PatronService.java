package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Patron;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Patron}.
 */
public interface PatronService {
    /**
     * Save a patron.
     *
     * @param patron the entity to save.
     * @return the persisted entity.
     */
    Patron save(Patron patron);

    /**
     * Updates a patron.
     *
     * @param patron the entity to update.
     * @return the persisted entity.
     */
    Patron update(Patron patron);

    /**
     * Partially updates a patron.
     *
     * @param patron the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Patron> partialUpdate(Patron patron);

    /**
     * Get all the patrons.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Patron> findAll(Pageable pageable);

    /**
     * Get the "id" patron.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Patron> findOne(Long id);

    /**
     * Delete the "id" patron.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
