package com.badiar.badiar_study.academic.controller;

import com.badiar.badiar_study.academic.dto.request.ExaminationRequest;
import com.badiar.badiar_study.academic.dto.response.ExaminationResponse;
import com.badiar.badiar_study.academic.service.ExaminationService;
import com.badiar.badiar_study.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/academic/examinations")
@RequiredArgsConstructor
public class ExaminationController {

    private final ExaminationService examinationService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<ExaminationResponse>>> searchWithFilters(
            @RequestParam(required = false) Long subjectId,
            @RequestParam(required = false) Long typeId,
            Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(
                examinationService.searchWithFilters(subjectId, typeId, pageable)));
    }

    @GetMapping("/by-subject/{subjectId}")
    public ResponseEntity<ApiResponse<List<ExaminationResponse>>> getBySubject(@PathVariable Long subjectId) {
        return ResponseEntity.ok(ApiResponse.success(examinationService.getBySubject(subjectId)));
    }

    @GetMapping("/by-semester/{semesterId}")
    public ResponseEntity<ApiResponse<List<ExaminationResponse>>> getBySemester(@PathVariable Long semesterId) {
        return ResponseEntity.ok(ApiResponse.success(examinationService.getBySemester(semesterId)));
    }

    @GetMapping("/by-program/{programId}/year/{yearId}")
    public ResponseEntity<ApiResponse<List<ExaminationResponse>>> getByProgramAndYear(
            @PathVariable Long programId,
            @PathVariable Long yearId) {
        return ResponseEntity.ok(ApiResponse.success(
                examinationService.getByProgramAndYear(programId, yearId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ExaminationResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(examinationService.getExaminationById(id)));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<ApiResponse<ExaminationResponse>> create(
            @Valid @RequestBody ExaminationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(examinationService.createExamination(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<ApiResponse<ExaminationResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody ExaminationRequest request) {
        return ResponseEntity.ok(ApiResponse.success(examinationService.updateExamination(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        examinationService.deleteExamination(id);
        return ResponseEntity.noContent().build();
    }
}