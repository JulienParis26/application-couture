package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Patron;
import com.mycompany.myapp.domain.enumeration.Category;
import com.mycompany.myapp.domain.enumeration.PatronType;
import com.mycompany.myapp.repository.PatronRepository;
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
 * Integration tests for the {@link PatronResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PatronResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_REF = "AAAAAAAAAA";
    private static final String UPDATED_REF = "BBBBBBBBBB";

    private static final PatronType DEFAULT_TYPE = PatronType.PAPER;
    private static final PatronType UPDATED_TYPE = PatronType.PDF;

    private static final Category DEFAULT_CATEGORY = Category.KIDS;
    private static final Category UPDATED_CATEGORY = Category.MAN;

    private static final Integer DEFAULT_SIZE_MIN = 1;
    private static final Integer UPDATED_SIZE_MIN = 2;

    private static final Integer DEFAULT_SIZE_MAX = 1;
    private static final Integer UPDATED_SIZE_MAX = 2;

    private static final LocalDate DEFAULT_BUY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BUY_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/patrons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PatronRepository patronRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPatronMockMvc;

    private Patron patron;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Patron createEntity(EntityManager em) {
        Patron patron = new Patron()
            .name(DEFAULT_NAME)
            .ref(DEFAULT_REF)
            .type(DEFAULT_TYPE)
            .category(DEFAULT_CATEGORY)
            .sizeMin(DEFAULT_SIZE_MIN)
            .sizeMax(DEFAULT_SIZE_MAX)
            .buyDate(DEFAULT_BUY_DATE)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        return patron;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Patron createUpdatedEntity(EntityManager em) {
        Patron patron = new Patron()
            .name(UPDATED_NAME)
            .ref(UPDATED_REF)
            .type(UPDATED_TYPE)
            .category(UPDATED_CATEGORY)
            .sizeMin(UPDATED_SIZE_MIN)
            .sizeMax(UPDATED_SIZE_MAX)
            .buyDate(UPDATED_BUY_DATE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        return patron;
    }

    @BeforeEach
    public void initTest() {
        patron = createEntity(em);
    }

    @Test
    @Transactional
    void createPatron() throws Exception {
        int databaseSizeBeforeCreate = patronRepository.findAll().size();
        // Create the Patron
        restPatronMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patron))
            )
            .andExpect(status().isCreated());

        // Validate the Patron in the database
        List<Patron> patronList = patronRepository.findAll();
        assertThat(patronList).hasSize(databaseSizeBeforeCreate + 1);
        Patron testPatron = patronList.get(patronList.size() - 1);
        assertThat(testPatron.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPatron.getRef()).isEqualTo(DEFAULT_REF);
        assertThat(testPatron.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testPatron.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testPatron.getSizeMin()).isEqualTo(DEFAULT_SIZE_MIN);
        assertThat(testPatron.getSizeMax()).isEqualTo(DEFAULT_SIZE_MAX);
        assertThat(testPatron.getBuyDate()).isEqualTo(DEFAULT_BUY_DATE);
        assertThat(testPatron.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testPatron.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createPatronWithExistingId() throws Exception {
        // Create the Patron with an existing ID
        patron.setId(1L);

        int databaseSizeBeforeCreate = patronRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPatronMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patron))
            )
            .andExpect(status().isBadRequest());

        // Validate the Patron in the database
        List<Patron> patronList = patronRepository.findAll();
        assertThat(patronList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPatrons() throws Exception {
        // Initialize the database
        patronRepository.saveAndFlush(patron);

        // Get all the patronList
        restPatronMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(patron.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].ref").value(hasItem(DEFAULT_REF)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].sizeMin").value(hasItem(DEFAULT_SIZE_MIN)))
            .andExpect(jsonPath("$.[*].sizeMax").value(hasItem(DEFAULT_SIZE_MAX)))
            .andExpect(jsonPath("$.[*].buyDate").value(hasItem(DEFAULT_BUY_DATE.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }

    @Test
    @Transactional
    void getPatron() throws Exception {
        // Initialize the database
        patronRepository.saveAndFlush(patron);

        // Get the patron
        restPatronMockMvc
            .perform(get(ENTITY_API_URL_ID, patron.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(patron.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.ref").value(DEFAULT_REF))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()))
            .andExpect(jsonPath("$.sizeMin").value(DEFAULT_SIZE_MIN))
            .andExpect(jsonPath("$.sizeMax").value(DEFAULT_SIZE_MAX))
            .andExpect(jsonPath("$.buyDate").value(DEFAULT_BUY_DATE.toString()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }

    @Test
    @Transactional
    void getNonExistingPatron() throws Exception {
        // Get the patron
        restPatronMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPatron() throws Exception {
        // Initialize the database
        patronRepository.saveAndFlush(patron);

        int databaseSizeBeforeUpdate = patronRepository.findAll().size();

        // Update the patron
        Patron updatedPatron = patronRepository.findById(patron.getId()).get();
        // Disconnect from session so that the updates on updatedPatron are not directly saved in db
        em.detach(updatedPatron);
        updatedPatron
            .name(UPDATED_NAME)
            .ref(UPDATED_REF)
            .type(UPDATED_TYPE)
            .category(UPDATED_CATEGORY)
            .sizeMin(UPDATED_SIZE_MIN)
            .sizeMax(UPDATED_SIZE_MAX)
            .buyDate(UPDATED_BUY_DATE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restPatronMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPatron.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPatron))
            )
            .andExpect(status().isOk());

        // Validate the Patron in the database
        List<Patron> patronList = patronRepository.findAll();
        assertThat(patronList).hasSize(databaseSizeBeforeUpdate);
        Patron testPatron = patronList.get(patronList.size() - 1);
        assertThat(testPatron.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPatron.getRef()).isEqualTo(UPDATED_REF);
        assertThat(testPatron.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPatron.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testPatron.getSizeMin()).isEqualTo(UPDATED_SIZE_MIN);
        assertThat(testPatron.getSizeMax()).isEqualTo(UPDATED_SIZE_MAX);
        assertThat(testPatron.getBuyDate()).isEqualTo(UPDATED_BUY_DATE);
        assertThat(testPatron.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testPatron.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingPatron() throws Exception {
        int databaseSizeBeforeUpdate = patronRepository.findAll().size();
        patron.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPatronMockMvc
            .perform(
                put(ENTITY_API_URL_ID, patron.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(patron))
            )
            .andExpect(status().isBadRequest());

        // Validate the Patron in the database
        List<Patron> patronList = patronRepository.findAll();
        assertThat(patronList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPatron() throws Exception {
        int databaseSizeBeforeUpdate = patronRepository.findAll().size();
        patron.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatronMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(patron))
            )
            .andExpect(status().isBadRequest());

        // Validate the Patron in the database
        List<Patron> patronList = patronRepository.findAll();
        assertThat(patronList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPatron() throws Exception {
        int databaseSizeBeforeUpdate = patronRepository.findAll().size();
        patron.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatronMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patron))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Patron in the database
        List<Patron> patronList = patronRepository.findAll();
        assertThat(patronList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePatronWithPatch() throws Exception {
        // Initialize the database
        patronRepository.saveAndFlush(patron);

        int databaseSizeBeforeUpdate = patronRepository.findAll().size();

        // Update the patron using partial update
        Patron partialUpdatedPatron = new Patron();
        partialUpdatedPatron.setId(patron.getId());

        partialUpdatedPatron.name(UPDATED_NAME).type(UPDATED_TYPE).category(UPDATED_CATEGORY).buyDate(UPDATED_BUY_DATE);

        restPatronMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPatron.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPatron))
            )
            .andExpect(status().isOk());

        // Validate the Patron in the database
        List<Patron> patronList = patronRepository.findAll();
        assertThat(patronList).hasSize(databaseSizeBeforeUpdate);
        Patron testPatron = patronList.get(patronList.size() - 1);
        assertThat(testPatron.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPatron.getRef()).isEqualTo(DEFAULT_REF);
        assertThat(testPatron.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPatron.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testPatron.getSizeMin()).isEqualTo(DEFAULT_SIZE_MIN);
        assertThat(testPatron.getSizeMax()).isEqualTo(DEFAULT_SIZE_MAX);
        assertThat(testPatron.getBuyDate()).isEqualTo(UPDATED_BUY_DATE);
        assertThat(testPatron.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testPatron.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdatePatronWithPatch() throws Exception {
        // Initialize the database
        patronRepository.saveAndFlush(patron);

        int databaseSizeBeforeUpdate = patronRepository.findAll().size();

        // Update the patron using partial update
        Patron partialUpdatedPatron = new Patron();
        partialUpdatedPatron.setId(patron.getId());

        partialUpdatedPatron
            .name(UPDATED_NAME)
            .ref(UPDATED_REF)
            .type(UPDATED_TYPE)
            .category(UPDATED_CATEGORY)
            .sizeMin(UPDATED_SIZE_MIN)
            .sizeMax(UPDATED_SIZE_MAX)
            .buyDate(UPDATED_BUY_DATE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restPatronMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPatron.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPatron))
            )
            .andExpect(status().isOk());

        // Validate the Patron in the database
        List<Patron> patronList = patronRepository.findAll();
        assertThat(patronList).hasSize(databaseSizeBeforeUpdate);
        Patron testPatron = patronList.get(patronList.size() - 1);
        assertThat(testPatron.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPatron.getRef()).isEqualTo(UPDATED_REF);
        assertThat(testPatron.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPatron.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testPatron.getSizeMin()).isEqualTo(UPDATED_SIZE_MIN);
        assertThat(testPatron.getSizeMax()).isEqualTo(UPDATED_SIZE_MAX);
        assertThat(testPatron.getBuyDate()).isEqualTo(UPDATED_BUY_DATE);
        assertThat(testPatron.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testPatron.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingPatron() throws Exception {
        int databaseSizeBeforeUpdate = patronRepository.findAll().size();
        patron.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPatronMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, patron.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(patron))
            )
            .andExpect(status().isBadRequest());

        // Validate the Patron in the database
        List<Patron> patronList = patronRepository.findAll();
        assertThat(patronList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPatron() throws Exception {
        int databaseSizeBeforeUpdate = patronRepository.findAll().size();
        patron.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatronMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(patron))
            )
            .andExpect(status().isBadRequest());

        // Validate the Patron in the database
        List<Patron> patronList = patronRepository.findAll();
        assertThat(patronList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPatron() throws Exception {
        int databaseSizeBeforeUpdate = patronRepository.findAll().size();
        patron.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatronMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(patron))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Patron in the database
        List<Patron> patronList = patronRepository.findAll();
        assertThat(patronList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePatron() throws Exception {
        // Initialize the database
        patronRepository.saveAndFlush(patron);

        int databaseSizeBeforeDelete = patronRepository.findAll().size();

        // Delete the patron
        restPatronMockMvc
            .perform(delete(ENTITY_API_URL_ID, patron.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Patron> patronList = patronRepository.findAll();
        assertThat(patronList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
