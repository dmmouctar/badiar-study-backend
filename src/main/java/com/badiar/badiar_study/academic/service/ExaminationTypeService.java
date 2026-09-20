package com.badiar.badiar_study.academic.service;

import com.badiar.badiar_study.academic.dto.request.ExaminationTypeRequest;
import com.badiar.badiar_study.academic.dto.response.ExaminationTypeResponse;
import java.util.List;

public interface ExaminationTypeService {
    ExaminationTypeResponse createExaminationType(ExaminationTypeRequest request);
    ExaminationTypeResponse updateExaminationType(Long id, ExaminationTypeRequest request);
    ExaminationTypeResponse getExaminationTypeById(Long id);
    List<ExaminationTypeResponse> getAllExaminationTypes();
    void deleteExaminationType(Long id);
}