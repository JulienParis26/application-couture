package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Seller;
import com.mycompany.myapp.repository.SellerRepository;
import com.mycompany.myapp.service.SellerService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Seller}.
 */
@Service
@Transactional
public class SellerServiceImpl implements SellerService {

    private final Logger log = LoggerFactory.getLogger(SellerServiceImpl.class);

    private final SellerRepository sellerRepository;

    public SellerServiceImpl(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    @Override
    public Seller save(Seller seller) {
        log.debug("Request to save Seller : {}", seller);
        return sellerRepository.save(seller);
    }

    @Override
    public Seller update(Seller seller) {
        log.debug("Request to save Seller : {}", seller);
        return sellerRepository.save(seller);
    }

    @Override
    public Optional<Seller> partialUpdate(Seller seller) {
        log.debug("Request to partially update Seller : {}", seller);

        return sellerRepository
            .findById(seller.getId())
            .map(existingSeller -> {
                if (seller.getName() != null) {
                    existingSeller.setName(seller.getName());
                }
                if (seller.getWebSite() != null) {
                    existingSeller.setWebSite(seller.getWebSite());
                }

                return existingSeller;
            })
            .map(sellerRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Seller> findAll(Pageable pageable) {
        log.debug("Request to get all Sellers");
        return sellerRepository.findAll(pageable);
    }

    public Page<Seller> findAllWithEagerRelationships(Pageable pageable) {
        return sellerRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Seller> findOne(Long id) {
        log.debug("Request to get Seller : {}", id);
        return sellerRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Seller : {}", id);
        sellerRepository.deleteById(id);
    }
}
