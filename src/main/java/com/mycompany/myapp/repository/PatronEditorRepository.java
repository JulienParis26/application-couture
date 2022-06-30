package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.PatronEditor;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PatronEditor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PatronEditorRepository extends JpaRepository<PatronEditor, Long> {}
