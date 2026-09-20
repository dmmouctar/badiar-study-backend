package com.badiar.badiar_study.academic.service;

import com.badiar.badiar_study.academic.dto.request.AcademicSemesterRequest;
import com.badiar.badiar_study.academic.dto.response.AcademicSemesterResponse;
import java.util.List;

public interface AcademicSemesterService {
    AcademicSemesterResponse createSemester(AcademicSemesterRequest request);
    AcademicSemesterResponse updateSemester(Long id, AcademicSemesterRequest request);
    AcademicSemesterResponse getSemesterById(Long id);
    List<AcademicSemesterResponse> getByProgramYearLevel(Long programYearLevelId);
    List<AcademicSemesterResponse> getByProgramAndYear(Long programId, Long yearId);
    void deleteSemester(Long id);
}