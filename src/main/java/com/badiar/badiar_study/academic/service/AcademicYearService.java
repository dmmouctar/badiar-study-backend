package com.badiar.badiar_study.academic.service;

import com.badiar.badiar_study.academic.dto.request.AcademicYearRequest;
import com.badiar.badiar_study.academic.dto.response.AcademicYearResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface AcademicYearService {
    AcademicYearResponse createYear(AcademicYearRequest request);
    AcademicYearResponse updateYear(Long id, AcademicYearRequest request);
    AcademicYearResponse getYearById(Long id);
    List<AcademicYearResponse> getAllActiveYears();
    Page<AcademicYearResponse> searchYears(String keyword, Boolean isActive, Pageable pageable);
    AcademicYearResponse toggleYearStatus(Long id);
    void deleteYear(Long id);
}