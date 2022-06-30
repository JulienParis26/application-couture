package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.PatronEditor;
import com.mycompany.myapp.repository.PatronEditorRepository;
import com.mycompany.myapp.service.PatronEditorService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PatronEditor}.
 */
@Service
@Transactional
public class PatronEditorServiceImpl implements PatronEditorService {

    private final Logger log = LoggerFactory.getLogger(PatronEditorServiceImpl.class);

    private final PatronEditorRepository patronEditorRepository;

    public PatronEditorServiceImpl(PatronEditorRepository patronEditorRepository) {
        this.patronEditorRepository = patronEditorRepository;
    }

    @Override
    public PatronEditor save(PatronEditor patronEditor) {
        log.debug("Request to save PatronEditor : {}", patronEditor);
        return patronEditorRepository.save(patronEditor);
    }

    @Override
    public PatronEditor update(PatronEditor patronEditor) {
        log.debug("Request to save PatronEditor : {}", patronEditor);
        return patronEditorRepository.save(patronEditor);
    }

    @Override
    public Optional<PatronEditor> partialUpdate(PatronEditor patronEditor) {
        log.debug("Request to partially update PatronEditor : {}", patronEditor);

        return patronEditorRepository
            .findById(patronEditor.getId())
            .map(existingPatronEditor -> {
                if (patronEditor.getName() != null) {
                    existingPatronEditor.setName(patronEditor.getName());
                }
                if (patronEditor.getPrintDate() != null) {
                    existingPatronEditor.setPrintDate(patronEditor.getPrintDate());
                }
                if (patronEditor.getNumber() != null) {
                    existingPatronEditor.setNumber(patronEditor.getNumber());
                }
                if (patronEditor.getEditor() != null) {
                    existingPatronEditor.setEditor(patronEditor.getEditor());
                }
                if (patronEditor.getLanguage() != null) {
                    existingPatronEditor.setLanguage(patronEditor.getLanguage());
                }
                if (patronEditor.getPrice() != null) {
                    existingPatronEditor.setPrice(patronEditor.getPrice());
                }
                if (patronEditor.getImage() != null) {
                    existingPatronEditor.setImage(patronEditor.getImage());
                }
                if (patronEditor.getImageContentType() != null) {
                    existingPatronEditor.setImageContentType(patronEditor.getImageContentType());
                }

                return existingPatronEditor;
            })
            .map(patronEditorRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PatronEditor> findAll(Pageable pageable) {
        log.debug("Request to get all PatronEditors");
        return patronEditorRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PatronEditor> findOne(Long id) {
        log.debug("Request to get PatronEditor : {}", id);
        return patronEditorRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PatronEditor : {}", id);
        patronEditorRepository.deleteById(id);
    }
}
