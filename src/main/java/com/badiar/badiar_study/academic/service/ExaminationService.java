package com.badiar.badiar_study.academic.service;

import com.badiar.badiar_study.academic.dto.request.ExaminationRequest;
import com.badiar.badiar_study.academic.dto.response.ExaminationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ExaminationService {
    ExaminationResponse createExamination(ExaminationRequest request);
    ExaminationResponse updateExamination(Long id, ExaminationRequest request);
    ExaminationResponse getExaminationById(Long id);
    List<ExaminationResponse> getBySubject(Long subjectId);
    List<ExaminationResponse> getBySemester(Long semesterId);
    List<ExaminationResponse> getByProgramAndYear(Long programId, Long yearId);
    Page<ExaminationResponse> searchWithFilters(Long subjectId, Long typeId, Pageable pageable);
    void deleteExamination(Long id);
}