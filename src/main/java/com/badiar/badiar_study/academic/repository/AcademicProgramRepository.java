package com.badiar.badiar_study.academic.repository;

import com.badiar.badiar_study.academic.entity.AcademicProgram;
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
public interface AcademicProgramRepository extends JpaRepository<AcademicProgram, Long> {

    @Transactional(readOnly = true)
    Optional<AcademicProgram> findByNameIgnoreCase(String name);

    @Transactional(readOnly = true)
    boolean existsByNameIgnoreCase(String name);

    @Transactional(readOnly = true)
    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);

    @Transactional(readOnly = true)
    List<AcademicProgram> findByIsActiveTrueOrderByNameAsc();

    @Transactional(readOnly = true)
    List<AcademicProgram> findByGradingScale(Integer gradingScale);

    // Recherche + filtre + tri + pagination
    @Transactional(readOnly = true)
    @Query("""
        SELECT p FROM AcademicProgram p
        WHERE (:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')))
        AND   (:gradingScale IS NULL OR p.gradingScale = :gradingScale)
        AND   (:isActive IS NULL OR p.isActive = :isActive)
        ORDER BY p.name ASC
    """)
    Page<AcademicProgram> searchWithFilters(
            @Param("keyword")      String  keyword,
            @Param("gradingScale") Integer gradingScale,
            @Param("isActive")     Boolean isActive,
            Pageable               pageable
    );
}
