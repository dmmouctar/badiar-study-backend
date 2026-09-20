package com.badiar.badiar_study.academic.repository;

import com.badiar.badiar_study.academic.entity.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    // Toutes les matières d'un semestre précis
    @Transactional(readOnly = true)
    List<Subject> findBySemesterIdOrderByNameAsc(Long semesterId);

    // Toutes les matières d'une filière (via le double lien direct)
    @Transactional(readOnly = true)
    List<Subject> findByAcademicProgramIdOrderByNameAsc(Long programId);

    // Toutes les matières d'une filière pour un semestre précis
    @Transactional(readOnly = true)
    List<Subject> findByAcademicProgramIdAndSemesterIdOrderByNameAsc(
            Long programId,
            Long semesterId
    );

    // Vérifier si une matière existe déjà dans ce semestre (éviter doublons)
    @Transactional(readOnly = true)
    boolean existsByNameIgnoreCaseAndSemesterId(String name, Long semesterId);

    @Transactional(readOnly = true)
    boolean existsByNameIgnoreCaseAndSemesterIdAndIdNot(
            String name,
            Long semesterId,
            Long id
    );

    // Recherche + filtre + tri + pagination pour l'admin
    @Transactional(readOnly = true)
    @Query("""
        SELECT s FROM Subject s
        JOIN s.academicProgram p
        JOIN s.semester sem
        WHERE (:keyword   IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')))
        AND   (:programId IS NULL OR p.id   = :programId)
        AND   (:semId     IS NULL OR sem.id = :semId)
        ORDER BY s.name ASC
    """)
    Page<Subject> searchWithFilters(
            @Param("keyword")   String  keyword,
            @Param("programId") Long    programId,
            @Param("semId")     Long    semId,
            Pageable            pageable
    );

    // Toutes les matières d'un étudiant via son niveau de programme
    @Transactional(readOnly = true)
    @Query("""
        SELECT s FROM Subject s
        JOIN s.semester sem
        JOIN sem.programYearLevel pyl
        WHERE pyl.id = :programYearLevelId
        ORDER BY sem.displayOrder ASC, s.name ASC
    """)
    List<Subject> findByProgramYearLevelId(
            @Param("programYearLevelId") Long programYearLevelId
    );
}
