package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Tissu;
import com.mycompany.myapp.domain.enumeration.TissuType;
import com.mycompany.myapp.repository.TissuRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link TissuResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TissuResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_REF = "AAAAAAAAAA";
    private static final String UPDATED_REF = "BBBBBBBBBB";

    private static final String DEFAULT_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_BUY_SIZE = "AAAAAAAAAA";
    private static final String UPDATED_BUY_SIZE = "BBBBBBBBBB";

    private static final TissuType DEFAULT_TYPE = TissuType.JERSEY;
    private static final TissuType UPDATED_TYPE = TissuType.MAILLE;

    private static final LocalDate DEFAULT_BUY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BUY_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/tissus";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TissuRepository tissuRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTissuMockMvc;

    private Tissu tissu;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tissu createEntity(EntityManager em) {
        Tissu tissu = new Tissu()
            .name(DEFAULT_NAME)
            .ref(DEFAULT_REF)
            .color(DEFAULT_COLOR)
            .buySize(DEFAULT_BUY_SIZE)
            .type(DEFAULT_TYPE)
            .buyDate(DEFAULT_BUY_DATE)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        return tissu;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tissu createUpdatedEntity(EntityManager em) {
        Tissu tissu = new Tissu()
            .name(UPDATED_NAME)
            .ref(UPDATED_REF)
            .color(UPDATED_COLOR)
            .buySize(UPDATED_BUY_SIZE)
            .type(UPDATED_TYPE)
            .buyDate(UPDATED_BUY_DATE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        return tissu;
    }

    @BeforeEach
    public void initTest() {
        tissu = createEntity(em);
    }

    @Test
    @Transactional
    void createTissu() throws Exception {
        int databaseSizeBeforeCreate = tissuRepository.findAll().size();
        // Create the Tissu
        restTissuMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tissu))
            )
            .andExpect(status().isCreated());

        // Validate the Tissu in the database
        List<Tissu> tissuList = tissuRepository.findAll();
        assertThat(tissuList).hasSize(databaseSizeBeforeCreate + 1);
        Tissu testTissu = tissuList.get(tissuList.size() - 1);
        assertThat(testTissu.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTissu.getRef()).isEqualTo(DEFAULT_REF);
        assertThat(testTissu.getColor()).isEqualTo(DEFAULT_COLOR);
        assertThat(testTissu.getBuySize()).isEqualTo(DEFAULT_BUY_SIZE);
        assertThat(testTissu.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testTissu.getBuyDate()).isEqualTo(DEFAULT_BUY_DATE);
        assertThat(testTissu.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testTissu.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createTissuWithExistingId() throws Exception {
        // Create the Tissu with an existing ID
        tissu.setId(1L);

        int databaseSizeBeforeCreate = tissuRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTissuMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tissu))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tissu in the database
        List<Tissu> tissuList = tissuRepository.findAll();
        assertThat(tissuList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTissus() throws Exception {
        // Initialize the database
        tissuRepository.saveAndFlush(tissu);

        // Get all the tissuList
        restTissuMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tissu.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].ref").value(hasItem(DEFAULT_REF)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].buySize").value(hasItem(DEFAULT_BUY_SIZE)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].buyDate").value(hasItem(DEFAULT_BUY_DATE.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }

    @Test
    @Transactional
    void getTissu() throws Exception {
        // Initialize the database
        tissuRepository.saveAndFlush(tissu);

        // Get the tissu
        restTissuMockMvc
            .perform(get(ENTITY_API_URL_ID, tissu.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tissu.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.ref").value(DEFAULT_REF))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR))
            .andExpect(jsonPath("$.buySize").value(DEFAULT_BUY_SIZE))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.buyDate").value(DEFAULT_BUY_DATE.toString()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }

    @Test
    @Transactional
    void getNonExistingTissu() throws Exception {
        // Get the tissu
        restTissuMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTissu() throws Exception {
        // Initialize the database
        tissuRepository.saveAndFlush(tissu);

        int databaseSizeBeforeUpdate = tissuRepository.findAll().size();

        // Update the tissu
        Tissu updatedTissu = tissuRepository.findById(tissu.getId()).get();
        // Disconnect from session so that the updates on updatedTissu are not directly saved in db
        em.detach(updatedTissu);
        updatedTissu
            .name(UPDATED_NAME)
            .ref(UPDATED_REF)
            .color(UPDATED_COLOR)
            .buySize(UPDATED_BUY_SIZE)
            .type(UPDATED_TYPE)
            .buyDate(UPDATED_BUY_DATE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restTissuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTissu.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTissu))
            )
            .andExpect(status().isOk());

        // Validate the Tissu in the database
        List<Tissu> tissuList = tissuRepository.findAll();
        assertThat(tissuList).hasSize(databaseSizeBeforeUpdate);
        Tissu testTissu = tissuList.get(tissuList.size() - 1);
        assertThat(testTissu.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTissu.getRef()).isEqualTo(UPDATED_REF);
        assertThat(testTissu.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testTissu.getBuySize()).isEqualTo(UPDATED_BUY_SIZE);
        assertThat(testTissu.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTissu.getBuyDate()).isEqualTo(UPDATED_BUY_DATE);
        assertThat(testTissu.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testTissu.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingTissu() throws Exception {
        int databaseSizeBeforeUpdate = tissuRepository.findAll().size();
        tissu.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTissuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tissu.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tissu))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tissu in the database
        List<Tissu> tissuList = tissuRepository.findAll();
        assertThat(tissuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTissu() throws Exception {
        int databaseSizeBeforeUpdate = tissuRepository.findAll().size();
        tissu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTissuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tissu))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tissu in the database
        List<Tissu> tissuList = tissuRepository.findAll();
        assertThat(tissuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTissu() throws Exception {
        int databaseSizeBeforeUpdate = tissuRepository.findAll().size();
        tissu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTissuMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tissu))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tissu in the database
        List<Tissu> tissuList = tissuRepository.findAll();
        assertThat(tissuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTissuWithPatch() throws Exception {
        // Initialize the database
        tissuRepository.saveAndFlush(tissu);

        int databaseSizeBeforeUpdate = tissuRepository.findAll().size();

        // Update the tissu using partial update
        Tissu partialUpdatedTissu = new Tissu();
        partialUpdatedTissu.setId(tissu.getId());

        partialUpdatedTissu.ref(UPDATED_REF).color(UPDATED_COLOR).type(UPDATED_TYPE).buyDate(UPDATED_BUY_DATE);

        restTissuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTissu.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTissu))
            )
            .andExpect(status().isOk());

        // Validate the Tissu in the database
        List<Tissu> tissuList = tissuRepository.findAll();
        assertThat(tissuList).hasSize(databaseSizeBeforeUpdate);
        Tissu testTissu = tissuList.get(tissuList.size() - 1);
        assertThat(testTissu.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTissu.getRef()).isEqualTo(UPDATED_REF);
        assertThat(testTissu.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testTissu.getBuySize()).isEqualTo(DEFAULT_BUY_SIZE);
        assertThat(testTissu.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTissu.getBuyDate()).isEqualTo(UPDATED_BUY_DATE);
        assertThat(testTissu.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testTissu.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateTissuWithPatch() throws Exception {
        // Initialize the database
        tissuRepository.saveAndFlush(tissu);

        int databaseSizeBeforeUpdate = tissuRepository.findAll().size();

        // Update the tissu using partial update
        Tissu partialUpdatedTissu = new Tissu();
        partialUpdatedTissu.setId(tissu.getId());

        partialUpdatedTissu
            .name(UPDATED_NAME)
            .ref(UPDATED_REF)
            .color(UPDATED_COLOR)
            .buySize(UPDATED_BUY_SIZE)
            .type(UPDATED_TYPE)
            .buyDate(UPDATED_BUY_DATE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restTissuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTissu.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTissu))
            )
            .andExpect(status().isOk());

        // Validate the Tissu in the database
        List<Tissu> tissuList = tissuRepository.findAll();
        assertThat(tissuList).hasSize(databaseSizeBeforeUpdate);
        Tissu testTissu = tissuList.get(tissuList.size() - 1);
        assertThat(testTissu.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTissu.getRef()).isEqualTo(UPDATED_REF);
        assertThat(testTissu.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testTissu.getBuySize()).isEqualTo(UPDATED_BUY_SIZE);
        assertThat(testTissu.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTissu.getBuyDate()).isEqualTo(UPDATED_BUY_DATE);
        assertThat(testTissu.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testTissu.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingTissu() throws Exception {
        int databaseSizeBeforeUpdate = tissuRepository.findAll().size();
        tissu.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTissuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tissu.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tissu))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tissu in the database
        List<Tissu> tissuList = tissuRepository.findAll();
        assertThat(tissuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTissu() throws Exception {
        int databaseSizeBeforeUpdate = tissuRepository.findAll().size();
        tissu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTissuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tissu))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tissu in the database
        List<Tissu> tissuList = tissuRepository.findAll();
        assertThat(tissuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTissu() throws Exception {
        int databaseSizeBeforeUpdate = tissuRepository.findAll().size();
        tissu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTissuMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tissu))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tissu in the database
        List<Tissu> tissuList = tissuRepository.findAll();
        assertThat(tissuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTissu() throws Exception {
        // Initialize the database
        tissuRepository.saveAndFlush(tissu);

        int databaseSizeBeforeDelete = tissuRepository.findAll().size();

        // Delete the tissu
        restTissuMockMvc
            .perform(delete(ENTITY_API_URL_ID, tissu.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tissu> tissuList = tissuRepository.findAll();
        assertThat(tissuList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
