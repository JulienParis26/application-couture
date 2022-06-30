package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Tissu;
import com.mycompany.myapp.repository.TissuRepository;
import com.mycompany.myapp.service.TissuService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Tissu}.
 */
@RestController
@RequestMapping("/api")
public class TissuResource {

    private final Logger log = LoggerFactory.getLogger(TissuResource.class);

    private static final String ENTITY_NAME = "tissu";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TissuService tissuService;

    private final TissuRepository tissuRepository;

    public TissuResource(TissuService tissuService, TissuRepository tissuRepository) {
        this.tissuService = tissuService;
        this.tissuRepository = tissuRepository;
    }

    /**
     * {@code POST  /tissus} : Create a new tissu.
     *
     * @param tissu the tissu to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tissu, or with status {@code 400 (Bad Request)} if the tissu has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tissus")
    public ResponseEntity<Tissu> createTissu(@RequestBody Tissu tissu) throws URISyntaxException {
        log.debug("REST request to save Tissu : {}", tissu);
        if (tissu.getId() != null) {
            throw new BadRequestAlertException("A new tissu cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Tissu result = tissuService.save(tissu);
        return ResponseEntity
            .created(new URI("/api/tissus/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tissus/:id} : Updates an existing tissu.
     *
     * @param id the id of the tissu to save.
     * @param tissu the tissu to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tissu,
     * or with status {@code 400 (Bad Request)} if the tissu is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tissu couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tissus/{id}")
    public ResponseEntity<Tissu> updateTissu(@PathVariable(value = "id", required = false) final Long id, @RequestBody Tissu tissu)
        throws URISyntaxException {
        log.debug("REST request to update Tissu : {}, {}", id, tissu);
        if (tissu.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tissu.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tissuRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Tissu result = tissuService.update(tissu);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tissu.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tissus/:id} : Partial updates given fields of an existing tissu, field will ignore if it is null
     *
     * @param id the id of the tissu to save.
     * @param tissu the tissu to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tissu,
     * or with status {@code 400 (Bad Request)} if the tissu is not valid,
     * or with status {@code 404 (Not Found)} if the tissu is not found,
     * or with status {@code 500 (Internal Server Error)} if the tissu couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tissus/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Tissu> partialUpdateTissu(@PathVariable(value = "id", required = false) final Long id, @RequestBody Tissu tissu)
        throws URISyntaxException {
        log.debug("REST request to partial update Tissu partially : {}, {}", id, tissu);
        if (tissu.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tissu.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tissuRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Tissu> result = tissuService.partialUpdate(tissu);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tissu.getId().toString())
        );
    }

    /**
     * {@code GET  /tissus} : get all the tissus.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tissus in body.
     */
    @GetMapping("/tissus")
    public ResponseEntity<List<Tissu>> getAllTissus(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Tissus");
        Page<Tissu> page = tissuService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tissus/:id} : get the "id" tissu.
     *
     * @param id the id of the tissu to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tissu, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tissus/{id}")
    public ResponseEntity<Tissu> getTissu(@PathVariable Long id) {
        log.debug("REST request to get Tissu : {}", id);
        Optional<Tissu> tissu = tissuService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tissu);
    }

    /**
     * {@code DELETE  /tissus/:id} : delete the "id" tissu.
     *
     * @param id the id of the tissu to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tissus/{id}")
    public ResponseEntity<Void> deleteTissu(@PathVariable Long id) {
        log.debug("REST request to delete Tissu : {}", id);
        tissuService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
