package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Tissu;
import com.mycompany.myapp.repository.TissuRepository;
import com.mycompany.myapp.service.TissuService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Tissu}.
 */
@Service
@Transactional
public class TissuServiceImpl implements TissuService {

    private final Logger log = LoggerFactory.getLogger(TissuServiceImpl.class);

    private final TissuRepository tissuRepository;

    public TissuServiceImpl(TissuRepository tissuRepository) {
        this.tissuRepository = tissuRepository;
    }

    @Override
    public Tissu save(Tissu tissu) {
        log.debug("Request to save Tissu : {}", tissu);
        return tissuRepository.save(tissu);
    }

    @Override
    public Tissu update(Tissu tissu) {
        log.debug("Request to save Tissu : {}", tissu);
        return tissuRepository.save(tissu);
    }

    @Override
    public Optional<Tissu> partialUpdate(Tissu tissu) {
        log.debug("Request to partially update Tissu : {}", tissu);

        return tissuRepository
            .findById(tissu.getId())
            .map(existingTissu -> {
                if (tissu.getName() != null) {
                    existingTissu.setName(tissu.getName());
                }
                if (tissu.getRef() != null) {
                    existingTissu.setRef(tissu.getRef());
                }
                if (tissu.getColor() != null) {
                    existingTissu.setColor(tissu.getColor());
                }
                if (tissu.getBuySize() != null) {
                    existingTissu.setBuySize(tissu.getBuySize());
                }
                if (tissu.getType() != null) {
                    existingTissu.setType(tissu.getType());
                }
                if (tissu.getBuyDate() != null) {
                    existingTissu.setBuyDate(tissu.getBuyDate());
                }
                if (tissu.getImage() != null) {
                    existingTissu.setImage(tissu.getImage());
                }
                if (tissu.getImageContentType() != null) {
                    existingTissu.setImageContentType(tissu.getImageContentType());
                }

                return existingTissu;
            })
            .map(tissuRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Tissu> findAll(Pageable pageable) {
        log.debug("Request to get all Tissus");
        return tissuRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Tissu> findOne(Long id) {
        log.debug("Request to get Tissu : {}", id);
        return tissuRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Tissu : {}", id);
        tissuRepository.deleteById(id);
    }
}
