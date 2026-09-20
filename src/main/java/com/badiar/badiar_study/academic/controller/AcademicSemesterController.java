package com.badiar.badiar_study.academic.controller;

import com.badiar.badiar_study.academic.dto.request.AcademicSemesterRequest;
import com.badiar.badiar_study.academic.dto.response.AcademicSemesterResponse;
import com.badiar.badiar_study.academic.service.AcademicSemesterService;
import com.badiar.badiar_study.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/academic/semesters")
@RequiredArgsConstructor
public class AcademicSemesterController {

    private final AcademicSemesterService semesterService;

    @GetMapping("/by-level/{programYearLevelId}")
    public ResponseEntity<ApiResponse<List<AcademicSemesterResponse>>> getByProgramYearLevel(
            @PathVariable Long programYearLevelId) {
        return ResponseEntity.ok(ApiResponse.success(
                semesterService.getByProgramYearLevel(programYearLevelId)));
    }

    @GetMapping("/by-program/{programId}/year/{yearId}")
    public ResponseEntity<ApiResponse<List<AcademicSemesterResponse>>> getByProgramAndYear(
            @PathVariable Long programId,
            @PathVariable Long yearId) {
        return ResponseEntity.ok(ApiResponse.success(
                semesterService.getByProgramAndYear(programId, yearId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AcademicSemesterResponse>> getSemesterById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(semesterService.getSemesterById(id)));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<ApiResponse<AcademicSemesterResponse>> createSemester(
            @Valid @RequestBody AcademicSemesterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(semesterService.createSemester(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<ApiResponse<AcademicSemesterResponse>> updateSemester(
            @PathVariable Long id,
            @Valid @RequestBody AcademicSemesterRequest request) {
        return ResponseEntity.ok(ApiResponse.success(semesterService.updateSemester(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<Void> deleteSemester(@PathVariable Long id) {
        semesterService.deleteSemester(id);
        return ResponseEntity.noContent().build();
    }
}