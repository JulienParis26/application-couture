package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Seller;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class SellerRepositoryWithBagRelationshipsImpl implements SellerRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Seller> fetchBagRelationships(Optional<Seller> seller) {
        return seller.map(this::fetchTissus);
    }

    @Override
    public Page<Seller> fetchBagRelationships(Page<Seller> sellers) {
        return new PageImpl<>(fetchBagRelationships(sellers.getContent()), sellers.getPageable(), sellers.getTotalElements());
    }

    @Override
    public List<Seller> fetchBagRelationships(List<Seller> sellers) {
        return Optional.of(sellers).map(this::fetchTissus).orElse(Collections.emptyList());
    }

    Seller fetchTissus(Seller result) {
        return entityManager
            .createQuery("select seller from Seller seller left join fetch seller.tissus where seller is :seller", Seller.class)
            .setParameter("seller", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Seller> fetchTissus(List<Seller> sellers) {
        return entityManager
            .createQuery("select distinct seller from Seller seller left join fetch seller.tissus where seller in :sellers", Seller.class)
            .setParameter("sellers", sellers)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
