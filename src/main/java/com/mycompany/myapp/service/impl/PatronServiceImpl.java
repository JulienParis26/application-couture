package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Patron;
import com.mycompany.myapp.repository.PatronRepository;
import com.mycompany.myapp.service.PatronService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Patron}.
 */
@Service
@Transactional
public class PatronServiceImpl implements PatronService {

    private final Logger log = LoggerFactory.getLogger(PatronServiceImpl.class);

    private final PatronRepository patronRepository;

    public PatronServiceImpl(PatronRepository patronRepository) {
        this.patronRepository = patronRepository;
    }

    @Override
    public Patron save(Patron patron) {
        log.debug("Request to save Patron : {}", patron);
        return patronRepository.save(patron);
    }

    @Override
    public Patron update(Patron patron) {
        log.debug("Request to save Patron : {}", patron);
        return patronRepository.save(patron);
    }

    @Override
    public Optional<Patron> partialUpdate(Patron patron) {
        log.debug("Request to partially update Patron : {}", patron);

        return patronRepository
            .findById(patron.getId())
            .map(existingPatron -> {
                if (patron.getName() != null) {
                    existingPatron.setName(patron.getName());
                }
                if (patron.getRef() != null) {
                    existingPatron.setRef(patron.getRef());
                }
                if (patron.getType() != null) {
                    existingPatron.setType(patron.getType());
                }
                if (patron.getCategory() != null) {
                    existingPatron.setCategory(patron.getCategory());
                }
                if (patron.getSizeMin() != null) {
                    existingPatron.setSizeMin(patron.getSizeMin());
                }
                if (patron.getSizeMax() != null) {
                    existingPatron.setSizeMax(patron.getSizeMax());
                }
                if (patron.getBuyDate() != null) {
                    existingPatron.setBuyDate(patron.getBuyDate());
                }
                if (patron.getImage() != null) {
                    existingPatron.setImage(patron.getImage());
                }
                if (patron.getImageContentType() != null) {
                    existingPatron.setImageContentType(patron.getImageContentType());
                }

                return existingPatron;
            })
            .map(patronRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Patron> findAll(Pageable pageable) {
        log.debug("Request to get all Patrons");
        return patronRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Patron> findOne(Long id) {
        log.debug("Request to get Patron : {}", id);
        return patronRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Patron : {}", id);
        patronRepository.deleteById(id);
    }
}
