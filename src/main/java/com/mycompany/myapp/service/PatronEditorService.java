package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.PatronEditor;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link PatronEditor}.
 */
public interface PatronEditorService {
    /**
     * Save a patronEditor.
     *
     * @param patronEditor the entity to save.
     * @return the persisted entity.
     */
    PatronEditor save(PatronEditor patronEditor);

    /**
     * Updates a patronEditor.
     *
     * @param patronEditor the entity to update.
     * @return the persisted entity.
     */
    PatronEditor update(PatronEditor patronEditor);

    /**
     * Partially updates a patronEditor.
     *
     * @param patronEditor the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PatronEditor> partialUpdate(PatronEditor patronEditor);

    /**
     * Get all the patronEditors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PatronEditor> findAll(Pageable pageable);

    /**
     * Get the "id" patronEditor.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PatronEditor> findOne(Long id);

    /**
     * Delete the "id" patronEditor.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
