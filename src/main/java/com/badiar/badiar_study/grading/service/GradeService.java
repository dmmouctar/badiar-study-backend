package com.badiar.badiar_study.grading.service;

import com.badiar.badiar_study.grading.dto.request.CreateGradeRequest;
import com.badiar.badiar_study.grading.dto.request.UpdateGradeRequest;
import com.badiar.badiar_study.grading.dto.response.GradeResponse;
import java.util.List;

public interface GradeService {
    GradeResponse saveGrade(CreateGradeRequest request);
    GradeResponse updateGrade(Long id, UpdateGradeRequest request);
    void deleteGrade(Long id);
    GradeResponse findById(Long id);
    List<GradeResponse> findByStudentAndSemester(Long studentId, Long semesterId);
}