package com.badiar.badiar_study.grading.repository;

import com.badiar.badiar_study.grading.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {

    Optional<Grade> findByStudentIdAndExaminationId(Long studentId, Long examinationId);

    boolean existsByStudentIdAndExaminationId(Long studentId, Long examinationId);

    @Query("""
    SELECT g FROM Grade g
    JOIN FETCH g.examination e
    JOIN FETCH e.subject s
    WHERE g.student.id = :studentId
    AND s.semester.id = :semesterId
    ORDER BY s.id, e.examOrder
    """)
    List<Grade> findAllByStudentIdAndSemesterId(
            @Param("studentId") Long studentId,
            @Param("semesterId") Long semesterId
    );
}