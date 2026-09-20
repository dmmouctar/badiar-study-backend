package com.badiar.badiar_study.academic.repository;

import com.badiar.badiar_study.academic.entity.ProgramYearLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProgramYearLevelRepository extends JpaRepository<ProgramYearLevel, Long> {

    // Trouver un niveau précis (filière + année + numéro de niveau)
    @Transactional(readOnly = true)
    Optional<ProgramYearLevel> findByAcademicProgramIdAndAcademicYearIdAndLevelNumber(
            Long programId,
            Long yearId,
            Integer levelNumber
    );

    // Vérifier l'existence (pour éviter les doublons)
    @Transactional(readOnly = true)
    boolean existsByAcademicProgramIdAndAcademicYearIdAndLevelNumber(
            Long programId,
            Long yearId,
            Integer levelNumber
    );

    // Tous les niveaux d'une filière (tous les L1, L2... de GI)
    @Transactional(readOnly = true)
    List<ProgramYearLevel> findByAcademicProgramIdOrderByLevelNumberAsc(Long programId);

    // Tous les niveaux pour une année académique donnée
    @Transactional(readOnly = true)
    List<ProgramYearLevel> findByAcademicYearIdOrderByLevelNumberAsc(Long yearId);

    // Tous les niveaux d'une filière pour une année précise
    @Transactional(readOnly = true)
    List<ProgramYearLevel> findByAcademicProgramIdAndAcademicYearIdOrderByLevelNumberAsc(
            Long programId,
            Long yearId
    );

    // Recherche avec filtre pour l'admin
    @Transactional(readOnly = true)
    @Query("""
        SELECT pyl FROM ProgramYearLevel pyl
        JOIN pyl.academicProgram p
        JOIN pyl.academicYear y
        WHERE (:programId IS NULL OR p.id = :programId)
        AND   (:yearId    IS NULL OR y.id = :yearId)
        ORDER BY p.name ASC, pyl.levelNumber ASC
    """)
    List<ProgramYearLevel> searchWithFilters(
            @Param("programId") Long programId,
            @Param("yearId")    Long yearId
    );
}
