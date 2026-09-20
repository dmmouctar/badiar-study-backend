package com.badiar.badiar_study.academic.service;

import com.badiar.badiar_study.academic.dto.request.SubjectRequest;
import com.badiar.badiar_study.academic.dto.response.SubjectResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface SubjectService {
    SubjectResponse createSubject(SubjectRequest request);
    SubjectResponse updateSubject(Long id, SubjectRequest request);
    SubjectResponse getSubjectById(Long id);
    List<SubjectResponse> getBySemester(Long semesterId);
    List<SubjectResponse> getByProgram(Long programId);
    Page<SubjectResponse> searchWithFilters(String keyword, Long programId, Long semId, Pageable pageable);
    void deleteSubject(Long id);
}