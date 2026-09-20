package com.badiar.badiar_study.grading.repository;

import com.badiar.badiar_study.grading.entity.ReportCard;
import com.badiar.badiar_study.grading.entity.ReportCard.ReportCardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportCardRepository extends JpaRepository<ReportCard, Long> {

    Optional<ReportCard> findByStudentIdAndSemesterId(Long studentId, Long semesterId);

    List<ReportCard> findByStudentIdAndAcademicYearIdAndReportCardType(
            Long studentId, Long academicYearId, ReportCardType type);

    @Query(value = """
        SELECT DISTINCT rc FROM ReportCard rc
        JOIN FETCH rc.student st
        JOIN FETCH rc.academicYear ay
        LEFT JOIN FETCH rc.semester sem
        WHERE rc.student.id = :studentId
        ORDER BY ay.name DESC
        """,
            countQuery = "SELECT COUNT(rc) FROM ReportCard rc WHERE rc.student.id = :studentId")
    Page<ReportCard> findByStudentId(@Param("studentId") Long studentId, Pageable pageable);

    @Query("""
        SELECT rc FROM ReportCard rc
        JOIN FETCH rc.student st
        JOIN FETCH rc.academicYear ay
        LEFT JOIN FETCH rc.semester sem
        LEFT JOIN FETCH rc.subjectAverages sa
        LEFT JOIN FETCH sa.subject subj
        WHERE rc.id = :id
        """)
    Optional<ReportCard> findByIdWithDetails(@Param("id") Long id);
}