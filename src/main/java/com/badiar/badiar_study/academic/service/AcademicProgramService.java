package com.badiar.badiar_study.academic.service;

import com.badiar.badiar_study.academic.dto.request.AcademicProgramRequest;
import com.badiar.badiar_study.academic.dto.response.AcademicProgramResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface AcademicProgramService {
    AcademicProgramResponse createProgram(AcademicProgramRequest request);
    AcademicProgramResponse updateProgram(Long id, AcademicProgramRequest request);
    AcademicProgramResponse getProgramById(Long id);
    List<AcademicProgramResponse> getAllActivePrograms();
    Page<AcademicProgramResponse> searchPrograms(String keyword, Integer gradingScale, Boolean isActive, Pageable pageable);
    AcademicProgramResponse toggleProgramStatus(Long id);
    void deleteProgram(Long id);
}