package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.PatronEditor;
import com.mycompany.myapp.domain.enumeration.Editors;
import com.mycompany.myapp.domain.enumeration.Language;
import com.mycompany.myapp.repository.PatronEditorRepository;
import java.math.BigDecimal;
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
 * Integration tests for the {@link PatronEditorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PatronEditorResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_PRINT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PRINT_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_NUMBER = "BBBBBBBBBB";

    private static final Editors DEFAULT_EDITOR = Editors.BURDA;
    private static final Editors UPDATED_EDITOR = Editors.LA_MAISON_VICTOR;

    private static final Language DEFAULT_LANGUAGE = Language.FRENCH;
    private static final Language UPDATED_LANGUAGE = Language.ENGLISH;

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/patron-editors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PatronEditorRepository patronEditorRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPatronEditorMockMvc;

    private PatronEditor patronEditor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PatronEditor createEntity(EntityManager em) {
        PatronEditor patronEditor = new PatronEditor()
            .name(DEFAULT_NAME)
            .printDate(DEFAULT_PRINT_DATE)
            .number(DEFAULT_NUMBER)
            .editor(DEFAULT_EDITOR)
            .language(DEFAULT_LANGUAGE)
            .price(DEFAULT_PRICE)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        return patronEditor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PatronEditor createUpdatedEntity(EntityManager em) {
        PatronEditor patronEditor = new PatronEditor()
            .name(UPDATED_NAME)
            .printDate(UPDATED_PRINT_DATE)
            .number(UPDATED_NUMBER)
            .editor(UPDATED_EDITOR)
            .language(UPDATED_LANGUAGE)
            .price(UPDATED_PRICE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        return patronEditor;
    }

    @BeforeEach
    public void initTest() {
        patronEditor = createEntity(em);
    }

    @Test
    @Transactional
    void createPatronEditor() throws Exception {
        int databaseSizeBeforeCreate = patronEditorRepository.findAll().size();
        // Create the PatronEditor
        restPatronEditorMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(patronEditor))
            )
            .andExpect(status().isCreated());

        // Validate the PatronEditor in the database
        List<PatronEditor> patronEditorList = patronEditorRepository.findAll();
        assertThat(patronEditorList).hasSize(databaseSizeBeforeCreate + 1);
        PatronEditor testPatronEditor = patronEditorList.get(patronEditorList.size() - 1);
        assertThat(testPatronEditor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPatronEditor.getPrintDate()).isEqualTo(DEFAULT_PRINT_DATE);
        assertThat(testPatronEditor.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testPatronEditor.getEditor()).isEqualTo(DEFAULT_EDITOR);
        assertThat(testPatronEditor.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
        assertThat(testPatronEditor.getPrice()).isEqualByComparingTo(DEFAULT_PRICE);
        assertThat(testPatronEditor.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testPatronEditor.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createPatronEditorWithExistingId() throws Exception {
        // Create the PatronEditor with an existing ID
        patronEditor.setId(1L);

        int databaseSizeBeforeCreate = patronEditorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPatronEditorMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(patronEditor))
            )
            .andExpect(status().isBadRequest());

        // Validate the PatronEditor in the database
        List<PatronEditor> patronEditorList = patronEditorRepository.findAll();
        assertThat(patronEditorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPatronEditors() throws Exception {
        // Initialize the database
        patronEditorRepository.saveAndFlush(patronEditor);

        // Get all the patronEditorList
        restPatronEditorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(patronEditor.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].printDate").value(hasItem(DEFAULT_PRINT_DATE.toString())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].editor").value(hasItem(DEFAULT_EDITOR.toString())))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }

    @Test
    @Transactional
    void getPatronEditor() throws Exception {
        // Initialize the database
        patronEditorRepository.saveAndFlush(patronEditor);

        // Get the patronEditor
        restPatronEditorMockMvc
            .perform(get(ENTITY_API_URL_ID, patronEditor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(patronEditor.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.printDate").value(DEFAULT_PRINT_DATE.toString()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.editor").value(DEFAULT_EDITOR.toString()))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE.toString()))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }

    @Test
    @Transactional
    void getNonExistingPatronEditor() throws Exception {
        // Get the patronEditor
        restPatronEditorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPatronEditor() throws Exception {
        // Initialize the database
        patronEditorRepository.saveAndFlush(patronEditor);

        int databaseSizeBeforeUpdate = patronEditorRepository.findAll().size();

        // Update the patronEditor
        PatronEditor updatedPatronEditor = patronEditorRepository.findById(patronEditor.getId()).get();
        // Disconnect from session so that the updates on updatedPatronEditor are not directly saved in db
        em.detach(updatedPatronEditor);
        updatedPatronEditor
            .name(UPDATED_NAME)
            .printDate(UPDATED_PRINT_DATE)
            .number(UPDATED_NUMBER)
            .editor(UPDATED_EDITOR)
            .language(UPDATED_LANGUAGE)
            .price(UPDATED_PRICE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restPatronEditorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPatronEditor.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPatronEditor))
            )
            .andExpect(status().isOk());

        // Validate the PatronEditor in the database
        List<PatronEditor> patronEditorList = patronEditorRepository.findAll();
        assertThat(patronEditorList).hasSize(databaseSizeBeforeUpdate);
        PatronEditor testPatronEditor = patronEditorList.get(patronEditorList.size() - 1);
        assertThat(testPatronEditor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPatronEditor.getPrintDate()).isEqualTo(UPDATED_PRINT_DATE);
        assertThat(testPatronEditor.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testPatronEditor.getEditor()).isEqualTo(UPDATED_EDITOR);
        assertThat(testPatronEditor.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testPatronEditor.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
        assertThat(testPatronEditor.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testPatronEditor.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingPatronEditor() throws Exception {
        int databaseSizeBeforeUpdate = patronEditorRepository.findAll().size();
        patronEditor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPatronEditorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, patronEditor.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(patronEditor))
            )
            .andExpect(status().isBadRequest());

        // Validate the PatronEditor in the database
        List<PatronEditor> patronEditorList = patronEditorRepository.findAll();
        assertThat(patronEditorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPatronEditor() throws Exception {
        int databaseSizeBeforeUpdate = patronEditorRepository.findAll().size();
        patronEditor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatronEditorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(patronEditor))
            )
            .andExpect(status().isBadRequest());

        // Validate the PatronEditor in the database
        List<PatronEditor> patronEditorList = patronEditorRepository.findAll();
        assertThat(patronEditorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPatronEditor() throws Exception {
        int databaseSizeBeforeUpdate = patronEditorRepository.findAll().size();
        patronEditor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatronEditorMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(patronEditor))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PatronEditor in the database
        List<PatronEditor> patronEditorList = patronEditorRepository.findAll();
        assertThat(patronEditorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePatronEditorWithPatch() throws Exception {
        // Initialize the database
        patronEditorRepository.saveAndFlush(patronEditor);

        int databaseSizeBeforeUpdate = patronEditorRepository.findAll().size();

        // Update the patronEditor using partial update
        PatronEditor partialUpdatedPatronEditor = new PatronEditor();
        partialUpdatedPatronEditor.setId(patronEditor.getId());

        partialUpdatedPatronEditor.name(UPDATED_NAME).printDate(UPDATED_PRINT_DATE).language(UPDATED_LANGUAGE);

        restPatronEditorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPatronEditor.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPatronEditor))
            )
            .andExpect(status().isOk());

        // Validate the PatronEditor in the database
        List<PatronEditor> patronEditorList = patronEditorRepository.findAll();
        assertThat(patronEditorList).hasSize(databaseSizeBeforeUpdate);
        PatronEditor testPatronEditor = patronEditorList.get(patronEditorList.size() - 1);
        assertThat(testPatronEditor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPatronEditor.getPrintDate()).isEqualTo(UPDATED_PRINT_DATE);
        assertThat(testPatronEditor.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testPatronEditor.getEditor()).isEqualTo(DEFAULT_EDITOR);
        assertThat(testPatronEditor.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testPatronEditor.getPrice()).isEqualByComparingTo(DEFAULT_PRICE);
        assertThat(testPatronEditor.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testPatronEditor.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdatePatronEditorWithPatch() throws Exception {
        // Initialize the database
        patronEditorRepository.saveAndFlush(patronEditor);

        int databaseSizeBeforeUpdate = patronEditorRepository.findAll().size();

        // Update the patronEditor using partial update
        PatronEditor partialUpdatedPatronEditor = new PatronEditor();
        partialUpdatedPatronEditor.setId(patronEditor.getId());

        partialUpdatedPatronEditor
            .name(UPDATED_NAME)
            .printDate(UPDATED_PRINT_DATE)
            .number(UPDATED_NUMBER)
            .editor(UPDATED_EDITOR)
            .language(UPDATED_LANGUAGE)
            .price(UPDATED_PRICE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restPatronEditorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPatronEditor.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPatronEditor))
            )
            .andExpect(status().isOk());

        // Validate the PatronEditor in the database
        List<PatronEditor> patronEditorList = patronEditorRepository.findAll();
        assertThat(patronEditorList).hasSize(databaseSizeBeforeUpdate);
        PatronEditor testPatronEditor = patronEditorList.get(patronEditorList.size() - 1);
        assertThat(testPatronEditor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPatronEditor.getPrintDate()).isEqualTo(UPDATED_PRINT_DATE);
        assertThat(testPatronEditor.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testPatronEditor.getEditor()).isEqualTo(UPDATED_EDITOR);
        assertThat(testPatronEditor.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testPatronEditor.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
        assertThat(testPatronEditor.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testPatronEditor.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingPatronEditor() throws Exception {
        int databaseSizeBeforeUpdate = patronEditorRepository.findAll().size();
        patronEditor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPatronEditorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, patronEditor.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(patronEditor))
            )
            .andExpect(status().isBadRequest());

        // Validate the PatronEditor in the database
        List<PatronEditor> patronEditorList = patronEditorRepository.findAll();
        assertThat(patronEditorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPatronEditor() throws Exception {
        int databaseSizeBeforeUpdate = patronEditorRepository.findAll().size();
        patronEditor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatronEditorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(patronEditor))
            )
            .andExpect(status().isBadRequest());

        // Validate the PatronEditor in the database
        List<PatronEditor> patronEditorList = patronEditorRepository.findAll();
        assertThat(patronEditorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPatronEditor() throws Exception {
        int databaseSizeBeforeUpdate = patronEditorRepository.findAll().size();
        patronEditor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatronEditorMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(patronEditor))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PatronEditor in the database
        List<PatronEditor> patronEditorList = patronEditorRepository.findAll();
        assertThat(patronEditorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePatronEditor() throws Exception {
        // Initialize the database
        patronEditorRepository.saveAndFlush(patronEditor);

        int databaseSizeBeforeDelete = patronEditorRepository.findAll().size();

        // Delete the patronEditor
        restPatronEditorMockMvc
            .perform(delete(ENTITY_API_URL_ID, patronEditor.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PatronEditor> patronEditorList = patronEditorRepository.findAll();
        assertThat(patronEditorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
