package com.badiar.badiar_study.academic.repository;

import com.badiar.badiar_study.academic.entity.AcademicSemester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface AcademicSemesterRepository extends JpaRepository<AcademicSemester, Long> {

    // Tous les semestres d'un niveau (ex: Module 1 et 2 de L1 GI 2024-2025)
    @Transactional(readOnly = true)
    List<AcademicSemester> findByProgramYearLevelIdOrderByDisplayOrderAsc(
            Long programYearLevelId
    );

    // Vérifier si un ordre existe déjà pour ce niveau (éviter doublons)
    @Transactional(readOnly = true)
    boolean existsByProgramYearLevelIdAndDisplayOrder(
            Long programYearLevelId,
            Integer displayOrder
    );

    // Trouver un semestre précis par niveau + ordre
    @Transactional(readOnly = true)
    Optional<AcademicSemester> findByProgramYearLevelIdAndDisplayOrder(
            Long programYearLevelId,
            Integer displayOrder
    );

    // Tous les semestres d'une filière pour une année donnée
    @Transactional(readOnly = true)
    @Query("""
        SELECT s FROM AcademicSemester s
        JOIN s.programYearLevel pyl
        WHERE pyl.academicProgram.id = :programId
        AND   pyl.academicYear.id    = :yearId
        ORDER BY pyl.levelNumber ASC, s.displayOrder ASC
    """)
    List<AcademicSemester> findByProgramAndYear(
            @Param("programId") Long programId,
            @Param("yearId")    Long yearId
    );

    // Compter le nombre de semestres d'un niveau
    @Transactional(readOnly = true)
    long countByProgramYearLevelId(Long programYearLevelId);
}
