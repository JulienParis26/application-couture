package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Project;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Project}.
 */
public interface ProjectService {
    /**
     * Save a project.
     *
     * @param project the entity to save.
     * @return the persisted entity.
     */
    Project save(Project project);

    /**
     * Updates a project.
     *
     * @param project the entity to update.
     * @return the persisted entity.
     */
    Project update(Project project);

    /**
     * Partially updates a project.
     *
     * @param project the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Project> partialUpdate(Project project);

    /**
     * Get all the projects.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Project> findAll(Pageable pageable);

    /**
     * Get all the projects with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Project> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" project.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Project> findOne(Long id);

    /**
     * Delete the "id" project.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
