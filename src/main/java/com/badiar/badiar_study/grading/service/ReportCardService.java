package com.badiar.badiar_study.grading.service;

import com.badiar.badiar_study.grading.dto.response.ReportCardResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReportCardService {
    ReportCardResponse generateSemesterReportCard(Long studentId, Long semesterId);
    ReportCardResponse generateAnnualReportCard(Long studentId, Long academicYearId);
    ReportCardResponse validateReportCard(Long id, String adminEmail, String notes);
    ReportCardResponse findById(Long id);
    Page<ReportCardResponse> findByStudent(Long studentId, Pageable pageable);
}