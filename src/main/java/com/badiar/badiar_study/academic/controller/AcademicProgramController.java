package com.badiar.badiar_study.academic.controller;

import com.badiar.badiar_study.academic.dto.request.AcademicProgramRequest;
import com.badiar.badiar_study.academic.dto.response.AcademicProgramResponse;
import com.badiar.badiar_study.academic.service.AcademicProgramService;
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
@RequestMapping("/api/v1/academic/programs")
@RequiredArgsConstructor
public class AcademicProgramController {

    private final AcademicProgramService programService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AcademicProgramResponse>>> getAllActivePrograms() {
        return ResponseEntity.ok(ApiResponse.success(programService.getAllActivePrograms()));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<AcademicProgramResponse>>> searchPrograms(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer gradingScale,
            @RequestParam(required = false) Boolean isActive,
            Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(
                programService.searchPrograms(keyword, gradingScale, isActive, pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AcademicProgramResponse>> getProgramById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(programService.getProgramById(id)));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<ApiResponse<AcademicProgramResponse>> createProgram(
            @Valid @RequestBody AcademicProgramRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(programService.createProgram(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<ApiResponse<AcademicProgramResponse>> updateProgram(
            @PathVariable Long id,
            @Valid @RequestBody AcademicProgramRequest request) {
        return ResponseEntity.ok(ApiResponse.success(programService.updateProgram(id, request)));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<ApiResponse<AcademicProgramResponse>> toggleStatus(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(programService.toggleProgramStatus(id)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<Void> deleteProgram(@PathVariable Long id) {
        programService.deleteProgram(id);
        return ResponseEntity.noContent().build();
    }
}