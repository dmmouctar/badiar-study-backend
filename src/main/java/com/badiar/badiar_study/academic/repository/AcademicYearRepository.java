package com.badiar.badiar_study.academic.repository;

import com.badiar.badiar_study.academic.entity.AcademicYear;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface AcademicYearRepository extends JpaRepository<AcademicYear, Long> {

    @Transactional(readOnly = true)
    Optional<AcademicYear> findByName(String name);

    @Transactional(readOnly = true)
    boolean existsByName(String name);

    @Transactional(readOnly = true)
    boolean existsByNameAndIdNot(String name, Long id);

    @Transactional(readOnly = true)
    List<AcademicYear> findByIsActiveTrueOrderByNameDesc();

    // Recherche + filtre + pagination
    @Transactional(readOnly = true)
    @Query("""
        SELECT y FROM AcademicYear y
        WHERE (:keyword IS NULL OR LOWER(y.name) LIKE LOWER(CONCAT('%', :keyword, '%')))
        AND   (:isActive IS NULL OR y.isActive = :isActive)
        ORDER BY y.name DESC
    """)
    Page<AcademicYear> searchWithFilters(
            @Param("keyword")  String  keyword,
            @Param("isActive") Boolean isActive,
            Pageable           pageable
    );
}
