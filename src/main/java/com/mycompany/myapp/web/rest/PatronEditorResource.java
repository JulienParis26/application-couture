package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.PatronEditor;
import com.mycompany.myapp.repository.PatronEditorRepository;
import com.mycompany.myapp.service.PatronEditorService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.PatronEditor}.
 */
@RestController
@RequestMapping("/api")
public class PatronEditorResource {

    private final Logger log = LoggerFactory.getLogger(PatronEditorResource.class);

    private static final String ENTITY_NAME = "patronEditor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PatronEditorService patronEditorService;

    private final PatronEditorRepository patronEditorRepository;

    public PatronEditorResource(PatronEditorService patronEditorService, PatronEditorRepository patronEditorRepository) {
        this.patronEditorService = patronEditorService;
        this.patronEditorRepository = patronEditorRepository;
    }

    /**
     * {@code POST  /patron-editors} : Create a new patronEditor.
     *
     * @param patronEditor the patronEditor to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new patronEditor, or with status {@code 400 (Bad Request)} if the patronEditor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/patron-editors")
    public ResponseEntity<PatronEditor> createPatronEditor(@RequestBody PatronEditor patronEditor) throws URISyntaxException {
        log.debug("REST request to save PatronEditor : {}", patronEditor);
        if (patronEditor.getId() != null) {
            throw new BadRequestAlertException("A new patronEditor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PatronEditor result = patronEditorService.save(patronEditor);
        return ResponseEntity
            .created(new URI("/api/patron-editors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /patron-editors/:id} : Updates an existing patronEditor.
     *
     * @param id the id of the patronEditor to save.
     * @param patronEditor the patronEditor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated patronEditor,
     * or with status {@code 400 (Bad Request)} if the patronEditor is not valid,
     * or with status {@code 500 (Internal Server Error)} if the patronEditor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/patron-editors/{id}")
    public ResponseEntity<PatronEditor> updatePatronEditor(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PatronEditor patronEditor
    ) throws URISyntaxException {
        log.debug("REST request to update PatronEditor : {}, {}", id, patronEditor);
        if (patronEditor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, patronEditor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!patronEditorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PatronEditor result = patronEditorService.update(patronEditor);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, patronEditor.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /patron-editors/:id} : Partial updates given fields of an existing patronEditor, field will ignore if it is null
     *
     * @param id the id of the patronEditor to save.
     * @param patronEditor the patronEditor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated patronEditor,
     * or with status {@code 400 (Bad Request)} if the patronEditor is not valid,
     * or with status {@code 404 (Not Found)} if the patronEditor is not found,
     * or with status {@code 500 (Internal Server Error)} if the patronEditor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/patron-editors/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PatronEditor> partialUpdatePatronEditor(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PatronEditor patronEditor
    ) throws URISyntaxException {
        log.debug("REST request to partial update PatronEditor partially : {}, {}", id, patronEditor);
        if (patronEditor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, patronEditor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!patronEditorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PatronEditor> result = patronEditorService.partialUpdate(patronEditor);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, patronEditor.getId().toString())
        );
    }

    /**
     * {@code GET  /patron-editors} : get all the patronEditors.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of patronEditors in body.
     */
    @GetMapping("/patron-editors")
    public ResponseEntity<List<PatronEditor>> getAllPatronEditors(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of PatronEditors");
        Page<PatronEditor> page = patronEditorService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /patron-editors/:id} : get the "id" patronEditor.
     *
     * @param id the id of the patronEditor to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the patronEditor, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/patron-editors/{id}")
    public ResponseEntity<PatronEditor> getPatronEditor(@PathVariable Long id) {
        log.debug("REST request to get PatronEditor : {}", id);
        Optional<PatronEditor> patronEditor = patronEditorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(patronEditor);
    }

    /**
     * {@code DELETE  /patron-editors/:id} : delete the "id" patronEditor.
     *
     * @param id the id of the patronEditor to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/patron-editors/{id}")
    public ResponseEntity<Void> deletePatronEditor(@PathVariable Long id) {
        log.debug("REST request to delete PatronEditor : {}", id);
        patronEditorService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
