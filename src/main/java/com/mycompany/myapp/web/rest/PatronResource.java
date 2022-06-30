package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Patron;
import com.mycompany.myapp.repository.PatronRepository;
import com.mycompany.myapp.service.PatronService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Patron}.
 */
@RestController
@RequestMapping("/api")
public class PatronResource {

    private final Logger log = LoggerFactory.getLogger(PatronResource.class);

    private static final String ENTITY_NAME = "patron";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PatronService patronService;

    private final PatronRepository patronRepository;

    public PatronResource(PatronService patronService, PatronRepository patronRepository) {
        this.patronService = patronService;
        this.patronRepository = patronRepository;
    }

    /**
     * {@code POST  /patrons} : Create a new patron.
     *
     * @param patron the patron to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new patron, or with status {@code 400 (Bad Request)} if the patron has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/patrons")
    public ResponseEntity<Patron> createPatron(@RequestBody Patron patron) throws URISyntaxException {
        log.debug("REST request to save Patron : {}", patron);
        if (patron.getId() != null) {
            throw new BadRequestAlertException("A new patron cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Patron result = patronService.save(patron);
        return ResponseEntity
            .created(new URI("/api/patrons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /patrons/:id} : Updates an existing patron.
     *
     * @param id the id of the patron to save.
     * @param patron the patron to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated patron,
     * or with status {@code 400 (Bad Request)} if the patron is not valid,
     * or with status {@code 500 (Internal Server Error)} if the patron couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/patrons/{id}")
    public ResponseEntity<Patron> updatePatron(@PathVariable(value = "id", required = false) final Long id, @RequestBody Patron patron)
        throws URISyntaxException {
        log.debug("REST request to update Patron : {}, {}", id, patron);
        if (patron.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, patron.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!patronRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Patron result = patronService.update(patron);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, patron.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /patrons/:id} : Partial updates given fields of an existing patron, field will ignore if it is null
     *
     * @param id the id of the patron to save.
     * @param patron the patron to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated patron,
     * or with status {@code 400 (Bad Request)} if the patron is not valid,
     * or with status {@code 404 (Not Found)} if the patron is not found,
     * or with status {@code 500 (Internal Server Error)} if the patron couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/patrons/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Patron> partialUpdatePatron(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Patron patron
    ) throws URISyntaxException {
        log.debug("REST request to partial update Patron partially : {}, {}", id, patron);
        if (patron.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, patron.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!patronRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Patron> result = patronService.partialUpdate(patron);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, patron.getId().toString())
        );
    }

    /**
     * {@code GET  /patrons} : get all the patrons.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of patrons in body.
     */
    @GetMapping("/patrons")
    public ResponseEntity<List<Patron>> getAllPatrons(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Patrons");
        Page<Patron> page = patronService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /patrons/:id} : get the "id" patron.
     *
     * @param id the id of the patron to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the patron, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/patrons/{id}")
    public ResponseEntity<Patron> getPatron(@PathVariable Long id) {
        log.debug("REST request to get Patron : {}", id);
        Optional<Patron> patron = patronService.findOne(id);
        return ResponseUtil.wrapOrNotFound(patron);
    }

    /**
     * {@code DELETE  /patrons/:id} : delete the "id" patron.
     *
     * @param id the id of the patron to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/patrons/{id}")
    public ResponseEntity<Void> deletePatron(@PathVariable Long id) {
        log.debug("REST request to delete Patron : {}", id);
        patronService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
