package com.badiar.badiar_study.academic.repository;

import com.badiar.badiar_study.academic.entity.Examination;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ExaminationRepository extends JpaRepository<Examination, Long> {

    // Tous les examens d'une matière (les 3 notes)
    @Transactional(readOnly = true)
    List<Examination> findBySubjectIdOrderByExamOrderAsc(Long subjectId);

    // Tous les examens d'un type donné (ex: tous les Devoirs Mensuels)
    @Transactional(readOnly = true)
    List<Examination> findByExaminationTypeIdOrderByExamDateAsc(Long examinationTypeId);

    // Vérifier si un examen existe déjà pour une matière + ordre donné
    @Transactional(readOnly = true)
    boolean existsBySubjectIdAndExamOrder(Long subjectId, Integer examOrder);

    @Transactional(readOnly = true)
    boolean existsBySubjectIdAndExamOrderAndIdNot(
            Long subjectId,
            Integer examOrder,
            Long id
    );

    // Tous les examens d'un semestre (via la matière)
    @Transactional(readOnly = true)
    @Query("""
        SELECT e FROM Examination e
        JOIN e.subject s
        WHERE s.semester.id = :semesterId
        ORDER BY s.name ASC, e.examOrder ASC
    """)
    List<Examination> findBySemesterId(@Param("semesterId") Long semesterId);

    // Tous les examens d'une filière pour une année (via semestre → niveau)
    @Transactional(readOnly = true)
    @Query("""
        SELECT e FROM Examination e
        JOIN e.subject s
        JOIN s.semester sem
        JOIN sem.programYearLevel pyl
        WHERE pyl.academicProgram.id = :programId
        AND   pyl.academicYear.id    = :yearId
        ORDER BY pyl.levelNumber ASC, sem.displayOrder ASC,
                 s.name ASC, e.examOrder ASC
    """)
    List<Examination> findByProgramAndYear(
            @Param("programId") Long programId,
            @Param("yearId")    Long yearId
    );

    // Recherche + filtre + pagination pour l'admin
    @Transactional(readOnly = true)
    @Query("""
        SELECT e FROM Examination e
        JOIN e.subject s
        JOIN e.examinationType t
        WHERE (:subjectId IS NULL OR s.id  = :subjectId)
        AND   (:typeId    IS NULL OR t.id  = :typeId)
        ORDER BY s.name ASC, e.examOrder ASC
    """)
    Page<Examination> searchWithFilters(
            @Param("subjectId") Long     subjectId,
            @Param("typeId")    Long     typeId,
            Pageable            pageable
    );
}
