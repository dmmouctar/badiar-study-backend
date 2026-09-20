package com.badiar.badiar_study.grading.repository;

import com.badiar.badiar_study.grading.entity.ReportCardValidation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReportCardValidationRepository extends JpaRepository<ReportCardValidation, Long> {
    Optional<ReportCardValidation> findByReportCardId(Long reportCardId);
}