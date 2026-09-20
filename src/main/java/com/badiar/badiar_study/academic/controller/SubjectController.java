package com.badiar.badiar_study.academic.controller;

import com.badiar.badiar_study.academic.dto.request.SubjectRequest;
import com.badiar.badiar_study.academic.dto.response.SubjectResponse;
import com.badiar.badiar_study.academic.service.SubjectService;
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
@RequestMapping("/api/v1/academic/subjects")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<SubjectResponse>>> searchWithFilters(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long programId,
            @RequestParam(required = false) Long semId,
            Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(
                subjectService.searchWithFilters(keyword, programId, semId, pageable)));
    }

    @GetMapping("/by-semester/{semesterId}")
    public ResponseEntity<ApiResponse<List<SubjectResponse>>> getBySemester(@PathVariable Long semesterId) {
        return ResponseEntity.ok(ApiResponse.success(subjectService.getBySemester(semesterId)));
    }

    @GetMapping("/by-program/{programId}")
    public ResponseEntity<ApiResponse<List<SubjectResponse>>> getByProgram(@PathVariable Long programId) {
        return ResponseEntity.ok(ApiResponse.success(subjectService.getByProgram(programId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SubjectResponse>> getSubjectById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(subjectService.getSubjectById(id)));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<ApiResponse<SubjectResponse>> createSubject(
            @Valid @RequestBody SubjectRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(subjectService.createSubject(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<ApiResponse<SubjectResponse>> updateSubject(
            @PathVariable Long id,
            @Valid @RequestBody SubjectRequest request) {
        return ResponseEntity.ok(ApiResponse.success(subjectService.updateSubject(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long id) {
        subjectService.deleteSubject(id);
        return ResponseEntity.noContent().build();
    }
}