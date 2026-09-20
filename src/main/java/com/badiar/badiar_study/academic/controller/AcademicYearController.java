package com.badiar.badiar_study.academic.controller;

import com.badiar.badiar_study.academic.dto.request.AcademicYearRequest;
import com.badiar.badiar_study.academic.dto.response.AcademicYearResponse;
import com.badiar.badiar_study.academic.service.AcademicYearService;
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
@RequestMapping("/api/v1/academic/years")
@RequiredArgsConstructor
public class AcademicYearController {

    private final AcademicYearService yearService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AcademicYearResponse>>> getAllActiveYears() {
        return ResponseEntity.ok(ApiResponse.success(yearService.getAllActiveYears()));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<AcademicYearResponse>>> searchYears(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Boolean isActive,
            Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(yearService.searchYears(keyword, isActive, pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AcademicYearResponse>> getYearById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(yearService.getYearById(id)));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<ApiResponse<AcademicYearResponse>> createYear(
            @Valid @RequestBody AcademicYearRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(yearService.createYear(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<ApiResponse<AcademicYearResponse>> updateYear(
            @PathVariable Long id,
            @Valid @RequestBody AcademicYearRequest request) {
        return ResponseEntity.ok(ApiResponse.success(yearService.updateYear(id, request)));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<ApiResponse<AcademicYearResponse>> toggleStatus(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(yearService.toggleYearStatus(id)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<Void> deleteYear(@PathVariable Long id) {
        yearService.deleteYear(id);
        return ResponseEntity.noContent().build();
    }
}