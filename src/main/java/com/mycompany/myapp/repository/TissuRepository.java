package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Tissu;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Tissu entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TissuRepository extends JpaRepository<Tissu, Long> {}
